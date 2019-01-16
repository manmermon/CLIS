package GUI.MyComponents;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class NumberedStringListCellRender extends JLabel implements ListCellRenderer
{	
	private static final long serialVersionUID = -1451764110600614238L;

	public NumberedStringListCellRender() 
	{
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, 
													int index, boolean isSelected, 
													boolean cellHasFocus) 
	{
		if( list != null )
		{
			int n = list.getModel().getSize();
			
			if( n > 0 )
			{
				int pos = index + 1;
				
				int maxNumberDigits = (int)( Math.log10( n ) + 1 );
				int numberIndexDigits = (int)( Math.log10( pos ) + 1 );
						
				String t = "<html><font color=rgb(190,190,190)>";
				
				for( int i = maxNumberDigits - numberIndexDigits; i > 0; i-- )
				{
					t += "&nbsp; ";
				}
				
				super.setText( t + pos + "&nbsp; |&nbsp; </font>" + value.toString() + "</html>" );
				 
				if(isSelected) 
				{
					super.setBackground( list.getSelectionBackground() );
					super.setForeground( list.getSelectionForeground() );				
			     }
				else
				{
			        setBackground( list.getBackground() );
			        setForeground( list.getForeground() );
			     }
				 
				 super.setEnabled( list.isEnabled() );
			     super.setFont( list.getFont() );
			     super.setOpaque( true );
			}
		}
			
		return this;
	}
}
