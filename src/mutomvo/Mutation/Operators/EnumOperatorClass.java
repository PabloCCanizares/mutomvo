/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators;

/**
 *
 * @author Pablo C. Cañizares 
 */
public enum EnumOperatorClass {
    eGENERALCLASS,eSIMCANCLASS,eOMNETCLASS,eMPICLASS;
    
    public String toString()
    {
        String strRet="";
        
        if(this == eGENERALCLASS)
            strRet = "eGENERALCLASS";
        else if (this == eSIMCANCLASS)
            strRet = "eSIMCANCLASS";
        else if (this == eOMNETCLASS)
            strRet = "eOMNETCLASS";       
        else if (this == eMPICLASS)
            strRet = "eMPICLASS";          
        return strRet;
    }
}
