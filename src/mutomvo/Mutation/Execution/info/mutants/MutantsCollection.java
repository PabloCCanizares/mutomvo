/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.mutants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import mutomvo.Mutation.Execution.info.aux.mutantClassHashInfo;

/**
 *
 * @author user
 */
public class MutantsCollection {

    int m_nTotalSize;
    LinkedList<mutantInfo> m_completeList;
    HashMap<String, mutantClassHashInfo> mutantMap;        
    
    public void MutantsCollection()
    {
        m_nTotalSize=0;
        m_completeList = new LinkedList<mutantInfo>();
        mutantMap = new HashMap<String, mutantClassHashInfo>();
    }
    
    public void insertMutantList(LinkedList<mutantInfo> mList) {
        
        String strClass;
        mutantInfo mInfo;
        mutantClassHashInfo hashCollection;
        
        if(mList != null)
        {
            if(m_completeList == null)
                m_completeList = new LinkedList<mutantInfo>();
            
            if(mutantMap == null)
                mutantMap = new HashMap<String, mutantClassHashInfo>();
                
            for(int i=0;i<mList.size();i++)
            {
                mInfo = mList.get(i);
                m_completeList.add(mInfo);
                
                strClass = mInfo.getMutationClassString();                        

               if(mutantMap.containsKey(strClass))
               {
                   hashCollection = mutantMap.get(strClass);
               }
               else
               {
                   //Create the subclass hash
                   hashCollection = new mutantClassHashInfo(strClass);
                   mutantMap.put(strClass, hashCollection);
               }
               hashCollection.insertElement(mInfo);
               m_nTotalSize++;
            }
        }
    }

    public String genReport() {
        String strRet, strMut;
        StringBuffer strTestExecution;
        mutantInfo mInfo;
        strRet="";
        if(m_completeList != null)
        {
            strTestExecution = new StringBuffer("");
            
            for(int i=0;i<m_completeList.size();i++)
            {
                mInfo = m_completeList.get(i);
                
                if(mInfo != null)
                {
                    strMut = Integer.toString(i)+" - "+mInfo.ToStringReport();
                    if(!strMut.isEmpty())
                        strTestExecution.append(strMut);
                }
                
            }
            strRet = strTestExecution.toString();
        }
        
        return strRet;
    }
public String genShortReport()
    {
        String strRet, strGeneralReport;
        StringBuffer buffer;
        mutantClassHashInfo mIndex;
        buffer= new StringBuffer();
        
        
        strGeneralReport = String.format("General data: %d", m_nTotalSize);
        
        buffer.append(strGeneralReport+"\n");

        Iterator<String> keySetIterator = mutantMap.keySet().iterator();
        while(keySetIterator.hasNext()){
          String key = keySetIterator.next();
          mIndex = mutantMap.get(key);
          buffer.append(mIndex.toStringSimple()+"\n");
        }

        strRet = buffer.toString();
        System.out.print(strRet);
        return strRet;
    }

    public int getGeneratedMutants() {
        return m_nTotalSize;
    }
}
