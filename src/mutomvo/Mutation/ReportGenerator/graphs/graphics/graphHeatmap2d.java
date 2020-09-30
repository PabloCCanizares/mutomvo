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
public class graphHeatmap2d implements graphTemplate
{
    private final String GRAPH_NAME = "graphHeatmap2d.gnu";
    public String genGraph(MutantsExecutionInfo mInfo) {
        String strRet;
        StringBuffer buffer;
        double dMean;
                
        buffer = new StringBuffer();
        dMean = calcMean(mInfo)*2;
        
        buffer.append("set pm3d map\n");
        buffer.append("set terminal svg size 800,800\n");
        buffer.append("set output '2d-polar.svg'\n");
        buffer.append("set palette defined (0 0 0 0.5, 1 0 0 1, 2 0 0.5 1, 3 0 1 1, 4 0.5 1 0.5, 5 1 1 0, 6 1 0.5 0, 7 1 0 0, 8 0.5 0 0, 9 0 0 0, 10 0 0 0)\n");
        
        buffer.append("set cbrange [ 0.00000 : "+Double.toString(dMean) +" ] noreverse nowriteback\n");
        //buffer.append("#set zrange [ 0.00000 : "+Double.toString(dMean) +" ] noreverse nowriteback\n");
        buffer.append("set pm3d interpolate 0,0\n");
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
