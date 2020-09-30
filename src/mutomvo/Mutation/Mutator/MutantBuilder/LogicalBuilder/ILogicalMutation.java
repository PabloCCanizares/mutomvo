/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.LogicalBuilder;

import java.util.List;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public interface ILogicalMutation {

    public abstract List<MutatedOperator> doMutation(MutationOperator Operator);
}
