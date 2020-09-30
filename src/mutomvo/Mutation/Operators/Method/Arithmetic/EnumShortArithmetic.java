/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Arithmetic;

/**
 *
 * @author Pablo C. Cañizares 
 */
public enum EnumShortArithmetic implements IEnumArithmetic{

    eAR_NONE, eAR_ADD_ADD, eAR_SUB_SUB;

    public EnumARTypes getType(){return EnumARTypes.eARType_SHORT;};
    
    public EnumShortArithmetic fromString(String strVal)
    {
        EnumShortArithmetic eRET = null;
        
        if(strVal.contentEquals("++"))
           eRET = eAR_ADD_ADD; 
        else if(strVal.contentEquals("--"))
           eRET = eAR_SUB_SUB;    
               
        return eRET;
    }
    public String ToString() {
        String StringReturn = "";
        switch (this) {
            case eAR_ADD_ADD:
                StringReturn = "++";
                break;
            case eAR_SUB_SUB:
                StringReturn = "--";
                break;
        }
        return StringReturn;
    }

    public String toString(EnumShortArithmetic eEnum) {
        String StringReturn = "";
        switch (eEnum) {
            case eAR_ADD_ADD:
                StringReturn = "++";
                break;
            case eAR_SUB_SUB:
                StringReturn = "--";
                break;

        }
        return StringReturn;
    }

}
