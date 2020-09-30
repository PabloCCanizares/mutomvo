/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.tests;

import mutomvo.Mutation.Execution.auxiliars.EnumMutantState;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class testInfo {

    int number;
    String  result;
    double time;
    EnumMutantState eState; 
    
    public testInfo(int nIndexTest, String strResult, double dExecTime, EnumMutantState eStateIn) {
        number = nIndexTest;
        result = strResult;
        time = dExecTime;
        eState = eStateIn;
    }
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTestVerboseResult() {

        return result;
    }
        
    public String getResult() {
        String strRet;
        
        if(eState == EnumMutantState.eMUTANT_ALIVE)
            strRet = "Alive";
        else
            strRet = "Dead";
        return strRet;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    String toStringCsv() {
        return String.format("%d,%.2f,%s,%s\n", number, time, result,eState);
    }
    String toStringTimeCsv() {
        return String.format("%d ", (int)time);
    }
    
    public boolean isAlive() {
        return (eState == EnumMutantState.eMUTANT_ALIVE);
    }
    

}
