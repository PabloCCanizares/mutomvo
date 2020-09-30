/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Arithmetic;

import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.Operators.Method.EnumOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ArithmeticOperator extends MutationOperator {

    IEnumArithmetic enumType;

    public IEnumArithmetic getEnum() {
        return enumType;
    }

    public ArithmeticOperator(String strTokenIn, EnumARTypes eType) {
        eOperatorType = EnumOperator.eARITHMETIC;
        Token = strTokenIn;

        switch (eType) {
            case eARType_BIN:
                EnumBinArithmetic eEnum = EnumBinArithmetic.eAR_NONE;
                enumType = eEnum.fromString(Token);
                break;
            case eARType_SHORT:
                EnumShortArithmetic eEnumS = EnumShortArithmetic.eAR_NONE;
                enumType = eEnumS.fromString(Token);
                break;
            case eARType_Unary:
                EnumUnArithmetic eEnumU = EnumUnArithmetic.eAR_NONE;
                enumType = eEnumU.fromString(Token);
                break;
        }
    }
}
