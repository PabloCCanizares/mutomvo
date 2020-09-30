/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.auxiliars;

import mutomvo.Exceptions.MutomvoException;
import mutomvo.Mutation.Execution.ExecutionConfig;

/**
 *
 * @author Pablo C. Cañizares 
 */
public interface IMuTeXec 
{    
    boolean Abort();
    boolean fullExec();

    public void reset(String projectName);

    //public void Configure(int nGeneratedMutants, int nGeneratedTests, String application, String applicationType, String scenario, Boolean bClean, Boolean bExecuteAll);
    public void Configure(ExecutionConfig exeConfig) throws MutomvoException;
    public void setVisible(boolean b);

    public void run();

    public void forceExecutionCmd();

    }
