/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.auxiliars;
import mutomvo.Mutation.Execution.JavaMuTeXec;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;
import mutomvo.Mutation.Execution.info.tests.testInfo;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class MTJavaeXecThread extends JavaMuTeXec implements Runnable{
        
    int nMutantStart;
    int nMutantEnd;
    int nProcessedMutants;
    int nTimeoutFactor;
    
    public MTJavaeXecThread ()
    {
        super();
        nProcessedMutants=0;
        
    }
    
    public void configThread(int nId, int nMutantStart, int nMutantEnd, int nTimeoutFactor, TestExecutionInfo testOriginalInfo)
    {
        this.nId = nId;
        this.nMutantStart = nMutantStart;
        this.nMutantEnd = nMutantEnd;
        this.m_testOriginalInfo = testOriginalInfo;
        
        //TODO: Esta funcion cambiarlar por la que recibe la clase config.
        nTimeoutFactor = 6;
    }
    public boolean isFinished()
    {
        return (super.m_eState == EnumMutexecState.eMUT_EXE_FIN_OK);
    }
    @Override
    public void run() 
    {
        execMutants();
    }
    private void execMutants()
    {
        EnumMutantState eState;
        m_bRunning = true;
        System.out.printf("<%d> Starting execution, thread %d - %d\n", nId,nMutantStart,nMutantEnd);

        if(nId ==3)
            nId=3;
        
        for(int i=nMutantStart;i<=nMutantEnd;i++)
        {

           System.out.printf("<%d> Executing mutant %d\n", nId, nMutantStart); 
           eState = execMutant(i, super.m_nTotalTests);
                                   
            if (eState != EnumMutantState.eMUTANT_ALIVE) {
                m_nDeadMutants++;
            }
            else
                m_nAliveMutants++;
             
            nProcessedMutants++;
        }
        System.out.printf("<%d> Execution finished!\n", nId);
        
        
        m_eState = EnumMutexecState.eMUT_EXE_FIN_OK;
        m_bRunning = false;
    }

    public int getMutantsProcessed() {
        
        return nProcessedMutants;
    }
    
    public int getAliveMutants()
    {
        return m_nAliveMutants;
    }
    public int getDeadMutants()
    {
        return m_nDeadMutants;
    }
}
