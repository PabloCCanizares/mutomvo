/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ShiftBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.Conditional.EnumConditional;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.Logical.EnumUnLogical;
import mutomvo.Mutation.Operators.Method.Shift.EnumShift;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class SfOROperation implements IShiftMutation {

    List<EnumShift> ShiftList;

    public SfOROperation() {
        ShiftList = new ArrayList<EnumShift>();

        for (EnumShift eENUM : EnumShift.values()) {
            ShiftList.add(eENUM);
        }

    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {

        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        for (int i = 0; i < ShiftList.size(); i++) {
            EnumShift eEnum = ShiftList.get(i);

            if (!eEnum.toString().equals(Operator.getToken()) && !Operator.isOperatorInsertion() && !Operator.isOperatorDeletion()) {
                MutatedOperator muOperator = new MutatedOperator(Operator);
                muOperator.mutate(eEnum.toString(), EnumMuOperation.eCOR);
                if (!eEnum.toString().isEmpty()) {
                    muList.add(muOperator);
                }
            }
        }

        return muList;
    }
}
