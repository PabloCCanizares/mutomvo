/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutomvo.Mutation.Operators;

import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.Method.EnumOperator;
import mutomvo.Mutation.Operators.Method.OmnetOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class MutatedOperator extends MutationOperator
{
    EnumMuOperation Operation;
    int DiffSize;
    
    public MutatedOperator(MutationOperator Operator) 
    {
        super.Token = Operator.Token;
        super.eOperatorType = Operator.eOperatorType;
        super.nPosEnd = Operator.nPosEnd;
        super.nPosInit = Operator.nPosInit;
        super.nLineNumber = Operator.nLineNumber;
        super.oNode = Operator.oNode;
        super.eOperatorClass = Operator.eOperatorClass;
        super.eOperatorSubClass = Operator.eOperatorSubClass;
        Operator.eOperatorSubClass  = super.eOperatorSubClass;
        
        if(Operator.eOperatorSubClass != null)
            Operation = Operator.eOperatorSubClass;
    }

    public void setOperatorClass()
    {
        
    }
    public EnumOperator geteOperatorType() {
        return eOperatorType;
    }

    public void seteOperatorType(EnumOperator eOperatorType) {
        this.eOperatorType = eOperatorType;
    }

    public EnumMuOperation getOperation() {
        return Operation;
    }

    public void setOperation(EnumMuOperation Operation) {
        this.Operation = Operation;
    }

    public int getDiffSize() {
        return DiffSize;
    }

    public void setDiffSize(int DiffSize) {
        this.DiffSize = DiffSize;
    }

    public void mutate(String string, EnumMuOperation muOperation) 
    {
       DiffSize = Token.length() - string.length();
       
       Token = string;
       Operation = muOperation;

    }

    public EnumOperatorClass getOperatorClass() {
        
        return eOperatorClass;
    }

    public void setToken(String Token) {
        if(Token != null && !Token.isEmpty())
        this.Token = Token;
    }
}
