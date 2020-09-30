/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.SourceCodeIndexer.Elements;

/**
 *
 * @author user
 */
public class CodeLine 
{
    String strLine;
    int InitPos;
    int EndPos;
    private int nIndex;

    public int getEndPos() {
        return EndPos;
    }

    public void setEndPos(int EndPos) {
        this.EndPos = EndPos;
    }

    public int getInitPos() {
        return InitPos;
    }

    public void setInitPos(int InitPos) {
        this.InitPos = InitPos;
    }

    public void setString(String LastLineString) {
        strLine = LastLineString;
    }

    public int getIndex() 
    {
        return nIndex;
    }

    //TODO: Para mejorar aun el sistema de lineas, se debería tener un puntero anterior y puntero siguiente

    //la línea no va a ser válida, si empieza por // ó está vacía , PD: añadir que sea { no está de más saltarla tambien
    public boolean isNotValid() 
    {
        boolean bRet =false;
        String strLineAux;     
        
        strLineAux = this.strLine;
        strLineAux = strLineAux.replaceAll("\\s","");
        //si está vacía, es un comentario, no una llave de apertura, no vale y pido otra
        if(strLineAux.length() == 0 || strLineAux.indexOf("//") != -1 /*|| strLineAux.indexOf("{") != -1*/)
        {
            bRet = true;
        }
        
        return bRet;
    }

    public boolean isNotValidDown() {
        boolean bRet =false;
        String strLineAux;     
        
        strLineAux = this.strLine;
        strLineAux = strLineAux.replaceAll("\\s","");
        //si está vacía, es un comentario, no una llave de apertura, no vale y pido otra
        if(strLineAux.length() == 0 || strLineAux.indexOf("//") != -1 /*|| strLineAux.indexOf("}") != -1*/)
        {
            bRet = true;
        }
        
        return bRet;
    }
}
