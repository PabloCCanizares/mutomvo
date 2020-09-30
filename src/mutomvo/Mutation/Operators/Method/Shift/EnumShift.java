/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Method.Shift;

import mutomvo.Mutation.Operators.Method.Relational.EnumRelational;

/**
 *
 * @author Pablo C. Cañizares
 */
public enum EnumShift {
    eSHIFT_NONE, eSHIFT_LEFT, eSHIFT_RIGHT;

     
    //TODO: Este fromString me parece una mierda, pero no se hacero de otra forma
    public EnumShift fromString(String strVal)
    {
        EnumShift eRET = EnumShift.eSHIFT_NONE;
        
        if(strVal.contentEquals("<<"))
           eRET = eSHIFT_LEFT; 
        else if(strVal.contentEquals(">>"))
           eRET = eSHIFT_RIGHT;    
              
        
        return eRET;
    }
    public String toString() {
        String StringReturn = "";
        switch (this) {
            case eSHIFT_LEFT:
                StringReturn = "<<";
                break;
            case eSHIFT_RIGHT:
                StringReturn = ">>";
                break;                           
        }
        return StringReturn;
    }

    public String toString(EnumShift eEnum) {
        String StringReturn = "";
        switch (eEnum) {
            case eSHIFT_LEFT:
                StringReturn = "<<";
                break;
            case eSHIFT_RIGHT:
                StringReturn = ">>";
                break;                         
        }
        return StringReturn;
    } 
}
