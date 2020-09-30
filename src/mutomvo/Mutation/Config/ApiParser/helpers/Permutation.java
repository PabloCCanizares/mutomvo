/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser.helpers;

import java.util.LinkedList;

/**
 *
 * @author Pablo C. Cañizares 
 */
public class Permutation {
    
    LinkedList<LinkedList<Integer>> oList;
    
    public Permutation()
    {
        oList= new LinkedList<LinkedList<Integer>>();
    }
    void permute(LinkedList<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            System.out.println(java.util.Arrays.toString(arr.toArray()));
            LinkedList<Integer> copy;
            copy= (LinkedList<Integer>) arr.clone();
            oList.add(copy);
        }
    }
    
    void resetPerm()
    {
        oList.clear();
    }

    LinkedList<LinkedList<Integer>> getList() {
        return oList;
    }
}
