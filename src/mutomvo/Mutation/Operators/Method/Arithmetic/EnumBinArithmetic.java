/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Arithmetic;

/**
 *
 * @author Pablo C. Cañizares
 */
public enum EnumBinArithmetic implements IEnumArithmetic {

    eAR_NONE(""),
    eAR_ADD("+"),
    eAR_MUL("*"),
    eAR_SUB("-"),
    eAR_DIV("/"),
    eAR_MOD("%"),
    eAR_ADDADD("++", EnumARTypes.eARType_SHORT),
    eAR_SUBSUB("--", EnumARTypes.eARType_SHORT),
    eAR_SUBUN("-", EnumARTypes.eARType_Unary);

    private final String strChar;
    private final EnumARTypes eType;

    public String getString() {
        return strChar;
    }

    ;
    @Override
   public EnumARTypes getType() {
        return eType;
    }
   
    EnumBinArithmetic(String strChar, EnumARTypes eType) {
        this.strChar = strChar;
        this.eType = eType;
    }

    EnumBinArithmetic(String strChar) {
        this.strChar = strChar;
        this.eType = EnumARTypes.eARType_BIN;
    }

    public EnumBinArithmetic fromString(String strVal) {
        EnumBinArithmetic eRET = EnumBinArithmetic.eAR_NONE;

        if (strVal.contentEquals("+")) {
            eRET = eAR_ADD;
        } else if (strVal.contentEquals("-")) {
            eRET = eAR_SUB;
        } else if (strVal.contentEquals("*")) {
            eRET = eAR_MUL;
        } else if (strVal.contentEquals("/")) {
            eRET = eAR_DIV;
        } else if (strVal.contentEquals("%")) {
            eRET = eAR_MOD;
        } else if (strVal.contentEquals("++")) {
            eRET = eAR_ADDADD;
        } else if (strVal.contentEquals("--")) {
            eRET = eAR_SUBSUB;
        }

        return eRET;
    }

    @Override
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eAR_ADD:
                StringReturn = "+";
                break;
            case eAR_MUL:
                StringReturn = "*";
                break;
            case eAR_SUB:
                StringReturn = "-";
                break;
            case eAR_DIV:
                StringReturn = "/";
                break;
            case eAR_MOD:
                StringReturn = "%";
                break;
            case eAR_ADDADD:
                StringReturn = "++";
                break;
            case eAR_SUBSUB:
                StringReturn = "--";
                break;
            case eAR_SUBUN:
                StringReturn = "-";
                break;
        }
        return StringReturn;
    }

    public String toString(EnumBinArithmetic eEnum) {
        String StringReturn = "";
        switch (eEnum) {
            case eAR_ADD:
                StringReturn = "+";
                break;
            case eAR_MUL:
                StringReturn = "*";
                break;
            case eAR_SUB:
                StringReturn = "-";
                break;
            case eAR_DIV:
                StringReturn = "/";
                break;
            case eAR_MOD:
                StringReturn = "%";
                break;
        }
        return StringReturn;
    }

    @Override
    public String ToString() {
        return toString();
    }
}
