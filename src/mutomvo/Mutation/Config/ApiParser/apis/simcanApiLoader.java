/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser.apis;

import java.util.LinkedList;

/**
 *
 * @author Pablo C. Cañizares 
 */

public class simcanApiLoader implements ApiLoader
{
    LinkedList<String> ApiList;
    private final String SIMCAN_API_NAME = "SIMCAN_API";  
    public boolean loadApi() {
        boolean bRet = true;
        
        ApiList = new LinkedList<String>();
        
        //TODO: Hacer con el CDT
        //TODO: Idea, generador de codigo ... 
        //Abres un textbox, que el usuario 
        //introduzca la api y su nombre
        //y genera la clase automaticamente y la inserta en el sistema
        
        
        ApiList.add("SIMCAN_request_cpu(unsigned int);");
        ApiList.add("SIMCAN_request_cpuTime (simtime_t);");
        
        ApiList.add("SIMCAN_request_allocMemory(unsigned int,unsigned int);");
        ApiList.add("SIMCAN_request_freeMemory(unsigned int,unsigned int);");          
        
        ApiList.add("SIMCAN_request_read(const char*,unsigned int,unsigned int);");
        ApiList.add("SIMCAN_request_write(const char*, unsigned int,unsigned int);");
        
        ApiList.add("SIMCAN_request_delete(const char*);");
        ApiList.add("SIMCAN_request_close(const char*);");
        ApiList.add("SIMCAN_request_open(const char*);");        
        ApiList.add("SIMCAN_request_create(const char*);");        
        
        ApiList.add("SIMCAN_request_createConnection (string,int,int);");        		 							   
        ApiList.add("SIMCAN_request_createListenConnection(int);");        
        
        ApiList.add("SIMCAN_request_sendDataToNetwork(SIMCAN_Message*, int id);"); 

        return bRet;
    }

    public LinkedList<String> getApiList() {
        return ApiList;
    }

    public String getName() {
        return SIMCAN_API_NAME;
    }
    
}
