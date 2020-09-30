/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators.Checker;

import mutomvo.Mutation.Operators.MutationOperator;

/**
 * Comprueba inconsistencias en la indexación de operadores
 * @author Pablo C. Cañizares
 */
public class OperatorChecker 
{
    //Igual para hacerlo más rápido deberíamos tener
    //todos los punteros a los tipos distintos de checkers
    //y mantenerlos en lugar de ir borrándolos
    public boolean checkOpEvolution(MutationOperator operator, String currentLine, int nIndex) 
    {
        //TODO: Aqui otra factoria please.
        boolean bRet = false;
        IOperatorChecker Checker = null;
        
        //En primer lugar se debe tratar de forma independiente cada tipo de operador
        switch(operator.getOperatorType())
        {
            case eARITHMETIC:
                Checker = new OperatorCheckerAR();
                
                break;
            //TODO: REsto de checkers
        }
        
        bRet = Checker.doCheck(operator,currentLine,nIndex);
        return bRet;
    }
    
}
