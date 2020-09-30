/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Checker;

import mutomvo.Mutation.Operators.Method.Arithmetic.EnumBinArithmetic;
import mutomvo.Mutation.Operators.Method.Assignment.EnumAssignment;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 * TODO: Creo que esto se va fuera
 *
 * @author usuario_local
 */
class OperatorCheckerAR implements IOperatorChecker {

    public OperatorCheckerAR() {
    }

    @Override
    public boolean doCheck(MutationOperator operator, String currentLine, int nIndex) {
        boolean bRet = false;

        String strNext = "";
        strNext = strNext + currentLine.charAt(nIndex + 1);

        EnumBinArithmetic eArValue = EnumBinArithmetic.eAR_NONE;
        EnumAssignment eAsValue = EnumAssignment.eAS_NONE;

        eArValue = eArValue.fromString(operator.getToken());
        eAsValue = eAsValue.fromString(operator.getToken() + strNext);

        switch (eArValue) {
            case eAR_ADD:   // Si no se trata de ++ o +=

                if (eArValue.fromString(strNext) == EnumBinArithmetic.eAR_ADD
                        || eAsValue == EnumAssignment.eAS_ADD_EQ) {
                    nIndex++;
                    bRet = true;//reconversion
                }
                break;
            case eAR_SUB:
                break;

            //Estas tres comparten método:
            case eAR_DIV:
            case eAR_MOD:
            case eAR_MUL:

                break;

        }

        return bRet;
    }
}
