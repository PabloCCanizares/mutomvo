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
public class omnetApiLoader implements ApiLoader
{
    LinkedList<String> ApiList;
    private final String OMNET_API_NAME = "OMNET_API";  
    public boolean loadApi() {
        boolean bRet = true;
        
        ApiList = new LinkedList<String>();
        
        //OMNeT
        ApiList.add("handleMesage ();");                
        ApiList.add("send(cMessage* , int);");
        ApiList.add("scheduleAt (simtime_t , cMessage* );");
        ApiList.add("cancelEvent (cMessage*);");
        ApiList.add("cancelAndDelete(cMessage*);");
        
        return bRet;
    }

    public LinkedList<String> getApiList() {
        return ApiList;
    }

    public String getName() {
        return OMNET_API_NAME;
    }
}
