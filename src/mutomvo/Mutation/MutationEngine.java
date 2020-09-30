/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation;

import mutomvo.Mutation.CodeGenerator.MutantCodeSaver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.Mutation.Parser.CDTParser.CDTMutantIndexer;
import mutomvo.Mutation.Parser.IMutationIndexer;
import mutomvo.Mutation.Mutator.BasicMutator;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Equivalents.EquivalentChecker;
import mutomvo.Mutation.Mutator.Mutator;
import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.Operators.MutationOperatorCharList;
import mutomvo.Mutation.Execution.info.mutants.MutantsCollection;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import static mutomvo.Mutation.ReportGenerator.StatsPersistence.STATS_MAIN_FOLDER;

/**
 *
 * @author Pablo C. Cañizares
 */
public class MutationEngine {

    MutationCfg mutationConfig;
    private EquivalentChecker equivalentChecker;
    private String appName;
    private String applicationFolder;
    private String mutantFolder;
    private String scenarioFolder;
    private String selectedApp;
    private String binaryFolder;
    private int numMutants;
    private int nGeneratedMutants;
    static long nFileSize;
    private boolean bEquivalentMode;
    
    public MutationEngine() {
        equivalentChecker = new EquivalentChecker();
        bEquivalentMode = false;
        nGeneratedMutants = -1;
    }

    /**
     * @throws mutomvo.Exceptions.MutomvoException
     */

    public void startProccess() throws MutomvoException {
        File srcFile;
        List<MutationOperator> OperatorsList;
        IMutationIndexer MUIndexer;
        MutationOperatorCharList oCharGenerator;

        //Initialize
        System.out.printf("Starting Mu-Console\n");
        MUIndexer = new CDTMutantIndexer();
        oCharGenerator = new MutationOperatorCharList();

        //Opening file
        srcFile = new File(applicationFolder);
        nFileSize = srcFile.length();
        System.out.printf("File opened: %s , size: %d bytes\n", srcFile.getAbsolutePath(), nFileSize);

        System.out.printf("Starting the code analysis process\n");
        //LookUP the mutations operators at the source code
        OperatorsList = MUIndexer.doIndex(oCharGenerator, srcFile);

        if (OperatorsList != null) {
            //Le pasamos la lista de operadores encontrados al mutador
            //que irá generando mutantes progresivamente            
            doMutation(OperatorsList, srcFile);
        }

        System.out.printf("Finished Mu-Console !\n");
    }

    public void doMutation(List<MutationOperator> OperatorsList, File srcFile) throws MutomvoException {
        MutantCodeSaver MuSaver = new MutantCodeSaver();
        String strAppPath;
        File mutantFolderFile;
        LinkedList<String> stringMutantList;
        int nIndex, nGenIndex;
        Mutator oMutantMaker;

        //Init
        strAppPath = srcFile.getAbsolutePath();
        nIndex = 0;

        mutantFolderFile = new File(mutantFolder);
        oMutantMaker = new BasicMutator(OperatorsList); //Vamos a hacer un mutante por operador, para no joder mucho        
        stringMutantList = new LinkedList<String>();

        MuSaver.setAppName(appName);            //Name of application
        MuSaver.setMutantFolder(mutantFolderFile);  //Folder of mutants          
        MuSaver.LoadCode(strAppPath);

        oMutantMaker.generateMutants();
        nGeneratedMutants = oMutantMaker.getSize();

        //If numMutants is 0, all the mutants will be generated!
        while (oMutantMaker.hasNext() && ((numMutants == 0) || (numMutants > nIndex))) {

            Mutant oMutant = oMutantMaker.getNext();

            //Generar Lista de mutantes, la línea que afecta y 
            //comprobar que no nos dejamos nada            
            stringMutantList.add("Mutant: " + Integer.toString(nIndex)
                    + " | line: " + oMutant.getHead().getLineNumber()
                    + " | type: " + oMutant.getHead().getToken()
                    + " | operation: " + oMutant.getHead().getOperation().name());

            nGenIndex = MuSaver.generateCode(oMutant, nIndex);
            nIndex += nGenIndex;
        }
        nGeneratedMutants = nIndex;
        System.out.printf("Total Mutants generated: %d\n", nIndex);

        //Saving to disk...
        
        //Save this on the metrics folder
        saveListToDisk(stringMutantList);
    }

    String readFile(String path, Charset encoding) throws IOException {

        String line, text;
        BufferedReader br;
        StringBuilder sb;

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

    public String getBinaryFolder() {
        return binaryFolder;
    }

    public void setBinaryFolder(String binaryFolder) {
        this.binaryFolder = binaryFolder;
    }

    public void setApplicationFolder(String appFolder) {
        applicationFolder = appFolder;
    }

    public void setMutantFolder(String mutantFolder) {
        this.mutantFolder = mutantFolder;
    }

    public void setScenarioFolder(String strScenarioFolder) {
        this.scenarioFolder = strScenarioFolder;
    }

    public void setSelectedApp(String selectedApp) {
        this.appName = selectedApp;
    }

    public void setMutantsNumber(int numMutants) {
        this.numMutants = numMutants;
    }

    public void setConfig(MutationCfg muConfig) {
        mutationConfig = muConfig;
    }

    private void saveListToDisk(LinkedList<String> stringMutantList) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(STATS_MAIN_FOLDER+File.separator+appName+File.separator+"mutant_list.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < stringMutantList.size(); i++) {
                pw.println(stringMutantList.get(i));
            }

        } catch (IOException e) {
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
            }
        }
    }

    public void checkEquivalentMutants() {
        int nExistMutants;
        boolean bCreateInterMutants;

        bCreateInterMutants = false;
        bEquivalentMode = true;
        //First stage, create the mutants in an special folder
        //Check if the special source code of mutants yet exist
        nExistMutants = Auxiliar.checkIfMutantsExist(mutantFolder + File.separatorChar + appName + File.separatorChar + "bin");

        if (nExistMutants == 0 || Auxiliar.AskToUserGUI("Do you want to generate intermediate mutants to carry out the equivalence analysis?")) {
            bCreateInterMutants = true;
        }

        if (bCreateInterMutants) {
            //Create the mutants
            try {
                startProccess();
            } catch (MutomvoException sException) {
                System.out.println("Exception checking equivalent mutants\n");
            }
            bEquivalentMode = false;
        }

        //Second stage: compilation phase of the mutants
        //Third stage: md5 analysis
        equivalentChecker.setAppName(appName);
        equivalentChecker.setMutantsFolder(mutantFolder);
        equivalentChecker.setApplicationFolder(applicationFolder);
        equivalentChecker.setBinaryFolder(binaryFolder);
        equivalentChecker.doStart();

        //TODO: Si no existen estas carpetas, las creamos
        //Fourth stage: combination with existing results.
    }

    public int getGeneratedMutants() {
        return nGeneratedMutants;
    }

    //Generates a report to show users the mutation results
    public MutantsCollection generateMutantsReport() throws MutomvoException {

        File srcFile, mutantFolderFile;
        List<MutationOperator> OperatorsList;
        IMutationIndexer MUIndexer;
        MutationOperatorCharList oCharGenerator;
        
        MutantsCollection muColl;
        String strDir = System.getProperty("user.dir");
        System.out.printf("Getting a report\n");

        muColl = null;
        MUIndexer =  new CDTMutantIndexer();
        oCharGenerator = new MutationOperatorCharList();
        
        //Get content of SRC application
        mutantFolderFile = new File(mutantFolder);

        srcFile = new File(this.applicationFolder);
        nFileSize = srcFile.length();
        System.out.printf("File opened: %s , size: %d bytes\n", srcFile.getAbsolutePath(), nFileSize);

        //LookUP the mutations operators at the source code
        OperatorsList = MUIndexer.doIndex(oCharGenerator, srcFile);

        if (OperatorsList != null) {
            //Le pasamos la lista de operadores encontrados al mutador
            //que irá generando mutantes progresivamente
            muColl = mutantReport(OperatorsList, srcFile);
        }

        System.out.printf("Finished Mutant report generation\n");
        return muColl;
    }

    public MutantsCollection mutantReport(List<MutationOperator> OperatorsList, File srcFile) throws MutomvoException {
        MutantCodeSaver MuSaver = new MutantCodeSaver();
        String strAppPath, strReport;
        int nIndex;
        Mutator oMutantMaker;
        MutantsCollection muColl;
        LinkedList<mutantInfo> mList;

        //Init
        strAppPath = srcFile.getAbsolutePath();
        nIndex = 0;

        oMutantMaker = new BasicMutator(OperatorsList); //Vamos a hacer un mutante por operador, para no joder mucho        
        muColl = new MutantsCollection();

        MuSaver.setAppName(appName);            //Name of application             
        MuSaver.LoadCode(strAppPath);

        oMutantMaker.generateMutants();
        
        //Si numMutants es 0, tomamos que queremos generar todos
        while (oMutantMaker.hasNext() && ((numMutants == 0) || (numMutants > nIndex))) {

            Mutant oMutant = oMutantMaker.getNext();

            mList = MuSaver.generateReport(oMutant, nIndex);
            muColl.insertMutantList(mList);
            nIndex += mList.size();
        }
        strReport = muColl.genShortReport();

        System.out.printf("Total Mutants generated: %d\n", nIndex);
        System.out.printf(strReport);

        return muColl;
    }
}
