/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Execution.info.aux;

/**
 *
 * @author user
 */
public class mutantHashInfo {

    public int getnAliveMutants() {
        return nAliveMutants;
    }

    public void setnAliveMutants(int nAliveMutants) {
        this.nAliveMutants = nAliveMutants;
    }

    public int getnDeadMutants() {
        return nDeadMutants;
    }

    public void setnDeadMutants(int nDeadMutants) {
        this.nDeadMutants = nDeadMutants;
    }

    public int getnTotalMutants() {
        return nTotalMutants;
    }

    public void setnTotalMutants(int nTotalMutants) {
        this.nTotalMutants = nTotalMutants;
    }

    public String getStrClass() {
        return strClass;
    }

    public void setStrClass(String strClass) {
        this.strClass = strClass;
    }
    
    protected int nTotalMutants;
    protected int nDeadMutants;
    protected int nAliveMutants;
    protected float mutationScore;
    String strClass;

    void insertValue(boolean bAlive) {
        nTotalMutants++;
        
        if(bAlive)
            nAliveMutants++;
        else
            nDeadMutants++;
            
        mutationScore = (float)((float) nDeadMutants / (float) nTotalMutants);
    }
    
    @Override
    public String toString()
    {
        String strRet;
        
        strRet = String.format("%s: %d / %d = %f", strClass, nDeadMutants,nTotalMutants,mutationScore);
        
        return strRet;                
    }
        
    public String toStringSimple()
    {
        String strRet;
        
        strRet = String.format("%s: %d", strClass, nDeadMutants,nTotalMutants,mutationScore);
        
        return strRet;                
    }    
}
