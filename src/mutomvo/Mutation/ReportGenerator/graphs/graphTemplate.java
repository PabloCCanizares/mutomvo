/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.graphs;

import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;

/**
 *
 * @author user
 */
public interface graphTemplate {
    
    String genGraph(MutantsExecutionInfo mInfo);

    public String getGraphName();
}
