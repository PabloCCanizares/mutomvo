/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.TabbedPanels.dataClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.TabbedPanels.MutationPanel;
import mutomvo.TabbedPanels.TestGeneratorPanel;
import mutomvo.Utils.Utils;

/**
 *
 * @author cana
 */
public class CommandLineOps {

    public CommandLineOps() {
    }

    /**
     * Gets the complete lists of existing projects
     */
    public String getExistingProjects() throws MutomvoException {

        String currentConfigText;
        StringBuffer text;
        MutationProject currentConfig;
        LinkedList<String> list;

        int i;

        // Init...
        text = new StringBuffer("");
        text.append("Showing current projects:\n");

        // Get the list of configurations
        list = Utils.getComponentListFromDir(Utils.projectsFolder);

        // For each config
        for (i = 0; i < list.size(); i++) {

            // Load current configuration
            currentConfig = Utils.loadMutationProject(list.get(i));
            currentConfigText = "  - " + currentConfig.getProjectName() + ": " + currentConfig.getComment() + "\n";
            text.append(currentConfigText);
        }

        if (list.size() == 0) {
            text.append("There are no existing projects... :(\n");
        }

        return text.toString();
    }

    /**
     * Gets a detailed description of a given project
     *
     * @param configName Configuration
     */
    public String getProjectConfig(String configName) throws MutomvoException {

        String text;
        MutationProject project;
        int i;

        text = "";

        // Load current configuration
        project = Utils.loadMutationProject(configName);

        if (project == null) {
            text = "Project " + configName + " not found!\n";
        } else {

            // Init...            
            text = "Showing configuration of the project " + configName + "\n";

            // Application to mutate
            text += "   - Source program:" + project.getSourceProgramPath() + "\n";

            // Mutants folder
            text += "   - Project folder" + project.getProjectFolder() + "\n";

            // Fixed mutants
            if (project.useFixedMutants()) {
                text += "   - # Fixed mutants:" + Integer.toString(project.getNumFixedMutants()) + "\n";
            } else {
                text += "   - # Generated mutants (not fixed):" + Integer.toString(project.getNumGeneratedMutants()) + "\n";
            }

            // Mutant operators
            text += "   - Selected operators:\n";

            for (i = 0; i < project.getNumOperators(); i++) {

                if (project.getOperatorByIndex(i).isIsSelected()) {
                    text += "     -> " + project.getOperatorByIndex(i).getAcronym() + "\n";
                }
            }

            // Test information
            text += "   - Test configuration:\n";

            if (project.useFixedTests()) {
                text += "      > # Fixed tests: " + Integer.toString(project.getNumFixedTests()) + "\n";
            } else {
                text += "      > # Generated tests: " + Integer.toString(project.getNumGeneratedTests()) + "\n";
            }

            text += "      > # Test file:\n" + project.getTestConfigLine() + "\n";
        }

        return text;
    }

    /**
     * Sets the folder path of a given project
     *
     * @param projectName
     * @return
     */
    public String setProjectFolderPath(String projectName, String path) throws MutomvoException, FileNotFoundException, IOException {

        String text;
        MutationProject project;
        String newProjectFile;
        FileOutputStream fout;
        ObjectOutputStream oos;
        int i;

        text = "Path set successfully!";

        // Load current configuration
        project = Utils.loadMutationProject(projectName);

        // Set the new path 
        project.setProjectFolder(path);

        // Generate new file's complete path
        newProjectFile = Utils.projectsFolder + File.separatorChar + project.getProjectName();

        fout = new FileOutputStream(newProjectFile);
        oos = new ObjectOutputStream(fout);
        oos.writeObject(project);
        oos.close();

        return text;
    }

    /**
     * Sets the path of the source program to be mutated
     *
     * @param projectName
     * @return
     */
    public String setSourceProgramPath(String projectName, String path) throws MutomvoException, FileNotFoundException, IOException {

        String text;
        MutationProject project;
        String newProjectFile;
        FileOutputStream fout;
        ObjectOutputStream oos;
        int i;

        text = "Path set successfully!";

        // Load current configuration
        project = Utils.loadMutationProject(projectName);

        // Set the new path 
        project.setSourceProgramPath(path);

        // Generate new file's complete path
        newProjectFile = Utils.projectsFolder + File.separatorChar + project.getProjectName();

        fout = new FileOutputStream(newProjectFile);
        oos = new ObjectOutputStream(fout);
        oos.writeObject(project);
        oos.close();

        return text;
    }

    /**
     * Generate mutants
     *
     * @param projectName
     * @return
     */
    public String generateMutants(String projectName) throws MutomvoException, FileNotFoundException, IOException {

        String text;
        MutationPanel mutation;

        mutation = new MutationPanel();
        text = mutation.generateMutantsFromCommandLine(projectName);

        return text;
    }

    /**
     * Generate tests
     *
     * @param projectName
     * @return
     */
    public String generateTests(String projectName) throws MutomvoException, FileNotFoundException, IOException {
        TestGeneratorPanel testPanel;
        String text;
        int nRet;
        
        testPanel = new TestGeneratorPanel();

        nRet = testPanel.generateTestsFromCommandLine(projectName);
        text= String.format("Total number of tests generated: %d\n", nRet);
        
        return text;
    }    
}
