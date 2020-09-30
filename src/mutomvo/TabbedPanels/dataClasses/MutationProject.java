package mutomvo.TabbedPanels.dataClasses;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import mutomvo.Utils.Utils;

/**
 * Class that represent a project.
 *
 * @author cana
 */
public class MutationProject implements java.io.Serializable {

    // General information
    private String projectName;
    private String appName;
    private String projectFolder;
    private String sourceProgramPath;
    private String comment;

    // Operators
    private LinkedList<MutantOperator> operatorList;
    private boolean selectedArithmetic;
    private boolean selectedRelational;
    private boolean selectedShifting;
    private boolean selectedLogical;
    private boolean selectedConditional;

    // Mutants    
    private boolean useFixedMutants;
    private int numFixedMutants;
    private int numGeneratedMutants;

    // Tests
    private boolean useFixedTests;
    private int numFixedTests;
    private int numGeneratedTests;
    private String testConfigLine;

    private LinkedList<LinkedList<String>> filteredLinesList;

    public MutationProject() {
        projectName = sourceProgramPath = projectFolder = comment = "";
        testConfigLine = Utils.testConfigComment;
        operatorList = new LinkedList<MutantOperator>();
        numGeneratedMutants = numGeneratedTests = numFixedTests = numFixedMutants = 0;
        useFixedMutants = useFixedTests = false;
        selectedArithmetic = false;
        selectedRelational = false;
        selectedShifting = false;
        selectedLogical = false;
        selectedConditional = false;
        filteredLinesList = new LinkedList<LinkedList<String>>();
        appName = new String();
    }

    public String getAppName() {
        int nIndex;

        nIndex = 0;
        if (appName.isEmpty()) {
            nIndex = sourceProgramPath.lastIndexOf(File.separatorChar);

            if (nIndex != -1) {
                appName = sourceProgramPath.substring(nIndex + 1);
            }
        }
        return appName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String configName) {
        this.projectName = configName;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public String getSourceProgramPath() {
        return sourceProgramPath;
    }

    public void setSourceProgramPath(String application) {
        this.sourceProgramPath = application;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean useFixedMutants() {
        return useFixedMutants;
    }

    public void setUseFixedMutants(boolean useFixedMutants) {
        this.useFixedMutants = useFixedMutants;
    }

    public int getNumGeneratedMutants() {
        return numGeneratedMutants;
    }

    public void setNumGeneratedMutants(int numMutants) {
        this.numGeneratedMutants = numMutants;
    }

    public LinkedList<MutantOperator> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(LinkedList<MutantOperator> operatorList) {
        this.operatorList = operatorList;
    }

    public int getNumOperators() {
        return operatorList.size();
    }

    public MutantOperator getOperatorByIndex(int index) {
        return operatorList.get(index);
    }

    public void addOperator(MutantOperator operator) {
        operatorList.add(operator);
    }

    public int getNumFixedTests() {
        return numFixedTests;
    }

    public void setNumFixedTests(int numFixedTests) {
        this.numFixedTests = numFixedTests;
    }

    public void setNumFixedMutants(int numFixedMutants) {
        this.numFixedMutants = numFixedMutants;
    }

    public int getNumFixedMutants() {
        return this.numFixedMutants;
    }

    public boolean isSelectedArithmetic() {
        return selectedArithmetic;
    }

    public void setSelectedArithmetic(boolean selectedArithmetic) {
        this.selectedArithmetic = selectedArithmetic;
    }

    public boolean isSelectedRelational() {
        return selectedRelational;
    }

    public void setSelectedRelational(boolean selectedRelational) {
        this.selectedRelational = selectedRelational;
    }

    public boolean isSelectedShifting() {
        return selectedShifting;
    }

    public void setSelectedShifting(boolean selectedShifting) {
        this.selectedShifting = selectedShifting;
    }

    public boolean isSelectedLogical() {
        return selectedLogical;
    }

    public void setSelectedLogical(boolean selectedLogical) {
        this.selectedLogical = selectedLogical;
    }

    public boolean isSelectedConditional() {
        return selectedConditional;
    }

    public void setSelectedConditional(boolean selectedConditional) {
        this.selectedConditional = selectedConditional;
    }

    public boolean useFixedTests() {
        return useFixedTests;
    }

    public void setUseFixedTests(boolean useFixedTests) {
        this.useFixedTests = useFixedTests;
    }

    public int getNumGeneratedTests() {
        return numGeneratedTests;
    }

    public void setNumGeneratedTests(int numGeneratedTests) {
        this.numGeneratedTests = numGeneratedTests;
    }

    public String getTestConfigLine() {
        return testConfigLine;
    }

    public void setTestConfigLine(String testConfigLine) {
        this.testConfigLine = testConfigLine;
    }

    public LinkedList<LinkedList<String>> getFilteredLinesList() {
        return filteredLinesList;
    }

    public void setFilteredLinesList(LinkedList<LinkedList<String>> filteredLinesList) {
        this.filteredLinesList = filteredLinesList;
    }

    /*
    public String getMutantsPath() {
       return sourceProgramPath + File.separator+Utils.defaultMutantsFolder+File.separator;
    }*/
}
