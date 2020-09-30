/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.readableReports;

import java.util.HashMap;
import java.util.Iterator;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Execution.info.aux.mutantClassHashInfo;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;

/**
 *
 * @author user
 */
public class generalReport implements reportTemplate{
    
    HashMap<String, mutantClassHashInfo> mutantMap;
    private final String REPORT_NAME = "generalReport.txt";
  
    
    public generalReport()
    {
        mutantMap = new HashMap<String, mutantClassHashInfo>();
    }
    
    @Override
    public String genReport(MutantsExecutionInfo mInfo) 
    {
       String strRet, strClass;
       mutantInfo mInfoIndex;
       mutantClassHashInfo hashCollection;
       
       strRet = "";
       for(int i = 0;i<mInfo.getSize();i++)
       {
           mInfoIndex = mInfo.getMutant(i);
           if(mInfoIndex != null)
           {
               //If the mutant has no associated an specific lass, set tbe standard class.
               if(mInfoIndex.getMutationClass() == null)
                mInfoIndex.setOperatorClass(EnumOperatorClass.eGENERALCLASS);
               
               strClass = mInfoIndex.getMutationClass().toString();
           
               if(mutantMap.containsKey(strClass))
               {
                   hashCollection = mutantMap.get(strClass);
               }
               else
               {
                   //Create the subclass hash
                   hashCollection = new mutantClassHashInfo(strClass);
                   mutantMap.put(strClass, hashCollection);
               }
               hashCollection.insertElement(mInfoIndex);
           }
           else
           {
               strClass="";
           }
       }
       
       //Call classes for generating report
       strRet = reportToString(mInfo);
       
       return strRet;
    }
    
    public String reportToString(MutantsExecutionInfo mInfo)
    {
        String strRet, strGeneralReport;
        StringBuffer buffer;
        mutantClassHashInfo mIndex;
        buffer= new StringBuffer();
        mInfo.getDeadMutants();
        strGeneralReport = String.format("General data: %d / %d = %f", mInfo.getDeadMutants(), 
                mInfo.getTotalMutants(),mInfo.getMS());
        
        buffer.append(strGeneralReport+"\n");

        Iterator<String> keySetIterator = mutantMap.keySet().iterator();
        while(keySetIterator.hasNext()){
          String key = keySetIterator.next();
          mIndex = mutantMap.get(key);
          buffer.append(mIndex.toStringClass()+"\n");
        }

        strRet = buffer.toString();
        System.out.print(strRet);
        return strRet;
    }
    
    public String getReportName() {
       return REPORT_NAME;
    }
}
