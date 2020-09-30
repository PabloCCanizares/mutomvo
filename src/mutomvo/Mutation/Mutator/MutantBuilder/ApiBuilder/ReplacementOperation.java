/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ApiBuilder;

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
public class ReplacementOperation implements IAPIMutation {

    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();
        if (Operator.getOperatorType() == EnumOperator.eAPI) {

            MutatedOperator muOperator = new MutatedOperator(Operator);
            muOperator.mutate(Operator.getToken(), EnumMuOperation.eOREP);
            muList.add(muOperator);

        }
        return muList;
    }

}
