/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.RelationalBuilder;

import java.util.List;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author PAblo C. Cañizares
 */
public interface IRelationalMutation 
{
    public abstract List<MutatedOperator> doMutation(MutationOperator Operator);
}