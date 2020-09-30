package mutomvo.TabbedPanels;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import mutomvo.MutomvoGUIView;
import mutomvo.Utils.Utils;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.Mutation.Config.MutationCfg;
import mutomvo.Mutation.Execution.ExecutionConfig;
import mutomvo.Mutation.Execution.JavaMuTeXec;
import mutomvo.Mutation.Execution.auxiliars.IMuTeXec;
import mutomvo.Mutation.Execution.info.mutants.MutantsCollection;
import mutomvo.Mutation.MutationEngine;
import mutomvo.Mutation.ReportGenerator.reportGenerator;
import mutomvo.TabbedPanels.dataClasses.MutantOperator;
import mutomvo.TabbedPanels.dataClasses.MutationProject;
import mutomvo.Tables.MutationTableModel;
import mutomvo.Tables.TableLinesSelection;

/**
 * Panel for editing a Switch component
 *
 * @author Alberto Núñez Covarrubias
 */
public class MutationPanel extends javax.swing.JPanel {

    MutomvoGUIView mainFrame;
    MutationProject currentLoadedProject;
    TestGeneratorPanel testPanel;
    TableLinesSelection lineSelection;
    private int nGeneratedTests;
    private boolean bLoadFromInterFile;
    IMuTeXec testRunner;
    
    /**
     * Initializes a Panel for editing the configuration of the mutation testing
     * process.
     *
     * @param mainFrame Main Frame of this GUI
     */
    public MutationPanel(MutomvoGUIView mainFrame) {

        initComponents();
        this.mainFrame = mainFrame;
        this.initPanel();

        // There is no loaded project when the app starts
        currentLoadedProject = new MutationProject();

        // Init mutation table
        Utils.initDataInMutationTable(tableMutation);

        //Test lines selection
        lineSelection = new TableLinesSelection();/*(Frame)this.getTopLevelAncestor());*/
        
        // Test configuration
        testPanel = new TestGeneratorPanel((Frame) getTopLevelAncestor());
        testPanel.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        testRunner = new JavaMuTeXec(mainFrame.getFrame(), true);   
    }

    /**
     * Constructor with no arguments.
     *
     */
    public MutationPanel() {
    }

    /**
     * Initialises current mutation panel
     *
     */
    private void initPanel() {

        try {

            // Init to empty values
            textProject.setText("");
            textNumFixedMutants.setText("0");
            textProgram.setText("");
            maxMutantsCheckBox.setSelected(true);

            // Init mutation table
            Utils.initDataInMutationTable(tableMutation);

        } catch (Exception e) {
            Utils.showErrorMessage(e.getLocalizedMessage(), "[MutationPanel] Error while initializing!");
        }
    }

    /**
     * Check if main values of the mutation panel are correct
     *
     * @return True if main values are OK or false in other case
     */
    private boolean checkValuesInMutationPanel() {

        boolean allOK, oneSelected;
        File file;
        int i;

        //Init...
        allOK = true;
        oneSelected = false;
        i = 0;

        // Check the name of the project! 
        if ((!Utils.checkName(textProject.getText())) && (allOK)) {

            allOK = false;
            Utils.showErrorMessage("Invalid name for this project.\n"
                    + Utils.nameRulesMessage,
                    "Wrong name!");
        }

        // Check the path of the source program
        if (allOK) {

            // Get the path of the source program
            file = new File(textProgram.getText());

            if (!file.exists()) {

                allOK = false;
                Utils.showErrorMessage("Source program not found.\n"
                        + Utils.nameRulesMessage,
                        "Wrong path!");
            }
        }

        // Check the path of the project folder
        if (allOK) {

            // If the folder does not exist
            file = new File(textProjectFolder.getText());

            if (!file.exists()) {

                allOK = false;
                Utils.showErrorMessage("Project folder not found",
                        "Wrong path!");
            } // If the path is not a folder
            else if (!file.isDirectory()) {
                allOK = false;
                Utils.showErrorMessage("The selected path to store the current project is not a folder",
                        "Wrong path!");
            }
        }

        // Check if the number of mutants is fixed
        if (maxMutantsCheckBox.isSelected() && (allOK)) {

            // Check the number of mutants
            try {
                if (allOK) {
                    Integer.parseInt(textNumFixedMutants.getText());
                }
            } catch (Exception e) {

                allOK = false;
                Utils.showErrorMessage("Wrong parameter for number of mutants.\n"
                        + "This parameter must be an integer.\n",
                        "Wrong parameter!");
            }
        }

        // Check operators
        if (allOK) {

            // At least one operator must be selected!
            while ((!oneSelected) && (i < tableMutation.getModel().getRowCount())) {

                if (((Boolean) tableMutation.getModel().getValueAt(i, 2))) {
                    oneSelected = true;
                }

                i++;
            }

            if (!oneSelected) {

                allOK = false;
                Utils.showErrorMessage("At least one mutation operator must be selected!", "Wrong configuration!");
            }
        }

        return allOK;
    }

    /**
     * Loads a mutation project into the panel
     *
     * @param projectName
     * @return
     */
    public boolean loadMutationProjectInPanel(String projectName) {

        int response;
        MutationProject project;
        boolean ok;

        ok = false;

        try {

            // Ask for clear current config
            response = JOptionPane.showConfirmDialog(null,
                    "A new project will be loaded. Previous data will be lost.\nAre you OK to proceed?",
                    "Confirm to load a new configuration",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // Proceed to load a new configuration
            if (response == JOptionPane.OK_OPTION) {

                // Load Config from file
                project = Utils.loadMutationProject(projectName);

                // If current config exists, then load its values!
                if (project != null) {
                    currentLoadedProject = project;
                    this.loadMutationProjectObjectInPanel(currentLoadedProject);
                    ok = true;
                }
            }
        } catch (MutomvoException e) {
            Utils.showErrorMessage(e.getMessage(), "[ScenarioPanel] Error while loading a scenario into the panel!");
        }

        return ok;
    }

    /**
     * Loads a project object into panel
     */
    private void loadMutationProjectObjectInPanel(MutationProject currentProject) {

        int i;

        try {
            textProject.setText(currentProject.getProjectName());
            textProgram.setText(currentProject.getSourceProgramPath());
            maxMutantsCheckBox.setSelected(currentProject.useFixedMutants());
            textProjectFolder.setText(currentProject.getProjectFolder());
            textComment.setText(currentProject.getComment());
            
            lineSelection.resetFilteredLines();
            lineSelection.insertData(currentProject.getFilteredLinesList());
            
            if (currentProject.useFixedMutants()) {
                textNumFixedMutants.setText(Integer.toString(currentProject.getNumFixedMutants()));
            }

            checkBoxArithmetic.setSelected(currentProject.isSelectedArithmetic());
            checkBoxRelational.setSelected(currentProject.isSelectedRelational());
            checkBoxShifting.setSelected(currentProject.isSelectedShifting());
            checkBoxConditional.setSelected(currentProject.isSelectedConditional());
            checkBoxAll.setSelected(currentProject.isSelectedLogical());

            for (i = 0; i < currentProject.getNumOperators(); i++) {
                tableMutation.getModel().setValueAt(currentProject.getOperatorByIndex(i).isIsSelected(), i, 2);
            }

        } catch (Exception e) {
            Utils.showErrorMessage(e.getLocalizedMessage(), "[MutationPanel] Error while loading a mutation configuration into panel!");
        }
    }

    /**
     * Copies each value from the panel to a MutationProject object
     *
     * @return MutationProject containing the values from the panel
     */
    private MutationProject panelToMutationProjectObject() {

        MutationProject currentProject;
        MutantOperator currentMutantOperator;
        LinkedList<LinkedList<String>> filteredList;
        int currentRow;

        // Init
        currentProject = new MutationProject();
        filteredList = null;
        
        if(lineSelection != null)
            filteredList = lineSelection.getFilterLinesList();
        
        // Set each parameter in MutationConfig object
        currentProject.setProjectName(textProject.getText());
        currentProject.setSourceProgramPath(textProgram.getText());
        currentProject.setUseFixedMutants(maxMutantsCheckBox.isSelected());
        currentProject.setProjectFolder(textProjectFolder.getText());

        currentProject.setComment(textComment.getText());

        currentProject.setSelectedArithmetic(checkBoxArithmetic.isSelected());
        currentProject.setSelectedConditional(checkBoxConditional.isSelected());
        currentProject.setSelectedLogical(checkBoxAll.isSelected());
        currentProject.setSelectedShifting(checkBoxShifting.isSelected());
        currentProject.setSelectedRelational(checkBoxRelational.isSelected());

        currentProject.setNumFixedTests(currentLoadedProject.getNumFixedTests());
        currentProject.setUseFixedTests(currentLoadedProject.useFixedTests());
        currentProject.setTestConfigLine(currentLoadedProject.getTestConfigLine());
        currentProject.setFilteredLinesList(filteredList);
        
        // Saves mutant table to an object
        for (currentRow = 0; currentRow < tableMutation.getModel().getRowCount(); currentRow++) {

            // Create a new mutant operator
            currentMutantOperator = new MutantOperator();

            // Saves current information to mutant operator object
            currentMutantOperator.setAcronym(tableMutation.getModel().getValueAt(currentRow, 0).toString());
            currentMutantOperator.setDescription(tableMutation.getModel().getValueAt(currentRow, 1).toString());
            currentMutantOperator.setIsSelected(((Boolean) tableMutation.getModel().getValueAt(currentRow, 2)).booleanValue());
            currentProject.addOperator(currentMutantOperator);
        }

        try {

            if (maxMutantsCheckBox.isSelected()) {
                currentProject.setNumFixedMutants(Integer.parseInt(textNumFixedMutants.getText()));
            } else {
                currentProject.setNumFixedMutants(0);
            }
        } catch (NumberFormatException e) {
            Utils.showErrorMessage("Wrong number format.\n"
                    + "The number of mutants must be an integer.\n"
                    + "Setting the number of fixed mutants to 0!", "Wrong number");
            currentProject.setNumFixedMutants(0);
        }

        return currentProject;
    }

    /**
     * Writes the configured project in this panel to a file
     *
     * @return True if the file has been correctly written and False in other
     * case
     */
    private boolean writeMutationConfigFile() {

        String newProjectFile;
        int response;
        FileOutputStream fout;
        ObjectOutputStream oos;
        boolean allOK;
        LinkedList<LinkedList<String>> testGeneratorConfig;

        // Init...
        allOK = false;
        //numFixedTests = 0;

        //Check if there exist some interval of lines where the mutations must not be seeded.            
        updateFilterLines();
            
        // Check main values
        if (checkValuesInMutationPanel()) {

            // Saves this panel to an object
            currentLoadedProject = panelToMutationProjectObject();

            // Generate new file's complete path
            newProjectFile = Utils.projectsFolder + File.separatorChar + textProject.getText();

            try {

                // Exists current file?
                if (new File(newProjectFile).exists()) {

                    // Ask for overwritting current file
                    response = JOptionPane.showConfirmDialog(null,
                            "Overwrite existing configuration?",
                            "Confirm Overwrite",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    // If ok, then overwrite
                    if (response == JOptionPane.OK_OPTION) {

                        // Write file to disk!
                        fout = new FileOutputStream(newProjectFile);
                        oos = new ObjectOutputStream(fout);
                        oos.writeObject(currentLoadedProject);
                        oos.close();
                        allOK = true;
                    }
                } // New file
                else {

                    // Write file to disk!
                    fout = new FileOutputStream(newProjectFile);
                    oos = new ObjectOutputStream(fout);
                    oos.writeObject(currentLoadedProject);
                    oos.close();
                    allOK = true;
                }

                // Check the configuration files
                checkProjectFiles();
            } catch (Exception e) {
                Utils.showErrorMessage(e.getMessage(), "[MutationPanel] Error while saving Configuration!");
            }
        }

        return allOK;
    }

    /**
     * Checks if the configuration files of the project are created.
     *
     * @return
     */
    private void checkProjectFiles() {

        File file;
        Writer writer;

        try {

            // Check mutants folder
            file = new File(textProjectFolder.getText() + File.separatorChar + Utils.defaultMutantsFolder);

            if (!file.exists()) {
                file.mkdir();
            }

            // Check testConfig file
            file = new File(textProjectFolder.getText() + File.separatorChar + Utils.defaultTestConfigFile);

            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(currentLoadedProject.getTestConfigLine());
            writer.close();

            // Check tests file
            file = new File(textProjectFolder.getText() + File.separatorChar + Utils.defaultTestFile);

            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            Utils.showErrorMessage(ex.getLocalizedMessage(), "Error while creating config files.");
        }
    }

    /**
     * Generate mutants using the configuration of this panel
     */
    private void generateMutants() {

        File applicationFolder;             // File to the folder of the selected application
        File scenarioFolder;                // File to the folder of selected scenario
        File mutantFolder;                  // Mutant folder into the application
        MutationEngine mutantCreator;
        MutationCfg muConfig;
        int numMutants, i, nGeneratedMutants;                  // Number of mutants
        String selectedAppName;
        long startTime, estimatedTime;

        // Init...
        mutantCreator = new MutationEngine();
        numMutants = 0;

        try {

            // Init...
            startTime = System.currentTimeMillis();

            // Gets the project folder
            scenarioFolder = new File(currentLoadedProject.getProjectFolder());

            // Check if scenario folder exists...
            if (!scenarioFolder.exists()) {
                throw new MutomvoException("Project folder not found.\nPlease, generate configuration files in Scenario tab");
            }

            // Debug...
            System.out.println("Selected scenario:" + scenarioFolder.getAbsolutePath());

            selectedAppName = currentLoadedProject.getAppName();

            // Get the application's path
            applicationFolder = new File(currentLoadedProject.getSourceProgramPath());

            // Debug...
            System.out.println("Selected application:" + applicationFolder.getAbsolutePath());

            // Check if application folder exists...
            if (!applicationFolder.exists()) {
                throw new MutomvoException("Application folder not found. Please, check manually!");
            }

            // Get the number of mutants to generate
            try {
                if (currentLoadedProject.getNumFixedMutants() > 0) {
                    numMutants = currentLoadedProject.getNumFixedMutants();
                } else {
                    numMutants = 0;
                }

            } catch (NumberFormatException e) {
                //e.printStackTrace();
                //Utils.showErrorMessage(e.getMessage(), "[MutationPanel] Wrong format for number of mutants!");
                numMutants = 0;
            }

            mutantCreator.setMutantsNumber(numMutants);
            mutantCreator.setSelectedApp(selectedAppName);
            mutantCreator.setScenarioFolder(scenarioFolder.getAbsolutePath());
            mutantCreator.setApplicationFolder(applicationFolder.getAbsolutePath());
            mutantCreator.setMutantFolder(currentLoadedProject.getProjectFolder() + File.separatorChar + Utils.defaultMutantsFolder);

            MutationCfg.getInstance().insertOperatorsConfig(currentLoadedProject);
           
            
            mutantCreator.startProccess();

            // End
            estimatedTime = System.currentTimeMillis() - startTime;

            nGeneratedMutants = mutantCreator.getGeneratedMutants();

            System.out.println("Time for generating mutants: " + estimatedTime + " ms\n");

        } catch (MutomvoException e) {
            e.printStackTrace();
            Utils.showErrorMessage(e.getMessage(), "[MutationPanel] Error loading components from folder!");
        }
    }

    /**
     * Methods that calculates if there exists previous mutants
     *
     */
    private int getNumberOfCurrentMutants() {
//
        int result;
        File applicationFolder;             // Folder of the selected application
        String selectedAppName;             // Application instance
        File mutantsFolder;

        // Init...
        result = 0;

        // Generate mutants folder
        mutantsFolder = new File(textProjectFolder.getText() + File.separatorChar + Utils.defaultMutantsFolder);

        // Check if application folder exists...
        if (!mutantsFolder.exists()) {
            result = 0;
        } else {
            result = mutantsFolder.listFiles().length;
        }

        return result;
    }

    /**
     * Generate mutants
     *
     * @param projectName
     * @return
     */
    public String generateMutantsFromCommandLine(String projectName) throws MutomvoException, FileNotFoundException, IOException {

        String text;
        MutationProject project;
        File mutantsFolder, originalProgram, originalProgramCopyFolder, originalProgramCopyFile;
        FileOutputStream fout;
        ObjectOutputStream oos;
        int i;

        text = "";

        // Load current configuration
        project = Utils.loadMutationProject(projectName);

        //Set the loaded project
        currentLoadedProject = project;

        // Generate new file's complete path
        mutantsFolder = new File(project.getProjectFolder() + File.separatorChar + Utils.defaultMutantFolder);

        // Mutatns folder does not exists
        if (!mutantsFolder.exists() || !mutantsFolder.isDirectory()) {
            text = "Folder " + mutantsFolder.getAbsolutePath() + "does not exists";
        } else {
            // Remove all the previous mutants        
            Utils.deleteFolder(mutantsFolder);

            // Create "mutants" folder (empty)
            mutantsFolder.mkdir();

            // Get source program source path
            originalProgram = new File(project.getSourceProgramPath());

            // Folder of the original program in the mutants folder
            originalProgramCopyFolder = new File(mutantsFolder.getAbsolutePath() + File.separatorChar + "0");
            originalProgramCopyFile = new File(originalProgramCopyFolder.getAbsolutePath() + File.separatorChar + originalProgram.getName());
            originalProgramCopyFolder.mkdirs();

            // Copy original file into the mutants folder
            Utils.copyFile(originalProgram, originalProgramCopyFile);

            // Generate mutants
            this.generateMutants();
        }

        return text;
    }

    /**
     * Generates mutants and tests
     *
     * @param configName Name of the configuration used to create mutants and
     * tests
     */
    public String generateFromConsole(String configName, boolean execute) {
//public String generateFromConsole(String configName, ExecutionConfig exeConfig, boolean execute){
//    String strReturn = null;
//    
//        try {
//            String text, messageToUser,selectedAppType;
//            int numMutantsInFolder;
//            boolean reWriteMutants, bExecuteAllTests;
//            
//           // Init...
//           text = "Generating mutants.. (TBI)";
//           reWriteMutants = false;
//           bExecuteAllTests = true;
//           strReturn = "";
//           
//           //TODO: Enlazar aquí la generación de mutantes y de tests (modo consola)
//           currentLoadedConfig = Utils.loadMutationProject(configName);
//           createSelectedTestGeneratorObject(currentLoadedConfig.getTestClass());
//           
//           //ASk to the user
//           // Check if there is mutants previously created
//           numMutantsInFolder = this.getNumberOfCurrentMutants();   
//           nGeneratedMutants = numMutantsInFolder;
//           nGeneratedTests = currentLoadedConfig.getNumFixedTests();
//
//           // There are some mutants in the application folder!
//           //TODO: No va muy bien las preguntas al usuario. Lo fuerzo para que genere siempre los mutantes y tests. Comprobar esto!!!!
////           if (numMutantsInFolder>0){               
////               
////               messageToUser = "There exists " + Integer.toString(numMutantsInFolder) + " mutants in the application folder.\nDo you want to re-generate mutants again?\n" +
////                   "If you proceed, current mutants will be overwritten!/n Insert: y/n";        
////               
////               if(exeConfig.AskUser() && AskToUser(messageToUser, text)){
////                   System.out.printf(messageToUser);
////                   reWriteMutants = true;
////                   generateMutants();
////                   generateTests();
////                   strReturn += "Mutants and tests successfully generated\n";
////                   System.out.printf("Clean: yes\n");
////               }
////               else{
////                   System.out.printf("Clean: no\n");
////                   nGeneratedMutants = numMutantsInFolder;
////                   nGeneratedTests = currentLoadedConfig.getNumFixedTests();                   
////                   System.out.printf("Stored mutants: %d | tests: %d no\n", nGeneratedMutants,nGeneratedTests);
////               }
////           }      
//           
//           
//           
//           if (!execute){
//                System.out.println ("There are currently " + numMutantsInFolder + " mutants generated!\n");
//                System.out.println("Generating mutants and tests\n");
//                generateMutants();
//                generateTests();
//                strReturn += "Mutants and tests successfully generated\n";
//           }
//          // }
//            
//           
//           if (execute){
//           
//                nGeneratedMutants = numMutantsInFolder;
//                nGeneratedTests = currentLoadedConfig.getNumFixedTests();
//           
//                //Ask to user if wants to execute all tests
//                if(exeConfig.AskUser() &&  AskToUser("Do you want to execute all tests?","n"))
//                {
//                    //Execute all tests
//                    bExecuteAllTests=false;
//                }    
//
//                if(testRunner == null)
//                {
//                    testRunner = new JavaMuTeXec(null, true);
//                    System.out.printf("Testrunner is empty, creating new testRunner\n");
//                }
//                selectedAppType = Utils.loadApplicationObject(currentLoadedConfig.getApplication()).getName();
//
//                exeConfig.setGenMutants(nGeneratedMutants);
//                exeConfig.setGenTests(nGeneratedTests);
//                exeConfig.setApp(currentLoadedConfig.getApplication());
//                exeConfig.setAppType(selectedAppType);
//                exeConfig.setScenario(currentLoadedConfig.getScenario());
//                exeConfig.setRWMutants(reWriteMutants);
//                exeConfig.setExecuteAllTests(bExecuteAllTests);
//
//
//                testRunner.reset(currentLoadedConfig.getScenario());
//                //testRunner.Configure(nGeneratedMutants,nGeneratedTests, currentLoadedConfig.getApplication(),selectedAppType, currentLoadedConfig.getScenario(),reWriteMutants, bExecuteAllTests);
//                testRunner.Configure(exeConfig);
//                testRunner.forceExecutionCmd();
//
//                strReturn = "Execution done!";
//            }    
//           
//        } 
//           catch (MutomvoException ex) {
//            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        catch (HeadlessException e)
//        {
//            System.out.printf("X11 - Config problems\n");
//            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, e);
//        }
//        catch (Exception ex) {
//            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return  strReturn;
        return new String("");
    }

    private boolean deleteAllMutants() {
//    boolean bRet =false;
//    ApplicationCollection appsInRepo = new ApplicationCollection();
//    LinkedList<String> appList;
//    String collectionName;
//        
//    try {
//        appsInRepo.loadModulesFromRepository();
//        appList = appsInRepo.getApplicationNames();
//        
//        for(int i = 0;i<appList.size();i++)
//        {
//            collectionName = appList.get(i);
//            deleteMutants(collectionName);
//        }
//    } catch (MutomvoException ex) {
//        Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    
//    return bRet;
        return false;
    }

    /**
     * Methods that calculates if there exists previous mutants
     *
     */
    private boolean deleteMutants(String strApp) {

//    boolean result;
//    SimcanApplication selectedApp;      // Application for performing mutation
//    File applicationFolder;             // Folder of the selected application
//    String selectedAppName;             // Application instance
//    File mutantsFolder;
//    Configuration configFile;
//    
//        // Init...
//        result = false;     
//        configFile = new Configuration();                
//
//        try {        
//
//           /* // Load selected application object
//            //selectedApp = Utils.loadApplicationObject(comboApplication.getSelectedItem().toString());        
//            selectedApp = Utils.loadApplicationObject(strApp);        
//            
//            // Get application name
//            selectedAppName = selectedApp.getName();
//*/
//            // Get the application's path
//            applicationFolder = new File(configFile.getProperty(Configuration.HOME_PATH)
//                    + configFile.APPLICATIONS_PATH + File.separatorChar
//                    + strApp);        
//
//            // Check if application folder exists...
//            if (!applicationFolder.exists()) {
//                throw new MutomvoException("Application folder not found. Please, check manually!");
//            }
//
//            // Generate mutants folder
//            mutantsFolder = new File (applicationFolder.getAbsolutePath() + File.separatorChar + Configuration.defaultMutantFolder);
//
//            // Check if application folder exists...
//            if (!mutantsFolder.exists()) {
//               result = false;
//            }
//            else{     
//                Utils.deleteFolder(mutantsFolder);                           
//                result = true;
//            }
//        }
//        catch (MutomvoException e) {
//            result = false;
//         }
//    
//    return result;
        return false;
    }

    private void searchAndSelect(String[][] operatorList, Vector v, boolean bValue) {
        String strCompare, strVector;
        Vector vCompare;
        for (int i = 0; i < operatorList.length; i++) {
            strCompare = operatorList[i][0];
            for (int j = 0; j < v.size(); j++) {
                vCompare = (Vector) v.get(j);
                strVector = (String) vCompare.get(0);

                if (strVector.contains(strCompare)) {
                    vCompare.set(2, bValue);
                }
            }

        }
    }

    private boolean AskToUserGUI(String messageToUser) {
        String text;
        boolean bRet = false;
        int response;

        response = JOptionPane.showConfirmDialog(null,
                messageToUser,
                "Confirm to re-load the test generator",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        // Proceed to load a new configuration
        if (response == JOptionPane.OK_OPTION) {

            bRet = true;
        }

        return bRet;
    }

    private boolean AskToUser(String messageToUser, String expected) {

        String text;
        boolean bRet = false;

        text = "";
        System.out.printf(messageToUser);
        //Ask the user...

        //catch the response
        InputStreamReader leer = new InputStreamReader(System.in);
        BufferedReader buff = new BufferedReader(leer);
        try {
            text = buff.readLine();
        } catch (IOException ex) {
            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (text.indexOf(expected) == 0) {
            bRet = true;
            System.out.printf("User response OK : %s vs %s \n", text, expected);
        } else {
            System.out.printf("User response KO : %s vs %s \n", text, expected);
        }

        return bRet;
    }

    public void action_deleteAllExistingMutants() {

        //Ask to user if want to delete all mutants
        if (AskToUserGUI("Do you want to delete all existing mutants?")) {
            System.out.printf("Deleting mutants ...\n");
            deleteAllMutants();
            System.out.printf("All mutants deleted!\n");
        }
    }

    public void action_filterLines() {
        
        lineSelection.setVisible(true);
        lineSelection.start(lineSelection);

    }

    private void doSelection(boolean bSelect) {
        String strSelected;

        MutationTableModel m = (MutationTableModel) tableMutation.getModel();
        Vector v = m.getDataVector();

        if (checkBoxArithmetic.isSelected()) {
            //Select all general checkbox            
            searchAndSelect(Utils.arithmeticList, v, bSelect);

        }
        if (checkBoxConditional.isSelected()) {
            //Select all conditional checkbox            
            searchAndSelect(Utils.conditionalList, v, bSelect);

        }
        if (checkBoxAll.isSelected()) {
            //Select all logic checkbox
            searchAndSelect(Utils.logicList, v, bSelect);

        }
        if (checkBoxRelational.isSelected()) {
            //Select all relational checkbox
            searchAndSelect(Utils.relationalList, v, bSelect);

        }
        
        if (checkBoxAll.isSelected()) {
            //Select all general checkbox
            searchAndSelect(Utils.operatorList,v, bSelect);
        }        

        tableMutation.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelProject = new javax.swing.JLabel();
        buttonSave = new javax.swing.JLabel();
        buttonClear = new javax.swing.JLabel();
        textNumFixedMutants = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMutation = new javax.swing.JTable();
        buttonGenerateFiles = new javax.swing.JLabel();
        labelSourceProgram = new javax.swing.JLabel();
        buttonConfigTest = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        checkBoxAll = new javax.swing.JCheckBox();
        checkBoxArithmetic = new javax.swing.JCheckBox();
        checkBoxRelational = new javax.swing.JCheckBox();
        checkBoxConditional = new javax.swing.JCheckBox();
        buttonSelectAll = new javax.swing.JButton();
        buttonSelectNone = new javax.swing.JButton();
        checkBoxShifting = new javax.swing.JCheckBox();
        checkBoxLogical1 = new javax.swing.JCheckBox();
        maxMutantsCheckBox = new javax.swing.JCheckBox();
        buttonSelectProgram = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textComment = new javax.swing.JTextArea();
        textProject = new javax.swing.JTextField();
        textProgram = new javax.swing.JTextField();
        labelProjectFolder = new javax.swing.JLabel();
        textProjectFolder = new javax.swing.JTextField();
        buttonProjectFolder = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(MutationPanel.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setToolTipText(resourceMap.getString("Form.toolTipText")); // NOI18N
        setMaximumSize(new java.awt.Dimension(790, 610));
        setMinimumSize(new java.awt.Dimension(790, 610));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(790, 610));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelProject.setFont(resourceMap.getFont("labelProject.font")); // NOI18N
        labelProject.setText(resourceMap.getString("labelProject.text")); // NOI18N
        labelProject.setName("labelProject"); // NOI18N
        add(labelProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        buttonSave.setIcon(resourceMap.getIcon("buttonSave.icon")); // NOI18N
        buttonSave.setToolTipText(resourceMap.getString("buttonSave.toolTipText")); // NOI18N
        buttonSave.setName("buttonSave"); // NOI18N
        buttonSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSaveMouseClicked(evt);
            }
        });
        add(buttonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 30, -1, -1));

        buttonClear.setIcon(resourceMap.getIcon("buttonClear.icon")); // NOI18N
        buttonClear.setToolTipText(resourceMap.getString("buttonClear.toolTipText")); // NOI18N
        buttonClear.setName("buttonClear"); // NOI18N
        buttonClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonClearMouseClicked(evt);
            }
        });
        add(buttonClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 30, 50));

        textNumFixedMutants.setText(resourceMap.getString("textNumFixedMutants.text")); // NOI18N
        textNumFixedMutants.setName("textNumFixedMutants"); // NOI18N
        add(textNumFixedMutants, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 90, -1));

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(750, 270));

        tableMutation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Operation", "Description", "Enabled"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableMutation.setGridColor(resourceMap.getColor("tableMutation.gridColor")); // NOI18N
        tableMutation.setName("tableMutation"); // NOI18N
        jScrollPane1.setViewportView(tableMutation);
        if (tableMutation.getColumnModel().getColumnCount() > 0) {
            tableMutation.getColumnModel().getColumn(0).setResizable(false);
            tableMutation.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tableMutation.columnModel.title0")); // NOI18N
            tableMutation.getColumnModel().getColumn(1).setResizable(false);
            tableMutation.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tableMutation.columnModel.title1")); // NOI18N
            tableMutation.getColumnModel().getColumn(2).setResizable(false);
            tableMutation.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tableMutation.columnModel.title2")); // NOI18N
        }

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 750, 260));

        buttonGenerateFiles.setIcon(resourceMap.getIcon("buttonGenerateFiles.icon")); // NOI18N
        buttonGenerateFiles.setToolTipText(resourceMap.getString("buttonGenerateFiles.toolTipText")); // NOI18N
        buttonGenerateFiles.setName("buttonGenerateFiles"); // NOI18N
        buttonGenerateFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonGenerateFilesMouseClicked(evt);
            }
        });
        add(buttonGenerateFiles, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 50, 50));

        labelSourceProgram.setFont(resourceMap.getFont("labelSourceProgram.font")); // NOI18N
        labelSourceProgram.setText(resourceMap.getString("labelSourceProgram.text")); // NOI18N
        labelSourceProgram.setName("labelSourceProgram"); // NOI18N
        add(labelSourceProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        buttonConfigTest.setText(resourceMap.getString("buttonConfigTest.text")); // NOI18N
        buttonConfigTest.setName("buttonConfigTest"); // NOI18N
        buttonConfigTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConfigTestActionPerformed(evt);
            }
        });
        add(buttonConfigTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, 130, -1));

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(780, 610));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(780, 610));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkBoxAll.setText(resourceMap.getString("checkBoxAll.text")); // NOI18N
        checkBoxAll.setName("checkBoxAll"); // NOI18N
        jPanel1.add(checkBoxAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        checkBoxArithmetic.setText(resourceMap.getString("checkBoxArithmetic.text")); // NOI18N
        checkBoxArithmetic.setName("checkBoxArithmetic"); // NOI18N
        jPanel1.add(checkBoxArithmetic, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        checkBoxRelational.setText(resourceMap.getString("checkBoxRelational.text")); // NOI18N
        checkBoxRelational.setName("checkBoxRelational"); // NOI18N
        jPanel1.add(checkBoxRelational, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        checkBoxConditional.setText(resourceMap.getString("checkBoxConditional.text")); // NOI18N
        checkBoxConditional.setName("checkBoxConditional"); // NOI18N
        jPanel1.add(checkBoxConditional, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        buttonSelectAll.setText(resourceMap.getString("buttonSelectAll.text")); // NOI18N
        buttonSelectAll.setName("buttonSelectAll"); // NOI18N
        buttonSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectAllActionPerformed(evt);
            }
        });
        jPanel1.add(buttonSelectAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 110, 20));

        buttonSelectNone.setText(resourceMap.getString("buttonSelectNone.text")); // NOI18N
        buttonSelectNone.setName("buttonSelectNone"); // NOI18N
        buttonSelectNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectNoneActionPerformed(evt);
            }
        });
        jPanel1.add(buttonSelectNone, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 110, 20));

        checkBoxShifting.setText(resourceMap.getString("checkBoxShifting.text")); // NOI18N
        checkBoxShifting.setName("checkBoxShifting"); // NOI18N
        jPanel1.add(checkBoxShifting, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        checkBoxLogical1.setText(resourceMap.getString("checkBoxLogical1.text")); // NOI18N
        checkBoxLogical1.setName("checkBoxLogical1"); // NOI18N
        jPanel1.add(checkBoxLogical1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 380, 110));

        maxMutantsCheckBox.setFont(resourceMap.getFont("maxMutantsCheckBox.font")); // NOI18N
        maxMutantsCheckBox.setText(resourceMap.getString("maxMutantsCheckBox.text")); // NOI18N
        maxMutantsCheckBox.setName("maxMutantsCheckBox"); // NOI18N
        maxMutantsCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                maxMutantsCheckBoxStateChanged(evt);
            }
        });
        add(maxMutantsCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, -1, -1));

        buttonSelectProgram.setText(resourceMap.getString("buttonSelectProgram.text")); // NOI18N
        buttonSelectProgram.setName("buttonSelectProgram"); // NOI18N
        buttonSelectProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectProgramActionPerformed(evt);
            }
        });
        add(buttonSelectProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, -1, -1));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 440, -1, -1));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        textComment.setColumns(20);
        textComment.setRows(5);
        textComment.setName("textComment"); // NOI18N
        jScrollPane3.setViewportView(textComment);

        add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 460, 350, 100));

        textProject.setText(resourceMap.getString("textProject.text")); // NOI18N
        textProject.setName("textProject"); // NOI18N
        add(textProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 230, -1));

        textProgram.setText(resourceMap.getString("textProgram.text")); // NOI18N
        textProgram.setName("textProgram"); // NOI18N
        add(textProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 400, -1));

        labelProjectFolder.setFont(resourceMap.getFont("labelProjectFolder.font")); // NOI18N
        labelProjectFolder.setText(resourceMap.getString("labelProjectFolder.text")); // NOI18N
        labelProjectFolder.setName("labelProjectFolder"); // NOI18N
        add(labelProjectFolder, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        textProjectFolder.setText(resourceMap.getString("textProjectFolder.text")); // NOI18N
        textProjectFolder.setName("textProjectFolder"); // NOI18N
        add(textProjectFolder, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 400, -1));

        buttonProjectFolder.setText(resourceMap.getString("buttonProjectFolder.text")); // NOI18N
        buttonProjectFolder.setName("buttonProjectFolder"); // NOI18N
        buttonProjectFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonProjectFolderActionPerformed(evt);
            }
        });
        add(buttonProjectFolder, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Method executed for saving a mutation configuration into a file
     *
     * @param evt Mouse event
     */
private void buttonSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSaveMouseClicked

    boolean allOK;

    // Write Mutation config to file
    allOK = this.writeMutationConfigFile();

    // If the project has been saved, update the tree
    if (allOK) {
        mainFrame.updateTree();
    }
}//GEN-LAST:event_buttonSaveMouseClicked

    /**
     * Method executed for clearing this panel
     *
     * @param evt Mouse event
     */
private void buttonClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonClearMouseClicked

    int response;

    // Ask for clear current config
    response = JOptionPane.showConfirmDialog(null,
            "Clear current configuration?",
            "Confirm to clear this form",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    // If ok, then clear!
    if (response == JOptionPane.OK_OPTION) {
        this.initPanel();
    }
}//GEN-LAST:event_buttonClearMouseClicked

    /**
     * Generate files for mutants and tests using the configuration of this
     * panel
     *
     * @param evt Event
     */
private void buttonGenerateFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonGenerateFilesMouseClicked

    boolean allOK, proceedWithExecution, reWriteMutants;
    String selectedAppType;
    int numMutants;
    int response;

    // Write Mutation config to file
    reWriteMutants = true;
    allOK = this.writeMutationConfigFile();
    proceedWithExecution = false;

    // Add new mutation config to the tree!        
    if (allOK) {

        // Check if there is mutants previously created
        numMutants = this.getNumberOfCurrentMutants();

        // There are some mutants in the application folder!
        if (numMutants > 0) {

            // Ask the user...
            response = JOptionPane.showConfirmDialog(null,
                    "There exists " + Integer.toString(numMutants) + " mutants in the application folder.\nDo you want to re-generate mutants again?\n"
                    + "If you proceed, current mutants will be overwritten!",
                    "Confirm to overwrite mutants",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // Re-write mutants and proceed with the execution
            if (response == JOptionPane.OK_OPTION) {
                this.generateMutants();
                proceedWithExecution = true;
            } // Do not generate mutants and proceed with the execution
            else if (response == JOptionPane.NO_OPTION) {
                proceedWithExecution = true;
                reWriteMutants = false;
                currentLoadedProject.setNumGeneratedMutants(numMutants);
            }

            // Cancel the execution!
            if (response == JOptionPane.CANCEL_OPTION) {
                proceedWithExecution = false;
            }

            /* if(AskToUserGUI("Do you want to re-generate the test suite?"))
                {
                    this.generateTests();
                }
                else
                {                
                    //TODO: Obtain the number of test from .ini file
                    //TODO: Ask the user if want to re-generate tests
                    nGeneratedTests=currentLoadedConfig.getNumFixedTests();                    
                }*/
        } // No previous mutants, generate mutants and proceed with the execution
        else {
            this.generateMutants();
            //this.generateTests();
            proceedWithExecution = true;
        }
                   
        // Proceed with the execution?
        if (proceedWithExecution){     
            ExecutionConfig execCfg;
            execCfg = createExecutionConfig(currentLoadedProject, reWriteMutants);
            
            try 
            {                
                selectedAppType = currentLoadedProject.getAppName();
                
                testRunner.reset(currentLoadedProject.getProjectName());
                testRunner.Configure(execCfg);
                
                // Show execution panel!
                testRunner.setVisible(true);
            } catch (MutomvoException ex) {
                System.out.println("Error configuring the test runner, reasons: "+execCfg.getParametersNeeded());
                Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}//GEN-LAST:event_buttonGenerateFilesMouseClicked

private void maxMutantsCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_maxMutantsCheckBoxStateChanged

    if (maxMutantsCheckBox.isSelected()) {
        textNumFixedMutants.setEnabled(true);
    } else {
        textNumFixedMutants.setEnabled(false);
    }
}//GEN-LAST:event_maxMutantsCheckBoxStateChanged

    private void buttonSelectNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectNoneActionPerformed
        doSelection(false);
    }//GEN-LAST:event_buttonSelectNoneActionPerformed

    private void buttonSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectAllActionPerformed

        doSelection(true);
    }//GEN-LAST:event_buttonSelectAllActionPerformed

    private void buttonSelectProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectProgramActionPerformed

        JFileChooser chooser;
        int result;

        // Create a new file chooser
        chooser = new JFileChooser();
        chooser.setDialogTitle("Select the source program");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Set a filter to only accept c files
        chooser.removeChoosableFileFilter(chooser.getFileFilter());
        chooser.setFileFilter(new FileNameExtensionFilter("C source code files", "c"));
        result = chooser.showOpenDialog(this);

        // OK button!
        if (result == JFileChooser.APPROVE_OPTION) {
            textProgram.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_buttonSelectProgramActionPerformed

    private void buttonProjectFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonProjectFolderActionPerformed

        JFileChooser chooser;
        int result;

        // Create a new file chooser
        chooser = new JFileChooser();
        chooser.setDialogTitle("Select the source program");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = chooser.showOpenDialog(this);

        // OK button!
        if (result == JFileChooser.APPROVE_OPTION) {
            textProjectFolder.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_buttonProjectFolderActionPerformed

    private void buttonConfigTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConfigTestActionPerformed

        boolean allOK;

        // Write Mutation config to file
        allOK = this.writeMutationConfigFile();

        if (allOK) {
            testPanel.showTestConfiguration(currentLoadedProject);
        }
    }//GEN-LAST:event_buttonConfigTestActionPerformed
    private void updateFilterLines() 
    {
        LinkedList<LinkedList<String>> lineList;
        LinkedList<String> l;        
        String strComment, strLine, strSelected, strAux;
        int nLine, nLineInit, nLineEnd, nIndex;  
        
        nLine = nLineInit = nLineEnd = nIndex = -1;
        lineList = lineSelection.getFilterLinesList();
        
        if(lineList != null)
        {
            MutationCfg.getInstance().clearLinesInterval();
            
            for(int j=0;j<lineList.size();j++)
            {
                l = lineList.get(j);
                if(l != null)
                {
                    strLine = l.get(0);
                    strComment = l.get(1);
                    strSelected = l.get(2);
                    
                    System.out.printf("Handling filtering line: [%s, %s, %s]\n", strLine, strComment, strSelected);
                    
                    if(strLine.contains("-"))
                    {
                        nIndex = strLine.indexOf("-");
                        strAux = strLine.substring(0, nIndex);
                        if(!strAux.isEmpty())
                            nLineInit = Integer.parseInt(strAux);
                        strAux = strLine.substring(nIndex+1);
                        if(!strAux.isEmpty())
                            nLineEnd = Integer.parseInt(strAux); 
                        
                        MutationCfg.getInstance().setBadInterval(nLineInit, nLineEnd);
                    }
                    else
                    {
                        nLine = Integer.parseInt(strLine);
                        MutationCfg.getInstance().setBadLine(nLine);
                    }
                }           
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel buttonClear;
    private javax.swing.JButton buttonConfigTest;
    private javax.swing.JLabel buttonGenerateFiles;
    private javax.swing.JButton buttonProjectFolder;
    private javax.swing.JLabel buttonSave;
    private javax.swing.JButton buttonSelectAll;
    private javax.swing.JButton buttonSelectNone;
    private javax.swing.JButton buttonSelectProgram;
    private javax.swing.JCheckBox checkBoxAll;
    private javax.swing.JCheckBox checkBoxArithmetic;
    private javax.swing.JCheckBox checkBoxConditional;
    private javax.swing.JCheckBox checkBoxLogical1;
    private javax.swing.JCheckBox checkBoxRelational;
    private javax.swing.JCheckBox checkBoxShifting;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelProject;
    private javax.swing.JLabel labelProjectFolder;
    private javax.swing.JLabel labelSourceProgram;
    private javax.swing.JCheckBox maxMutantsCheckBox;
    private javax.swing.JTable tableMutation;
    private javax.swing.JTextArea textComment;
    private javax.swing.JTextField textNumFixedMutants;
    private javax.swing.JTextField textProgram;
    private javax.swing.JTextField textProject;
    private javax.swing.JTextField textProjectFolder;
    // End of variables declaration//GEN-END:variables

    private ExecutionConfig createExecutionConfig(MutationProject currentLoadedProject, boolean reWriteMutants) {
      
        ExecutionConfig execCfg = new ExecutionConfig();
        String strOCompilationLine, strMCompilationLine, strTestSuiteFile, strMutantsPath;
        
        //TODO: Delete this fixed content!! Extract from an specific dialog.
        strOCompilationLine = "gcc -O3 -lm -Wall [[ORIGINAL_PATH]]/[[APP_NAME]].c -o [[ORIGINAL_PATH]]/[[APP_NAME]] -lcrypto -lssl";
        strMCompilationLine = "gcc -O3 -lm -Wall [[MUTANTS_PATH]]/[[INDEX_MUTANT]]/[[APP_NAME]].c -o [[MUTANTS_PATH]]/[[INDEX_MUTANT]]/[[APP_NAME]] -lcrypto -lssl";
        strTestSuiteFile = "/localSpace/mutomvo/project_bmp/testsFile.txt";    
        
        strMutantsPath = currentLoadedProject.getProjectFolder() + File.separator+Utils.defaultMutantsFolder+File.separator;
        
        execCfg.setNumGeneratedMutants(currentLoadedProject.getNumGeneratedMutants());
        execCfg.setNumGeneratedTests(currentLoadedProject.getNumGeneratedTests());
        execCfg.setAppName(currentLoadedProject.getAppName());
        execCfg.setAppPath(currentLoadedProject.getSourceProgramPath());
        execCfg.setMutantsPath(strMutantsPath);        
        execCfg.setFixedMutantsCfg(currentLoadedProject.getNumFixedMutants(),currentLoadedProject.useFixedMutants());
        
        execCfg.setOriginalCompilationLine(strOCompilationLine);
        execCfg.setMutantsCompilationLine(strMCompilationLine);
        execCfg.setProjectName(currentLoadedProject.getProjectName());
        execCfg.setIsRewriteMutants(reWriteMutants);
        execCfg.setTestSuitePath(strTestSuiteFile);
        execCfg.setFixedTestsCfg(currentLoadedProject.getNumFixedTests(),currentLoadedProject.useFixedTests());
        
        return execCfg;
    }

    public void action_generateMutantsReport() {
        
        GraphicsSelection graphPanel;
        String strName;
        boolean bGraphical;
        
        bGraphical = true;
        if(currentLoadedProject != null)
        {
            strName = currentLoadedProject.getAppName();            
            if(!strName.isEmpty())
                bGraphical = false;
        }
        
        if(!bGraphical)
        {
            reportGenerator repGen = new reportGenerator();
            repGen.generateReport(currentLoadedProject.getAppName());
        }
        else
        {
            graphPanel = new GraphicsSelection(mainFrame.getFrame());
            
            graphPanel.setVisible(true);        
        }
    }

    public void action_generatePrevShortMutantReport() {
      
        MutationEngine mutantCreator;
        String reportRet;
        MutantsCollection muColl;
        long startTime, estimatedTime;               
        
        try {                        
            mutantCreator = createMutantEngine(currentLoadedProject);       
            muColl = mutantCreator.generateMutantsReport();
            
            if(muColl != null)
            {
                //Initialise
                startTime = System.currentTimeMillis();   
                
                //Configure short report or extended report -> in order to make debugging!                 
                reportRet = muColl.genShortReport();

                // End
                estimatedTime = System.currentTimeMillis() - startTime;            

                System.out.println ("Time for generating report: " + estimatedTime + " ms\n");
                System.out.println ("Report: "+reportRet);

                JOptionPane.showMessageDialog(this,reportRet);
            }

        } catch (MutomvoException ex) {
            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    public void action_generatePrevExtendedMutantsReport() {
      
        MutationEngine mutantCreator;
        String reportRet;
        MutantsCollection muColl;
        long startTime, estimatedTime;               
        
        try {                        
            mutantCreator = createMutantEngine(currentLoadedProject);       
            muColl = mutantCreator.generateMutantsReport();
            
            if(muColl != null)
            {
                //Initialise
                startTime = System.currentTimeMillis();   
                
                //Configure short report or extended report -> in order to make debugging!                 
                reportRet = muColl.genReport();

                // End
                estimatedTime = System.currentTimeMillis() - startTime;            

                System.out.println ("Time for generating report: " + estimatedTime + " ms\n");
                System.out.println ("Report: "+reportRet);

                JOptionPane.showMessageDialog(this,reportRet);
            }

        } catch (MutomvoException ex) {
            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }           
    }
    private MutationEngine createMutantEngine(MutationProject currentLoadedProject) throws MutomvoException {
    
        MutationEngine mutantCreator;
        File scenarioFolder, applicationFolder;
        String selectedAppName;
        int numMutants;        
        
        mutantCreator = new MutationEngine();
        
        // Gets the project folder
        scenarioFolder = new File(currentLoadedProject.getProjectFolder());

        // Check if scenario folder exists...
        if (!scenarioFolder.exists()) {
            throw new MutomvoException("Project folder not found.\nPlease, generate configuration files in Scenario tab");
        }

        // Debug...
        System.out.println("Selected scenario:" + scenarioFolder.getAbsolutePath());

        selectedAppName = currentLoadedProject.getAppName();

        // Get the application's path
        applicationFolder = new File(currentLoadedProject.getSourceProgramPath());

        // Debug...
        System.out.println("Selected application:" + applicationFolder.getAbsolutePath());

        // Check if application folder exists...
        if (!applicationFolder.exists()) {
            throw new MutomvoException("Application folder not found. Please, check manually!");
        }

        // Get the number of mutants to generate
        try {
            if (currentLoadedProject.getNumFixedMutants() > 0) {
                numMutants = currentLoadedProject.getNumFixedMutants();
            } else {
                numMutants = 0;
            }

        } catch (NumberFormatException e) {
            //e.printStackTrace();
            //Utils.showErrorMessage(e.getMessage(), "[MutationPanel] Wrong format for number of mutants!");
            numMutants = 0;
        }
        
        //Configure the engine
        mutantCreator.setMutantsNumber(numMutants);
        mutantCreator.setSelectedApp(selectedAppName);
        mutantCreator.setScenarioFolder(scenarioFolder.getAbsolutePath());
        mutantCreator.setApplicationFolder(applicationFolder.getAbsolutePath());
        mutantCreator.setMutantFolder(currentLoadedProject.getProjectFolder() + File.separatorChar + Utils.defaultMutantsFolder);

        MutationCfg.getInstance().insertOperatorsConfig(currentLoadedProject);  
            
        return mutantCreator;
    }

    public void action_analyseEquivalentMutants() {
    
MutationEngine mutantCreator;
        String reportRet;
        MutantsCollection muColl;
        long startTime, estimatedTime;               
        
        try {                        
            mutantCreator = createMutantEngine(currentLoadedProject);       
            if(mutantCreator != null)
            {
                mutantCreator.checkEquivalentMutants();
            }

        } catch (MutomvoException ex) {
            Logger.getLogger(MutationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }

    


}
