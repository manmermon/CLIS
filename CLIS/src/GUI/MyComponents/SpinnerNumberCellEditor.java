package GUI.MyComponents;
import java.awt.Component;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class SpinnerNumberCellEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = 1L;
	private JSpinner spinner;

    public SpinnerNumberCellEditor( JSpinner sp )
    {
    	super( new JTextField() );    	    	
    	spinner = sp;
    }
    
    public Component getTableCellEditorComponent(
    	JTable table, Object value, boolean isSelected, int row, int column)
    {
    	try
    	{
    		spinner.setValue( value );
    	}
    	catch( Exception e)
    	{
    		Comparable v =((SpinnerNumberModel)spinner.getModel()).getMinimum();
    		spinner.setValue( v );
    	}
    	
    	return spinner;
    }
    
    public Object getCellEditorValue()
    {
    	try 
    	{
			spinner.commitEdit();
		}
    	catch (ParseException e) 
    	{}
    	
    	return spinner.getValue();
    }    
}