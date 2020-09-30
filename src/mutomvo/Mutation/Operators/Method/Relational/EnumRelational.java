/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Relational;

/**
 *
 * @author user
 */
public enum EnumRelational 
{
    
     eREL_NONE, eREL_GT, eREL_GET, eREL_LT, eREL_LET, eREL_EQ_EQ, eRE_NOT_EQUAL;

     
    //TODO: Este fromString me parece una mierda, pero no se hacero de otra forma
    public EnumRelational fromString(String strVal)
    {
        EnumRelational eRET = EnumRelational.eREL_NONE;
        
        if(strVal.contentEquals(">="))
           eRET = eREL_GET; 
        else if(strVal.contentEquals(">"))
           eRET = eREL_GT;    
        else if(strVal.contentEquals("<="))
           eRET = eREL_LET;     
        else if(strVal.contentEquals("<"))
           eRET = eREL_LT;     
        else if(strVal.contentEquals("=="))
           eRET = eREL_EQ_EQ; 
        else if(strVal.contentEquals("!="))
           eRET = eRE_NOT_EQUAL;        
        
        return eRET;
    }
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eREL_GET:
                StringReturn = ">=";
                break;
            case eREL_GT:
                StringReturn = ">";
                break;
            case eREL_LT:
                StringReturn = "<";
                break;
            case eREL_LET:
                StringReturn = "<=";
                break;
            case eREL_EQ_EQ:
                StringReturn = "==";
                break;
            case eRE_NOT_EQUAL:
                StringReturn = "!=";
                break;                
        }
        return StringReturn;
    }

    public String toString(EnumRelational eEnum) {
        String StringReturn = "";
        switch (eEnum) {
            case eREL_GET:
                StringReturn = ">=";
                break;
            case eREL_GT:
                StringReturn = ">";
                break;
            case eREL_LT:
                StringReturn = "<";
                break;
            case eREL_LET:
                StringReturn = "<=";
                break;
            case eREL_EQ_EQ:
                StringReturn = "==";
                break;
            case eRE_NOT_EQUAL:
                StringReturn = "!=";
                break;                
        }
        return StringReturn;
    }
}
