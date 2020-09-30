/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;



/**
 *
 * @author Pablo C. Cañizares 
 */
public class StatsPersistence {
    
    public static final String STATS_MAIN_FOLDER = "stats";
    public static final String MUTANTS_FOLDER = "mutants";
    public static final String ORIGINAL_RES_NAME = "originalResults";
    public static final String ORIGINAL_TIMES_NAME = "originalTimeResults";
    
    String m_strInstanceName;
    File m_instanceFolder;
    File m_mutantFolder;
    File m_originalResFile;
    
    TestExecutionInfo m_originalRes;
    
    public StatsPersistence(String strInstanceName)
    {
        this.m_strInstanceName = strInstanceName;
        createStatsFolder();
    }
    public StatsPersistence()
    {

    }
    
    void createStatsFolder() {        

        File mainFolder;
        
        System.out.printf("createStatsFolder - Init\n");
        mainFolder = new File(STATS_MAIN_FOLDER);
        
        if(m_strInstanceName != null)
        {


            if (!mainFolder.exists()) 
                mainFolder.mkdir();

            // Check if mutant folder exists
            m_instanceFolder = new File(STATS_MAIN_FOLDER + File.separatorChar + m_strInstanceName);

            // Remove mutants previously created
            if (!m_instanceFolder.exists()) {
                m_instanceFolder.mkdir();
            }       
            m_mutantFolder = new File(STATS_MAIN_FOLDER + File.separatorChar + m_strInstanceName+File.separatorChar + MUTANTS_FOLDER);
            if (!m_mutantFolder.exists()) {
                m_mutantFolder.mkdir();
            }   

            m_originalResFile = new File(m_instanceFolder.getAbsolutePath()+File.separatorChar+ORIGINAL_RES_NAME+".csv");
            System.out.printf("createStatsFolder - End\n");
        }
    }
   
    public void saveMutant(mutantInfo mutant) {
        
        String strMutant, strMutantName;
        File fileMutant;
        if(mutant!= null)
        {
            strMutant = mutant.toStringPlain();
            strMutantName = mutant.getName()+".txt";
            fileMutant = new File(m_mutantFolder.getAbsolutePath()+File.separatorChar+strMutantName);
            
            if(fileMutant.exists())
                fileMutant.delete();
            
            saveContentToDisk(fileMutant.getAbsolutePath(), strMutant, false);
        }
    }
    //TODO: Sacar estos métodos de salvar a disco a una clase genérica persistence
    public void saveResultsToDisk(String fileName, LinkedList<String>  dataList, boolean bPrint){
        PrintWriter pw;
        String strLine;
        int nSize;
        boolean bRet;
        
        nSize= dataList.size();
        bRet = true;
        if(nSize>0)
        {
            try 
            {
                pw = new PrintWriter(new FileOutputStream(fileName));

                for (int i=0;i<nSize;i++)
                {
                    strLine = dataList.get(i);
                    pw.println(strLine);
                    
                    if(bPrint)
                        System.out.printf(strLine);
                }
               
                pw.close();
            } catch (FileNotFoundException ex) {
                bRet = false;
            }
        }
    }
    
    public boolean saveContentToDisk(String fileName, String strContent, boolean bPrint){
        PrintWriter pw;
        String strLine;
        int nSize;
        boolean bRet;
        
        nSize= strContent.length();
        bRet = true;
        if(nSize>0)
        {
            try 
            {
                pw = new PrintWriter(new FileOutputStream(fileName));
                pw.println(strContent);

                if(bPrint)
                    System.out.println(strContent);
               
                pw.close();
            } catch (FileNotFoundException ex) {
                bRet = false;
            }
        }
        return bRet;
    }      
    

    public void saveMutationProcessCSV(MutantsExecutionInfo mutantsInfo) {
        int nSize;
        String strLine, strName, strDate, strFileName;
        mutantInfo mInfo;
        nSize = mutantsInfo.getSize();
        LinkedList<String>  csvList;
        
        if(nSize >0)
        {
            csvList = new LinkedList<String>();
            for(int i=0;i<nSize;i++)
            {
                mInfo = mutantsInfo.getMutant(i);
                
                if(mInfo != null)
                {
                    strLine = mInfo.toStringPlain();
                    csvList.add(strLine);
                }

            }
            strDate = getDateString();
            strName = String.format("stats_%s_%s.csv",m_strInstanceName,strDate);
            strFileName = m_instanceFolder.getAbsolutePath()+File.separator+strName;
            saveResultsToDisk(strName, csvList, true);
        }
    }
    
    public String getDateString()
    {
        String strDate;
        Calendar now = Calendar.getInstance();
        int year, month, day, hour, minute, second;
        
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH); // Note: zero based!
        day = now.get(Calendar.DAY_OF_MONTH);
        hour = now.get(Calendar.HOUR_OF_DAY);
        minute = now.get(Calendar.MINUTE);
        second = now.get(Calendar.SECOND);
         
        strDate = String.format("%d_%02d_%02d__%02d:%02d:%02d", year, month + 1, day, hour, minute, second);
        return strDate;
    }

    public void saveOriginalRes(TestExecutionInfo m_testOriginalInfo) {
        
        String strRes;
        
        strRes = m_testOriginalInfo.toStringCsv();
        saveContentToDisk(m_instanceFolder.getAbsolutePath()+File.separatorChar+ORIGINAL_RES_NAME+".csv",strRes,false);
        
    }
    public void saveOriginalTimeRes(TestExecutionInfo m_testOriginalInfo) {
        
        String strRes;
        
        strRes = m_testOriginalInfo.toStringTimeCsv();
        saveContentToDisk(m_instanceFolder.getAbsolutePath()+File.separatorChar+ORIGINAL_TIMES_NAME+".csv",strRes,false);
        
    }    
    public boolean existsOriginalRes()
    {
        boolean bRet = false;
        
        if(m_originalResFile != null)
            bRet= m_originalResFile.exists();
        
        return bRet;
    }
    public boolean loadOriginalRes()
    {
        boolean bRet = false;
        double dTime;
        EnumMutantState eStat;
        
        System.out.printf("loadOriginalRes - Init\n");
        
        LinkedList<LinkedList<String>> dataList;        
        if(existsOriginalRes())
        {
            //Load original results in order to avoiding the com
            dataList = loadDBExt(m_instanceFolder.getAbsolutePath()+File.separatorChar+ORIGINAL_RES_NAME+".csv");
            
            if(dataList!=null)
            {
                m_originalRes = new TestExecutionInfo(dataList.size());
                for(int i=0;i<dataList.size();i++)
                {
                    LinkedList<String> lineList = dataList.get(i);
                    dTime = Double.valueOf(lineList.get(1));                    
                    m_originalRes.addTest(i, lineList.get(2), dTime,EnumMutantState.eMUTANT_ALIVE);
                }
                bRet =true;
            }
        }
        System.out.printf("loadOriginalRes - End\n");        
        return bRet;
    }
   
    private LinkedList<LinkedList<String>> loadDBExt(String strDB)
    {              
        BufferedReader br = null;
        LinkedList<LinkedList<String>> retList = null;
        LinkedList<String> lineList = null;
        
        System.out.printf("loadDBExt\n");
        
        try {
            String line;
            String[] toks;
        
            //CSVParser parser=new CSVParser();            
            int nIndex;
            String strName;
            boolean first=true;
            
            System.out.printf("Opening: %s\n", strDB);
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

                        toks=line.split(",");//parser.parseLine(line);
                        lineList = new LinkedList<String>(Arrays.asList(toks));

                    nIndex++;
                    retList.add(lineList);
                }   
            }else
            {
                System.out.printf("Original tests does not exists");
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(StatsPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       finally {
            try {
                br.close();
            } catch (IOException ex) {              
            }
        }
        
        return retList;
    }

    public TestExecutionInfo getOriginalResults() {
        return m_originalRes;
    }
}
