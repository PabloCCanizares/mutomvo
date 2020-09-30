/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mutomvo.Mutation.Operators;

import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import mutomvo.Mutation.Operators.Method.EnumOperator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class MutationOperator 
{
    protected int nPosInit;
    protected int nPosEnd;
    protected int nLineNumber;
    protected EnumOperator eOperatorType;
    protected EnumOperatorClass eOperatorClass;
    protected EnumMuOperation eOperatorSubClass;
    protected String Token;
    protected IASTNode oNode;

    public void setOperatorClass(EnumOperatorClass eClass)
    {
        eOperatorClass = eClass;
    }
    public void setSubClass(EnumMuOperation eClass)
    {
        eOperatorSubClass = eClass;
    }    
    public IASTNode getNode() {
        return oNode;
    }
        
    public String getToken() {
        return Token;
    }
    
    public int getLineNumber()
    {
        return nLineNumber;
    }
    public int getnPosInit() {
        return nPosInit;
    }

    public void setnPosInit(int nPosInit) 
    {
        if(eOperatorType != EnumOperator.eAPI)
            this.nPosInit = nPosInit;//-Token.length();
        else
            this.nPosInit = nPosInit;//-(Token.length()-1);
    }

    public int getnPosEnd() {
        return nPosEnd;
    }

    public void setnPosEnd(int nPosEnd) {
        this.nPosEnd = nPosEnd;
    }

    public EnumOperator getOperatorType() {
        return eOperatorType;
    }

    public void MutationOperator()
    {
        oNode = null;
        nPosInit = nPosEnd= nLineNumber = 0;    
    }

    public void setLine(int lineNumber) 
    {
        nLineNumber = lineNumber;
    }
    public boolean isOperatorInsertion()
    {
        boolean bRet;
        
        bRet = false;
        if(eOperatorSubClass!= null)
        {
            if (eOperatorSubClass == EnumMuOperation.eAOIs || eOperatorSubClass == EnumMuOperation.eAOIu ||
                eOperatorSubClass == EnumMuOperation.eCOI || eOperatorSubClass == EnumMuOperation.eLOI)
                bRet = true;
        }
        
        return bRet;
    }

    public boolean isOperatorDeletion() {
               boolean bRet;
        
        bRet = false;
        if(eOperatorSubClass!= null)
        {
            if (eOperatorSubClass == EnumMuOperation.eAODs || eOperatorSubClass == EnumMuOperation.eAODu ||
                eOperatorSubClass == EnumMuOperation.eCOD || eOperatorSubClass == EnumMuOperation.eLOD)
                bRet = true;
        }
        
        return bRet; 
    }

}
