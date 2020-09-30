/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Operators;

import mutomvo.Mutation.Operators.Method.Arithmetic.EnumBinArithmetic;
import java.util.ArrayList;
import java.util.List;
import mutomvo.Mutation.Operators.Method.Assignment.EnumAssignment;
import mutomvo.Mutation.Operators.Method.Conditional.EnumConditional;
import mutomvo.Mutation.Operators.Method.Relational.EnumRelational;

/**
 * Dada una configuración, ofrece todos los operadores de mutacion disponibles
 * en formato caracter, utilizado para realizar búsquedas directamtene en el
 * texto
 *
 * @author Pablo C. Cañizares
 */
public class MutationOperatorCharList {

    List<String> CharMutantsList;
    boolean GenerateArithmetic;
    boolean GenerateRelational;
    boolean GenerateConditional;
    boolean GenerateOMNET;
    private boolean GenerateAssignment;
    
    public boolean isGenerateArithmetic() {
        return GenerateArithmetic;
    }

    public boolean isGenerateRelational() {
        return GenerateArithmetic;
    }
    public boolean isGenerateConditional() {
        return GenerateConditional;
    }
    public void setArithmetic(boolean GenerateArithmetic) {
        this.GenerateArithmetic = GenerateArithmetic;
    }

    public void setRelational(boolean GenerateRelational) {
        this.GenerateRelational = GenerateRelational;
    }

    public MutationOperatorCharList() {
        CharMutantsList = new ArrayList<String>();
    }

    public void generateCharMutants() {
        if (GenerateArithmetic) {
            insertArithmetic();
        }
        if(GenerateRelational)
            insertRelational();
        
        if(GenerateConditional)
            insertConditional();    
        
        if(GenerateAssignment)
            insertAssignment();
        
        //TO-Do
    }

    private void insertRelational() 
    {
        for (EnumRelational eENUM : EnumRelational.values()) {
            if ((eENUM.ordinal()) != 0) {
                CharMutantsList.add(eENUM.toString());
            }
        }
    }
    private void insertConditional() 
    {
        for (EnumConditional eCond : EnumConditional.values()) {
            if ((eCond.ordinal()) != 0) {
                CharMutantsList.add(eCond.toString());
            }
        }
    }
    private void insertArithmetic() {
        EnumBinArithmetic eArithmetic = EnumBinArithmetic.eAR_NONE;

        for (EnumBinArithmetic eENUM : EnumBinArithmetic.values()) {
            if ((eENUM.ordinal()) != 0) {
                CharMutantsList.add(eENUM.toString());
            }
        }
    }
    private void insertAssignment() 
    {
        for (EnumAssignment eENUM : EnumAssignment.values()) {
            if ((eENUM.ordinal()) != 0) {
                CharMutantsList.add(eENUM.toString());
            }
        }    
    }

    public boolean hasNext() {
        boolean bRet = false;

        if (CharMutantsList != null) {
            bRet = (CharMutantsList.size() > 0);
        }

        return bRet;
    }

    public String getNext() {
        String strReturn = "";

        if (CharMutantsList.size() > 0) {
            strReturn = CharMutantsList.remove(0);
        }

        return strReturn;
    }

    public int getSize() {
        int nRet = 0;

        if (CharMutantsList != null) {
            nRet = CharMutantsList.size();
        }

        return nRet;
    }

    public void setOMNET(boolean generateOMNET) 
    {
        GenerateOMNET = generateOMNET;
    }

    public void setConditional(boolean generateConditional) 
    {
        GenerateConditional = generateConditional;
    }

    public void setAssignment(boolean generateAssignement) 
    {
        GenerateAssignment =generateAssignement;
    }

    public boolean isGenerateAssignment() {
        return GenerateAssignment;
    }

}
