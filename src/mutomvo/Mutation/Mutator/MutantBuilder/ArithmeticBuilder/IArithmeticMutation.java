/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder;

import java.util.List;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author User
 */
public interface IArithmeticMutation 
{
    public abstract List<MutatedOperator> doMutation(MutationOperator Operator);
}
