/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mutomvo.TabbedPanels.dataClasses;

/**
 * Class that represent a mutant operator.
 * 
 * @author cana
 */
public class MutantOperator implements java.io.Serializable {
    
    private String acronym;
    private String description;
    private boolean isSelected;
    
   

    public MutantOperator(){
    }
    
    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
