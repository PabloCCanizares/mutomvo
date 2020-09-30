/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.AssigmentBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.Assignment.EnumAssignment;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author user
 */
public class ASOROperation implements IAssignmentMutation {
    List<EnumAssignment> AssignmentList;

    public ASOROperation() 
    {
        AssignmentList = new ArrayList<EnumAssignment>();

        for (EnumAssignment eENUM : EnumAssignment.values()) 
        {
           AssignmentList.add(eENUM);
        }
        
    }

    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        for (int i = 0; i < AssignmentList.size(); i++) {
            EnumAssignment eEnum = AssignmentList.get(i);
            
            if(!eEnum.toString().equals(Operator.getToken()))
            {
                MutatedOperator muOperator = new MutatedOperator(Operator);
                muOperator.mutate(eEnum.toString(), EnumMuOperation.eASOR);
                if(!eEnum.toString().isEmpty())
                    muList.add(muOperator);
            }
        }

        return muList;
    } 
}
