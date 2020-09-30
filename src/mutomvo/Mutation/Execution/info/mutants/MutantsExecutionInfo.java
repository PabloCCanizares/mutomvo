/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.mutants;

import java.util.ArrayList;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class MutantsExecutionInfo {
    
    int m_nTotalMutants;
    int m_nInsertedmutants;
    int m_nDeadMutants;
    int m_nAliveMutants;
    int m_nEquivalentMutants;
    int m_nDuplicatedMutants;
    float m_fmutationScore;
    ArrayList<mutantInfo> m_mutantList;

    private void initialize()
    {
        m_mutantList = new ArrayList<mutantInfo>();
        m_nDeadMutants = m_nAliveMutants = m_nInsertedmutants = m_nTotalMutants = 0;
        m_fmutationScore = (float) 0.0;
        m_nDuplicatedMutants=m_nEquivalentMutants=0;
    }
    public MutantsExecutionInfo()
    {
        initialize();
    }

    public MutantsExecutionInfo(int m_nMutants) {
        initialize();
        m_nTotalMutants = m_nMutants;
    }

    public void insert(int nIndexMutant, int nIndexKiller, TestExecutionInfo eTestsInfo, EnumMutantState enumMutantState, String strLastLine) 
    {        
        mutantInfo mInsert;        
        mInsert = new mutantInfo(nIndexMutant, nIndexKiller, eTestsInfo, enumMutantState, strLastLine);
        m_nInsertedmutants++;
        m_mutantList.add(mInsert);
        
        if(enumMutantState == EnumMutantState.eMUTANT_ALIVE)
            m_nAliveMutants++;
        else
        {
            m_nDeadMutants++;
        }
        m_fmutationScore = (float) m_nDeadMutants / m_nInsertedmutants;
    }

    public int getSize() {
        return m_nInsertedmutants;
    }

    
    public mutantInfo getMutant(int nIndex) {
        
        boolean bRet =false;
        mutantInfo mRet;
        int nSize;
        
        mRet = null;
        nSize = m_mutantList.size();
        for(int i=0;i<nSize &&!bRet;i++)
        {
            mRet = m_mutantList.get(i);
            if(mRet.getIndex() == nIndex)
                bRet =true;
        }
        if(!bRet)
            mRet =null;
            
        
        return mRet;
    }

    public void add(mutantInfo mInfo) {
       
        m_nInsertedmutants++;
        m_mutantList.add(mInfo);
        
        if(mInfo.getState() == EnumMutantState.eMUTANT_ALIVE)
            m_nAliveMutants++;
        else
        {
            m_nDeadMutants++;
        }
        m_fmutationScore = (float) m_nDeadMutants / m_nInsertedmutants;
    }

    public int getDeadMutants() {
        return m_nDeadMutants;
    }
    public int getTotalMutants() {
        return m_nInsertedmutants;
    }
    public float getMS()
    {
        return m_fmutationScore;
    }

    public int getTotalTests() {
        int nRet;
        
        if(m_mutantList != null && m_mutantList.size()>0)
            nRet = m_mutantList.get(0).getTotalTests();
        else
            nRet = 0;
        
        return nRet;
    }

    public mutantInfo getMutantByIndex(int i) {
        mutantInfo mInfo;
        
        mInfo=null;
        if(i<m_mutantList.size())
            mInfo = m_mutantList.get(i);
        
        return mInfo;
    }

    public int getEquivalentMutants() {
        return m_nEquivalentMutants;
    }

    public int getDuplicated() {
        return m_nDuplicatedMutants;
    }

    public void incEquivalents() {
        m_nEquivalentMutants++;
    }

    public void incDuplicated() {
        m_nDuplicatedMutants++;
    }
    
}
