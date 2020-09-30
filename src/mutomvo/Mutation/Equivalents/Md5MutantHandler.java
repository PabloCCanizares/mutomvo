/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Equivalents;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo C. Cañizares
 */
public class Md5MutantHandler {

    private String appName;
    private String applicationFolder;
    private String binaryFolder;
    private String mutantsFolder;
    private int nMutants;
    private String originalHash;
    private LinkedList<EquivalentMutantInfo> mutantList;    
    
    public Md5MutantHandler()
    {
        mutantList = new LinkedList<EquivalentMutantInfo>();
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApplicationFolder() {
        return applicationFolder;
    }

    public void setApplicationFolder(String applicationFolder) {
        this.applicationFolder = applicationFolder;
    }

    public String getMutantsFolder() {
        return mutantsFolder;
    }

    public void setMutantsFolder(String mutantsFolder) {
        this.mutantsFolder = mutantsFolder;
    }  
  
   public static byte[] createChecksum(String filename) throws Exception {
       InputStream fis =  new FileInputStream(filename);

       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;

       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);

       fis.close();
       return complete.digest();
   }

   // see this How-to for a faster way to convert
   // a byte array to a HEX string
   private String getMD5Checksum(String filename) throws Exception {
       byte[] b = createChecksum(filename);
       String result = "";

       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
       return result;
   }
   
    public String getMd5(int nMutant)
    {
        String strMd5;
        String strMutantFile;
        
        strMd5 = "";
        try {            
            strMutantFile = String.format("%s/%d/%s",mutantsFolder, nMutant, appName);
            strMutantFile = strMutantFile.replace(".c", "");
            strMd5 = getMD5Checksum(strMutantFile);
            
            System.out.println ("analyseMd5 - Mutant "+nMutant+" | Md5: "+strMd5+"\n");
        } catch (Exception ex) {
            System.out.println ("analyseMd5 -Exception catched (file not exists) while analysing mutant "+nMutant+"\n");            
        }
        return strMd5;
    }
    public String getOriginalMd5()
    {
        String strMd5;
        String strMutantFile;
        
        strMd5 = "";
        try {            
            strMutantFile = String.format("%s/simcan",binaryFolder);
            strMd5 = getMD5Checksum(strMutantFile);
            
            System.out.println ("analyseMd5 - Original App | Md5: "+strMd5+"\n");
        } catch (Exception ex) {
         System.out.println("Error creating a Md5 hash from the original application");
        }
        return strMd5;
    }
    
    public boolean analyseMd5()
    {
        boolean bRet;
        String strMd5Hash;
        EquivalentMutantInfo equivMutInfo, equivRoot;
        int nEquivalents, nDup, nRootId;
        HashMap<String, EquivalentMutantInfo> mutmap = new HashMap<String, EquivalentMutantInfo>();
        
        
        bRet = false;
        nEquivalents = nDup = nRootId = 0;
        
        System.out.println ("analyseMd5 - Analysing mutants\n");
        
        //The total number of mutants is extracted from the mutant folder!
        originalHash = getOriginalMd5();
        nMutants = calculateNumberOfMutants(); 
        
        System.out.printf ("analyseMd5 - Mutant binary files found: %d\n", nMutants);
        for(int i=0;i<nMutants;i++)
        {
            strMd5Hash = getMd5(i);
            if(strMd5Hash != null && !strMd5Hash.isEmpty())
            {
                equivMutInfo =  new EquivalentMutantInfo(i, strMd5Hash);
                if(strMd5Hash.equals(originalHash))
                {
                    equivMutInfo.setEquivalent(true);
                    nEquivalents++;
                }
                else if (mutmap.containsKey(strMd5Hash))
                {
                    equivRoot = mutmap.get(strMd5Hash);
                    nRootId = equivRoot.getMutantId();
                    equivMutInfo.setDuplicated(true);

                    equivMutInfo.setnEquivRoot(nRootId);
                    nDup++;
                }
                else
                {
                    mutmap.put(strMd5Hash, equivMutInfo);
                }


                mutantList.add(equivMutInfo);
            }

        }
        
        System.out.println ("analyseMd5 - Equivalent mutants: "+nEquivalents+" | Dupped mutants: "+nDup+"\n");
        
        if(nEquivalents>0 || nDup>0)
            bRet = true;
        
        return bRet;
    }
    
    private int calculateNumberOfMutants()
    {
        File pathFile;
        int nRet;
        
        pathFile = new File(mutantsFolder);
        if(!pathFile.exists())
            pathFile.mkdir();
        nRet = pathFile.listFiles().length;
        
        return nRet;
    }

    void setBinaryFolder(String binaryFolder) {
        this.binaryFolder = binaryFolder;
    }

    LinkedList<EquivalentMutantInfo> getEquivalentList() {
        return mutantList;
    }
}
