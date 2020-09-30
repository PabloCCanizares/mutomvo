/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser;

import mutomvo.Mutation.Config.ApiParser.helpers.ApiCallParser;
import java.util.Arrays;
import java.util.LinkedList;
import mutomvo.Mutation.Config.ApiParser.helpers.ApiParamChecker;

/**
 *
 * @author pablo
 */
public class ApiCollection {

    ApiCallParser apiParser;
    ApiParamChecker apiParamChecker;
    EnumApiCollection enumCollection;
    String strCollectionName;
    LinkedList<ApiCall> apiList;
    boolean collectionReplacement;
    
    public ApiCollection(String strCollName)
    {
        apiParser = new ApiCallParser();
        apiParamChecker = new ApiParamChecker();
        apiList =  new LinkedList<ApiCall>();
        strCollectionName = strCollName;
        collectionReplacement=false;
        
        enumCollection = EnumApiCollection.eGENERAL;
        enumCollection = enumCollection.fromString(strCollName);
    }
    
    void setCollectionName(String strColl) {
        strCollectionName = strColl;
    }

    boolean insertApiCall(String strApiCall) {
        boolean bRet = false;
        ApiCall apiCall;

        apiCall = null;
        
        //Si no existe
        if (!exists(strApiCall)) {
            
            //Crea la API.
            apiCall = apiParser.createApiCall(strCollectionName,strApiCall);
            if(apiCall != null)
            {
                //Realiza las comparaciones necesarias, para saber con qué operaciones puede hacer Replacement.
                analyzeParameters(apiCall);                
                
                //inserta la API
                apiList.add(apiCall);
                bRet = true;            
            }
        }

        return bRet;
    }

    public int countAppearances(String strApiCall) {
        int nRet = 0;

        for(int i=0;i<apiList.size();i++)
        {
            ApiCall api = apiList.get(i);
            
            if(api.getCallName().equals(strApiCall))
                nRet++;
        }
        
        return nRet;
    }
    
    public boolean exists(String strApiCall) {
        boolean bRet = false;
        int nFind;
        nFind = strApiCall.indexOf("(");

        if(nFind !=-1)
            strApiCall = (String) strApiCall.subSequence(0, nFind);
        
        strApiCall = strApiCall.trim();
       
        if(!strApiCall.isEmpty())
        {
            for(int i=0;i<apiList.size()&&!bRet;i++)
            {
                ApiCall api = apiList.get(i);

                bRet = api.getCallName().contains(strApiCall);
            }        
        }
        
        
        return bRet;
    }

    String getCollectionName() {
        return strCollectionName;
    }

    private boolean analyzeParameters(ApiCall apiCall) 
    {
        boolean bRet = false;
        LinkedList<LinkedList<Integer>> listRet, finalList;
        LinkedList<Boolean> paramStatus = new LinkedList<Boolean>();
        
        try
        {
            if(apiCall.nArguments>1)
            {
               //Comprobamos qué argumentos son iguales
               listRet = apiParamChecker.getMergableParams(apiCall); 
               paramStatus = apiCall.getParamStatus();
               if(listRet != null)
               {
                   //tenemos la lista. ahora habría que rellenarla con los parámetros estáticos y ordenarla
                   finalList = apiParamChecker.buildFinalList(listRet, paramStatus);
                   if(finalList != null)
                   {
                       apiCall.setParamsList(finalList);
                       bRet = true;
                   }
               }
               apiParamChecker.Clear();
            }
            
        }catch(NullPointerException e){}
        
        return bRet;
    }

    ApiCall getApiCall(ApiCall apiAux) {
        ApiCall apiRet;
        boolean bRet;
        String strApiCall;
        
        apiRet=null;
        bRet = false;
        if(apiAux != null)
        {
            strApiCall = apiAux.getCallName();
            //Buscamos un API con el mismo nombre, mismos parametros y mismos tipos
            for(int i=0;i<apiList.size()&&!bRet;i++)
            {
                ApiCall api = apiList.get(i);

                if(api.getCallName().contains(strApiCall))
                {
                    //Mismo numero de parametros
                    if(api.getnArguments() == apiAux.getnArguments())
                    {

                        //TODO: Aqui la comprobacion de tipos de parametros (no hecha)
                        bRet = checkArgumentsTypes(api, apiAux);
                        bRet = true;
                        apiRet = api;
                    }
                }
            }
        }
        else
        {
            System.out.printf("Warning, inconsistences: ApiCollection");
        }
        
        return apiRet;
    }

    private boolean checkArgumentsTypes(ApiCall api, ApiCall apiAux) {
        boolean bRet =true;
        LinkedList<String> params1,params2;
        
        try
        {
            params1 = api.getArgumentsTypeList();
            params2 = apiAux.getArgumentsTypeList();
            
            if(params1.size() == params2.size())
            {
                for(int i=0;i<params1.size()&&bRet;i++)
                {
                    bRet = (params1.get(i).contains(params2.get(i)));
                }
            }
            else
                bRet = false;
            
        }catch (NullPointerException e)
        {
            bRet =false;
        }
        return bRet;
    }
    public boolean analyzeReplacement()
    {
        boolean bRet;
        ApiCall apiCallIndex, apiCallAux;
        bRet = false;
        
        for(int i=0;i<apiList.size();i++)
        {
            apiCallIndex = apiList.get(i);
            for(int j=i+1;j<apiList.size();j++)
            {
                apiCallAux = apiList.get(j);
                
                if(checkArgumentsTypes(apiCallIndex, apiCallAux))
                {
                    apiCallIndex.setReplacementCall(apiCallAux);
                    apiCallAux.setReplacementCall(apiCallIndex);
                    bRet = true;
                }
            }
            apiCallIndex.resetRepList();
                    
        }
        collectionReplacement = bRet;
        
        return bRet;
    }

    EnumApiCollection getCollectionClass() {
        return enumCollection;
    }
}
