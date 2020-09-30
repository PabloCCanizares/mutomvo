/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;

import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.Method.Logical.EnumLogicalTypes;
import mutomvo.Mutation.Operators.Method.Logical.EnumUnLogical;
import mutomvo.Mutation.Operators.Method.Logical.IEnumLogical;
import mutomvo.Mutation.Operators.Method.Logical.LogicalOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
class LODOperation implements ILogicalMutation {

    private final ArrayList<IEnumLogical> LogicalList;

    public LODOperation() {

        LogicalList = new ArrayList<IEnumLogical>();

        for (EnumUnLogical eENUM : EnumUnLogical.values()) {
            if (eENUM.getType() == EnumLogicalTypes.eLogicalType_Unary) {
                LogicalList.add(eENUM);
            }
        }
    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        if (Operator.getOperatorType() == EnumOperator.eLOGICAL) {
            LogicalOperator oLogicalOp = (LogicalOperator) Operator;

            if (oLogicalOp != null && oLogicalOp.getEnum().getType() == EnumLogicalTypes.eLogicalType_Unary && Operator.isOperatorDeletion()) {

                MutatedOperator muOperator = new MutatedOperator(Operator);
                muOperator.mutate("", EnumMuOperation.eLOD);
                muList.add(muOperator);
            }
        }

        return muList;
    }
}
