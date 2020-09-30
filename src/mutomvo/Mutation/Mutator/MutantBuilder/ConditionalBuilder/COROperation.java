/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ConditionalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.Conditional.EnumConditional;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class COROperation implements IConditionalMutation {

    List<EnumConditional> ConditionalList;

    public COROperation() {
        ConditionalList = new ArrayList<EnumConditional>();

        for (EnumConditional eENUM : EnumConditional.values()) {
            ConditionalList.add(eENUM);
        }

    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        for (int i = 0; i < ConditionalList.size(); i++) {
            EnumConditional eEnum = ConditionalList.get(i);

            if (!eEnum.toString().equals(Operator.getToken()) && !Operator.isOperatorInsertion() && !Operator.isOperatorDeletion()) {
                MutatedOperator muOperator = new MutatedOperator(Operator);
                muOperator.mutate(eEnum.toString(), EnumMuOperation.eCOR);
                if (!eEnum.toString().isEmpty() && eEnum != eEnum.eCOND_NEG) {
                    muList.add(muOperator);
                }
            }
        }

        return muList;
    }
}
