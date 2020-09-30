/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.AssigmentBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class AssignmentBuilder {
    List<IAssignmentMutation> MuOperationList;
        

    public AssignmentBuilder()
    {
        MuOperationList = new ArrayList<IAssignmentMutation>();
        //Aqui con la configuracion, que vamos a hacer un p** singleton
        //generamos todas las operaciones que vayamos a realizar
        IAssignmentMutation ASOROperation = new ASOROperation();
        MuOperationList.add(ASOROperation);
    }   
        

    public List<MutatedOperator> doMutation(MutationOperator Operator) 
    {
        List<MutatedOperator> MutatedOp = new ArrayList<MutatedOperator>();
        int nSize = MuOperationList.size();
        for(int i = 0; i< nSize ;i++)
        {
            List<MutatedOperator> MutatedLocal;
            
            //Obtenemos la lista de mutantes y la mezclamos con la total
            IAssignmentMutation Operation = MuOperationList.get(i);
            
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
