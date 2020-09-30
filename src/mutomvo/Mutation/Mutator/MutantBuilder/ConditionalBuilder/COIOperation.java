/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ConditionalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.Conditional.EnumConditional;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class COIOperation implements IConditionalMutation{

     List<EnumConditional> ConditionalList;

    public COIOperation() 
    {

    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        if(Operator.isOperatorInsertion())
        {
            MutatedOperator muOperator = new MutatedOperator(Operator);
            muOperator.mutate(EnumConditional.eCOND_NEG.toString(), EnumMuOperation.eCOI);
            muList.add(muOperator);
        }

        return muList;
    }
}
