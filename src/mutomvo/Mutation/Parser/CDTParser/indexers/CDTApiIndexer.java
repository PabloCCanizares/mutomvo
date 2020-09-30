/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.CDTParser.indexers;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBinaryExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarationStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTExpressionStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.OmnetOperator;
import mutomvo.Mutation.Operators.MutationOperator;

/**
 *
 * @author pablo
 */
public class CDTApiIndexer {

    public List<MutationOperator> generateApiList(LinkedList<IASTNode> apilist) {
        List<MutationOperator> retList = null;

        if (apilist.size() > 0) {
            retList = new LinkedList<MutationOperator>();
            for (int i = 0; i < apilist.size(); i++) {
                MutationOperator muOperator;
                IASTNode exp = apilist.get(i);
                muOperator = handleApiExpression(exp); //devuelve una lista, vas añadiendo los valores a esta lista.
                if (muOperator != null) {
                    if(!MutationCfg.getInstance().isBadLine(muOperator.getLineNumber()))
                        retList.add(muOperator);
                }
            }
        }

        return retList;
    }

    private MutationOperator handleApiExpression(IASTNode node) {
        MutationOperator opRet;
        int nOperator, nLen;
        boolean bFound;
        IASTFunctionCallExpression exp;

        opRet = null;
        exp = null;

        if (node instanceof IASTExpressionStatement) {
            //Ejemplo:
            // - IASTExpressionStatement: stream = fproc("/proc/meminfo","r")
            // - IASTFunctionCallExpression: fproc("/proc/mem...
            //En este caso hay que buscar entre los nodos hijo
            //FunctionCallExpression
            exp = findChild(node);
            
            try
            {
                //TODO: Extraer elementos.
                //CPPASTIdExpression test =(CPPASTIdExpression) (exp.getArguments())[0];
                //IType type = test.getExpressionType();

                if (exp != null) {
                    //Tenemos el hijo. Ahora toca extraer la información y crear el objeto
                    opRet = CreateApiMutantOperator((IASTFunctionCallExpression) exp,node);
                }
            }catch(ClassCastException e){}

        }
        else if(node instanceof CPPASTDeclarationStatement)
        {
            /*CPPASTDeclarationStatement n = (CPPASTDeclarationStatement) node;
            CPPASTSimpleDeclaration ns = (CPPASTSimpleDeclaration) n.getDeclaration();
            String strName= n.getDeclarators()[0].getName().toString();
            int i=0;*/
            
            opRet = CreateApiMutantOperator(null, node);
        }
        
        
        return opRet;
    }

    private MutationOperator CreateApiMutantOperator(IASTFunctionCallExpression exp, IASTNode node) {
        MutationOperator opRet;
        String strName;
        EnumOperatorClass eClass;
        
        if(exp!= null && node instanceof CPPASTExpressionStatement && ((CPPASTExpressionStatement)node).getExpression() instanceof CPPASTBinaryExpression)
            strName = exp.getRawSignature();
        else            
            strName = node.getRawSignature(); //Para sacar sçolo el nombre de la funcion, exp.getfunctionname
        
        opRet = CreateOp(strName, node);

        //IF the operator is not null
        //Insert the type of mutation operator
        if(opRet != null)
        {
            if(MutationCfg.getInstance().isCallApi(strName))
            {
                eClass = MutationCfg.getInstance().getApiClass(strName);
                opRet.setOperatorClass(eClass);
            }
        }
        
        return opRet;
    }

    private MutationOperator CreateOp(String strName, IASTNode node) {
        MutationOperator opRet;
        int nInit, nEnd, nSize, nLine;
        opRet = new OmnetOperator( strName , node);

        nInit = nEnd = nSize = nLine = 0;
         
        //Aparición del operador de mutación.
        nInit = node.getFileLocation().getNodeOffset();
        nEnd = node.getFileLocation().getNodeOffset() + node.getFileLocation().getNodeLength();

        nSize = nEnd - nInit;
        nLine = node.getFileLocation().getStartingLineNumber();

        opRet.setnPosInit(nInit);
        opRet.setnPosEnd(nEnd);
        opRet.setLine(nLine);

        if (nInit > nEnd) {
            nEnd = 0;
        }
        return opRet;
    }
    //Normalmente esta función unicamente va a tener un hijo.
    //por si acaso, lo dejamos flexible.
    //Todo: probar con algo del estilo bRet = SIMCAN_request ....
    private IASTFunctionCallExpression findChild(IASTNode node) {
        int nLen;
        IASTExpressionStatement exp;
        boolean bFound;
        IASTNode[] childs;
        IASTFunctionCallExpression nodeRet;

        nodeRet = null;
        nLen = 0;
        bFound = false;
        exp = (IASTExpressionStatement) node;

        //buscamos el hijo
        childs = exp.getChildren();

        if (childs != null) {
            nLen = childs.length;
            for (int i = 0; i < nLen; i++) {
                IASTNode nodeChild = childs[i];

                if (nodeChild instanceof IASTFunctionCallExpression) 
                {
                    bFound = true;
                    nodeRet = (IASTFunctionCallExpression) nodeChild;
                }else if(nodeChild instanceof CPPASTBinaryExpression)
                {
                    CPPASTBinaryExpression expBin = (CPPASTBinaryExpression) nodeChild;
                    
                    if(expBin.getOperand2() instanceof IASTFunctionCallExpression)
                    {
                        nodeRet = (IASTFunctionCallExpression) expBin.getOperand2();
                    }                    
                }
            }
        }

        return nodeRet;
    }

}
