/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ShiftBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder.ILogicalMutation;
import mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder.LOIOperation;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.Logical.EnumUnLogical;
import mutomvo.Mutation.Operators.Method.Shift.EnumShift;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class ShiftBuilder implements IShiftMutation{

    List<IShiftMutation> MuOperationList;
    
    public ShiftBuilder() {
        MuOperationList = new ArrayList<IShiftMutation>();
        //Aqui con la configuracion, que vamos a hacer un p** singleton
        //generamos todas las operaciones que vayamos a realizar

        if (MutationCfg.getInstance().isGenerateShift()) {
            MuOperationList.add(new SfOROperation());
        }
        //if(MutationCfg.getInstance().isGenerateLOD())
        //    MuOperationList.add(new LODOperation());    
    }

    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> MutatedOp = new ArrayList<MutatedOperator>();
        int nSize = MuOperationList.size();
        for (int i = 0; i < nSize; i++) {
            List<MutatedOperator> MutatedLocal;

            //Obtenemos la lista de mutantes y la mezclamos con la total
            IShiftMutation Operation = MuOperationList.get(i);

            MutatedLocal = Operation.doMutation(Operator);

            Merge(MutatedOp, MutatedLocal);
        }

        return MutatedOp;
    }

    private void Merge(List<MutatedOperator> MutatedOp, List<MutatedOperator> MutatedLocal) {
        int nSize = MutatedLocal.size();

        if (MutatedOp != null && MutatedLocal != null) {
            for (int i = 0; i < nSize; i++) {
                MutatedOperator oMutated = MutatedLocal.get(i);

                //Merge and set the Type of mutant
                //oMutated.setOperatorClass(EnumOperatorClass.eGENERALCLASS);
                MutatedOp.add(oMutated);
            }
        }
    }
    
}
