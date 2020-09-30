/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser;

import java.util.HashMap;
import java.util.LinkedList;
import mutomvo.Mutation.Config.ApiParser.apis.ApiLoader;
import mutomvo.Mutation.Config.ApiParser.apis.mpiApiLoader;
import mutomvo.Mutation.Config.ApiParser.apis.omnetApiLoader;
import mutomvo.Mutation.Config.ApiParser.apis.simcanApiLoader;
import mutomvo.Mutation.Config.ApiParser.helpers.ApiCallParser;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.MutatedOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ApiManager 
{

  
    //Lista enlazada de colecciones
    LinkedList<ApiCollection> ApiCollectionsList;
    //ApiCall apiShuffle;
    
    HashMap<String, ApiCall> apiShuffleMap;
    HashMap<String, ApiCall> apiReplacementMap;
    HashMap<String, ApiCall> apiReplacementBackUp;
    
    //Esta clase encapsula a ApiCollection y ApiCall
    //Va a recibir consultas con llamadas a la API del tipo:
    // x mpi_send (i, dataSize); :
    // - - Se puede intercambiar (OOR)? (Tengan los mismos argumentos, en número y tipo)
    // - - - Si es así, dame todas las llamadas a la API que cumplan esta propiedad
    // 
    // x mpi_send (i, dataSize); :
    // - - Se puede hacer merge (OMER)?
    //
    //-Métodos:
    // - boolean canBeMerged()
    // - boolean hasNextMerge
    // - <T> getNextMerge  -> <T> sería una tupla de órdenes de parámetros:
    //      (1,2,3) -> (1,3,2), (2,1,3), (2,3,1), (3,2,1),(3,1,2)
    // - boolean canBeReplaced()
    //Internos:
    // - Buscar una colección de apis dada una llamada
    // - Crear una colección dada una llamada
    // - 
    public ApiManager()
    {
        ApiCollectionsList = new LinkedList<ApiCollection>();
        apiShuffleMap = new HashMap<String, ApiCall>();
        apiReplacementMap = new HashMap<String, ApiCall>();
        apiReplacementBackUp = new HashMap<String, ApiCall>();
    }
    
    public boolean insertCall(String strClass, String strCall)
    {
        boolean bRet =false;
        ApiCollection apiColl;
        
        apiColl = null;
        bRet =false;
        if(!strClass.isEmpty())
        {
            apiColl = getCollection(strClass);
            if(apiColl == null)
            {
                apiColl = new ApiCollection(strClass);                
                ApiCollectionsList.add(apiColl);
            }
            bRet = apiColl.insertApiCall(strCall);             
        }
        
        return bRet;
    }

    private ApiCollection getCollection(String strClass) {
        ApiCollection colRet, colAux;
        
        colRet = colAux = null;
        for(int i=0;(i<ApiCollectionsList.size()) && colRet == null;i++)
        {
            colAux = ApiCollectionsList.get(i);
            if(colAux != null && colAux.getCollectionName().equals(strClass))
            {
                colRet = colAux;
            }
        }
        return colRet;
    }
    
    public boolean LoadApiCollection(EnumApiCollection eApi)
    {
        //Preloaded collections
        boolean bRet = false;
        ApiLoader iApi = null;
        switch(eApi)
        {
            case eMPI:      
                iApi = new mpiApiLoader();
                bRet = iApi.loadApi();                
                break;
            case eOMNET:
                iApi = new omnetApiLoader();
                bRet = iApi.loadApi();                   
                break;
            case eSIMCAN:
                iApi = new simcanApiLoader();
                bRet = iApi.loadApi();
                break;
        }
        
        if(bRet)
            loadApiList(iApi);
        
        return bRet;
    }

    private boolean collectionExists(String strClass)
    {
        return (getCollection(strClass) != null);
    }
    private boolean loadApiList(ApiLoader iApi) 
    {
        boolean bRet = true;
        String strApi, strClass;
        LinkedList<String> list;
        ApiCollection apiColl;
        
        try
        {            
            list = iApi.getApiList();
            strClass = iApi.getName();
            apiColl = new ApiCollection(strClass);                
            
            if(!collectionExists(strClass))
            {                
                for(int i=0;i<list.size()&&bRet;i++)
                {
                    strApi = list.get(i);

                    bRet = apiColl.insertApiCall(strApi); 
                }
                
                if(bRet)
                {
                    apiColl.analyzeReplacement();
                    ApiCollectionsList.add(apiColl);
                }
                
                //Comprobamos los elementos intercambiables.
            }
            
        }catch(NullPointerException e){
            bRet = false;
        }
            
        return bRet;
    }

    public boolean isCallApi(String strName) {
        boolean bRet = false;
        ApiCollection colRet, colAux;
        //For each api collection, search the name.                
        
        colRet = colAux = null;
        for(int i=0;(i<ApiCollectionsList.size() && !bRet);i++)
        {
            colAux = ApiCollectionsList.get(i);
            bRet = colAux.exists(strName);
        }
        
        return bRet;
    }

        
     public ApiCall getApiCall(MutatedOperator Operator, String strCall, String strParameters) {
        boolean bRet;
        ApiCall apiRet, apiAux;
        ApiCallParser apiParserAux;
        ApiCollection colRet, colAux;
        int nAppearances;
        
        //For each api collection, search the name.                
        bRet=false;
        colRet = colAux = null;
        apiRet = null;
        nAppearances = 0;
        apiParserAux = new ApiCallParser();
                
        for(int i=0;(i<ApiCollectionsList.size() && !bRet);i++)
        {
            colAux = ApiCollectionsList.get(i);
            nAppearances = colAux.countAppearances(strCall);
            if(nAppearances == 1)
            {
                //apiAux = apiParserAux.createApiCall(colAux.getCollectionName(),strCall+strParameters);
                apiAux = apiParserAux.createApiCall(colAux.getCollectionName(),Operator);
                apiRet = colAux.getApiCall(apiAux);
                if(apiRet != null)
                    bRet = true;
            }
            else if (nAppearances >1)
            {
                //TODO:: Para hacerlo de verdad de verdad flexible. Hay que utilizar el parser CDT para saber los tipos
                //de cada parámetro. Hacer el 
                //Si hay más de una aparición hay que tratarlo de otra forma
                //Teniendo en cuenta el número de operadores y el tipo.
                apiAux = apiParserAux.createApiCall(colAux.getCollectionName(),Operator); 
                apiRet = colAux.getApiCall(apiAux);
                if(apiRet != null)
                    bRet = true;                
            }
        }
        
        return apiRet;
    }
        
    public boolean checkShuffableMethod(MutatedOperator Operator, String strCall, String strParameters) {
        boolean bRet = false;
        ApiCall apiCall = getApiCall(Operator, strCall, strParameters);
        
        if(apiCall != null)
        {   
            if(apiCall.isShuffle())
            {
                //guardamos el elemento para hacer un getNext
                apiShuffleMap.put(strCall,apiCall);
                bRet = true;
            }
        }
        
        return bRet;
    }
    public boolean checkReplacementMethod(MutatedOperator Operator,String strCall, String strParameters) {
        boolean bRet = false;
        ApiCall apiCall = getApiCall(Operator, strCall, strParameters);
        
        if(apiCall != null)
        {            
            //guardamos el elemento para hacer un getNext
            apiReplacementMap.put(strCall,apiCall);
            bRet = true;
        }
        
        return bRet;        
    }
    
    public boolean hasNextReplacement(String strCall)
    {
        boolean bRet = false;
        ApiCall apiCheck;
                
        try
        {
            //bRet = apiShuffle.hasNextShuffle();
            apiCheck =  apiReplacementMap.get(strCall);
            bRet = apiCheck.hasNextReplacement();
        }catch(NullPointerException e)
        {      
        }
        
        return bRet;
    }
    public boolean hasNextShuffle(String strCall)
    {
        boolean bRet = false;
        ApiCall apiCheck;
                
        try
        {
            //bRet = apiShuffle.hasNextShuffle();
            apiCheck =  apiShuffleMap.get(strCall);
            bRet = apiCheck.hasNextShuffle();
        }catch(NullPointerException e)
        {      
        }
        
        return bRet;
    }
    public String getNextReplacement(String strCall)
    {
        boolean bRet = false;        
        ApiCall apiCheck, apiRet;
        String strRet = null;
        
        apiRet = null;
        try
        {
            apiCheck = apiReplacementMap.get(strCall);
            apiRet = apiCheck.getNextReplacement();
            
            if(apiRet != null)
            {
                strRet = apiRet.getCallName();
            }
            if(!apiCheck.hasNextReplacement())
            {
                apiReplacementMap.remove(strCall);
                apiCheck.resetRepList();
            }
            
        }catch(NullPointerException e)
        {      
        }
        
        return strRet;
    }    
    public LinkedList<Integer> getNextShuffle(String strCall)
    {
        boolean bRet = false;
        LinkedList<Integer> ret;
        ApiCall apiCheck;
        ret = null;
        try
        {
            apiCheck = apiShuffleMap.get(strCall);
            if(apiCheck != null)
            {
                ret = apiCheck.getNextShuffle();
                if(!apiCheck.hasNextShuffle())
                {
                    apiShuffleMap.remove(strCall);
                    apiCheck.resetShuffleList();
                }            
            }
            
        }catch(NullPointerException e)
        {      
        }
        
        return ret;
    }

    public EnumOperatorClass getApiClass(String strName) {
        
        EnumOperatorClass eOpRet;
        boolean bRet = false;
        ApiCollection colRet, colAux;
        EnumApiCollection eApiClass;
        //For each api collection, search the name.                
        
        eOpRet= null;
        colRet = colAux = null;
        for(int i=0;(i<ApiCollectionsList.size() && !bRet);i++)
        {
            colAux = ApiCollectionsList.get(i);
            bRet = colAux.exists(strName);
            
            if(bRet)
            {
                eApiClass = colAux.getCollectionClass();
                
                //TODO: This conversion must be replaced
                // Only one enum is necessary
                if(eApiClass == EnumApiCollection.eSIMCAN)
                    eOpRet = EnumOperatorClass.eSIMCANCLASS;
                if(eApiClass == EnumApiCollection.eMPI)
                    eOpRet = EnumOperatorClass.eMPICLASS;  
                if(eApiClass == EnumApiCollection.eOMNET)
                    eOpRet = EnumOperatorClass.eOMNETCLASS;                 
            }
        }
        
        return eOpRet;               
    }


}
