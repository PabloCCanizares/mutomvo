/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Relational;

import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class RelationalOperator extends MutationOperator 
{
    public RelationalOperator(String strTokenIn)
    {
        
        eOperatorType = EnumOperator.eRELATIONAL;
        
        Token = strTokenIn;
    }
}
