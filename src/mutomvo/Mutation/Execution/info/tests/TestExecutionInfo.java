/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.tests;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class TestExecutionInfo {
    
    int m_nTotalTests;
    int m_nInsertedtests;
    ArrayList<testInfo> m_testList;    
    double m_dTotalTime;
    EnumMutantState eGlobalMutantState;
    
    //tests statistics
    int nKillingTests;
    int nAliveTests;
    
    public TestExecutionInfo(int nTotalTests)
    {
        m_nTotalTests = nTotalTests;
        m_testList = new ArrayList<testInfo>();
        m_nInsertedtests=0;
        m_dTotalTime=0.0;
        eGlobalMutantState = EnumMutantState.eMUTANT_ALIVE;
        nKillingTests = nAliveTests=0;
    }
   
    public void addTest(int nIndexTest, String strResult, double dExecTime, EnumMutantState eState) {
        testInfo test;
        test = new testInfo(nIndexTest,strResult, dExecTime,eState);
        m_dTotalTime+=dExecTime;
        m_nInsertedtests++;
        m_testList.add(test);
        
        if(eState == EnumMutantState.eMUTANT_DEAD)
        {
            eGlobalMutantState = eState;
            nKillingTests++;
        }
        else
            nAliveTests++;        
    }

    public boolean equalRes(int nIndexTest, String strResult) {
        boolean bRet =false;
        testInfo testCheck;
        String strOriginal;
        testCheck = m_testList.get(nIndexTest);
        
        if(testCheck != null)
        {
            strOriginal = testCheck.getTestVerboseResult();
            bRet = strResult.equals(strOriginal);
            
            if(!bRet)
            {
                System.out.printf("Size: %d vs %d\n", strOriginal.length(), strResult.length());
                System.out.printf("Original: %s\n", createMd5(strOriginal));
                System.out.printf("Mutant: %s\n", createMd5(strResult));
            }
        }
        return bRet;
    }

    public double getTotalTime() {
        
        return m_dTotalTime;
    }

    public double getAverageTime()
    {
        double dRet;
        
        dRet = 0.0;
        if(m_nInsertedtests != 0 && m_dTotalTime != 0)
            dRet = m_dTotalTime / m_nInsertedtests;
        
        return dRet;
    }
    public double getTestTime(int i) {
        double dRet;
        testInfo testCheck;
                
        dRet = 0.0;
        if(i<m_nInsertedtests)
        {
            testCheck = m_testList.get(i); 
            dRet = testCheck.getTime();
        }    
        return dRet;
    }

    public int getNumberTests() {
        return m_nInsertedtests;
    }

    public testInfo getTest(int i) {
        testInfo testCheck = null;
                
        if(i<m_nInsertedtests)
        {
            testCheck = m_testList.get(i); 
            
        }    
        return testCheck;
    }
    public String createMd5(String strOriginal)
    {
        String strReturn = null;
        try {
            String original = strOriginal;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(original.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
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
    public String toStringCsv()
    {
        StringBuffer strTestExecution;
        String strLine;
        testInfo tInfo;
        
        strTestExecution = new StringBuffer("");   
        for(int i=0;i<m_testList.size();i++)
        {
            tInfo = m_testList.get(i);
            
            strLine = tInfo.toStringCsv();
            strTestExecution.append(strLine);
        }
        
        return strTestExecution.toString();
    }
    public String toStringTimeCsv()
    {
        StringBuffer strTestExecution;
        String strLine;
        testInfo tInfo;
        
        strTestExecution = new StringBuffer("");   
        for(int i=0;i<m_testList.size();i++)
        {
            tInfo = m_testList.get(i);
            
            strLine = tInfo.toStringTimeCsv();
            strTestExecution.append(strLine);
        }
        
        return strTestExecution.toString();
    }
    public EnumMutantState getMutantState() {
        return eGlobalMutantState;
    }
    
    public int getAliveTests() {
        return nAliveTests;
    }

    public void setAliveTests(int nAliveTests) {
        this.nAliveTests = nAliveTests;
    }

    public int getKillingTests() {
        return nKillingTests;
    }

    public void setKillingTests(int nKillingTests) {
        this.nKillingTests = nKillingTests;
    }
    
}
