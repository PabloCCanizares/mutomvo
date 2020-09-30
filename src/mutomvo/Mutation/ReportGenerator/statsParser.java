/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator;

import com.opencsv.CSVParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;
import mutomvo.Mutation.Execution.info.tests.testInfo;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class statsParser {
    
    public static final String MAIN_FOLDER = "stats";
    public static final String MUTANTS_FOLDER = "mutants";
    public static final String ORIGINAL_RES_NAME = "originalResults";
    public static final String MUTANT_PARSER_TOKEN ="mutants/mutant_";
    protected MutantsExecutionInfo mExecInfoList;
    
    public statsParser()
    {
    
    }
    
    public boolean doParse(String strApplication)
    {
        boolean bRet;
        String strLocation;
        bRet=false;
        
        if(!strApplication.isEmpty())
        {
        
            strLocation = MAIN_FOLDER+File.separatorChar+strApplication+File.separatorChar+MUTANTS_FOLDER;
            
            //Getting all the elements of a folder...        
            File folder = new File(strLocation);

            if(folder.exists())
            {
                File[] listOfFiles = folder.listFiles();
                LinkedList<mutantInfo> mutantList;
                
                mExecInfoList = new MutantsExecutionInfo();
                for (File file : listOfFiles) {
                    if (file.isFile()) {

                        System.out.println(file.getName());
                        mutantList = parseMutantSingleFile(strLocation+File.separatorChar+file.getName());

                        if(mutantList!= null)
                        {
                            for(int i=0;i<mutantList.size();i++)
                            {
                                mExecInfoList.add(mutantList.get(i));
                            }
                        }
                    }
                }
            }

        
        }
        if(mExecInfoList != null && mExecInfoList.getSize()>0)
            bRet = true;

        return bRet;
    }
    public LinkedList<mutantInfo> parseMutantSingleFile(String strFile)
    {
        LinkedList<LinkedList<String>> resultCsvList;
        LinkedList<mutantInfo> mInfoList;
        TestExecutionInfo testInfoList;
        int nMutantState, nLine, nTestsNumber, i,j, nMutantIndex;
        String strMutantInfo, strResult;
        mutantInfo mInfo;
        testInfo testInfo;
        double dTime;
        EnumMutantState eStat, eStatMutant;
        
        mInfo=null;
        mInfoList = null;
        testInfoList=null;
        resultCsvList = loadCsv(strFile);
        nMutantIndex = 0;
        
        
        if(resultCsvList != null)
        {      
            nMutantIndex = getMutantFromFileName(strFile);
            mInfoList = new LinkedList<mutantInfo>();
            for(i=0;i<resultCsvList.size();i++)
            {
                LinkedList<String> lineList = resultCsvList.get(i);
                
                //Example: 0,100, //Line: 238 | Token: mpi_barrier(); | Class: eMPICLASS | Op: eOMOVDOWN,
                //          MutantNumber, Number of tests, MutantInfo
                
                //First Line, contains the mutant header
                nMutantState = Integer.parseInt(lineList.get(0));
                nTestsNumber = Integer.parseInt(lineList.get(1));
                strMutantInfo = lineList.get(2);
                //TODO: Hay que arreglar esto: las ',' del token nos joden.
                if(lineList.size()>3)
                {
                    for(int nIn=3;nIn<lineList.size();nIn++)
                    {
                        strMutantInfo = strMutantInfo + lineList.get(nIn);
                    }
                }
                if(nMutantState == 0)
                    eStatMutant = EnumMutantState.eMUTANT_DEAD;
                else
                    eStatMutant = EnumMutantState.eMUTANT_ALIVE;
                
                //Parse mutant info
                testInfoList = new TestExecutionInfo(nTestsNumber);
                //Insert the tests
                for(j=i+1;j<(i+1+nTestsNumber);j++)
                {
                    lineList = resultCsvList.get(j);
                    dTime = Double.valueOf(lineList.get(1));   
                    strResult = lineList.get(3);
                    if(strResult.isEmpty())
                    {
                        strResult = lineList.get(2);
                    }
                    //TODO: Ojo con las compatibilidades de formato ...
                    if(strResult.equals("Dead"))
                        eStat = EnumMutantState.eMUTANT_DEAD;
                    else
                        eStat = EnumMutantState.eMUTANT_ALIVE;
                    
                    testInfoList.addTest(j-i, lineList.get(2), dTime,eStat);                    
                }
                i=j-1;
                
                //Insert the mutant to list
                mInfo = new mutantInfo(nMutantIndex, nTestsNumber, testInfoList, eStatMutant, strMutantInfo);                
                mInfoList.add(mInfo);
                nMutantIndex++;
            }            
        }
        
        return mInfoList; 
              
    }
    LinkedList<LinkedList<String>> loadCsv(String strDB)
    {
        
        BufferedReader br = null;
        LinkedList<LinkedList<String>> retList = null;
        LinkedList<String> lineList = null;
        try {
            String line;
            String[] toks;
            CSVParser parser=new CSVParser();            
            int nIndex;
            String strName;
            boolean first=true;
            
            File file = new File(strDB);
            if(file.exists())
            {            
                br = new BufferedReader(new FileReader(file));
                nIndex = 0;
                retList = new LinkedList<LinkedList<String>>();

                while ((line=br.readLine()) != null) 
                {
                    //line=line.trim();
                    if (line.length() <= 0)     continue;

                        toks=parser.parseLine(line);
                        lineList = new LinkedList<String>(Arrays.asList(toks));

                    nIndex++;
                    retList.add(lineList);
                }              
            }
            else
                System.out.printf("loadCsv - The file does not exists %s", strDB);
        } 
        catch (IOException ex) {            
        }
        
       finally {
            try {
                if(br  != null)
                    br.close();
            } catch (IOException ex) {              
            }
        }
        
        return retList;
    }

    private int getMutantFromFileName(String strFile) {
        
        int nFind,nMutantNumber;
        String number;
        
        nMutantNumber = 0;
        nFind = strFile.indexOf(MUTANT_PARSER_TOKEN);
        if(nFind != -1)
        {
            nFind += MUTANT_PARSER_TOKEN.length();
            number = strFile.substring(nFind);
            number = number.replace(".txt", "");
            try
            {
                nMutantNumber = Integer.parseInt(number);
            }catch(java.lang.NumberFormatException e)
            {}
        }
        return nMutantNumber;
    }
    
}
