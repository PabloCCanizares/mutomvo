/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.CDTParser;

import mutomvo.Mutation.Parser.CDTParser.CDT.SourceParser;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.Mutation.Parser.CDTParser.indexers.CDTApiIndexer;
import mutomvo.Mutation.Parser.CDTParser.indexers.CDTGeneralIndexer;
import mutomvo.Mutation.Parser.IMutationIndexer;
import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.Operators.MutationOperatorCharList;

/**
 *
 * @author Pablo C. Cañizares
 */
public class CDTMutantIndexer implements IMutationIndexer {

    SourceParser srcParser;
    CDTApiIndexer apiIndexer;
    CDTGeneralIndexer genIndexer;

    public CDTMutantIndexer() {
        srcParser = new SourceParser();
        apiIndexer = new CDTApiIndexer();
        genIndexer = new CDTGeneralIndexer();
    }

    public List<MutationOperator> doIndex(MutationOperatorCharList oCharGenerator, File srcFile) throws MutomvoException {
        List<MutationOperator> retList = null;
        List<MutationOperator> retListAux = null;
        List<MutationOperator> retListAux2 = null;
        List<MutationOperator> retBList = null;
        List<MutationOperator> retUList = null;
        List<MutationOperator> retAPIList = null;

        if (srcParser.parse(srcFile)) {

            LinkedList<IASTBinaryExpression> binlist = srcParser.getBinaryList();
            LinkedList<IASTUnaryExpression> unlist = srcParser.getUnaryList();
            LinkedList<IASTNode> apilist = srcParser.getApiList();

            System.out.println("Total unary operators: " + Integer.toString(unlist.size()));
            System.out.println("Total API operators: " + Integer.toString(apilist.size()));

            if (binlist != null) {
                System.out.println("Total binary operators found : " + Integer.toString(binlist.size()));
                retBList = genIndexer.generateBinaryList(binlist);

                if (retBList != null) {
                    System.out.println("Total binary operators translated, ready to save to disk : " + Integer.toString(retBList.size()));
                } else {
                    System.out.println("CDTMutantIndexer::doIndex - WARNING! Some of the mutant operator list is null");
                }
            }
            if (unlist != null) {
                retUList = genIndexer.generateUnaryList(unlist);
            }
            if (apilist != null) {
                retAPIList = apiIndexer.generateApiList(apilist);
            }

            retListAux = MergeLists(retBList, retUList);
            retList = MergeLists(retListAux, retAPIList);
        }

        return retList;
    }

    //Añadir a una lista los valores de la otra y return
    private List<MutationOperator> MergeLists(List<MutationOperator> retBList, List<MutationOperator> retUList) {

        List<MutationOperator> retList = null;

        if (retBList != null || retUList != null) {
            if (retBList == null) {
                retList = retUList;
            } else if (retUList == null) {
                retList = retBList;
            } else {
                retList = new LinkedList<MutationOperator>();
                retList.addAll(retUList);
                retList.addAll(retBList);
            }
        }

        return retList;
    }
    //TODO: Tratar de que no se produzcan errores con simtime *,%, ...
    // No mutar los printf

}
