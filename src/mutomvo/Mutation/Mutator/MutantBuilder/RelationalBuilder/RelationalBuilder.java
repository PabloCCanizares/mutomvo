/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.RelationalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author user
 */
public class RelationalBuilder {
    
    List<IRelationalMutation> MuOperationList;
        

    public RelationalBuilder()
    {
        MuOperationList = new ArrayList<IRelationalMutation>();
        //Aqui con la configuracion, que vamos a hacer un p** singleton
        //generamos todas las operaciones que vayamos a realizar
        IRelationalMutation ROROperation = new ROROperation();
        MuOperationList.add(ROROperation);
    }   
        

    public List<MutatedOperator> doMutation(MutationOperator Operator) 
    {
        List<MutatedOperator> MutatedOp = new ArrayList<MutatedOperator>();
        int nSize = MuOperationList.size();
        for(int i = 0; i< nSize ;i++)
        {
            List<MutatedOperator> MutatedLocal;
            
            //Obtenemos la lista de mutantes y la mezclamos con la total
            IRelationalMutation Operation = MuOperationList.get(i);
            
            MutatedLocal = Operation.doMutation(Operator);
            
            Merge(MutatedOp,MutatedLocal);
        }
        
        int i=0;

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
