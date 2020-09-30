/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.SourceCodeIndexer;

import mutomvo.Mutation.Parser.SourceCodeIndexer.Elements.CodeBlock;
import java.io.File;
import java.util.List;
import mutomvo.Exceptions.MutomvoException;

/**
 *  Interfaz que permite enmascarar varios tipos de analizadores de código, para obtener una mayor o menor precisión
 * @author PAblo C. Cañizares
 */
public interface ISourceCodeAnalyzer 
{
    public boolean doAnalyze(File srcFile) throws MutomvoException;

    public List<CodeBlock> getList();
}
