/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package mutomvo.Tables;
/*
 * TableSelectionDemo.java requires no other files.
 */

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.LinkedList;

public class TableLinesSelection extends JPanel 
                                implements ActionListener { 
    private JTable table;
    private JCheckBox rowCheck;
    private JCheckBox columnCheck;
    private JCheckBox cellCheck;
    MyTableModel tableModel;
    private ButtonGroup buttonGroup;
    private JTextArea output;
    
    private JTextField textLine;
    private JTextField comment;
            
    JPanel buttonsPanel;
    JPanel tablePanel;
    JPanel textAreaPanel;
    
    LinkedList<LinkedList<String>> outputList;
    
    private int nInsertedElements;
    public TableLinesSelection()  {
        
        //Creating sub panels
        buttonsPanel = new JPanel();
        tablePanel = new JPanel();
        textAreaPanel = new JPanel(new GridLayout(2,2));
        
        //Creating the table
        tableModel = new MyTableModel();
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(new RowListener());
        table.getColumnModel().getSelectionModel().
            addListSelectionListener(new ColumnListener());
        
        tablePanel.add(new JScrollPane(table));
        buttonsPanel.add(new JLabel("Action"));

        addButton("Add");
        addButton("Delete");
        addButton("Select All");
        addButton("Save");
        
        //Now adding text boxes
        textLine = new JTextField("", 1);
        comment =  new JTextField("", 1);
        textAreaPanel.add(new JLabel("Line number"));
        textAreaPanel.add(new JLabel("Comment"));
        textAreaPanel.add(textLine);
        textAreaPanel.add(comment);
        textAreaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(tablePanel);
        add(buttonsPanel);
        add(textAreaPanel);
        
        output = new JTextArea(5, 40);
        output.setEditable(true);
        //add(new JScrollPane(output));

    }



    private JCheckBox addCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.addActionListener(this);
        add(checkBox);
        return checkBox;
    }

    private JRadioButton addRadio(String text) {
        JRadioButton b = new JRadioButton(text);
        b.addActionListener(this);
        buttonGroup.add(b);
        add(b);
        return b;
    }
    private JButton addButton(String text) {
        JButton b = new JButton(text);
        b.addActionListener(this);
       // buttonGroup.add(b);
        buttonsPanel.add(b);
        return b;
    }
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        //Cell selection is disabled in Multiple Interval Selection
        //mode. The enabled state of cellCheck is a convenient flag
        //for this status.
        if ("Row Selection" == command) {
            table.setRowSelectionAllowed(rowCheck.isSelected());
            //In MIS mode, column selection allowed must be the
            //opposite of row selection allowed.
            if (!cellCheck.isEnabled()) {
                table.setColumnSelectionAllowed(!rowCheck.isSelected());
            }
        } else if ("Column Selection" == command) {
            table.setColumnSelectionAllowed(columnCheck.isSelected());
            //In MIS mode, row selection allowed must be the
            //opposite of column selection allowed.
            if (!cellCheck.isEnabled()) {
                table.setRowSelectionAllowed(!columnCheck.isSelected());
            }
        } else if ("Cell Selection" == command) {
            table.setCellSelectionEnabled(cellCheck.isSelected());
        } else if ("Multiple Interval Selection" == command) { 
            table.setSelectionMode(
                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            //If cell selection is on, turn it off.
            if (cellCheck.isSelected()) {
                cellCheck.setSelected(false);
                table.setCellSelectionEnabled(false);
            }
            //And don't let it be turned back on.
            cellCheck.setEnabled(false);
        } else if ("Single Interval Selection" == command) {
            table.setSelectionMode(
                    ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            //Cell selection is ok in this mode.
            cellCheck.setEnabled(true);
        } else if ("Single Selection" == command) {
            table.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION);
            //Cell selection is ok in this mode.
            cellCheck.setEnabled(true);
        }
         else if ("Add" == command) {
             String strTextLine;
             String strComment;
             strTextLine = textLine.getText();
             strComment= comment.getText();
             
             //The line number must be an integer: E.g. 23
             //OR an interval: E.g. 1-10
             
             //Insert a new element
             table.setValueAt(strTextLine, nInsertedElements, 0);
             table.setValueAt(strComment, nInsertedElements, 1);
             table.setValueAt(true, nInsertedElements, 2);
            
             nInsertedElements++;
        }
        else if ("Delete" == command) {
             //Delete the selected items
            deleteLines();
        }
       else if ("Select All" == command) {
             //Select all items
           selectAll();
        }        
       else if ("Save" == command) {
             //Save the current configuration
           saveConfig();
        }         
    }
    
    private void outputSelection() {
        output.append(String.format("Lead: %d, %d. ",
                    table.getSelectionModel().getLeadSelectionIndex(),
                    table.getColumnModel().getSelectionModel().
                        getLeadSelectionIndex()));
        output.append("Rows:");
        for (int c : table.getSelectedRows()) {
            output.append(String.format(" %d", c));
        }
        output.append(". Columns:");
        for (int c : table.getSelectedColumns()) {
            output.append(String.format(" %d", c));
        }
        output.append(".\n");
    }

    private void deleteLines() {
        tableModel.deleteLines();
    }

    private void saveConfig() {
        outputList = tableModel.getOutputList();
    }

    private void selectAll() {
        tableModel.selectAll();
    }

    public LinkedList<LinkedList<String>> getFilterLinesList() {
        LinkedList<LinkedList<String>> retList;
        
        retList = null;
        
        if(tableModel != null)
            retList = tableModel.getOutputList();
        
        return retList;
    }

    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            output.append("ROW SELECTION EVENT. ");
            outputSelection();
        }
    }

    private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            output.append("COLUMN SELECTION EVENT. ");
            outputSelection();
        }
    }

    public void resetFilteredLines()
    {
        if(tableModel != null)
        {
            tableModel.deleteLines();
        }
    }
    public void insertData(LinkedList<LinkedList<String>> l )
    {
        try
        {
            LinkedList<String> subList;
            int nLine;
            String strComment, strLine, strSelected;
            boolean bSelected;
            
            if(l!= null)
            {
                for(int i=0;i<l.size();i++)
                {
                    subList = l.get(i);

                    if(subList.size()>=3)
                    {
                        strLine = subList.get(0);                
                        strComment = subList.get(1);
                        strSelected = subList.get(2);

                        tableModel.insertNewElementString(strLine, strComment, strSelected);
                    }

                }
            }
            
            
        }catch(Exception e)
        {
        
        }
    }
    
    class MyTableModel extends AbstractTableModel {
        private final int MAX_COLS=3;
        private final int MAX_ROWS=1000;
        
        private String[] columnNames = {"Line",
                                        "Comment",
                                        "Enabled"};
               
       private  LinkedList<LinkedList<Object>> data;
       private  Object[][] data3 = new Object [MAX_ROWS][MAX_COLS];        
        private Object[][] data2 = {
	    {"1", "", new Boolean(false)},
            {"1-35", "", new Boolean(true)},
            {"40", "", new Boolean(true)},
        };

        public MyTableModel()
        {
            data = new LinkedList<LinkedList<Object>>();
      
         /* 
            insertNewElement(1,"",true);
            insertNewElement(2,"",true);
            insertNewElement(3,"",true);             
         */
        }
        
        public void insertNewElementString(String strLine, String comment, String strEnabled)
        {
           LinkedList<Object> l =  new LinkedList<Object>();
           boolean bEnabled;
           
           bEnabled = Boolean.parseBoolean(strEnabled);
           l.add(strLine);
           l.add(comment);
           l.add(bEnabled);
           data.add(l);
           
           nInsertedElements++;
        }
                
        public void insertNewElement(int nLine, String comment, boolean bEnabled)
        {
           LinkedList<Object> l =  new LinkedList<Object>();
           
           l.add(nLine);
           l.add(comment);
           l.add(bEnabled);
           data.add(l);
           
           nInsertedElements++;
        }
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            Object oRet = null;
            
            try
            {
                if(data.size() <= col)
                {
                    if(data.get(col).size() <= row)
                    oRet = data.get(row).get(col);
                }
            }catch(Exception c){}

            return data.get(row).get(col);
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            LinkedList<Object> l;
            if(row >= data.size())
            {
                l =  new LinkedList<Object>();
                data.add(l);
                l.add(value);
                l.add("");
                l.add(true);
            }
            else
            {
                l = data.get(row);
                l.set(col, value);
            }           
            fireTableCellUpdated(row, col);
        }

        public void deleteLines() {
            LinkedList<Object> l;
            int nIndexLine = 0;
            for(int i=0;i<data.size();i++)
            {
                l = data.get(i);
                
                if(isSelected(l))
                {
                    data.remove(i);
                    
                    fireTableRowsDeleted(i,i);
                    i--;
                    nInsertedElements--;
                }
                
                nIndexLine++;
            }
        }
        private void selectAll() {

            LinkedList<Object> l;
            int nIndexLine = 0;
            for(int i=0;i<data.size();i++)
            {
                l = data.get(i);
                l.set(2, true);
                
            }

        }
        private boolean isSelected(LinkedList<Object> l) {
            
            boolean bRet = false;
            
            try
            {
                bRet = (Boolean) l.getLast();
            }catch(Exception c)
            {}
            
            return bRet;
        }

        private LinkedList<LinkedList<String>> getOutputList() {
           
            LinkedList<LinkedList<String>> retList;
            LinkedList<Object> l;
            LinkedList<String> sList;
            
            retList = new LinkedList<LinkedList<String>>();
            for(int i=0;i<data.size();i++)
            {                                
                l = data.get(i);
                
                sList  = new LinkedList<String>();

                sList.add(String.valueOf(l.get(0)));
                sList.add(String.valueOf(l.get(1)));
                sList.add(String.valueOf(l.get(2)));

                retList.add(sList);
            }
            return retList;
        }

    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(TableLinesSelection table) {
        //Disable boldface controls.
        UIManager.put("swing.boldMetal", Boolean.FALSE); 

        //Create and set up the window.
        JFrame frame = new JFrame("Disable mutation lines");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    
        frame.setContentPane(table);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void start(TableLinesSelection tableIn){
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        final TableLinesSelection table = tableIn;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(table);
            }
        });    
    }
}
