/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.ReportGenerator.reports.readableReports;

import java.util.LinkedList;
import java.util.List;
import mutomvo.Mutation.Execution.info.mutants.MutantsExecutionInfo;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;
import mutomvo.Mutation.ReportGenerator.reports.auxiliar.GroupedMutExecInfo;
import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;
import mutomvo.Mutation.Operators.EnumOperatorClass;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Operators.MutationOperator;
import mutomvo.Mutation.ReportGenerator.reports.reportTemplate;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class subsumingReport implements reportTemplate{

    GroupedMutExecInfo infoGroup;
    private final String REPORT_NAME = "SubsumingReport.txt";
    @Override
    public String genReport(MutantsExecutionInfo mInfo) {
        String strRet;
        List<mutantInfo> initialList, processedList;
        
        strRet="";
        processedList = initialList = null;        
        if(mInfo != null)
        {
            initialList = generateFullInitialList(mInfo);            
                        
            processedList = processSubsumingMutants(initialList);            
            
            strRet = generateInternalReport(processedList);
        }
        return strRet;
    }

    @Override
    public String getReportName() {
       return REPORT_NAME;
    }

    private boolean checkSubsuming(mutantInfo mInfoActual, mutantInfo mInfoAux) {
        boolean bRet, bSubsuming;
        int nTotalTestsActual, nTotalTestsAux;
        
        bRet = false;
        bSubsuming=true;
        if(mInfoActual!=null && mInfoAux != null)
        {
            nTotalTestsActual = mInfoActual.getTotalTests();
            nTotalTestsAux = mInfoAux.getTotalTests();
            
            if(nTotalTestsActual == nTotalTestsAux)
            {
                for(int i=0;i<nTotalTestsActual&&bSubsuming;i++)
                {
                    if(!mInfoActual.isAlive(i))
                    {
                        if(mInfoAux.isAlive(i))
                            bSubsuming=false;
                    }
                }
            }
            if(bSubsuming && areUndistinguished(mInfoActual, mInfoAux))
            {
                bSubsuming = false;
            }
            bRet = bSubsuming;
        }
        
        return bRet;
    }
    private boolean areUndistinguished(mutantInfo mInfoActual, mutantInfo mInfoAux)
    {
        boolean bRet, bUndish;
        int nTotalTestsActual, nTotalTestsAux;
        
        bRet= false;
        bUndish=false;
        if(mInfoActual!=null && mInfoAux != null)
        {
            nTotalTestsActual = mInfoActual.getTotalTests();
            nTotalTestsAux = mInfoAux.getTotalTests();
            
            if(nTotalTestsActual == nTotalTestsAux)
            {
                if((mInfoActual.getAliveTests() == mInfoAux.getAliveTests()) &&
                   (mInfoActual.getKillingTests() == mInfoAux.getKillingTests()))
                {
                    bUndish=true;
                    for(int i=0;i<nTotalTestsActual&&bUndish;i++)
                    {
                        if((!mInfoActual.isAlive(i) && mInfoAux.isAlive(i)) ||
                           (mInfoActual.isAlive(i) && !mInfoAux.isAlive(i)))
                        {
                            bUndish=false;
                        }
                    }
                }                
            }
        }        
        
        bRet = bUndish;
        
        return bRet;
    }

    private List<mutantInfo> generateFullInitialList(MutantsExecutionInfo mInfo) {
        
        List<mutantInfo> initialList = new LinkedList<mutantInfo>();
        
        //Delete duplicated, equivalents and alive mutants
        for(int i=0;i<mInfo.getSize();i++)
        {          
            mutantInfo mutantInfo = mInfo.getMutant(i);
           
            if(mutantInfo != null && !mutantInfo.isAlive())
            {

                if(!mutantInfo.isEquivalent() && !mutantInfo.isDuplicated())
                {
                    initialList.add(mutantInfo);
                }
            }                
        }
        return initialList;
    }
    private List<mutantInfo> generateInitialListByClass(MutantsExecutionInfo mInfo, EnumOperatorClass muClass) {
        
        List<mutantInfo> initialList = new LinkedList<mutantInfo>();
        
        //Delete duplicated, equivalents and alive mutants
        for(int i=0;i<mInfo.getSize();i++)
        {          
            mutantInfo mutantInfo = mInfo.getMutant(i);
           
            if(mutantInfo != null && !mutantInfo.isAlive() && mutantInfo.getMutationClass() == muClass)
            {

                if(!mutantInfo.isEquivalent() && !mutantInfo.isDuplicated())
                {
                    initialList.add(mutantInfo);
                }
            }                
        }
        return initialList;
    }

    List<mutantInfo> processSubsumingMutants(List<mutantInfo> initialList) 
    {
        List<mutantInfo> retList;
        mutantInfo mInfoActual, mInfoAux;
        
        retList = new LinkedList<mutantInfo>();
        for(int i=0;i<initialList.size();i++)
        {
            mInfoActual = initialList.get(i);
            for(int j=0;j<initialList.size();j++)
            {
                if(i!=j && !mInfoActual.isSubsumed())
                {
                    mInfoAux = initialList.get(j);

                    System.out.printf("Analysing mutant [%d | A: %d vs K: %d] vs mutant [%d |  A: %d vs K: %d]\n",
                                i, mInfoActual.getAliveTests(), mInfoActual.getKillingTests(), 
                                j, mInfoAux.getAliveTests(), 
                                mInfoAux.getKillingTests());

                    if(checkSubsuming(mInfoActual, mInfoAux))
                    {
                        mInfoAux.setSubsumed(true);
                        mInfoAux.setSubsumingMutant(mInfoActual);
                        mInfoActual.setSubsuming(true);
                        mInfoActual.insertSubsumedMutant(mInfoAux);                            
                    }
                    /*else
                        System.out.printf("The mutant [%d | A: %d vs K: %d] not subsumes mutant [%d |  A: %d vs K: %d]\n",
                                i, mInfoActual.getAliveTests(), mInfoActual.getKillingTests(), 
                                j, mInfoAux.getAliveTests(), 
                                mInfoAux.getKillingTests());*/                                        
                }                
            }
            retList.add(mInfoActual);
        }
        return retList;
    }

    private String generateInternalReport(List<mutantInfo> processedList) {
        String strRet;
        mutantInfo mInfoActual;                
        List<mutantInfo> subsumingList, subsumedList;
        
        //Initialization
        strRet="";
        subsumingList = new LinkedList<mutantInfo>();
        subsumedList = new LinkedList<mutantInfo>();
        
        for(int i=0;i<processedList.size();i++)
        {
            mInfoActual = processedList.get(i);

            if(!mInfoActual.isSubsumed())
            {
                subsumingList.add(mInfoActual);
                System.out.printf("Mutant %d, subsumes %d mutants\n", mInfoActual.getIndex(), mInfoActual.getSubsumedMutants());
                System.out.printf(mInfoActual.ToStringReport());
                System.out.printf("[A: %d vs K: %d]",mInfoActual.getAliveTests(), mInfoActual.getKillingTests());

                strRet = strRet.concat(String.format("Mutant %d, subsumes %d mutants: %s\n", 
                        mInfoActual.getIndex(), mInfoActual.getSubsumedMutants(),mInfoActual.getSubsumedMutantsToString()));
                strRet = strRet.concat(String.format("[A: %d vs K: %d]",mInfoActual.getAliveTests(), mInfoActual.getKillingTests()));
                strRet = strRet.concat(String.format(mInfoActual.ToStringReport()));
            }
            else
            {
                subsumedList.add(mInfoActual);
                System.out.printf("Mutant %d, subsumes %d mutants, is subsumed by %d mutants: %s\n",
                        mInfoActual.getIndex(), mInfoActual.getSubsumedMutants(), mInfoActual.getSubsumingMutants(), mInfoActual.getSubsumingMutantsToString());
            }
        }
        System.out.printf("Total subsuming mutants: %d of %d\n", subsumingList.size(), processedList.size());     
        strRet = strRet.concat(String.format("Total subsuming mutants: %d of %d\n", subsumingList.size(), processedList.size()));

        return strRet;
    }
    
} 
