/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.Mutation.Config.ApiParser.apis;

import java.util.LinkedList;

/**
 *
 * @author Pablo C. Cañizares 
 */
public interface ApiLoader 
{
    boolean loadApi();
    LinkedList<String> getApiList();

    public String getName();
}
