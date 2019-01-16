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
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import Config.ConfigApp;
import Controls.Commands;
import GUI.MyComponents.IPAddressCellEditor;
import GUI.MyComponents.SpinnerNumberCellEditor;
import Sockets.Info.streamSocketInfo;

public class settingMenu_SocketSetting extends JPanel
{	
	private static final long serialVersionUID = 2157401336834064879L;
		
	//PANELS
	private JPanel jPanelTCP_IP;
	private JPanel jPanelServerCommands;
	private JPanel jPanelServerSocket;
	private JPanel jPanelServerControl;
	private JPanel jPanelTableServerSocket;
	private JPanel jPanelClientCommands;
	private JPanel jPanelContentClientSockets;
	private JPanel jPanelControlClientSockets;
	private JPanel jPanelControlClientSockects2;
	private JPanel jPanelTableClientSockets;
	private JPanel jPanelServerContentCommandsValues;
	private JPanel jPanelClientContentCommandsValues;
	private JPanel jPanelInfo;
	
	//SPLITSPANES
	private JSplitPane jPanelCommands;
	
	//TABBEDPANES
	private JTabbedPane jTabPanelClientMessages;
	
	//CHECKBOXES
	private JCheckBox checkServerSocket;
	private JCheckBox checkClientSocket;
	//private JCheckBox chckbxAddingTimeStamp;
	
	//TABLES
	private JTable jTableServerSocket;
	private JTable jTableServerCommandValue;
	private JTable jTableClientSockets;
	private JTable jTableClientCommandValue;
	
	//BUTTONS
	private JButton jBtnNewClientSocket;
	private JButton jBtnDeleteClientSocket;
	private JButton jBtnInfo;
	
	//SCROLLPANES
	//private JScrollPane jScrollPanelCommandLegend;
	//private JScrollPane jScrollPanelTriggeredEventLegend;
	private JScrollPane jScrollPanelClientSockets;
	private JScrollPane jScrollPaneServerCommands;
	private JScrollPane jScrollPaneClientCommands;
	
	//JTEXTPANE
	private JTextPane textAreaInfo;
	
	//TEXTAREAS
	//private JTextArea textAreaCommandLegend;
	//private JTextArea textAreaTriggeredLegend;
	
	//JDIALGO
	private JDialog winInfo;
	
	//Map
	private Map< String, Component > parameters;
	
	//JDIALOG
	private settingMenu_TaskSetting winOwner;
	
	public settingMenu_SocketSetting( settingMenu_TaskSetting owner )//, String title )
	{
		//super( owner );
		this.winOwner = owner;
		
		this.parameters = new HashMap<String, Component>();
		
		this.init( );//title );
		
		/*
		this.setVisible( false );
		
		this.loadConfigValues();
		
		this.setVisible( true );
		*/
		//super.pack();
		super.validate();
	}
	
	protected void loadConfigValues()
	{
		Set< String > IDs = this.parameters.keySet();
		
		for( String id : IDs )
		{
			Component c = this.parameters.get( id );
			c.setVisible( false );			
			
			if( c instanceof JToggleButton )
			{
				((JToggleButton)c).setSelected( (Boolean)ConfigApp.getProperty( id ) );
			}
			else if( c instanceof JTable )
			{
				Map< String, Object[][]> values = (Map< String, Object[][]>)ConfigApp.getProperty( id );
				
				
				Set< String > SOCKETS = new HashSet< String >();
				Map< String, Object[][]> valuesCopy = new HashMap< String, Object[][] >(); 
				for( String k : values.keySet() )
				{
					SOCKETS.add( k );
					valuesCopy.put( k, values.get( k ) );
				}
				
				JTable tableSocket = this.getTableServerSockets();				
				if( id.equals( ConfigApp.CLIENT_SOCKET_TABLE ) )
				{
					tableSocket = this.getTableClientSockets();
				}
				
				DefaultTableModel socketModel = (DefaultTableModel)tableSocket.getModel();
				int nRowBefore = socketModel.getRowCount();
				
				for( int i = nRowBefore - 1 ; i >= 0; i-- )
				{
					socketModel.removeRow( i );
				}
				
				for( String socket : SOCKETS )
				{
					Object[] socketInfo = new Object[3];
					
					System.arraycopy( socket.split( ":" ), 0, socketInfo, 0, 3 );
					socketInfo[ 2 ] = new Integer( socketInfo[ 2 ].toString() );
					
					Object[][] contentCommandTable = valuesCopy.get( socket );
					
					socketModel.addRow( socketInfo );
					
					setCommandTableSetting( id, socket, contentCommandTable );
				}
				
				if( tableSocket.getRowCount() > 0 )
				{
					tableSocket.setRowSelectionInterval( 0, 0 );
				}
				
			}
			
			c.setVisible( true );
		}		
	}
	
	private void init( )//String title )
	{	
		//super.setTitle( title );		
		/*
		super.getRootPane().registerKeyboardAction( keyActions.getEscapeCloseWindows( "EscapeCloseWindow" ), 
													KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), 
													JComponent.WHEN_IN_FOCUSED_WINDOW );
		*/
		
		super.setLayout( new BorderLayout() );
		//TODO
		super.add( getJPanelTCP_IP(), BorderLayout.CENTER );
		//jDialogTCPCfg.pack();
		//jDialogTCPCfg.validate();		
		
		/*
		super.addWindowListener(new WindowAdapter()
		{
			public void windowDeactivated(WindowEvent e) 
			{	
				((JDialog)e.getSource()).dispose();
			}
						
			public void windowClosing(WindowEvent e) 
			{
				((JDialog)e.getSource()).dispose();
			}
		});
		*/		
	}
	
	public void updateTables()
	{
		if( this.jTableServerSocket != null )
		{
			TableCellEditor ed = this.jTableServerSocket.getCellEditor();
			
			if( ed != null )
			{						
				ed.stopCellEditing();
			}
			else
			{
				this.jTableServerSocket.transferFocus();
			}
		}
		
		if( this.jTableServerCommandValue != null )
		{
			TableCellEditor ed = this.jTableServerCommandValue.getCellEditor();
			
			if( ed != null )
			{						
				ed.stopCellEditing();
			}
			else
			{
				this.jTableServerCommandValue.transferFocus();
			}
		}
		
		if( this.jTableClientSockets != null )
		{
			TableCellEditor ed = this.jTableClientSockets.getCellEditor();
			
			if( ed != null )
			{						
				ed.stopCellEditing();
			}
			else
			{
				this.jTableClientSockets.transferFocus();
			}
		}
		
		if( this.jTableClientCommandValue != null )
		{
			TableCellEditor ed = this.jTableClientCommandValue.getCellEditor();
			
			if( ed != null )
			{						
				ed.stopCellEditing();
			}
			else
			{
				this.jTableClientCommandValue.transferFocus();
			}
		}
	}
	
	private JPanel getJPanelTCP_IP() 
	{
		if (jPanelTCP_IP == null) 
		{
			jPanelTCP_IP = new JPanel();
			jPanelTCP_IP.setLayout(new BorderLayout(0, 0));
			//jPanelTCP_IP.add( getJPanelTCP_IP_PORT(), BorderLayout.NORTH);
			jPanelTCP_IP.add( getJPanelInfo(), BorderLayout.NORTH );
			jPanelTCP_IP.add( getJPanelCommands(), BorderLayout.CENTER);
			//jPanelTCP_IP.add( getJPanelCommandLegends(), BorderLayout.SOUTH );
		}
		return jPanelTCP_IP;
	}
	
	private JPanel getJPanelInfo()
	{
		if( this.jPanelInfo == null )
		{
			this.jPanelInfo = new JPanel();
			this.jPanelInfo.setLayout( new FlowLayout( FlowLayout.RIGHT ));
			this.jPanelInfo.add( this.getJButtonInfo() );
		}
		
		return this.jPanelInfo;
	}
	
	private JButton getJButtonInfo()
	{
		if( this.jBtnInfo == null )
		{
			Dimension d = new Dimension( 16, 16 );
			
			BufferedImage image = null;
			/*
			try
			{			
				Icon icon = UIManager.getIcon( "OptionPane.questionIcon" );				
				int w = icon.getIconWidth();
	            int h = icon.getIconHeight();
	            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            GraphicsDevice gd = ge.getDefaultScreenDevice();
	            GraphicsConfiguration gc = gd.getDefaultConfiguration();
	            image = gc.createCompatibleImage( w, h );
	            Graphics2D g = image.createGraphics();
	            icon.paintIcon(null, g, 0, 0);
	            g.dispose();
			}
			catch( Exception e )
			{}
			catch( Error e )
			{				
			}*/
			
			this.jBtnInfo = new JButton( );
			
			if( image != null )
			{
				//this.jBtnInfo.setIcon( new ImageIcon( image.getScaledInstance( d.width, d.height, BufferedImage.SCALE_SMOOTH ) ) );
			}
			else
			{
				this.jBtnInfo.setText( "?" );
				this.jBtnInfo.setBorder( BorderFactory.createSoftBevelBorder( SoftBevelBorder.RAISED ) );
				this.jBtnInfo.setBackground( Color.YELLOW.darker() );
				this.jBtnInfo.setForeground( Color.BLACK );
			}
			
			this.jBtnInfo.setPreferredSize( d );
			
			this.jBtnInfo.addActionListener( new ActionListener()
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JButton b = (JButton)e.getSource();
					
					JDialog w = getInfoWindow();
					//w.pack();
					//w.validate();
					w.setSize( 350, 310 );
					Dimension size = w.getSize();
					Point pos = b.getLocationOnScreen();
					
					Point loc = new Point( pos.x - size.width, pos.y ); 
					
					w.setLocation( loc );
					
					w.setVisible( true );
				}
			});
		}
		
		return this.jBtnInfo;
	}
	
	private JDialog getInfoWindow()
	{
		if( this.winInfo == null )
		{
			this.winInfo = new JDialog( this.winOwner );
			this.winInfo.setUndecorated( true );
			this.winInfo.setLayout( new BorderLayout() );
			this.winInfo.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
			
			this.winInfo.add( new JScrollPane( this.getTextAreaInfo() ) );			
			
			this.winInfo.addWindowFocusListener( new WindowFocusListener()
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
		}
		
		return this.winInfo;
	}
	
	private JTextPane getTextAreaInfo()
	{
		if( this.textAreaInfo == null )
		{
			this.textAreaInfo = new JTextPane();
			this.textAreaInfo.setEnabled(true);
			this.textAreaInfo.setEditable(false);
			//this.textAreaInfo.setLineWrap(true);
			//this.textAreaInfo.setWrapStyleWord(true);
			this.textAreaInfo.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
			
			this.writeCommandLegends( this.textAreaInfo );
		}
		
		return this.textAreaInfo;
	}
	
	private JSplitPane getJPanelCommands() 
	{
		if (jPanelCommands == null) 
		{
			jPanelCommands = new JSplitPane();
			jPanelCommands.setBorder( BorderFactory.createEtchedBorder() );
			//jPanelCommands.setLayout(new BoxLayout(jPanelCommands, BoxLayout.X_AXIS));
			jPanelCommands.setResizeWeight( 0.55 );
			//jPanelCommands.setDividerLocation( 0.5 );
			jPanelCommands.setDividerSize( 5 );
			jPanelCommands.setEnabled( true );
			
			BasicSplitPaneUI splitUI = (BasicSplitPaneUI)jPanelCommands.getUI();
			BasicSplitPaneDivider div = splitUI.getDivider();
			div.setBorder( BorderFactory.createEtchedBorder() );
			
			jPanelCommands.setLeftComponent( getJPanelServerCommands());
			jPanelCommands.setRightComponent( getJPanelClientCommands());
		}
		
		return jPanelCommands;
	}
	
	private JPanel getJPanelClientCommands() 
	{
		if (jPanelClientCommands == null) 
		{
			jPanelClientCommands = new JPanel();

			TitledBorder border = BorderFactory.createTitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Output Messages:" );
			
			jPanelClientCommands.setBorder( border );			
			jPanelClientCommands.setLayout(new BorderLayout(0, 0));
						
			//TODO
			jPanelClientCommands.add(getJPanelTCP_Client(), BorderLayout.NORTH);
			//jPanelClientCommands.add(getJScrollPaneClientCommands(), BorderLayout.CENTER);
			jPanelClientCommands.add( this.getJTabbedPaneClientMessages(), BorderLayout.CENTER );
			//jPanelClientCommands.add( getJScrollPanelTriggeredEventLegend(), BorderLayout.SOUTH );
		}
		return jPanelClientCommands;
	}
	
	private JPanel getJPanelTCP_Client()
	{
		if( jPanelContentClientSockets == null )
		{
			jPanelContentClientSockets = new JPanel();
			jPanelContentClientSockets.setBorder(new EmptyBorder(5, 5, 0, 5));
			jPanelContentClientSockets.setLayout(new BorderLayout(0, 0));
						
			//TODO
			jPanelContentClientSockets.add( getPanelControlClientSockets(), BorderLayout.NORTH);
			jPanelContentClientSockets.add( getScrollPanelClientSockect(), BorderLayout.CENTER);
			//jPanelContentClientSockets.add( getPanelTimeStamp(), BorderLayout.SOUTH);
			
			jPanelContentClientSockets.setVisible( true );
		}
			
		return jPanelContentClientSockets;
	}
	
	private JPanel getPanelControlClientSockets() 
	{
		if ( jPanelControlClientSockets == null) 
		{
			jPanelControlClientSockets = new JPanel();
			jPanelControlClientSockets.setBorder(new EmptyBorder(2, 0, 2, 0));			
			jPanelControlClientSockets.setLayout( new BorderLayout());
						
			jPanelControlClientSockets.add( getJCheckboxClientSocket(), BorderLayout.WEST );
			jPanelControlClientSockets.add( getPanelControlClientSockects2(), BorderLayout.EAST );			
			
		}
		return jPanelControlClientSockets;
	}
	
	private JCheckBox getJCheckboxClientSocket()
	{
		if( checkClientSocket == null )
		{
			final String ID = ConfigApp.IS_SOCKET_CLIENT_ACTIVE;
			
			checkClientSocket = new JCheckBox( "Active" );
			checkClientSocket.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			
			//final Window owner = super.getOwner();
			checkClientSocket.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					JCheckBox c = (JCheckBox)e.getSource();
										
					ConfigApp.setProperty( ID, c.isSelected() );
					
					winOwner.updateSockectStatusText();
				}
			});
			
			this.parameters.put( ID, checkClientSocket );
						
			checkClientSocket.setVisible( true );
		}
		
		return checkClientSocket;
	}
	
	private JPanel getPanelControlClientSockects2()
	{
		if( this.jPanelControlClientSockects2 == null )
		{
			this.jPanelControlClientSockects2 = new JPanel();
			jPanelControlClientSockects2.setBorder(new EmptyBorder(2, 0, 2, 0));
			jPanelControlClientSockects2.setLayout(new BoxLayout( jPanelControlClientSockects2, BoxLayout.X_AXIS));
			
			jPanelControlClientSockects2.add( getBtnNewClientSocket() );
			jPanelControlClientSockects2.add( Box.createRigidArea( new Dimension( 3, 0 ) ) );
			jPanelControlClientSockects2.add( getBtnDeleteClientSockets() );			
		}
		return this.jPanelControlClientSockects2;		
	}
	
	/*
	private JPanel getPanelTimeStamp() 
	{
		if (jPanelTimeStamp == null) 
		{
			jPanelTimeStamp = new JPanel();
			jPanelTimeStamp.setBorder( BorderFactory.createEtchedBorder() );
			jPanelTimeStamp.setLayout(new BoxLayout( jPanelTimeStamp, BoxLayout.Y_AXIS));
			//jPanelTimeStamp.add( getChckbxAddingTimeStamp() );			
		}
		return jPanelTimeStamp;
	}
	
	private JCheckBox getChckbxAddingTimeStamp() 
	{
		if (chckbxAddingTimeStamp == null) 
		{
			final String ID = ConfigApp.IS_TIME_STAMP_ACTIVED;
			
			chckbxAddingTimeStamp = new JCheckBox("Adding Time Stamp");
			chckbxAddingTimeStamp.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			chckbxAddingTimeStamp.setToolTipText( "<html><p><i>\"" + ConfigApp.timeStampHeader + "+(time in microsecond from task start)\"</i> is appended to the end of output message.</p></html>" );
			
			this.chckbxAddingTimeStamp.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					JCheckBox c = (JCheckBox)e.getSource();
					
					ConfigApp.setProperty( ID, c.isSelected() );
				}
			});
			
			this.parameters.put( ID, chckbxAddingTimeStamp );
		}
		
		return chckbxAddingTimeStamp;
	}
	*/
	
	private JScrollPane getScrollPanelClientSockect() 
	{
		if (jScrollPanelClientSockets == null) 
		{
			jScrollPanelClientSockets = new JScrollPane();
			jScrollPanelClientSockets.setViewportView( getPanelTableClientSocket() );
			
			Dimension d = jScrollPanelClientSockets.getPreferredSize();
			d.height *= 3;
			jScrollPanelClientSockets.setPreferredSize( d );
		}
		
		return jScrollPanelClientSockets;
	}
	
	private JPanel getPanelTableClientSocket()
	{
		if( this.jPanelTableClientSockets == null )
		{
			this.jPanelTableClientSockets = new JPanel();
			jPanelTableClientSockets.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			
			this.jPanelTableClientSockets.setLayout( new BorderLayout() );
			
			//TODO
			this.jPanelTableClientSockets.add( getTableClientSockets().getTableHeader(), BorderLayout.NORTH );
			this.jPanelTableClientSockets.add( getTableClientSockets(), BorderLayout.CENTER );			
		}
		
		return this.jPanelTableClientSockets;
	}
	
	private JPanel getJPanelServerCommands() 
	{
		if (jPanelServerCommands == null) 
		{
			jPanelServerCommands = new JPanel();
			
			TitledBorder border = BorderFactory.createTitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Input Messages:" );
			
			jPanelServerCommands.setBorder( border );
			jPanelServerCommands.setLayout(new BorderLayout(0, 0));
			jPanelServerCommands.add( getJPanelTCP_Server(), BorderLayout.NORTH);
			jPanelServerCommands.add( getJScrollPaneServerCommands(), BorderLayout.CENTER);
			//jPanelServerCommands.add( getJScrollPanelCommandLegend(), BorderLayout.SOUTH);
		}
		return this.jPanelServerCommands;
	}
	
	private JScrollPane getJScrollPaneServerCommands() 
	{
		if ( this.jScrollPaneServerCommands == null ) 
		{
			this.jScrollPaneServerCommands = new JScrollPane();
			this.jScrollPaneServerCommands.setViewportView( this.getJPanelServerContentCommandsValues() );
		}
		return this.jScrollPaneServerCommands;
	}
	
	private JPanel getJPanelServerContentCommandsValues() 
	{
		if ( this.jPanelServerContentCommandsValues == null ) 
		{
			this.jPanelServerContentCommandsValues = new JPanel();
			this.jPanelServerContentCommandsValues.setLayout( new BorderLayout( 0, 0 ) );
			//jPanelServerContentCommandsValues.add(getJPanelServerCommandValues(), BorderLayout.NORTH);
			//TODO
			this.jPanelServerContentCommandsValues.add( this.getJTableServerCommandValues().getTableHeader(), BorderLayout.NORTH );
			this.jPanelServerContentCommandsValues.add( this.getJTableServerCommandValues(), BorderLayout.CENTER );
		}
		return this.jPanelServerContentCommandsValues;
	}
	
	private JScrollPane getJScrollPaneClientCommands() 
	{
		if ( this.jScrollPaneClientCommands == null ) 
		{
			this.jScrollPaneClientCommands = new JScrollPane();
			this.jScrollPaneClientCommands.setViewportView( this.getJPanelClientContentCommandsValues() );
		}
		return this.jScrollPaneClientCommands;
	}
	
	private JPanel getJPanelClientContentCommandsValues() 
	{
		if ( this.jPanelClientContentCommandsValues == null ) 
		{
			jPanelClientContentCommandsValues = new JPanel();
			jPanelClientContentCommandsValues.setLayout(new BorderLayout(0, 0));
			//jPanelClientContentCommandsValues.add( getJPanelClientCommandValues(), BorderLayout.NORTH);
			//TODO			
			jPanelClientContentCommandsValues.add( getJTableClientCommandValues().getTableHeader(), BorderLayout.NORTH);
			jPanelClientContentCommandsValues.add( getJTableClientCommandValues(), BorderLayout.CENTER);
		}
		return jPanelClientContentCommandsValues;
	}	
		
	private JPanel getJPanelTCP_Server() 
	{
		if (jPanelServerSocket == null) 
		{
			jPanelServerSocket = new JPanel();
			jPanelServerSocket.setLayout(new BoxLayout(jPanelServerSocket, BoxLayout.Y_AXIS));
			jPanelServerSocket.setBorder( BorderFactory.createEmptyBorder( 2, 0,  5,  0 ) );
			
			jPanelServerSocket.add( getJPanelServerControl() );
			jPanelServerSocket.add( getPanelTableServerSocket() );
		}
		return jPanelServerSocket;
	}
	
	private JPanel getJPanelServerControl() 
	{
		if (jPanelServerControl == null) 
		{
			jPanelServerControl = new JPanel();
			
			jPanelServerControl.setLayout( new BorderLayout() );
			jPanelServerControl.add( getJCheckboxServerSocket(), BorderLayout.WEST );
						
		}
		
		return jPanelServerControl;
	}
	
	private JCheckBox getJCheckboxServerSocket()
	{
		if( this.checkServerSocket == null )
		{
			final String ID = ConfigApp.IS_SOCKET_SERVER_ACTIVE;
			
			checkServerSocket = new JCheckBox( "Active");
			checkServerSocket.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			
			//final Window owner = super.getOwner();
			checkServerSocket.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JCheckBox c = (JCheckBox)e.getSource();
					
					ConfigApp.setProperty( ID, c.isSelected() );
					
					winOwner.updateSockectStatusText();
				}
			});
			
			this.parameters.put( ID, checkServerSocket );
		}
		
		return this.checkServerSocket;
	}
	
	private JPanel getPanelTableServerSocket()
	{
		if( this.jPanelTableServerSocket == null )
		{
			this.jPanelTableServerSocket = new JPanel();
			jPanelTableServerSocket.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			
			this.jPanelTableServerSocket.setLayout( new BorderLayout() );
			
			//TODO
			this.jPanelTableServerSocket.add( getTableServerSockets().getTableHeader(), BorderLayout.NORTH );
			this.jPanelTableServerSocket.add( getTableServerSockets(), BorderLayout.CENTER );			
		}
		
		return this.jPanelTableServerSocket;
	}
	
	private Object[][] updateSocketTableSetting( String propertyID, String newSocketID, String oldSocketID )
	{	
		Object[][] newCommanTable = null;
		
		Map< String, Object[][] > map = (Map< String, Object[][]> )ConfigApp.getProperty( propertyID );
		Object[][] commandTable = map.get( oldSocketID );		
		
		map.remove( oldSocketID );
		
		if( commandTable == null )
		{
			commandTable = ConfigApp.getDefaultCommandSocketTable( propertyID );			
			newCommanTable = commandTable;
		}
		
		if( newSocketID != null )
		{
			map.put( newSocketID, commandTable );
		}
		
		return newCommanTable;
	}
	
	private JTable getTableServerSockets()
	{
		if( this.jTableServerSocket == null )
		{
			final String propertyID = ConfigApp.SERVER_SOCKET_TABLE; 
			
			this.jTableServerSocket = this.getTableSockets( null ); 
			
			this.jTableServerSocket.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
						
			TableModel tm = this.jTableServerSocket.getModel();
			tm.addTableModelListener( new TableModelListener()
			{				
				@Override
				public void tableChanged( TableModelEvent e ) 
				{
					DefaultTableModel m = (DefaultTableModel)e.getSource();
					
					if( m.getRowCount() > 0 )
					{
						int r = e.getFirstRow();
						
						int protocol = streamSocketInfo.TCP_PROTOCOL;
						
						if( m.getValueAt( r, 0 ).toString().toLowerCase().equals( "udp" ) )
						{
							protocol = streamSocketInfo.UDP_PROTOCOL;
						}
						
						String newSocketID = streamSocketInfo.getSocketString( protocol, 
																				m.getValueAt( r, 1).toString(),
																				(Integer)m.getValueAt( r, 2) );
					
						Object[][] newCommandTable = updateSocketTableSetting( propertyID, newSocketID, getJTableServerCommandValues().getName() );
						
						if( newCommandTable != null )
						{
							if( m.getRowCount() == 1 )
							{
								getJTableServerCommandValues().setName( newSocketID );
								setJCommandTableSocket( (DefaultTableModel)getJTableServerCommandValues().getModel(), newCommandTable );
							}
						}
						
						getJTableServerCommandValues().setName( newSocketID );
					}
				}
			});
			
			this.parameters.put( propertyID, jTableServerSocket );
			
			this.jTableServerSocket.getSelectionModel().addListSelectionListener( this.getTableListSelectionListener( propertyID, this.jTableServerSocket, this.getJTableServerCommandValues(), null ) );						
		}
		
		return this.jTableServerSocket;
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
											        	cellComponent.setBackground( new Color(255, 255, 224) );
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
	
	private TableModel createClientSocketTableModel( Object[][] table)
	{
		return new DefaultTableModel( table,new String[] {"TCP/UDP", "IP address", "Port"} )
									{			
										Class[] columnTypes = new Class[] {	String.class, String.class, Integer.class };
										
										public Class getColumnClass(int columnIndex) 
										{
											return columnTypes[columnIndex];
										}
									} ;
	}
	
	private JTable getTableSockets( Object[][] t)
	{
		JTable table = this.getCreateJTable();
		table.putClientProperty("terminateEditOnFocusLost", true);
			            
		table.setBackground( Color.WHITE );
        table.setRowSelectionAllowed( false );
        					
		table.setModel( this.createClientSocketTableModel( t ) );
		
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(125);
		
		TableColumn columnType = table.getColumnModel().getColumn( 0 );
		
		JComboBox< String > opts = new JComboBox<String>();
		opts.addItem( "TCP" );
		opts.addItem( "UDP" );
		columnType.setCellEditor( new DefaultCellEditor( opts ) );
				
		columnType = table.getColumnModel().getColumn( 1 );			
		JTextField ipEditor = new JTextField();
		columnType.setCellEditor( new IPAddressCellEditor( ipEditor ) );	
		ipEditor.addMouseWheelListener( new MouseWheelListener() 
		{	
			@Override
			public void mouseWheelMoved( MouseWheelEvent e ) 
			{
				if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
				{
					int d = e.getUnitsToScroll();
					
					JTextField jtext = (JTextField)e.getSource();
					
					String ip = jtext.getText();
					
					int i = ip.lastIndexOf( "." ) + 1;
					
					if( i > 0 )
					{
						int aux = new Integer( ip.substring( i ) );
						
						if( d > 0 )
						{
							aux -= 1; 
						}
						else
						{
							aux += 1;
						}
						
						if( aux >= 0 && aux <= 255 )
						{
							ip = ip.substring( 0, i ) + aux;
							jtext.setText( ip );
						}
					}
				}
			}
		});		
		
		columnType = table.getColumnModel().getColumn( 2 );
		JSpinner portRange = new JSpinner( new SpinnerNumberModel( 1025, 1025, 65535, 1) );
		portRange.setEditor( new JSpinner.NumberEditor( portRange, "#") );		
		portRange.addMouseWheelListener( new MouseWheelListener() 
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
				
		columnType.setCellEditor( new SpinnerNumberCellEditor( portRange ) );
		
		return table;
	}
	
	private void setCommandTableSetting( String propertyID, String socketID, Object[][] commandTable )
	{		
		if( socketID != null && commandTable != null )
		{	
			Map< String, Object[][] > map = (Map<String, Object[][]>)ConfigApp.getProperty( propertyID );
			map.put( socketID, commandTable );			
		}
	}
	
	private void setJCommandTableSocket( DefaultTableModel m, Object[][] newTableContent )
	{
		if( m.getRowCount() == 0 )
		{
			for( int i = 0; i < newTableContent.length; i++ )
			{
				m.addRow( newTableContent[ i ] );
			}
		}
		else
		{
			for( int i = 0; i < newTableContent.length; i++ )
			{
				for( int j = 0; j < newTableContent[ 0 ].length; j++ )
				{
					m.setValueAt( newTableContent[ i ][ j ], i, j );
				}
			}
		}
	}
	
	private TableModel createSocketCommandTablemodel( )
	{					
		TableModel tm =  new DefaultTableModel(				
								null,
								new String[] {
									"Enable", "Event", "Message"
								}
							) {
								private static final long serialVersionUID = 1L;
								Class[] columnTypes = new Class[] {
									Boolean.class, String.class, String.class
								};
								public Class getColumnClass(int columnIndex) {
									return columnTypes[columnIndex];
								}
								boolean[] columnEditables = new boolean[] {
									true, false, true
								};
								public boolean isCellEditable(int row, int column) {
									return columnEditables[column];
								}
							};
		return tm;
	}
	
	private JTable getJTableServerCommandValues()
	{
		if( this.jTableServerCommandValue == null )
		{
			final String ID = ConfigApp.SERVER_SOCKET_TABLE;
			
			this.jTableServerCommandValue = this.getCreateJTable();
			this.jTableServerCommandValue.setModel( this.createSocketCommandTablemodel( ) );
			
			FontMetrics fm = this.jTableServerCommandValue.getFontMetrics( this.jTableServerCommandValue.getFont() );			
			String hCol0 = this.jTableServerCommandValue.getColumnModel().getColumn( 0 ).getHeaderValue().toString();
			
			int s = fm.stringWidth( " " + hCol0 + " " );
			this.jTableServerCommandValue.getColumnModel().getColumn( 0  ).setResizable( false );
			this.jTableServerCommandValue.getColumnModel().getColumn( 0 ).setPreferredWidth( s );
			this.jTableServerCommandValue.getColumnModel().getColumn( 0 ).setMaxWidth( s );
			
			//this.jTableServerCommandValue.getModel().addTableModelListener( this.getTableModelListener( this.jTableServerCommandValue, ID ) );			
	
			/*
			Object[][] t = (Object[][])ConfigApp.getProperty( ConfigApp.MESSAGE_TABLE_COMMAND_SERVER );
			this.jTableServerCommandValue.setModel( ctrUI.getInstance().createSocketCommandTablemodel( t ) );			
						
			//TODO
			this.jTableServerCommandValue.getColumnModel().getColumn( 0  ).setResizable( false );
			this.jTableServerCommandValue.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
			this.jTableServerCommandValue.getColumnModel().getColumn( 0 ).setMaxWidth( 50 );
			this.jTableServerCommandValue.getColumnModel().getColumn( 1 ).setPreferredWidth( 75 );
			this.jTableServerCommandValue.getColumnModel().getColumn( 1 ).setMaxWidth( 75 );
			this.jTableServerCommandValue.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );
			*/
			
			this.jTableServerCommandValue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			this.jTableServerCommandValue.getModel().addTableModelListener( new TableModelListener()
			{				
				@Override
				public void tableChanged(TableModelEvent e) 
				{
					if( e.getType() == TableModelEvent.UPDATE )
					{
						DefaultTableModel tm = (DefaultTableModel)e.getSource();
						
						int rs = tm.getRowCount();
						int cs = tm.getColumnCount();
						
						Object[][] t = new Object[ rs ][ cs ];
						
						for( int i = 0; i < rs; i++ )
						{
							for( int j = 0; j < cs; j++ )
							{
								t[ i ][ j ] = tm.getValueAt( i, j );
							}
						}
									
						setCommandTableSetting( ID, jTableServerCommandValue.getName(), t );
					}
				}
			});			
			
			this.jTableServerCommandValue.setPreferredScrollableViewportSize( this.jTableServerCommandValue.getPreferredSize() );
			this.jTableServerCommandValue.setFillsViewportHeight( true );
			
			this.parameters.put( ID, jTableServerCommandValue );
		}
		
		return this.jTableServerCommandValue;
	}
	
	private JButton getBtnNewClientSocket() 
	{
		if (jBtnNewClientSocket == null) 
		{
			jBtnNewClientSocket = new JButton("New socket");
			jBtnNewClientSocket.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					updateTableSockets( getTableClientSockets(), true );
				}
			});
		}
		return jBtnNewClientSocket;
	}
	
	private JButton getBtnDeleteClientSockets() 
	{
		if (jBtnDeleteClientSocket == null) 
		{
			jBtnDeleteClientSocket = new JButton("Delete socket(s)");
			jBtnDeleteClientSocket.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					updateTableSockets( getTableClientSockets(), false );
				}
			});
		}
		return jBtnDeleteClientSocket;
	}
	
	private JTable getTableClientSockets()
	{
		if( this.jTableClientSockets == null )
		{
			final String propertyID = ConfigApp.CLIENT_SOCKET_TABLE;
						
			this.jTableClientSockets = getTableSockets( null );
						
			this.jTableClientSockets.setRowSelectionAllowed( true );
			this.jTableClientSockets.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
			
			this.jTableClientSockets.getModel().addTableModelListener( new TableModelListener() 
			{				
				@Override
				public void tableChanged(TableModelEvent e) 
				{	
					DefaultTableModel tm = (DefaultTableModel)e.getSource();
					
					int r = e.getLastRow();
										
					if( e.getType() == TableModelEvent.UPDATE )
					{
						int protocol = streamSocketInfo.TCP_PROTOCOL;
						
						if( tm.getValueAt( r, 0).toString().toLowerCase().equals( "udp" ) )
						{
							protocol = streamSocketInfo.UDP_PROTOCOL;
						}
						
						String newSocketID = streamSocketInfo.getSocketString( protocol, 
																				tm.getValueAt( r, 1).toString(),
																				(Integer)tm.getValueAt( r, 2) );
						
						updateSocketTableSetting( propertyID, newSocketID, jTableClientSockets.getName() );
						jTableClientSockets.setName( newSocketID );		
						getJTableClientCommandValues().setName( newSocketID );
						getJTabbedPaneClientMessages().setTitleAt( 0, newSocketID );
					}
					else if( e.getType() == TableModelEvent.INSERT )
					{	
						int protocol = streamSocketInfo.TCP_PROTOCOL;
						
						if( tm.getValueAt( r, 0).toString().toLowerCase().equals( "udp" ) )
						{
							protocol = streamSocketInfo.UDP_PROTOCOL;
						}
						
						String newSocketID = streamSocketInfo.getSocketString( protocol, 
																				tm.getValueAt( r, 1).toString(),
																				(Integer)tm.getValueAt( r, 2) );
						
						Object[][] newCommandTable = updateSocketTableSetting( propertyID, newSocketID, null );
						
						if( newCommandTable != null && tm.getRowCount() == 1 )
						{	
							setJCommandTableSocket( (DefaultTableModel)getJTableClientCommandValues().getModel(), newCommandTable );							
							//setCommandTableSetting( propertyID, newSocketID, newCommandTable );							
							getJTableClientCommandValues().setName( newSocketID );
							jTableClientSockets.setName( newSocketID );							
							
							getJTabbedPaneClientMessages().setTitleAt( 0, newSocketID );
						}
					}	
					else
					{
						updateSocketTableSetting( propertyID, null, jTableClientSockets.getName() );	
						jTableClientSockets.setName( null );
						
						getJTableClientCommandValues().setName( null );
						
						if( jTableClientSockets.getRowCount() > 0 )
						{
							jTableClientSockets.setRowSelectionInterval( 0, 0 );
						}
					}
				}
			});
			
			this.parameters.put( propertyID, jTableClientSockets );
			
			this.jTableClientSockets.getSelectionModel().addListSelectionListener( getTableListSelectionListener( propertyID, this.jTableClientSockets, this.getJTableClientCommandValues(), this.getJTabbedPaneClientMessages() ) );
		}
		
		return this.jTableClientSockets;
	}
	
	private ListSelectionListener getTableListSelectionListener( final String propertyID,
																final JTable socketTable, 
																final JTable commandTable, 
																final JTabbedPane tab )
	{
		return new ListSelectionListener() 
		{			
			@Override
			public void valueChanged(ListSelectionEvent e) 
			{
				int[] r = socketTable.getSelectedRows();
				if( r.length == 1 )
				{
					TableModel tm = socketTable.getModel();
					
					int protocol = streamSocketInfo.TCP_PROTOCOL;
					if( tm.getValueAt( r[ 0 ],  0 ).toString().toLowerCase().equals( "udp" ) )
					{
						protocol = streamSocketInfo.UDP_PROTOCOL;
					}
					
					String socketID = streamSocketInfo.getSocketString( protocol,  
																		tm.getValueAt( r[ 0 ], 1).toString(), 
																		(Integer)tm.getValueAt( r[ 0 ], 2) );
					
					Object[][] table = ((Map< String, Object[][]> )ConfigApp.getProperty( propertyID )).get( socketID );
					
					if( table != null )
					{
						socketTable.setName( socketID );
						commandTable.setName( socketID );
										
						setJCommandTableSocket( (DefaultTableModel)commandTable.getModel(), table );				
						
						getJTabbedPaneClientMessages().setTitleAt( 0, socketID );
					}
				}
			}
		};
	}
	
	private JTable getJTableClientCommandValues()
	{
		if( this.jTableClientCommandValue == null )
		{
			final String ID = ConfigApp.CLIENT_SOCKET_TABLE;
			
			this.jTableClientCommandValue = this.getCreateJTable();
			
			this.jTableClientCommandValue.setModel( this.createSocketCommandTablemodel() );
			
			FontMetrics fm = this.jTableClientCommandValue.getFontMetrics( this.jTableClientCommandValue.getFont() );
			
			String hCol0 = this.jTableClientCommandValue.getColumnModel().getColumn( 0 ).getHeaderValue().toString();
			int s = fm.stringWidth( " " + hCol0 + " " );
			this.jTableClientCommandValue.getColumnModel().getColumn( 0  ).setResizable( false );
			this.jTableClientCommandValue.getColumnModel().getColumn( 0 ).setPreferredWidth( s );
			this.jTableClientCommandValue.getColumnModel().getColumn( 0 ).setMaxWidth( s );
			
			//this.jTableClientCommandValue.getModel().addTableModelListener( this.getTableModelListener( this.jTableClientCommandValue, ID ) );			
			
			/*
			Object[][] t = (Object[][])ConfigApp.getProperty( ConfigApp.MESSAGE_TABLE_COMMAND_CLIENT );
			this.jTableClientCommandValue.setModel( ctrUI.getInstance().createSocketCommandTablemodel( t.clone() ) );
			
			//TODO
			this.jTableClientCommandValue.getColumnModel().getColumn( 0  ).setResizable( false );
			this.jTableClientCommandValue.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
			this.jTableClientCommandValue.getColumnModel().getColumn( 0 ).setMaxWidth( 50 );
			this.jTableClientCommandValue.getColumnModel().getColumn( 1 ).setPreferredWidth( 135 );
			this.jTableClientCommandValue.getColumnModel().getColumn( 1 ).setMaxWidth( 135 );
			this.jTableClientCommandValue.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );
			*/
			this.jTableClientCommandValue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			this.jTableClientCommandValue.getModel().addTableModelListener( new TableModelListener()
			{				
				@Override
				public void tableChanged(TableModelEvent e) 
				{
					if( e.getType() == TableModelEvent.UPDATE )
					{
						DefaultTableModel m = (DefaultTableModel)e.getSource();
						
						int rs = m.getRowCount();
						int cs = m.getColumnCount();
						
						Object[][] t = new Object[ rs ][ cs ];
						
						for( int i = 0; i < rs; i++ )
						{
							for( int j = 0; j < cs; j++ )
							{
								t[ i ][ j ] = m.getValueAt( i, j );
							}
						}
						
						setCommandTableSetting( ID, jTableClientCommandValue.getName(), t );
						
						//ctrUI.getInstance().setSocketMessages( jTableClientCommandValue, ID );
					}
				}
			});			
			
			this.parameters.put( ID, jTableClientCommandValue );
		}
		
		return this.jTableClientCommandValue;
	}	

	/*
	private JScrollPane getJScrollPanelCommandLegend()
	{
		if( this.jScrollPanelCommandLegend == null )
		{
			this.jScrollPanelCommandLegend = new JScrollPane( this.getTextAreaCommandLegend() );	
			this.jScrollPanelCommandLegend.setBorder( BorderFactory.createTitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Input event meaning" ) );
			
			Dimension d = this.jScrollPanelCommandLegend.getPreferredSize();
			d.height = 110;
			this.jScrollPanelCommandLegend.setPreferredSize( new Dimension( d ) );	
		}
		
		return this.jScrollPanelCommandLegend;
	}
	*/
	
	/*
	private JTextArea getTextAreaCommandLegend() 
	{
		if (textAreaCommandLegend == null) 
		{
			textAreaCommandLegend = new JTextArea();
			textAreaCommandLegend.setEnabled(true);
			textAreaCommandLegend.setEditable(false);
			textAreaCommandLegend.setLineWrap(true);
			textAreaCommandLegend.setWrapStyleWord(true);
			textAreaCommandLegend.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
			
			this.writeCommandLegends( textAreaCommandLegend, true );
		}
		return textAreaCommandLegend;
	}
	*/
	/*
	private JScrollPane getJScrollPanelTriggeredEventLegend()
	{
		if( this.jScrollPanelTriggeredEventLegend == null )
		{
			this.jScrollPanelTriggeredEventLegend = new JScrollPane( this.getTextAreaTriggeredLegend() );
			this.jScrollPanelTriggeredEventLegend.setBorder( BorderFactory.createTitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Output event meaning" ) );
			
			Dimension d = this.jScrollPanelTriggeredEventLegend.getPreferredSize();
			d.height = 110;
			this.jScrollPanelTriggeredEventLegend.setPreferredSize( new Dimension( d ) );			
		}
		
		return this.jScrollPanelTriggeredEventLegend;
	}
	*/
	/*
	private JTextArea getTextAreaTriggeredLegend() 
	{
		if (textAreaTriggeredLegend == null) 
		{
			textAreaTriggeredLegend = new JTextArea();
			textAreaTriggeredLegend.setEnabled(true);
			textAreaTriggeredLegend.setEditable(false);
			textAreaTriggeredLegend.setLineWrap(true);
			textAreaTriggeredLegend.setWrapStyleWord(true);
			textAreaTriggeredLegend.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
					
			this.writeCommandLegends( textAreaTriggeredLegend, false );
		}
		return textAreaTriggeredLegend;
	}
	*/
	
	private JTabbedPane getJTabbedPaneClientMessages()
	{
		if( this.jTabPanelClientMessages == null )
		{
			this.jTabPanelClientMessages = new JTabbedPane();
			
			this.jTabPanelClientMessages.addTab( "", null, getJScrollPaneClientCommands(), null );			
		}
		
		return this.jTabPanelClientMessages;
	}
	
	private void writeCommandLegends( JTextPane jPanelCommandLegends )
	{
			Map< String, String > legends = Commands.getCommandLengeds();
			legends.putAll( Commands.getTriggeredEventLengeds() );
			
			SimpleAttributeSet at = new SimpleAttributeSet();
			StyleConstants.setFontFamily( at, jPanelCommandLegends.getFont().getFamily());
			StyleConstants.setUnderline( at, true );
			
			String text = "";
			
			for( String cm : legends.keySet() )
			{
				String lg = legends.get( cm );				
				text += cm.toUpperCase() + ": ";
				text += lg + "\n";
			}				
			
			FontMetrics fm = jPanelCommandLegends.getFontMetrics( jPanelCommandLegends.getFont() );
			
			MutableAttributeSet set = new SimpleAttributeSet();
			StyleConstants.setSpaceBelow( set, fm.getHeight() * 0.5F );
			
			jPanelCommandLegends.setParagraphAttributes( set, false );
			
			jPanelCommandLegends.setText( text.substring( 0, text.length() - 2) );		
	}
	
	protected void updateTableSockets( JTable table, boolean add )
	{	
		DefaultTableModel m = (DefaultTableModel)table.getModel();
		
		if( add )
		{	
			int port = 5555;
			
			ServerSocket localmachine = null;
			try 
			{
				localmachine = new ServerSocket( 0 );
				localmachine.setReuseAddress( true );			
				port = localmachine.getLocalPort();
				localmachine.close();
			} 
			catch (IOException e1) {}
			finally
			{
				localmachine = null;
			}
			
			String ip = Inet4Address.getLoopbackAddress().getHostAddress();
			Object[] rt = new Object[]{"TCP", ip, port };
			
			m.addRow( rt );			
		}
		else
		{		
			int[] rs = table.getSelectedRows();
			Arrays.sort( rs );
			
			for( int i = rs.length - 1; i >= 0; i-- )
			{ 
				m.removeRow( rs[ i ] ); 
			}
			
			if( table.getRowCount() == 0 )
			{
				this.updateTableSockets( table, true );
			}
			else
			{
				table.setRowSelectionInterval( 0, 0 );
			}
		}
	}
}
