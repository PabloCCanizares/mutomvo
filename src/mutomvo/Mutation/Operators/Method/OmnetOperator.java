/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class OmnetOperator extends MutationOperator 
{
    public OmnetOperator(String strTokenIn)
    {
        
        eOperatorType = EnumOperator.eAPI;
        
        Token = strTokenIn;
    }
    public OmnetOperator(String strTokenIn, IASTNode node)
    {
        oNode = node;
        eOperatorType = EnumOperator.eAPI;
        
        Token = strTokenIn;
    }

    public IASTNode getNode() {
        return oNode;
    }
}
