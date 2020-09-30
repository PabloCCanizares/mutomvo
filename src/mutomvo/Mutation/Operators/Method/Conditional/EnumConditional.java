/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Conditional;

/**
 *
 * @author user
 */
public enum EnumConditional {

    eCOND_NONE, eCOND_2AND, eCOND_2OR, eCOND_AND, eCOND_OR, eCOND_NEG; // TODO: Falta ^ pero no se como llamarlo


    public EnumConditional fromString(String strVal)
    {
        EnumConditional eRET = EnumConditional.eCOND_NONE;
        
        if(strVal.contentEquals("&&"))
           eRET = eCOND_2AND; 
        else if(strVal.contentEquals("&"))
           eRET = eCOND_AND;    
        else if(strVal.contentEquals("||"))
           eRET = eCOND_2OR;     
        else if(strVal.contentEquals("|"))
           eRET = eCOND_OR;     
        else if(strVal.contentEquals("!"))
           eRET = eCOND_NEG;     
        
        return eRET;
    }
    @Override
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eCOND_2AND:
                StringReturn = "&&";
                break;
            case eCOND_AND:
                StringReturn = "&";
                break;
            case eCOND_2OR:
                StringReturn = "||";
                break;
            case eCOND_OR:
                StringReturn = "|";
                break;
            case eCOND_NEG:
                StringReturn = "!";
                break;
        }
        return StringReturn;
    }

    public String toString(EnumConditional eEnum) {
        String StringReturn = "";
        switch (eEnum) {
            case eCOND_2AND:
                StringReturn = "&&";
                break;
            case eCOND_AND:
                StringReturn = "&";
                break;
            case eCOND_2OR:
                StringReturn = "||";
                break;
            case eCOND_OR:
                StringReturn = "|";
                break;
            case eCOND_NEG:
                StringReturn = "!";
                break;                
        }
        return StringReturn;
    }

    public EnumConditional ToString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
