/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method;

/**
 *
 * @author Pablo C. Cañizares
 */
public enum EnumOperator {

    eNONE, eARITHMETIC, eRELATIONAL, eCONDITIONAL, eSHIFT,
    eLOGICAL, eASSIGMENT, eAPI;

    @Override
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eARITHMETIC:
                StringReturn = "eARITHMETIC";
                break;
            case eRELATIONAL:
                StringReturn = "eRELATIONAL";
                break;
            case eCONDITIONAL:
                StringReturn = "eCONDITIONAL";
                break;
            case eSHIFT:
                StringReturn = "eSHIFT";
                break;
            case eLOGICAL:
                StringReturn = "eLOGICAL";
                break;
            case eASSIGMENT:
                StringReturn = "eASSIGMENT";
                break;
            case eAPI:
                StringReturn = "eAPI";
                break;
            default:
                StringReturn = "N.A.";
                break;

        }
        return StringReturn;
    }
}
