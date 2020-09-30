/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.readableReports;

import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class generalExtendedReport implements reportTemplate{

    private final String REPORT_NAME = "generalExtendedReport.txt";
    
    public String genReport(MutantsExecutionInfo mInfo) {
        String strRet, strMut;
        StringBuffer strTestExecution;
        mutantInfo mutantInfo;
        
        strRet="";
        if(mInfo != null)
        {
            strTestExecution = new StringBuffer("");
            
            for(int i=0;i<mInfo.getSize();i++)
            {                
                mutantInfo= mInfo.getMutant(i);     
                if(mutantInfo != null)
                {
                    strMut = mutantInfo.ToStringReport();
                    strTestExecution.append(strMut);
                }                
            }
            strRet = strTestExecution.toString();
        }
        
        return strRet;
    }

    public String getReportName() {
        return REPORT_NAME;
    }
    
}
