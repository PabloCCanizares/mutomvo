/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method;

/**
 *
 * @author Pablo C. Cañizares
 */
public enum EnumMuOperation {
    eAORb, eAORu, eAORs, eAOIu, eAOIs, eAODu, eAODs, eROR, eCOR, eCOI, eCOD, eASOR, eSfOR, eLOI, eLOD, eLOR, eOMOVUP, eOMOVDOWN, eOMER,
    eODEL, eOREP;

    @Override
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eAORb:
                StringReturn = "eAORb";
                break;
            case eAORu:
                StringReturn = "eAORu";
                break;
            case eAORs:
                StringReturn = "eAORs";
                break;
            case eAOIu:
                StringReturn = "eAOIu";
                break;
            case eAOIs:
                StringReturn = "eAOIs";
                break;
            case eAODu:
                StringReturn = "eAODu";
                break;
            case eAODs:
                StringReturn = "eAODs";
                break;
            case eROR:
                StringReturn = "eROR";
                break;
            case eCOR:
                StringReturn = "eCOR";
                break;
            case eCOI:
                StringReturn = "eCOI";
                break;
            case eCOD:
                StringReturn = "eCOD";
                break;
            case eLOR:
                StringReturn = "eLOR";
                break;                   
            case eLOI:
                StringReturn = "eLOI";
                break;
            case eLOD:
                StringReturn = "eLOD";
                break;                
            case eASOR:
                StringReturn = "eASOR";
                break;
            case eSfOR:
                StringReturn = "SfOR";
                break;
            case eOMOVUP:
                StringReturn = "eOMOVUP";
                break;
            case eOMOVDOWN:
                StringReturn = "eOMOVDOWN";
                break;
            case eOMER:
                StringReturn = "eOMER";
                break;
            case eOREP:
                StringReturn = "eOREP";
                break;
            case eODEL:
                StringReturn = "eODEL";
                break;
        }
        return StringReturn;
    }

}
