package mutomvo.TabbedPanels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import mutomvo.Exceptions.MutomvoException;
import mutomvo.TabbedPanels.dataClasses.MutationProject;
import mutomvo.Utils.Utils;

/**
 * Dialog that configures the test cases.
 *
 * @author cana
 */
public class TestGeneratorPanel extends javax.swing.JDialog {

    MutationProject currentProject;
    ArrayList<LinkedList<String>> tests;

    /**
     * Creates a Dialog to configure tests
     *
     * @param parent
     * @param project
     */
    public TestGeneratorPanel(java.awt.Frame parent) {

        super(parent, true);
        initComponents();
        this.setVisible(false);
        textNumFixedTests.setEnabled(false);
        checkBoxFixTests.setSelected(false);
    }

    /**
     * Creates a Dialog to configure tests
     *
     * @param parent
     */
    public TestGeneratorPanel() {

        initComponents();
        this.setVisible(false);
        textNumFixedTests.setEnabled(false);
        checkBoxFixTests.setSelected(false);
    }
    
    /**
     * Shows this dialog and updates the parameters of the current project
     *
     * @param project
     */
    public void showTestConfiguration(MutationProject project) {

        // Set current project
        currentProject = project;

        // Sets the configuration into the panel
        checkBoxFixTests.setSelected(currentProject.useFixedTests());

        if (currentProject.useFixedTests()) {
            textNumFixedTests.setText(Integer.toString(project.getNumFixedTests()));
        }

        // Sets test config
        textTestLine.setText(project.getTestConfigLine());

        this.setVisible(true);
    }

    private void updateTestConfiguration() {

        try {

            // Set fixedTest flag
            currentProject.setUseFixedTests(checkBoxFixTests.isSelected());

            // Set number of fixed tests
            if (checkBoxFixTests.isSelected()) {
                currentProject.setNumFixedTests(Integer.parseInt(textNumFixedTests.getText()));
            }

            // Set test line
            currentProject.setTestConfigLine(textTestLine.getText());
        } catch (Exception e) {

            Utils.showErrorMessage("Wrong parameter for number of fixed tests.\n"
                    + "This parameter must be an integer.\n",
                    "Wrong parameter!");
        }
    }

    /**
     * Parses the configuration line into a list of String containing values.
     *
     * Each element of the list contains the name of the file with the values of
     * a parameter.
     *
     * @param line
     * @return
     */
    private LinkedList<String> parseFilesToStringList(String line) {

        StringTokenizer st;
        LinkedList<String> list;
        String token;

        // Init...
        list = new LinkedList<String>();
        st = new StringTokenizer(line);

        while (st.hasMoreElements()) {

            token = (String) st.nextElement();

            if (token.startsWith("[[")) {
                list.add(token.substring(2, token.length() - 2));
            }
        }
        return list;
    }

    /**
     * Parses the configuration line into a list of String containing tags.
     *
     * Each element of the list contains the name of the file with the values of
     * a parameter.
     *
     * @param line
     * @return
     */
    private LinkedList<String> parseFileTagsToStringList(String line) {

        StringTokenizer st;
        LinkedList<String> list;
        String token;

        // Init...
        list = new LinkedList<String>();
        st = new StringTokenizer(line);

        while (st.hasMoreElements()) {

            token = (String) st.nextElement();

            if (token.startsWith("[[")) {
                list.add(token);
            }
        }
        return list;
    }

    /**
     * Update indexes for the list of parameters.
     *
     * @param indexes
     * @param maxLength
     * @return
     */
    private boolean updateIndexes(ArrayList<Integer> indexes, ArrayList<Integer> maxLength) {

        boolean finish, propagate;
        int index;

        // Init...
        finish = propagate = true;
        index = 0;

        // While index is in the array
        while (index < indexes.size() && propagate) {

            // Update current index
            indexes.set(index, indexes.get(index) + 1);

            if (indexes.get(index) >= maxLength.get(index)) {
                indexes.set(index, 0);
                index++;
            } else {
                propagate = false;
            }
        }

        // Check if all the combinations has been completed
        index = 0;
        while (index < indexes.size() && finish) {

            if (indexes.get(index) != 0) {
                finish = false;
            } else {
                index++;
            }
        }

        return finish;
    }

    /**
     * Generates all the tests using the configuration file
     *
     * @param line
     */
    private void generateTests(String line) {

        LinkedList<String> filesList;
        LinkedList<String> tagsList;
        LinkedList<String> valuesList;
        ArrayList<Integer> indexes;
        ArrayList<Integer> maxLength;
        StringBuilder sb;
        String currentTest;
        BufferedReader br;
        String a, b, aux, currentLine;
        Writer writer;
        File file;
        int i;
        boolean end;

        // Parses the configuration file
        filesList = parseFilesToStringList(line);
        tagsList = parseFileTagsToStringList(line);
        indexes = new ArrayList<Integer>(filesList.size());
        maxLength = new ArrayList<Integer>(filesList.size());
        end = false;
        currentTest = "";

        // Create the list of lists
        tests = new ArrayList<LinkedList<String>>(filesList.size());

        try {

            // Parse all the parameters in the tests list of lists
            for (i = 0; i < filesList.size(); i++) {

                // Current configuration file
                file = new File(currentProject.getProjectFolder() + File.separatorChar + filesList.get(i));
                br = new BufferedReader(new FileReader(file));
                currentLine = br.readLine();

                // Create inner list
                valuesList = new LinkedList<String>();

                // Read each line
                while (currentLine != null) {
                    valuesList.add(currentLine);
                    currentLine = br.readLine();
                }

                br.close();
                tests.add(i, valuesList);
            }

            // Creates a String Builder
            sb = new StringBuilder("");

            // Init indexes
            for (i = 0; i < tests.size(); i++) {
                indexes.add(i, 0);
            }

            for (i = 0; i < tests.size(); i++) {
                maxLength.add(i, tests.get(i).size());
            }

            // Generate tests...
            do {
                aux = line;

                for (i = 0; i < tagsList.size(); i++) {
                    a = tagsList.get(i);
                    b = tests.get(i).get(indexes.get(i));
                    currentTest = aux.replaceFirst(Pattern.quote(a), b);
                    aux = currentTest;
                }

                // Append current test
                sb.append(currentTest + "\n");
                end = updateIndexes(indexes, maxLength);

            } while (!end);

            // Write tests in the test file
            file = new File(currentProject.getProjectFolder() + File.separatorChar + Utils.defaultTestFile);

            // If the test file does not exists...
            if (!file.exists()) {
                Utils.showErrorMessage("Error while generating test.\n"
                        + "File " + Utils.defaultTestFile + " is not in the project folder",
                        "File not found");
            } else {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writer.write(sb.toString());
                writer.flush();
                writer.close();
            }

        } catch (Exception e) {
            Utils.showErrorMessage(e.getLocalizedMessage(), "Error while generating tests");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        checkBoxFixTests = new javax.swing.JCheckBox();
        textNumFixedTests = new javax.swing.JTextField();
        labelNumGeneratedTests = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textTestLine = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        textPreview = new javax.swing.JTextArea();
        labelOutputPreview = new javax.swing.JLabel();
        labelGenratedTest = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        buttonSetTestConfig = new javax.swing.JButton();
        buttonGenerateTests = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(750, 500));
        setName("Form"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mutomvo.MutomvoGUIApp.class).getContext().getResourceMap(TestGeneratorPanel.class);
        labelTitle.setFont(resourceMap.getFont("labelTitle.font")); // NOI18N
        labelTitle.setText(resourceMap.getString("labelTitle.text")); // NOI18N
        labelTitle.setName("labelTitle"); // NOI18N
        getContentPane().add(labelTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        checkBoxFixTests.setText(resourceMap.getString("checkBoxFixTests.text")); // NOI18N
        checkBoxFixTests.setName("checkBoxFixTests"); // NOI18N
        checkBoxFixTests.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxFixTestsItemStateChanged(evt);
            }
        });
        getContentPane().add(checkBoxFixTests, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        textNumFixedTests.setText(resourceMap.getString("textNumFixedTests.text")); // NOI18N
        textNumFixedTests.setName("textNumFixedTests"); // NOI18N
        getContentPane().add(textNumFixedTests, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 60, -1));

        labelNumGeneratedTests.setText(resourceMap.getString("labelNumGeneratedTests.text")); // NOI18N
        labelNumGeneratedTests.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelNumGeneratedTests.setName("labelNumGeneratedTests"); // NOI18N
        getContentPane().add(labelNumGeneratedTests, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, 80, 20));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        textTestLine.setColumns(20);
        textTestLine.setRows(5);
        textTestLine.setName("textTestLine"); // NOI18N
        jScrollPane2.setViewportView(textTestLine);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 630, 160));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        textPreview.setEditable(false);
        textPreview.setBackground(resourceMap.getColor("textPreview.background")); // NOI18N
        textPreview.setColumns(20);
        textPreview.setForeground(resourceMap.getColor("textPreview.foreground")); // NOI18N
        textPreview.setRows(5);
        textPreview.setName("textPreview"); // NOI18N
        jScrollPane3.setViewportView(textPreview);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 630, 160));

        labelOutputPreview.setText(resourceMap.getString("labelOutputPreview.text")); // NOI18N
        labelOutputPreview.setName("labelOutputPreview"); // NOI18N
        getContentPane().add(labelOutputPreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        labelGenratedTest.setText(resourceMap.getString("labelGenratedTest.text")); // NOI18N
        labelGenratedTest.setName("labelGenratedTest"); // NOI18N
        getContentPane().add(labelGenratedTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 310, -1, -1));

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        buttonSetTestConfig.setText(resourceMap.getString("buttonSetTestConfig.text")); // NOI18N
        buttonSetTestConfig.setName("buttonSetTestConfig"); // NOI18N
        buttonSetTestConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSetTestConfigActionPerformed(evt);
            }
        });
        getContentPane().add(buttonSetTestConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 520, -1, -1));

        buttonGenerateTests.setText(resourceMap.getString("buttonGenerateTests.text")); // NOI18N
        buttonGenerateTests.setName("buttonGenerateTests"); // NOI18N
        buttonGenerateTests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGenerateTestsActionPerformed(evt);
            }
        });
        getContentPane().add(buttonGenerateTests, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxFixTestsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxFixTestsItemStateChanged

        if (checkBoxFixTests.isSelected()) {
            textNumFixedTests.setEnabled(true);
        } else {
            textNumFixedTests.setEnabled(false);
        }
    }//GEN-LAST:event_checkBoxFixTestsItemStateChanged

    private void buttonSetTestConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSetTestConfigActionPerformed

        this.updateTestConfiguration();

        // Hide this panel
        this.setVisible(false);
    }//GEN-LAST:event_buttonSetTestConfigActionPerformed

    private void buttonGenerateTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGenerateTestsActionPerformed

        generateTests(true);
    }//GEN-LAST:event_buttonGenerateTestsActionPerformed

    private int generateTests(boolean bGUI)
    {
        File file;
        BufferedReader br;
        String line;
        StringBuilder sb;
        boolean found;
        int totalTests, i, nRet;
        Writer writer;

        // Update current information
        if(bGUI)
            this.updateTestConfiguration();
        totalTests = nRet = 0;

        // Init
        found = false;
        file = new File(currentProject.getProjectFolder() + File.separatorChar + Utils.defaultTestConfigFile);

        try {

            // Test configuration file does not exists
            if (!file.exists()) {
                Utils.showErrorMessage("File " + Utils.defaultTestConfigFile + " not found in the project folder", "File not found!");
            } // File exists
            else {

                // Update the current test file config                    
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writer.write(currentProject.getTestConfigLine());
                writer.close();

                br = new BufferedReader(new FileReader(file));
                line = br.readLine();

                // Read the first uncommented line
                while ((line != null) && (!found)) {
                    if (!line.startsWith("###")) {
                        found = true;
                    } else {
                        line = br.readLine();
                    }
                }

                // Configuration line found
                if (found) {
                    generateTests(line);
                } else {
                    Utils.showErrorMessage("Text configuration line not found.", "Wrong config.");
                }

                // Calculating the number of tests...
                totalTests = 1;
                for (i = 0; i < tests.size(); i++) {
                    totalTests *= tests.get(i).size();
                }

                labelNumGeneratedTests.setText(Integer.toString(totalTests));

                // Preview the tests
                file = new File(currentProject.getProjectFolder() + File.separatorChar + Utils.defaultTestFile);
                br = new BufferedReader(new FileReader(file));
                line = br.readLine();
                sb = new StringBuilder("");
                i = 0;

                // Read the first uncommented line
                while ((line != null) && (i < Utils.defaultNumberPreviewTest)) {
                    sb.append(line + "\n");
                    line = br.readLine();
                    i++;
                }

                textPreview.setText(sb.toString());
                br.close();
                nRet = totalTests;
            }
        } catch (Exception e) {
            Utils.showErrorMessage(e.getLocalizedMessage(), "Error while reading " + file.getName());
        }    
        
        return nRet;
    }
    public int generateTestsFromCommandLine(String projectName) throws MutomvoException {
            
        int nRet;
        
        nRet = 0;
        // Load current configuration
        currentProject = Utils.loadMutationProject(projectName);
        
        if(currentProject!=null)
        {
            nRet = generateTests(false);
        }
        
        return nRet;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonGenerateTests;
    private javax.swing.JButton buttonSetTestConfig;
    private javax.swing.JCheckBox checkBoxFixTests;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelGenratedTest;
    private javax.swing.JLabel labelNumGeneratedTests;
    private javax.swing.JLabel labelOutputPreview;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextField textNumFixedTests;
    private javax.swing.JTextArea textPreview;
    private javax.swing.JTextArea textTestLine;
    // End of variables declaration//GEN-END:variables

}
