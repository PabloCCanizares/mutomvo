/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.Replacement;

import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Mutator.MutantBuilder.ArithmeticBuilder.IArithmeticMutation;
import mutomvo.Mutation.Operators.Method.Arithmetic.ArithmeticOperator;
import mutomvo.Mutation.Operators.Method.Arithmetic.EnumARTypes;
import mutomvo.Mutation.Operators.Method.Arithmetic.EnumBinArithmetic;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class AORuOperation implements IArithmeticMutation{
    
    List<EnumBinArithmetic> ArithmeticList;

    public AORuOperation() 
    {
        //TODO: Cambiar a un foreach
        ArithmeticList = new ArrayList<EnumBinArithmetic>();

        for (EnumBinArithmetic eENUM : EnumBinArithmetic.values()) 
        {
           if(eENUM.getType() == EnumARTypes.eARType_Unary)
                ArithmeticList.add(eENUM);
        }     
    }

    @Override
    public List<MutatedOperator> doMutation(MutationOperator Operator) {
        List<MutatedOperator> muList = new ArrayList<MutatedOperator>();
        
        String strEnum, strOperator;
        //Hay que tener en cuenta que algunos operandos
        //no pueden ser combinados con otros i =+4; -> solo por -

        if(Operator.getOperatorType() == EnumOperator.eARITHMETIC)
        {
            ArithmeticOperator oArithOp = (ArithmeticOperator) Operator;
            
            if(oArithOp.getEnum().getType() == EnumARTypes.eARType_Unary && !Operator.isOperatorInsertion())
            {
                //Aqui tenemos que saber que tipo es, si es unario, binario o short
                //Se me ocurre hacerlo con el constructor doble ... 
                for (int i = 0; i < ArithmeticList.size(); i++) 
                {
                    EnumBinArithmetic eEnum = ArithmeticList.get(i);

                    strEnum = eEnum.toString();
                    strOperator = Operator.getToken();
                    if(!strEnum.equals(strOperator))
                    {
                        MutatedOperator muOperator = new MutatedOperator(Operator);
                        muOperator.mutate(eEnum.toString(), EnumMuOperation.eAORu);
                        if(!eEnum.toString().isEmpty())//ShotDown
                            muList.add(muOperator);
                    }
                }
            }
        }

        return muList;
    }
}
