/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.SourceCodeIndexer.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloque de código, utilizado para localizar instrucciones correspondientes
 * a las llamadas OMNET y saber si se puede cambiar su localizacion e incluso borrarlas
 * 
 * @author Pablo C. Cañizares
 */
public class CodeBlock 
{
   
    public List<CodeBlock> getBlockList() {
        return BlockList;
    }

    public String getBlockName() {
        return blockName;
    }

    public int getAbsolutInitLine() {
        return AbsolutInitLine;
    }

    public void setAbsolutInitLine(int AbsolutInitLine) {
        this.AbsolutInitLine = AbsolutInitLine;
    }

    public int getEndLine() {
        return EndLine;
    }

    public void setEndLine(int EndLine) {
        this.EndLine = EndLine;
    }

    public int getInitLine() {
        return InitLine;
    }

    public void setInitLine(int InitLine) {
        this.InitLine = InitLine;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public void setBlockList(List<CodeBlock> BlockList) {
        this.BlockList = BlockList;
    }

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

    public List<CodeLine> getLineList() {
        return LineList;
    }

    public void setLineList(List<CodeLine> LineList) {
        this.LineList = LineList;
    }

    public CodeBlock getRoot() {
        return root;
    }

    public void setRoot(CodeBlock root) {
        this.root = root;
    }

    public EnumBlockType getType() {
        return type;
    }

    public void setType(EnumBlockType type) {
        this.type = type;
    }
    
    public void insertBlock(CodeBlock block)
    {
        BlockList.add(block);        
    }
    
    public void insertLine(CodeLine line)
    {
        LineList.add(line);        
    }

    public int getnNumLines() {
        return nNumLines;
    }

    public void setnNumLines(int nNumLines) {
        this.nNumLines = nNumLines;
    }
    
    String blockName;
    boolean mainBlock;
    int AbsolutInitLine;
    int AbsolutInitPos; //Esta es la posición donde empieza el método
    int InitPos;        //Esta es la posición de la llave de apertura
    int InitLine;
    int EndPos;
    int EndLine;
            
    CodeBlock root;
    EnumBlockType type;
    int nNumLines;
    
    List<CodeBlock> BlockList=null;
    List<CodeLine> LineList=null;
    
    //TODO: Hay que replantearse tener una lista de un tipo "superior"
    //que englobe las líneas y los bloques. En orden ElementList <Bloques + lineas>
    //para facilitar encontrar 
    public CodeBlock()
    {
        BlockList= new ArrayList<CodeBlock>();  
        LineList= new ArrayList<CodeLine>();
        root = null;
        InitPos = InitLine = -1;
        EndPos = EndLine = -1;
        nNumLines=0;
        AbsolutInitPos = AbsolutInitLine = -1;
        mainBlock = false;
    }

    public void setAbsolutInitPos(int nCharCount) 
    {
        AbsolutInitPos = nCharCount;
    }

    public void IncLine() 
    {
        nNumLines++;
        if(root != null)
            IncLine();
    }

    public void setMainBlock(boolean b) 
    {
        mainBlock = b;
    }
}
