/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config;

import java.util.ArrayList;
import java.util.LinkedList;
import mutomvo.Mutation.Config.ApiParser.ApiManager;
import mutomvo.Mutation.Config.ApiParser.EnumApiCollection;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.MutatedOperator;
import mutomvo.TabbedPanels.dataClasses.MutantOperator;
import mutomvo.TabbedPanels.dataClasses.MutationProject;

/**
 *
 * @author Pablo C. Cañizares
 */
public class MutationCfg {

    ApiManager apiManager;
    ArrayList<Integer> badList = new ArrayList<Integer>();

    //Arithmetic
    private boolean GenerateAllArithmetic = false;
    private boolean GenerateAORb = false;
    private boolean GenerateAORs = false;
    private boolean GenerateAORu = false;
    private boolean GenerateAOIu = false;
    private boolean GenerateAOIs = false;
    private boolean GenerateAODu = false;
    private boolean GenerateAODs = false;

    //Relational
    private boolean GenerateROR = false;

    private boolean GenerateCOR = false;
    private boolean GenerateCOI = false;
    private boolean GenerateCOD = false;

    private boolean GenerateShift = false;
    private boolean GenerateAssignement = false;

    //Logical
    boolean GenerateLOR = false;
    boolean GenerateLOI = false;
    boolean GenerateLOD = false;

    //OMNET
    boolean GenerateOOR = false;
    boolean GenerateOOD = false;
    boolean GenerateOOMD = false;
    boolean GenerateOOMU = false;
    boolean GenerateOOS = false;

    //MPI
    boolean GenerateMOR = false;
    boolean GenerateMOD = false;
    boolean GenerateMOMD = false;
    boolean GenerateMOMU = false;
    boolean GenerateMOS = false;

    //SIMCAN
    boolean GenerateSOR = false;
    boolean GenerateSOD = false;
    boolean GenerateSOMD = false;
    boolean GenerateSOMU = false;
    boolean GenerateSOS = false;

    private boolean GenerateAllConditional;
    private boolean GenerateAllRelational;
    private boolean GenerateAllOMNET;
    private boolean GenerateAllLogical;

    private boolean debugMode = false;

    private static MutationCfg instance = null;

    public static MutationCfg getInstance() {
        if (instance == null) {
            instance = new MutationCfg();
        }
        return instance;
    }

    public void allIn() {
        GenerateAORb = GenerateAORs = GenerateAORu = GenerateAOIu = GenerateAOIs = true;
        GenerateAODu = GenerateAODs = GenerateCOR = GenerateCOI = GenerateCOD = GenerateShift = true;
        GenerateLOR = GenerateLOI = GenerateLOD = true;
        GenerateOOR = GenerateOOD = GenerateOOMD = GenerateOOMU = true;
        GenerateMOR = GenerateMOD = GenerateMOMD = GenerateMOMU = true;
        GenerateSOR = GenerateSOD = GenerateSOMD = GenerateSOMU = true;
        GenerateAssignement = true;
        GenerateROR = true;
    }

    public void setGenerateRelational(boolean GenerateRelational) {
        this.GenerateAllRelational = GenerateRelational;
    }

    public void setGenerateArithmetic(boolean GenerateArithmetic) {
        this.GenerateAllArithmetic = GenerateArithmetic;
    }

    public void setGenerateAssignement(boolean GenerateAssignement) {
        this.GenerateAssignement = GenerateAssignement;
    }

    public void setGenerateConditional(boolean GenerateConditional) {
        this.GenerateAllConditional = GenerateConditional;
    }

    public boolean isGenerateArithmetic() {
        return GenerateAORb || GenerateAORs || GenerateAORu || GenerateAOIu || GenerateAOIs
                || GenerateAODu || GenerateAODs;
    }

    public boolean isGenerateBinArithmetic() {
        return GenerateAORb;
    }

    public boolean isGenerateInsertArithmetic() {
        return GenerateAOIu || GenerateAOIs;
    }

    public boolean isGenerateAssignement() {
        return GenerateAssignement;
    }

    public boolean isGenerateConditional() {
        return GenerateCOR || GenerateCOI || GenerateCOD;
    }

    public boolean isGenerateRelational() {
        return GenerateROR;
    }

    public boolean isGenerateShift() {
        return GenerateShift;
    }

    public void setGenerateShift(boolean GenerateShift) {
        this.GenerateShift = GenerateShift;
    }

    public boolean isGenerateAODs() {
        return GenerateAODs;
    }

    public boolean isGenerateAODu() {
        return GenerateAODu;
    }

    public boolean isGenerateAOIs() {
        return GenerateAOIs;
    }

    public boolean isGenerateAOIu() {
        return GenerateAOIu;
    }

    public boolean isGenerateAORb() {
        return GenerateAORb;
    }

    public boolean isGenerateAORs() {
        return GenerateAORs;
    }

    public boolean isGenerateAORu() {
        return GenerateAORu;
    }

    public boolean isGenerateCOR() {
        return GenerateCOR;
    }

    public boolean isGenerateCOD() {
        return GenerateCOD;
    }

    public boolean isGenerateCOI() {
        return GenerateCOI;
    }

    public boolean isGenerateLOR() {
        return GenerateLOR;
    }

    public boolean isGenerateLOI() {
        return GenerateLOI;
    }

    public boolean isGenerateLOD() {
        return GenerateLOD;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void insertApiChain(String strClass, String strChain) {
        apiManager.insertCall(strClass, strChain);
    }

    public MutationCfg() {
        apiManager = new ApiManager();
    }

    public void setBadLine(int i) {
        badList.add(i);
    }

    public boolean isBadLine(int nLine) {
        boolean bRet = false;
        if (badList.size() > 0) {
            bRet = badList.contains(nLine);
        }

        return bRet;
    }

    public void setBadInterval(int nInit, int nEnd) {

        if (nInit < nEnd) {
            for (int i = nInit; i <= nEnd; i++) {
                setBadLine(i);
            }
        }
    }

    //TODO: Esto ya no va así. Esto lo lleva el ApiManager
    //Buscamos si alguno de las api encaja con la entrada
    public boolean isCallApi(String strName) {

        boolean bRet = false;
        String strApi;

        if (strName.indexOf("_cpu") != -1) {
            bRet = false;
        }

        bRet = apiManager.isCallApi(strName);

        return bRet;
    }

    public void insertOperatorsConfig(MutationProject opConfig) {
        MutantOperator opCfg;
        String strAcronym;
        int nMutants;
        if (opConfig != null) {
            nMutants = opConfig.getNumOperators();
            for (int i = 0; i < nMutants; i++) {
                opCfg = opConfig.getOperatorByIndex(i);

                strAcronym = opCfg.getAcronym();

                if (opCfg.isIsSelected()) {
                    if (strAcronym.equals("AORb")) {
                        GenerateAORb = true;
                    } else if (strAcronym.equals("AORs")) {
                        GenerateAORs = true;
                    } else if (strAcronym.equals("AORu")) {
                        GenerateAORu = true;
                    } else if (strAcronym.equals("AODu")) {
                        GenerateAODu = true;
                    } else if (strAcronym.equals("AODs")) {
                        GenerateAODs = true;
                    } else if (strAcronym.equals("AOIu")) {
                        GenerateAOIu = true;
                    } else if (strAcronym.equals("AOIs")) {
                        GenerateAOIs = true;
                    } else if (strAcronym.equals("COR")) {
                        GenerateCOR = true;
                    } else if (strAcronym.equals("COI")) {
                        GenerateCOI = true;
                    } else if (strAcronym.equals("COD")) {
                        GenerateCOD = true;
                    } else if (strAcronym.equals("LOR")) {
                        GenerateLOR = true;
                    } else if (strAcronym.equals("ROR")) {
                        GenerateROR = true;
                    } else if (strAcronym.equals("SfOR")) {
                        GenerateShift = true;
                    } else if (strAcronym.equals("ASR")) {
                        GenerateAssignement = true;
                    } else if (strAcronym.equals("LOI")) {
                        GenerateLOI = true;
                    } else if (strAcronym.equals("LOD")) {
                        GenerateLOD = true;
                    } else if (strAcronym.equals("OOR")) {
                        GenerateOOR = true;
                    } else if (strAcronym.equals("OOD")) {
                        GenerateOOD = true;
                    } else if (strAcronym.equals("OOMD")) {
                        GenerateOOMD = true;
                    } else if (strAcronym.equals("OOMU")) {
                        GenerateOOMU = true;
                    } else if (strAcronym.equals("OOSl")) {
                        GenerateOOS = true;
                    } else if (strAcronym.equals("MOR")) {
                        GenerateMOR = true;
                    } else if (strAcronym.equals("MOD")) {
                        GenerateMOD = true;
                    } else if (strAcronym.equals("MOMD")) {
                        GenerateMOMD = true;
                    } else if (strAcronym.equals("MOMU")) {
                        GenerateMOMU = true;
                    } else if (strAcronym.equals("MOSl")) {
                        GenerateMOS = true;
                    } else if (strAcronym.equals("SOR")) {
                        GenerateSOR = true;
                    } else if (strAcronym.equals("SOD")) {
                        GenerateSOD = true;
                    } else if (strAcronym.equals("SOMD")) {
                        GenerateSOMD = true;
                    } else if (strAcronym.equals("SOMU")) {
                        GenerateSOMD = true;
                    } else if (strAcronym.equals("SOSl")) {
                        GenerateSOS = true;
                    }
                }
            }
        }
        checkLoadApiCalls();
    }

    public void checkLoadApiCalls() {
        if (isGenerateOMNET()) {
            apiManager.LoadApiCollection(EnumApiCollection.eOMNET);
        }
        if (isGenerateSIMCAN()) {
            apiManager.LoadApiCollection(EnumApiCollection.eSIMCAN);
        }
        if (isGenerateMPI()) {
            apiManager.LoadApiCollection(EnumApiCollection.eMPI);
        }

    }

    public EnumOperatorClass getApiClass(String strName) {

        return apiManager.getApiClass(strName);
    }

    public void setGenerateOMNET(boolean b) {
        GenerateAllOMNET = true;
    }

    public boolean isGenerateOMNET() {
        return GenerateOOD || GenerateOOMD || GenerateOOMU || GenerateOOR || GenerateOOS;
    }

    public boolean isGenerateSIMCAN() {
        return GenerateSOD || GenerateSOMD || GenerateSOMU || GenerateSOR || GenerateSOS;
    }

    public boolean isGenerateMPI() {
        return GenerateMOD || GenerateMOMD || GenerateMOMU || GenerateMOR || GenerateMOS;
    }

    public boolean isGenerateLogical() {
        return GenerateLOD || GenerateLOR || GenerateLOI;
    }

    public boolean isGenerateAssignment() {
        return GenerateAssignement;
    }

    public boolean checkShuffableMethod(MutatedOperator Operator, String strCall, String strParameters) {
        return apiManager.checkShuffableMethod(Operator, strCall, strParameters);
    }

    public boolean checkReplacementMethod(MutatedOperator Operator, String strCall, String strParameters) {
        return apiManager.checkReplacementMethod(Operator, strCall, strParameters);
    }

    public boolean hasNextShuffe(String strCall) {
        return apiManager.hasNextShuffle(strCall);
    }

    public LinkedList<Integer> getNextShuffe(String strCall) {
        return apiManager.getNextShuffle(strCall);
    }

    public boolean hasNextReplacement(String strCall) {
        return apiManager.hasNextReplacement(strCall);
    }

    public String getNextReplacement(String strCall) {
        return apiManager.getNextReplacement(strCall);
    }

    public boolean isGenerateMoveUp() {
        return GenerateMOMU || GenerateSOMU || GenerateOOMU;
    }

    public boolean isGenerateMoveDown() {
        return GenerateMOMD || GenerateSOMD || GenerateOOMD;
    }

    public boolean isGenerateMerge() {
        return GenerateMOS || GenerateSOS || GenerateOOS;
    }

    public boolean isGenerateRep() {
        return GenerateMOR || GenerateSOR || GenerateOOR;
    }

    public boolean isGenerateDel() {
        return GenerateMOD || GenerateSOD || GenerateOOD;
    }

    public void clearLinesInterval() {
        if(badList != null)
            badList.clear();
    }

    public ArrayList<Integer> getFilteredLines() {
        return badList;
    }

}
