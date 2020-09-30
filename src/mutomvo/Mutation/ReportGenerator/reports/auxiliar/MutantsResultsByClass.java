/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.auxiliar;

import java.util.LinkedList;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class MutantsResultsByClass {
    
    LinkedList<MutantResultsByOperator> mutOperators;
    EnumOperatorClass mutationClass;
    int nOperators;
    
    int nTotalTests;
    int nMutantsInserted;
    float fAvgKillerTest;
    float fAvgKilled;
    Long lAvgTimeToKill;
    
    MutantsResultsByClass(EnumOperatorClass mutationClass)
    {
        mutOperators = new LinkedList<MutantResultsByOperator>();
        this.mutationClass = mutationClass;
        nOperators=nMutantsInserted=0;
        fAvgKillerTest=fAvgKilled=0;
        lAvgTimeToKill = (long) 0;
    }
    public EnumOperatorClass getMutationClass()
    {
        return mutationClass;        
    }

    void addInfo(EnumMuOperation enumMuOperation, int nTotalTests, int nKillerTest, int nKillers, Long nTimeToKill) {
        MutantResultsByOperator partialOperator = getOperatorClass(enumMuOperation);
        //Search if exists the operator class
        if(partialOperator == null)
        {
            //If not exists, create it!
            partialOperator = createOperator(enumMuOperation,nTotalTests);
        }
        
        //insert the values into the corresponding class
        partialOperator.addInfo(enumMuOperation, nKillerTest, nKillers, nTimeToKill);
   
    }
    private MutantResultsByOperator getOperatorClass(EnumMuOperation enumMuOperation) {
        MutantResultsByOperator mutOperator;
        int nFound, nIndex;
        
        nFound = nIndex = 0;
        mutOperator = null;
        if(mutOperators.size()>0)
        {
            while(nIndex<mutOperators.size() && nFound==0)
            {
                mutOperator = mutOperators.get(nIndex);
                nIndex++;
                if(mutOperator.getMutationOperator() == enumMuOperation)
                {
                    nFound = 1;
                }
            }
            if(nFound ==0)
                mutOperator = null;
        }
        
        return mutOperator;
    }

    private MutantResultsByOperator createOperator(EnumMuOperation enumMuOperation, int nTotalTests) {
        MutantResultsByOperator mutOperator;
        
        mutOperator =  new MutantResultsByOperator(mutationClass,enumMuOperation);
        mutOperator.addTotalTests(nTotalTests);
        mutOperators.add(nOperators, mutOperator);
        nOperators++;
        
        return mutOperator;
    }

    void process() {
    
        float fAvgKillerTest,fAvgKilled;
        
        nMutantsInserted = 0;
        fAvgKillerTest =fAvgKilled= 0;
        
        if(mutOperators != null && mutOperators.size()>0)
        {
            for(int i = 0;i<mutOperators.size();i++)
            { 
                MutantResultsByOperator mutOperator = mutOperators.get(i);
                mutOperator.process();
                fAvgKilled += (float) mutOperator.getlKillers();
                fAvgKillerTest += (float)mutOperator.getlKillerTest();
                lAvgTimeToKill += mutOperator.getlAvgTime();
            }
            if(fAvgKilled>0)
                this.fAvgKilled = (float)(fAvgKilled/(float)mutOperators.size());
            if(fAvgKillerTest>0)
                this.fAvgKillerTest = (float)(fAvgKillerTest/(float)mutOperators.size());
            if(lAvgTimeToKill.intValue()>0)
                lAvgTimeToKill = lAvgTimeToKill/mutOperators.size();
        }
    }
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String strRet;
        
        if(mutOperators != null && mutOperators.size()>0)
        {
            for(int i = 0;i<mutOperators.size();i++)
            {
                MutantResultsByOperator mutOperator = mutOperators.get(i);
                strRet = mutOperator.toString();
                buffer.append(strRet);
            }
        }
        strRet = String.format("%.2f, %.2f, %d\n",fAvgKillerTest,fAvgKilled,lAvgTimeToKill.intValue());
        buffer.append(strRet);
        
        return buffer.toString();
    }

}
