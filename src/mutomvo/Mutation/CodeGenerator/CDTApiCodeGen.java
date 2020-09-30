    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.CodeGenerator;

import java.io.File;
import java.util.LinkedList;
import org.eclipse.cdt.internal.core.parser.scanner.Token;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Mutant;
import mutomvo.Mutation.Parser.SourceCodeIndexer.CDTSourceAnalyzer;
import mutomvo.Mutation.Parser.SourceCodeIndexer.Elements.MuPosition;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;

/**
 *
 * @author Pablo C. Cañizares
 */
class CDTApiCodeGen extends CodeGenerator
{
    CDTSourceAnalyzer codeAnalyzer;
    File srcFile;        
    
    public CDTApiCodeGen()
    {
        codeAnalyzer = new CDTSourceAnalyzer();    //Se pueden derivar nuevos tipos
        m_bReportMode =false;
        m_bEquivalentGenMode=false;
    }
        
    @Override
    public void LoadCode(String csPath) 
    {
        srcFile = new File(csPath);
        
        super.LoadCode(csPath);
        codeAnalyzer.setOriginalCode(OriginalCode);
        
    }
    public void GlobalSaveMutant(String MutantCode, MutatedOperator Operator, int nIndex)
    {
        mutantInfo mInfo;
        String strDesc, MutantCodePath;

        
         if(Operator != null)
         {
             EnumMuOperation eOperation = Operator.getOperation();
             EnumOperatorClass eClass = Operator.getOperatorClass();
             
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
             
            MutantCode = MutantCode.concat(strDesc);
            if(m_bReportMode == false && m_bEquivalentGenMode == false)
            {
                MutantCodePath = createMutantFolder(nIndex);
                saveMutantToDisk(MutantCode, MutantCodePath, nIndex);
            }
            else if(m_bReportMode)
            {
                
                //Insert the mutant in the report
                mInfo = new mutantInfo(nIndex,strDesc);
                
                mInfo.setOperatorClass(eClass);
                mInfo.setMutationOperator(eOperation);
                mInfo.setLine(Operator.getLineNumber());
                mInfo.setToken(Operator.getToken());
                m_mutantsReportList.add(mInfo);
            }
            else if(m_bEquivalentGenMode)
            {
                //In this case, the generation is different
                MutantCodePath = createEquivalentMutantFolder(nIndex);
                saveMutantToDisk(MutantCode, MutantCodePath, nIndex);                    
            }
         }

    }
    public int save(Mutant oMutant, int nIndex) 
    {
        //Hay operaciones comunes que realizar, como crear las carpetas, modificar el ned y todo eso.
        boolean bRet, bDeleteOp;
        int nLastPos,nIndexRet;
        String MutantCodePath, Concat, Token, lineUp, lineDown, interLine;        
        String MutantCode = new String();        
        MuPosition upPos, downPos, muPos;        
        
        nLastPos = nIndexRet = 0;
        bDeleteOp = bRet = false;
        
        //Una vez nos llega el mutante, con este tipo de operadores OMNET
        //hay que hacer operaciones extra, ya que no se han automutado (de momento)
        while (oMutant.hasNext()) 
        {
            //Analizar solo una vez, el codigo fuente es el mismo ...
            //eso si, pero hay que definir cuales son la linea de arriba, de abajo, etc ...
            
            MutatedOperator Operator = oMutant.getHead();
            bDeleteOp = (Operator.getOperation() == EnumMuOperation.eODEL);
                        
            //Si la operación es borrado, se toma diferente. Hay que dejar un ;
            codeAnalyzer.analyzeMutant(oMutant, bDeleteOp);
            
            //TODO: Operador mover sentencia.
            if(MutationCfg.getInstance().isGenerateMoveUp() && Operator.getOperation() == EnumMuOperation.eOMOVUP && codeAnalyzer.isMoveUp())
            {
               //lo movemos parriba y lo guardamos
                upPos = codeAnalyzer.getPosUp();
                muPos = codeAnalyzer.getMuPos();
                
                if(OriginalCode.length() > muPos.getnEndPos() && muPos != null && upPos != null)
                {
                    Token = OriginalCode.substring(muPos.getnInitPos(), muPos.getnEndPos());
                    if(!Token.isEmpty())
                        Operator.setToken(Token.trim());
                    //Operator.setToken(Token);
                    Concat = OriginalCode.substring(0, upPos.getnInitPos());
                    lineUp = OriginalCode.substring(upPos.getnInitPos(), upPos.getnEndPos());
                    
                    interLine = OriginalCode.substring(upPos.getnEndPos()+1, muPos.getnInitPos()-1);
                    if(codeAnalyzer.needsUpSemicolon())
                        interLine = interLine+ ";\n";
                                        
                    if(codeAnalyzer.hasUpInterCode())
                    {           
                        if(codeAnalyzer.needsUpBrackets())
                            Concat = Concat  +"{"+lineUp + Token+"}"+interLine + OriginalCode.substring(muPos.getnEndPos());
                        else                            
                            Concat = Concat  +  Token+interLine + lineUp+OriginalCode.substring(muPos.getnEndPos());
                    }
                    else
                    {                        
                        if(codeAnalyzer.needsUpBrackets())
                            Concat = Concat +"{"+Token+"\r\n" + lineUp +"}"+interLine+OriginalCode.substring(muPos.getnEndPos());
                        else                            
                            Concat = Concat +Token+"\r\n" + lineUp +interLine+OriginalCode.substring(muPos.getnEndPos());
                    }

                    MutantCode = Concat;
                    bRet=true;
                    GlobalSaveMutant(MutantCode, Operator, nIndex);
                    
                    nIndexRet++;
                }
                else
                {
                    //some problems ... launch warnings!
                    System.out.println("OPMOVUP - Some problems found creating APIs mutants!\n");
                }

            }
            if(MutationCfg.getInstance().isGenerateMoveDown() && Operator.getOperation() == EnumMuOperation.eOMOVDOWN && codeAnalyzer.isMoveDown())
            {

                //lo movemos hacia abajo y lo guardamos               
                downPos = codeAnalyzer.getPosDown();
                muPos = codeAnalyzer.getMuPos();                

                if(downPos!= null && muPos != null &&OriginalCode.length() > muPos.getnEndPos() )
                {
                    Token = OriginalCode.substring(muPos.getnInitPos(), muPos.getnEndPos());
                    if(!Token.isEmpty())
                        Operator.setToken(Token.trim());
                    
                    Concat = OriginalCode.substring(0, muPos.getnInitPos());

                    lineDown = OriginalCode.substring(downPos.getnInitPos(), downPos.getnEndPos());
                    
                    if(muPos.getnEndPos()+1 <= downPos.getnInitPos()-1)
                        interLine = OriginalCode.substring(muPos.getnEndPos()+1, downPos.getnInitPos()-1);
                    else interLine ="";
                    if(codeAnalyzer.needsDownSemicolon())
                        interLine = ";\n"+interLine;
                    //Por si acaso, hay que concatenar los datos entre la línea a mover y la línea de abajo
                    //por si tiene hasDownInterCode
                    if(codeAnalyzer.hasDownInterCode())
                    {                        
                        Concat = Concat + interLine + Token + OriginalCode.substring(downPos.getnInitPos());
                    }
                    else                        
                        Concat = Concat + interLine + lineDown + Token + OriginalCode.substring(downPos.getnEndPos());

                    MutantCode = Concat;
                    bRet=true;
                    GlobalSaveMutant(MutantCode, Operator, nIndex);
                    nIndexRet++;
                }
                else
                {
                    System.out.println("OMOVDOWN - Some problems found creating APIs mutants!\n");
                }
            }
            
            if(MutationCfg.getInstance().isGenerateMerge() && Operator.getOperation() == EnumMuOperation.eOMER)
            {
                //intercambiamos los parámetros y lo guardamos
                //funcion genérica que parte la llamada en N partes, la primera el nombre de la llamada, 
                //los N-1 restantes los parámetros
                String strCall, strParameters;
                boolean bShuffle=false;
                String delims = "[,]";
                String delimsOp = "[_]";
                int nFirstPar, nLastPar, nIndexValue;
                LinkedList<Integer> shuffleList;
                
                muPos = codeAnalyzer.getMuPos();
                
                if(muPos != null)
                {
                    Token = OriginalCode.substring(muPos.getnInitPos(), muPos.getnEndPos());
                    Token = Token.replaceAll(" ","");
                    if(!Token.isEmpty())
                        Operator.setToken(Token.trim());
                    
                    nFirstPar = Token.indexOf('(');
                    nLastPar = Token.indexOf(");");

                    if(nFirstPar != -1 && nLastPar != -1)
                    {
                        strCall = Token.substring(0, nFirstPar);
                        strParameters = Token.substring(nFirstPar+1, nLastPar);

                        //Tenemos la llamada              
                        String[] tokens = strParameters.split(delims);

                        //Mínimo: llamada+ 2 parámetros +final, eoc. fuera
                        if(tokens.length>0 && tokens.length>=2)
                        {
                            String finalOp;
                            String[] operation = strCall.split(delimsOp);
                            int nLastValue = operation.length -1;

                            //la clave está en este valor, si es send/receive/recv las dos últimas posiciones se intercambian.
                           finalOp =operation[nLastValue];
                           
                           //Comprobamos con el API Manager si es 'shuffleable'
                           if(MutationCfg.getInstance().checkShuffableMethod(Operator, strCall, strParameters))
                           {
                               bShuffle=true;
                           }
                           if(bShuffle)
                           {
                               
                               while(MutationCfg.getInstance().hasNextShuffe(strCall))
                               {
                                   Token = strCall+"(";
                                   shuffleList = MutationCfg.getInstance().getNextShuffe(strCall);

                                   for(int i=0;i<shuffleList.size();i++)
                                   {
                                       nIndexValue = shuffleList.get(i);
                                       Token+= tokens[nIndexValue];
                                       if(i < shuffleList.size()-1)
                                       {
                                           Token+= ",";
                                       }
                                   }                                   
                                   Token+=");";                                   
                                   if(!Token.isEmpty())
                                    Operator.setToken(Token.trim());
                                   //Primera parte
                                   Concat = OriginalCode.substring(0, muPos.getnInitPos());
                                   Concat += Token;
                                   Concat = Concat + OriginalCode.substring(muPos.getnEndPos());
                                   MutantCode = Concat;
                                   bRet=true;
                                   GlobalSaveMutant(MutantCode, Operator, nIndex);
                                   nIndexRet++;
                               }
                           }
                        }
                    }
                }
                else
                {
                    System.out.println("OMER - Some problems found creating APIs mutants!\n");
                }
            }
            if(MutationCfg.getInstance().isGenerateRep() && Operator.getOperation() == EnumMuOperation.eOREP)
            {                
                nIndexRet+= doReplacement(Operator, nIndex);
            }
            
            if(MutationCfg.getInstance().isGenerateDel() && Operator.getOperation() == EnumMuOperation.eODEL)
            {
                muPos = codeAnalyzer.getMuPos();
                if(muPos != null)
                {
                    Token = OriginalCode.substring(muPos.getnInitPos(), muPos.getnEndPos());
                    if(!Token.isEmpty())
                        Operator.setToken(Token.trim());
                    
                    Concat = OriginalCode.substring(0, muPos.getnInitPos());                   
                    Concat = Concat +"\r\n" + OriginalCode.substring(muPos.getnEndPos());
                    
                    MutantCode = Concat;
                    bRet=true;
                    GlobalSaveMutant(MutantCode, Operator, nIndex);                              
                }
                else
                {
                    System.out.println("ODEL - Some problems found creating APIs mutants!\n");
                }
                nIndexRet++;
            }            
            oMutant.getNext();
        }
        return nIndexRet;        
    }
    private int doReplacement(MutatedOperator Operator, int nIndex)
    {
        int nIndexRet;
        String strCall, strParameters, strRepCall, Concat, MutantCode;
        int nFirstPar, nLastPar;
        MuPosition muPos; 
        String Token, finalToken;    
        
        nIndexRet=0;
        muPos = codeAnalyzer.getMuPos();
        
        if(muPos != null)
        {
            Token = OriginalCode.substring(muPos.getnInitPos(), muPos.getnEndPos());
            Token = Token.replaceAll(" ","");
            nFirstPar = Token.indexOf('(');
            nLastPar = Token.indexOf(");");

            if(nFirstPar != -1 && nLastPar != -1)
            {
                strCall = Token.substring(0, nFirstPar);
                strParameters = Token.substring(nFirstPar+1, nLastPar);

                if(MutationCfg.getInstance().checkReplacementMethod(Operator, strCall, strParameters))
                {
                    while(MutationCfg.getInstance().hasNextReplacement(strCall))//hasNext
                    {
                        strRepCall = MutationCfg.getInstance().getNextReplacement(strCall);

                        if(strRepCall.length()>0)
                        {
                            System.out.printf("RepCall: "+strRepCall+"\n");
                        
                            finalToken = strRepCall+Token.substring(nFirstPar);
                            System.out.printf("FinalToken: "+finalToken+"\n");

                            Concat = OriginalCode.substring(0, muPos.getnInitPos());

                           // System.out.print(Concat);
                            Concat = Concat +finalToken+"\r\n" + OriginalCode.substring(muPos.getnEndPos());

                            MutantCode = Concat;                    
                            GlobalSaveMutant(MutantCode, Operator, nIndex+nIndexRet);
                            nIndexRet++;
                         }
                    }
                }
            }
        }

        
        return nIndexRet;
    }
     /*
     * Obtains a report of the 
     */
    public LinkedList<mutantInfo> genReport(Mutant oMutant, int nIndex) {
        
        if(m_mutantsReportList == null)
            m_mutantsReportList = new LinkedList<mutantInfo>();
        else
            m_mutantsReportList.clear();
        
        m_bReportMode = true;
        save(oMutant, nIndex);
        m_bReportMode = false;
        
        return m_mutantsReportList;
    }
    
}
