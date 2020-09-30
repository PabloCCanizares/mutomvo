/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.auxiliars;

/**
 *
 * @author Pablo C. Cañizares 
 */
public enum EnumMutexecState {
    eMUT_NONE("Idle"), 
    eMUT_COMPILING("Compiling"),
    eMUT_EXE_ORIG("Executing original"),
    eMUT_EXE_MUT("Executing mutants"),
    eMUT_EXE_FIN_KO("Error"),
    eMUT_EXE_FIN_ABORT("Abort"),
    eMUT_EXE_FIN_OK("Finished OK");
    
    private final String name;       

    private EnumMutexecState(String s) {
        name = s;
    }
    
    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}
