/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.mutants;

import java.util.LinkedList;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;
import mutomvo.Mutation.Execution.info.tests.TestExecutionInfo;
import mutomvo.Mutation.Execution.info.tests.testInfo;
import mutomvo.Mutation.Operators.Method.EnumOperator;


/**
 *
 * @author Pablo C. Cañizares 
 */
public class mutantInfo 
{
    int nNumber;
    int nLine;    
    int nTestKiller; //Number of test that kills the mutant
    
    EnumMutantState eState; //State of the mutant Dead=0, Alive =1
    String strFullDescription; //Brief description: type of mutant, line of code ...    
    String strMutatedToken;
    TestExecutionInfo testInfo;
    
    EnumMutantState mState;             // Dead or Alive
    EnumOperatorClass mutationClass;    //Class of mutation operator <General, mpi, simcan, omnet>
    EnumMuOperation mutationOperator;   //Mutation operator
    EnumOperator generalMutationClass;   //Only applies to the general mutation operators <Arithmethic, relational, ...>
    
    //Equivalent & duplicated info
    boolean bIsEquivalent;
    boolean bIsDuplicated;
    int nRootDupId;
    
    //subsuming info
    boolean bIsSubsumed;
    boolean bIsSubsuming;
    LinkedList<mutantInfo> m_subsumedList;
    LinkedList<mutantInfo> m_subsumingList;
    
    public mutantInfo(int nIndexMutant, String strDesc)
    {
        nNumber = nIndexMutant;
        strFullDescription = strDesc;
        bIsEquivalent=bIsDuplicated=false;
        nRootDupId=-1;
        bIsSubsumed=bIsSubsuming=false;        
        m_subsumedList = new LinkedList<mutantInfo>();
        m_subsumingList = new LinkedList<mutantInfo>();
    }
    public mutantInfo(int nIndexMutant, int nIndexKiller, TestExecutionInfo TestsInfoIn, EnumMutantState enumMutantState, String strDesc) {
        
        nNumber=nIndexMutant;
        nTestKiller = nIndexKiller;
        eState = enumMutantState;
        strFullDescription = strDesc;
        testInfo = TestsInfoIn;
                
        mState = testInfo.getMutantState();
        bIsEquivalent = bIsDuplicated=false;
        nRootDupId=-1;
        bIsSubsumed=bIsSubsuming=false;
        
        m_subsumedList= new LinkedList<mutantInfo>();
        m_subsumingList = new LinkedList<mutantInfo>();
        
        //Parse the description
        parseDescription(strFullDescription);
    }
    public EnumOperatorClass getMutationClass()
    {
        return mutationClass;        
    }
    public String getMutationClassString()
    {
        String strRet="";
        
        if(mutationClass != null)
            strRet = mutationClass.toString();
            
        return strRet;            
    }
    public EnumMuOperation getMutationOperator()
    {
        return mutationOperator;        
    }
    public void reParseOperator()
    {
        //Re-parse the value taking into account the strFullDescription
        
        //In this case, it is neccesary to extract the general operators
    }
    public String ToStringReport()
    {
        String strRet;
        
        strRet = "";
        //if(mutationClass != null && mutationOperator!= null)
        //    strRet = String.format(" [%d] - Class: %s , Operation: %s , Line: %d , Token: %s\n", nNumber, mutationClass.toString(), mutationOperator.toString(), this.nLine, this.strMutatedToken);               
      
        if(mutationClass != null && mutationOperator!= null)
        {
            if(generalMutationClass == null)
            {                        
                if (eState == EnumMutantState.eMUTANT_ALIVE)
                    strRet = String.format(">[%d][E:%b,D:%b,R:%d] Alive! Class: %s , Operation: %s , Line: %d , Token: %s \n", nNumber, bIsEquivalent, bIsDuplicated, nRootDupId,mutationClass.toString(), mutationOperator.toString(), this.nLine, this.strMutatedToken);
                else
                    strRet = String.format(">[%d][E:%b,D:%b,R:%d] Dead! killed by test %d | Class: %s , Operation: %s , Line: %d , Token: %s\n", nNumber, bIsEquivalent, bIsDuplicated, nRootDupId,nTestKiller, mutationClass.toString(), mutationOperator.toString(), this.nLine, this.strMutatedToken);
            }
            else
            {
                if (eState == EnumMutantState.eMUTANT_ALIVE)
                    strRet = String.format(">[%d][E:%b,D:%b,R:%d] Alive! Class: %s , Subclass: %s, Operation: %s , Line: %d , Token: %s\n", nNumber,  bIsEquivalent, bIsDuplicated, nRootDupId,mutationClass.toString(), generalMutationClass.toString(), mutationOperator.toString(), this.nLine, this.strMutatedToken);
                else
                    strRet = String.format(">[%d][E:%b,D:%b,R:%d] Dead! killed by test %d | Class: %s , Subclass: %s, Operation: %s , Line: %d , Token: %s\n", nNumber,  bIsEquivalent, bIsDuplicated, nRootDupId,nTestKiller, mutationClass.toString(), generalMutationClass.toString(), mutationOperator.toString(), this.nLine, this.strMutatedToken);
            
            }
        }
        return strRet;
    }
    public String ToString()
    {
        String strRet, strTestExecution,strLine;
        testInfo tInfo;
        strRet = "";
        
        strTestExecution= "";
        if(testInfo != null)
        {
            for(int i=0;i<testInfo.getNumberTests();i++)
            {
                tInfo = testInfo.getTest(i);

                strLine = String.format("%d,%f,%s|", tInfo.getNumber(), tInfo.getTime(),tInfo.getResult());
                strTestExecution = strTestExecution + strLine;
            }
        }
        
        if (eState == EnumMutantState.eMUTANT_ALIVE)
            strRet = String.format(">[%d] Alive!                | %s | Tests: <%s>", nNumber, strFullDescription,strTestExecution);
        else
            strRet = String.format(">[%d] Dead! killed by test %d | %s | Tests: <%s>", nNumber, nTestKiller, strFullDescription,strTestExecution);
                        
        return strRet;
    }
    public String toStringPlain()
    {
        StringBuffer strTestExecution;
        String strRet,strLine;
        testInfo tInfo;
        strRet = "";
        
        if(testInfo != null)
        {
            strTestExecution = new StringBuffer("");
        
            for(int i=0;i<testInfo.getNumberTests();i++)
            {
                tInfo = testInfo.getTest(i);

                strLine = String.format("%d,%f,%s,\n", tInfo.getNumber(), tInfo.getTime(),tInfo.getResult());
                strTestExecution.append(strLine);
            }

            if (eState == EnumMutantState.eMUTANT_ALIVE)
                //strRet = String.format("1,%s,%s,%s,\n",mutationClass.toString(),mutationOperator.toString(),strDescription );
                strRet = String.format("1,%d,%s,\n",testInfo.getNumberTests(),strFullDescription );
            else
                strRet = String.format("0,%d,%s,\n",testInfo.getNumberTests(),strFullDescription );

            strRet = strRet + strTestExecution.toString();
        }   
        
        return strRet;
    }

    public String getName() {
        String strName;
        
        strName = String.format("mutant_%04d",nNumber);
        
        return strName;
    }

    public int getIndex() {
        return nNumber;
    }

    private void parseDescription(String strDesc) {
        String strAux, strLine, strClass, strSubClass;        
        String[] splitElems;
        
        strAux = strDesc;
        splitElems = strAux.split("\\|");
        
        if(splitElems.length == 4)
        {
            //Parse the line number.
            strLine = splitElems[0];
            strLine = strLine.replace("//Line:", "");
            strLine = strLine.trim();
            nLine = Integer.parseInt(strLine);
            
            //Get the Token (mutated line)
            strMutatedToken = splitElems[1];
            
            //Get the class
            strLine = strLine = splitElems[2];
            strLine = strLine.replace("Class: ", "");
            strLine = strLine.trim();
            if(strLine != null)
            try
            {        
                mutationClass = EnumOperatorClass.valueOf(strLine);
            }catch(IllegalArgumentException e)
            {
                mutationClass = EnumOperatorClass.eGENERALCLASS;
                System.out.printf("Exception creating mtation class, data: %s", strLine);
            }
            
            //Get the subclass
            strLine = strLine = splitElems[3];
            strLine = strLine.replace("Op: ", "");
            strLine = strLine.trim();
            mutationOperator = EnumMuOperation.valueOf(strLine);
        }
        //This is the general-operators case.
        else if(splitElems.length == 3)
        {
            
            //Parse the line number.
            strLine = splitElems[0];
            strLine = strLine.replace("//Line:", "");
            strLine = strLine.trim();
            nLine = Integer.parseInt(strLine);
            
            //Parse the token.
            strMutatedToken = splitElems[1];
            strMutatedToken = strMutatedToken.replace("//Token:", "");
            strMutatedToken = strMutatedToken.trim();
            
            //Get the subclass
            strLine = splitElems[2];
            strLine = strLine.replace("Op: ", "");
            strLine = strLine.trim();
            mutationOperator = EnumMuOperation.valueOf(strLine);
            
            
            //Refill the class.
            if(mutationOperator != null)
            {
                mutationClass = EnumOperatorClass.eGENERALCLASS;
                if(mutationOperator == EnumMuOperation.eAODs || mutationOperator == EnumMuOperation.eAODu ||
                   mutationOperator == EnumMuOperation.eAOIu || mutationOperator == EnumMuOperation.eAORb ||
                   mutationOperator == EnumMuOperation.eAORs || mutationOperator == EnumMuOperation.eAORu)
                {
                    generalMutationClass = EnumOperator.eARITHMETIC;
                }
                else if(mutationOperator == EnumMuOperation.eCOR)
                {
                    generalMutationClass = EnumOperator.eCONDITIONAL;
                }
                else if(mutationOperator == EnumMuOperation.eROR)
                {
                    generalMutationClass = EnumOperator.eRELATIONAL;
                }
                else if (mutationOperator == EnumMuOperation.eASOR)
                {
                    generalMutationClass = EnumOperator.eSHIFT;
                }                
                
            }
        }
        else
        {
            System.out.printf("parseDescription - Error parsing mutant info!");
        }
    }

    EnumMutantState getState() {
        return mState;
    }
    public boolean isAlive()
    {
        //TODO: Arreglar esto ...
        return (mState == EnumMutantState.eMUTANT_ALIVE && eState == EnumMutantState.eMUTANT_ALIVE);
    }
    public boolean isDuplicated()
    {
        return bIsDuplicated;
    }
    public boolean isEquivalent()
    {
        return bIsEquivalent;
    }    
    public int getTotalTests() {
        int nRet;
        
        if(testInfo != null)
            nRet = testInfo.getNumberTests();
        else
            nRet = 0;
        
        return nRet;
    }

    public int getTestTime(int i) {
        int nRet;
        
        if(testInfo != null)
            nRet = (int) testInfo.getTestTime(i);
        else
            nRet = 0;
        
        return nRet;
    }

    public double getTestsAverage() {
        double dRet;
        
        if(testInfo!=null)
            dRet = testInfo.getAverageTime();
        else
            dRet = 0;
        
        return dRet;
    }

    public boolean isAlive(int j) {
        boolean bRet;
        testInfo tInfo;
        bRet = false;

        if(testInfo!=null)
        {
            tInfo = testInfo.getTest(j);
            if(tInfo != null)
                bRet = tInfo.isAlive();
        }

        
        return bRet;
    }

    public void setMutationOperator(EnumMuOperation enumMuOperation) {
        this.mutationOperator = enumMuOperation;
    }

    public void setOperatorClass(EnumOperatorClass enumOperatorClass) {
        this.mutationClass = enumOperatorClass;
    }

    public void setIndex(int nIndex) {
        this.nNumber = nIndex;
    }

    public void setLine(int lineNumber) {
        this.nLine = lineNumber;
    }

    public void setToken(String token) {
        this.strMutatedToken = token;
    }

    public void setEquivalent(boolean b) {
        bIsEquivalent =b;
    }

    public void setDuplicated(boolean b, int nEquivalent) {
        bIsDuplicated=b;
        nRootDupId=nEquivalent;
    }

    public void setSubsumed(boolean b) {
        bIsSubsumed=true;
    }

    public void setSubsuming(boolean b) {
        bIsSubsuming = true;
    }
    
    public void insertSubsumedMutant(mutantInfo mInfoAux) 
    {
        if(mInfoAux!=null)
        {
            m_subsumedList.add(mInfoAux);
        }
    }

    public boolean isSubsumed() {
        return bIsSubsumed;
    }

    public int getKillingTests()
    {
        int nRet;
        
        nRet = 0;
        if(testInfo!=null)
        {
            nRet = testInfo.getKillingTests();
        }
        return nRet;
    }
    
    public int getAliveTests()
    {
        int nRet;
        
        nRet = 0;
        if(testInfo!=null)
        {
            nRet = testInfo.getAliveTests();
        }
        return nRet;
    }    
    public int getSubsumedMutants()
    {
        return m_subsumedList.size();
    }
        public int getSubsumingMutants()
    {
        return m_subsumingList.size();
    }
    public String getSubsumedMutantsToString()
    {
        String strRet;
        mutantInfo mInfo;
        
        strRet = "[ ";
        for(int i=0;i<m_subsumedList.size();i++)
        {
            mInfo = m_subsumedList.get(i);
            if(i+1<m_subsumedList.size())
                strRet+= String.format(" %d,", mInfo.getIndex());
            else
                strRet+= String.format(" %d", mInfo.getIndex());
            
        }
        strRet += " ]";
        
        return strRet;
    }

    public void setSubsumingMutant(mutantInfo mInfoActual) {
        m_subsumingList.add(mInfoActual);
    }
    public String getSubsumingMutantsToString()
    {
        String strRet;
        mutantInfo mInfo;
        
        strRet = "[ ";
        for(int i=0;i<m_subsumingList.size();i++)
        {
            mInfo = m_subsumingList.get(i);
            if(i+1<m_subsumingList.size())
                strRet+= String.format(" %d,", mInfo.getIndex());
            else
                strRet+= String.format(" %d", mInfo.getIndex());
            
        }
        strRet += " ]";
        
        return strRet;
    }
}