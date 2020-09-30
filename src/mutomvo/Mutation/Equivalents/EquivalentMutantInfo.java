/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Equivalents;

/**
 *
 * @author Pablo C. Cañizares
 */
public class EquivalentMutantInfo {
    
    private int mutantId;
    private String md5Hash;
    private boolean equivalent;
    private boolean duplicated;
    private int nEquivRoot;

    public EquivalentMutantInfo(int mutantId, String md5Hash)
    {
        this.mutantId = mutantId;
        this.md5Hash = md5Hash;
    }
    public int getMutantId() {
        return mutantId;
    }

    public void setMutantId(int mutantId) {
        this.mutantId = mutantId;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public boolean isEquivalent() {
        return equivalent;
    }

    public void setEquivalent(boolean equivalent) {
        this.equivalent = equivalent;
    }

    public boolean isDuplicated() {
        return duplicated;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public int getnEquivRoot() {
        return nEquivRoot;
    }

    public void setnEquivRoot(int nEquivRoot) {
        this.nEquivRoot = nEquivRoot;
    }
}
