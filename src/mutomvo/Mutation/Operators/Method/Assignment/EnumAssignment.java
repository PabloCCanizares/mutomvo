/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Assignment;

/**
 *
 * @author usuario_local
 */
public enum EnumAssignment {
    

    eAS_NONE, eAS_ADD_EQ, eAS_MUL_EQ, eAS_SUB_EQ, eAS_DIV_EQ, eAS_MOD_EQ,
    eAS_AMP_EQ, eAS_OR_EQ, eAS_EL_EQ, eAS_SHIFL_EQ, eAS_SHIFR_EQ;
    
    public EnumAssignment fromString(String strVal)
    {
        EnumAssignment eRET = EnumAssignment.eAS_NONE;
        
        if(strVal.contentEquals("+="))
           eRET = eAS_ADD_EQ; 
        else if(strVal.contentEquals("-="))
           eRET = eAS_SUB_EQ;    
        else if(strVal.contentEquals("*="))
           eRET = eAS_MUL_EQ;     
        else if(strVal.contentEquals("/="))
           eRET = eAS_DIV_EQ;     
        else if(strVal.contentEquals("%="))
           eRET = eAS_MOD_EQ;         
        else if(strVal.contentEquals("&="))
           eRET = eAS_AMP_EQ;    
        else if (strVal.contentEquals("|="))
           eRET = eAS_OR_EQ;
        else if (strVal.contentEquals("^="))
           eRET = eAS_EL_EQ;
        else if (strVal.contentEquals("<<="))
           eRET = eAS_SHIFL_EQ;
        else if (strVal.contentEquals(">>="))
           eRET = eAS_SHIFR_EQ;               
                
        return eRET;
    }
    
    @Override
    public String toString() 
    {
       String StringReturn = "";
       switch (this) {
         case eAS_NONE:
              StringReturn = "+=";
              break;
         case eAS_ADD_EQ:
              StringReturn = "+=";
              break;
         case eAS_MUL_EQ:
              StringReturn = "*=";
              break;         
         case eAS_SUB_EQ:
              StringReturn = "-=";
              break;              
         case eAS_DIV_EQ:
              StringReturn = "/=";
              break;   
         case eAS_MOD_EQ:
              StringReturn = "%=";
              break;   
          case eAS_AMP_EQ:
              StringReturn = "&=";
              break;   
          case eAS_OR_EQ:
              StringReturn = "|=";
              break;
          case eAS_EL_EQ:
              StringReturn = "^=";
              break;
          case eAS_SHIFL_EQ:
              StringReturn = "<<=";
              break;
          case eAS_SHIFR_EQ:
              StringReturn = ">>=";
              break;
    
        }
        return StringReturn;
    }
    public String toString(EnumAssignment eEnum) 
    {
       String StringReturn = "";
       switch (eEnum) {
         case eAS_NONE:
              StringReturn = "+=";
              break;
         case eAS_ADD_EQ:
              StringReturn = "*=";
              break;
         case eAS_MUL_EQ:
              StringReturn = "-=";
              break;         
         case eAS_SUB_EQ:
              StringReturn = "/=";
              break;              
         case eAS_DIV_EQ:
              StringReturn = "%=";
              break;              
        }
        return StringReturn;
    }
}
