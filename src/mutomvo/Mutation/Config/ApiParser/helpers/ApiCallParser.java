/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser.helpers;

import java.util.Arrays;
import java.util.LinkedList;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.internal.core.dom.parser.ASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBinaryExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarationStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIdExpression;
import mutomvo.Mutation.Config.ApiParser.ApiCall;
import mutomvo.Mutation.Operators.MutatedOperator;

/**
 *
 * @author pablo
 */
public class ApiCallParser {
    

    public  ApiCall createApiCall(String strCollectionName, String strApiCall) {
        ApiCall apiCall;
        int nFirstPar, nLastPar;
        LinkedList <String> paramList;
        String[] tokens;
        String strCall, strToken, strParameters, delims, delimsOp;
        boolean bShuffle = false;
        
        delims = "[,]";
        delimsOp = "[_]";        
        apiCall = null;
        paramList = null;
        
        strToken = strApiCall;
        strToken = strToken.replaceAll(" ", "");
        nFirstPar = strToken.indexOf('(');
        nLastPar = strToken.indexOf(");");

        if (nFirstPar != -1 && nLastPar != -1) {
            strCall = strToken.substring(0, nFirstPar);
            strParameters = strToken.substring(nFirstPar + 1, nLastPar);

            if(strParameters.length()>0)
            {
                //Tenemos la llamada              
                tokens = strParameters.split(delims);
                paramList = new LinkedList(Arrays.asList(tokens));
            }
            apiCall = new ApiCall(strCollectionName, strCall, paramList);
            
        }
        return apiCall;
    }

    public ApiCall createApiCall(String collectionName, MutatedOperator Operator) {
        ApiCall apiCall;
        IASTNode node;
        String strName;
        int nArguments;
        LinkedList <String> paramList = null;
        apiCall = null;
        node = null;
        
        try
        {
            //sin terminar!!!
            node = Operator.getNode(); 
            if(node instanceof IASTExpressionStatement)
            {
                IASTFunctionCallExpression exp = findChild(node); 

                strName = exp.getFunctionNameExpression().getRawSignature();
                nArguments = exp.getArguments().length;
                if(nArguments>0)
                {
                    paramList = new LinkedList();
                    
                    for(int i=0;i<nArguments;i++)
                    {
                        CPPASTIdExpression test;
                        //TODO: Falta, básicamente porque no encuentra los tipos :( . Habría que acudir al parser original con las
                        //variables almacenadas.
                        if((exp.getArguments())[i] instanceof CPPASTIdExpression)
                        {
                            test=(CPPASTIdExpression) (exp.getArguments())[i];                            
                            IType type = test.getExpressionType();
                            paramList.add(type.toString());
                            
                        }/*else if((exp.getArguments())[i] instanceof CPPASTBinaryExpression)
                        {
                        
                        }*/
                        else
                        {
                            paramList.add("unknown");      
                        }
                                          
                    }                    
                }
                apiCall = new ApiCall(collectionName, strName, paramList);
            }    
            else
            {
                //This case is special:
                //The CDT is detecting some API calls like CPPASTDeclarationStatement
                //So, we are going to parse it handly :(!
                CPPASTDeclarationStatement expression = (CPPASTDeclarationStatement) node;
            
                strName = expression.getRawSignature();
                apiCall = createApiCall(collectionName, strName);
                apiCall.setAllParamsTypeUnk();
            }
        }catch(NullPointerException e)
        {
            apiCall = null;
        }

        return apiCall;
    }
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

                if (nodeChild instanceof IASTFunctionCallExpression) {
                    bFound = true;
                    nodeRet = (IASTFunctionCallExpression) nodeChild;
                }
            }
        }

        return nodeRet;
    }
}
