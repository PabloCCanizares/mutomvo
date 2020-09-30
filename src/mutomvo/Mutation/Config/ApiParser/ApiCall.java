/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser;

import java.util.LinkedList;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ApiCall 
{
    String strApiName;
    String strCallName;
    int nArguments;
    boolean bShuffle;
    LinkedList<String> ArgumentsTypeList;
    LinkedList<Boolean> StatusTypeList;
    LinkedList<LinkedList<Integer>> shuffleList;
    LinkedList<LinkedList<Integer>> shuffleAuxList;
    LinkedList<ApiCall>replacementList;
    LinkedList<ApiCall>repAuxList;
    
    public LinkedList<String> getArgumentsTypeList() {
        return ArgumentsTypeList;
    }

    public void setArgumentsTypeList(LinkedList<String> ArgumentsTypeList) {
        this.ArgumentsTypeList = ArgumentsTypeList;
    }
    
    public ApiCall(String strApi, String strCall, LinkedList<String> argList)
    {
        strApiName = strApi;
        strCallName = strCall;
        ArgumentsTypeList = argList;
        
        replacementList = new LinkedList<ApiCall>();
        repAuxList = new LinkedList<ApiCall>();
        if(ArgumentsTypeList != null)
        {
            StatusTypeList = new LinkedList<Boolean>();
            nArguments = ArgumentsTypeList.size();
            shuffleList = null;

            this.bShuffle = false;
            for(int i=0;i<nArguments;i++)
            {
               StatusTypeList.add(i, false); 
            }
        }

    }
    public boolean isShuffle() {
        return bShuffle;
    }

    public void setShuffle(boolean bShuffle) {
        this.bShuffle = bShuffle;
    }

    public int getnArguments() {
        return nArguments;
    }

    public void setnArguments(int nArguments) {
        this.nArguments = nArguments;
    }

    public String getApiName() {
        return strApiName;
    }

    public void setApiName(String strApiName) {
        this.strApiName = strApiName;
    }

    public String getCallName() {
        return strCallName;
    }

    public void setCallName(String strCallName) {
        this.strCallName = strCallName;
    }

    public void setDynamicParam(int iPosition) {
        StatusTypeList.set(iPosition, true);
    }

    public LinkedList<Boolean> getParamStatus() {
        return StatusTypeList;
    }

    void setParamsList(LinkedList<LinkedList<Integer>> finalList) {
        
        if(finalList != null)
        {
            bShuffle = true;
            shuffleList = finalList;
            shuffleAuxList = new LinkedList<LinkedList<Integer>>();
            shuffleAuxList.addAll(shuffleList);
        }
    }

    public boolean IsDynamicParam(int i) {
        return StatusTypeList.get(i);
    }

    boolean hasNextShuffle() {
        return (shuffleList.size()>0);
    }
    LinkedList<Integer> getNextShuffle() 
    {
        return shuffleList.removeFirst();
    }

    void setReplacementCall(ApiCall apiCallAux) {
        replacementList.add(apiCallAux);
    }

    boolean isReplacement() {
        return (replacementList.size()>0);
    }

    boolean hasNextReplacement() {
        return (repAuxList.size()>0);
    }
    
    ApiCall getNextReplacement() {
        return repAuxList.removeFirst();
    }    
    void resetRepList()
    {
        repAuxList.addAll(replacementList);
    }
    void resetShuffleList() {
        shuffleList.addAll(shuffleAuxList);
    }    
    public void setAllParamsTypeUnk()
    {
        int nSize;
        if(ArgumentsTypeList != null)
        {
            nSize = ArgumentsTypeList.size();
            ArgumentsTypeList.clear();
            for(int i=0;i<nSize;i++)
            {
                ArgumentsTypeList.add(i, "unknow");
            }
        }
    }

}
