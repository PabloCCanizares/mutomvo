/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Deletion;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.IArithmeticMutation;
import mutomvo.Mutation.Operators.Method.Arithmetic.ArithmeticOperator;
import mutomvo.Mutation.Operators.Method.Arithmetic.EnumARTypes;
import mutomvo.Mutation.Operators.Method.Arithmetic.EnumBinArithmetic;
import mutomvo.Mutation.Operators.Method.Arithmetic.EnumShortArithmetic;
import mutomvo.Mutation.Operators.Method.Arithmetic.IEnumArithmetic;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class AODsOperation implements IArithmeticMutation {
 //List<EnumBinArithmetic> ArithmeticList;
    List<IEnumArithmetic> ArithmeticList;

    public AODsOperation() 
    {
        //TODO: Cambiar a un foreach
        //ArithmeticList = new ArrayList<EnumBinArithmetic>();
        ArithmeticList = new ArrayList<IEnumArithmetic>();
        
        for (EnumShortArithmetic eENUM : EnumShortArithmetic.values()) 
        {           
           if(eENUM.getType() == EnumARTypes.eARType_SHORT)
                ArithmeticList.add(eENUM);
        }        
    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();
        
        String strEnum, strOperator;
        //TODO:: Hay que tener en cuenta que algunos operandos
        //no pueden ser combinados con otros i =+4; -> solo por -

        if(Operator.getOperatorType() == EnumOperator.eARITHMETIC)
        {
            ArithmeticOperator oArithOp = (ArithmeticOperator) Operator;
            
            if(oArithOp != null && oArithOp.getEnum().getType() == EnumARTypes.eARType_SHORT && Operator.isOperatorDeletion())
            {

                MutatedOperator muOperator = new MutatedOperator(Operator);
                muOperator.mutate("", EnumMuOperation.eAODs);
                muList.add(muOperator);
            }
        }

        return muList;
    }   
}
