/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.CodeGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mutomvo.Mutation.Mutant;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Utils.Utils;

/**
 *
 * @author user
 */
public abstract class CodeGenerator 
{
    protected File mutantFolder;
    protected String appName;
    protected String OriginalCode;
    protected LinkedList<mutantInfo> m_mutantsReportList;
    protected boolean m_bReportMode;
    protected boolean m_bEquivalentGenMode;

    abstract public int save(Mutant oMutant, int nIndex);
    abstract public LinkedList<mutantInfo> genReport(Mutant oMutant, int nIndex);
    
    public String getOriginalCode() {
        return OriginalCode;
    }

    public void setOriginalCode(String OriginalCode) {
        this.OriginalCode = OriginalCode;
    }

    public void SetEquivalentMode(boolean bEquivMode)
    {
        m_bEquivalentGenMode = bEquivMode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public File getMutantFolder() {
        return mutantFolder;
    }

    public void setMutantFolder(File mutantFolder) {
        this.mutantFolder = mutantFolder;
    }
    
    
    public void saveMutantToDisk(String MutantCode, String MutantCodePath, int nIndex)
    {
        saveToDisk(MutantCode, MutantCodePath);
    }

    //TODO: Si no existe la carpeta la creo
    //Ojo que tanto disco intercalado con ejecucion en memoria va a ir mal
    void saveToDisk(String Code, String MutantCodePath) {
        try {
            int i = 0;
            PrintWriter out = new PrintWriter(MutantCodePath);

            out.println(Code);

            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MutantCodeSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static String readFile(String path)
            throws IOException {
                   
        String line, text;
        BufferedReader br;
        StringBuilder sb;
        
        //byte[] encoded = Files.readAllBytes(Paths.get(path));
        
            // Init                    
            br = new BufferedReader(new FileReader(path));
            sb = new StringBuilder();
            line = text = "";
            
            
            // Read from file
            line = br.readLine();

            // Until the EOF!
            while (line != null) {        
                text += line + "\n";
                line = br.readLine();                
            }
        
        return new String(text);
    }

    public String createEquivalentMutantFolder(int nIndex)
    {
        File destFolder;
        String mutantName;
        
        // Current mutant name
        mutantName = appName + Utils.defaultMutantSuffix + Integer.toString(nIndex);

        // Folder for current Mutant
        destFolder = new File(mutantFolder.getAbsoluteFile()+File.separator+appName);
        
        // Create mutant folder
        if(!destFolder.exists())
            destFolder.mkdir();
        
        return destFolder.getAbsolutePath()+File.separator+mutantName;
    }            
    public String createMutantFolder(int nIndex)
    {
        File destFolder;
        String mutantName;
        
        // Current mutant name
        mutantName = appName;

        // Folder for current Mutant
        destFolder = new File(mutantFolder.getAbsoluteFile() + File.separator + Integer.toString(nIndex));

        // Create mutant folder
        destFolder.mkdir();
        
        return destFolder.getAbsolutePath()+File.separator+mutantName;
    }
    public void LoadCode(String csPath) {
        try {
            OriginalCode = readFile(csPath);
        } catch (IOException ex) {
            Logger.getLogger(MutantCodeSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String ReadCode(String csPath) throws IOException {
        String strReturn = "";
            strReturn = readFile(csPath);
        
        return strReturn;
    }

}
