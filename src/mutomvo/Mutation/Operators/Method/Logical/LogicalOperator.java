/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Logical;

import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class LogicalOperator extends MutationOperator{
    
    IEnumLogical enumType;
    
    public LogicalOperator(String strTokenIn, EnumLogicalTypes eType)
    {        
        eOperatorType = EnumOperator.eLOGICAL;       
        Token = strTokenIn;    

        switch(eType)
        {
            case eLogicalType_BIN:
                EnumBinLogical eEnum = EnumBinLogical.eLOG_NONE;
                enumType = eEnum.fromString(Token);
                break;             
            case eLogicalType_Unary:
                EnumUnLogical eEnumU = EnumUnLogical.eLOG_NONE;       
                enumType = eEnumU.fromString(Token);
                break;                
        }
    }

    public IEnumLogical getEnum(){return enumType;};
}
