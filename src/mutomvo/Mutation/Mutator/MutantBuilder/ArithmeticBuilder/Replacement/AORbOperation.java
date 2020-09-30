/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Replacement;

import mutomvo.Mutation.Operators.Method.Arithmetic.EnumBinArithmetic;
import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.IArithmeticMutation;
import mutomvo.Mutation.Operators.Method.Arithmetic.ArithmeticOperator;
import mutomvo.Mutation.Operators.Method.Arithmetic.EnumARTypes;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author User
 */
public class AORbOperation implements IArithmeticMutation {

    List<EnumBinArithmetic> ArithmeticList;

    public AORbOperation() 
    {
        ArithmeticList = new ArrayList<EnumBinArithmetic>();

        for (EnumBinArithmetic eENUM : EnumBinArithmetic.values()) 
        {
           if(eENUM.getType() == EnumARTypes.eARType_BIN)
                ArithmeticList.add(eENUM);
        }
        
    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();

        //TODO:: Hay que tener en cuenta que algunos operandos
        //no pueden ser combinados con otros i =+4; -> solo por -

        if(Operator.getOperatorType() == EnumOperator.eARITHMETIC && !Operator.isOperatorInsertion())
        {
            ArithmeticOperator oArithOp = (ArithmeticOperator) Operator;
            
            if(oArithOp.getEnum().getType() == EnumARTypes.eARType_BIN)
            {
                //Aqui tenemos que saber que tipo es, si es unario, binario o short
                //Se me ocurre hacerlo con el constructor doble ... 
                for (int i = 0; i < ArithmeticList.size(); i++) 
                {
                    EnumBinArithmetic eEnum = ArithmeticList.get(i);

                    if(!eEnum.toString().equals(Operator.getToken()) )
                    {
                        MutatedOperator muOperator = new MutatedOperator(Operator);
                        muOperator.mutate(eEnum.toString(), EnumMuOperation.eAORb);
                        if(!eEnum.toString().isEmpty()&& !eEnum.toString().equals("%"))//ShotDown
                            muList.add(muOperator);
                    }
                }
            }
        }

        return muList;
    }
}
