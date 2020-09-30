/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Logical;

/**
 *
 * @author Pablo C. Cañizares
 */
public enum EnumBinLogical implements IEnumLogical
{
    eLOG_NONE(""), eLOG_AND("&"), eLOG_OR("|"), eLOG_EL("^");
    
    private final EnumLogicalTypes eType;
    private String strChar;
    
    EnumBinLogical()
    {
        this.eType = EnumLogicalTypes.eLogicalType_BIN; 
    }
    
    EnumBinLogical(String strChar) {
        this.strChar = strChar;
        this.eType = EnumLogicalTypes.eLogicalType_BIN;  
    }
    
    public EnumBinLogical fromString(String strVal)
    {
        EnumBinLogical eRET;
        
        if(strVal.contentEquals("&"))
           eRET = eLOG_AND; 
        else if (strVal.contentEquals("|"))
           eRET = eLOG_OR; 
        else if (strVal.contentEquals("^"))
           eRET = eLOG_EL;     
        else
           eRET = eLOG_NONE;
        
        return eRET;
    }
    
    @Override
    public String toString() {
        String StringReturn;
        
        switch (this) {
            case eLOG_AND:
                StringReturn = "&";
                break;     
            case eLOG_OR:
                StringReturn = "|";
                break;   
            case eLOG_EL:
                StringReturn = "^";
                break; 
            default:
                StringReturn = "";
        }
        return StringReturn;
    }

    @Override
    public String ToString() {
        return toString();
    }

    @Override
    public EnumLogicalTypes getType() {
        return eType;
    }
}

