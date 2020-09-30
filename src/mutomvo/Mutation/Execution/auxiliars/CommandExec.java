/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.auxiliars;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import javax.security.auth.login.Configuration;

/**
 *
 * @author user
 */
public class CommandExec {
    private ProcessBuilder m_builder;
    private Process m_process;
    private BufferedReader m_br;
    
    
    public String execCommand(String strCommand) {

        String line;
        String simcanPath, omnetPath;
        
        
        File workingDir;
        InputStream is;
        InputStreamReader isr;
        int i, portNumber;
        Configuration configFile;
        String strResult;
        Map<String, String> env = null;

        strResult = "";
        try {

            m_builder = new ProcessBuilder("bash", "-c", strCommand);
            //workingDir = new File(m_strSimulationDir);
            //omnetPath = m_strSimcan_Home.substring(0, m_strSimcan_Home.lastIndexOf(File.separator));
            
            //builder.redirectErrorStream(true);
            //m_builder.directory(workingDir);
            // Sets the needed libraries for the execution
            //env = m_builder.environment();
            
            // Configure environment variables
            //env.put("LD_LIBRARY_PATH", env.get("LD_LIBRARY_PATH") + File.pathSeparator + omnetPath + "/lib");
            //env.put("PATH", env.get("PATH") + File.pathSeparator + omnetPath + "/bin");
            //env.put("TCL_LIBRARY", "/usr/share/tcltk/tcl8.4");

            // Sets the needed libraries for the execution
            env = m_builder.environment();
            //Launch the simulation
            m_process = m_builder.start();

            //Read the result of the simulation
            is = m_process.getInputStream();
            isr = new InputStreamReader(is);
            m_br = new BufferedReader(isr);

            // Manage the output
            while ((line = m_br.readLine()) != null) {
                //System.out.println(line);
                strResult = strResult.concat(line);
            }
            m_br.close();
        } catch (IOException ex) {            
        }

        return strResult;
    }
    
}
