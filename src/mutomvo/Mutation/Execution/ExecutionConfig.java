/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution;

import java.io.File;
import static mutomvo.Utils.Utils.DEFAULT_TIMEOUT_FACTOR;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class ExecutionConfig {
    
    private boolean reWriteMutants;                 //
    private boolean bExecuteAllTests;               //
    private boolean bAskUser;                       //
    private boolean bClean;                         //
    private boolean bInterval;                      //
    private boolean bAllTests;                      //
    private boolean bMultiThreading;                //
    
    private int nInitInterval;                      //
    private int nEndInterval;                       //
    private int nGeneratedMutants;                  //Number of generated mutants
    private int nGeneratedTests;                    //Number of generated tests

    private int nTotalMutants;
    private int nTotalFixedMutants;
    private boolean bIsFixedMutants;
    private boolean bIsFixedTests;
    private int nTotalTests;
    private int nTotalFixedTests;
    
    //Generic execution config  
    private String strProjectName;
    private String strOriginalCompilationLine;      //Line used to compile the original program
    private String strMutantsCompilationLine;       //Line used to compile the mutants
    private String strAppPath;                      //Full path of the application  
    private String strAppFolder;                    //Folder of the original application
    private String strAppName;                      //Name of the application
    private String strMutantsPath;                  //Full path of the mutants
    private String strTestSuiteFile;                //Full path of the test suite.
    private String m_strCleanLine;                  //Line used to clean the project
    
    private boolean bStopWhenFail;    
    private int nTimeoutFactor;
    
    private boolean m_bIsIntervalExec;
    private boolean m_bLoadOriginalResults;
    private boolean m_bCompile;
    private int m_nMutantIntervalIni;
    private int m_nMutantIntervalEnd;
    
    //Multi-threading
    private boolean m_bMultithreading;              //Flag that represents if the execution is perfomed in multithread mode
    private int numThreads;                         //Number of threads used to conduct the execution
    
    public ExecutionConfig()
    {
        reset();
    }
    public boolean isAllTests() {
        return bAllTests;
    }

    public void setAllTests(boolean bAllTests) {
        this.bAllTests = bAllTests;
    }

    public boolean isInterval() {
        return bInterval;
    }

    public void setInterval(boolean bInterval) {
        this.bInterval = bInterval;
    }

    public int getnEndInterval() {
        return nEndInterval;
    }

    public void setnEndInterval(int nEndInterval) {
        this.nEndInterval = nEndInterval;
    }

    public int getnInitInterval() {
        return nInitInterval;
    }

    public void setnInitInterval(int nInitInterval) {
        this.nInitInterval = nInitInterval;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setGenMutants(int nGeneratedMutants) {
        this.nGeneratedMutants = nGeneratedMutants;
    }

    public void setGenTests(int nGeneratedTests) {
        this.nGeneratedTests = nGeneratedTests;
    }

    public void setApp(String application) {
        this.strAppName = application;
    }

    public void setRWMutants(boolean reWriteMutants) {
        this.reWriteMutants = reWriteMutants;
    }

    public void setExecuteAllTests(boolean bExecuteAllTests) {
        this.bExecuteAllTests = bExecuteAllTests;
    }

    int getGeneratedMutants() {
        return nGeneratedMutants;
    }

    int getGeneratedTests() {
        return nGeneratedTests;
    }

    String getAppName() {
        return strAppName;
    }

    boolean getExecuteAll() {
        return bExecuteAllTests;
    }

    boolean getClean() {
        return bClean;
    }

    public void setRawInterval(String string) {
        String[] strSplit;
        
        if(string.length()>0)
        {
            strSplit = string.split("-");
            
            nInitInterval = Integer.parseInt(strSplit[0]);
            nEndInterval = Integer.parseInt(strSplit[1]);
            bInterval = true;
        }
    }

    public boolean AskUser() {
        return bAskUser;
    }

    public int getNumGeneratedMutants() {
        return nGeneratedMutants;
    }

    public void setNumGeneratedMutants(int nGeneratedMutants) {
        this.nGeneratedMutants = nGeneratedMutants;
    }

    public int getNumGeneratedTests() {
        return nGeneratedTests;
    }

    public void setNumGeneratedTests(int nGeneratedTests) {
        this.nGeneratedTests = nGeneratedTests;
    }

    public void setAppName(String application) {
        this.strAppName = application;
    }

    public boolean isReWriteMutants() {
        return reWriteMutants;
    }

    public void setReWriteMutants(boolean reWriteMutants) {
        this.reWriteMutants = reWriteMutants;
    }

    public boolean isbExecuteAllTests() {
        return bExecuteAllTests;
    }

    public void setbExecuteAllTests(boolean bExecuteAllTests) {
        this.bExecuteAllTests = bExecuteAllTests;
    }

    public boolean isbAskUser() {
        return bAskUser;
    }

    public void setbAskUser(boolean bAskUser) {
        this.bAskUser = bAskUser;
    }

    public boolean isbClean() {
        return bClean;
    }

    public void setbClean(boolean bClean) {
        this.bClean = bClean;
    }

    public String getOriginalCompilationLine() {
        return strOriginalCompilationLine;
    }

    public void setOriginalCompilationLine(String m_strOriginalCompilationLine) {
        this.strOriginalCompilationLine = m_strOriginalCompilationLine;
    }

    public String getMutantsCompilationLine() {
        return strMutantsCompilationLine;
    }

    public void setMutantsCompilationLine(String m_strMutantsCompilationLine) {
        this.strMutantsCompilationLine = m_strMutantsCompilationLine;
    }

    public String getAppPath() {
        return strAppPath;
    }

    public void setStrAppPath(String m_strAppPath) {
        this.strAppPath = m_strAppPath;
    }

    public String getStrAppName() {
        return strAppName;
    }

    public String getProjectName() {
        return strProjectName;
    }

    public void setProjectName(String strProjectName) {
        this.strProjectName = strProjectName;
    }

    public void setStrAppName(String m_strAppName) {
        this.strAppName = m_strAppName;
    }

    public String getMutantsPath() {
        return strMutantsPath;
    }

    public void setMutantsPath(String m_strMutantsPath) {
        this.strMutantsPath = m_strMutantsPath;
    }

    public void setIsRewriteMutants(boolean reWriteMutants) {
        this.reWriteMutants = reWriteMutants;
    }

    public void setTestSuitePath(String strTestSuiteFile) {
       this.strTestSuiteFile = strTestSuiteFile;
    }

    
    public String getTestSuiteFile() {
        return strTestSuiteFile;
    }

    public void setTestSuiteFile(String strTestSuiteFile) {
        this.strTestSuiteFile = strTestSuiteFile;
    }

    public boolean isbStopWhenFail() {
        return bStopWhenFail;
    }

    public void setbStopWhenFail(boolean bStopWhenFail) {
        this.bStopWhenFail = bStopWhenFail;
    }

    public int getnTimeoutFactor() {
        return nTimeoutFactor;
    }

    public void setnTimeoutFactor(int nTimeoutFactor) {
        this.nTimeoutFactor = nTimeoutFactor;
    }

    public boolean isMultithreading() {
        return m_bMultithreading;
    }

    public void setMultithreading(boolean m_bMultithreading) {
        this.m_bMultithreading = m_bMultithreading;
    }

    public boolean isLoadOriginalResults() {
        return m_bLoadOriginalResults;
    }

    public void setLoadOriginalResults(boolean m_bLoadOriginalResults) {
        this.m_bLoadOriginalResults = m_bLoadOriginalResults;
    }

    public boolean isCompile() {
        return m_bCompile;
    }

    public void setCompile(boolean m_bCompile) {
        this.m_bCompile = m_bCompile;
    }

    public int getMutantIntervalIni() {
        return m_nMutantIntervalIni;
    }

    public void setMutantIntervalIni(int m_nMutantIntervalIni) {
        this.m_nMutantIntervalIni = m_nMutantIntervalIni;
    }

    public int getMutantIntervalEnd() {
        return m_nMutantIntervalEnd;
    }

    public void setMutantIntervalEnd(int m_nMutantIntervalEnd) {
        this.m_nMutantIntervalEnd = m_nMutantIntervalEnd;
    }
    
    boolean isCleanLineEmpty() {
       return m_strCleanLine == null ? false : m_strCleanLine.isEmpty();
    }

    String getCleanLine() {
       return m_strCleanLine == null ? "" : m_strCleanLine;
    }

    public void setAppPath(String sourceProgramPath) {
       
        int nIndex;
        
        if(!sourceProgramPath.isEmpty())
        {
            nIndex = sourceProgramPath.lastIndexOf(File.separatorChar);

            if(nIndex != -1)
                strAppFolder = sourceProgramPath.substring(0, nIndex);
        }
        //Process the sourceProgram path to obtain the container folder
        this.strAppPath = sourceProgramPath;
    }   
    
    public String getAppFolder() {
        return strAppFolder;
    }

    public void setAppFolder(String strAppFolder) {
        this.strAppFolder = strAppFolder;
    }   
public int getnTotalFixedMutants() {
        return nTotalFixedMutants;
    }

    public void setnTotalFixedMutants(int nTotalFixedMutants) {
        this.nTotalFixedMutants = nTotalFixedMutants;
    }

    public boolean isbIsFixedMutants() {
        return bIsFixedMutants;
    }

    public void setbIsFixedMutants(boolean bIsFixedMutants) {
        this.bIsFixedMutants = bIsFixedMutants;
    }

    public boolean isbIsFixedTests() {
        return bIsFixedTests;
    }

    public void setbIsFixedTests(boolean bIsFixedTests) {
        this.bIsFixedTests = bIsFixedTests;
    }

    public int getnTotalFixedTests() {
        return nTotalFixedTests;
    }

    public void setnTotalFixedTests(int nTotalFixedTests) {
        this.nTotalFixedTests = nTotalFixedTests;
    }    
     void reset() {
        //TODO: Terminar esto
        bStopWhenFail = m_bCompile = m_bMultithreading = false;    
        m_bIsIntervalExec = m_bLoadOriginalResults = false;
        m_nMutantIntervalIni = m_nMutantIntervalEnd = -1;
        
        numThreads = nGeneratedMutants = nGeneratedTests = nInitInterval = nEndInterval = -1;
        reWriteMutants = bExecuteAllTests = bAskUser = bInterval = bAllTests = bMultiThreading = false;  
        
        m_strCleanLine = strTestSuiteFile = strMutantsPath = strAppName = strAppPath = "";
        strMutantsCompilationLine = strOriginalCompilationLine = strProjectName = "";  
        
        nTimeoutFactor = DEFAULT_TIMEOUT_FACTOR;
    }
     
    public String getParametersNeeded()
    {
        //TODO: Ayudar al usuario a saber que parametros le faltan por incluir
        String strRet;
        
        strRet = "";
        
        if (strAppPath == null || strAppPath.isEmpty())
            strRet += "| AppPath";
        return strRet;
    }

    public void setFixedMutantsCfg(int numFixedMutants, boolean useFixedMutants) {
 
        bIsFixedMutants = useFixedMutants;
        nTotalFixedMutants = numFixedMutants;
    }

    public void setFixedTestsCfg(int numFixedTests, boolean useFixedTests) {
        bIsFixedTests = useFixedTests;
        nTotalFixedTests = numFixedTests;
    }

    
}
