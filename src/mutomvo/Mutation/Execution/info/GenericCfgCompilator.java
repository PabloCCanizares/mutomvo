/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info;

import mutomvo.Mutation.Execution.ExecutionConfig;

/**
 *
 * @author Pablo C. Cañizares
 */
public class GenericCfgCompilator {
    
    private final String APP_NAME_TOKEN = "[[APP_NAME]]";
    private final String MUTANTS_PATH_TOKEN = "[[MUTANTS_PATH]]";
    private final String ORIGINAL_PATH_TOKEN = "[[ORIGINAL_PATH]]";
    private final String INDEX_MUTANT_TOKEN = "[[INDEX_MUTANT]]";
    
    //TODO: Falta execution
    
    String m_strOriginalCompilationLine;        //Line used to compile the original program
    String m_strMutantsCompilationLine;         //Line used to compile the mutants
    String m_strAppPath;                        //Full path of the application    
    String m_strAppName;                        //Name of the application
    String m_strMutantsPath;                    //Full path of the mutants
        
    //Pre-processed lines
    String m_strPreOriCompLine;
    String m_strPreMutCompLine;    

    public GenericCfgCompilator(ExecutionConfig exeConfig) {
        
        this.m_strOriginalCompilationLine = exeConfig.getOriginalCompilationLine();
        this.m_strOriginalCompilationLine = exeConfig.getMutantsCompilationLine();
    }
    
    /**
     * Check if the parameters that are necessary to process the info
     * are not empty, and in the case of the paths, if they exist
     * @return 
     */
    public boolean IsCfgValid()
    {
        boolean bRet = true;
        
        //TODO: Check all parameters
        //Check if are not empty and, if exists;
        
        return bRet;
    }
    public String processLine(String strLine)
    {
        String strLineAux;
        
        strLineAux = strLine;
        strLineAux = strLineAux.replace(APP_NAME_TOKEN, m_strAppName);
        strLineAux = strLineAux.replace(ORIGINAL_PATH_TOKEN, m_strAppPath);
        strLineAux = strLineAux.replace(MUTANTS_PATH_TOKEN, m_strMutantsPath);                
        
        return strLineAux;
    }
    
    public String processLineByMutantId(String strLine, int nMutant)
    {
        String strLineAux;
        
        strLineAux = processLine(strLine);
        strLineAux = strLineAux.replace(INDEX_MUTANT_TOKEN, Integer.toString(nMutant));        
                
        return strLineAux;
    }      
    
    public String processPartialLineByMutantId(String strLine, int nMutant)
    {
        String strLineAux;

        strLineAux = strLine.replace(INDEX_MUTANT_TOKEN, Integer.toString(nMutant));        
                
        return strLineAux;
    }  
    
    public String getCompilationMutantLine(int nMutant)
    {
        String strCompLine;
        
        if(m_strPreMutCompLine.isEmpty())
            m_strPreMutCompLine = processLine(m_strMutantsCompilationLine);
        
        strCompLine = processLineByMutantId(m_strPreMutCompLine, nMutant);
        
        return strCompLine;
    }
    
    public String getCompilationOriginalLine()
    {
        String strCompLine;
        
        strCompLine = processLine(m_strOriginalCompilationLine);

        return strCompLine;
    }    

}
