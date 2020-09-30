/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.aux;

import java.util.HashMap;
import java.util.Iterator;
import mutomvo.Mutation.Operators.Method.EnumMuOperation;
import mutomvo.Mutation.Execution.info.mutants.mutantInfo;

/**
 *
 * @author user
 */
public class mutantClassHashInfo extends mutantHashInfo{
    

    HashMap<String, mutantHashInfo> map;
    
    
    public mutantClassHashInfo(String strClass)
    {
        this.strClass = strClass;
        map = new HashMap<String, mutantHashInfo>();
        nTotalMutants = nAliveMutants = nDeadMutants = 0;
        mutationScore = (float) 0.0;
    }
    void insertElement(String strSubClass, boolean bAlive)
    {
        mutantHashInfo muHashIndex;
        if(map != null && strSubClass != null)
        {
            if(map.containsKey(strSubClass))
            {
                muHashIndex = map.get(strSubClass);
            }
            else
            {
                muHashIndex = new mutantClassHashInfo(strSubClass);
                map.put(strSubClass, muHashIndex);
            }
            muHashIndex.insertValue(bAlive);
        }
    }

    public void insertElement(mutantInfo mInfoIndex) {
        
        boolean bAlive;
        String strMutationOperator;
        EnumMuOperation enumMu;
        if(mInfoIndex!= null)
        {
            
            enumMu = mInfoIndex.getMutationOperator();

            
            if(enumMu != null)
            {
                nTotalMutants++;
                bAlive = mInfoIndex.isAlive();
                if(bAlive)
                    nAliveMutants++;
                else
                    nDeadMutants++;
                
                mutationScore = (float)((float) nDeadMutants / (float) nTotalMutants);
                strMutationOperator = enumMu.toString();
                insertElement(strMutationOperator, bAlive);
            }
        }
    }
    public String toStringClass()
    {
        String strRet;
        StringBuffer buffer;
        mutantHashInfo mIndex;
        buffer= new StringBuffer();
        
        buffer.append(this.toString()+"\n");

        Iterator<String> keySetIterator = map.keySet().iterator();
        while(keySetIterator.hasNext()){
          String key = keySetIterator.next();
          mIndex = map.get(key);
          buffer.append(mIndex.toString()+"\n");
        }

        strRet = buffer.toString();
        return strRet;
    }
    @Override
    public String toStringSimple()
    {
        String strRet;
        StringBuffer buffer;
        mutantHashInfo mIndex;
        buffer= new StringBuffer();
        
        buffer.append(super.toStringSimple()+"\n");

        Iterator<String> keySetIterator = map.keySet().iterator();
        while(keySetIterator.hasNext()){
          String key = keySetIterator.next();
          mIndex = map.get(key);
          buffer.append(mIndex.toStringSimple());
        }

        strRet = buffer.toString();
        return strRet;
    }    
}
