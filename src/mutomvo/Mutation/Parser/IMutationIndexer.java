/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser;

import java.io.File;
import java.util.List;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.Operators.MutationOperatorCharList;

/**
 * Indexa los operadores de mutacion encontrados en el código y los devuelve en una lista.
 * Permite encapsular distintos métodos de detección bajo la misma interfaz.
 * @author PAblo C. Cañizares
 */
public interface IMutationIndexer 
{    
    List<MutationOperator> doIndex(MutationOperatorCharList oCharGenerator, File srcFile) throws MutomvoException;
}
