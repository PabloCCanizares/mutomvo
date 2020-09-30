/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutomvo.Mutation;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author PAblo C. Cañizares
 */
public class Mutant 
{
    EnumOperator MutantHighType;
    String MutantCategory;
    String MutantName;                      //Name of mutant 
    List<MutatedOperator> OperatorsList=null;   //List of mutations to make

    public EnumOperator getMutantHighType() {
        return MutantHighType;
    }

    public void setMutantHighType(EnumOperator MutantHighType) {
        this.MutantHighType = MutantHighType;
    }

    public void setMutantName(String MutantNameIn) 
    {
       MutantName = MutantNameIn;
    }

    public void insertMutatedOperator(MutatedOperator Operator) 
    {
        if(OperatorsList == null)
        {
            OperatorsList = new ArrayList<MutatedOperator>();
        }
        OperatorsList.add(Operator);
    }

    public boolean hasNext() 
    {
        boolean bRet = false;
        
        if(OperatorsList != null)
            bRet = (OperatorsList.size()>=1);            
        return bRet;
    }

    public MutatedOperator getNext() 
    {
        MutatedOperator ret = null;
        
        if(hasNext())
            ret = OperatorsList.remove(0);
                    
        return ret;
    }
    public MutatedOperator getHead() 
    {
        MutatedOperator ret = null;
        
        if(hasNext())
            ret = OperatorsList.get(0);
                    
        return ret;
    }    

    public String getMutantName() {
        return MutantName;
    }
    
}
