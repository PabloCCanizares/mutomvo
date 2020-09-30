/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator;

import java.util.ArrayList;
import mutomvo.Mutation.Mutator.MutantBuilder.MutantBuilder;
import mutomvo.Mutation.Operators.MutationOperator;
import java.util.List;
import mutomvo.Mutation.Mutant;

/**
 *
 * @author usuario_local
 */
public abstract class Mutator 
{
    protected List<MutationOperator> OperatorList; 
    protected List<Mutant> MutantList;
    protected MutantBuilder MutantBuilder;
    
            
    public Mutator(List<MutationOperator> OperatorsListIn)
    {
        this.MutantList = new ArrayList<Mutant>();
        OperatorList= OperatorsListIn;
        
    }
    
    public abstract void generateMutants();
    public abstract boolean hasNext();
    public abstract int getSize();
    public abstract Mutant getNext();
}
