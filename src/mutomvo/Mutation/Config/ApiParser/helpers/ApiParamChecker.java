/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser.helpers;

import java.util.LinkedList;
import mutomvo.Mutation.Config.ApiParser.ApiCall;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ApiParamChecker 
{

    ContainerParamList combSetList = null;
    ApiCall savedApiCall;
    public ApiParamChecker()
    {
        combSetList = new ContainerParamList();
        savedApiCall = null;
    }
    public void Clear()
    {
        combSetList.clear();
        savedApiCall = null;
    }
    /**
     * Search the call parameters that can be merged
     * @param apiCall : 
     */
    public LinkedList<LinkedList<Integer>> getMergableParams(ApiCall apiCall) {
        
        //Devuelve tuplas de valores intercambiables:
        // {0,3,4,5},{1,2,6} ... 
        String strInit, strNext;
        LinkedList<String> paramList;
        LinkedList<Integer> tuplaList = null;
        
        LinkedList<LinkedList<Integer>> retList = null;
        boolean bDynamic;
        
        
        if(apiCall != null)
        {            
            savedApiCall = apiCall;
            paramList = apiCall.getArgumentsTypeList();
            for(int i=0;i<paramList.size()-1;i++)
            {             
                bDynamic = false;
                strInit = paramList.get(i);
                
                if(apiCall.IsDynamicParam(i) == false)
                {
                    for(int j=i;j<paramList.size();j++)
                    {
                        if(i!=j)
                        {
                            strNext = paramList.get(j);
                            if(CompareTypes(strInit, strNext))
                            {
                                //Si no existe lista la creamos
                                //E insertamos en el contenedor
                                //Añadimos a la lista
                                //<i,j,j', ...
                                if(tuplaList == null)
                                {
                                    tuplaList = new LinkedList<Integer>();
                                    tuplaList.add(i);
                                    apiCall.setDynamicParam(i);
                                }
                                tuplaList.add(j);
                                apiCall.setDynamicParam(j);                            
                            }
                        }     
                    }   
                    if(tuplaList != null)
                        combSetList.insertNewList(tuplaList);
                    tuplaList = null;                
                }
                
            }
            retList = combSetList.MergeList();
        }
        return retList;
    }
    
    //generateAllCombinations -> MergeParametersList: Clase que contiene lista de listas

    private boolean CompareTypes(String strInit, String strNext) {
        boolean bRet = false;
        
        bRet = strInit.equals(strNext);
        
        if(!bRet)
        {
            //Comparamos que valores como (unsigned int ~ int)
            //Y ya lo mejor sería que el sistema reconvirtiera las variables directamente, ej: char[] a String
            //etc
        }
        
        return bRet;
    }

    public LinkedList<LinkedList<Integer>> buildFinalList(LinkedList<LinkedList<Integer>> listIn, LinkedList<Boolean> paramStatus) {
        LinkedList<LinkedList<Integer>> retList;
        LinkedList<Integer> orderedList, disorderList;
        
        retList = null;
        
        if(!AllParametersDynamic(paramStatus) || !combSetList.checkCombContiguous())
        {
            //En este caso hay que construir las listas finales.
            //Se suele dar en los casos en que no todos los elementos son dinámicos
            //Y en aquellos en los que las combinaciones no son contíguas.
            retList = new LinkedList<LinkedList<Integer>>();
            
            //Tenemos el orden original, únicamente hay que hacer una primera pasada
            //inicializando el vector a {0,1,2,3,4 ... n}
            //E intercambiando las posiciones con las iniciales
            for(int i=0;i<listIn.size();i++)
            {
                disorderList = listIn.get(i);
                orderedList = GenerateFinalOrder(disorderList, paramStatus.size());
                
                if(orderedList != null)
                    retList.add(orderedList);
            }
        }
        else
            retList = listIn;
        
        return retList;
    }

    private boolean AllParametersDynamic(LinkedList<Boolean> paramStatus) {
        
        return (paramStatus.indexOf(false) == -1);
    }

    private LinkedList<Integer> GenerateFinalOrder(LinkedList<Integer> listIn, int nTotalElements) 
    {
        LinkedList<Integer>  orderList, setList;
        int nIndexElement, nParam, nOrder;
        
        nIndexElement = nParam = nOrder = 0;
        orderList = null;
        
        if(listIn != null && listIn.size()<=nTotalElements)
        {
            
            orderList = new LinkedList<Integer>();
            
            //Lo primero de todo inicializamos.
            initializeList(orderList, nTotalElements);
            
            //Por cada uno de los conjuntos, ordenar a gusto la lista
            for(int i=0;i<combSetList.CombSize();i++)
            {
                setList = combSetList.getSetComb(i);
                for(int j=0;j<setList.size();j++)
                {                   
                    nOrder = setList.get(j);
                    nParam = listIn.get(nIndexElement);

                    orderList.set(nOrder, nParam);
                    nIndexElement++;               
                }
            }
        }
        else 
            System.out.printf("Warning! Generating array order [ApiParamChecker]\n");
        
        return orderList;
    }    

    private void initializeList(LinkedList<Integer> orderList, int nTotalElements) 
    {
        //{0,1,2,3 ... n}
        for(int i=0;i<nTotalElements;i++)
        {
            orderList.add(i);
        }
    }
    
}
