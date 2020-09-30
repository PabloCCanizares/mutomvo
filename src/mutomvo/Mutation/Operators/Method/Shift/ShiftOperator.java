/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Shift;

import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ShiftOperator extends MutationOperator 
{
    public ShiftOperator(String strTokenIn)
    {
        
        eOperatorType = EnumOperator.eSHIFT;
        
        Token = strTokenIn;
    }
}
