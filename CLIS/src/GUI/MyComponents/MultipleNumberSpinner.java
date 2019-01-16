package GUI.MyComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class MultipleNumberSpinner extends JPanel 
{	
	private static final long serialVersionUID = 1L;
	
	public static final String WEST = BorderLayout.WEST;
	public static final String EAST = BorderLayout.EAST;
	
	public static final int LEFT = JTextField.LEFT;
	public static final int RIGHT = JTextField.RIGHT;
	public static final int CENTER = JTextField.CENTER;
	public static final int LEADING = JTextField.LEADING;
	public static final int TRAILING = JTextField.TRAILING;
	
	public static final int VERTICAL = BoxLayout.Y_AXIS;
	public static final int HORIZONTAL = BoxLayout.X_AXIS;
	
	public static final int INFINITY = -1;
	
	private NumberRange range = null;
	
	private double steps = 1D;
	
	private List< Double > valuesList = null;
	
	private String separator = ";";
		
	private Double defaultValue = 0D;
	
	private boolean realNumber = true;
	
	private JTextField textField = null;
	private JPanel panelBts = null;
	private JButton bt1 = null;
	private JButton bt2 = null;
	
	private int textAlignment = RIGHT;
	private String btsPosition = WEST;
	private int btsAlignment = VERTICAL;	
	
	public MultipleNumberSpinner()
	{
		super( );
		super.setLayout( new BorderLayout() );
		
		this.textField = new JTextField();
		this.textField.setHorizontalAlignment( this.textAlignment );
		Font f = this.textField.getFont();
		Font f2 = new Font( f.getName(), Font.BOLD, f.getSize() );
		this.textField.setFont( f2 );
		
		super.add( this.textField, BorderLayout.CENTER );
		
		this.panelBts = new JPanel();
		this.panelBts.setLayout( new BorderLayout() );
		Dimension d = this.textField.getPreferredSize();
		d.width = 15;
		this.panelBts.setPreferredSize( d );
		
		this.bt1 = new JButton( "+" );
		
		Dimension d2 = this.bt1.getPreferredSize();
		d2.height = (int)( d.height / 2 );
		
		this.bt1.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
		this.bt1.setPreferredSize( d2 );
		f = this.bt1.getFont();
		f2 = new Font( f.getName(), Font.BOLD, f.getSize() + 1 );
		this.bt1.setFont( f2 );
		this.bt1.setFocusable( false );
		this.bt1.addActionListener( new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sumSteps( 1 );
			}
		});
		
		this.bt2 = new JButton( "-" );
		this.bt2.setPreferredSize( d2 );
		this.bt2.setFocusable( false );
		this.bt2.setFont( f2 );
		this.bt2.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
		this.bt2.addActionListener( new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sumSteps( -1 );
			}
		});
		
		if( this.btsAlignment == VERTICAL )
		{
			this.panelBts.add( this.bt1, BorderLayout.NORTH );
			this.panelBts.add( this.bt2, BorderLayout.SOUTH );
		}
		else			
		{
			this.panelBts.add( this.bt1, BorderLayout.WEST );
			this.panelBts.add( this.bt2, BorderLayout.EAST );
		}
		
		super.add( this.panelBts, this.btsPosition );
		
		this.valuesList = new ArrayList<Double>();
		this.valuesList.add( this.defaultValue );
		
		updateValueListText();
		
		this.range = new NumberRange( Double.MIN_VALUE, Double.MAX_VALUE );
		
		this.textField.addFocusListener(new FocusAdapter() 
		{
			private String oldValue;
			@Override
			public void focusGained(FocusEvent e) 
			{
				if( !( e.getOppositeComponent() instanceof JRootPane ) )
				{
					oldValue = ((JTextField)e.getSource()).getText();
				}
			}
			
			@Override
			public void focusLost(FocusEvent arg0) 
			{
				JTextField t = ((JTextField)arg0.getSource());				
				
				if( !t.getText().equals( "" ) )
				{
					List< Number > values = checkValues();
					
					if( !t.getText().equals( oldValue )
							&& !( arg0.getOppositeComponent() instanceof JRootPane ) 
							&& values == null )
					{
						t.setText( oldValue );
					}
					else
					{
						setNumberList( values );
						//valuesList = values;
					}
				}
				else
				{
					//valuesList.clear();
					//valuesList.add( defaultValue );
					setNumberList( null );
				}
				
				updateValueListText();
			}
		});
		
		super.addMouseWheelListener( new MouseWheelListener() 
		{	
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) 
			{
				sumSteps( -e.getWheelRotation()  );
			}
		});		
	}
	
	public  MultipleNumberSpinner( Long min, Long max
									, Long value, Long steps ) 
	{
		this();
		
		if( min != null && max != null )
		{
			this.range = new NumberRange( min, max );			
		}
		else if( min != null )
		{
			this.range = new NumberRange( min, Double.MAX_VALUE );
		}
		else
		{
			this.range = new NumberRange( Double.MIN_VALUE, max );
		}
		
		if( value != null )
		{			
			this.valuesList.clear();
						
			this.valuesList.add( value.doubleValue() );						
		}
		
		if( steps != null )
		{
			this.steps = Math.abs( steps );
		}
		
		this.realNumber = false;
		
		updateValueListText();
	}
	
	public MultipleNumberSpinner( Double min, Double max
									, Double value, Double steps ) 
	{
		this();
				
		if( min != null && max != null )
		{
			this.range = new NumberRange( min, max );			
		}
		else if( min != null )
		{
			this.range = new NumberRange( min, Double.MAX_VALUE );
		}
		else
		{
			this.range = new NumberRange( Double.MIN_VALUE, max );
		}
		
		if( value != null )
		{			
			this.valuesList.clear();
			this.valuesList.add( value );			
		}
		
		if( steps != null )
		{
			this.steps = Math.abs( steps );
		}
		
		updateValueListText();
	}
	
	public List< Number > getNumberList()
	{
		List v = this.valuesList;
		
		if( !realNumber )
		{
			v = new ArrayList< Long >();
			for( Double va : this.valuesList )
			{
				v.add( va.longValue() );
			}
		}
		
		return v;
	}
	
	public void setNumberList( List< Number > values )
	{
		this.valuesList.clear();
		
		double lastValue = this.defaultValue;
		
		if( values != null )
		{	
			Iterator< Number > it = values.iterator();
			
			while( it.hasNext() )
			{
				lastValue = it.next().doubleValue();
				this.valuesList.add( lastValue );
			}
		}
				
		 if( this.valuesList.isEmpty() )
		{
			 this.valuesList.add( this.defaultValue );
		}
		
		updateValueListText();			
	}
	
	public void resetValues( Number value )
	{
		double aux = this.defaultValue;
		
		if( value != null )
		{
			this.defaultValue = value.doubleValue();
		}
		
		this.setNumberList( null );
		
		this.defaultValue = aux;
	}
		
	private void updateValueListText()
	{
		this.textField.setText( "" );
		
		String text = "";
		
		for( Double value : this.valuesList )
		{	
			if( this.realNumber )
			{
				text += value + ";";			
			}
			else
			{
				text += value.longValue() + ";";
			}
		}
		
		this.textField.setText( text );
	}
	
	private List< Number > checkValues( )
	{
		String patron = "(-?\\d+\\.?\\d*\\s*;?\\s*)*";
		
		List< Number > valuesList = null;
		
		String txt = this.textField.getText();
		boolean ok =  txt.matches( patron );
		
		if( ok )
		{		
			valuesList = new ArrayList< Number >();
						
			txt = txt.replaceAll( "\\s", "" );
			String[] values = txt.split(  this.separator );
			txt = "";
			for( int i = 0; i < values.length && ok ; i++ )
			{
				String value = values[ i ];
				if( !value.isEmpty() )
				{
					Double val = new Double( value );

					ok = this.range.within( val );
					
					if( ok )
					{
						valuesList.add( val );
					}
					else
					{
						valuesList = null;
					}
				}
			}
		}
		
		return valuesList;
	}
	
	private void sumSteps( int Qty )
	{
		for( int i = 0; i < valuesList.size(); i++ )
		{
			Double value = valuesList.get( i );
			
			value += Qty * steps;
			
			if( range.within( value ) )
			{
				valuesList.set( i, value );
			}
		}
		
		updateValueListText();
	}
		
	public void setTextAlignment( int alignment )
	{
		this.textAlignment = alignment;
		
		this.textField.setHorizontalAlignment( this.textAlignment );
	}
	
	public int getTextAlignment( )
	{
		return this.textAlignment;
	}
	
	public void setButtonsPosition( String position )
	{
		this.btsPosition = position;
		
		if( this.btsPosition == EAST )
		{
			this.add( this.panelBts, EAST );
		}
		else
		{
			this.btsPosition = WEST;
			this.add( this.panelBts, WEST );
		}
	}
	
	public String getButtonsPosition()
	{
		return this.btsPosition;
	}
	
	public void setButtonsAlignment( int alignment)
	{
		this.btsAlignment = alignment;
		
		if( this.btsAlignment == VERTICAL )			
		{
			this.panelBts.add( this.bt1, BorderLayout.WEST );
			this.panelBts.add( this.bt2, BorderLayout.EAST );
		}
		else
		{
			this.panelBts.add( this.bt1, BorderLayout.NORTH );
			this.panelBts.add( this.bt2, BorderLayout.SOUTH );
		}
	}
	
	public int getButtonsAlignment()
	{
		return this.btsAlignment;
	}
	
	public void addFocusListener( FocusListener l )
	{
		this.textField.addFocusListener( l );
	}
}
