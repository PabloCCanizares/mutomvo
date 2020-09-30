/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Parser.SourceCodeIndexer.Elements;

/**
 * Representa un par de posiciones, inicio y final, para facilitar el trabajo
 * 
 * @author Pablo Cerro Cañizares
 */
public class MuPosition 
{
    int nInitPos;
    int nEndPos;

    public MuPosition()
    {
        nInitPos = nEndPos = -1;
    }

    public MuPosition(int nPosInit, int nPosEnd) {
        nInitPos = nPosInit;
        nEndPos = nPosEnd;
    }
    public int getnEndPos() {
        return nEndPos;
    }

    public void setnEndPos(int nEndPos) {
        this.nEndPos = nEndPos;
    }

    public int getnInitPos() {
        return nInitPos;
    }

    public void setnInitPos(int nInitPos) {
        this.nInitPos = nInitPos;
    }
}
