/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Arithmetic;

/**
 *
 * @author Pablo C. Cañizares 
 */
public enum EnumUnArithmetic implements IEnumArithmetic{

    eAR_NONE,eAR_ADDUN, eAR_SUBUN, eAR_NOTUN;

    public EnumARTypes getType(){return EnumARTypes.eARType_Unary;};
    
    public EnumUnArithmetic fromString(String strVal)
    {
        EnumUnArithmetic eRET = null;
        
        if(strVal.contentEquals("+"))
           eRET = eAR_ADDUN; 
        else if(strVal.contentEquals("-"))
           eRET = eAR_SUBUN;   
        else if(strVal.contentEquals("!"))
           eRET = eAR_NOTUN;           
               
        return eRET;
    }
    @Override
    public String ToString() {
        String StringReturn = "";
        switch (this) {
            case eAR_ADDUN:
                StringReturn = "+";
                break;
            case eAR_SUBUN:
                StringReturn = "-";
                break;
            case eAR_NOTUN:
                StringReturn = "!";
                break;                
        }
        return StringReturn;
    }

    public String toString(EnumUnArithmetic eEnum) {
        String StringReturn = "";
        switch (eEnum) {
            case eAR_ADDUN:
                StringReturn = "+";
                break;
            case eAR_SUBUN:
                StringReturn = "-";
                break;
        }
        return StringReturn;
    }

}
