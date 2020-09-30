/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.auxiliar;

import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class MutantResultsByOperator {

    Long lKillerTest;
    Long lKillers;
    Long lAvgTime;
    
    float fAvgKillers;
    float fAvgKillerTest;
    int nTotalTests;
    int nInsertedMutants;
    EnumMuOperation enumOperator;
    EnumOperatorClass enumOperatorClass;

    public Long getlAvgTime() {
        return lAvgTime;
    }

    public void setlAvgTime(Long lAvgTime) {
        this.lAvgTime = lAvgTime;
    }

    public int getlKillerTest() {
        return lKillerTest.intValue();
    }

    public void setlKillerTest(Long lKillerTest) {
        this.lKillerTest = lKillerTest;
    }

    public int getlKillers() {
        return lKillers.intValue();
    }

    public void setlKillers(Long lKillers) {
        this.lKillers = lKillers;
    }

    public int getnInsertedMutants() {
        return nInsertedMutants;
    }

    public void setnInsertedMutants(int nInsertedMutants) {
        this.nInsertedMutants = nInsertedMutants;
    }

    public int getnTotalTests() {
        return nTotalTests;
    }

    public void setnTotalTests(int nTotalTests) {
        this.nTotalTests = nTotalTests;
    }
    
    MutantResultsByOperator(EnumOperatorClass enumOperatorClass, EnumMuOperation enumOperator) {
        this.enumOperator =  enumOperator;
        this.enumOperatorClass = enumOperatorClass;
        nInsertedMutants=0;
        lKillerTest=lAvgTime=lKillers=(long)0;
    }

    EnumMuOperation getMutationOperator() {
        return enumOperator;
    }
    
    void addInfo(EnumMuOperation enumMuOperation, int nKillerTest, int nKillers, Long lAvgTime) {
        this.nInsertedMutants++;
        this.lKillerTest = this.lKillerTest + (long)nKillerTest;
        this.lKillers = this.lKillers + (long)nKillers;
        this.lAvgTime = this.lAvgTime + lAvgTime;
        
        try
        {
        System.out.printf("[+] %s->%s (%d, %d, %d)\n", enumOperatorClass.toString(),
                enumMuOperation.toString(),nKillerTest,nKillers,lAvgTime.intValue());
        }catch(NullPointerException np)
        {
            System.out.println("Mal");
        }
    }

    void addTotalTests(int nTotalTests) {
        this.nTotalTests=nTotalTests;
    }

    void process() {
        
        if(this.lKillerTest.intValue()>0)
        {
            this.fAvgKillerTest = (float)((float)lKillerTest /(float)nInsertedMutants);
        }
        
        if(this.lKillers.intValue()>0)
            this.fAvgKillers = (float)((float)this.lKillers  / (float)nInsertedMutants);
        
        if(this.lAvgTime.intValue()>0)
            this.lAvgTime = this.lAvgTime / nInsertedMutants;
        
               /* System.out.printf("%s->%s (%.2f, %.2f, %d)\n", enumOperatorClass.toString(),
                enumOperator.toString(),this.lKillerTest.intValue(),
                this.lKillers.intValue(),lAvgTime.intValue());*/
    }
    @Override
    public String toString()
    {
        String strRet = "";
        strRet= String.format("%s->%s (%.2f, %.2f, %d)\n", enumOperatorClass.toString(),
                enumOperator.toString(),this.fAvgKillerTest,
                this.fAvgKillers,lAvgTime.intValue());
        return strRet;
    }
}
