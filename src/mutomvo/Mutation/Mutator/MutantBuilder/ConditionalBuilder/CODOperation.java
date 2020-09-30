/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ConditionalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class CODOperation implements IConditionalMutation {

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        if (Operator.getOperatorType() == EnumOperator.eCONDITIONAL && Operator.isOperatorDeletion()) {
            MutatedOperator muOperator = new MutatedOperator(Operator);
            muOperator.mutate("", EnumMuOperation.eCOD);
            muList.add(muOperator);
        }

        return muList;
    }

}
