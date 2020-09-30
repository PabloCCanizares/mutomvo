/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Conditional;
import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.Operators.Method.EnumOperator;

/**
 *
 * @author user
 */
public class ConditionalOperator extends MutationOperator  {   
    
    boolean bIsUnary;
    public ConditionalOperator(String strTokenIn)
    {
        
        eOperatorType = EnumOperator.eCONDITIONAL;
        
        Token = strTokenIn;
        
        if(Token.equals("!"))
            bIsUnary =true;
        
    }    
}
