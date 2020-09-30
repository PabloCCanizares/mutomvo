/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class LogicalBuilder {

    List<ILogicalMutation> MuOperationList;

    public LogicalBuilder() {
        MuOperationList = new ArrayList<ILogicalMutation>();

        if (MutationCfg.getInstance().isGenerateLOR()) {
            MuOperationList.add(new LOROperation());
        }
        if (MutationCfg.getInstance().isGenerateLOI()) {
            MuOperationList.add(new LOIOperation());
        }
        if (MutationCfg.getInstance().isGenerateLOD()) {
            MuOperationList.add(new LODOperation());
        }
    }

    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> MutatedOp = new ArrayList<MutatedOperator>();
        int nSize = MuOperationList.size();
        for (int i = 0; i < nSize; i++) {
            List<MutatedOperator> MutatedLocal;

            //Obtenemos la lista de mutantes y la mezclamos con la total
            ILogicalMutation Operation = MuOperationList.get(i);

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

                MutatedOp.add(oMutated);
            }
        }
    }
}
