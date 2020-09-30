/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaidentity;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class DBGenerator {
    
    
    final private String  DB_NAMES = "Z:\\Software development\\JAVA\\JavaIdentity\\spanish-names\\hombres.csv";
    final private String  DB_SURNAMES = "Z:\\Software development\\JAVA\\JavaIdentity\\spanish-names\\apellidos.csv";
    final private String  DB_QUOTES = "";
    
    private  CSVReader namesDB;
    private  CSVReader surnamesDB;
    private LinkedList<String> namesList;
    private LinkedList<String> surnamesList;
    private LinkedList<String> quotesList;
    public DBGenerator()
    {
    
    }
    
    public boolean loadDB()
    {
        boolean bRet = false;
        //load databases
        namesList = loadDBExt(DB_NAMES);
        surnamesList = loadDBExt(DB_SURNAMES);
        
        if((namesList != null && namesList.size()>0) && (surnamesList != null && surnamesList.size()>0))
        {
            bRet = true;
            System.out.printf("Total names: %d | Total surnames: %d", namesList.size(), surnamesList.size());
        }
        return bRet;
    }
    
    private LinkedList<String> loadDB(String strDB)
    {
        LinkedList<String> retList = null;
        String strName;
        try {
             
            CSVReader reader = new CSVReader(new FileReader(strDB), ',' , '"' , 1);
            
            retList = new LinkedList<>();
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    //Verifying the read data here
                    strName = nextLine[0];
                    strName.toLowerCase();
                    retList.add(strDB);
                    System.out.println(Arrays.toString(nextLine));
                }    
            }} catch (FileNotFoundException ex) {
            Logger.getLogger(DBGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retList;
    }
    public String getName()
    {
        String strRet="";
        int nTotalSize;
        nTotalSize = namesList.size();
        if(namesList != null && nTotalSize>0)
        {
            //Número aleatorio
            int nNumber;
        
             nNumber =  (int) (Math.round(Math.random()*nTotalSize));
             try
             {
                strRet = namesList.get(nNumber);
             }catch(IndexOutOfBoundsException e)
             {
             
             }
        }
        
        return strRet;
    }
    public String getSurname()
    {
        String strRet="";
        int nTotalSize;
        nTotalSize = surnamesList.size();
        if(surnamesList != null && nTotalSize>0)
        {
            //Número aleatorio
            int nNumber;
        
             nNumber =  (int) (Math.round(Math.random()*nTotalSize));
             try
             {
                strRet = surnamesList.get(nNumber);
             }catch(IndexOutOfBoundsException e)
             {
             
             }
        }
        
        return strRet;
    }
    private LinkedList<String> loadDBExt(String strDB)
    {              
        BufferedReader br = null;
        LinkedList<String> retList = null;
        try {
            String line;
            String[] toks;
            CSVParser parser=new CSVParser();            
            int nIndex;
            String strName;
            boolean first=true;
            
            File file = new File(strDB);
            br = new BufferedReader(new FileReader(file));
            nIndex = 0;
            retList = new LinkedList<>();
            
            while ((line=br.readLine()) != null) 
            {
                //line=line.trim();
                if (line.length() <= 0)     continue;
                try {
                    toks=parser.parseLine(line);
                    strName = toks[0];
                   // System.out.println(line);
                    if(nIndex>0)
                        retList.add(strName);
                }catch(IOException e)
                {}
                nIndex++;
            }   
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(DBGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) 
        {
            Logger.getLogger(DBGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } 
      /*  finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(DBGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        return retList;
}
}
