/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.Operators.Method.Logical.EnumUnLogical;

/**
 *
 * @author Pablo C. Cañizares
 */
public class LOIOperation implements ILogicalMutation {

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        if (Operator.getOperatorType() == EnumOperator.eLOGICAL && Operator.isOperatorInsertion()) {
            MutatedOperator muOperator = new MutatedOperator(Operator);
            muOperator.mutate(EnumUnLogical.eLOG_INV.toString(), EnumMuOperation.eLOI);
            muList.add(muOperator);
        }

        return muList;
    }
}
