/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.auxiliar;

import java.util.LinkedList;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class GroupedMutExecInfo {
    
    LinkedList<MutantsResultsByClass> mutOpClasses;
    
    int nClasses;
    public GroupedMutExecInfo()
    {
        mutOpClasses = new LinkedList<MutantsResultsByClass>();
        nClasses=0;
    }
    boolean groupMutantExecutionInfo(MutantsExecutionInfo mInfo)
    {
        boolean bRet = false;
        
        return bRet;
    }

    public void addInfo(EnumOperatorClass enumOperatorClass, EnumMuOperation enumMuOperation, int nTotalTests, int nKillerTest, int nKillers, Long nTimeToKill) {
          
        if(enumOperatorClass != null && enumMuOperation!=null)
        {
            MutantsResultsByClass partialClass = getOperatorClass(enumOperatorClass);
            //Search if exists the operator class
            if(partialClass == null)
            {
                //If not exists, create it!
                partialClass = createClass(enumOperatorClass);
            }

            //insert the values into the corresponding class
            partialClass.addInfo(enumMuOperation, nTotalTests, nKillerTest,nKillers, nTimeToKill);
        }
    }

    private MutantsResultsByClass getOperatorClass(EnumOperatorClass enumOperatorClass) {
        MutantsResultsByClass mutClassOperator;
        int nFound, nIndex;
        
        nFound = nIndex = 0;
        mutClassOperator = null;
        if(mutOpClasses.size()>0)
        {
            while(nIndex<mutOpClasses.size() && nFound==0)
            {
                mutClassOperator = mutOpClasses.get(nIndex);
                nIndex++;
                if(mutClassOperator.getMutationClass() == enumOperatorClass)
                {
                    nFound = 1;
                }
            }
            if(nFound ==0)
                mutClassOperator = null;
        }
        
        return mutClassOperator;
    }

    private MutantsResultsByClass createClass(EnumOperatorClass enumOperatorClass) {
        MutantsResultsByClass mutClassOperator;
        
        mutClassOperator =  new MutantsResultsByClass(enumOperatorClass);
        mutOpClasses.add(nClasses, mutClassOperator);
        nClasses++;
        return mutClassOperator;
    }

    public void processEntries() {
        if(mutOpClasses != null && mutOpClasses.size()>0)
        {
            for(int i = 0;i<mutOpClasses.size();i++)
            {
                MutantsResultsByClass mutClass = mutOpClasses.get(i);
                mutClass.process();
            }
        }
    }
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String strRet;
        if(mutOpClasses != null && mutOpClasses.size()>0)
        {
            for(int i = 0;i<mutOpClasses.size();i++)
            {
                MutantsResultsByClass mutClass = mutOpClasses.get(i);
                strRet = mutClass.toString();
                buffer.append(strRet);
            }
        }        
        
        return buffer.toString();
    }
}
