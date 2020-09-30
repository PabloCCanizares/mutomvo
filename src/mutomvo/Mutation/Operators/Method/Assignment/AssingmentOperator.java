/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Assignment;

import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author PAblo C. Cañizares
 */

public class AssingmentOperator extends MutationOperator 
{

    public AssingmentOperator(String strTokenIn)
    {        
        eOperatorType = EnumOperator.eASSIGMENT;
        
        Token = strTokenIn;
    }
}