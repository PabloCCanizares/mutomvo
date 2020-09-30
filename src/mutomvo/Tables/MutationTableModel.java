package mutomvo.Tables;

import javax.swing.table.DefaultTableModel;

/**
 * Model for mutation table
 * 
 * @author Alberto Núñez Covarrubias
 */
public class MutationTableModel extends DefaultTableModel{
    
    /**
     * Controls is the selected cell is editable or not
     * 
     * @param row Row number
     * @param column Column number
     * @return True if this cell is editable and False in other case
     */
    @Override
    public boolean isCellEditable(int row, int column){
    
        boolean result;
        
        if (column == 2)
            result = true;
        else
            result = false;
        
        return result;
    }
    
     public Class getColumnClass(int column) {
         
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            default:
                return Boolean.class;
        }
      }
}
