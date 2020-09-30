/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.readableReports;

import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.ReportGenerator.reports.auxiliar.GroupedMutExecInfo;
import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class AvgTimeAndKillTableReport implements reportTemplate{

    GroupedMutExecInfo infoGroup;
    private final String REPORT_NAME = "AvgTimeAndKill.tex";
    
    @Override
    public String genReport(MutantsExecutionInfo mInfo) {
        String strRet;
        mutantInfo mutantInfo;
        int nKillerTest;
        int nKillers;
        int nTotalTests;
        Long nTimeToKill;
        
        infoGroup =  new GroupedMutExecInfo();
        strRet="";
        if(mInfo != null)
        {

            for(int i=0;i<mInfo.getSize();i++)
            {          
                nKillerTest=-1;
                nKillers=0;
                nTimeToKill=(long)0;
                mutantInfo= mInfo.getMutant(i);   
                
                //TODO: Incluir esta informacion en el parseo original, que no es tan dificil.
                //Preguntar si los vivos se suman tambien
                if(mutantInfo != null && !mutantInfo.isAlive())
                {
                    nTotalTests = mutantInfo.getTotalTests();
                   for(int j=0;j<mutantInfo.getTotalTests();j++)
                   {
                       //If the mutant is alive, add the execution time.
                       if(nKillerTest==-1)
                       {
                           nTimeToKill = nTimeToKill + (long)mutantInfo.getTestTime(j);
                       }
                       if(!mutantInfo.isAlive(j))
                       {
                           if(nKillerTest==-1)
                           {
                               nKillerTest=j;                               
                           }
                           nKillers++;
                       }                       
                   }
                   //TEmporal printf!
                   infoGroup.addInfo(mutantInfo.getMutationClass(), mutantInfo.getMutationOperator(), nTotalTests, nKillerTest, nKillers, nTimeToKill);
                }                
            }
            infoGroup.processEntries();
            strRet = infoGroup.toString();
        }
        return strRet;
    }

    @Override
    public String getReportName() {
        return REPORT_NAME;
    }
    
}
