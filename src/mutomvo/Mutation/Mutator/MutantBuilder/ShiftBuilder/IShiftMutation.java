/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ShiftBuilder;

import java.util.List;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public interface IShiftMutation 
{
    public abstract List<MutatedOperator> doMutation(MutationOperator Operator);
}