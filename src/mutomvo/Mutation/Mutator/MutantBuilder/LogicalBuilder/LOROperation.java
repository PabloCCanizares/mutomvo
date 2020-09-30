/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.Method.Logical.EnumBinLogical;
import mutomvo.Mutation.Operators.Method.Logical.EnumLogicalTypes;
import mutomvo.Mutation.Operators.Method.Logical.LogicalOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
class LOROperation implements ILogicalMutation {

    private final ArrayList<EnumBinLogical> LogicalList;

    public LOROperation() {

        LogicalList = new ArrayList<EnumBinLogical>();

        for (EnumBinLogical eENUM : EnumBinLogical.values()) {
            if (eENUM.getType() == EnumLogicalTypes.eLogicalType_BIN) {
                LogicalList.add(eENUM);
            }
        }
    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        //TODO:: Hay que tener en cuenta que algunos operandos
        //no pueden ser combinados con otros i =+4; -> solo por -
        if (Operator.getOperatorType() == EnumOperator.eLOGICAL && !Operator.isOperatorInsertion() && !Operator.isOperatorDeletion()) {
            LogicalOperator oArithOp = (LogicalOperator) Operator;

            if (oArithOp.getEnum().getType() == EnumLogicalTypes.eLogicalType_BIN) {

                for (int i = 0; i < LogicalList.size(); i++) {
                    EnumBinLogical eEnum = LogicalList.get(i);

                    if (!eEnum.toString().equals(Operator.getToken())) {
                        MutatedOperator muOperator = new MutatedOperator(Operator);
                        muOperator.mutate(eEnum.toString(), EnumMuOperation.eLOR);
                        if (!eEnum.toString().isEmpty()) {
                            muList.add(muOperator);
                        }
                    }
                }
            }
        }

        return muList;
    }

}
