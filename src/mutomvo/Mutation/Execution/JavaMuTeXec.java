/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution;

import mutomvo.Mutation.Execution.auxiliars.EnumMutexecState;
import mutomvo.Mutation.Execution.auxiliars.IMuTeXec;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.Mutation.Execution.auxiliars.MTJavaeXecThread;
import mutomvo.Mutation.Execution.info.GenericCfgCompilator;
import mutomvo.Mutation.ReportGenerator.StatsPersistence;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;
import static mutomvo.Mutation.ReportGenerator.StatsPersistence.STATS_MAIN_FOLDER;
import mutomvo.TabbedPanels.ExecutionPanelExt;
import mutomvo.Utils.Utils;


/**
 *
 * @author Pablo C. Cañizares 
 */
public class JavaMuTeXec implements IMuTeXec, Runnable {

    private final int MASTER_ID = 0;
    protected int nId;
    protected int m_nMutants;
    protected int m_nTotalTests;
    protected int m_nDeadMutants;
    protected int m_nAliveMutants;    
    
    //Timing
    protected double m_dInit;
    protected double m_dCompilationTime;
    protected double m_dTotalTime;
    
    //Configuration
    GenericCfgCompilator m_oGenericCfgCompilator;
    ExecutionConfig m_oCfg;
    
    //Statistics
    protected StatsPersistence m_oStatsSaver;

    protected boolean m_bRunning;    
    protected boolean m_bKilledTimeOut;
    protected String m_strLastError;                //Last error detected  
    
    protected EnumMutexecState m_eState;
    
    //Process management
    ProcessBuilder m_builder;
    Process m_process;
    
    private LinkedList<MTJavaeXecThread> multiThreadList;
    protected javax.swing.Timer durationTimer;
    
    //Test Info
    LinkedList<String> m_oTestSuiteList;
    protected TestExecutionInfo m_testOriginalInfo;
    MutantsExecutionInfo m_mutantsInfo;
    
    //Graphics
    JFrame m_parent;
    boolean m_bModal;
    protected ExecutionPanelExt exePanel;
    private final String MARKER_TOKEN = "#__#";
        
    public JavaMuTeXec()
    {
        initialize();
    }
    public JavaMuTeXec(JFrame parent, boolean bModal) {
        
        initialize();
        
        m_parent=parent;
        m_bModal = bModal;               
        
        if(parent != null)
            exePanel = new ExecutionPanelExt(m_parent, m_bModal, this);
        else
            exePanel = null;
        
        multiThreadList = new LinkedList<MTJavaeXecThread>();

    }
    private void initialize()
    {
        m_eState = EnumMutexecState.eMUT_NONE;
        m_mutantsInfo = new MutantsExecutionInfo();
        
        nId = MASTER_ID;

        m_bRunning = false;
        m_bKilledTimeOut = false;        
    }
    public void doKill()
    {
        Thread t = new Thread()
        {
            @Override
            public void run() {
                try {
                    final String EXPECTED_IMPL_CLASS_NAME = "java.lang.UNIXProcess";
                    final String EXPECTED_PID_FIELD_NAME = "pid";


                    Field f = m_process.getClass().getDeclaredField("pid");
                    f.setAccessible(true); // allows access to non-public fields
                    int pid = f.getInt(m_process);

                    System.out.println("Killing pid: "+Integer.toString(pid));
                    //execCommand("pkill -P "+Integer.toString(pid));
                    //kill -9 -$(ps -o pgid= $PID | grep -o '[0-9]*')
                    execCommand("kill -9 -$(ps -o pgid= "+Integer.toString(pid)+" | grep -o '[0-9]*'");
                } 
                catch (IllegalThreadStateException ite)
                {
                    m_process.destroy();
                }
                catch (IllegalArgumentException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchFieldException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        t.start();
    }
    public void configureTimer(int nTime)
    {
        int nTimeoutFactor;
        
        //Get the timeout factor from config
        nTimeoutFactor = m_oCfg.getnTimeoutFactor();
        
        //Check the size of timer. Depending on the time elapsed waiting, the timeout may be invalid
        if(nTime < 1000)
            nTime = ((nTime + 1000)*nTimeoutFactor)*4;
        else if(nTime < 20000)
            nTime = (nTime*nTimeoutFactor)*3;
        else if(nTime < 100000)
            nTime = (nTime*nTimeoutFactor)*2;
        else
            nTime = (nTime*nTimeoutFactor);
        
        durationTimer = new javax.swing.Timer(nTime, new ActionListener() {               
            @Override
            public void actionPerformed(ActionEvent e) {       
                try {
                    System.out.printf("Aborting by timeout!!\n");
                    
                    m_bKilledTimeOut=true;                
                    
                    //doKill();
                    final String EXPECTED_IMPL_CLASS_NAME = "java.lang.UNIXProcess";
                    final String EXPECTED_PID_FIELD_NAME = "pid";                
                    Field f = m_process.getClass().getDeclaredField("pid");
                    f.setAccessible(true); // allows access to non-public fields
                    int pid = f.getInt(m_process);

                    System.out.println("Killing pid: "+Integer.toString(pid));

                    //Kill the tree process
                    execCommand("kill `pstree -p "+Integer.toString(pid)+" | grep -oP '(?<=\\()[0-9]+(?=\\))'`");

                    durationTimer.stop();
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchFieldException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        });                         
    }
    @Override
    public boolean Abort() {
        boolean bRet = false;
                        
        changeState(EnumMutexecState.eMUT_EXE_FIN_ABORT);
        if(!m_oCfg.isbExecuteAllTests())
            m_bRunning=false;
        
        eprintf("Aborted!");
        return bRet;
    }

    public double getTick() {
        return System.currentTimeMillis();
    }

    @Override
    public boolean fullExec() {
        boolean bRet = false;

        if (checkArguments()) {

            updateTextArea(0,0,0);
            //Initialize(m_nMutants);
            m_dInit = getTick();
            m_bRunning= true;
                        
            if (compile(m_oCfg.isbClean())) {
                
                m_dCompilationTime = getTick() - m_dInit;
                if (execOriginalProgram(m_nTotalTests)) {
                    
                    if(!m_oCfg.isMultithreading())
                        bRet = exeSingleThread();
                    else
                        bRet = exeMultiThreading();

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

    private boolean exeMultiThreading()
    {
        boolean bRet,bRunning;
        int nFirst, nEnd, nTotalMutants, nAlive, nDead, nProcMutants;
        int nThreads, nThreadCount,nThreadsRunning;
        Thread th;
        MTJavaeXecThread mtExec;
        
        //Initialize
        nThreads = m_oCfg.getNumThreads();
        nThreadCount= nThreads;
        nThreadsRunning = nThreadCount;
        nFirst = nEnd = nAlive = nDead = nProcMutants = 0;
        nTotalMutants = this.m_nMutants;
        bRet = bRunning = true;
        
        //Launch the tests
        for(int i=0;i<nThreads;i++)
        {
            try {
                if(nEnd !=0)
                    nFirst = nEnd+1;
                
                nEnd = (nFirst + (nTotalMutants / nThreadCount))-1;
                nTotalMutants -= (nTotalMutants / nThreadCount);
                nThreadCount--;
                
                mtExec= new MTJavaeXecThread();
                multiThreadList.add(i,mtExec);
                mtExec.Configure(m_oCfg);
                mtExec.configThread(i, nFirst, nEnd, m_oCfg.getnTimeoutFactor(), m_testOriginalInfo);
                
                th = new Thread(mtExec, "New Thread");
                th.start();
            } catch (MutomvoException ex) {
                System.out.println("Error configuring the thread!\n");
                Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Todo: Polling results of the different threads
        do
        {
            nThreadsRunning = nAlive = nDead = nProcMutants = 0;
            
            for(int i=0;i<nThreads;i++)
            {                                
                if(!multiThreadList.get(i).isFinished())
                    nThreadsRunning++;
                
                nProcMutants += multiThreadList.get(i).getMutantsProcessed();
                nAlive += multiThreadList.get(i).getAliveMutants();
                nDead += multiThreadList.get(i).getDeadMutants();                
            }
            
            incTotalBar(nProcMutants,m_nMutants);
            updateTextArea(nProcMutants, nAlive, nDead);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {       
            }            
        }while(nThreadsRunning>0);
             
        m_nDeadMutants = nDead;
        printResults(m_nMutants, m_nDeadMutants, m_nTotalTests, m_dCompilationTime, m_dTotalTime);
                
        return bRet;
    }
    private boolean exeSingleThread()
    {
        boolean bRet;
        EnumMutantState mutantState;
        int nIndexMutant, nMutantIntervalIni, nMutantIntervalEnd;
        
        //Get from config
        nMutantIntervalIni = m_oCfg.getMutantIntervalIni();
        nMutantIntervalEnd = m_oCfg.getMutantIntervalEnd();
        
        incTotalBar(1,m_nMutants+1);
        eprintf("Executing mutants ... \n");
        for (int i = nMutantIntervalIni; i < nMutantIntervalEnd && m_bRunning; i++) {

            //durationTimer.setDelay();

            System.out.printf("Waiting ... \n"+Integer.toString((int)(m_testOriginalInfo.getTotalTime()*4)));
            mutantState = execMutant(i, m_nTotalTests);

            if (mutantState != EnumMutantState.eMUTANT_ALIVE) {
                m_nDeadMutants++;
            }
            else
                m_nAliveMutants++;

            nIndexMutant = (i - nMutantIntervalIni)+1;
            incTotalBar(nIndexMutant,m_nMutants+1);
            updateTextArea(nIndexMutant,m_nAliveMutants,m_nDeadMutants);
        }
        incTotalBar(m_nMutants+1,m_nMutants+1);
        setRemTime(m_nMutants,m_nMutants,m_testOriginalInfo.getTotalTime());
        m_dTotalTime = getTick() - m_dInit;
        printResults(m_nMutants, m_nDeadMutants, m_nTotalTests, m_dCompilationTime, m_dTotalTime);
        bRet = true;
                    
        return bRet;
    }
    private boolean checkArguments() {
        boolean bRet = false;
        String strParametersNeeded;

        strParametersNeeded = "";
        //Obtenemos el scenario dir

        if (m_nMutants == 0) {
            strParametersNeeded = "nMutants| ";
        }

        strParametersNeeded += m_oCfg.getParametersNeeded();
        
        if (strParametersNeeded.isEmpty()) {
            bRet = true;
        } else {
            eprintf("Parameters not found: " + strParametersNeeded + "\n");
        }

        //Graphic MODE!
        if(exePanel != null)
        {
            m_oCfg.setAllTests(exePanel.getAllTestExecution());
            m_oCfg.setMultithreading(exePanel.getMultiThreading());            
            m_oCfg.setInterval(exePanel.isInterval());       
            
            if(m_oCfg.isInterval())
            {
                m_oCfg.setMutantIntervalIni(exePanel.getInitInterval());
                m_oCfg.setMutantIntervalEnd(exePanel.getEndInterval());
                
                if(m_oCfg.getMutantIntervalEnd() >= m_oCfg.getMutantIntervalIni())
                {
                    // +1 [init, End] Both included
                    m_nMutants = (m_oCfg.getMutantIntervalEnd()-
                            m_oCfg.getMutantIntervalIni())+1;
                }
                //else
                //    throws new MutomvoException("The mutant interval is invalid");
            }else
            {
                m_oCfg.setMutantIntervalIni(0);
                m_oCfg.setMutantIntervalEnd(m_nMutants);
            }

        }
        
        return bRet;
    }

    private boolean compile(boolean m_bClean) {

        boolean bRet;
        String strResult, strAppPath, strMutantPath, strOriginalCompLine;

        //Initialise
        bRet = false;
        changeState(EnumMutexecState.eMUT_COMPILING);
        
        //Get paths
        strAppPath = m_oCfg.getAppPath();
        strMutantPath = m_oCfg.getMutantsPath();

        eprintf("\nStarting compilation!");
        eprintf("Application path = " + strAppPath);
        eprintf("Mutants path = " + strMutantPath);
        
        if(m_oCfg.isCompile())
        {
            if (m_bClean) {
                clean();
            }

            if (!strAppPath.isEmpty()) {
                //Redireccionamos la salida estandar a null, y sólo mostramos los errores
                
                strOriginalCompLine = m_oGenericCfgCompilator.getCompilationOriginalLine();
                
                strResult = execCommand(strOriginalCompLine + " 2>&1 >/dev/null\n");                
                
                if (!strResult.isEmpty() && !strResult.contains("Error")) {
                    eprintf("Compilation err: " + strResult);
                } else {
                    eprintf("Original program compilation done!");
                    bRet = true;
                    
                    eprintf("Compiling now the mutants!");
                }
            } else {
                eprintf("ERROR!! Insert environment variable $SIMCAN_HOME;");
            }

            eprintf("Compilation End!");
       }
        else
        {
            bRet = true;
            eprintf("Compilation is disabled!");
        }

        return bRet;
    }
    
    private void Initialize(int nMutants) {

        m_nMutants = m_nTotalTests = m_nDeadMutants = m_nAliveMutants = 0;
        m_dInit = m_dCompilationTime = m_dTotalTime = 0;

    }

    private void eprintf(String string) {
        if(exePanel != null)
            exePanel.printLine(string);
        
        System.out.printf("<%d> %s\n",nId, string);
    }

    private boolean execOriginalProgram(int nTotalTests) {

        boolean bRet;
        int nIndexTest, nValue;
        double dExecTime, dTick, dProgress;
        String strResult, strIndexTest;        
        
        changeState(EnumMutexecState.eMUT_EXE_ORIG);

        bRet = true;
        nIndexTest = 0;
        dExecTime = 0.0;
        
        //Executing data for creating results
        if(!m_oCfg.isLoadOriginalResults() || !m_oStatsSaver.existsOriginalRes())
        {
            eprintf("\nExecuting original program...");
            //lo primero hacer
            while (nIndexTest < nTotalTests && bRet) {
                incActBar(nIndexTest,nTotalTests);

                dTick = getTick();
                strIndexTest = m_oTestSuiteList.get(nIndexTest);
                                    
                strResult = execCommand("cd "+m_oCfg.getAppFolder()+ File.separator +" && "+strIndexTest+" | sort | grep '" + MARKER_TOKEN + "'");

                dExecTime = getTick() - dTick;

                if (!strResult.isEmpty() && !strResult.contains("Error") && strResult.contains(MARKER_TOKEN)) {

                    //Guardamos el resultado+tiempo en una lista
                    m_testOriginalInfo.addTest(nIndexTest, strResult, dExecTime, EnumMutantState.eMUTANT_ALIVE);
         
                    nIndexTest++;
                } else {
                    bRet = false;
                    eprintf("Error executing test ["+strIndexTest+"]"+ strResult);
                }

                System.out.printf(strIndexTest+" | result: "+createMd5(strResult)+"\n");      
            }

            if (bRet) {
                eprintf("Original program OK!\n");
                m_oStatsSaver.saveOriginalRes(m_testOriginalInfo);
            } else {
                eprintf("Original program NOT ok!!\n");
            }
        }else
        {
            eprintf("\nLoading original results from disk...");
            //Load original results from disk
            if(m_oStatsSaver.loadOriginalRes())
                m_testOriginalInfo = m_oStatsSaver.getOriginalResults();
        }

        return bRet;
    }

    protected EnumMutantState execMutant(int nIndexMutant, int nTotalTests) {

        boolean bRet = true;
        int nIndexTest;
        double dExecTime, dTick;
        String strResult, strIndexTest, strMutantTest,strRemTime;        
        EnumMutantState eRet;
        TestExecutionInfo testInfo;
        
        testInfo = new TestExecutionInfo(nTotalTests);
        eRet = EnumMutantState.eMUTANT_ALIVE;
        
        
        changeState(EnumMutexecState.eMUT_EXE_MUT);
        setRemTime(nIndexMutant,m_nMutants,m_testOriginalInfo.getTotalTime());
        strRemTime = getRemTime(nIndexMutant,m_nMutants,m_testOriginalInfo.getTotalTime());
        nIndexTest = 0;
           
        eprintf("\nExecuting mutant "+Integer.toString(nIndexMutant)+ "| RemTime: "+strRemTime);
        
        strMutantTest = String.format("mutant_%04d", nIndexMutant);
        
        //lo primero hacer
        while (nIndexTest < nTotalTests && (m_oCfg.isbExecuteAllTests() || (bRet && !m_bKilledTimeOut))) {
            
            eRet = EnumMutantState.eMUTANT_ALIVE;
            configureTimer((int)(m_testOriginalInfo.getTestTime(nIndexTest)));
            incActBar(nIndexTest,nTotalTests);
            
            dTick = getTick();
            strIndexTest = m_oTestSuiteList.get(nIndexTest);
            
            durationTimer.start();            
            strResult = execCommand("cd "+m_oCfg.getMutantsPath() +Integer.toString(nIndexMutant)+File.separator+" && "+strIndexTest+" | sort | grep '" + MARKER_TOKEN + "'");
            durationTimer.stop();
            
            dExecTime = getTick() - dTick;
            
            if(!strResult.isEmpty())
                System.out.printf(strMutantTest+"_"+strIndexTest+" | result: "+createMd5(strResult)+"\n"); 
            else
                System.out.printf(strMutantTest+"_"+strIndexTest+" | result: "+strResult+"\n"); 
               
            if (!strResult.isEmpty() && !strResult.contains("Error") && strResult.contains(MARKER_TOKEN)) {

                //It is necessary to check the result of the test case
                if(!m_testOriginalInfo.equalRes(nIndexTest,strResult))
                {
                    bRet =false;
                    if(m_eState != EnumMutexecState.eMUT_EXE_FIN_ABORT)
                        eprintf("Mutant Killed by test: " + Integer.toString(nIndexTest));
                    eRet = EnumMutantState.eMUTANT_DEAD;                    
                } 
                //nIndexTest++;
            } 
            else {
                bRet = false;
                if(m_eState != EnumMutexecState.eMUT_EXE_FIN_ABORT)
                    eprintf("Mutant Killed by test: " + Integer.toString(nIndexTest));
                eRet = EnumMutantState.eMUTANT_DEAD;
            }
            testInfo.addTest(nIndexTest, strResult, dExecTime,eRet);            
            nIndexTest++;
            
            if(m_bKilledTimeOut && m_oCfg.isbExecuteAllTests())
            {
                eprintf(Integer.toString(nIndexTest)+" - Killed by timeout!\n");
                m_bKilledTimeOut=false;
            }                        
        }
        
        if(m_bKilledTimeOut)
        {
            eprintf("Killed by timeout! In test "+ Integer.toString(nIndexTest));
            m_bKilledTimeOut= false;
            eRet = EnumMutantState.eMUTANT_DEAD;
        }

        if (bRet) {
            eprintf("Mutant Alive!\n");
        }
        
        insertMutantInfo(nIndexMutant,nIndexTest, testInfo, testInfo.getMutantState());
        
        return eRet;        
    }

    private void printResults(int nMutants, int nDeadMutants, int nTotalTests, double dCompilationTime, double dTotalTime) {
        float fMS;
        LinkedList<String> printReadableList;
        
        printReadableList = new LinkedList<String>();        
        
        fMS = (float) nDeadMutants / (float) nMutants;

        printReadableList.add("---------------------------------");
        printReadableList.add("MuTomVo says:");
        printReadableList.add("Compilation time: " + String.valueOf(dCompilationTime));
        printReadableList.add("Total elapsed time: "+ String.valueOf(dTotalTime));
        printReadableList.add("");
        printReadableList.add("Total mutants: " + String.valueOf(nMutants));
        printReadableList.add("Alive mutants: " + String.valueOf(nMutants - nDeadMutants));
        printReadableList.add("Dead mutants: " + String.valueOf(nDeadMutants));
        printReadableList.add("Mutation Score: " +  String.valueOf(fMS));

        printReadableList.add("");
        printMutantsInfo(printReadableList, true);
        
        m_oStatsSaver.saveMutationProcessCSV(m_mutantsInfo);
    }
    public void saveResultsToDisk(String fileName, LinkedList<String>  dataList, boolean bPrint){
        PrintWriter pw;
        String strLine;
        int nSize;
        
        nSize= dataList.size();
        
        if(nSize>0)
        {
            try 
            {
                pw = new PrintWriter(new FileOutputStream(fileName));

                for (int i=0;i<nSize;i++)
                {
                    strLine = dataList.get(i);
                    pw.println(strLine);
                    
                    if(bPrint)
                        eprintf(strLine);
                }
               
                pw.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void insertMutantInfo(int nIndexMutant, int nIndexKiller, TestExecutionInfo testInfo, EnumMutantState enumMutantState) {
        
        String strDesc, strMutantPath;
        
        //Faltaría obtener la última línea del mutante
        if(m_oCfg.getMutantsPath() != null && !m_oCfg.getMutantsPath().endsWith("/"))
            strMutantPath = String.format("%s/%d/%s", m_oCfg.getMutantsPath(), nIndexMutant, m_oCfg.getAppName());
        else
            strMutantPath = String.format("%s%d/%s", m_oCfg.getMutantsPath(), nIndexMutant, m_oCfg.getAppName());
        
        strDesc = getLastLine(strMutantPath);
        m_mutantsInfo.insert(nIndexMutant, nIndexKiller, testInfo, enumMutantState,strDesc);
        
        //Save mutant to persistance
        m_oStatsSaver.saveMutant(m_mutantsInfo.getMutant(nIndexMutant));
    }

    private void clean() {

        String strResult;
        eprintf("Cleaning ...");
        
        //make clean
        if (!m_oCfg.isCleanLineEmpty()) {
            strResult = execCommand(m_oCfg.getCleanLine()+ " 2>&1 >/dev/null\n");
            eprintf("Clean!");              
        }

    }

    private String execCommand(String strCommand) {

        String line;
        
        File workingDir;
        InputStream is;
        InputStreamReader isr;
        int i, portNumber;
        String strResult;
        Map<String, String> env = null;
        BufferedReader buffReader;
        
        strResult = "";
        try {

            ProcessBuilder builder = new ProcessBuilder("bash", "-c", strCommand);
            //workingDir = new File(m_strSimulationDir);
            //Depending on the phase of the MuT process (original program or ), the working dir must be different. 
            //workingDir = new File(m_strSimcan_Home);            
            
            //m_builder.directory(workingDir);
            
            // Sets the needed libraries for the execution
            //env = m_builder.environment();
            
            // Configure environment variables
            //TODO: Allow in the future, to introduce these values, by using configuration.
            //env.put("LD_LIBRARY_PATH", env.get("LD_LIBRARY_PATH") + File.pathSeparator + omnetPath + "/lib");
            //env.put("PATH", env.get("PATH") + File.pathSeparator + omnetPath + "/bin");
            //env.put("TCL_LIBRARY", "/usr/share/tcltk/tcl8.4");
            //env.put("SIMCAN_HOME", m_strSimcan_Home);
            
            // Sets the needed libraries for the execution
            //env = m_builder.environment();
            
            //Launch the simulation
            m_process = builder.start();

            //Read the result of the simulation
            is = m_process.getInputStream();
            isr = new InputStreamReader(is);
            buffReader = new BufferedReader(isr);

            // Manage the output
            while (m_bRunning  && !m_bKilledTimeOut && (line = buffReader.readLine()) != null) {
                //System.out.println(line);
                strResult = strResult.concat(line);
            }
            buffReader.close();
            
        } catch (IOException ex) {
            Logger.getLogger(JavaMuTeXec.class.getName()).log(Level.SEVERE, null, ex);
        }

        return strResult;
    }

    @Override
    public void reset(String strProjectName) {

        System.out.printf("Doing reset: "+strProjectName);
        m_nMutants = m_nTotalTests = m_nDeadMutants = m_nAliveMutants = 0;
        m_dInit = m_dCompilationTime = m_dTotalTime = 0;

        if(m_oCfg != null)
            m_oCfg.reset();
     
        m_bRunning = false;
        
        if(m_parent != null)
            exePanel = new ExecutionPanelExt(m_parent, m_bModal, this);        
    }

    @Override
    public void Configure(ExecutionConfig exeConfig) throws MutomvoException{
        
        String strException;
        
        strException = "";
        try{
                
            m_oGenericCfgCompilator = new GenericCfgCompilator(exeConfig);
            m_oCfg = exeConfig;

            //Load the test suite (limiting with the fixed tests to improve the performance)
            if(Utils.existsFile(m_oCfg.getTestSuiteFile()))
                m_oTestSuiteList = Utils.loadTestSuiteFromFile(m_oCfg.getTestSuiteFile(), m_oCfg.isbIsFixedTests(), m_oCfg.getnTotalFixedTests());
            else
                strException = "The test suite path is empty";
            
            //Configure the test suite
            if(!m_oCfg.isbIsFixedTests())
                m_nTotalTests = m_oTestSuiteList.size();
            else
                m_nTotalTests = m_oCfg.getnTotalFixedTests();
            
            //Configure the mutant set
            if(!m_oCfg.isbIsFixedMutants())
                m_nMutants = m_oCfg.getGeneratedMutants(); 
            else
                m_nMutants = m_oCfg.getnTotalFixedMutants();            
                         
            m_testOriginalInfo  = new TestExecutionInfo(m_nTotalTests);
            m_mutantsInfo = new MutantsExecutionInfo(m_nMutants);
            m_oStatsSaver = new StatsPersistence(m_oCfg.getAppName());

            updateTextArea(0,m_nAliveMutants,m_nDeadMutants);

            if(exePanel != null)
                exePanel.setEndInterval(m_nMutants);
            
            eprintf("Application: " + m_oCfg.getAppName());
            eprintf("Mutants set: " + Integer.toString(m_nMutants) + ", tests: " + Integer.toString(m_nTotalTests));
            
            if(m_oCfg.isInterval())
                eprintf(String.format("Interval: %b [%d, %d)", m_oCfg.isInterval(), m_oCfg.getMutantIntervalIni(), m_oCfg.getMutantIntervalEnd()));
            else
                eprintf(String.format("Interval: %b [%d, %d)", m_oCfg.isInterval(), 0, m_nMutants));
        }
        catch(Exception e)
        {
            throw new MutomvoException("[JavaMuTeXec::Configure] - Error configuring the executor: "+strException);
        }
        
    }

    @Override
    public void setVisible(boolean b) {
        if(exePanel != null)
            exePanel.setVisible(b);
    }

    @Override
    public void run() {
                
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

    private void incActBar(int nIndexTest, int nTotalTests) {
        
        double dProgress;
        int nValue;
        
        dProgress = (100/(double)m_nTotalTests);
        nValue =   (int) ((nIndexTest)*dProgress);
        if(exePanel != null)
            exePanel.setActBarValue(nValue);            
    }

    private void incTotalBar(int nIndexMut, int nTotalMut) {
        
        double dProgress;
        int nValue;
        
        dProgress = (100/(double)nTotalMut);
        nValue =   (int) ((nIndexMut)*dProgress);
        
        if(exePanel != null)
            exePanel.setTotalBarValue(nValue);     
        
    }
    private String getRemTime(int nMutantIndex, int nTotalMutants, double dOriginalTime)
    {
        String strRet;
        int nMilliseconds;
        
        nMilliseconds = (int) ((nTotalMutants-nMutantIndex) * dOriginalTime);
        
        if(nMilliseconds>1000)
            nMilliseconds = nMilliseconds /1000;
        
        strRet = TimeToString(nMilliseconds);
        
        return strRet;
    }
    private void setRemTime(int nMutantIndex, int nTotalMutants, double dOriginalTime)
    {
        int nMilliseconds;
        
        nMilliseconds = (int) ((nTotalMutants-nMutantIndex) * dOriginalTime);

        if(exePanel != null)
            exePanel.setRemTime(nMilliseconds);
    }
    //TODO: Quitar de aqui el readable -> Que sea el propop mutants info quien proporcione la info
    private void printMutantsInfo(LinkedList<String> readableList, boolean bPrint) {
        
        String strName;
        
        readableList.add("-------------------------------------------");
        readableList.add("Mutants Info " + String.valueOf(m_mutantsInfo.getSize()));
        for (int i = 0; i < m_mutantsInfo.getSize(); i++) {

            mutantInfo mut= m_mutantsInfo.getMutant(i);

            if (mut != null) {
                readableList.add(mut.ToString());
            }
        }
        readableList.add("-------------------------------------------\n");
        strName = String.format(STATS_MAIN_FOLDER+File.separator+m_oCfg.getAppName()+File.separator+"ReadableStats.txt");
        saveResultsToDisk(strName, readableList, true);
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // all done
        return result;
    } 
    public void updateTextArea(int nMutantIndex, int nAlive, int nDead)
    {
        String strRes;
        float fMS;
        
        fMS = (float) 0.0;
        
        if(nMutantIndex!=0)
            fMS = (float) nDead / (float) nMutantIndex;
        
        strRes = String.format("Total mutants: \t%d\nExec Mutants:\t%d\nAlive:\t\t%d\nDead:\t\t%d\nMS:\t\t%.2f\n",
                m_nMutants,nMutantIndex,nAlive, nDead,fMS);
        
        if(exePanel != null)
            exePanel.updateTextArea(strRes);
    }

    @Override
    public void forceExecutionCmd() {
        this.run();
    }
    //TODO: Cambiar este método a estático en una única clase
    public String createMd5(String strOriginal)
    {
        String strReturn,original;
        byte[] digest ;
        StringBuffer sb;
        MessageDigest md;
                
        strReturn = original = null;
        
        try {
            original = strOriginal;
            md = MessageDigest.getInstance("MD5");
            md.update(original.getBytes());
            digest = md.digest();
            sb = new StringBuffer();
            for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
            }

            //System.out.println("original:" + original);
            //System.out.println("digested(hex):" + sb.toString());
            strReturn = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TestExecutionInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return strReturn;
    }
    private String TimeToString(long elapsedTime) {
        
        String seconds = Integer.toString((int) (elapsedTime % 60));
        String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
        String hours = Integer.toString((int) (elapsedTime / 3600));

        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }

        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }

        if (hours.length() < 2) {
            hours = "0" + hours;
        }

        return hours + ":" + minutes + ":" + seconds;
    }
}