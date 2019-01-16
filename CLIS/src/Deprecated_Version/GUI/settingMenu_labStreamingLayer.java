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
package Deprecated_Version.GUI;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Deprecated_Version.Auxiliar.LSLConfigParameters;
import Deprecated_Version.Config.ConfigApp;
import Controls.Commands;
import Controls.coreControl;
import GUI.guiLSLDataPlot;
import GUI.settingMenu_TaskSetting;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.SelectedButtonGroup;
import GUI.MyComponents.TupleHashSet;
import Deprecated_Version.OutputDataFile.*;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.sccn.LSL;
import edu.ucsd.sccn.LSL.StreamInfo;

public class settingMenu_labStreamingLayer extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//JSPLITPANE
	//private JSplitPane splitPane;

	private JTabbedPane tabDevice;
	private guiLSLDataPlot LSLplot;

	//JPANEL
	private JPanel contentPanel;
	private JPanel jPanelEvents;
	private JPanel jPanelDeviceInfo;
	private JPanel jOutFile;
	//private JPanel jOutFilePath;
	private JPanel jOutFileFormat;
	private JPanel paneDeviceInfo;
	private JPanel panelOutFileOption;
	private JPanel panelOutFileName;

	//JCOMBOX
	private JComboBox< String > fileFormat;

	//JTABLE
	private JTable tableEvents;

	//JTEXTFIELD
	//private JTextField filePath;
	private JTextField fileName;

	//JCHECKBOX
	//private JCheckBox tempDataFileDelete;

	//JBUTTON
	//private JButton btnSelectFile;
	private JButton btnRefreshDevices;
	//private JButton btnDrawingData;

	//LSL.STREAMINFO
	private StreamInfo[] deviceInfo;

	//settingMenu_TaskSetting
	private settingMenu_TaskSetting winOwner;

	//NONESELECTEDBUTTONGROUP
	//private NoneSelectedButtonGroup selectedDeviceGroup;
	private SelectedButtonGroup selectedDeviceGroup;

	// JTree
	private JTree devInfoTree;
	
	//Map
	private Map< String, Component > parameters;

	//Tuple
	//private Tuple< String, String > currentSelectedDev;
	//private LSLConfigParameters currentSelectedDev;

	/**
	 * Create the panel.
	 */
	public settingMenu_labStreamingLayer( settingMenu_TaskSetting owner )  throws Exception
	{
		this.winOwner = owner;

		setLayout(new BorderLayout(0, 0));

		this.parameters = new HashMap< String, Component >();

		this.selectedDeviceGroup = this.getRadioButtonGroup();

		updateDeviceInfos();

		if( this.deviceInfo == null || this.deviceInfo.length < 1 )
		{
			//ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, new Tuple< String, String >( null, null ) );
			ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, new TupleHashSet() );
		}

		//this.currentSelectedDev = null;

		super.add( this.getContentPanel(), BorderLayout.CENTER );
		super.setName( "Lab Streaming Layer" );
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
			else if( c instanceof JTextComponent )
			{
				((JTextComponent)c).setText( ConfigApp.getProperty( id ).toString() );
			}
			else if( c instanceof JComboBox )
			{
				((JComboBox)c).setSelectedItem( ConfigApp.getProperty( id ).toString() );
			}
			/*
			else if( c instanceof NoneSelectedButtonGroup )
			{
				NoneSelectedButtonGroup gr = (NoneSelectedButtonGroup)c;

				Set< Tuple< String, String > > devs = (Set< Tuple<String, String> >) ConfigApp.getProperty( id );

				for( Tuple< String, String > dev : devs )
				{
					if( dev.x != null && dev.y != null )
					{
						String devID = dev.toString();

						Enumeration< AbstractButton > bts = gr.getElements();
						boolean find = false;
						while( bts.hasMoreElements() && !find )
						{
							AbstractButton b = bts.nextElement();
							find = b.getName().equals(devID );

							if( find )
							{
								b.setSelected( true );
							}
						}

						if( !find )
						{
							ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, new TupleHashSet( ) );
						}

						this.winOwner.updateLabStreamingLayerStatusIcon();
					}
				}
			}
			*/			
			else if( c instanceof SelectedButtonGroup )
			{
				SelectedButtonGroup gr = (SelectedButtonGroup)c;

				HashSet< LSLConfigParameters > devs = (HashSet< LSLConfigParameters >) ConfigApp.getProperty( id );
				Iterator< LSLConfigParameters > itDevs = devs.iterator();
				
				while( itDevs.hasNext() )
				{
					LSLConfigParameters dev = itDevs.next();
					
					if( dev.isSelected() )
					{
						String devID = dev.getSourceID();

						Enumeration< AbstractButton > bts = gr.getElements();
						boolean find = false;
						while( bts.hasMoreElements() && !find )
						{
							AbstractButton b = bts.nextElement();
							find = b.getName().equals( devID );

							if( find )
							{
								b.setSelected( true );
							}
						}

						if( !find )
						{
							itDevs.remove();
						}

						//TODO						
						//this.winOwner.updateLabStreamingLayerStatusIcon();
					}
				}
				
				getJButtonRefreshDevice().doClick();
				
				ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, devs );
			}

			c.setVisible( true );
		}		
	}

	private void updateDeviceInfos() throws Exception
	{
		this.deviceInfo = null;

		try
		{

			StreamInfo[] devices = null;

			LSL lsl = new LSL();

			this.deviceInfo = lsl.resolve_streams( );	
			/*
			AbstractStoppableThread t = new AbstractStoppableThread() 
			{				
				@Override
				protected void runInLoop() throws Exception 
				{
					StreamInfo[] devices = null;

					LSL lsl = new LSL();

					deviceInfo = lsl.resolve_streams( );	
				}

				@Override
				protected void preStopThread(int friendliness) throws Exception 
				{					
				}

				@Override
				protected void postStopThread(int friendliness) throws Exception 
				{					
				}
			};

			t.startThread();
			 */

		}
		catch( Exception e )
		{			
		}		
	}

	private JPanel getContentPanel()
	{
		if( this.contentPanel == null )
		{
			this.contentPanel = new JPanel( new BorderLayout() );// JSplitPane();
			/*
			this.splitPane.setResizeWeight( 0.15 );
			this.splitPane.setDividerLocation( 0.5 );
			this.splitPane.setDividerSize( 5 );
			this.splitPane.setEnabled( true );

			this.splitPane.setLeftComponent( new JScrollPane( this.getJPanelEvent() ) );
			this.splitPane.setRightComponent( this.getJPanelDeviceInfo() );
			 */

			this.contentPanel.add( new JScrollPane( this.getJPanelEvent() ), BorderLayout.WEST );
			this.contentPanel.add( this.getJPanelDeviceInfo(), BorderLayout.CENTER );			
		}

		return this.contentPanel;
	}

	private JPanel getJPanelEvent()
	{
		if( this.jPanelEvents == null )
		{
			this.jPanelEvents = new JPanel();
			this.jPanelEvents.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
			this.jPanelEvents.setLayout( new BorderLayout() );

			this.jPanelEvents.add( this.getJTableEventValues().getTableHeader(), BorderLayout.NORTH );
			this.jPanelEvents.add( this.getJTableEventValues(), BorderLayout.CENTER );
		}

		return this.jPanelEvents;
	}

	private JPanel getJPanelDeviceInfo()
	{
		if( this.jPanelDeviceInfo == null )
		{
			this.jPanelDeviceInfo = new JPanel();
			this.jPanelDeviceInfo.setLayout( new BorderLayout() );

			this.jPanelDeviceInfo.add( this.getJPanelOutFile(), BorderLayout.NORTH );
			this.jPanelDeviceInfo.add( this.getJPaneDeviceInfo( ), BorderLayout.CENTER );
			this.jPanelDeviceInfo.add( this.getJButtonRefreshDevice(), BorderLayout.SOUTH );
		}

		return this.jPanelDeviceInfo;
	}

	private JButton getJButtonRefreshDevice()
	{
		if( this.btnRefreshDevices == null )
		{
			this.btnRefreshDevices = new JButton( "Refresh" );
			ImageIcon icon = null;

			try
			{
				//icon = new ImageIcon( settingMenu_labStreamingLayer.class.getResource( "/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png" ) );
				icon = GeneralAppIcon.Refresh( 16, 16, Color.BLACK, null );
			}
			catch( Exception e )
			{				
			}
			catch( Error e )
			{				
			}

			this.btnRefreshDevices.setIcon( icon );

			this.btnRefreshDevices.addActionListener( new ActionListener()
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JPanel p = getJPaneDeviceInfo();
					p.setVisible( false );
					p.removeAll();
					
					boolean findDevice = false;
					selectedDeviceGroup.removeAllButtons();

					try
					{
						updateDeviceInfos();						
						p.add( getContentPanelDeviceInfo() );

						HashSet< LSLConfigParameters > devs = (HashSet< LSLConfigParameters >) ConfigApp.getProperty( ConfigApp.LSL_ID_DEVICES );

						for( LSLConfigParameters dev : devs )
						{
							if( dev.isSelected() )
							{
								boolean find = false;
								Enumeration< AbstractButton > BTS = selectedDeviceGroup.getElements();
	
								while( BTS.hasMoreElements() && !find )
								{
									AbstractButton bt = BTS.nextElement();
									find = bt.getName().equals( dev.getSourceID() );
	
									if( find )
									{
										findDevice = findDevice || find;
										bt.setSelected( true );
									}
								}
								
								if( !find )
								{
									dev.setSelected( false );
								}
							}
						}
					}					
					catch( Exception ex )
					{						
					}

					if( !findDevice )
					{
						//TODO
						//winOwner.updateLabStreamingLayerStatusIcon();
					}

					p.setVisible( true );
				}
			});						
		}

		return this.btnRefreshDevices;
	}

	private JPanel getJPanelOutFile()
	{
		if( this.jOutFile == null )
		{
			this.jOutFile = new JPanel( );
			this.jOutFile.setLayout( new BoxLayout( this.jOutFile, BoxLayout.Y_AXIS ) );

			//this.jOutFile.add( this.getJPanelOutFilePath() );		
			this.jOutFile.add( this.getJPanelOutFileFormat() );
		}

		return this.jOutFile;
	}
	/*
	private JPanel getJPanelOutFilePath()
	{
		if( this.jOutFilePath == null )
		{
			this.jOutFilePath = new JPanel( new BorderLayout() ); 

			this.jOutFilePath.add( new JLabel(" Output file: "), BorderLayout.WEST );
			this.jOutFilePath.add( this.getFilePath(), BorderLayout.CENTER );
			this.jOutFilePath.add( this.getJButtonSelectOutFile(), BorderLayout.EAST );		
		}

		return this.jOutFilePath;
	}
	 */
	private JPanel getJPanelOutFileFormat()
	{
		if( this.jOutFileFormat == null )
		{
			this.jOutFileFormat = new JPanel( );
			this.jOutFileFormat.setLayout( new BorderLayout( ) );
			//this.jOutFileFormat.setLayout( new BoxLayout( this.jOutFileFormat, BoxLayout.X_AXIS ) );
			//this.jOutFileFormat.setLayout( new FlowLayout( FlowLayout.LEFT ) );

			this.jOutFileFormat.add( this.getPanelOutFileOption(), BorderLayout.EAST );
			this.jOutFileFormat.add( this.getPanelOutFileName(), BorderLayout.CENTER );			
		}

		return this.jOutFileFormat;
	}

	private JPanel getPanelOutFileName()
	{
		if( this.panelOutFileName == null )
		{
			this.panelOutFileName = new  JPanel( new BorderLayout() );

			this.panelOutFileName.add( new JLabel( " File name: "), BorderLayout.WEST );
			this.panelOutFileName.add( this.getJTextFileName(), BorderLayout.CENTER );
		}

		return this.panelOutFileName;
	}

	private JTextField getJTextFileName()
	{
		if( this.fileName == null )
		{
			this.fileName = new JTextField();

			this.fileName.setText( ConfigApp.getProperty( ConfigApp.LSL_OUTPUT_FILE_NAME ).toString() );

			final String ID = ConfigApp.LSL_OUTPUT_FILE_NAME;

			this.fileName.getDocument().addDocumentListener( new DocumentListener() 
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
						String name = e.getDocument().getText( 0, e.getDocument().getLength() );
						ConfigApp.setProperty( ID, name );
					}
					catch (BadLocationException e1) 
					{
						e1.printStackTrace();
					}
				}
			});

			this.fileName.addFocusListener( new FocusAdapter() 
			{	
				private String text = "";
				@Override
				public void focusGained(FocusEvent e) 
				{
					text = ((JTextField)e.getSource()).getText();
				}

				@Override
				public void focusLost(FocusEvent e) 
				{
					JTextField jtxt = (JTextField)e.getSource();

					String txt = jtxt.getText();
					int firstDot = txt.indexOf( ".");
					int lastDot = txt.lastIndexOf( "." );

					if( txt.isEmpty() 
							|| firstDot == 0
							|| lastDot == txt.length() - 1 )
					{
						jtxt.setText( text );
					}
				}
			});

			this.parameters.put( ID, this.fileName );
		}

		return this.fileName;
	}

	private JPanel getPanelOutFileOption()
	{
		if( this.panelOutFileOption == null )
		{
			this.panelOutFileOption = new JPanel();
			this.panelOutFileOption.setLayout( new FlowLayout( FlowLayout.LEFT ) );

			this.panelOutFileOption.add( new JLabel( "Output file format:") );
			this.panelOutFileOption.add( this.getJComboxFileFormat() );

			JSeparator separator = new JSeparator();
			separator.setBorder( BorderFactory.createBevelBorder(BevelBorder.RAISED ) );
			Dimension d = this.getJComboxFileFormat().getPreferredSize();			
			separator.setPreferredSize( new Dimension( 1, d.height ) );
			/*
			this.panelOutFileOption.add( separator );
			this.panelOutFileOption.add( this.getJCheckBoxDeleteTempDataFile() );
			*/
		}

		return this.panelOutFileOption;
	}

	/*
	private JCheckBox getJCheckBoxDeleteTempDataFile()
	{
		if( this.tempDataFileDelete == null )
		{
			this.tempDataFileDelete = new JCheckBox( "Delete temporal data file" );

			final String ID = ConfigApp.LSL_OUTPUT_TEMP_FILE_DELETE;
			
			this.tempDataFileDelete.addItemListener( new ItemListener() 
			{	
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					JCheckBox c = (JCheckBox)e.getSource();

					ConfigApp.setProperty( ID, c.isSelected() );
				}
			});

			this.tempDataFileDelete.setSelected( (Boolean)ConfigApp.getProperty( ConfigApp.LSL_OUTPUT_TEMP_FILE_DELETE ) );
			//this.tempDataFileDelete.setVisible( false );
			
			this.parameters.put( ID,this.tempDataFileDelete );
		}

		return this.tempDataFileDelete;
	}
	*/
	
	private JComboBox< String > getJComboxFileFormat()
	{
		if( this.fileFormat == null )
		{
			this.fileFormat = new JComboBox< String >();
			this.fileFormat.setEditable( false );

			final String ID = ConfigApp.LSL_OUTPUT_FILE_FORMAT;

			String[] fileFormat = DataFileFormat.getSupportedFileFormat();
			for( int i = 0; i < fileFormat.length; i++ )
			{
				this.fileFormat.addItem( fileFormat[ i ] );
			}

			this.fileFormat.setSelectedItem( ConfigApp.getProperty( ID ).toString() );

			this.fileFormat.addActionListener( new ActionListener()
			{	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JComboBox< String > c = (JComboBox<String>)e.getSource();
					String format =  c.getSelectedItem().toString();
					
					ConfigApp.setProperty( ID , format );
					
					String ext = DataFileFormat.getSupportedFileExtension( format );
					String nameFile = getJTextFileName().getText();
					int pos = nameFile.lastIndexOf( "." );
					if( pos >= 0 )
					{
						nameFile = nameFile.substring( 0 , pos );
					}
					
					nameFile += ext;
					
					getJTextFileName().setText( nameFile );					
				}
			});

			this.parameters.put( ID, this.fileFormat );
		}

		return this.fileFormat;
	}
	/*
	private JTextField getFilePath()
	{
		if( this.filePath == null )
		{
			this.filePath = new JTextField();

			final String ID = ConfigApp.LSL_OUTPUT_FILE_PATH;
			this.filePath.setText( ConfigApp.getProperty( ID ).toString() );

			this.filePath.getDocument().addDocumentListener( new DocumentListener() 
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
						String path = e.getDocument().getText( 0, e.getDocument().getLength() );
						ConfigApp.setProperty( ID, path );

						ConfigApp.setProperty( ID, e.getDocument().getText( 0, e.getDocument().getLength() ) );
					}
					catch (BadLocationException e1) 
					{
						e1.printStackTrace();
					}
				}
			});

			this.parameters.put( ID, this.filePath );
		}

		return this.filePath;
	}
	 */
	/*
	private JButton getJButtonSelectOutFile()
	{
		if( this.btnSelectFile == null )
		{			
			ImageIcon ico = null;
			String txt = "";
			try
			{
				ico = new ImageIcon(settingMenu_labStreamingLayer.class.getResource("/javax/swing/plaf/metal/icons/ocean/upFolder.gif") );
			}
			catch( Exception e )
			{
				txt = "...";
			}
			catch( Error e )
			{
				txt = "...";
			}

			this.btnSelectFile = new JButton( txt, ico );
			this.btnSelectFile.setPreferredSize( new Dimension( ico.getIconWidth() + 4, ico.getIconHeight() + 1 ) );

			this.btnSelectFile.addActionListener( new ActionListener() 
			{	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					String file = guiManager.getInstance().selectResultUserFile( ConfigApp.defaultNameOutputDataFile, false, JFileChooser.DIRECTORIES_ONLY );

					if( !file.isEmpty() )
					{
						getFilePath().setText( file );
					}
				}
			});
		}

		return this.btnSelectFile;
	}
	 */

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

	private TableModel createTablemodel( String[] columnNames, final Class[] classColumnType, final boolean[] colEditables  )
	{					
		if( columnNames == null || classColumnType == null || colEditables == null )
		{
			throw new IllegalArgumentException( "Input(s) null" );
		}		
		else if( ( columnNames.length + classColumnType.length ) != ( colEditables.length * 2 ) )
		{
			throw new IllegalArgumentException( "Different input array sizes" );
		}

		TableModel tm =  new DefaultTableModel( null, columnNames )
		{
			private static final long serialVersionUID = 1L;

			Class[] columnTypes = classColumnType;

			public Class getColumnClass(int columnIndex) 
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = colEditables;

			public boolean isCellEditable(int row, int column) 
			{
				return columnEditables[column];
			}
		};
		return tm;
	}

	private JTable getJTableEventValues()
	{
		if( this.tableEvents == null )
		{
			this.tableEvents = this.getCreateJTable();
			this.tableEvents.setModel( this.createTablemodel( new String[] { "Event", "Mark"}, 
																new Class[]{ String.class, 
																			 Integer.class }, 
																new boolean[] { false, false }) );

			this.tableEvents.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			
			this.tableEvents.getColumnModel().getColumn( 0 ).setResizable( true );
			this.tableEvents.getColumnModel().getColumn( 0 ).setPreferredWidth( 100 );
			
			this.tableEvents.getColumnModel().getColumn( 1 ).setResizable( true );
			this.tableEvents.getColumnModel().getColumn( 1 ).setPreferredWidth( 35 );
			
			DefaultTableModel model = (DefaultTableModel)this.tableEvents.getModel();
						
			Map< String, Integer > marks = Commands.getOutputDataFileMark();

			for( String key : marks.keySet() )
			{							
				model.addRow( new Object[ ]{ key, marks.get(  key ) } );
			}

			this.tableEvents.setPreferredScrollableViewportSize( this.tableEvents.getPreferredSize() );
			this.tableEvents.setFillsViewportHeight( true );
		}

		return this.tableEvents;
	}

	private JPanel getJPaneDeviceInfo( )
	{		
		if( this.paneDeviceInfo == null )
		{
			this.paneDeviceInfo = new JPanel();
			this.paneDeviceInfo.setLayout( new BorderLayout() );

			this.paneDeviceInfo.add( this.getContentPanelDeviceInfo( ), BorderLayout.CENTER ); 
		}

		return this.paneDeviceInfo;
	}

	private JPanel getContentPanelDeviceInfo( )
	{		
		JPanel panel = new JPanel();

		if( this.deviceInfo != null
				&& this.deviceInfo.length > 0 )
		{
			panel.setLayout( new BorderLayout() );

			DefaultMutableTreeNode tmodel = new DefaultMutableTreeNode();
			tmodel.setUserObject( "Devices" );

			//final Set< Tuple< String, String > > deviceIDs = (Set< Tuple< String, String > >) ConfigApp.getProperty( ConfigApp.LSL_ID_DEVICES );
			//final TupleHashSet deviceIDs = (TupleHashSet) ConfigApp.getProperty( ConfigApp.LSL_ID_DEVICES );
			final HashSet< LSLConfigParameters > deviceIDs = ( HashSet< LSLConfigParameters > )ConfigApp.getProperty( ConfigApp.LSL_ID_DEVICES );
			
			JPanel panelRadioButtons  = new JPanel();			
			/*
			BoxLayout bl = new BoxLayout( panelRadioButtons,BoxLayout.PAGE_AXIS );
			panelRadioButtons.setLayout( bl );
			*/
			
			
			GridBagLayout bl = new GridBagLayout();			
			panelRadioButtons.setLayout( bl );
						
			//panelRadioButtons.setSize( new Dimension( 200, 200 ));
			//panelRadioButtons.add( this.getJButtonDataPlot() );
			
			//Remove unplugged devices
			Iterator< LSLConfigParameters > itLSL = deviceIDs.iterator();
			while( itLSL.hasNext() )
			{
				LSLConfigParameters lslcfg = itLSL.next();
				boolean enc = false;
				for( int i = 0; i < this.deviceInfo.length && !enc; i++ )
				{
					StreamInfo info = this.deviceInfo[ i ];
					String uid = info.uid();
					
					enc = uid.equals( lslcfg.getUID() );
				}
				
				if( !enc )
				{
					itLSL.remove();
				}
			}				
				
			// Adding new devices			
			for( int i = 0; i < this.deviceInfo.length; i++ )
			{
				StreamInfo info = this.deviceInfo[ i ];
				String deviceName = info.name();
				String deviceType = info.type();
				String uid = info.uid();
				String sourceID = info.source_id(); 
						
				itLSL = deviceIDs.iterator();
				boolean enc = false;
				while( itLSL.hasNext() && !enc )
				{
					LSLConfigParameters lslCfg = itLSL.next();
					enc = lslCfg.getUID().equals( uid );
				}
				
				if( !enc )
				{
					if( sourceID.isEmpty() )
					{
						sourceID = deviceName + deviceType;
					}
					
					deviceIDs.add( new LSLConfigParameters( uid, deviceName, deviceType, sourceID ) );
				}
			}
			
			ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, deviceIDs  );

			// 
			for( int i = 0; i < this.deviceInfo.length; i++ )
			{
				final StreamInfo info = this.deviceInfo[ i ];

				String uid = info.uid();
				String deviceName = info.name();
				String deviceType = info.type();
				String sourceID = info.source_id();

				String idNode = deviceName + " (" + uid + ")";
				DefaultMutableTreeNode t = this.getDeviceInfo( info );				 
				t.setUserObject( idNode );
				tmodel.insert( t, tmodel.getChildCount() );
				
				itLSL = deviceIDs.iterator();
				boolean enc = false;
				LSLConfigParameters auxDev = null;
				while( itLSL.hasNext() && !enc )
				{
					auxDev = itLSL.next();
					
					enc = auxDev.getUID().equals( uid );
				}
				
				if( !enc )
				{
					if( sourceID.isEmpty() )
					{
						sourceID = deviceName + deviceType;
					}
					
					auxDev = new LSLConfigParameters( uid, deviceName, deviceType, sourceID );
					deviceIDs.add( auxDev );
				}
				final LSLConfigParameters dev = auxDev;
				
				//JRadioButton r = new JRadioButton( deviceName );
				/*
				String txtID = "<html>" + deviceName + "<br/><p style=\"font-size:8px;\">";
				int splitSize = 14;
				if( uid.length() > splitSize )
				{
					int p1 = 0;
					int p2 = splitSize;
					while( p2 < uid.length() )
					{
						txtID += uid.substring( p1, p2 ) + "<br/>";
						p1 = p2;
						p2 += splitSize;
					}
					
					if( p1 < uid.length() )
					{
						txtID += uid.substring( p1 );
					}
				}
				txtID += "</p></html>";
				JLabel idDev = new JLabel(  txtID );
				*/
				JCheckBox r = new JCheckBox( deviceName );
				if( !sourceID.isEmpty() )
				{	
					r.setName( sourceID );
				}
				else
				{
					r.setName( deviceName + deviceType );
				}
				
				//r.setName( "Stream" );
				
				r.setToolTipText( deviceName + "- uid: " + uid );
			
				//r.setName( new Tuple< String, String >( deviceType, deviceName ).toString() );
				//r.setName( deviceName + deviceType );
				r.setHorizontalTextPosition( JCheckBox.RIGHT );
				
				r.addItemListener( new ItemListener()
				{	
					@Override
					public void itemStateChanged(ItemEvent e) 
					{
						//JRadioButton b = (JRadioButton)e.getSource();
						JCheckBox b = (JCheckBox)e.getSource();
						
						//boolean con = false;					
						//Tuple< String, String > dev = new Tuple< String, String >( deviceType, deviceName );
						//LSLConfigParameters dev = new LSLConfigParameters( deviceName, deviceType );

						if( b.isSelected() )
						{
							dev.setSelected( true );
							//currentSelectedDev = dev;							

							//deviceIDs.add( dev );
							//ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, deviceIDs  );

							//getJButtonDataPlot().setEnabled( true );
						}
						else
						{
							/*
							if( dev.equals( currentSelectedDev ) )
							{
								currentSelectedDev = null;
							}

							getJButtonDataPlot().setEnabled( false );	
							*/						
							//deviceIDs.remove( dev );
							//ConfigApp.setProperty( ConfigApp.LSL_ID_DEVICES, deviceIDs );
							
							dev.setSelected( false );
						}
						
						if( winOwner != null )
						{
							//TODO
							//winOwner.updateLabStreamingLayerStatusIcon();
						}
					}
				});

				JButton addInfo = new JButton();
				addInfo.setName( "Extra" );
				addInfo.setBorder( BorderFactory.createEtchedBorder() );
				Dimension d = r.getPreferredSize();
				d.width = Math.min( d.width, d.height ) - 1;
				d.height = d.width;
				addInfo.setPreferredSize( d );
				addInfo.setIcon( GeneralAppIcon.Pencil( (int)(addInfo.getPreferredSize().height * 0.75D), Color.BLACK ) );
				//addInfo.setSize( addInfo.getPreferredSize() );
				addInfo.addActionListener( new ActionListener() 
				{	
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{						
						String textInfo = dev.getAdditionalInfo();
						
						String txInfo = JOptionPane.showInputDialog( deviceName + " (" + uid + ").\nAdditional Information:", textInfo );
						if( txInfo != null )
						{
							textInfo = txInfo;
						}
						
						dev.setAdditionalInfo( textInfo );
						
						info.desc().remove_child( LSLConfigParameters.ID_EXTRA_INFO_LABEL );
						info.desc().append_child_value( LSLConfigParameters.ID_EXTRA_INFO_LABEL, textInfo );
						
						getJTabDevice( null ).setVisible( false );
						
						int numDevices = tmodel.getRoot().getChildCount();
						boolean enc = false;
						for( int i =  0; i < numDevices; i++ )
						{
							DefaultMutableTreeNode dev = (DefaultMutableTreeNode)tmodel.getChildAt( i );
							enc = dev.getUserObject().equals( idNode );
							if( enc )
							{
								tmodel.remove( i );
								
								DefaultMutableTreeNode t = getDeviceInfo( info );
								t.setUserObject( idNode );
								tmodel.insert( t, i );								
							}
						}
												
						JTree tree = getDeviceInfoTree();						
						DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
						model.setRoot( tmodel );
						model.reload( );
						
						getJTabDevice( null ).setVisible( true );
					}
				});
				
				JButton plot = new JButton();
				plot.setName( "Plot" );
				plot.setBorder( BorderFactory.createEtchedBorder() );
				plot.setPreferredSize( d );
				plot.setIcon( GeneralAppIcon.Plot( (int)(plot.getPreferredSize().width * 0.75D)
														, Color.BLACK ) );
				plot.addActionListener( new ActionListener() 
				{					
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						try 
						{
							tabDevice.setSelectedIndex( 1 );
															
							coreControl.getInstance().createLSLDataPlot( getLSLPlot(), dev );
						}
						catch (Exception e1) 
						{
							e1.printStackTrace();
						}
					}
				});
				
				
				JSpinner chunckSize = new JSpinner();
				chunckSize.setToolTipText("Chunck size");
				FontMetrics fm = chunckSize.getFontMetrics( chunckSize.getFont() );
				Dimension dm = chunckSize.getPreferredSize();
				dm.width = fm.stringWidth( " 99999 ");
				chunckSize.setPreferredSize( dm );
				chunckSize.setSize( dm );
				
				chunckSize.setName( "Chunck" );
				
				chunckSize.setModel(new SpinnerNumberModel( new Integer( 1 ), new Integer( 1 ), null , new Integer( 1 ) ) );
				chunckSize.setValue( dev.getChunckSize() );
				
				chunckSize.addMouseWheelListener( new MouseWheelListener() 
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
				
				chunckSize.addChangeListener( new ChangeListener()
				{				
					@Override
					public void stateChanged(ChangeEvent e) 
					{
						try
						{
							JSpinner sp = (JSpinner)e.getSource();

							Integer v = (Integer)sp.getValue();
							
							dev.setChunckSize( v );
						}
						catch( IllegalArgumentException ex )
						{

						}			
					}
				});		

				JToggleButton interleaved = new JToggleButton();
				interleaved.setBorder( BorderFactory.createRaisedBevelBorder() );
				interleaved.setPreferredSize( d );
				
				interleaved.setToolTipText( "Interleaved LSL data" );
				interleaved.setName( "Interleaved" );
				
				interleaved.setSelected( dev.isInterleavedData() );
				if( interleaved.isSelected() )
				{
					interleaved.setBorder( BorderFactory.createLoweredBevelBorder() );
				}
				
				Icon ic = GeneralAppIcon.InterleavedIcon( d.width, d.height, Color.BLACK, null, null );
				if( ic == null )
				{
					interleaved.setText( "Interleaved" );
				}
				else
				{
					interleaved.setIcon( ic );
				}
				
				
				interleaved.addActionListener( new ActionListener() 
				{	
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						JToggleButton bt = (JToggleButton)e.getSource();
						
						if( bt.isSelected() )
						{
							bt.setBorder( BorderFactory.createLoweredBevelBorder() );						
						}
						else
						{
							bt.setBorder( BorderFactory.createRaisedBevelBorder() );
						}
						
						dev.setInterleaveadData( bt.isSelected() );
					}
				});
				//interleaved.setName( "interleaved-" + deviceName );
								
				JPanel deviceGroup = new JPanel();
				deviceGroup.setAlignmentX( Component.LEFT_ALIGNMENT );
					
				deviceGroup.setLayout( new BoxLayout( deviceGroup, BoxLayout.X_AXIS ) );
				deviceGroup.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
				deviceGroup.add( addInfo );
				deviceGroup.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
				deviceGroup.add( plot );
				deviceGroup.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
				deviceGroup.add( chunckSize );
				deviceGroup.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
				deviceGroup.add( interleaved );
				deviceGroup.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
				deviceGroup.add( r );				
				
				/*
				GridBagLayout gbl = new GridBagLayout();
				
				deviceGroup.setLayout( gbl );
				
				GridBagConstraints gbInfo = new GridBagConstraints();
				gbInfo.gridx = 0;
				gbInfo.gridy = 0;				
				deviceGroup.add( addInfo, gbInfo );
				
				GridBagConstraints gbPlot = new GridBagConstraints();
				gbPlot.gridx = 1;
				gbPlot.gridy = 0;				
				deviceGroup.add( plot, gbPlot );
				
				GridBagConstraints gbR = new GridBagConstraints();
				gbR.gridx = 2;
				gbR.gridy = 0;
				deviceGroup.add( r, gbR );
				
				GridBagConstraints gbChunck = new GridBagConstraints();
				gbChunck.gridx = 3;
				gbChunck.gridy = 0;
				deviceGroup.add( chunckSize );
				*/				
				
				this.selectedDeviceGroup.add( r );
				//panelRadioButtons.add( r );
				
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.NORTHWEST;
				gbc.gridy = panelRadioButtons.getComponentCount() + 1;
				
				if( i == this.deviceInfo.length - 1 )
				{
					gbc.weighty = 1.0;
				}
				
				panelRadioButtons.add( deviceGroup, gbc );
				
				if( dev.isSelected() )
				{
					r.setSelected( true );
				}
			}
			
			if( panelRadioButtons.getComponentCount() > 0 )
			{
				JPanel dGr = (JPanel)panelRadioButtons.getComponent( 0 );
				
				JPanel header = new JPanel( );
				header.setBackground( header.getBackground().brighter() );
				//FlowLayout ly = new FlowLayout( FlowLayout.LEFT );
				BoxLayout ly = new BoxLayout( header, BoxLayout.LINE_AXIS );
				header.setLayout( ly );
				header.setBorder( BorderFactory.createMatteBorder( 0, 0, 1, 0, Color.BLACK ) );
				
				Insets pad = new Insets( 0, 0, 0, 0 );
				String lb = "";
				boolean prevPad = true;
				Component[] cs = dGr.getComponents();
				
				for( int iC = 0; iC < cs.length; iC++ )
				{
					Component c = cs[ iC ];
					String name = c.getName();					

					if( name != null && !name.isEmpty() )
					{
						prevPad = false;

						if( !lb.isEmpty() )
						{
							Dimension dc = c.getPreferredSize();
							FontMetrics fm = c.getFontMetrics( c.getFont() );

							while( fm.stringWidth( name ) > dc.width + pad.left + pad.right / 2 ) 
							{
								name = name.substring( 0, name.length() - 1 );
							}

							if( header.getComponentCount() > 0 )
							{
								JSeparator s = new JSeparator( JSeparator.VERTICAL );
								Dimension ds = new Dimension( s.getPreferredSize().width, s.getMaximumSize().height );
								s.setMaximumSize( ds );
								header.add( s );
							}

							header.add( new JLabel( lb ) );							
							header.add( Box.createRigidArea( new Dimension( 2, 0 ) )  );

							//prevPad = true;
							pad.left = (int)Math.ceil( pad.right / 2.0D );
							pad.right = 0;
						}

						lb = name;
					}
					else
					{
						if( prevPad )
						{
							pad.left += c.getPreferredSize().width;
						}
						else
						{
							pad.right += c.getPreferredSize().width;
						}
					}
				}
				
				if( header.getComponentCount() > 0 )
				{
					JSeparator s = new JSeparator( JSeparator.VERTICAL );
					Dimension ds = new Dimension( s.getPreferredSize().width, s.getMaximumSize().height );
					s.setMaximumSize( ds );
					header.add( s );
					
					header.add( new JLabel( "Stream" ) );
				}				
				
				GridBagConstraints gbcHeader = new GridBagConstraints();
				gbcHeader.weightx = 1.0;
				gbcHeader.anchor = GridBagConstraints.NORTHWEST;
				gbcHeader.fill = GridBagConstraints.BOTH;
				gbcHeader.gridy = 0;
				
				panelRadioButtons.add( header, gbcHeader, 0 );
			}

			JTree tree = this.getDeviceInfoTree();
			DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			model.setRoot( tmodel );
			model.reload( );

			JScrollPane scr = new JScrollPane( panelRadioButtons );
			Dimension dscr = scr.getPreferredSize();
			dscr.width = 225;
			scr.setPreferredSize( dscr );
						
			panel.add( scr, BorderLayout.WEST );
			panel.add( this.getJTabDevice( tree ), BorderLayout.CENTER );
		}

		return panel;
	}

	private JTree getDeviceInfoTree()
	{
		if( this.devInfoTree == null )
		{
			this.devInfoTree = new JTree( new DefaultMutableTreeNode() );
			
		}
		
		return this.devInfoTree;
	}	
	
	private JTabbedPane getJTabDevice( JTree tree )
	{
		if( this.tabDevice == null )
		{
			this.tabDevice = new JTabbedPane();

			this.tabDevice.addTab( "Devices", new JScrollPane( tree ));

			this.tabDevice.addTab( "Plot", this.getLSLPlot() );
		}

		return this.tabDevice;
	}

	private guiLSLDataPlot getLSLPlot()
	{
		if( this.LSLplot == null )
		{
			this.LSLplot = new guiLSLDataPlot( 100 );
		}

		return this.LSLplot;
	}

	/*
	private NoneSelectedButtonGroup getRadioButtonGroup()
	{
		if( this.selectedDeviceGroup == null )
		{
			this.selectedDeviceGroup = new NoneSelectedButtonGroup();

			this.parameters.put( ConfigApp.LSL_ID_DEVICESs, this.selectedDeviceGroup );
		}

		return this.selectedDeviceGroup;
	}
	 */

	private SelectedButtonGroup getRadioButtonGroup()
	{
		if( this.selectedDeviceGroup == null )
		{
			this.selectedDeviceGroup = new SelectedButtonGroup();
			//this.selectedDeviceGroup.setLayout( new BoxLayout( this.selectedDeviceGroup, BoxLayout.Y_AXIS ) );

			this.parameters.put( ConfigApp.LSL_ID_DEVICES, this.selectedDeviceGroup );
		}

		return this.selectedDeviceGroup;
	}

	private DefaultMutableTreeNode getDeviceInfo( StreamInfo info )  
	{		 	
		DefaultMutableTreeNode tree = null;

		if( info != null )
		{
			try
			{  
				String xml = info.as_xml();

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
				Document doc = db.parse( bis );
				Node root = (Node)doc.getDocumentElement();
				tree = this.builtTreeNode( root );
			}
			catch( Exception e )
			{			 
			}
		}

		return tree;
	}

	private DefaultMutableTreeNode builtTreeNode( Node root )
	{		 
		DefaultMutableTreeNode dmtNode = new DefaultMutableTreeNode( this.getXMLAttributes( root ) );
		NodeList nodeList = root.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++)
		{
			Node tempNode = nodeList.item( count );
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if( tempNode.hasChildNodes( ) )
				{
					// loop again if has child nodes
					dmtNode.add( builtTreeNode( tempNode ) );
				}
				else
				{
					dmtNode.add( new DefaultMutableTreeNode( this.getXMLAttributes( tempNode ) ) );					 
				}
			}
		}

		return dmtNode;
	}

	private String getXMLAttributes( Node node )
	{
		String nodeText = node.getNodeName();

		if( node.getPreviousSibling() != null )
		{
			nodeText += "="+ node.getTextContent();
		}

		String nodeAttributes = "";

		if( node.hasAttributes() )
		{
			nodeAttributes = "{";
			NamedNodeMap attrs = node.getAttributes();

			for( int i = 0; i < attrs.getLength(); i++ )
			{
				nodeAttributes += attrs.item( i ) + "; " ;
			}

			nodeAttributes = nodeAttributes.substring( 0, nodeAttributes.length() - 2 ) + "}";
		}

		if( !nodeAttributes.isEmpty() )
		{
			nodeText += ":" + nodeAttributes; 
		}

		return nodeText;
	}

	/*
	private JButton getJButtonDataPlot()
	{
		if( this.btnDrawingData == null )
		{
			this.btnDrawingData = new JButton( "Plot" );
			this.btnDrawingData.setAlignmentX( JButton.LEFT );
			this.btnDrawingData.setEnabled( false );

			this.btnDrawingData.addActionListener( new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					try 
					{
						if( currentSelectedDev != null )
						{
							try
							{
								tabDevice.setSelectedIndex( 1 );
							}
							catch( Exception ex)
							{}
							
							coreControl.getInstance().createLSLDataPlot( getLSLPlot(), currentSelectedDev );
						}
					}
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				}
			});
		}

		return this.btnDrawingData;
	}
	*/

	/*
	 private List< Tuple< String, String> > getNodesToString( Node parent, NodeList nList )
	 {
		 List< Tuple< String, String > > list = new ArrayList< Tuple<String,String> >();
		 if( nList != null && nList.getLength() > 0 )
		 {			
			 for( int i = 0; i < nList.getLength(); i ++ )
			 {
				 Node an = nList.item( i );
				 String nodeName = an.getNodeName();
				 String nodeText = an.getTextContent();

				 if( an.getNodeType()==Node.ELEMENT_NODE )
				 {
					 NodeList nl2 = an.getChildNodes();

					 final ArrayList< Node > nodes = new ArrayList< Node >();
					 for( int j = 0; j < nl2.getLength(); j++ )
					 {
						 Node n = nl2.item( j );
						 if( n != null )
						 {
							 nodes.add( n );
						 }
					 }

					 if( nodes.isEmpty() )
					 {
						 int index = nodeText.indexOf("\n");
						 if( index > -1 )
						 {
							 nodeText = nodeText.substring( 0, index );
						 }

						 if( !nodeText.isEmpty() )
						 {
							 list.add( new Tuple<String, String>( nodeName, nodeText ) );
						 }
					 }
					 else
					 {
						 NodeList nCopy = new NodeList() 
						 {							
							@Override
							public Node item(int index) 
							{
								Node n = null;
								if( index < nodes.size() )
								{
									n = nodes.get( index );
								}

								return n;
							}

							@Override
							public int getLength() 
							{
								return nodes.size();
							}
						};

						list.addAll( getNodesToString( an, nCopy ) );						 
					 }

				 }
				 else if( an.getNodeType() == Node.TEXT_NODE )
				 {
					 int index = nodeText.indexOf("\n");
					 if( index > -1 )
					 {
						 nodeText = nodeText.substring( 0, index );
					 }

					 if( !nodeText.isEmpty() )
					 {
						 list.add( new Tuple<String, String>( parent.getNodeName(), nodeText ) );
					 }
				 }

		 }}

		 return list;
	 }
	 */
}
