package GUI.MyComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import GUI.keyActions;

public class infoDialog extends JDialog 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3453716688958678189L;
	private JTextPane textAreaInfo = null;

	public infoDialog( String text ) 
	{
		this.init( text );
	}
	
	public infoDialog( Frame winOwner, String text ) 
	{
		super( winOwner );
		this.init( text );
	}
	
	public infoDialog( Window winOwner, String text ) 
	{
		super( winOwner );
		this.init( text );
	}
	
	public infoDialog( Dialog winOwner, String text ) 
	{
		super( winOwner );
		this.init( text );
	}
	
	public infoDialog( Frame winOwner, String text, boolean modal ) 
	{
		super( winOwner, modal );
		this.init( text );
	}
	
	public infoDialog( Dialog winOwner, String text, boolean modal ) 
	{
		super( winOwner, modal );
		this.init( text );
	}
	
	private void init( String text )
	{		
		super.setUndecorated( true );
		super.setLayout( new BorderLayout() );
		super.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		
		super.add( new JScrollPane( this.getTextAreaInfo( text ) ) );			
		
		super.addWindowFocusListener( new WindowFocusListener()
		{				
			@Override
			public void windowLostFocus(WindowEvent e) 
			{
				((JDialog)e.getSource()).dispose();
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) 
			{					
			}
		});
		

		super.getRootPane().registerKeyboardAction( keyActions.getEscapeCloseWindows( "EscapeCloseWindow"), 
														KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), 
														JComponent.WHEN_IN_FOCUSED_WINDOW );
	}
	
	private JTextPane getTextAreaInfo( String text )
	{
		if( this.textAreaInfo == null )
		{
			this.textAreaInfo = new JTextPane();
			this.textAreaInfo.setEnabled(true);
			this.textAreaInfo.setEditable(false);						
			this.textAreaInfo.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
			
			FontMetrics fm = this.textAreaInfo.getFontMetrics( this.textAreaInfo.getFont() );
			
			MutableAttributeSet set = new SimpleAttributeSet();
			StyleConstants.setSpaceBelow( set, fm.getHeight() * 0.5F );
			
			this.textAreaInfo.setParagraphAttributes( set, false );
						
			this.textAreaInfo.setText( text );
		}
		
		return this.textAreaInfo;
	}
	
}
