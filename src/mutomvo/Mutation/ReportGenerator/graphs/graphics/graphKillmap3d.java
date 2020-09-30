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
public class graphKillmap3d implements graphTemplate {

    private final String GRAPH_NAME = "graphKillmap3d.gnu";
    
    public graphKillmap3d() {
    }

    public String genGraph(MutantsExecutionInfo mInfo) {
        String strRet;
        StringBuffer buffer;
        double dMean;
                
        buffer = new StringBuffer();
        dMean = calcMean(mInfo)*2;

        buffer.append("set terminal svg size 800,800\n");
        buffer.append("set output '3d-polar.svg'\n");
        //buffer.append("set palette defined (0 0 0 0.5, 1 0 0 1, 2 0 0.5 1, 3 0 1 1, 4 0.5 1 0.5, 5 1 1 0, 6 1 0.5 0, 7 1 0 0, 8 0.5 0 0, 9 0 0 0, 10 0 0 0)\n");
        buffer.append("set border 4095 front linetype -1 linewidth 1.000\n");
        buffer.append("set samples 25, 25\n");
        buffer.append("set isosamples 40, 40\n");
        buffer.append("set title \"Reservoir Title\"\n");
        buffer.append("set xlabel \"Test number\"\n");
        buffer.append("set xrange [ 0.0000 : "+Integer.toString(mInfo.getTotalTests()) +" ] noreverse nowriteback\n");
        buffer.append("set ylabel \"Mutant number\"\n");
        buffer.append("set yrange [ 0.0000 : "+Integer.toString(mInfo.getTotalMutants()) +" ] noreverse nowriteback\n");
        buffer.append("set zrange [ 0.00000 : "+Double.toString(dMean) +" ] noreverse nowriteback\n");
        buffer.append("set pm3d implicit at s\n");
        buffer.append("set pm3d interpolate 0,0\n");
        buffer.append("splot 'heatmap_dataset.txt' matrix with points pointtype 0\n");
       

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
