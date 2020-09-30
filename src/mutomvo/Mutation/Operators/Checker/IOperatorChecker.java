/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Checker;

import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author usuario_local
 */
public interface IOperatorChecker 
{
    public boolean doCheck(MutationOperator operator, String currentLine, int nIndex); 
}
