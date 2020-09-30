/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.AssigmentBuilder;

import java.util.List;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author PAblo C. Cañizares
 */
public interface IAssignmentMutation 
{
    public abstract List<MutatedOperator> doMutation(MutationOperator Operator);
}