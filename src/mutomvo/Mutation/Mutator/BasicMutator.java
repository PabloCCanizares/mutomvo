/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator;

import java.util.List;
import mutomvo.Mutation.Mutant;
import mutomvo.Mutation.Mutator.MutantBuilder.MutantBuilder;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author User
 */
public class BasicMutator extends Mutator {

    int MutantIndex;

    public BasicMutator(List<MutationOperator> OperatorsListIn) {
        super(OperatorsListIn);
        MutantIndex = 0;
        MutantBuilder = new MutantBuilder(null);

    }

    public int getSize() {
        int nRet = 0;

        if (MutantList != null) {
            nRet = MutantList.size();
        }
        return nRet;
    }

    @Override
    public boolean hasNext() {
        boolean bhasNext = (MutantList.size()) >= 1;

        return bhasNext;
    }

    @Override
    public Mutant getNext() {
        return MutantList.remove(0);
    }

    public void generateMutants() {

        for (int i = 0; i < OperatorList.size(); i++) {

            //System.out.printf("Rolling %d\n", i);
           
            List<MutatedOperator> MutatedOperatorList =null;
            MutationOperator Operator = OperatorList.get(i);

            //Comprobamos los tipos con la config
           
            if(Operator != null)
            {
                MutatedOperatorList = MutantBuilder.generateMutants(Operator);
            

                if(MutatedOperatorList != null && MutatedOperatorList.size()>0)
                    //convertirlos e insertarlos en la lista de mutantes
                    convertAndInsertMutants(MutatedOperatorList);
             }
        }
    }

    private void convertAndInsertMutants(List<MutatedOperator> MutatedOperatorList) {
        for (int i = 0; i < MutatedOperatorList.size(); i++) {
            MutantIndex++;
            String result = String.format("Mutant_ %2d", MutantIndex);
            String MutantName = "Mutant_" + Integer.toString(MutantIndex);
            MutatedOperator Operator = MutatedOperatorList.get(i);

            Mutant MutantToInsert = new Mutant();
            MutantToInsert.setMutantName(MutantName);
            MutantToInsert.insertMutatedOperator(Operator);
            MutantToInsert.setMutantHighType(Operator.getOperatorType());
            MutantList.add(MutantToInsert);
        }
    }
}
