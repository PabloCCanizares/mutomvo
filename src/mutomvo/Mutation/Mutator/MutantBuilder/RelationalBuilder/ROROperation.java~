/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.RelationalBuilder;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.Relational.EnumRelational;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author user
 */
public class ROROperation implements IRelationalMutation
{
 List<EnumRelational> RelationalList;

    public ROROperation() 
    {
        RelationalList = new ArrayList<EnumRelational>();

        for (EnumRelational eENUM : EnumRelational.values()) 
        {
           RelationalList.add(eENUM);
        }        
    }

    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        //TODO:: Hay que tener en cuenta que algunos operandos
        //no pueden ser combinados con otros i =+4; -> solo por -
        //
        for (int i = 0; i < RelationalList.size(); i++) {
            EnumRelational eEnum = RelationalList.get(i);
            
            if(!eEnum.toString().equals(Operator.getToken()) && !Operator.isOperatorInsertion())
            {
                MutatedOperator muOperator = new MutatedOperator(Operator);
                muOperator.mutate(eEnum.toString(), EnumMuOperation.eROR);
                if(!eEnum.toString().isEmpty())
                    muList.add(muOperator);
            }
        }

        return muList;
    }    
}
