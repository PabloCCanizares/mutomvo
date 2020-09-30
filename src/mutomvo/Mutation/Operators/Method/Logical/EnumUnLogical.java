/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Logical;

/**
 *
 * @author user
 */

/**
 *
 * @author user
 */
public enum EnumUnLogical implements IEnumLogical
{
    eLOG_NONE, eLOG_INV;
    
    public EnumUnLogical fromString(String strVal)
    {
        EnumUnLogical eRET = EnumUnLogical.eLOG_NONE;
        
        if(strVal.contentEquals("~"))
           eRET = eLOG_INV; 
        
        return eRET;
    }
    
    @Override
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eLOG_INV:
                StringReturn = "~";
                break;            
        }
        return StringReturn;
    }

    @Override
    public String ToString() {
        return toString();
    }

    @Override
    public EnumLogicalTypes getType() {
        return EnumLogicalTypes.eLogicalType_Unary;
    }
}

