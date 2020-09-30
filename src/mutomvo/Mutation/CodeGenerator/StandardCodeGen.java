/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.CodeGenerator;

import java.util.LinkedList;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Mutant;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;

/**
 *
 * @author Pablo C. Cañizares
 */
class StandardCodeGen extends CodeGenerator
{   
    public StandardCodeGen()
    {
        m_bReportMode =false;
        m_bEquivalentGenMode=false;
    }

    public int save(Mutant oMutant, int nIndex) 
    {
        int nLastPos, nRet;
        String MutantCodePath, strDesc;
        mutantInfo mInfo;
        String MutantCode = new String();
        MutatedOperator Operator = null;
        EnumMuOperation eOperation;
        EnumOperatorClass eClass;
        boolean bError = false;
        boolean bRet =false;
      
       
        MutantCodePath = "";
        nRet = nLastPos = 0;
        try
        {

            if(m_bEquivalentGenMode == false && m_bReportMode==false)
            {
                if(MutationCfg.getInstance().isDebugMode())
                    System.out.printf("Generation mode: normal !\n");

                MutantCodePath = createMutantFolder(nIndex);
            }        
            else if(m_bEquivalentGenMode)
            {
                if(MutationCfg.getInstance().isDebugMode())
                    System.out.printf("Generation mode: equivalent !\n");
                //In this case, the generation is different
                MutantCodePath = createEquivalentMutantFolder(nIndex);                    
            }
            else
                MutantCodePath = "";

            while (oMutant.hasNext()) 
            {
                int nPosInit;
                String Concat, Token;
                Operator = oMutant.getNext();
                Token = Operator.getToken();

                if(m_bReportMode)
                {
                     eOperation = Operator.getOperation();
                     eClass = Operator.getOperatorClass();
                     if(eClass != null)
                     {
                         strDesc = "\n //Line: "+Operator.getLineNumber()+" | Token: "+Operator.getToken()+" | Class: "+eClass.toString()+ " | Op: "+Operator.getOperation().toString();
                         //MutantCode = MutantCode.concat("\n //Line: "+Operator.getLineNumber()+" | Token: "+Operator.getToken()+" | Class: "+eClass.toString()+ " | Op: "+Operator.getOperation().toString());                 
                     }              
                     else
                     {
                         strDesc = "\n //Line: "+Operator.getLineNumber()+" | Token: "+Operator.getToken()+" | Op: "+Operator.getOperation().toString();
                         //MutantCode = MutantCode.concat("\n //Line: "+Operator.getLineNumber()+" | Token: "+Operator.getToken()+" | Op: "+Operator.getOperation().toString());
                     }

                    //Insert the mutant in the report
                    mInfo = new mutantInfo(nIndex,strDesc);

                    mInfo.setOperatorClass(eClass);
                    mInfo.setMutationOperator(eOperation);
                    mInfo.setLine(Operator.getLineNumber());
                    mInfo.setToken(Operator.getToken());
                    m_mutantsReportList.add(mInfo);
                }

                nPosInit = Operator.getnPosInit();
                nLastPos = Operator.getnPosEnd();

                //Is insert type
                if(isOperationInsert(Operator) || isOperationDeletion(Operator))
                {
                    if(nLastPos >= nPosInit)
                    {
                        Concat = OriginalCode.substring(0, nPosInit);            
                       // System.out.print(Concat);
                        MutantCode = MutantCode.concat(Concat);
                        MutantCode = MutantCode.concat(Token);
                        Concat = OriginalCode.substring(nPosInit);
                    }
                }
                else
                {
                    if(nLastPos > nPosInit)
                    {
                        Concat = OriginalCode.substring(0, nPosInit);            
                       // System.out.print(Concat);
                        MutantCode = MutantCode.concat(Concat);
                        MutantCode = MutantCode.concat(Token);

                        Concat = OriginalCode.substring(nLastPos,nLastPos+10);
                        //System.out.print(Concat);
                    }
                    else
                        bError = true;
                }   
            }
        }catch(Exception e)
        {
            bError = true;
            nRet = 0;
        }
                
        if(!bError && Operator != null)
        {
            bRet =true;
            nRet = 1;
            MutantCode = MutantCode.concat(OriginalCode.substring(nLastPos));
            MutantCode = MutantCode.concat("\n //Line: "+Operator.getLineNumber()+" | Token: "+Operator.getToken()+ " | Op: "+Operator.getOperation().toString());
            if(m_bReportMode == false)
                saveMutantToDisk(MutantCode, MutantCodePath, nIndex);
        }
        return nRet;
    }

    public boolean isOperationInsert(MutatedOperator operator)
    {
        boolean bRet;
        
        bRet = false;
        if(operator.getOperation() == EnumMuOperation.eAOIs || operator.getOperation() == EnumMuOperation.eAOIu ||
           operator.getOperation() == EnumMuOperation.eCOI || operator.getOperation() == EnumMuOperation.eLOI )
        {
            bRet = true;
        }
        
        return bRet;
    }
    
    @Override
    public LinkedList<mutantInfo> genReport(Mutant oMutant, int nIndex) 
    {
        
        if(m_mutantsReportList == null)
            m_mutantsReportList = new LinkedList<mutantInfo>();
        else
            m_mutantsReportList.clear();
        
        m_bReportMode = true;
        save(oMutant, nIndex);
        
        return m_mutantsReportList;
    }

    private boolean isOperationDeletion(MutatedOperator Operator) 
    {
        boolean bRet;
        
        bRet = false;
        if(Operator.getOperation() == EnumMuOperation.eAODs || Operator.getOperation() == EnumMuOperation.eAODu)
        {
            bRet = true;
        }
        
        return bRet;
    }
}
