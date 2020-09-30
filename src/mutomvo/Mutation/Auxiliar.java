/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Pablo C. Cañizares
 */
public class Auxiliar {
    
public static boolean AskToUserGUI(String messageToUser)
{
   String text;
   boolean bRet = false;
   int response;


  response = JOptionPane.showConfirmDialog(null,
                        messageToUser,
                        "Confirm to re-load the test generator",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

    // Proceed to load a new configuration
    if (response == JOptionPane.OK_OPTION){

        bRet =true;
    }   

   return bRet;
}    
public static boolean AskToUser(String messageToUser, String expected) {
       
       String text;
       boolean bRet = false;
                 
       text="";
       System.out.printf(messageToUser);
       //Ask the user...

       //catch the response
        InputStreamReader leer = new InputStreamReader(System.in);
        BufferedReader buff = new BufferedReader(leer);
           try {
               text = buff.readLine();
           } catch (IOException ex) {
               Logger.getLogger(Auxiliar.class.getName()).log(Level.SEVERE, null, ex);
           }
       if (text.indexOf(expected) == 0)
       {
           bRet = true;
           System.out.printf("User response OK : %s vs %s \n", text, expected);
       }
       else
       {
           System.out.printf("User response KO : %s vs %s \n", text, expected);
       }
           
       return bRet;
    }    

    public static int checkIfMutantsExist(String path)
    {
        File pathFile;
        int nRet;
        
        nRet = 0;
        pathFile = new File(path);
        if(!pathFile.exists())
            pathFile.mkdir();
        
        if(pathFile.exists())
            nRet = pathFile.listFiles().length;
        
        //.h .cc .ned : es necesario dividir entre 3
        if(nRet >0 )
            nRet = nRet /3;
        return nRet;
    }
}
