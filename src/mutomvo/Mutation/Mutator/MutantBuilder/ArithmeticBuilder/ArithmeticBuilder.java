/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder;

import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Replacement.AORsOperation;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Replacement.AORbOperation;
import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Deletion.AODsOperation;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Deletion.AODuOperation;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Insertion.AOIsOperation;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Insertion.AOIuOperation;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Replacement.AORuOperation;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ArithmeticBuilder 
{
    List<IArithmeticMutation> MuOperationList;
            
    public ArithmeticBuilder()
    {
        MuOperationList = new ArrayList<IArithmeticMutation>();


        IArithmeticMutation AORuOperation = new AORuOperation();
        
        if(MutationCfg.getInstance().isGenerateAODs())
            MuOperationList.add(new AODsOperation());         
        if(MutationCfg.getInstance().isGenerateAODu())
            MuOperationList.add(new AODuOperation()); 
        if(MutationCfg.getInstance().isGenerateAORb())
            MuOperationList.add(new AORbOperation()); 
        if(MutationCfg.getInstance().isGenerateAORs())
            MuOperationList.add(new AORsOperation());
        if(MutationCfg.getInstance().isGenerateAORu())
            MuOperationList.add(new AORuOperation());              
        if(MutationCfg.getInstance().isGenerateAOIs())
            MuOperationList.add(new AOIsOperation());
        if(MutationCfg.getInstance().isGenerateAOIu())    
            MuOperationList.add(new AOIuOperation());       
    }   
        

    public List<MutatedOperator> doMutation(MutationOperator Operator) 
    {
        List<MutatedOperator> MutatedOp = new ArrayList<MutatedOperator>();
        int nSize = MuOperationList.size();
        for(int i = 0; i< nSize ;i++)
        {
            List<MutatedOperator> MutatedLocal;
            
            //Obtenemos la lista de mutantes y la mezclamos con la total
            IArithmeticMutation Operation = MuOperationList.get(i);
            
            MutatedLocal = Operation.doMutation(Operator);
            
            Merge(MutatedOp,MutatedLocal);
        }
        
        
        return MutatedOp;
    }

    private void Merge(List<MutatedOperator> MutatedOp, List<MutatedOperator> MutatedLocal)
    {
        int nSize = MutatedLocal.size();
        
        for(int i=0;i<nSize;i++)
        {
            MutatedOperator oMutated = MutatedLocal.get(i);
            
            //Merge and set the Type of mutant
            //oMutated.setOperatorClass(EnumOperatorClass.eGENERALCLASS);
            MutatedOp.add(oMutated);
        }
    }

}
