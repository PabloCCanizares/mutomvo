/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.CodeGenerator;

//import static MuConsole.MuConsole.readFile;
import java.io.File;
import java.util.LinkedList;
import mutomvo.Mutation.Mutant;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;

/**
 *
 * @author PAblo C. Cañizares
 */
public class MutantCodeSaver {


    private File mutantFolder;
    private String appName;
    private String OriginalCode;

    CodeGenerator StdCodeGen;
    //CodeGenerator OmnetCodeGen;
    CodeGenerator ApiCodeGen;
    
    public MutantCodeSaver ()
    {
        StdCodeGen = new StandardCodeGen();
        //OmnetCodeGen = new OmnetCodeGen();
        ApiCodeGen = new CDTApiCodeGen();
    }

    public String getOriginalCode() {
        return OriginalCode;
    }

    public void setOriginalCode(String OriginalCode) 
    {
        this.OriginalCode = OriginalCode;
        StdCodeGen.setOriginalCode(OriginalCode);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        StdCodeGen.setAppName(appName);
        ApiCodeGen.setAppName(appName);        
    }

    public File getMutantFolder() {
        return mutantFolder;
    }

    public void setMutantFolder(File mutantFolder) 
    {
        this.mutantFolder = mutantFolder;
        StdCodeGen.setMutantFolder(mutantFolder);
        ApiCodeGen.setMutantFolder(mutantFolder);           
    }

    
    //public void generateMutant(int index, File mutantFolder, String appName)

    public int generateCode(Mutant oMutant, int nIndex) 
    {
        //Dependiendo del tipo de mutante, realizamos la generación de código de una forma distinta
        int nRet =0;
        switch(oMutant.getMutantHighType())
        {
            case eAPI:
                nRet = ApiCodeGen.save(oMutant,nIndex);
                break;
                
            default:                
                nRet = StdCodeGen.save(oMutant, nIndex);
                break;
        }
        return nRet;
    }
    public void SetEquivalentMode(boolean bEquivMode)
    {
        System.out.println ("Setting equivalent mode to: "+bEquivMode+"\n");
        
        StdCodeGen.SetEquivalentMode(bEquivMode);
        ApiCodeGen.SetEquivalentMode(bEquivMode);
    }
    public void LoadCode(String csPath) 
    {
        StdCodeGen.LoadCode(csPath);
        ApiCodeGen.LoadCode(csPath);
    }

    public LinkedList<mutantInfo> generateReport(Mutant oMutant, int nIndex) {
        
        LinkedList<mutantInfo> mRet = null;
        switch(oMutant.getMutantHighType())
        {
            case eAPI:
                mRet = ApiCodeGen.genReport(oMutant,nIndex);
                break;
                
            default:                
                mRet = StdCodeGen.genReport(oMutant, nIndex);
                break;
        }        
        return mRet;
    }
}
