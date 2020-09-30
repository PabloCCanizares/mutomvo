/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Equivalents;

import java.util.LinkedList;
import mutomvo.Mutation.Auxiliar;
import mutomvo.Mutation.ReportGenerator.reportGenerator;

/**
 *
 * @author Pablo C. Cañizares
 */
public class EquivalentChecker {
    
    private Md5MutantHandler md5Checker;
    private String appName;
    
    public void setApplicationFolder(String applicationFolder) {
        md5Checker.setApplicationFolder(applicationFolder);
    }

       public void setMutantsFolder(String mutantsFolder) {
        md5Checker.setMutantsFolder(mutantsFolder);
    }
    
    public void setAppName(String appName) {
        md5Checker.setAppName(appName);
        this.appName = appName;
    }
    
    public EquivalentChecker()
    {
        md5Checker =  new Md5MutantHandler();
        appName="";
    }
    
    public void doStart()
    {
        LinkedList<EquivalentMutantInfo> equivInfoList;
        reportGenerator repGenerator;
        
        if(md5Checker.analyseMd5())
        {
            if(Auxiliar.AskToUserGUI("Do you want to merge the equivalent information with the existing one?"))
            {
                equivInfoList = md5Checker.getEquivalentList();
                if(equivInfoList!= null && equivInfoList.size()>0)
                {
                    //The next step consists in including the information obtained during the equivalence analysis in the execution info.
                    repGenerator = new reportGenerator();
                    repGenerator.generateReportWithEquivInfo(appName, equivInfoList);
                }
            }            
        }                        
    }

    public void setBinaryFolder(String applicationFolder) {
        md5Checker.setBinaryFolder(applicationFolder);
    }
}