/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser;

/**
 *
 * @author Pablo C. Cañizares 
 */
public enum EnumApiCollection {
    
    eGENERAL, eSIMCAN, eMPI, eOMNET;
    
    public EnumApiCollection fromString(String strVal)
    {
        EnumApiCollection eRET = EnumApiCollection.eGENERAL;
        
        if(strVal.contentEquals("SIMCAN_API"))
           eRET = eSIMCAN;    
        else if(strVal.contentEquals("MPI_API"))
           eRET = eMPI;              
        else if(strVal.contentEquals("OMNET_API"))
           eRET = eOMNET;
        
        return eRET;
    }
}
