/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports;

import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;

/**
 *
 * @author Pablo C. Cañizares 
 */
public interface reportTemplate {
    
    String genReport(MutantsExecutionInfo mInfo);

    public String getReportName();
}
