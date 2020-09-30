package mutomvo.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.TabbedPanels.dataClasses.MutationProject;
import mutomvo.Tables.MutationTableModel;

/**
 * Some utilities
 *
 * @author Alberto Núñez Covarrubias
 */
public class Utils {

    /**
     * Root node in the tree
     */
    public static final String nodeRoot = "Projects";

    /**
     * String that marks the root path in the jar file
     */
    public static final String rootPathInJarFile = "!/";

    /**
     * Tittle for the Projects tab
     */
    public static final String projectTabTitle = "Projects";

    /**
     * Tittle for the Mutation tab
     */
    public static final String mutationTabTitle = "Mutation Config.";

    /**
     * Projects path
     */
    public final static String projectsFolder = System.getProperty("user.dir") + File.separatorChar + "Projects";

    /**
     * Message that shows how to build a correct name
     */
    public final static String nameRulesMessage = "A name must start with a letter and can contain alphanumeric characters, '-' and '_'";

    /**
     * Default file to store the tests
     */
    public final static String defaultTestFile = "testsFile.txt";

    /**
     * Default file to store the test configuration
     */
    public final static String defaultTestConfigFile = "testsConfig.cfg";

    /**
     * Default folder to store the mutants
     */
    public final static String defaultMutantsFolder = "mutants";

    /**
     * Test configuration comment
     */
    public final static String testConfigComment = "### Test configuration file.\n"
            + "### Type the execution test line, just after the comments, using the required flags.\n"
            + "###\n"
            + "### Usage: program -flag_1 [fileFlags_1]] - flag_2 [[fileFlags_2]] ... -flag_n [[fileFlags_n]]\n"
            + "###   where each fileFlag_x contains all the possible values (one per line) for the flag parameter\n"
            + "###   paths are relative to the current project folder.\n"
            + "###\n"
            + "### Example: filterImage -filter [[fileWithFilters.txt]] -inputImage [[fileWithInputImages]]\n";

    /**
     * Test configuration comment
     */
    public final static String initComment = "Welcome to the Mutomvo tool!.\n\n"
            + "Right-click on the elements of the tree to load a project\n"
            + "or double-ckick on the elements to show its description.\n\n"
            + "   Enjoy :)\n\n\n"
            + " Pablo & Alberto";

    /**
     * List of mutant operators
     */
    public static final String[][] operatorList = {{"AORb", "Replace basic binary arithmetic operators with other binary arithmetic operators."},
    {"AORs", "Replace short-cut arithmetic operators with other unary arithmetic operators."},
    {"AORu", "Replace basic unary arithmetic operators with other unary arithmetic operators."},
    {"AOIu", "Insert basic unary arithmetic operators."},
    {"AOIs", "Insert short-cut arithmetic operators."},
    {"AODu", "Delete basic unary arithmetic operators."},
    {"AODs", "Delete short-cut arithmetic operators."},
    {"COR", "Conditional Operator Replacement."},
    {"COI", "Insert unary conditional operators."},
    {"COD", "Conditional Operator Deletion."},
    {"SfOR", "Shift Operator Replacement."},
    {"LOR", "Logical Operator Replacement."},
    {"LOI", "Logical Operator Insertion."},
    {"LOD", " Logical Operator Insertion."},
    {"ROR", "Relational Operator Replacement."},
    {"ASR", "Assignment operator replacement."},};

public static final String[][] arithmeticList = {{"AORb", "Replace basic binary arithmetic operators with other binary arithmetic operators."},
    {"AORs", "Replace short-cut arithmetic operators with other unary arithmetic operators."},
    {"AORu", "Replace basic unary arithmetic operators with other unary arithmetic operators."},
    {"AOIu", "Insert basic unary arithmetic operators."},
    {"AOIs", "Insert short-cut arithmetic operators."},
    {"AODu", "Delete basic unary arithmetic operators."},
    {"AODs", "Delete short-cut arithmetic operators."}};

    public static final String[][] conditionalList = {{"COR", "Conditional Operator Replacement."},
    {"COI", "Insert unary conditional operators."},
    {"COD", "Conditional Operator Deletion."}
    };

    public static final String[][] logicList = {{"LOR", "Logical Operator Replacement."},
    {"LOI", "Logical Operator Insertion."},
    {"LOD", " Logical Operator Insertion."},};

    public static final String[][] relationalList = {{"ROR", "Relational Operator Replacement."}
    };

    /**
     * Column names for operators in the node
     */
    public static final String columnOperatorsInMutationTable[] = {"AOR", "AOD", "ROR", "COR", "COD", "SfOR", "LOR", "LOD", "ASR"};

    /**
     * Column names for operators in the node
     */
    public static final String columnDescriptionInMutationTable[] = {"Arithmetic operator replacement",
        "Arithmetic operator deletion",
        "Relational operator replacement",
        "Conditional operator replacement",
        "Conditional operator deletion",
        "Shift operator replacement",
        "Logical operator replacement",
        "Logical operator deletion",
        "Assigment operator deletion"};

    /**
     * Column names for applications in the node
     */
    public static final String columnNamesAppInGraphGen[] = {"Name", "Enabled"};

    /**
     * Column names for applications in the node
     */
    public static final String columnNamesAppInMutationTable[] = {"Operator", "Description", "Enabled?"};

    /**
     * Number of default rows in mutation table
     */
    public final static int defaultNumRowsOperatorsTable = 9;

    /**
     * Name of the file than contains the configuration of a scenario with tests
     */
    public final static String persistenceTestFile = "testsFile_";

    /**
     * Name of the folder that contains mutants
     */
    public final static String defaultMutantFolder = "mutants";

    /**
     * Suffix for mutants
     */
    public final static String defaultMutantSuffix = "_mutant_";

    /**
     * Suffix for mutants
     */
    public final static String defaultHMutator = "_H_";

    public static final String pathToGraphsClasses = "mutomvo/Mutation/ReportGenerator/graphs/graphics/";         
    public static final String pathToReportsClasses = "mutomvo/Mutation/ReportGenerator/reports/readableReports/";
    
    /**
     * Number of test to be shown in the preview area
     */
    public final static int defaultNumberPreviewTest = 100;
    
    public final static int DEFAULT_TIMEOUT_FACTOR = 5;
    /**
     * Checks that a given name is correct. This name only can contain [A-Z |
     * a-z | 0-9 | _ | -]
     *
     * @param name Name to be checked
     * @return True if the given name is correct or false in anotgher case
     */
    static public boolean checkName(String name) {

        boolean isOK;
        int i;

        // Init...
        isOK = true;
        i = 0;

        // Check the length of the name
        if (name.length() <= 0) {
            isOK = false;
        }

        // Name must start by a letter!
        if ((isOK) && (!Character.isLetter(name.charAt(0)))) {
            isOK = false;
        }

        // Check each charactrer
        while ((i < name.length()) && (isOK)) {

            if ((!Character.isDigit(name.charAt(i)))
                    && (!Character.isLetter(name.charAt(i)))
                    && (name.charAt(i) != '_')
                    && (name.charAt(i) != '-')) {

                isOK = false;
            } else {
                i++;
            }
        }

        return isOK;
    }

    /**
     * Shows an error message using a JDialog
     *
     * @param message Error message
     * @param title Title of the message
     */
    static public void showErrorMessage(String message, String title) {

        try {
            JOptionPane.showMessageDialog(new JFrame(),
                    message,
                    title,
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.printf("Exception showing error!");
        }
    }

    /**
     * Initializes a comboBox to show the element indicated in the second
     * parameter
     *
     * @param combo ComboBox to be initialized
     * @param defaultName Name of the element that will be set as selected in
     * the comboBox
     * @throws MutomvoException Thrown exception
     */
    static public void initCombo(JComboBox combo, String defaultName) throws MutomvoException {

        int i;
        boolean found;

        // Init...
        found = false;
        i = 0;

        // Search for the appname
        while ((!found) && (i < combo.getItemCount())) {

            if (combo.getItemAt(i).toString().equals(defaultName)) {
                found = true;
                combo.setSelectedIndex(i);
            } else {
                i++;
            }
        }

        // Element not found!
        if (!found) {
            throw new MutomvoException("Element not found in combo: " + defaultName);
        }
    }

    /**
     * Load the elements from the list into a comboBox
     *
     * @param list List that contains the elements to be included in the
     * comboBox
     * @param combo ComboBox that will allocate the elements from the list
     */
    static public void loadCombo(LinkedList<String> list, JComboBox combo) {

        int currentComponent;

        // Reset combo
        combo.removeAllItems();

        // Adds every component in the list to the combo
        for (currentComponent = 0; currentComponent < list.size(); currentComponent++) {
            combo.addItem(list.get(currentComponent));
        }
    }

    /**
     * Load the elements from the list into a comboBox
     *
     * @param list List that contains the elements to be included in the
     * comboBox
     * @param combo ComboBox that will allocate the elements from the list
     */
    static public void loadCombo(String[] list, JComboBox combo) {

        int currentComponent;

        // Reset combo
        combo.removeAllItems();

        // Adds every component in the list to the combo
        for (currentComponent = 0; currentComponent < list.length; currentComponent++) {
            combo.addItem(list[currentComponent]);
        }
    }

    /**
     * Initializes an application table including the corresponding comboBox
     * (used in nodePanel)
     *
     * @param table Applications table
     */
    static public void initDataInMutationTable(JTable table) {

        int currentRow;
        JCheckBox checkBox;
        MutationTableModel model;
        TableColumnModel columnModel;
        TableColumn muColumn;
        DefaultTableCellRenderer renderer;

        try {

            // Create a new table model
            model = new MutationTableModel();
            model.setColumnCount(columnNamesAppInMutationTable.length);
            model.setColumnIdentifiers(columnNamesAppInMutationTable);

            // Init values...
            for (currentRow = 0; currentRow < Utils.operatorList.length; currentRow++) {
                model.addRow(new Object[]{Utils.operatorList[currentRow][0],
                    Utils.operatorList[currentRow][1],
                    false});
            }

            // Set new model
            table.setModel(model);

            // Set a new column model
            columnModel = table.getColumnModel();
            columnModel.getColumn(0).setWidth(100);
            columnModel.getColumn(0).setMaxWidth(100);
            columnModel.getColumn(0).setMinWidth(100);
            columnModel.getColumn(0).setResizable(false);

            columnModel.getColumn(1).setWidth(550);
            columnModel.getColumn(1).setMaxWidth(550);
            columnModel.getColumn(1).setMinWidth(550);
            columnModel.getColumn(1).setResizable(false);

            columnModel.getColumn(2).setWidth(80);
            columnModel.getColumn(2).setMaxWidth(80);
            columnModel.getColumn(2).setMinWidth(80);
            columnModel.getColumn(2).setResizable(false);

            // Update table
            table.setShowGrid(true);
            table.updateUI();
        } catch (Exception e) {
            Utils.showErrorMessage(e.getMessage(), "[Utils] Error while loading mutation operators in table");
        }
    }

    /**
     * Mask used to filter files
     *
     * @param mask Mask
     * @return Processed file
     */
    static public String getRegex(String mask) {

        mask = mask.replace(".", "\\.");
        mask = mask.replace("*", ".*");
        mask = mask.replace("?", ".");
        return mask;
    }

    /**
     * Get the list of components allocated in path
     *
     * @param path Path containing components
     * @return List of components allocated in path
     * @throws MutomvoException Thrown exception
     */
    static public LinkedList<String> getComponentListFromDir(String path) throws MutomvoException {

        LinkedList<String> list;
        File initialDir;
        File[] files;

        // Create an empty list
        list = new LinkedList<String>();

        try {
            // Get folder that contains the components
            initialDir = new File(path);

            // For each CPU in the repo
            if (initialDir.isDirectory()) {

                // Get list components
                files = initialDir.listFiles();

                // For each file, read the cpu and update the tree!
                for (File file : files) {
                    // Generate absolute path
                    if (!file.getName().startsWith(".")) {
                        list.add(file.getName());
                    }
                }
            } // Path is not a folder
            else {
                throw new MutomvoException("Entered path is not a folder:" + path);
            }

        } catch (MutomvoException e) {
            throw new MutomvoException(e.getMessage());
        } catch (Exception e) {
            Utils.showErrorMessage(e.getMessage(), "[Utils] Error loading components from folder!");
        }

        return list;
    }

    /**
     * Updates a specific node in the tree
     *
     * @param path Path containing components to be updated in the tree
     * @param root Node to be updated in the tree
     * @throws MutomvoException Thrown exception
     */
    static public void updateTreeNode(String path, DefaultMutableTreeNode root) throws MutomvoException {

        File initialDir;
        File[] files;
        DefaultMutableTreeNode node;

        try {
            // Folder
            initialDir = new File(path);

            // Remove all elements from node
            root.removeAllChildren();

            // For each CPU in the repo
            if (initialDir.isDirectory()) {

                // Get list of CPU files
                files = initialDir.listFiles();

                // For each file, read the cpu and update the tree!
                for (File file : files) {

                    // Insert current node to the tree
                    if (!file.getName().startsWith(".")) {
                        node = new DefaultMutableTreeNode(file.getName());
                        root.add(node);
                    }
                }
            } // Path is not a folder
            else {
                throw new MutomvoException("Entered path is not a folder:" + path);
            }

        } catch (MutomvoException e) {
            throw new MutomvoException(e.getMessage());
        } catch (Exception e) {
            Utils.showErrorMessage(e.getMessage(), "[Utils] Error while updating a tree node!");
        }
    }

    /**
     * Removes a component from path
     *
     * @param path Path that allocates the component to be removed
     * @param componentName Component to be removed
     * @return True if the component has been removed and False in other case
     * @throws MutomvoException Thrown exception
     */
    static public boolean removeComponent(String path, String componentName) throws MutomvoException {

        File componentToDelete;
        int response;
        boolean componentDeleted;

        // Init...
        componentDeleted = false;

        // Get the absolute path
        componentToDelete = new File(path + File.separatorChar + componentName);

        // Does selected component exists?
        if (componentToDelete.exists()) {

            // Ask for removing current file
            response = JOptionPane.showConfirmDialog(null,
                    "Are you sure to remove " + componentName + "?",
                    "Confirm to remove",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // If ok, then overwrite
            if (response == JOptionPane.OK_OPTION) {
                componentToDelete.delete();
                componentDeleted = true;
            }
        } else {
            throw new MutomvoException("Component not found: " + componentToDelete.getAbsolutePath());
        }

        return componentDeleted;
    }

    /**
     * Gets the index of the panel given its tab-name
     *
     * @param pane Panel
     * @param name Name of the panel
     * @return Index of the panel
     * @throws MutomvoException Thrown exception
     */
    static public int getTabIndexFromName(JTabbedPane pane, String name) throws MutomvoException {

        int index;
        int i;
        boolean found;

        // Init
        index = -1;
        i = 0;
        found = false;

        while ((i < pane.getTabCount()) && (!found)) {

            if (pane.getTitleAt(i).equals(name)) {
                index = i;
                found = true;
            } else {
                i++;
            }
        }

        // Not found!
        if (!found) {
            throw new MutomvoException("Tab not found: " + name);
        }

        return index;
    }

    /**
     * Loads a Mutation configuration obtect given its name
     *
     * @param configName Name of the configuration
     * @return Loaded configuration
     * @throws MutomvoException Thrown exception
     */
    static public MutationProject loadMutationProject(String configName) throws MutomvoException {

        File currentConfig;
        InputStream file;
        InputStream buffer;
        ObjectInput input;
        MutationProject loadedConfig;

        // Init...
        loadedConfig = null;

        try {

            // Get the absolute path
            currentConfig = new File(projectsFolder + File.separatorChar + configName);

            // Exist selected configuration?
            if (currentConfig.exists()) {

                // Read object from file
                file = new FileInputStream(currentConfig);
                buffer = new BufferedInputStream(file);
                input = new ObjectInputStream(buffer);

                // Deserialize object!
                loadedConfig = (MutationProject) input.readObject();
                file.close();
            } else {
                throw new MutomvoException("Requested project does not exists:" + configName);
            }

        } catch (MutomvoException e) {
            throw new MutomvoException(e.getMessage());
        } catch (IOException e) {
            Utils.showErrorMessage(e.getMessage(), "[Utils] I/O Error while loading a mutation configuration");
        } catch (ClassNotFoundException e) {
            Utils.showErrorMessage(e.getMessage(), "[Utils] Class not found. Error loading a mutation configuration");
        }

        return loadedConfig;
    }

    /**
     * Copy a file from in to out.
     *
     * @param in Input file
     * @param out Output file
     * @throws java.io.IOException
     */
    public static void copyFile(File in, File out)
            throws IOException {

        FileChannel inChannel;
        FileChannel outChannel;

        // Init...
        inChannel = new FileInputStream(in).getChannel();
        outChannel = new FileOutputStream(out).getChannel();

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * Deletes a folder recursively
     *
     * @param folder Folder to be deleted
     */
    public static void deleteFolder(File folder) {

        File[] files;

        // Get the folder list                
        files = folder.listFiles();

        if (files != null) {
            for (File f : files) {

                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static String readFileToString(String path)
            throws IOException {

        String line, text;
        BufferedReader br;
        StringBuilder sb;

        //byte[] encoded = Files.readAllBytes(Paths.get(path));
        // Init                    
        br = new BufferedReader(new FileReader(path));
        sb = new StringBuilder();
        text = "";

        // Read from file
        line = br.readLine();

        // Until the EOF!
        while (line != null) {
            text += line + "\n";
            line = br.readLine();
        }

        return text;
    }
    /**
     * Check if a given string corresponds to a existing file.
     * @param testSuitePath
     * @return 
     */
    public static boolean existsFile(String testSuitePath) {
    
        boolean bRet;
        File fileTemp;
        
        bRet = false;
        
        if(testSuitePath != null && !testSuitePath.isEmpty())
        {
            fileTemp = new File(testSuitePath);
            
            bRet = fileTemp.exists();
        }
        
        return bRet;
    }

    /**
     * Read classes for generating tests and load them into a linked list.
     *
     * @param pathToClass
     * @return List of classes for generating tests
     */
    public LinkedList<String> loadFromJar(String pathToClass) {

        LinkedList methodList;
        Enumeration enumuration;
        JarFile jarFile;
        JarEntry entry;
        File file;
        String completePath, testGeneratorClass;
        int i;

        // Init...
        methodList = new LinkedList<String>();

        try {

            // Build .jar path
            file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

            // simcanGUI has been executed from .jar file
            if ((file.exists()) && (file.isFile())) {

                jarFile = new JarFile(file);

                // Get entries of jar file
                enumuration = jarFile.entries();

                // Process each entry
                while (enumuration.hasMoreElements()) {

                    // Cet current entry
                    entry = (JarEntry) enumuration.nextElement();
                    completePath = entry.getName();

                    // Is current entry a test generator class?
                    if ((completePath.startsWith(pathToClass)) && (completePath.endsWith(".class"))) {

                        testGeneratorClass = completePath.substring(completePath.lastIndexOf("/") + 1, completePath.indexOf("."));
                        methodList.add(testGeneratorClass);
                        //System.out.println ("-.-.-.-.->" + testGeneratorClass);                    
                    }
                }
            } // simcanGUI has been executed from NetBeans
            else {

                // Generate path to the folder that contains .class files
                file = new File(file.getAbsolutePath() + File.separatorChar + pathToClass);

                // This folder contains the test generator classes
                if ((file.exists()) && (file.isDirectory())) {

                    // 
                    methodList = Utils.getComponentListFromDir(file.getAbsolutePath());

                    // Remove .class from each entry
                    for (i = 0; i < methodList.size(); i++) {
                        testGeneratorClass = methodList.get(i).toString();
                        if (testGeneratorClass.lastIndexOf(".") != -1) {
                            testGeneratorClass = testGeneratorClass.substring(0, testGeneratorClass.lastIndexOf("."));
                            methodList.set(i, testGeneratorClass);
                        }
                    }
                } // Folder not found!
                else {
                    throw new MutomvoException("Folder containing test generator classes not found: " + file.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            Utils.showErrorMessage(e.getMessage(), "[MutationPanel] Error while reading test generator classes");
        }

        return methodList;
    }

    public static LinkedList<String> loadTestSuiteFromFile(String strTsPath, boolean bMaxTests, int nMaxTests)
    {
        LinkedList<String> testSuiteList;
        BufferedReader bufReader;
        int nIndexTest;
        
        testSuiteList = null;
        nIndexTest = 0;
        
        if(existsFile(strTsPath))
        {
            testSuiteList = new LinkedList<String>();
            
            try {
                bufReader = new BufferedReader(new FileReader(strTsPath));
                
                String line = bufReader.readLine();
                while (line != null && (!bMaxTests || nIndexTest < nMaxTests)) {
                    testSuiteList.add(line); 
                    line = bufReader.readLine();
                    nIndexTest++;
                }
                bufReader.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return testSuiteList;
    }
}
