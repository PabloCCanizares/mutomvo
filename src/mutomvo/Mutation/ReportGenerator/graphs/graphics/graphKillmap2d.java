/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.graphs.graphics;

import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.ReportGenerator.graphs.graphTemplate;

/**
 *
 * @author user
 */
public class graphKillmap2d implements graphTemplate
{
    private final String GRAPH_NAME = "graphKillmap2d.gnu";
    public String genGraph(MutantsExecutionInfo mInfo) {
        String strRet;
        StringBuffer buffer;
        double dMean;
                
        buffer = new StringBuffer();
        dMean = calcMean(mInfo)*2;
        
        buffer.append("set pm3d map\n");
        buffer.append("set terminal svg size 800,800\n");
        buffer.append("set output '2d-kill.svg'\n");
        
        buffer.append("set palette model RGB");
        buffer.append("set palette model RGB defined (0 \"green\", 2 \"dark-red\" )");
      
        buffer.append("splot 'heatmap_dataset.txt' matrix\n");

        strRet = buffer.toString();
        
        return strRet;
    }

    public String getGraphName() {
        return GRAPH_NAME;
    }
    double calcMean(MutantsExecutionInfo mInfo)
    {
        double dMean;
        int nTotalMutants;       
        long accExec;
        
        accExec=0;
        dMean=0.0;
        nTotalMutants = 0;
        for(int i = 0;i<mInfo.getSize();i++)
        {
            mutantInfo mInfoIndex = mInfo.getMutant(i);
            
            accExec =  (long) mInfoIndex.getTestsAverage();
            if(accExec > dMean)
                dMean = accExec;
            nTotalMutants++;
        }  
        //if(nTotalMutants > 0 && accExec>0)
        //    dMean = accExec/nTotalMutants;
        
        return dMean;
    }
    
        
}
