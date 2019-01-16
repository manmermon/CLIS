/*
 * Copyright 2011-2018 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import Config.ConfigApp;
import Plugin.PluginConfiguration.PluginConfig;
import Plugin.PluginConfiguration.PluginParameter;

public class settingMenu_PluginConfig extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String PANEL_TITLE = "Plugin Settings";
	
	//JDIALOG
	private settingMenu_TaskSetting winOwner = null;
	
	private JScrollPane scroll = null;
	
	private JPanel parementerPanel = null;
	
	private PluginConfig plgCfg = null;
		
	public settingMenu_PluginConfig( settingMenu_TaskSetting owner )
	{
		this.winOwner = owner;
		
		super.setLayout( new BorderLayout() );
		super.add( this.getJScrollPanel(), BorderLayout.CENTER );
	}
	
	private JScrollPane getJScrollPanel()
	{
		if( this.scroll == null )
		{
			this.scroll = new JScrollPane();
			this.scroll.setViewportView( this.getJPanelPluginParamenters() );
			
			/*
			Dimension d = this.scroll.getPreferredSize();
			d.height *= 3;
			this.scroll.setPreferredSize( d );
			*/			
		}
		
		return this.scroll;
	}
	
	private JPanel getJPanelPluginParamenters()
	{
		if( this.parementerPanel == null )
		{
			this.parementerPanel = new JPanel();
		}
		
		return this.parementerPanel;
	}
	
	public void setPluginConfig( PluginConfig plugCfg )
	{
		this.getJPanelPluginParamenters().setVisible( false );
		
		if( this.plgCfg != null
				&& !this.plgCfg.equals( plugCfg ) )
		{
			this.parementerPanel.removeAll(); 
		}
		
		this.plgCfg = plugCfg;
		
		if( plugCfg != null && this.parementerPanel.getComponentCount() == 0 )
		{
			
			BorderLayout ly = new BorderLayout();			
			this.parementerPanel.setLayout( ly );			
			
			JPanel aux1 = new JPanel();
			aux1.setLayout( new BoxLayout( aux1, BoxLayout.Y_AXIS ) );
			
			JPanel aux2 = null;
			
			int col = 0;
			for( PluginParameter par : plugCfg.getParameters() )
			{				
				if( aux2 == null )
				{
					aux2 = new JPanel();
					FlowLayout ly2 = new FlowLayout() ;
					ly2.setAlignment( FlowLayout.LEFT );
					aux2.setLayout( ly2 ); // new BoxLayout( aux2, BoxLayout.X_AXIS ));
				}
				String cap = par.getCaption( ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() );				
				JLabel caption = new JLabel( " " + cap + " " ); 
				
				aux2.add( caption );
				col++;
								
				Component comp = getPluginParameterComponent( par);
				aux2.add( comp );
				col++ ;
				
				col = col % 4;
				
				if( col == 0 )
				{
					aux1.add( aux2 );
					aux2 = null;
				}
			}
			
			if( aux2 != null )
			{
				aux1.add( aux2 );
			}
			
			this.parementerPanel.add( aux1, BorderLayout.NORTH );
		}
		
		this.parementerPanel.setVisible( true );
	}
	
	private Component getPluginParameterComponent( final PluginParameter par )
	{
		Component c = null;
		
		if( par != null )
		{
			final Object parValue = par.getValue();
			
			
			if( parValue instanceof Boolean )
			{
				JCheckBox check = new JCheckBox();
				check.setSelected( (Boolean)parValue );
				
				check.addItemListener( new ItemListener() 
				{				
					@Override
					public void itemStateChanged(ItemEvent e) 
					{
						par.setValue( ((JCheckBox)e.getSource()).isSelected() );
					}
				});
				
				c = check;
			}
			else if( parValue instanceof String )
			{
				JTextField text = new JTextField( (String)parValue );
				text.getDocument().addDocumentListener( new DocumentListener() 
				{				
					@Override
					public void removeUpdate(DocumentEvent e) 
					{
						updateDoc( e );
					}
					
					@Override
					public void insertUpdate(DocumentEvent e) 
					{
						updateDoc( e );
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) 
					{
						updateDoc( e );
					}
					
					private void updateDoc( DocumentEvent e )
					{
						try 
						{
							par.setValue( e.getDocument().getText( 0, e.getDocument().getLength() ) );
						}
						catch (BadLocationException e1) 
						{
							e1.printStackTrace();
						}
					}
				});
				c = text;
			}
			else
			{	
				JSpinner sp = new JSpinner();
				
				SpinnerNumberModel spModel = null;
							
				if( parValue instanceof Float )
				{
					spModel = new SpinnerNumberModel( ((Float)parValue).floatValue()
														, Float.MIN_VALUE
														, Float.MAX_VALUE
														, 1.0F );
				}
				else if( parValue instanceof Double )
				{
					spModel = new SpinnerNumberModel( ((Double)parValue).doubleValue()
														, Double.MIN_VALUE
														, Double.MAX_VALUE
														, 1.0D );
				}
				else if( parValue instanceof Byte )
				{				
					spModel = new SpinnerNumberModel( ((Byte)parValue).byteValue()
														, Byte.MIN_VALUE 
														, Byte.MAX_VALUE
														, (byte)1 );
				}
				else if( parValue instanceof Short )
				{				
					spModel = new SpinnerNumberModel( ((Short)parValue).shortValue()
														, Short.MIN_VALUE 
														, Short.MAX_VALUE
														, (short)1 );
				}
				else if( parValue instanceof Integer )
				{
					
					spModel = new SpinnerNumberModel( ((Integer)parValue).intValue()
														, Integer.MIN_VALUE 
														, Integer.MAX_VALUE
														, 1 );
				}
				else
				{
					
					spModel = new SpinnerNumberModel( ((Long)parValue).longValue()
														, Long.MIN_VALUE 
														, Long.MAX_VALUE
														, 1L );
				}
							
				sp.setModel( spModel );
				
				sp.addFocusListener( new FocusAdapter() 
				{
					@Override
					public void focusLost(FocusEvent arg0) 
					{
						try 
						{
							((JSpinner)arg0.getSource()).commitEdit();
						} 
						catch (ParseException e) 
						{}
					}
				});
				
				sp.addChangeListener( new ChangeListener() 
				{				
					@Override
					public void stateChanged(ChangeEvent e) 
					{						
						try 
						{
							Double newValue = (Double)((JSpinner)e.getSource()).getValue();
							
							if( parValue instanceof Short )
							{
								par.setValue( newValue.shortValue() );
							}
							else if( parValue instanceof Byte )
							{
								par.setValue( newValue.byteValue() );
							}
							else if( parValue instanceof Integer )
							{
								par.setValue( newValue.intValue() );
							}
							else if( parValue instanceof Long )
							{
								par.setValue( newValue.longValue() );
							}
							else if( parValue instanceof Float )
							{
								par.setValue( newValue.floatValue() );
							}
							else
							{
								par.setValue( newValue );
							}
												
						} 
						catch ( Exception e1) 
						{
							e1.printStackTrace();
						}
						
					}
				});
				
				sp.addMouseWheelListener( new MouseWheelListener() 
				{				
					@Override
					public void mouseWheelMoved(MouseWheelEvent e) 
					{
						if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
						{
							try
							{
								JSpinner sp = (JSpinner)e.getSource();
								
								int d = e.getWheelRotation();
								
								if( d > 0 )
								{
									sp.setValue( sp.getModel().getPreviousValue() );
								}
								else
								{
									sp.setValue( sp.getModel().getNextValue() );
								}
							}
							catch( IllegalArgumentException ex )
							{
								
							}
						}
					}
				});
				
				c = sp;
			}
		}
		
		return c;
	}
}
