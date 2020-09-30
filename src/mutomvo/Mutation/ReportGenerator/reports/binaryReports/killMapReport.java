/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.binaryReports;

import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Execution.info.aux.mutantClassHashInfo;
import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;

/**
 *
 * @author user
 */
public class killMapReport implements reportTemplate
{
    private final String REPORT_NAME = "killmap_dataset.txt";
    
    public killMapReport()
    {
        
    }
    public String genReport(MutantsExecutionInfo mInfo) 
    {
        StringBuffer buffer;
        String strRet, strTestLine;
        mutantInfo mInfoIndex;
        mutantClassHashInfo hashIndex;

        strRet = "";
        buffer = new StringBuffer();
        for(int i = 0;i<mInfo.getSize();i++)
        {
           mInfoIndex = mInfo.getMutant(i);
           
           if(mInfoIndex != null)
           {
                for(int j=0;j<mInfoIndex.getTotalTests();j++)
                {
                    if(mInfoIndex.isAlive(j))
                     strTestLine = "1 ";
                    else
                     strTestLine = "0 ";   
                    buffer.append(strTestLine);
                }
           }
           buffer.append("\n");
        }

        strRet = buffer.toString();
        
        return strRet;
     }

    public String getReportName() {
       return REPORT_NAME;
    }
}
