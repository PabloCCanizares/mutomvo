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

public class mpiApiLoader implements ApiLoader
{
    LinkedList<String> ApiList;
    private final String MPI_API_NAME = "MPI_API";  
    public boolean loadApi() {
        boolean bRet = true;
        
        ApiList = new LinkedList<String>();
        
        //MPI
        ApiList.add("mpi_send (int, int);");
        ApiList.add("mpi_recv (int, int);");                
        ApiList.add("mpi_bcast (int, int);");
        ApiList.add("mpi_barrier ();");
        ApiList.add("mpi_gather(int, int);");
        ApiList.add("mpi_scatter(int, int);");
        
        return bRet;
    }

    public LinkedList<String> getApiList() {
        return ApiList;
    }

    public String getName() {
        return MPI_API_NAME;
    }
    
}
