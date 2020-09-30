/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution;

import mutomvo.Mutation.Execution.auxiliars.EnumMutexecState;
import mutomvo.Mutation.Execution.auxiliars.IMuTeXec;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;
import mutomvo.TabbedPanels.ExecutionPanelBasic;


/**
 *
 * @author Pablo C. Cañizares 
 */
public class SimulationeXec implements IMuTeXec, Runnable {

    private final String SIMCAN_APPS_PATH = "/src/Applications/Apps/";
    
    ExecutionPanelBasic exePanel;

    double m_dInit;
    double m_dCompilationTime;
    double m_dTotalTime;
    
   // int m_nTotalTests;
    
    String m_strScenarioName;
    String m_strSimulationDir;
    String m_strApplication;
    String m_strSimcan_Home;
    String m_strLastError;
    
    boolean m_bClean;
    boolean m_bStopWhenFail;
    boolean m_bRunning;
    
    EnumMutexecState m_eState;
    ProcessBuilder m_builder;
    TestExecutionInfo m_testInfo;
    MutantsExecutionInfo m_mutantsInfo;
    
    private final String MARKER_TOKEN = "#___#";
    JFrame m_parent;
    boolean m_bModal;
    
    public SimulationeXec(JFrame parent, boolean bModal) {
        m_parent=parent;
        m_bModal = bModal;
        
        if(parent != null)
            exePanel = new ExecutionPanelBasic(m_parent, m_bModal, this);
        else
            exePanel = null;
        
        m_bRunning = false;
        
    }

    public boolean Abort() {
        boolean bRet = false;
                        
        changeState(EnumMutexecState.eMUT_EXE_FIN_ABORT);
        m_bRunning=false;
        eprintf("Aborted!");
        return bRet;
    }

    public double getTick() {
        return System.currentTimeMillis();
    }

    public boolean fullExec() {
        boolean bRet = false;
        int nMutantState;
        EnumMutantState mutantState;

        if (checkArguments()) {
            
            //Initialize(m_nMutants);
            m_dInit = getTick();
            m_bRunning= true;
            
            if (compile(m_bClean)) {

                m_dCompilationTime = getTick() - m_dInit;
                if (execOriginalProgram(m_strSimulationDir)) {

                    printResults(m_dCompilationTime, m_dTotalTime);

                    bRet = true;
                } else {
                    if(m_eState != EnumMutexecState.eMUT_EXE_FIN_ABORT)
                        setError("Error executing original program, please fix the errors");
                }
            } else {
                if(m_eState != EnumMutexecState.eMUT_EXE_FIN_ABORT)
                    setError("Error compiling!");
            }
        } else {
            setError("Error parsing arguments");
        }

        if(exePanel != null)
            exePanel.endOfProcess();
        if (bRet) {
            changeState(EnumMutexecState.eMUT_EXE_FIN_OK);
        } else {
            changeState(EnumMutexecState.eMUT_EXE_FIN_KO);
        }

        return bRet;
    }
    //compilar

    private boolean checkArguments() {
        boolean bRet = false;
        String strParametersNeeded;

        strParametersNeeded = "";
        //Obtenemos el scenario dir



        if (m_strSimulationDir.isEmpty()) {
            strParametersNeeded += "simDir| ";
        }

        if (strParametersNeeded.isEmpty()) {
            bRet = true;
        } else {
            eprintf("Parameters not found: " + strParametersNeeded + "\n");
        }

        return bRet;
    }

    private boolean compile(boolean m_bClean) {

        boolean bRet;
        String strResult;

        bRet = false;
        changeState(EnumMutexecState.eMUT_COMPILING);
        eprintf("\nStarting compilation!");
        eprintf("Environment value: simcan_home = " + m_strSimcan_Home);


        if (m_bClean) {
            clean();
        }

        if (!m_strSimcan_Home.isEmpty()) {
            //Redireccionamos la salida estandar a null, y sólo mostramos los errores
            strResult = execCommand("make -C " + m_strSimcan_Home + " 2>&1 >/dev/null\n");

            if (!strResult.isEmpty() && strResult.indexOf("Error") != -1) {
                eprintf("Compilation err: " + strResult);
            } else {
                eprintf("Compilation done!");
                bRet = true;
            }
        } else {
            eprintf("ERROR!! Insert environment variable $SIMCAN_HOME;");
        }

        eprintf("Compilation End!");

        return bRet;
    }

    private void Initialize(int m_nMutants) {

        m_nMutants = 0;
        m_dInit = m_dCompilationTime = m_dTotalTime = 0;

        m_strSimulationDir = m_strApplication = "";
        m_bClean = m_bStopWhenFail = false;
    }

    private boolean execOriginalProgram(String strSimulationDir) {

        boolean bRet = true;
        int nIndexTest, nValue;
        String strResult, strIndexTest;
        double dExecTime, dTick, dProgress;

        eprintf("\nExecuting original program...");
        changeState(EnumMutexecState.eMUT_EXE_ORIG);

        nIndexTest = 0;
        dExecTime = 0.0;
        
        dTick = getTick();        
        //strResult = execCommand("cd %s \n sh %srun -f omnetppTest.ini -u Cmdenv -c test_%04d | grep '%s'", 1, dir, dir, nIndexTest, MARKER_TOKEN);
        //strResult = execCommand("sh "+strSimulationDir + File.separator +"run -f omnetppTest.ini -u Cmdenv -c " + strIndexTest + " | grep '" + MARKER_TOKEN + "'");
        strResult = execCommand("cd "+strSimulationDir + File.separator +" && ./run -u Cmdenv");

        dExecTime = getTick() - dTick;

        if (!strResult.isEmpty() && strResult.indexOf("Error") == -1 ) {

            eprintf("Execution OK");
        } else {
            bRet = false;
            eprintf("Error executing test ");
        }

        System.out.printf("result: "+strResult+"\n");            

        if (bRet) {
            eprintf("Original program OK!\n");
        } else {
            eprintf("Original program NOT ok!!\n");
        }

        return bRet;
    }

    private void eprintf(String string) {
        if(exePanel != null)
            exePanel.printLine(string);
        
        System.out.printf(string+"\n");
    }

    private void printResults(double dCompilationTime, double dTotalTime) {
        float fMS;                       

        eprintf("---------------------------------");
        eprintf("MuTomVo says:");
        eprintf("Compilation time: " + String.valueOf(dCompilationTime));
        eprintf("Total elapsed time: "+ String.valueOf(dTotalTime));
        eprintf("");        
        eprintf("--------------");

    }

    private void clean() {

        String strResult;
        eprintf("Cleaning ...");
        //make clean
        if (!m_strSimcan_Home.isEmpty()) {
            strResult = execCommand("make clean -C " + m_strSimcan_Home + " 2>&1 >/dev/null\n");

            eprintf("Making makefiles ...");
            strResult = execCommand("make makefiles -C " + m_strSimcan_Home + " 2>&1 >/dev/null\n");
        }

    }

    private String execCommand(String strCommand) {

        String line;
        String simcanPath, omnetPath;
        
        Process process;
        File workingDir;
        InputStream is;
        InputStreamReader isr;
        BufferedReader br;
        int i, portNumber;
        String strResult;
        Map<String, String> env = null;

        strResult = "";
        try {

            m_builder = new ProcessBuilder("bash", "-c", strCommand);
            workingDir = new File(m_strSimulationDir);
            omnetPath = m_strSimcan_Home.substring(0, m_strSimcan_Home.lastIndexOf(File.separator));
            
            //builder.redirectErrorStream(true);
            m_builder.directory(workingDir);
            // Sets the needed libraries for the execution
            env = m_builder.environment();
            
            // Configure environment variables
            env.put("LD_LIBRARY_PATH", env.get("LD_LIBRARY_PATH") + File.pathSeparator + omnetPath + "/lib");
            env.put("PATH", env.get("PATH") + File.pathSeparator + omnetPath + "/bin");
            env.put("TCL_LIBRARY", "/usr/share/tcltk/tcl8.4");

            // Sets the needed libraries for the execution
            env = m_builder.environment();
            //Launch the simulation
            process = m_builder.start();

            //Read the result of the simulation
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            // Manage the output
            while (m_bRunning && (line = br.readLine()) != null) {
                //System.out.println(line);
                strResult = strResult.concat(line);
                eprintf(line);
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(SimulationeXec.class.getName()).log(Level.SEVERE, null, ex);
            eprintf("Error executing builder!! No suck simulation");
            
            strResult = strResult.concat("Error executing process builder");
        }

        return strResult;
    }

    public void reset(String scenario) {

        System.out.printf("Doing reset: "+scenario);
        
        m_dInit = m_dCompilationTime = m_dTotalTime = 0;

        m_strSimulationDir = m_strApplication = "";
        m_bClean = m_bStopWhenFail = false;        
        m_bRunning = false;
        
        if(m_parent != null)
            exePanel = new ExecutionPanelBasic(m_parent, m_bModal, this);
        
    }

    public void Configure(String scenario, Boolean bClean) {
        File simulationDir;
                
        m_strScenarioName = scenario;
        //Esto se puede utilizar para saber que test mata que mutante
        //Ejecución completa de mutante
        m_bStopWhenFail = true;

        eprintf("Scenario: " + m_strScenarioName);
        eprintf("Application: " + m_strApplication);        

//        //Simcanhome
//        configFile = new Configuration();
//        m_strSimcan_Home = configFile.getProperty(Configuration.HOME_PATH);
//
//        //Scenario name       
//        simulationDir = new File(m_strSimcan_Home + File.separatorChar + configFile.defaultSimulationsFolder + File.separatorChar + m_strScenarioName);
//        m_strSimulationDir = simulationDir.getAbsolutePath();
//        m_bClean = bClean;                
    }

    public void setVisible(boolean b) {
        if(exePanel != null)
            exePanel.setVisible(b);
    }

    public void run() {
        setVisible(true);
        fullExec();
    }

    private void changeState(EnumMutexecState enumMutexecState) {

        m_eState = enumMutexecState;
        
        if(exePanel != null)
        {
            switch (enumMutexecState) {
                case eMUT_COMPILING:
                    exePanel.setActBarIndet(true);
                    break;
                case eMUT_EXE_ORIG:
                    exePanel.setActBarIndet(false);
                    break;
                case eMUT_EXE_FIN_KO:
                    exePanel.setActBarIndet(false);
                    break;
                case eMUT_EXE_FIN_OK:
                    exePanel.setActBarIndet(false);
                    exePanel.setBarsColor(Color.red);
                    break;
                case eMUT_EXE_FIN_ABORT:
                    exePanel.setActBarIndet(false);
                    break;
            }

            exePanel.setState(enumMutexecState.toString());
        }
        else
            eprintf("ChangeState:  "+m_eState.toString()+"\n");
        
        
    }

    private void setError(String string) {
        m_strLastError = string;
        eprintf(m_strLastError);
    }

    private void printMutantsInfo() {
        
        eprintf("-------------------------------------------");
        eprintf("Mutants Info " + String.valueOf(m_mutantsInfo.getSize()));
        for (int i = 0; i < m_mutantsInfo.getSize(); i++) {

            mutantInfo mut= m_mutantsInfo.getMutant(i);

            if (mut != null) {
                eprintf(mut.ToString());
            }
        }
        eprintf("-------------------------------------------\n");
    }
    
    public String getLastLine(String strPath)
    {
        File fPath = new File(strPath);
        return getLastLine(fPath);
    }
    public String getLastLine(final File file){
        String result = "";
        
        // file needs to exist
        if(file.exists() == false || file.isDirectory()){
            return "";
        }
        
        // avoid empty files
        if(file.length() == 0){
            return "";
        }
        
        try {
            // open the file for read-only mode
            RandomAccessFile fileAccess = new RandomAccessFile(file, "r");

            // set initial position as last one, except if this is an empty line
            long position = file.length()-2;
            int breakLine = "\n".charAt(0);

            // keep looking for a line break
            while(position > 0){
                // look for the offset
                fileAccess.seek(position);
                // read the new char
                final int thisChar = fileAccess.read();
                // do we have a match?
                if(thisChar == breakLine){
                    result = fileAccess.readLine();
                    //System.out.println(position + ": " + fileAccess.readLine());
                    break;
                }
                // decrease the offset
                position--;
            }
           // close the stream (thanks to Michael Schierl)
          fileAccess.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // all done
        return result;
    } 


    public void forceExecutionCmd() {
        this.run();
    }

    public void Configure(int nGeneratedMutants, int nGeneratedTests, String application, String applicationType, String scenario, Boolean bClean, Boolean bAll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Configure(ExecutionConfig exeConfig) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
