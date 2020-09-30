/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser.helpers;

import java.util.LinkedList;

/**
 *
 * @author pablo
 */
class ContainerParamList 
{
    int nElements;
    LinkedList<LinkedList<Integer>> combinationList;    
    
    //  [{1,2,3,12}, ..., {4,6}, {5,10}]
    public ContainerParamList()
    {
        combinationList = new LinkedList<LinkedList<Integer>>();
    }
    public void insertNewList(LinkedList<Integer> listToAdd)
    {
        if(listToAdd != null && listToAdd.size()>0)
        {
            combinationList.add(listToAdd);
            nElements++;
        }
    }
    public boolean checkCombContiguous()
    {
        boolean bRet =false;
        int nIndexValue, nValue,i,j;
        LinkedList<Integer> listIndex;
        
        nIndexValue = nValue = i = j = 0;
        try
        {
            if(combinationList.size()>0)
            {
                //Ponemos a true, para 
                bRet = true;
                //Para cada conjunto, comprobamos que no haya saltos en el orden                
                while(i<combinationList.size() && bRet)
                {
                    j=0;
                    listIndex = combinationList.get(i);
                    while(j<listIndex.size() && bRet)
                    {
                        nValue = listIndex.get(j);
                        if(nValue<=nIndexValue && nIndexValue!=0)
                            bRet = false;
                        j++;
                        nIndexValue=nValue;
                        
                    }
                    i++;
                }
            }
        }catch(NullPointerException e){}
        
        return bRet;
    }
    public LinkedList<LinkedList<Integer>> MergeList()
    {
        LinkedList<LinkedList<Integer>> combinedRetList;
        LinkedList<LinkedList<Integer>> auxList, appendList = null;
        int nTotalSize,nIndex;
        
        nIndex = nTotalSize = 0;
        combinedRetList = null;
        
        //La primera combinación es equivalente.
        //El objetivo es 
        if(nElements >0)
        {            
            nTotalSize = (int) Math.pow(2, nElements);
            
            combinedRetList = new LinkedList<LinkedList<Integer>>();            
            nIndex =1;
            
            while(nIndex<nTotalSize)//nº de combinaciones
            {   
                appendList = new LinkedList<LinkedList<Integer>>();
                //Creamos todas las combinaciones posibles de llamadas
                for(int j=0;j<nElements;j++)
                {
                    // ej: teniendo 4 conjuntos, donde 0 significa que no se combina el conjunto
                    // y 1, que se combina: 0001 para i=1, 0010 para i=2, 0011 para i=3 etc.
                    if ( (nIndex & (int) Math.pow(2, j)) != 0)
                    {                            
                        auxList = combineList(combinationList.get(j), true);                            
                    }
                    else
                    {
                        auxList = combineList(combinationList.get(j), false);
                    }

                    appendList = append(appendList, auxList);

                }
                //Hay que borrar la primera que da jaleo siempre.
                nIndex++;
                //Mezclamos las listas que vayan saliendo
                //Añadimos appendList al final de 
                combinedRetList.addAll(appendList);
            }
        }
        
        return combinedRetList;
    }

    //Hace todas las combinaciones posibles
    private LinkedList<LinkedList<Integer>> combineList(LinkedList<Integer> list, boolean bCombine)
    {
        LinkedList<LinkedList<Integer>> listRet;
        LinkedList<Integer> listInsert;
        Permutation permutator;
        
        if(bCombine)
        {
            //Hay que hacer todas las combinaciones posibles de este elemento:
            permutator = new Permutation();
            listRet = null;
            listInsert = null;

            permutator.permute(list, 0);
            listRet = permutator.getList();
             //Borramos la primera, porque únicamente nos sirven las combinaciones que sean 
            //distintas al original
            listRet.remove(0);             
        }
        else
        {
            listRet = new LinkedList<LinkedList<Integer>>();
            listRet.add(list);
        }
        
        return listRet;
    }

    private LinkedList<LinkedList<Integer>> append(LinkedList<LinkedList<Integer>> appendList, LinkedList<LinkedList<Integer>> auxList) {
        LinkedList<LinkedList<Integer>> listRet;
        LinkedList<Integer> retIndex = null;
        listRet = null;
        
        if(appendList.size()>0)
        {   
            listRet = new LinkedList<LinkedList<Integer>>();
            
            for(int i=0;i<appendList.size();i++)
            {
                LinkedList<Integer> listIndex = appendList.get(i);
                for(int j=0;j<auxList.size();j++)
                {
                    retIndex = addLast(listIndex, auxList.get(j));
                    listRet.add(retIndex);
                }                
            }
        }
        else
        {
            listRet = auxList;
        }

        return listRet;
    }
    //Añade a la lista de enteros ya existente, la nueva lista
    private LinkedList<Integer> addLast(LinkedList<Integer> listIndex, LinkedList<Integer> auxList) 
    {
        LinkedList<Integer> retList = null;
        
        retList = new LinkedList<Integer>();
        retList.addAll(listIndex);
        
        for(int j=0;j<auxList.size();j++)
        {
            retList.addLast(auxList.get(j));
        }
            
       return retList;
    }

    int CombSize() {
        return combinationList.size();
    }

    LinkedList<Integer> getSetComb(int i) 
    {
        LinkedList<Integer> retList;
        
        if(i<=CombSize())
            retList = combinationList.get(i);
        else 
            retList = null;
        
        return retList;
    }

    void clear() {        
        nElements=0;
        combinationList.clear();        
    }
}
