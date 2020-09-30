/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator;

import mutomvo.Mutation.ReportGenerator.graphs.graphTemplate;
import java.io.File;
import mutomvo.Mutation.Execution.auxiliars.CommandExec;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.ReportGenerator.graphs.graphics.graphKillmap2d;
import mutomvo.Mutation.ReportGenerator.graphs.graphics.graphKillmap3d;

/**
 *
 * @author user
 */
public class graphGenerator {

    reportGenerator reporter;
    Persistence m_oSaver;
    public graphGenerator() {
        
        m_oSaver= new Persistence();
    }

    public boolean generateGraph(reportGenerator reporter) {
        
        boolean bRet =false;
        MutantsExecutionInfo exeInfo;
        String strReport;
        File fLocation;
        graphTemplate grapGen, grapGen3d;
        CommandExec com;
        this.reporter = reporter;
        if(reporter != null)
        {
            com = new CommandExec();
            exeInfo = reporter.getExecutionInfo();
            
            try
            {
                if(exeInfo != null)
                {                
                    grapGen = new graphKillmap2d();
                    strReport = grapGen.genGraph(exeInfo);
                    fLocation = reporter.getLocation();
                    if(fLocation != null)
                        m_oSaver.saveContentToDisk(fLocation.getAbsolutePath()+File.separatorChar+grapGen.getGraphName(), strReport, false);

                    //ejecutamos comando para generar el bicho
                    com.execCommand("cd "+fLocation.getAbsolutePath()+File.separatorChar+" && gnuplot "+grapGen.getGraphName());

                    grapGen3d = new graphKillmap3d();
                    strReport = grapGen3d.genGraph(exeInfo);
                    fLocation = reporter.getLocation();
                    if(fLocation != null)
                        m_oSaver.saveContentToDisk(fLocation.getAbsolutePath()+File.separatorChar+grapGen3d.getGraphName(), strReport, false);

                    //ejecutamos comando para generar el bicho
                    com.execCommand("cd "+fLocation.getAbsolutePath()+File.separatorChar+" && gnuplot "+grapGen3d.getGraphName());                            
                }
            }catch(NullPointerException np)
            {
                System.out.println("EXCEPTION CACHED! This exception must be analysed");
            }
        }            
        return bRet;
    }    
}
