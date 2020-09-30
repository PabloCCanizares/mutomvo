/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ApiBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author user
 */
public class ApiBuilder
{
    List<IAPIMutation> MuOperationList;
    
    public ApiBuilder()
    {
        MuOperationList = new ArrayList<IAPIMutation>();
        //Aqui con la configuracion, que vamos a hacer un p** singleton
        //generamos todas las operaciones que vayamos a realizar
        IAPIMutation MoveOperation = new MoveOperation();
        IAPIMutation DelOperation = new DeleteOperation();
        IAPIMutation MerOperation = new ShuffleOperation();
        IAPIMutation RepOperation = new ReplacementOperation();
        
        MuOperationList.add(MoveOperation);
        MuOperationList.add(DelOperation);
        MuOperationList.add(MerOperation);
        MuOperationList.add(RepOperation);
    }          

    public List<MutatedOperator> doMutation(MutationOperator Operator) 
    {
        List<MutatedOperator> MutatedOp = new ArrayList<MutatedOperator>();
        int nSize = MuOperationList.size();
        for(int i = 0; i< nSize ;i++)
        {
            List<MutatedOperator> MutatedLocal;
            
            //Obtenemos la lista de mutantes y la mezclamos con la total
            IAPIMutation Operation = MuOperationList.get(i);
            
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
