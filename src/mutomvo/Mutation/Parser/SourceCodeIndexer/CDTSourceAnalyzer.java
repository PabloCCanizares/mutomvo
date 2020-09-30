/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.SourceCodeIndexer;

import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBinaryExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompoundStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarationStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTExpressionStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTForStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionCallExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDefinition;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIfStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTWhileStatement;
import mutomvo.Mutation.Mutant;
import mutomvo.Mutation.Parser.SourceCodeIndexer.Elements.CodeLine;
import mutomvo.Mutation.Parser.SourceCodeIndexer.Elements.MuPosition;
import mutomvo.Mutation.Operators.MutatedOperator;

/**
 *
 * @author pablo
 */
public class CDTSourceAnalyzer {

    MuPosition posUp;
    MuPosition posDown;
    MuPosition posMu;
    private String OriginalCode;
    private boolean moveUp;
    private boolean moveDown;
    private boolean hasDownInterCode;   //Indica si al mover una sentencia, existe código entre la 
    private boolean hasUpInterCode;     //sentencia a mover y el candidato up o down
    private boolean moveDownNeedSemicolon;
    private boolean moveUpNeedSemicolon;
    private boolean moveDownNeedBrackets;
    private boolean moveUpNeedBrackets;
    
    private static final int NODE_IN_THEN = 1;
    private static final int NODE_IN_ELSE = 2;
    
    public boolean isMoveDown() {
        return moveDown;
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public MuPosition getPosDown() {
        return posDown;
    }

    public MuPosition getPosUp() {
        return posUp;
    }

    public MuPosition getMuPos() {
        return posMu;
    }

    public CDTSourceAnalyzer() {
        moveUp = false;
        moveDown = false;
        posUp = null;
        posDown = null;
        posMu = null;
    }

    public void initAnalyzer() {
        moveUp = moveDown = false;
        posUp = null;
        posDown = null;
        posMu = null;
        hasUpInterCode = hasDownInterCode = false;
    }

    public void analyzeMutant(Mutant oMutant, boolean bDeleteOp) {

        IASTNode node;
        CPPASTFunctionCallExpression exp;
        MutatedOperator oMutated;
        int nInitPos, nEndPos;

        nInitPos = nEndPos = 0;
        if (oMutant != null) {
            initAnalyzer();
            //Comprobar lo primero si existe el operando de mutación en el código original y procesarlo
            //Se puede comprobar, pero es tontería.
            //Más bien para debug
            // if (true && processMutant(oMutant)) {
            //Ahora se analizará sus posibles movimientos

            //Si se puede mover hacia arriba y posición
            canMoveUp(oMutant);

            //Si se puede mover hacia abajo y posición
            canMoveDown(oMutant);
            // }
            if (bDeleteOp) {
                //Hay que comprobar donde se encuentra la sentencia:
                //-Si está dentro de un IF:
                //- -Si hay más elementos, dejar los elementos y quitar el operador que los une
                //- -Si no hay ... Hay que tomar una decisión.
                //- E.O.C: dejar un ;
                oMutated = oMutant.getHead();

                if (oMutated != null) {
                    node = oMutated.getNode();
                    if (node instanceof CPPASTExpressionStatement) {
                        //Si es una expresión corriente, generamos la pos teniendo en cuenta
                        //que queremos dejar el ;
                        if(((CPPASTExpressionStatement) node).getExpression() instanceof CPPASTBinaryExpression)
                        {
                            nInitPos = node.getFileLocation().getNodeOffset();//exp.getFileLocation().getNodeOffset();
                            nEndPos = nInitPos + node.getFileLocation().getNodeLength()+1;
                            posMu = new MuPosition(nInitPos, nEndPos);     
                        }                        
                        else//if(((CPPASTExpressionStatement) node).getExpression() instanceof CPPASTFunctionCallExpression)
                        {
                            exp = (CPPASTFunctionCallExpression) ((CPPASTExpressionStatement) node).getExpression();

                            nInitPos = exp.getFileLocation().getNodeOffset();
                            nEndPos = nInitPos + exp.getFileLocation().getNodeLength();
                            posMu = new MuPosition(nInitPos, nEndPos);                        
                        }


                    }
                }
            }
            if(posMu == null)
                posMu = new MuPosition(nInitPos, nEndPos);
        }
    }

    private boolean processMutant(Mutant oMutant) {
        boolean bRet = false;
        MutatedOperator Operator;
        int nPosInit;
        String strToken;
        Operator = oMutant.getHead();

        if (Operator != null) {
            nPosInit = Operator.getnPosInit();
            strToken = Operator.getToken();

            if (tokenMatch(nPosInit, strToken)) {
                bRet = searchMutantEnd(oMutant, nPosInit, strToken);
            }
        }

        return bRet;
    }

    private void canMoveUp(Mutant oMutant) {
        //Tomamos la posición inicial de la linea de operador de mutación y la final
        MutatedOperator oMutated;
        int nInitPos, nEndPos, nLineNumber, nSize, nPosInFather;
        IASTNode node, father, nodeAscendant, nodeDeepest;

        nInitPos = nEndPos = 0;
        if (oMutant.hasNext()) {

            oMutated = oMutant.getHead();

            if (oMutated != null) {
                nInitPos = oMutated.getnPosInit();
                nEndPos = oMutated.getnPosEnd();
                node = oMutated.getNode();

                if (node != null) {

                    father = node.getParent();
                    nodeAscendant = nodeDeepest = null;

                    //Dependiendo del tipo de padre
                    //se podrá mover ó no.
                    
                    nPosInFather = getPosInParentFixed(node);
                    //El nodo padre es un método
                    if (isMethodRoot(father)) //functionDeclarator
                    {
                        //si el nodo actual se encuentra en la 1ªpos -> moveUp:false
                        //e.o.c true.
                        if (nPosInFather == 0) {
                            moveUp = false;
                        } else {
                            //Mueve hacia arriba 
                            moveUpChild(father, node);
                            moveUp = true;
                        }

                    } else {
                        //En el caso que sea la posición 0 hay que comprobar el tipo del
                        //'abuelo' Ej: un switch.
                        //Lo normal sería coger la posición del último hijo 
                        if (nPosInFather == 0) {
                            //Hay que mirar en el nodo abuelo y ver si hay posibilidad de 
                            //intercambiarlos
                            nodeAscendant = searchAscendant(father, node);

                            if (nodeAscendant != null) 
                            {
                                nodeDeepest = searchDeepest(nodeAscendant);
                                if (nodeDeepest != null) {                                
                                    //si no hay un nodo en profundidad,
                                    //la posición es el ascendente                                    
                                    hasUpInterCode = true;
                                    moveUp = true; 
                                    createPositionUp(nodeDeepest);                                
                                    checkNeedUpSemicolon(node);
                                }
                            }
                            else
                            {
                                //Si no tiene ascendente, pero sí que tiene padre
                                //Y el padre no es el máster del universo. Lo subimos
                                //Si el padre es compound, cogemos el abuelo
                                if(father instanceof CPPASTCompoundStatement)
                                {
                                    father = getGrandFather(node);
                                }
                                if(!isMethodSentence(father))
                                {
                                    createInlinePositionUp(father);
                                    moveUp = true; 
                                    checkNeedUpSemicolon(node);
                                }
                            }
                            //e.o.c no se puede
                        } else {
                            //suponemos que si se puede mover.
                            moveUpChild(father, node);
                            moveUp = true;
                            checkNeedUpSemicolon(node);
                        }
                    }
                }
            }
            if (moveUp) {
                                      
                posMu = new MuPosition(nInitPos, nEndPos);
            }
        }
    }

    private int getPosInParent(IASTNode node) {
        int nRet;
        IASTNode father;

        nRet = 0;
        father = node.getParent();

        if (father != null) {
            nRet = findChild(father, node);
        }

        return nRet;
    }
    //Si se necesita saber el número real de hijos, utilizar este método.
    private int getNumberOfChildsFixed(IASTNode father)
    {
        int nChilds=0;
        
        nChilds = getNChilds(father);
        if(isIfForWhile(father))
        {
            if(father instanceof CPPASTIfStatement)
                nChilds -=1;
            if(father instanceof CPPASTForStatement)
                nChilds -=3;
            if(father instanceof CPPASTWhileStatement)                
                nChilds -=1;
        }
        return nChilds;
    }
    private int getPosInParentFixed(IASTNode node) {
        int nReturn=-1;
        IASTNode father;
        try
        {
            father = node.getParent();
            nReturn = getPosInParent(node);
            //Si el padre es if, for, while sin brackets, hay que restar elementos
            //3 al for, 1 al if, 1 al while
            if(isIfForWhile(father))
            {
                if(father instanceof CPPASTIfStatement)
                    nReturn -=1;
                if(father instanceof CPPASTForStatement)
                    nReturn -=3;
                if(father instanceof CPPASTWhileStatement)                
                    nReturn -=1;
            }
        
        }catch(NullPointerException e){};
        
        return nReturn;
    }
    private void canMoveDown(Mutant oMutant) {
        //Tomamos la posición inicial de la linea de operador de mutación y la final
        MutatedOperator oMutated;
        int nInitPos, nEndPos, nLineNumber, nSize, nPosInFather;
        IASTNode node, father, grandFather, nodeDescendant, nodeSuperficial;

        nInitPos = nEndPos = 0;
        if (oMutant.hasNext()) {

            oMutated = oMutant.getHead();

            if (oMutated != null) {
                nInitPos = oMutated.getnPosInit();
                nEndPos = oMutated.getnPosEnd();
                node = oMutated.getNode();

                if (node != null) {

                    father = node.getParent();
                    nodeSuperficial = nodeDescendant = null;

                    //Dependiendo del tipo de padre
                    //se podrá mover ó no.
                    //El nodo padre es un método
                    nPosInFather = getPosInParent(node);
                    if (isMethodRoot(father)) //Si 
                    {
                        //si el nodo actual se encuentra en la Última posición del nodo -> moveDown:false
                        //e.o.c true.
                        if (nPosInFather == (getNChilds(father) - 1)) 
                        {
                            moveDown = false;
                        } else {
                            //Mueve hacia abajo 
                            moveDownChild(father, nPosInFather);
                            moveDown = true;
                        }
                    } else {
                        //Es el último hijo del padre. Hay que obtener otra línea más abajo.
                        if (nPosInFather == (getNChilds(father) - 1)) {
                            //Hay que mirar en el nodo tío y ver si hay posibilidad de 
                            //intercambiarlos
                            nodeDescendant = searchDescendant(father, node);

                            if (nodeDescendant != null) {
                                nodeSuperficial = searchSuperficial(nodeDescendant);
                                if (nodeSuperficial != null) {

                                    createPositionDown(nodeSuperficial);
                                    //Si el padre es compuesto, tiene {} -> intercode
                                    if (father instanceof CPPASTCompoundStatement
                                            || !isBrother(father, nodeSuperficial, true)
                                            || isGrandFatherIfWithElse(node)) {
                                        hasDownInterCode = true;
                                    }
                                    checkNeedDownSemicolon(node);

                                    moveDown = true;
                                }
                            }
                            else
                            {
                                //Lo mismo que en el caso del moveUp.
                                //Si no hay descendientes, se podrá colocar detrás del mismo padre
                                //Siempre y cuando sea composed
                                //Si no tiene ascendente, pero sí que tiene padre
                                //Y el padre no es el máster del universo. Lo subimos
                                //Si el padre es compound, cogemos el abuelo
                                if(father instanceof CPPASTCompoundStatement)
                                {
                                    father = getGrandFather(node);
                                    if(!isMethodSentence(father))
                                    {
                                        //Esto va a ser válido siempre y cuando
                                        //el else sea compuesto.
                                        //Esto significa que el padre sería compuesto
                                        createInlinePositionDown(father);
                                        moveDown = true; 
                                        checkNeedDownSemicolon(node);
                                    } 
                                }
                               
                            }

                        } else {
                            //suponemos que si se puede mover.
                            moveDownChild(father, nPosInFather);
                            moveDown = true;

                            //OJo que los ifs sin brackets caen por aqui
                            // ->Habrá que buscar dentro de un if, donde se encuentra
                            //la sentencia. thenClause o elseClause
                            if (checkInterIf(node)) {
                            }
                            checkNeedDownSemicolon(node);
                        }
                    }

                }
            }
            if (moveDown) {
                posMu = new MuPosition(nInitPos, nEndPos);
            }
        }
    }
    //Normalmente esta función unicamente va a tener un hijo.
    //por si acaso, lo dejamos flexible.
    //Todo: probar con algo del estilo bRet = SIMCAN_request ....

    private int findChild(IASTNode node, IASTNode child) {
        int nLen, nIndex;
        IASTExpressionStatement exp;
        boolean bFound;
        IASTNode[] childs;
        int nRet;

        nRet = -1;
        nIndex = nLen = 0;
        bFound = false;

        //buscamos el hijo
        childs = node.getChildren();

        if (childs != null) {
            nLen = childs.length;
            for (int i = 0; i < nLen && !bFound; i++) {
                IASTNode nodeChild = childs[i];

                if (nodeChild == child) {
                    bFound = true;
                    nRet = i;
                }
            }
        }

        return nRet;
    }

    private void moveUpChild(IASTNode father, IASTNode nodeChild) {

        //Cogemos la línea de arriba
        CodeLine lineUp;
        IASTNode[] childs;
        IASTNode child, childUp, deepestNode;
        int nPosInFather, nPosInFatherFixed, nChildsFixed;
        
        nPosInFather = nPosInFatherFixed = 0;
        child = childUp = null;
        childs = null;

        
        //Lo primero cogemos el hijo anterior a la posición que se nos indica
        if (father != null) {
            nPosInFather = getPosInParent(nodeChild);
            nPosInFatherFixed = getPosInParentFixed(nodeChild);
            nChildsFixed = getNumberOfChildsFixed(father);
            
            childs = father.getChildren();
            if (nPosInFatherFixed != 0 && nChildsFixed > 0 && nChildsFixed >= nPosInFatherFixed) {
                childUp = childs[nPosInFather - 1];

                //Una vez tenemos el hijo, hay que comprobar el tipo de sentencia que es:
                // - Puede ser una sentencia corta, en este caso hemos encontrado la línea de arriba
                if (isBasicSentence(childUp)) {
                    moveUp = true;
                    createPositionUp(childUp);
                    //movemos
                } else {
                    // - Puede ser un IF, FOR, ETC: En ese caso hay que coger la línea más profunda
                    //Pero cuidado, que la cosa varía si tiene llaves cerrando o no...
                    deepestNode = searchDeepest(childUp);

                    if (deepestNode != null) {
                        //intercambiamos hacia arriba
                        hasUpInterCode = true;
                        moveUp = true;
                        createPositionUp(deepestNode);
                    }
                }

            } else {
                //someproblems
            }
        }

    }
    //Si es una sentencia básica, podemos intercambiarlo con ella.

    private void moveDownChild(IASTNode father, int nPosInFather) {
        //Cogemos la línea de abajo
        CodeLine lineUp;
        IASTNode[] childs;
        IASTNode child, childDown, superficialNode;

        child = childDown = null;
        childs = null;

        //Lo primero cogemos el hijo posterior a la posición que se nos indica
        if (father != null) {
            childs = father.getChildren();
            if ((nPosInFather < (childs.length)) && childs.length > 0 && childs.length >= nPosInFather) {
                childDown = childs[nPosInFather + 1];

                //Una vez tenemos el hijo, hay que comprobar el tipo de sentencia que es:
                // - Puede ser una sentencia corta, en este caso hemos encontrado la línea de arriba
                if (isBasicSentence(childDown)) {
                    moveDown = true;
                    createPositionDown(childDown);
                    //movemos
                } else {
                    // - Puede ser un IF, FOR, ETC: En ese caso hay que coger la línea más profunda
                    //Pero cuidado, que la cosa varía si tiene llaves cerrando o no...
                    //Además, hay que tener encuenta que el if, for etc: el hijo 0 la expresión

                    superficialNode = searchSuperficial(childDown);

                    if (superficialNode != null) {
                        //intercambiamos hacia arriba
                        moveDown = true;
                        createPositionDown(superficialNode);
                        hasDownInterCode = true;
                    }
                }

            } else {
                //someproblems
            }
        }
    }

    private boolean isBasicSentence(IASTNode child) {
        boolean bRet = false;

        bRet = ((child instanceof CPPASTExpressionStatement)
                || (child instanceof CPPASTDeclarationStatement));

        return bRet;
    }
    //En el caso que lleguemos al inicio del método, nos paramos.

    private boolean isMethodRoot(IASTNode child)
    {
        IASTNode father;
        boolean bRet =false;
        
        if(child instanceof CPPASTCompoundStatement)
        {
            //Compound y su padre es function Declarator
            father = child.getParent();
            bRet = ((father instanceof CPPASTFunctionDeclarator)
                   // || (child instanceof CPPASTSimpleDeclSpecifier)
                    || (father instanceof CPPASTFunctionDefinition)); //CPPASTCompoundStatement

        }
        
        return bRet;
    }
    private boolean isMethodSentence(IASTNode child) {
        boolean bRet = false;
        
        bRet = ((child instanceof CPPASTFunctionDeclarator)
               // || (child instanceof CPPASTSimpleDeclSpecifier)
                || (child instanceof CPPASTFunctionDefinition)); //CPPASTCompoundStatement


        return bRet;
    }
    //Buscamos el primer ascendente que tenga hijos
    //Esto permite seleccionar alguno de sus hijos (último) para intercambiarlo con el nodo requerido

    private IASTNode searchAscendant(IASTNode father, IASTNode node) {
        IASTNode grandFather, grandChild, retAscendant;
        int nPosInParent, nPosInParentFixed;
        grandFather = grandChild = retAscendant = null;
        
        if (father != null) {
            grandFather = father.getParent();
            nPosInParent = getPosInParent(father);
            nPosInParentFixed = getPosInParentFixed(father);
            
            if (!isMethodSentence(grandFather)) {
                if (getNumberOfChildsFixed(grandFather) > 1 && nPosInParentFixed != 0) {
                    retAscendant = getChildIn(grandFather, nPosInParent-1);
                    //retAscendant = grandFather;
                    
                    if(isMethodSentence(grandFather))
                       retAscendant = null; 
                } else {
                    //Llamada recursiva buscando ascendente posible
                    retAscendant = searchAscendant(grandFather, node);
                }
            }
            //eoc -> no va a ser posible mover hacia arriba
        }

        return retAscendant;
    }
    //El objetivo es encontrar el nodo sobre el que se va a realizar el intercambio.
    //La operación consisten en bajar una línea el nodo.

    private IASTNode searchDescendant(IASTNode father, IASTNode node) {
        IASTNode grandFather, grandChild, retDescendant;
        int nPosition;

        nPosition = 0;
        grandFather = grandChild = retDescendant = null;

        if (father != null) {
            grandFather = father.getParent();

            nPosition = findChild(grandFather, father);
            //  if (!isMethodSentence(grandFather)) {
            if (getNChilds(grandFather) > nPosition + 1) {
                retDescendant = getChildIn(grandFather, nPosition + 1);
            } else {
                if (!isMethodSentence(grandFather)) {
                    //Llamada recursiva buscando ascendente posible
                    retDescendant = searchDescendant(grandFather, node);
                }
            }
            //   }
            //eoc -> no va a ser posible mover hacia arriba
        }

        return retDescendant;
    }

    private int getNChilds(IASTNode granFather) {
        int nRet = 0;

        if (granFather != null) {
            nRet = granFather.getChildren().length;
        }

        return nRet;
    }
    //Comprueba si el Token encaja en el código fuente original    

    private boolean tokenMatch(int nPosInit, String strToken) {
        boolean bRet = true;
        int nIndex;

        nIndex = 0;

        while (bRet && nIndex < strToken.length()) {
            System.out.print(OriginalCode.charAt(nPosInit + nIndex));

            bRet = (OriginalCode.charAt(nPosInit + nIndex) == strToken.charAt(nIndex));

            bRet = true;
            nIndex++;
        }
        // System.out.print(OriginalCode);
        return bRet;
    }

    public void setOriginalCode(String OriginalCode) {
        this.OriginalCode = OriginalCode;
    }

    // Deprecated!! Encuentra el final del mutante: SIMCAN_request_cpuTime (0.5); 
    private boolean searchMutantEnd(Mutant oMutant, int nPosInit, String strToken) {
        boolean bRet = false;
        char charEnd;
        int nIndex, nInc;

        nInc = 0;
        nIndex = strToken.length();

        do {
            charEnd = OriginalCode.charAt(nPosInit + nIndex);

            if (charEnd == '(') {
                nInc++;
            } else if (charEnd == ')') {
                nInc--;
            }

            if (charEnd == ')' && nInc == 0) {
                bRet = true;
                //
                if (OriginalCode.charAt(nPosInit + nIndex + 1) != ';') {
                    nIndex--;
                }
            }

            nIndex++;

        } while (!bRet || charEnd == '\n');

        if (bRet) {
            if (oMutant.hasNext()) {
                oMutant.getHead().setnPosEnd(nPosInit + nIndex);
                posMu = new MuPosition(nPosInit, nPosInit + nIndex + 1);
            }
        }
        return bRet;
    }

    public boolean isCanInterchange() {
        return false;
    }

    //Este método consiste en seleccionar la expresión básica más profunda
    //de modo que si encontramos una sentencia que no sea básica, se recurra a la recursión
    //para encontrar el elemento
    private IASTNode searchDeepest(IASTNode nodeAscendant) {
        IASTNode nodeRet, nodeChild;
        int nChilds;

        nChilds = 0;
        nodeRet = nodeChild = null;

        if (nodeAscendant != null) {
            //Si es básico devolvemos el elemento
            if (isBasicSentence(nodeAscendant)) {
                nodeRet = nodeAscendant;
            } else {
                //eoc seguimos                
                nChilds = getNChilds(nodeAscendant);
                if (nChilds > 0) {
                    nodeChild = getChildIn(nodeAscendant, nChilds - 1);
                    nodeRet = searchDeepest(nodeChild);
                }
            }
        }

        return nodeRet;
    }

    private IASTNode searchSuperficial(IASTNode nodeDescendant) {
        IASTNode nodeRet, nodeChild;
        int nChilds;

        nChilds = 0;
        nodeRet = nodeChild = null;

        if (nodeDescendant != null) {
            //Si es básico devolvemos el elemento
            if (isBasicSentence(nodeDescendant)) {
                nodeRet = nodeDescendant;
            } else {
                //eoc seguimos
                nChilds = getNChilds(nodeDescendant);
                if (nChilds > 0) {
                    if (isIfForWhile(nodeDescendant)) {
                        nodeChild = getChildIn(nodeDescendant, 1);
                    } else {
                        nodeChild = getChildIn(nodeDescendant, 0);
                    }

                    nodeRet = searchSuperficial(nodeChild);
                }
            }
        }

        return nodeRet;
    }
    //En este caso, el primer hijo corresponde a la condición

    private boolean isIfForWhile(IASTNode node) {
        boolean bRet = false;

        if (node != null) {
            bRet = ((node instanceof CPPASTIfStatement)
                    || (node instanceof CPPASTForStatement)
                    || (node instanceof CPPASTWhileStatement));
        }

        return bRet;
    }

    private IASTNode getChildIn(IASTNode nodeAscendant, int nIndex) {

        int nLen;
        IASTNode nodeRet;
        boolean bFound;
        IASTNode[] childs;
        int nRet;

        nodeRet = null;
        nRet = nLen = 0;
        bFound = false;

        if (nodeAscendant != null) {
            //buscamos el hijo
            childs = nodeAscendant.getChildren();

            if (childs != null && nIndex < childs.length) {
                nLen = childs.length;
                nodeRet = childs[nIndex];
            }
        }

        return nodeRet;
    }

    private void createPositionUp(IASTNode node) {
        int nPosInit, nPosEnd;

        nPosInit = nPosEnd = 0;

       // nPosInit = node.getFileLocation().getNodeOffset();
         nPosInit = node.getFileLocation().getNodeOffset();
        nPosEnd = nPosInit + node.getFileLocation().getNodeLength();
        posUp = new MuPosition(nPosInit, nPosEnd);
    }
    private void createInlinePositionDown(IASTNode node) {
        int nPosInit, nPosEnd;

        nPosInit = nPosEnd = 0;

        nPosInit = node.getFileLocation().getNodeOffset();
        nPosInit += node.getFileLocation().getNodeLength() +1; 
        
        posDown = new MuPosition(nPosInit, nPosInit);  
    }
    private void createInlinePositionUp(IASTNode node) 
    {
        int nPosInit, nPosEnd;

        nPosInit = nPosEnd = 0;

        nPosInit = node.getFileLocation().getNodeOffset()-1;
        
        posUp = new MuPosition(nPosInit, nPosInit);    
    }
    private void createPositionDown(IASTNode node) {

        int nPosInit, nPosEnd;

        nPosInit = nPosEnd = 0;

        if (node != null) {
            nPosInit = node.getFileLocation().getNodeOffset();
            nPosEnd = nPosInit + node.getFileLocation().getNodeLength();
            posDown = new MuPosition(nPosInit, nPosEnd);
        }

    }

    public boolean hasDownInterCode() {
        return hasDownInterCode;
    }

    public boolean hasUpInterCode() {
        return hasUpInterCode;
    }

    private boolean isBrother(IASTNode father, IASTNode nodeSuperficial, boolean bNext) {
        boolean bRet = false;
        IASTNode grandFather;
        int nPosFather, nPosSup;

        nPosFather = nPosSup = 0;
        if (father != null && nodeSuperficial != null) {
            grandFather = father.getParent();
            if (nodeSuperficial.getParent() == grandFather) {
                nPosFather = getPosInParent(father);
                nPosSup = getPosInParent(nodeSuperficial);

                if (bNext) {
                    bRet = (nPosFather + 1 == (nPosSup));
                } else {
                    bRet = (nPosFather - 1 == (nPosSup));
                }
            }
        }

        return bRet;
    }

    private IASTNode getGrandFather(IASTNode node)
    {
        IASTNode nodeRet = null;
        
        try
        {
            nodeRet = node.getParent().getParent();
        }catch(NullPointerException e)
        {}
        
        return nodeRet;
    }
    // Deprecated
    private boolean isGrandFatherIfWithElse(IASTNode node) {
        boolean bRet = false;
        IASTNode father, grandFather;
        CPPASTIfStatement grandIf;

        father = grandFather = null;
        father = node.getParent();

        if (node != null && father != null) {
            grandFather = father.getParent();
            if (grandFather != null) {
                if (grandFather instanceof CPPASTIfStatement) {
                    grandIf = (CPPASTIfStatement) grandFather;
                    bRet = (grandIf.getElseClause() != null);
                }
            }
        }

        return bRet;
    }

    private boolean checkInterIf(IASTNode node) {
        boolean bRet = false;

        return bRet;
    }

    private int nodeInsideIf(IASTNode node) {
        int nRet = 0;
        IASTNode father, grandFather;
        CPPASTIfStatement ifNode = null;
        IASTStatement thenClause, elseClause;

        father = grandFather = null;
        thenClause = elseClause = null;
        father = node.getParent();

        try
        {
            if (father instanceof CPPASTIfStatement) {
                ifNode = (CPPASTIfStatement) father;

            } else if (grandFather != null && grandFather instanceof CPPASTIfStatement) {
                ifNode = (CPPASTIfStatement) grandFather;
            }

            //Tomamos el if y el else
            thenClause = ifNode.getThenClause();     
            elseClause = ifNode.getElseClause();
            
            if(findChild(thenClause,node) != -1 || node == thenClause)
            {
                //El nodo se encuentra en el hijo.
                nRet = NODE_IN_THEN;
            }else if(findChild(elseClause,node) != -1 || node == elseClause)
            {
                nRet = NODE_IN_ELSE;
            }
            
        }catch(NullPointerException e){}
        
        return nRet;
    }

    private void checkNeedDownSemicolon(IASTNode node) {
        
        IASTNode father = node.getParent();
        //Comprobamos si se necesita introducir ;
        if (!(father instanceof CPPASTCompoundStatement)) {
            IASTNode grandFather = father.getParent();
            //Es un if, for, etc. Sin {}
            if (grandFather != null && getPosInParent(father) == getNChilds(father) - 1) {
                //El abuelo no tiene más hijos
                if (grandFather instanceof CPPASTCompoundStatement) {
                    //El abuelo tiene llaves
                    moveDownNeedSemicolon = true;
                }
            }
        }
        //Tenemos que comprobar el mismo caso que el intercode de if(condition)thenClause else elseClause 
        //Si estamos moviendo hacia abajo, con un padre sin brackets y nos encontramos en el if
        if(father instanceof CPPASTIfStatement)
        {
            //Padre if sin {}.
            if(nodeInsideIf(node) == NODE_IN_THEN)
                moveDownNeedSemicolon = true;
            //Brackets UP -> Cuando el nodo se encuentra en el else
        }
    }
    
    private void checkNeedUpSemicolon(IASTNode node) {
        
        try
        {
            IASTNode father = node.getParent();
            //Comprobamos si se necesita introducir ;
            if (!(father instanceof CPPASTCompoundStatement)) {
                IASTNode grandFather = father.getParent();
                //Es un if, for, etc. Sin {}
                if (grandFather != null && getPosInParent(father) == getNChilds(father) - 1) {
                    //El abuelo no tiene más hijos
                    if (grandFather instanceof CPPASTCompoundStatement) {
                        //El abuelo tiene llaves
                        moveUpNeedSemicolon = true;
                    }
                }
            }
        //Tenemos que comprobar el mismo caso que el intercode de if(condition)thenClause else elseClause 
        //Si estamos moviendo hacia abajo, con un padre sin brackets y nos encontramos en el if
       // if(isIfForWhile(father))
      //  {
            if(father instanceof CPPASTIfStatement)
            {
                //Padre if sin {}.
                if(nodeInsideIf(node) == NODE_IN_THEN)
                    moveUpNeedSemicolon = true;
                else if(nodeInsideIf(node) == NODE_IN_ELSE)
                {
                    moveUpNeedBrackets = true;      
                    hasUpInterCode = true;
                }                
            }
             
      //  }
        }catch(NullPointerException e)
        {}
        
    }

    public boolean needsDownSemicolon() {
        return moveDownNeedSemicolon;
    }

    public boolean needsUpSemicolon() {
        return moveUpNeedSemicolon;
    }

    public boolean needsUpBrackets() {
        return moveUpNeedBrackets;
    }

    
}
