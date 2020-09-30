/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator;

import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;
import java.io.File;
import java.util.LinkedList;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.ReportGenerator.reports.readableReports.generalExtendedReport;
import mutomvo.Mutation.ReportGenerator.reports.readableReports.generalReport;
import mutomvo.Mutation.ReportGenerator.reports.binaryReports.heathMapReport;
import mutomvo.Mutation.ReportGenerator.reports.binaryReports.killMapReport;
import mutomvo.Mutation.Equivalents.EquivalentMutantInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.ReportGenerator.reports.readableReports.AvgTimeAndKillTableReport;
import mutomvo.Mutation.ReportGenerator.reports.readableReports.subsumingReport;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class reportGenerator extends statsParser{
    
    
    LinkedList<String> appList;
    StatsPersistence m_oSaver;
    File fLocation;
    public void reportGenerator()
    {
        appList = new LinkedList<String>();
        m_oSaver = new StatsPersistence();        
    }   
    public boolean scanStats()
    {
        boolean bRet = false;
        File fLocation;
        
        if(appList == null)
            appList = new LinkedList<String>();
        
        fLocation = new File(MAIN_FOLDER);
        if(fLocation.exists())
        {
            //Getting all the elements of a folder...                    
            File[] listOfFiles = fLocation.listFiles();

            for (File file : listOfFiles) {
                if (file.isDirectory()) {

                    //System.out.println(file.getName());
                    appList.add(file.getName());
                    bRet = true;
                }
            }
        }
            
        return bRet;
    }

    public LinkedList<String> getAppStats() {
        return appList;
    }
    MutantsExecutionInfo getExecutionInfo()
    {
        return mExecInfoList;
    }
    public boolean generateReportWithEquivInfo(String strApp, LinkedList<EquivalentMutantInfo> equivInfoList) {
        
        boolean bRet;
        mutantInfo mInfo;
        EquivalentMutantInfo eInfo;
        int nEquivalent;
        
        bRet =false;
        if(equivInfoList != null)
        {               
            if(doParse(strApp))
            {
                if(mExecInfoList!= null)
                {
                    for(int i=0;i<mExecInfoList.getSize();i++)
                    {
                        if(i< equivInfoList.size())
                        {
                            mInfo = mExecInfoList.getMutant(i);
                            eInfo = equivInfoList.get(i);
                            if(mInfo != null && eInfo != null)
                            {
                                if(eInfo.isEquivalent())
                                {
                                    mInfo.setEquivalent(true);
                                    mExecInfoList.incEquivalents();
                                }
                                else if (eInfo.isDuplicated())
                                {
                                    mExecInfoList.incDuplicated();
                                    nEquivalent = eInfo.getnEquivRoot();
                                    mInfo.setDuplicated(true, nEquivalent);
                                }
                            }
                        }
                    }
                    
                    //Lets generate general report
                    reportTemplate genReport = (reportTemplate) new generalExtendedReport();
                    reportTemplate genReport2 = (reportTemplate) new generalReport();

                    genReport(genReport, strApp);
                    genReport(genReport2, strApp);
                }
            }
        }
        return bRet;
    }
    public boolean generateReport(String strApp) {
        
        boolean bRet;
        
        
        bRet =false;
        if(doParse(strApp))
        {
            if(mExecInfoList!= null)
            {
                //Lets generate general report
                reportTemplate genReport = (reportTemplate) new generalExtendedReport();
                reportTemplate genReport2 = (reportTemplate) new generalReport();
                reportTemplate genReport3 = (reportTemplate) new killMapReport();
                reportTemplate genReport4 = (reportTemplate) new heathMapReport();
                reportTemplate genReport5 = (reportTemplate) new AvgTimeAndKillTableReport();
                reportTemplate genReport6 = (reportTemplate) new subsumingReport();
                
                //TOCHANGE: Generate all reports
                genReport(genReport, strApp);
                genReport(genReport2, strApp);
                genReport(genReport3, strApp);
                genReport(genReport4, strApp);
                genReport(genReport5, strApp);
                genReport(genReport6, strApp);
            }
        }
        return bRet;
    }

    public boolean genReport(reportTemplate genReport, String strApp)
    {
        boolean bRet;
        String strReport, strFile;
        
        bRet = false;
        strReport = genReport.genReport(mExecInfoList);

        fLocation = new File(MAIN_FOLDER+File.separator+strApp);
        if(fLocation.exists())
        {
            if(m_oSaver == null)
                m_oSaver = new StatsPersistence();
            bRet = m_oSaver.saveContentToDisk(fLocation.getAbsolutePath()+File.separator+genReport.getReportName(), strReport, false);
        }
        return bRet;
    }
    
    File getLocation() {
        return fLocation;
    }

    public void generateAllReports() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
