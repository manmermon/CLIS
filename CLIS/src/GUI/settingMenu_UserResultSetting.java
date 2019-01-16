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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import Activities.ActivityRegister;
import Activities.IActivity;
import Config.ConfigApp;
import Config.Results.ConfigPropertiesResults;
import Config.Results.ConfigResults;
import GUI.MyComponents.SpinnerNumberCellEditor;

public class settingMenu_UserResultSetting extends JDialog 
{
	private static final long serialVersionUID = -816047425109610764L;

	private ConfigResults cfgTime = null;
	private List< JTable > tables = new ArrayList< JTable >();
	
	private Window winOwner;
	
	private boolean firstTimeLoad = true;
	
	public settingMenu_UserResultSetting( Window owner, String title )
	{
		super( owner );
		init( title );
	}
	
	private void init( String title)
	{
		super.setTitle( title );
				
		super.getRootPane().registerKeyboardAction( keyActions.getEscapeCloseWindows( "EscapeCloseWindow"), 
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		//super.setLayout( new BorderLayout() );
		//this.reLoadTables();
		//super.add( this.createConfigUsersResults(), BorderLayout.CENTER );
		super.setContentPane( createConfigUsersResults() );
		
		super.addWindowListener( new WindowAdapter() 
		{	
			@Override
			public void windowDeactivated(java.awt.event.WindowEvent e) 
			{
				((JDialog)e.getSource()).dispose();
			}
			
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) 
			{
				updateTables();
				
				tables.clear();
				
				saveUserResults();
				
				getParent().setEnabled( true );
				dispose();
			}			
		});		
	}
	
	private void updateTables()
	{
		for( JTable t : this.tables )
		{
			TableCellEditor ed = t.getCellEditor();
			
			if( ed != null )
			{						
				ed.stopCellEditing();
			}
			else
			{
				t.transferFocus();
			}
		}
		
		this.tables.clear();
		
		saveUserResults();
	}
		
	private void saveUserResults()
	{
		try 
		{
			if( this.cfgTime != null )
			{
				this.cfgTime.saveConfigResults( ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString()  );
				this.cfgTime = null;
			}
		}
		catch (Exception e1) 
		{
			String errorMsg = "Problem during data saving.";
			//ErrorException.showErrorException( getOwner(), errorMsg, "Error", ErrorException.ERROR_MESSAGE );
			ErrorException.showErrorException( super.getParent(), errorMsg, "Error", ErrorException.ERROR_MESSAGE );
		}
	}
		
	private TableModelListener getTableModelListener( final JTable table, final ConfigPropertiesResults prop )
	{
		return new TableModelListener() 
		{			
			@Override
			public void tableChanged(TableModelEvent e) 
			{				
				int row = table.getSelectedRow();				
				int col = table.getSelectedColumn();
											
					if( table.isEditing() && row >= 0 && col > 0 )
					{
						try
						{
							Long value = new Long( table.getModel().getValueAt( row, col ).toString() );
								
							switch( row )
							{
								case( 0 ):						
								{	
									prop.getNumberTests()[ col - 1 ] = value.intValue();
									prop.getIsDefaultVaule()[ col - 1 ] = false;
									
									break;
								}
								case( 1 ):						
								{
									prop.getTestTimes()[ col - 1 ] = value;
									prop.getIsDefaultVaule()[ col - 1 ] = false;
									
									break;
								}
								case( 2 ):						
								{									
									prop.getNumberCorrectAnswers()[ col - 1 ] = value.intValue();
									prop.getIsDefaultVaule()[ col - 1 ] = false;
									
									break;
								}
								default:
								{
									prop.getAnswerTimes()[ col - 1 ] = value;
									prop.getIsDefaultVaule()[ col - 1 ] = false;
									
									break;
								}
							}													
						}
						catch( NumberFormatException ex )
						{
							String errorMsg = "" + ex.getMessage();
							if( errorMsg.isEmpty() )
							{
								errorMsg += ex.getCause();
							}
							
							//ErrorException.showErrorException( getOwner(), errorMsg, "Error", ErrorException.ERROR_MESSAGE );
							ErrorException.showErrorException( getParent(), errorMsg, "Error", ErrorException.ERROR_MESSAGE );
						}
					}
				}
			};
	}
	
	private JTabbedPane createConfigUsersResults()
	{	
		String pathFile = ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString();
		this.cfgTime = new ConfigResults( pathFile );
		
		if( !this.cfgTime.LoadDataOK() && !this.firstTimeLoad )
		{
			ErrorException.showErrorException( this.winOwner, "Problem in user's Result file: " + pathFile, 
												"Load problem", ErrorException.WARNING_MESSAGE ); 
		}
		
		this.firstTimeLoad = false;
		
		JTabbedPane jPaneTask = new JTabbedPane( );
		//jPaneTask.setLayout( new BoxLayout( jPaneTask, BoxLayout.Y_AXIS ) );
			
		//for( int k = 0; k < IActivity.activities.length - 1; k++ )
		String[] tasks = ActivityRegister.getActivitiesID();
		for( int k = 0; k < tasks.length; k++ )
		{
			//String task = IActivity.activities[ k ];
			String task = tasks[ k ];
			ConfigPropertiesResults res = this.cfgTime.getConfigProperties( task );
			
			if( res != null )
			{
				final TableModel content = this.createTablemodel( res );
				
				final JTable table = this.getCreateJTable();
				
				this.tables.add( table );
				
				table.setModel( content );
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.getTableHeader().setReorderingAllowed( false );
				table.setFillsViewportHeight(true);
				table.setCellSelectionEnabled( false );			
				table.setRowSelectionAllowed( false );
				table.setColumnSelectionAllowed( false );
				table.setEnabled( false );
				table.setBackground( new Color( 215, 215, 215 ) );
								
				for( int i = 1; i < content.getColumnCount(); i++)
				{	
					JSpinner userResults = new JSpinner( new SpinnerNumberModel( 1, 1, Integer.MAX_VALUE, 1 ) );
					userResults.setEditor( new JSpinner.NumberEditor( userResults, "#") );		
					userResults.addMouseWheelListener( new MouseWheelListener() 
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
							
					TableColumn col = table.getColumnModel().getColumn( i );
					col.setCellEditor( new SpinnerNumberCellEditor( userResults ) );			
				}
			
			
				content.addTableModelListener( this.getTableModelListener( table, res ) );
			
				table.setPreferredScrollableViewportSize( table.getPreferredSize() );
				table.setFillsViewportHeight( true );
				JScrollPane scrollTable = new JScrollPane( table );
				scrollTable.validate();
				
				final JCheckBox btnEdit = new JCheckBox( "Edit" );
				btnEdit.addActionListener( new ActionListener() 
					{				
						public void actionPerformed(ActionEvent e) 
						{	
							int r = table.getSelectedRow();
							int c = table.getSelectedColumn();
							
							if( table.isEditing() && r >= 0 && c >= 0 )
							{
								TableCellEditor cell = table.getCellEditor( r, c );
								if( !cell.stopCellEditing() )
								{
									cell.cancelCellEditing();
								}
							}
						
							table.setEnabled( !table.isEnabled() );
							if( table.isEnabled() )
							{
								table.setBackground( Color.WHITE );
							}
							else
							{
								table.setBackground( new Color( 215, 215, 215 ) );
							}
						}
					});	
				
				JPanel panel_1 = new JPanel();
				panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				panel_1.add( btnEdit );
				
				JPanel panel = new JPanel();
				panel.setLayout( new BorderLayout(0, 0) );
				panel.add( panel_1, BorderLayout.NORTH );
				panel.add( scrollTable, BorderLayout.CENTER );
				//panel.setBorder( BorderFactory.createTitledBorder( task ) );
				
				//jPaneTask.add( panel );
				jPaneTask.addTab( task, null, panel );
			}
		}			
		
		JScrollPane jScrollPaneContentUsersResults = new JScrollPane();		
		jScrollPaneContentUsersResults.setViewportView( jPaneTask );
				
		return jPaneTask;
	}
	
	private JTable getCreateJTable()
	{
		JTable t =  new JTable()
						{
							private static final long serialVersionUID = 1L;
			
							//Implement table cell tool tips.           
				            public String getToolTipText( MouseEvent e) 
				            {
				                String tip = null;
				                Point p = e.getPoint();
				                int rowIndex = rowAtPoint(p);
				                int colIndex = columnAtPoint(p);
				
				                try 
				                {
				                    tip = getValueAt(rowIndex, colIndex).toString();
				                }
				                catch ( RuntimeException e1 )
				                {
				                    //catch null pointer exception if mouse is over an empty line
				                }
				
				                return tip;
				            }				            
				        };
				        
		t.setDefaultRenderer( Object.class, new DefaultTableCellRenderer(){
			
												public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
											    {
											        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

											        if( !table.isCellEditable( row, column ) )
											        {											        	
											        	cellComponent.setBackground( new Color(235, 235, 235) );
											        }
											        else
											        {
											        	cellComponent.setBackground( Color.WHITE );
											        }
											        
											        return cellComponent;
											    }
											});
		
		t.getTableHeader().setReorderingAllowed( false );
		
		return t;
	}

	private TableModel createTablemodel( ConfigPropertiesResults c )
	{					
		String[] headerTable = new String[ IActivity.NUMBER_DIFICUL_LEVELS + 1];
		headerTable[ 0 ] = "Fields";
		
		Class[] colTypes = new Class[ headerTable.length ];
		colTypes[ 0 ] = String.class;
						
		for( int i  = 1; i < colTypes.length; i++ )
		{
			headerTable[ i ] = "Level " + 1;
			colTypes[ i ] = Long.class;
		};
		
		final Class[] cTypes = Arrays.copyOf( colTypes, colTypes.length );
		
		TableModel tm =  new DefaultTableModel( this.getCells( 4, headerTable.length, c ), headerTable )
							{
								private static final long serialVersionUID = 1L;
								
								Class[] columnTypes = cTypes;
								
								public Class getColumnClass( int columnIndex ) 
								{
									return columnTypes[columnIndex];
								}
																
								public boolean isCellEditable(int row, int column) 
								{
									boolean editable = true;
									
									if( column == 0 )
									{
										editable = false;
									}
									
									return editable;
								}
							};
		
		return tm;
	}
	
	private Object[][] getCells( int rows, int cols, ConfigPropertiesResults c )
	{			
		Object[][] cells = new Object[ rows ][ cols ];
		
		String[] tasks = ActivityRegister.getActivitiesID();
		
		//for( int k = 0; k < IActivity.activities.length - 1; k++ )
		for( int k = 0; k < tasks.length; k++ )
		{	
			//String task = IActivity.activities[ k ];
			String task = tasks[ k ];
			
			for( int i = 0; i < 4 ; i++ )
			{
				Object[]  r = new Object[ cols ];
				switch( i )
				{
					case( 0 ):						
					{
						r[ 0 ] = "" + ConfigResults.NUM_TESTS;
						for( int j = 1; j < cols; j++ )
						{	
							r[ j ] = (int)c.getNumberTest( j - 1 );
						}		
						
						break;
					}
					case( 1 ):						
					{
						r[ 0 ] = "" + ConfigResults.TEST_TIME;
						for( int j = 1; j < cols; j++ )
						{	
							r[ j ] = (int)c.getTestTime( j - 1 );
						}		
						break;
					}
					case( 2 ):						
					{
						r[ 0 ] = "" + ConfigResults.NUM_CORRECT_ANSWER;
						for( int j = 1; j < cols; j++ )
						{	
							r[ j ] = (int)c.getNumberCorrectAnswer( j - 1 );
						}		
						break;
					}
					default:
					{
						r[ 0 ] = "" + ConfigResults.ANSWER_TIME;
						for( int j = 1; j < cols; j++ )
						{	
							r[ j ] = (int)c.getAnswerTime( j - 1 );
						}		
						break;
					}
				}
				
				cells[ i ] = r;
			}
		}
		
		return cells;
	}
}
