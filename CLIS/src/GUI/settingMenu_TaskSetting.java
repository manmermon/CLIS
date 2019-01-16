package GUI;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;

import Activities.ActivityRegister;
import Activities.IActivity;
import Config.ConfigApp;
import Config.Language.Language;
import Controls.coreControl;
import GUI.MyComponents.DisabledGlassPane;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.MultipleNumberSpinner;
import GUI.MyComponents.NoneSelectedButtonGroup;
import Plugin.PluginConfiguration.PluginConfig;

public class settingMenu_TaskSetting extends JDialog 
{
	private static final long serialVersionUID = -7041875290846711443L;

	//WINDOWS
	private Window windowOwner;

	//PANELS
	private JPanel jpanelContent;
	private JPanel jPanelParametros;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_7;
	private JPanel panel_8;
	//private JPanel panel_9;
	//private JPanel panel_10;
	private JPanel panel_11;
	private JPanel panel_12;
	private JPanel panel_13;
	//private JPanel panel_19;
	private JPanel panel_16;
	private JPanel panel_17;
	private JPanel jPanelTimeOut;
	private JPanel panel_Task;
	private JPanel panelAux;
	private JPanel panelAux2;
	private JPanel jPanelTrajectoryAxis;
	private JPanel jPanelPrePosRun;
	private JPanel jPanelLog;
	private JPanel jPanelTimeFile;
	private JPanel activityReportPanel;
	//private JPanel activityReportFilePanel;
	private JPanel jPanelLogFileName;
	private JPanel jPanelTestBech;

	//SETTING MENUS
	private settingMenu_SocketSetting jpanelSocket;	
	private settingMenu_PluginConfig jPanelPluginConfig;

	//JTABBEDPANE
	private JTabbedPane settingMenu;

	// JSplitPane
	private JSplitPane splitPane;
	
	//JMENU
	private JMenuBar menuBar;
	private JMenuItem menuSave;
	private JMenuItem menuLoad;

	//BUTTONS
	//private JButton jButtonSaveConfig;
	//private JButton jButtonLoadConfig;
	private JButton jButtonSlideConfig;
	private JButton jButtonUsersResults;
	private JButton jButtonSocketSetting;
	private JButton jButtonSelectLogFile;
	private JButton jButtonSelectFile;	
	private JButton jButtonEditSAM;
	private JButton jButtonSelectTestBechFiles;

	//RADIOBUTTONS
	private JRadioButton jRadioButtonTimeOutManual;
	private JRadioButton jRadioButtonTimeOutAuto;

	//NoneSelectedButtonGroup
	private NoneSelectedButtonGroup groupJRadioButton = null;

	//CHECKBOXES
	private JCheckBox jCheckBoxTiempoTest;
	private JCheckBox jCheckBoxRepeticiones;
	private JCheckBox jChkbxTrajectoryXAxis;
	private JCheckBox jChkbxTrajectoryYAxis;
	private JCheckBox jCheckBoxSlide;
	private JCheckBox jCheckBoxSound;
	private JCheckBox jCheckBoxTimeUntilSoundEnd;
	private JCheckBox chckbxLog;
	private JCheckBox jCheckEntrenamiento;
	private JCheckBox jCheckBoxFullScreen;
	private JCheckBox jCheckBoxShowTrialTimer;
	private JCheckBox jCheckBoxCountdownSound;
	private JCheckBox jCheckBoxPreTimerCountdownSound;
	private JCheckBox jCheckBoxPostTimerCountdownSound;
	private JCheckBox jCheckBoxActivityReport;
	private JCheckBox jCheckBoxPrePosBlackBackground;	
	private JCheckBox jCheckBoxSamDominance;
	
	// ToggleButton
	private JToggleButton jToggleButtonBoxSamBeep;

	//COMBOBOXES
	private JComboBox< String > jComboboxTipoOper;
	private JComboBox< Integer > jComboboxDificultad;

	//TEXTFIELDS
	private JTextField jTextField_OutputFolder;
	private JTextField jTextField_LogFilePath;
	private JTextField jTextFieldPathTimeFile;
	private JTextField jTextFieldTestBench;

	//SPINNERS
	private JSpinner jSpinnerTimeTest;
	private JSpinner jSpinnerRepeats;
	private JSpinner jSpinnerTimeOutManual;
	//private JSpinner jSpinnerSecondaryTimer;
	private MultipleNumberSpinner jMNSpinner;
	private JSpinner jSpinnerPreRun;
	private JSpinner jSpinnerPosRun;
	private JSpinner jSpinnerSamSetSize;
	private JSpinner jSpinnerScreens;

	//LABELS
	private JLabel jLabelPosRun;
	private JLabel jLabelPreRun;
	private JLabel jLabelTimeMemory;

	//Map
	private Map< String, Component > parameters;

	//Color
	private Color colorIconLoadFolder = new Color( 150, 150, 255 );

	public settingMenu_TaskSetting( Window owner, String title )
	{
		super( owner );
		
		this.windowOwner = owner;

		this.parameters = new HashMap<String, Component>();
		
		init( title );

		loadConfigValues();
	}

	private void loadConfigValues()
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
			else if( c instanceof JSpinner )
			{
				((JSpinner)c).setValue( ConfigApp.getProperty( id ) );
			}
			else if( c instanceof JTextComponent )
			{
				((JTextComponent)c).setText( ConfigApp.getProperty( id ).toString() );
			}
			else if( c instanceof JComboBox )
			{
				((JComboBox)c).setSelectedIndex( (Integer)ConfigApp.getProperty( id ) );
			}
			else if( c instanceof MultipleNumberSpinner )
			{
				((MultipleNumberSpinner)c).setNumberList( (List)ConfigApp.getProperty( id ) );
			}

			c.setVisible( true );
		}

		if( this.jpanelSocket != null )
		{
			//this.jpanelSocket.setVisible( false );

			this.jpanelSocket.loadConfigValues();
			this.updateSockectStatusText();

			//this.jpanelSocket.setVisible( true );
		}
	}

	private void init( String title )
	{	
		DisabledGlassPane glass = new DisabledGlassPane();
		glass.activate( Language.getCaption( Language.MSG_WINDOW_LOAD_CONFIG, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ) );
		super.setGlassPane( glass );
		super.getGlassPane().setVisible( false );
		
		super.setTitle( title );
		super.setResizable( false );
		super.setModal( true );
		super.setJMenuBar( this.getMenuBar() );		

		super.getRootPane().registerKeyboardAction( keyActions.getEscapeCloseWindows( "EscapeCloseWindow"), 
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW );
		super.setContentPane( this.getJPanelContent() );
		//super.setContentPane( this.getJPanelParametros() );
		super.pack();
		//super.validate();

		super.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
		super.addWindowListener(new WindowAdapter()
		{		
			public void windowClosing(WindowEvent e) 
			{	
				JDialog d = (JDialog)e.getSource();
				
				if( !d.getGlassPane().isVisible() )
				{
					if( jpanelSocket != null )
					{
						jpanelSocket.updateTables();
					}

					try
					{
						Component c = getFocusOwner();
						if( c != null )
						{
							c.transferFocus();
						}
					}
					catch( Exception ex )
					{						
					}
					
					((JDialog)e.getSource()).dispose();
				}
			}

			public void windowActivated( WindowEvent e )
			{
			}

			public void windowOpened( WindowEvent e )
			{
				JDialog d = (JDialog)e.getSource();
				if( d.getIconImages().isEmpty() )
				{
					if( !d.getOwner().getIconImages().isEmpty() )
					{
						d.setIconImages( d.getOwner().getIconImages() );
					}
					else
					{
						d.setIconImage( appUI.getInstance().getIconImage() );
					}
				}
			}
		});
	}

	private JMenuBar getMenuBar()
	{
		if( this.menuBar == null )
		{
			this.menuBar = new JMenuBar();

			JMenu menu = new JMenu( "File" );
			menu.setMnemonic( KeyEvent.VK_ALT );

			menu.add( this.getMenuLoad() );			
			menu.add( this.getMenuSave() );

			this.menuBar.add( menu );
		}

		return this.menuBar;
	}

	private JMenuItem getMenuLoad()
	{
		if( this.menuLoad == null )
		{
			this.menuLoad = new JMenuItem( "Load" );
			this.menuLoad.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, KeyEvent.ALT_MASK ));

			try
			{
				this.menuLoad.setIcon( GeneralAppIcon.LoadFolder( 22, 22, Color.BLACK, this.colorIconLoadFolder ) );
			}
			catch( Exception e )
			{				
			}
			catch( Error e )
			{}

			final settingMenu_TaskSetting setTask = this;
			
			this.menuLoad.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					setTask.getGlassPane().setVisible( true );					
					
					guiManager.getInstance().loadFileConfig();

					setTask.getGlassPane().setVisible( false );
					
					loadConfigValues();

				}
			});
		}

		return this.menuLoad;
	}

	private JMenuItem getMenuSave()
	{
		if( this.menuSave == null )
		{	
			this.menuSave = new JMenuItem( "Save" );
			this.menuSave.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, KeyEvent.ALT_MASK ) );

			try
			{
				this.menuSave.setIcon( GeneralAppIcon.SaveFile( 16, Color.BLACK ) );
			}
			catch( Exception e)
			{}
			catch( Error e )
			{}

			this.menuSave.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					guiManager.getInstance().saveFileConfig();
				}
			});
		}

		return this.menuSave;
	}

	private JPanel getJPanelContent()
	{		
		if( this.jpanelContent == null )
		{
			this.jpanelContent = new JPanel();
			this.jpanelContent.setLayout( new BorderLayout() );

			//this.jpanelContent.add( new JScrollPane( this.getJPanelParametros() ), BorderLayout.NORTH );
			//this.jpanelContent.add( this.getJTabbedPaneSettingMenu(), BorderLayout.CENTER );
			//this.jpanelContent.add( this.getPanel_9(), BorderLayout.SOUTH)
			this.jpanelContent.add( this.getJSplitPanelSetting(), BorderLayout.CENTER );
		}

		return this.jpanelContent;
	}

	private JSplitPane getJSplitPanelSetting()
	{
		if ( this.splitPane == null) 
		{
			this.splitPane = new JSplitPane();
			//jPanelSlideImg.setLayout(new BorderLayout(0, 0));
			this.splitPane.setResizeWeight( 0.51);
			this.splitPane.setDividerLocation( 0.5 );
			this.splitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
			this.splitPane.setLeftComponent( new JScrollPane( this.getJPanelParametros() ) );
			this.splitPane.setRightComponent( this.getJTabbedPaneSettingMenu() );
		}
		
		return this.splitPane;
	}
	
	private JTabbedPane getJTabbedPaneSettingMenu()
	{
		if( this.settingMenu == null )
		{
			this.settingMenu = new JTabbedPane();

			this.jpanelSocket = this.getSocketMenu();
			this.jpanelSocket.setName( "Socket");

			this.settingMenu.addTab( this.jpanelSocket.getName(), null,	new JScrollPane( this.jpanelSocket ) );
			try
			{
				this.settingMenu.setIconAt( 0, GeneralAppIcon.Socket( Color.RED, Color.RED));
			}
			catch( Exception e )
			{				
			}
			catch( Error e )
			{				
			}
		}

		return this.settingMenu;
	}

	private settingMenu_SocketSetting getSocketMenu()
	{
		if( this.jpanelSocket == null )
		{
			this.jpanelSocket = new settingMenu_SocketSetting( this );
		}

		return this.jpanelSocket;
	}

	private JPanel getJPanelParametros() 
	{
		if ( this.jPanelParametros == null) 
		{	
			this.jPanelParametros = new JPanel();
			this.jPanelParametros.setBorder( BorderFactory.createEmptyBorder( 5, 2, 2, 2));
			this.jPanelParametros.setLayout(new BorderLayout(0, 0));

			this.jPanelParametros.add( this.getPanel_2(), BorderLayout.CENTER);			

		}
		return jPanelParametros;
	}

	private JPanel getPanel_1() 
	{
		if ( this.panel_1 == null)
		{
			this.panel_1 = new JPanel();
			this.panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
			this.panel_1.add( this.getJCheckBoxEntrenamiento());
			this.panel_1.add( this.getJCheckBoxFullScreen());
			this.panel_1.add( this.getJCheckBoxShowTrialTimer());
			this.panel_1.add( this.getJCheckBoxCountdownSound());
			this.panel_1.add( this.getJPanelActivityReport() );
		}

		return this.panel_1;
	}

	private JPanel getPanel_2() 
	{
		if (this.panel_2 == null) 
		{
			this.panel_2 = new JPanel();
			this.panel_2.setLayout(new BorderLayout(0, 0));
			this.panel_2.add( this.getPanel_4(), BorderLayout.NORTH);
		}
		return this.panel_2;
	}

	private JPanel getPanel_3() 
	{
		if ( this.panel_3 == null) {
			this.panel_3 = new JPanel();
			this.panel_3.setLayout(new BorderLayout(0, 0));
			//panel_3.add(getPanel_7_1(), BorderLayout.SOUTH);
			this.panel_3.add( this.getPanel_16(), BorderLayout.NORTH);
		}
		return this.panel_3;
	}

	private JPanel getPanel_4() 
	{
		if ( this.panel_4 == null) 
		{
			this.panel_4 = new JPanel();
			this.panel_4.setLayout(new BorderLayout(0, 0));
			this.panel_4.add( this.getPanel_8(), BorderLayout.NORTH);
		}
		return panel_4;
	}

	private JPanel getPanel_5() 
	{
		if ( this.panel_5 == null) 
		{
			this.panel_5 = new JPanel();
			this.panel_5.setBorder(new EmptyBorder(0, 8, 0, 10));
			this.panel_5.setLayout(new BorderLayout(0, 0));
			this.panel_5.add( this.getPanel_12(), BorderLayout.CENTER);
			this.panel_5.add( this.getPanel_6(), BorderLayout.NORTH);
			//this.panel_5.add( this.getJPanelActivityReport(), BorderLayout.SOUTH );
		}
		return panel_5;
	}

	private JPanel getPanel_6() 
	{
		if (this.panel_6 == null) 
		{
			this.panel_6 = new JPanel();
			this.panel_6.setBorder(new EmptyBorder(2, 0, 0, 0));
			this.panel_6.setLayout(new BorderLayout(0, 0));
			//this.panel_6.add( this.getPanel_19(), BorderLayout.NORTH);
			this.panel_6.add( this.getPanel_7(), BorderLayout.CENTER );
			this.panel_6.add( this.getPanelTestBech(), BorderLayout.NORTH);
		}
		return panel_6;
	}

	private JPanel getPanelTestBech()
	{
		if( this.jPanelTestBech == null )
		{
			this.jPanelTestBech = new JPanel( new BorderLayout() );
			
			this.jPanelTestBech.setBorder( BorderFactory.createTitledBorder( "Test Bench" ));
			
			this.jPanelTestBech.add( this.getJButtonTestBechFiles(), BorderLayout.WEST );
			this.jPanelTestBech.add( this.getJTextTestBechFiles(), BorderLayout.CENTER );
		}
		
		return this.jPanelTestBech;
	}
	
	private JButton getJButtonTestBechFiles()
	{
		if( this.jButtonSelectTestBechFiles == null )
		{
			this.jButtonSelectTestBechFiles = new JButton( " + " );
				
			this.getJButtonTestBechFiles().setFont( new Font( Font.DIALOG, Font.BOLD, 18 ) );
						
			this.jButtonSelectTestBechFiles.setBorder( new LineBorder( Color.black ));

			this.jButtonSelectTestBechFiles.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					String testBenchFiles = getJTextTestBechFiles().getText();
					
					String path[] = guiManager.getInstance().selectUserFile( ConfigApp.defaultPathFile + "*." + ConfigApp.defaultNameFileConfigExtension, true
																				, true, JFileChooser.FILES_ONLY
																				, "config (*." + ConfigApp.defaultNameFileConfigExtension + ")"
																				, new String[] { ConfigApp.defaultNameFileConfigExtension } );
					
					if( path != null )
					{
						for( int iF = path.length - 1; iF >= 0; iF-- )
						{
							String f = path[ iF ];
							
							testBenchFiles += f + ";" ;
						}
					}
										
					getJTextTestBechFiles().setText( testBenchFiles );
				}
			});
		}
		
		return this.jButtonSelectTestBechFiles;
	}
		
	private JTextField getJTextTestBechFiles()
	{
		if( this.jTextFieldTestBench == null )
		{
			final String ID = ConfigApp.TEST_BENCH_FILES;
			
			this.jTextFieldTestBench = new JTextField();
			this.jTextFieldTestBench.setColumns( 25 );
			
			this.jTextFieldTestBench.getDocument().addDocumentListener( new DocumentListener() 
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
						ConfigApp.setProperty( ID, e.getDocument().getText( 0, e.getDocument().getLength() ) );
					}
					catch (BadLocationException e1) 
					{
						e1.printStackTrace();
					}
				}
			});

			this.parameters.put( ID, this.jTextFieldTestBench );
		}
		
		return this.jTextFieldTestBench;
	}
	
	private JPanel getPanel_7() 
	{
		if ( this.panel_7 == null) 
		{
			this.panel_7 = new JPanel();
			this.panel_7.setBorder( BorderFactory.createEmptyBorder( 2, 0, 0, 0 ) );

			GridBagLayout ly = new GridBagLayout();
			ly.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
			ly.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
			ly.columnWeights = new double[]{0.0, 0.5, 0.0, 0.5, 0.0, 0.5, Double.MIN_VALUE};
			ly.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			this.panel_7.setLayout( ly );

			GridBagConstraints gbc_jCheckBoxTiempoTest = new GridBagConstraints();
			gbc_jCheckBoxTiempoTest.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxTiempoTest.insets = new Insets(0, 0, 5, 0);
			gbc_jCheckBoxTiempoTest.gridx = 0;
			gbc_jCheckBoxTiempoTest.gridy = 0;
			this.panel_7.add( this.getJCheckBoxTiempoTest(), gbc_jCheckBoxTiempoTest);

			GridBagConstraints gbc_jTextFieldTimeTest = new GridBagConstraints();
			gbc_jTextFieldTimeTest.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldTimeTest.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldTimeTest.gridx = 1;
			gbc_jTextFieldTimeTest.gridy = 0;
			this.panel_7.add( this.getJSpinnerTimeTest(), gbc_jTextFieldTimeTest);

			GridBagConstraints gbc_jCheckBoxRepeticiones = new GridBagConstraints();
			gbc_jCheckBoxRepeticiones.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxRepeticiones.insets = new Insets(0, 0, 5, 5);
			gbc_jCheckBoxRepeticiones.gridx = 2;
			gbc_jCheckBoxRepeticiones.gridy = 0;
			this.panel_7.add( this.getJCheckBoxRepeticiones(), gbc_jCheckBoxRepeticiones);

			GridBagConstraints gbc_jTextFieldRepeticiones = new GridBagConstraints();
			gbc_jTextFieldRepeticiones.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldRepeticiones.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldRepeticiones.gridx = 3;
			gbc_jTextFieldRepeticiones.gridy = 0;
			this.panel_7.add( this.getJSpinnerRepeats(), gbc_jTextFieldRepeticiones);
			
			GridBagConstraints gbc_jlabelScreen = new GridBagConstraints();
			gbc_jlabelScreen.fill = GridBagConstraints.HORIZONTAL;
			gbc_jlabelScreen.insets = new Insets(0, 0, 5, 0);
			gbc_jlabelScreen.gridx = 4;
			gbc_jlabelScreen.gridy = 0;
			this.panel_7.add( new JLabel( " Monitor " ), gbc_jlabelScreen);
			
			GridBagConstraints gbc_jspinnerScreen = new GridBagConstraints();
			gbc_jspinnerScreen.fill = GridBagConstraints.HORIZONTAL;
			gbc_jspinnerScreen.insets = new Insets(0, 0, 5, 0);
			gbc_jspinnerScreen.gridx = 5;
			gbc_jspinnerScreen.gridy = 0;
			this.panel_7.add( this.getJSpinnerScreens(), gbc_jspinnerScreen);
			
		}
		return this.panel_7;
	}

	private JPanel getPanel_8() 
	{
		if ( this.panel_8 == null) 
		{
			this.panel_8 = new JPanel();
			this.panel_8.setLayout(new BorderLayout(0, 0));
			this.panel_8.add( this.getPanel_3(), BorderLayout.WEST);
			this.panel_8.add( this.getPanel_11(), BorderLayout.CENTER);
		}
		return this.panel_8;
	}

	/*
	private JPanel getPanel_9() 
	{
		if ( this.panel_9 == null) 
		{
			this.panel_9 = new JPanel();
			this.panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.X_AXIS));
			this.panel_9.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
			this.panel_9.add( this.getPanel_10());
			this.panel_9.add( Box.createRigidArea( new Dimension( 3, 0 ) ) );
			this.panel_9.add( this.getPanel_14());
		}
		return this.panel_9;
	}
	 */

	/*
	private JPanel getPanel_10() 
	{
		if ( this.panel_10 == null) 
		{
			this.panel_10 = new JPanel();
			this.panel_10.setLayout(new BorderLayout(0, 0));
			this.panel_10.add( this.getJButtonSaveConfig(), BorderLayout.NORTH);
		}
		return this.panel_10;
	}
	 */

	private JPanel getPanel_11()
	{
		if ( this.panel_11 == null) 
		{
			this.panel_11 = new JPanel();
			this.panel_11.setLayout(new BorderLayout(0, 0));
			this.panel_11.add( this.getPanel_5(), BorderLayout.WEST);
		}
		return this.panel_11;
	}

	private JPanel getPanel_12() 
	{
		if ( this.panel_12 == null) 
		{
			this.panel_12 = new JPanel();
			//panel_12.setBorder( BorderFactory.createBevelBorder( 1 ));
			this.panel_12.setLayout(new BorderLayout(0, 0));
			//panel_12.add( getPanel_18(), BorderLayout.NORTH );

			this.panel_12.add( this.getPanel_Aux(), BorderLayout.NORTH);
			//panel_12.add(getJPanelPrePosRun(), BorderLayout.WEST);
			//panel_12.add(getPanel_13(), BorderLayout.CENTER);
			this.panel_12.add( this.getPanel_Aux2(), BorderLayout.CENTER);
		}
		return this.panel_12;
	}

	private JPanel getPanel_13() 
	{
		if ( this.panel_13 == null) 
		{
			this.panel_13 = new JPanel();
			this.panel_13.setLayout(new BorderLayout(0, 0));
			//panel_13.setBorder( BorderFactory.createBevelBorder( 1 ));
			this.panel_13.add( this.getJPanelTimeOut(), BorderLayout.WEST);
			//panel_13.add(getJPanelLog(), BorderLayout.SOUTH);
		}
		return panel_13;
	}

	/*
	private JPanel getPanel_14() 
	{
		if ( this.panel_14 == null) 
		{
			this.panel_14 = new JPanel();
			this.panel_14.setLayout(new BorderLayout(0, 0));
			this.panel_14.add( this.getJButtonLoadConfig(), BorderLayout.NORTH);
		}
		return this.panel_14;
	}
	 */

	/*
	private JPanel getPanel_15() 
	{
		if( this.panel_15 == null) 
		{
			this.panel_15 = new JPanel();
			this.panel_15.setBorder(new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Tasks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			this.panel_15.setLayout(new BoxLayout( this.panel_15, BoxLayout.Y_AXIS));
			this.panel_15.add( this.getPanel_16() );
			this.panel_15.add( this.getPanel_17() );

			this.jButtonGroupSelectTask = new ButtonGroup();
			this.jButtonGroupSelectTask.add( this.getJRadioButtonArousalTask() );
			this.jButtonGroupSelectTask.add( this.getJRadioButtonEmotionalTask() );
		}
		return panel_15;
	}*/
	/*
	private JRadioButton getJRadioButtonArousalTask()
	{
		if( this.jRadioButtonArousalTask == null )
		{
			final String ID = ConfigApp.IS_SELECTED_AROUSAL_TASK;

			this.jRadioButtonArousalTask = new JRadioButton( "Cognitive Tasks" );

			this.jRadioButtonArousalTask.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent arg0)
				{
					ConfigApp.setProperty( ID, ((JRadioButton)arg0.getSource()).isSelected() );
					getPanel_16().setVisible( false );
					getPanel_16().setVisible( true );
				}
			});

			this.parameters.put( ID, this.jRadioButtonArousalTask );

			this.jRadioButtonArousalTask.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
		}

		return this.jRadioButtonArousalTask;
	}
	 */
	/*
	private JRadioButton getJRadioButtonEmotionalTask()
	{
		if( this.jRadioButtonEmotionalTask == null )
		{
			final String ID = ConfigApp.IS_SELECTED_EMOTIONAL_TASK;

			this.jRadioButtonEmotionalTask = new JRadioButton( "Affective Tasks" );

			this.jRadioButtonEmotionalTask.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent arg0)
				{
					ConfigApp.setProperty( ID, ((JRadioButton)arg0.getSource()).isSelected() );

					getPanel_17().setVisible( false );
					getPanel_17().setVisible( true );

					getJTextField_LogFilePath().setPreferredSize( getJTextField_LogFilePath().getSize() );

					appUI.getInstance().getJPanelObjetivo().setVisible( !((JRadioButton)arg0.getSource()).isSelected() );
					//ctrUI.getInstance().showComparationPanel();
				}
			});

			this.parameters.put( ID, this.jRadioButtonEmotionalTask );

			this.jRadioButtonEmotionalTask.setSelected( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECTED_EMOTIONAL_TASK ) );
		}

		return this.jRadioButtonEmotionalTask;
	}
	 */
	private JPanel getPanel_16() 
	{
		if ( this.panel_16 == null) 
		{
			this.panel_16 = new JPanel();

			//this.panel_16.setBorder( new componentTitleBorder( this.getJRadioButtonArousalTask(), this.panel_16,  BorderFactory.createLineBorder( SystemColor.inactiveCaption, 2 ) ) );
			this.panel_16.setBorder( BorderFactory.createLineBorder( SystemColor.inactiveCaption, 2 ) );
			this.panel_16.setLayout( new BoxLayout( this.panel_16, BoxLayout.Y_AXIS));
			this.panel_16.add( this.getPanel_Task() );
			this.panel_16.add( this.getJPanelTrajectoryAxis() );
			this.panel_16.add( this.getPanel_17() );
		}
		return panel_16;
	}

	private JPanel getJPanelActivityReport()
	{
		if( this.activityReportPanel == null )
		{
			this.activityReportPanel = new JPanel( new BorderLayout() );

			this.activityReportPanel.add( this.getJCheckActivityReport(), BorderLayout.WEST );
		}	

		return this.activityReportPanel;
	}

	private JCheckBox getJCheckActivityReport()
	{
		if( this.jCheckBoxActivityReport == null )
		{
			final String ID = ConfigApp.IS_SELECT_ACTIVITY_REPORT;

			this.jCheckBoxActivityReport = new JCheckBox( "Activity Report" );		

			this.jCheckBoxActivityReport.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxActivityReport );
		}			

		return this.jCheckBoxActivityReport;
	}	

	private JPanel getPanel_17() 
	{
		if ( this.panel_17 == null) 
		{
			panel_17 = new JPanel();

			//this.panel_17.setBorder( new componentTitleBorder( this.getJRadioButtonEmotionalTask(), this.panel_17,  BorderFactory.createLineBorder( SystemColor.inactiveCaption, 2 ) ) );
			this.panel_17.setBorder( BorderFactory.createTitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Affective task" ) );

			GridBagLayout gbl_panel_17 = new GridBagLayout();
			gbl_panel_17.columnWidths = new int[]{0, 0, 0, 0};
			gbl_panel_17.rowHeights = new int[]{0, 0, 0, 0};
			gbl_panel_17.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel_17.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			this.panel_17.setLayout( gbl_panel_17 );

			GridBagConstraints gbc_jCheckBoxSlide = new GridBagConstraints();
			gbc_jCheckBoxSlide.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxSlide.insets = new Insets(0, 0, 5, 5);
			gbc_jCheckBoxSlide.gridx = 0;
			gbc_jCheckBoxSlide.gridy = 0;
			this.panel_17.add( this.getJCheckBoxSlide(), gbc_jCheckBoxSlide);

			GridBagConstraints gbc_jCheckBoxSound = new GridBagConstraints();
			gbc_jCheckBoxSound.insets = new Insets(0, 0, 5, 0);
			gbc_jCheckBoxSound.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxSound.gridwidth = 2;
			gbc_jCheckBoxSound.gridx = 1;
			gbc_jCheckBoxSound.gridy = 0;
			this.panel_17.add( this.getJCheckBoxSound(), gbc_jCheckBoxSound);

			GridBagConstraints gbc_jCheckBoxTimeUntilSoundEnd = new GridBagConstraints();
			//gbc_jCheckBoxTimeUntilSoundEnd.insets = new Insets(0, 0, 5, 0);
			//gbc_jCheckBoxTimeUntilSoundEnd.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxTimeUntilSoundEnd.gridwidth = 3;
			gbc_jCheckBoxTimeUntilSoundEnd.fill = GridBagConstraints.HORIZONTAL;
			gbc_jCheckBoxTimeUntilSoundEnd.gridx = 0;
			gbc_jCheckBoxTimeUntilSoundEnd.gridy = 1;
			this.panel_17.add( this.getJCheckBoxTimeUntilSoundEnd(), gbc_jCheckBoxTimeUntilSoundEnd);

			GridBagConstraints gbc_SamSetSizeLabel = new GridBagConstraints();
			gbc_SamSetSizeLabel.gridwidth = 1;
			gbc_SamSetSizeLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_SamSetSizeLabel.gridx = 0;
			gbc_SamSetSizeLabel.gridy = 2;			
			this.panel_17.add( new JLabel( "S.A.M. Set Size " ), gbc_SamSetSizeLabel);

			GridBagConstraints gbc_SamSetSize = new GridBagConstraints();
			gbc_SamSetSize.gridwidth = 1;
			gbc_SamSetSize.fill = GridBagConstraints.HORIZONTAL;
			gbc_SamSetSize.gridx = 1;
			gbc_SamSetSize.gridy = 2;
			this.panel_17.add( this.getJSpinnerSamSetSize(), gbc_SamSetSize);

			GridBagConstraints gbc_SamSetEdit = new GridBagConstraints();
			gbc_SamSetEdit.gridwidth = 1;
			gbc_SamSetEdit.fill = GridBagConstraints.WEST;
			gbc_SamSetEdit.gridx = 2;
			gbc_SamSetEdit.gridy = 2;
			this.panel_17.add( this.getJButtonEditSAMTest(), gbc_SamSetEdit );

			GridBagConstraints gbc_SamBeep = new GridBagConstraints();
			gbc_SamBeep.gridwidth = 1;
			gbc_SamBeep.fill = GridBagConstraints.WEST;
			gbc_SamBeep.gridx = 3;
			gbc_SamBeep.gridy = 2;
			this.panel_17.add( this.getSAMBeep(), gbc_SamBeep );
			
			GridBagConstraints gbc_SamDominance = new GridBagConstraints();
			gbc_SamDominance.gridwidth = 3;
			gbc_SamDominance.fill = GridBagConstraints.HORIZONTAL;
			gbc_SamDominance.gridx = 0;
			gbc_SamDominance.gridy = 3;
			this.panel_17.add( this.getJCheckboxSamDominance(), gbc_SamDominance);
			
			GridBagConstraints gbc_jButtonSlideConfig = new GridBagConstraints();
			gbc_jButtonSlideConfig.gridwidth = 3;
			gbc_jButtonSlideConfig.fill = GridBagConstraints.HORIZONTAL;
			gbc_jButtonSlideConfig.gridx = 0;
			gbc_jButtonSlideConfig.gridy = 4;
			gbc_jButtonSlideConfig.insets = new Insets( 5, 2, 0, 0 );
			this.panel_17.add( this.getJButtonSlideConfig(), gbc_jButtonSlideConfig);					
		}

		return this.panel_17;
	}

	private JCheckBox getJCheckboxSamDominance() 
	{
		if ( this.jCheckBoxSamDominance == null) 
		{
			final String ID = ConfigApp.IS_SAM_DOMINANCE;

			this.jCheckBoxSamDominance = new JCheckBox( );
			this.jCheckBoxSamDominance.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			this.jCheckBoxSamDominance.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED ));
			this.jCheckBoxSamDominance.setText("Dominance instead of emotions");

			this.jCheckBoxSamDominance.addItemListener( new ItemListener() 
			{	
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxSamDominance );
		}

		return this.jCheckBoxSamDominance;
	}
	
	private JSpinner getJSpinnerSamSetSize() 
	{
		if ( this.jSpinnerSamSetSize == null) 
		{			
			final String ID = ConfigApp.SAM_SET_SIZE;

			this.jSpinnerSamSetSize = new JSpinner(  );
			FontMetrics fm = this.getJSpinnerPreRun().getFontMetrics( this.getJSpinnerPreRun().getFont());

			this.jSpinnerSamSetSize.setPreferredSize( new Dimension( fm.stringWidth("1")*7, this.jSpinnerSamSetSize.getPreferredSize().height ) );
			this.jSpinnerSamSetSize.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			this.jSpinnerSamSetSize.setModel(new SpinnerNumberModel( (Integer)ConfigApp.getProperty( ID ),
					new Integer(1), null , new Integer(1)));

			this.jSpinnerSamSetSize.setFont(new Font("Dialog", Font.BOLD, 12));
			this.jSpinnerSamSetSize.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerSamSetSize.addChangeListener( new ChangeListener()
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});		

			this.jSpinnerSamSetSize.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerSamSetSize );
		}

		return jSpinnerSamSetSize;
	}

	private JButton getJButtonEditSAMTest()
	{
		if( this.jButtonEditSAM == null ) 
		{
			this.jButtonEditSAM = new JButton( );
			
			Icon ic =  GeneralAppIcon.Pencil( this.jButtonEditSAM.getPreferredSize().height, Color.BLACK );			 
				
 			if( ic != null )
			{
				this.jButtonEditSAM.setIcon( ic );
			}
			else
			{
				this.jButtonEditSAM.setText( "Edit" );
			}
			
			this.jButtonEditSAM.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JButton b = (JButton)e.getSource();
					
					JDialog jDialogSAMEditor  = new settingMenu_SAMEditor( SwingUtilities.getWindowAncestor( b ), "SAM editor" );

					jDialogSAMEditor.setVisible( false );
					jDialogSAMEditor.pack();
					jDialogSAMEditor.validate();

					jDialogSAMEditor.setLocation( getJDialogPosition( jDialogSAMEditor ) );

					jDialogSAMEditor.toFront();		
					jDialogSAMEditor.setResizable( true );
					jDialogSAMEditor.setModal( true );
					jDialogSAMEditor.setVisible( true );
				}
			});
		}
				
		return this.jButtonEditSAM;
	}
	
	private JToggleButton getSAMBeep()
	{
		if( this.jToggleButtonBoxSamBeep == null )
		{
			final String ID = ConfigApp.SAM_BEEP_ACTIVE;
						
			this.jToggleButtonBoxSamBeep = new JToggleButton();
			this.jToggleButtonBoxSamBeep.setBorder( BorderFactory.createRaisedBevelBorder() );
						
			Icon ic =  GeneralAppIcon.Sound( 16, 16, Color.BLACK, null );	
						
 			if( ic != null )
			{
				this.jToggleButtonBoxSamBeep.setIcon( ic );
			}
			else
			{
				this.jToggleButtonBoxSamBeep.setText( "Beep" );
			}			
			
			this.jToggleButtonBoxSamBeep.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
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
					
					ConfigApp.setProperty( ID, bt.isSelected());
				}
			});
			
			this.parameters.put( ID, this.jToggleButtonBoxSamBeep );
		}
		
		return this.jToggleButtonBoxSamBeep;
	}
	
	private JSpinner getJSpinnerScreens() 
	{
		if ( this.jSpinnerScreens == null) 
		{			
			final String ID = ConfigApp.SELECTED_SCREEN;

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] gs = ge.getScreenDevices();
			
			this.jSpinnerScreens = new JSpinner(  );
			FontMetrics fm = this.getJSpinnerPreRun().getFontMetrics( this.getJSpinnerPreRun().getFont());

			this.jSpinnerScreens.setPreferredSize( new Dimension( fm.stringWidth("1")*7, this.jSpinnerSamSetSize.getPreferredSize().height ) );
			this.jSpinnerScreens.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			
			Integer screen = (Integer)ConfigApp.getProperty( ID );
			
			if( screen < 1 || screen > gs.length )
			{
				screen = 1;
			}
			
			this.jSpinnerScreens.setModel(new SpinnerNumberModel( screen,
					new Integer(1), new Integer( gs.length ), new Integer(1) ) );

			this.jSpinnerScreens.setFont(new Font("Dialog", Font.BOLD, 12));
			this.jSpinnerScreens.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerScreens.addChangeListener( new ChangeListener()
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});		

			this.jSpinnerScreens.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerScreens );
		}

		return jSpinnerScreens;
	}
	
	protected void updateSockectStatusText()
	{
		if( this.jpanelSocket != null )
		{
			Color in = Color.RED;
			Color out = Color.RED;

			if( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SOCKET_SERVER_ACTIVE ) )
			{
				in = Color.GREEN;
			}

			if( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SOCKET_CLIENT_ACTIVE ) )
			{
				out = Color.GREEN;
			}


			int i = 0;
			boolean enc = false;			
			while( i < this.jpanelSocket.getComponentCount() && !enc )
			{
				enc = this.settingMenu.getTitleAt( i ).equals( this.jpanelSocket.getName() );
				if( !enc )
				{
					i++;
				}
			}

			if( enc )
			{
				ImageIcon ico = null;

				try
				{
					ico = GeneralAppIcon.Socket( in, out );					
				}
				catch( Exception e )
				{
				}
				catch( Error e )
				{					
				}

				this.settingMenu.setIconAt( i, ico );
			}
		}
	}

	private JPanel getPanel_Task()
	{
		if( this.panel_Task == null )
		{
			this.panel_Task = new JPanel();

			this.panel_Task.setLayout(new BoxLayout( this.panel_Task, BoxLayout.X_AXIS));
			this.panel_Task.add( this.getJComboboxTipoOper());
			this.panel_Task.add( this.getJListDificultad());			
		}

		return panel_Task;
	}

	private JPanel getPanel_Aux()
	{
		if( this.panelAux == null )
		{
			this.panelAux = new JPanel();
			this.panelAux.setLayout(new BorderLayout(0, 0));
			this.panelAux.add(getJPanelPrePosRun(), BorderLayout.WEST);
			this.panelAux.add(getPanel_13(), BorderLayout.CENTER);
		}

		return this.panelAux;
	}

	private JPanel getPanel_Aux2()
	{
		if( this.panelAux2 == null )
		{
			this.panelAux2 = new JPanel();
			this.panelAux2.setLayout(new BorderLayout(0, 0));
			this.panelAux2.add( this.getJPanelLog(), BorderLayout.NORTH);
			//panelAux2.add( getJLabelStatus(), BorderLayout.SOUTH );
		}

		return this.panelAux2;
	}

	private JCheckBox getJCheckBoxTiempoTest() 
	{
		if ( this.jCheckBoxTiempoTest == null) 
		{
			final String ID = ConfigApp.IS_TIME_TEST_ACTIVED;

			this.jCheckBoxTiempoTest = new JCheckBox( );
			this.jCheckBoxTiempoTest.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			this.jCheckBoxTiempoTest.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED ));
			this.jCheckBoxTiempoTest.setText("Time Test (minuts)");

			this.jCheckBoxTiempoTest.addItemListener( new ItemListener() 
			{	
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxTiempoTest );
		}

		return this.jCheckBoxTiempoTest;
	}

	private JSpinner getJSpinnerTimeTest() 
	{
		if ( this.jSpinnerTimeTest == null) 
		{
			final String ID = ConfigApp.VALUE_TEST_TIME;

			this.jSpinnerTimeTest = new JSpinner(  );
			this.jSpinnerTimeTest.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			this.jSpinnerTimeTest.setModel(new SpinnerNumberModel( (Long)ConfigApp.getProperty( ID ), 
					new Long( 1 ), null , new Long( 1 ) ) );

			this.jSpinnerTimeTest.setFont(new Font("Dialog", Font.BOLD, 12));

			this.jSpinnerTimeTest.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerTimeTest.addChangeListener( new ChangeListener() 
			{	
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );
				}
			});

			this.jSpinnerTimeTest.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerTimeTest );
		}

		return this.jSpinnerTimeTest;
	}

	private JCheckBox getJCheckBoxRepeticiones()
	{
		if ( this.jCheckBoxRepeticiones == null) 
		{
			final String ID = ConfigApp.IS_REPETITION_ACTIVE;

			this.jCheckBoxRepeticiones = new JCheckBox();
			this.jCheckBoxRepeticiones.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			this.jCheckBoxRepeticiones.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED ));
			this.jCheckBoxRepeticiones.setText("Repetitions");

			this.jCheckBoxRepeticiones.addItemListener( new ItemListener( ) 
			{
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxRepeticiones );
		}

		return this.jCheckBoxRepeticiones;
	}

	private JSpinner getJSpinnerRepeats() 
	{
		if ( this.jSpinnerRepeats == null) 
		{
			final String ID = ConfigApp.VALUE_REPETITION;

			this.jSpinnerRepeats = new JSpinner(  );
			this.jSpinnerRepeats.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );

			this.jSpinnerRepeats.setModel( new SpinnerNumberModel( (Integer)ConfigApp.getProperty( ID ),
					new Integer( 1 ), null , new Integer( 1 ) ) );

			this.jSpinnerRepeats.setFont(new Font("Dialog", Font.BOLD, 12));
			this.jSpinnerRepeats.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerRepeats.addChangeListener( new ChangeListener() 
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});

			this.jSpinnerRepeats.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerRepeats );
		}

		return this.jSpinnerRepeats;
	}

	/*
	private JButton getJButtonSaveConfig()
	{
		if ( this.jButtonSaveConfig == null) 
		{
			this.jButtonSaveConfig = new JButton();
			this.jButtonSaveConfig.setText("Save");
			//jButtonSaveConfig.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED));

			this.jButtonSaveConfig.setIcon( UIManager.getIcon( "FileView.floppyDriveIcon" ) );

			this.jButtonSaveConfig.addActionListener(new ActionListener()   
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					guiManager.getInstance().saveFileConfig();
				}
			});
		}

		return this.jButtonSaveConfig;
	}
	 */

	private JPanel getJPanelTimeOut() 
	{
		if ( this.jPanelTimeOut == null) 
		{	
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			//gridBagConstraints10.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.gridx = 1;

			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;


			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;

			GridBagConstraints gbc_jPanelTimeFile = new GridBagConstraints();
			gbc_jPanelTimeFile.gridx = 1;
			gbc_jPanelTimeFile.fill = GridBagConstraints.HORIZONTAL;

			this.jPanelTimeOut = new JPanel();
			GridBagLayout gbl_jPanelTimeOut = new GridBagLayout();
			gbl_jPanelTimeOut.columnWeights = new double[]{0.0, 0.0};
			this.jPanelTimeOut.setLayout(gbl_jPanelTimeOut);
			this.jPanelTimeOut.setBorder( new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Timers"));

			GridBagConstraints gbc_jLabelTimeMemory = new GridBagConstraints();
			gbc_jLabelTimeMemory.anchor = GridBagConstraints.WEST;
			gbc_jLabelTimeMemory.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelTimeMemory.gridx = 0;
			gbc_jLabelTimeMemory.gridy = 2;

			GridBagConstraints gbc_jFormattedTimeMemory = new GridBagConstraints();
			gbc_jFormattedTimeMemory.fill = GridBagConstraints.HORIZONTAL;
			gbc_jFormattedTimeMemory.gridx = 1;
			gbc_jFormattedTimeMemory.gridy = 2;

			//Group the radio buttons.			
			this.getGroupJRadioButton().add( this.getJRadioButtonTimeOut() );
			this.getGroupJRadioButton().add( this.getJRadioButtonTimeOutAuto() );

			this.jPanelTimeOut.add( this.getJRadioButtonTimeOutAuto(), gridBagConstraints8);
			this.jPanelTimeOut.add( this.getJPanelTimeFile(), gbc_jPanelTimeFile);
			this.jPanelTimeOut.add( this.getJRadioButtonTimeOut(), gridBagConstraints9);
			this.jPanelTimeOut.add( this.getJSpinnerTimeOut(), gridBagConstraints10);
			this.jPanelTimeOut.add( this.getJLabelTimeMemory(), gbc_jLabelTimeMemory );
			//this.jPanelTimeOut.add( this.getJSpinnerSecondaryTimer(), gbc_jFormattedTimeMemory );
			this.jPanelTimeOut.add( this.getMultipleNumberSpinner(), gbc_jFormattedTimeMemory );
		}
		return this.jPanelTimeOut;
	}

	private NoneSelectedButtonGroup getGroupJRadioButton()
	{
		if( this.groupJRadioButton ==  null )
		{
			this.groupJRadioButton = new NoneSelectedButtonGroup();
		}

		return this.groupJRadioButton;
	}

	/*
	private JButton getJButtonLoadConfig()
	{
		if ( this.jButtonLoadConfig == null) 
		{
			this.jButtonLoadConfig = new JButton();
			this.jButtonLoadConfig.setText("Load");
			//jButtonLoadConfig.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED));	

			Icon ico = UIManager.getIcon( "FileChooser.upFolderIcon" );
			Icon ico2 = UIManager.getIcon( "FileView.floppyDriveIcon" );

			if( ico instanceof ImageIcon )
			{
				Image img = ((ImageIcon) ico ).getImage();
				ico = new ImageIcon( img.getScaledInstance( ico2.getIconWidth(), ico2.getIconHeight(), Image.SCALE_SMOOTH ) );
			}
			else
			{
				ico = null;
			}

			this.jButtonLoadConfig.setIcon( ico );

			this.jButtonLoadConfig.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					guiManager.getInstance().loadFileConfig();

					loadConfigValues();
				}
			});
		}
		return this.jButtonLoadConfig;
	}
	 */

	private JTextField getJTextField_OutputFolder()
	{
		if( this.jTextField_OutputFolder == null )
		{
			final String ID = ConfigApp.OUTPUT_FOLDER;

			this.jTextField_OutputFolder = new JTextField( ConfigApp.getProperty( ID ).toString() );
			this.jTextField_OutputFolder.setFont( new Font( Font.DIALOG, Font.BOLD, 12) );
			this.jTextField_OutputFolder.setColumns( 25 );

			this.jTextField_OutputFolder.getDocument().addDocumentListener( new DocumentListener() 
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
						ConfigApp.setProperty( ID, e.getDocument().getText( 0, e.getDocument().getLength() ) );
					}
					catch (BadLocationException e1) 
					{
						e1.printStackTrace();
					}
				}
			});

			this.parameters.put( ID, this.jTextField_OutputFolder );
		}

		return this.jTextField_OutputFolder;
	}

	private JTextField getJTextField_LogFilePath()
	{
		if( this.jTextField_LogFilePath == null )
		{
			final String ID = ConfigApp.LOG_FILE_NAME;

			this.jTextField_LogFilePath = new JTextField( ConfigApp.getProperty( ID ).toString() );
			this.jTextField_LogFilePath.setFont( new Font( Font.DIALOG, Font.BOLD, 12) );

			Dimension d = this.jTextField_LogFilePath.getPreferredSize();
			d.width *= 2;

			this.jTextField_LogFilePath.setPreferredSize( d );

			this.jTextField_LogFilePath.getDocument().addDocumentListener( new DocumentListener() 
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
						ConfigApp.setProperty( ID, e.getDocument().getText( 0, e.getDocument().getLength() ) );
					}
					catch (BadLocationException e1) 
					{
						e1.printStackTrace();
					}
				}
			});

			this.parameters.put( ID, this.jTextField_LogFilePath );
		}

		return this.jTextField_LogFilePath;
	}

	private JPanel getJPanelTrajectoryAxis()
	{
		if( this.jPanelTrajectoryAxis == null )
		{
			this.jPanelTrajectoryAxis = new JPanel();
			this.jPanelTrajectoryAxis.setBorder( BorderFactory.createTitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Trajectory: Inverted Axes" ) );
			this.jPanelTrajectoryAxis.setLayout( new BorderLayout() );

			this.jPanelTrajectoryAxis.add( getJChkbxTrajectoryXAxis(), BorderLayout.WEST );
			this.jPanelTrajectoryAxis.add( getJChkbxTrajectoryYAxis(), BorderLayout.CENTER );			
		}

		return this.jPanelTrajectoryAxis;
	}

	private JCheckBox getJChkbxTrajectoryXAxis()
	{
		if( this.jChkbxTrajectoryXAxis == null )
		{
			final String ID = ConfigApp.TRAJECTORY_INVERTED_XAXIS;

			this.jChkbxTrajectoryXAxis = new JCheckBox( "X Axis" );
			this.jChkbxTrajectoryXAxis.setSelected((Boolean)ConfigApp.getProperty( ID ) );

			this.jChkbxTrajectoryXAxis.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});	

			this.parameters.put( ID, this.jChkbxTrajectoryXAxis );
		}

		return this.jChkbxTrajectoryXAxis;
	}

	private JCheckBox getJChkbxTrajectoryYAxis()
	{
		if( this.jChkbxTrajectoryYAxis == null )
		{
			final String ID = ConfigApp.TRAJECTORY_INVERTED_YAXIS;

			this.jChkbxTrajectoryYAxis = new JCheckBox( "Y Axis" );
			this.jChkbxTrajectoryYAxis.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jChkbxTrajectoryYAxis.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jChkbxTrajectoryYAxis );
		}

		return this.jChkbxTrajectoryYAxis;
	}

	private JCheckBox getJCheckBoxSlide() 
	{
		if ( this.jCheckBoxSlide == null) 
		{
			final String ID = ConfigApp.IS_SELECTED_SLIDE;

			this.jCheckBoxSlide = new JCheckBox("Slide");

			this.jCheckBoxSlide.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxSlide.addItemListener(new ItemListener() 
			{
				public void itemStateChanged(ItemEvent arg0) 
				{	
					ConfigApp.setProperty( ID, ((JCheckBox)arg0.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxSlide );
		}

		return this.jCheckBoxSlide;
	}

	private JCheckBox getJCheckBoxSound() 
	{
		if ( this.jCheckBoxSound == null) 
		{
			final String ID = ConfigApp.IS_SELECTED_SOUND;

			this.jCheckBoxSound = new JCheckBox("Sounds");

			this.jCheckBoxSound.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxSound.addItemListener(new ItemListener() 
			{
				public void itemStateChanged(ItemEvent arg0) 
				{
					JCheckBox c = (JCheckBox)arg0.getSource();

					//getJCheckBoxTimeUntilSoundEnd().setEnabled( c.isSelected() );
					getJCheckBoxTimeUntilSoundEnd().setEnabled( false );

					ConfigApp.setProperty( ID, c.isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxSound );
		}

		return this.jCheckBoxSound;
	}

	private JCheckBox getJCheckBoxTimeUntilSoundEnd() 
	{
		if ( this.jCheckBoxTimeUntilSoundEnd == null) 
		{
			final String ID = ConfigApp.IS_SELECTED_TIME_UNTIL_SOUND_END;

			this.jCheckBoxTimeUntilSoundEnd = new JCheckBox("Slide until sound is finished");
			this.jCheckBoxTimeUntilSoundEnd.setEnabled( false );

			this.jCheckBoxTimeUntilSoundEnd.setSelected((Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxTimeUntilSoundEnd.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					JCheckBox c = (JCheckBox)e.getSource();
					c.setSelected( false );
					ConfigApp.setProperty( ID, c.isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxTimeUntilSoundEnd );
		}

		return jCheckBoxTimeUntilSoundEnd;
	}

	private Point getJDialogPosition( JDialog dialog )
	{
		Point l = windowOwner.getLocation();
		Dimension d = windowOwner.getSize();

		Dimension dd = Toolkit.getDefaultToolkit().getScreenSize();

		dd.width /= 2;
		dd.height /= 2;

		dialog.setSize( dd );

		Point loc = new Point( l.x + d.width / 2 - dd.width / 2 , l.y + d.height / 2 - dd.height / 2 );					

		Insets ssooPAD = Toolkit.getDefaultToolkit().getScreenInsets( windowOwner.getGraphicsConfiguration() );
		if( loc.x < ssooPAD.left + 1)
		{
			loc.x = ssooPAD.left + 1;
		}

		if( loc.y < ssooPAD.top + 1 )
		{
			loc.y = ssooPAD.top + 1;
		}

		return loc;
	}

	private JButton getJButtonSlideConfig() 
	{
		if ( this.jButtonSlideConfig == null) 
		{
			this.jButtonSlideConfig = new JButton("Affective Task Config");

			this.jButtonSlideConfig.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					JButton b = (JButton)e.getSource();

					//JDialog jDialogSlide  = new settingMenu_AffectiveSetting( SwingUtilities.getWindowAncestor( b ), b.getText() );
					JDialog jDialogSlide  = new settingMenu_AffectiveSetting( SwingUtilities.getWindowAncestor( b ), b.getText() );

					jDialogSlide.setVisible( false );
					jDialogSlide.pack();
					jDialogSlide.validate();

					jDialogSlide.setLocation( getJDialogPosition( jDialogSlide ) );

					jDialogSlide.toFront();		
					jDialogSlide.setResizable( true );
					jDialogSlide.setVisible( true );
				}
			});
		}

		return this.jButtonSlideConfig;
	}

	private JButton getjButtonUsersResults() 
	{
		if ( this.jButtonUsersResults == null) 
		{
			ImageIcon ico = null;
			try
			{
				//ico = new ImageIcon( Toolkit.getDefaultToolkit().getImage( settingMenu_UserResultSetting.class.getResource( "/com/sun/javafx/scene/web/skin/FontBackgroundColor_16x16_JFX.png") ) );
				//ico = GeneralAppIcon.getPencilIcon( 20, 20, Color.BLACK, Color.ORANGE );
				ico = GeneralAppIcon.Pencil( 16, Color.BLACK );
			}
			catch( Exception e )
			{}
			catch( Error e)
			{}

			this.jButtonUsersResults = new JButton( ico );
			if( ico == null 
					|| ico.getIconWidth() == 0 
					|| ico.getIconHeight() == 0 )
			{				
				this.jButtonUsersResults.setText( "Edit" );
			}
			else
			{	
				this.jButtonUsersResults.setPreferredSize( new Dimension( 20, 16 ) );
			}

			this.jButtonUsersResults.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					JButton b = (JButton)e.getSource();

					JDialog jDialogCfgUsersResults = new settingMenu_UserResultSetting( SwingUtilities.getWindowAncestor( b ), b.getText() );
					jDialogCfgUsersResults.setVisible( false );
					jDialogCfgUsersResults.pack();
					jDialogCfgUsersResults.validate();
					jDialogCfgUsersResults.setTitle( ConfigApp.getProperty( ConfigApp.PATH_FILE_TIME_OUT_AUTO ).toString() );

					Dimension dd = new Dimension( 550, 200 );
					jDialogCfgUsersResults.setPreferredSize( dd );
					jDialogCfgUsersResults.setSize( dd );

					jDialogCfgUsersResults.setLocation( getJDialogPosition( jDialogCfgUsersResults ) );

					jDialogCfgUsersResults.setResizable( true );
					jDialogCfgUsersResults.setVisible( true );
					jDialogCfgUsersResults.setSize( dd );					
				}
			});

		}
		return this.jButtonUsersResults;
	}

	private JComboBox< String > getJComboboxTipoOper() 
	{
		if ( this.jComboboxTipoOper == null) 
		{
			final String ID = ConfigApp.TASK_INDEX_SELECTED;

			this.jComboboxTipoOper = new JComboBox< String >();
			//jListTipoOper.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//jComboboxTipoOper.setSelectedIndex(0);		
			//jListTipoOper.setLayoutOrientation( JList.VERTICAL );
			//jListTipoOper.setVisibleRowCount( 2 );
			this.jComboboxTipoOper.setBorder( new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Task", TitledBorder.LEADING, TitledBorder.TOP, null, null));

			//String[] t = IActivity.activities;
			String[] t = ActivityRegister.getActivitiesID();

			DefaultComboBoxModel< String > list = new DefaultComboBoxModel< String >();

			for( int i = 0; i < t.length; i++ )
			{
				list.addElement( t[ i ] );
			}


			this.jComboboxTipoOper.setModel( list );
			this.jComboboxTipoOper.setSelectedIndex( (Integer)ConfigApp.getProperty( ID ) );

			this.jComboboxTipoOper.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					JComboBox< String > cmb = (JComboBox< String >)e.getSource();

					ConfigApp.setProperty( ID, cmb.getSelectedIndex() );

					String actID = ActivityRegister.getActivityID( cmb.getSelectedIndex() );

					/*
					if( cmb.getSelectedItem().toString().equals( IActivity.OpAffective )
							|| cmb.getSelectedItem().toString().equals( IActivity.OpSAM ) )
					 */
					if( !ActivityRegister.isChallegeActivity( ActivityRegister.getActivityID( cmb.getSelectedIndex() ) ) )
					{
						appUI.getInstance().getJPanelObjetivo().setVisible( false );
					}
					else
					{
						appUI.getInstance().getJPanelObjetivo().setVisible( true );
					}

					//String actID = cmb.getSelectedItem().toString();
					showPluginTab( ActivityRegister.isPluginActivity( actID ), actID );
				}
			});

			this.jComboboxTipoOper.addMouseWheelListener( new MouseWheelListener() 
			{				
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) 
				{
					if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
					{
						try
						{
							JComboBox< String > c = ( JComboBox< String > )e.getSource();

							int d = c.getSelectedIndex() + e.getWheelRotation();

							if( d >= 0 && d < c.getItemCount() )
							{
								c.setSelectedIndex( d );

								String actID = c.getSelectedItem().toString();
								showPluginTab( ActivityRegister.isPluginActivity( actID ), actID );
							}
						}
						catch( IllegalArgumentException ex )
						{

						}
					}
				}
			});

			this.parameters.put( ID, this.jComboboxTipoOper );
		}
		return jComboboxTipoOper;
	}

	private void showPluginTab( boolean show, String activityID )
	{
		JTabbedPane jtp = getJTabbedPaneSettingMenu();

		if( !show )
		{
			boolean remove = false;
			for( int i = 0; i < jtp.getTabCount() && !remove; i++ )
			{
				remove = jtp.getTitleAt( i ).equals( settingMenu_PluginConfig.PANEL_TITLE );

				if( remove )
				{
					jtp.removeTabAt( i );
				}
			}
		}
		else
		{
			jtp.addTab( settingMenu_PluginConfig.PANEL_TITLE
					, this.getPluginConfigPanel( activityID ) );
		}
	}

	private settingMenu_PluginConfig getPluginConfigPanel( String activityID )
	{
		if( this.jPanelPluginConfig == null )
		{

			this.jPanelPluginConfig = new settingMenu_PluginConfig( this );
		}

		PluginConfig cfg = ActivityRegister.getActivityConfig( activityID );

		this.jPanelPluginConfig.setPluginConfig( cfg );

		return this.jPanelPluginConfig;
	}

	private JComboBox< Integer > getJListDificultad() 
	{
		if ( this.jComboboxDificultad == null) 
		{
			final String ID = ConfigApp.DIFICULTY_INDEX_SELECTED;

			this.jComboboxDificultad = new JComboBox< Integer >();
			//jListDificultad.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//jComboboxDificultad.setSelectedIndex(0);
			//jListDificultad.setLayoutOrientation( JList.VERTICAL );
			//jListDificultad.setVisibleRowCount( 2 );
			this.jComboboxDificultad.setBorder( new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Dificulty", TitledBorder.LEADING, TitledBorder.TOP, null, null));

			DefaultComboBoxModel< Integer > list = new DefaultComboBoxModel< Integer >(); 

			for( int i = 1; i <= IActivity.NUMBER_DIFICUL_LEVELS; i++ )
			{
				list.addElement( i );
			}		

			this.jComboboxDificultad.setModel( list );
			this.jComboboxDificultad.setSelectedIndex( (Integer)ConfigApp.getProperty( ID ) );

			this.jComboboxDificultad.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JComboBox< Integer >)e.getSource()).getSelectedIndex() );
				}
			});

			this.jComboboxDificultad.addMouseWheelListener( new MouseWheelListener() 
			{				
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) 
				{
					if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
					{
						try
						{
							JComboBox< Integer > c = ( JComboBox< Integer > )e.getSource();

							int d = c.getSelectedIndex() + e.getWheelRotation();

							if( d >= 0 && d < c.getItemCount() )
							{
								c.setSelectedIndex( d );
							}
						}
						catch( IllegalArgumentException ex )
						{

						}
					}
				}
			});

			this.parameters.put( ID, this.jComboboxDificultad );
		}
		return this.jComboboxDificultad;
	}

	private JPanel getJPanelPrePosRun() 
	{
		if ( this.jPanelPrePosRun == null) 
		{	
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();			
			gridBagConstraints14.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.anchor = GridBagConstraints.EAST;

			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();			
			gridBagConstraints13.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.anchor = GridBagConstraints.EAST;

			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();			
			gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();			
			gridBagConstraints11.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;

			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.anchor = GridBagConstraints.EAST;

			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();			
			gridBagConstraints9.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;

			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints8.gridx = 0;			
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;

			this.jPanelPrePosRun = new JPanel();
			this.jPanelPrePosRun.setBorder( new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Pre/Pos-Run Time"));

			this.jPanelPrePosRun.setLayout( new GridBagLayout());

			this.jPanelPrePosRun.add( this.getJLabelPreRun(), gridBagConstraints8 );
			this.jPanelPrePosRun.add( this.getJSpinnerPreRun(), gridBagConstraints9 );
			this.jPanelPrePosRun.add(this.getJCheckBoxPreTimerCountDown(), gridBagConstraints10 );

			this.jPanelPrePosRun.add( this.getJLabelPosRun(), gridBagConstraints11 );
			this.jPanelPrePosRun.add( this.getJSpinnerPosRun(), gridBagConstraints12 );
			this.jPanelPrePosRun.add( this.getJCheckBoxPostTimerCountDown(), gridBagConstraints13 );
			this.jPanelPrePosRun.add( this.getJCheckboxPrePosBlackBackground(), gridBagConstraints14 );
		}
		return jPanelPrePosRun;
	}

	private JLabel getJLabelPosRun()
	{
		if( this.jLabelPosRun == null)
		{
			this.jLabelPosRun = new JLabel();
			this.jLabelPosRun.setText("PosRun Time (s)");
		}

		return this.jLabelPosRun;
	}

	private JLabel getJLabelPreRun()
	{
		if( this.jLabelPreRun == null)
		{
			this.jLabelPreRun = new JLabel();
			this.jLabelPreRun.setText("PreRun Time (s)");
		}

		return this.jLabelPreRun;
	}

	private JPanel getJPanelLog( )
	{
		if( this.jPanelLog == null )
		{
			this.jPanelLog = new JPanel();
			this.jPanelLog.setLayout( new BorderLayout() );
			this.jPanelLog.setBorder( BorderFactory.createEmptyBorder( 5, 0, 5, 0) );

			this.jPanelLog.add( this.getJButtonLogFilePath(), BorderLayout.WEST );
			this.jPanelLog.add( this.getJTextField_OutputFolder(), BorderLayout.CENTER );
			this.jPanelLog.add( this.getJPanelLogFileName(), BorderLayout.EAST );			
			this.jPanelLog.add( this.getPanel_1(), BorderLayout.SOUTH);
		}

		return this.jPanelLog;
	}

	private JPanel getJPanelLogFileName()
	{
		if( this.jPanelLogFileName == null )
		{
			this.jPanelLogFileName = new JPanel( new BorderLayout() );

			this.jPanelLogFileName.add( this.getJCheckBoxchckbxLog(), BorderLayout.WEST );
			this.jPanelLogFileName.add( this.getJTextField_LogFilePath(), BorderLayout.CENTER );
		}

		return this.jPanelLogFileName;
	}

	private JRadioButton getJRadioButtonTimeOut() 
	{
		if ( this.jRadioButtonTimeOutManual == null) 
		{
			final String ID = ConfigApp.IS_TIME_OUT_MANUAL_ACTIVE;

			this.jRadioButtonTimeOutManual = new JRadioButton();
			this.jRadioButtonTimeOutManual.setSelected( (Boolean)ConfigApp.getProperty( ID ) );
			this.jRadioButtonTimeOutManual.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED ));			
			this.jRadioButtonTimeOutManual.setText("Manual (s)");

			this.jRadioButtonTimeOutManual.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JRadioButton)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jRadioButtonTimeOutManual );
		}

		return this.jRadioButtonTimeOutManual;
	}

	private JRadioButton getJRadioButtonTimeOutAuto() 
	{
		if ( this.jRadioButtonTimeOutAuto == null) 
		{
			final String ID = ConfigApp.IS_TIME_OUT_AUTO_ACTIVE;

			this.jRadioButtonTimeOutAuto = new JRadioButton();			
			this.jRadioButtonTimeOutAuto.setText("Auto");
			this.jRadioButtonTimeOutAuto.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jRadioButtonTimeOutAuto.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JRadioButton)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jRadioButtonTimeOutAuto );
		}

		return jRadioButtonTimeOutAuto;
	}

	private JPanel getJPanelTimeFile() 
	{
		if ( this.jPanelTimeFile == null) 
		{			
			this.jPanelTimeFile = new JPanel();
			this.jPanelTimeFile.setLayout(new BoxLayout( this.jPanelTimeFile, BoxLayout.X_AXIS ) );

			this.jPanelTimeFile.add( this.getjButtonUsersResults() );
			this.jPanelTimeFile.add( Box.createRigidArea( new Dimension( 3, 0 ) ) );
			this.jPanelTimeFile.add( this.getJTextFieldPathTimeFile() );
			this.jPanelTimeFile.add( this.getJButtonSelectFile() );
		}

		return this.jPanelTimeFile;
	}

	private JSpinner getJSpinnerTimeOut() 
	{
		if ( this.jSpinnerTimeOutManual == null) 
		{
			final String ID = ConfigApp.VALUE_TIME_OUT_MANUAL;

			this.jSpinnerTimeOutManual = new JSpinner(  );
			this.jSpinnerTimeOutManual.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			this.jSpinnerTimeOutManual.setModel( new SpinnerNumberModel( (Long)ConfigApp.getProperty( ID ), 
					new Long( 1 ), null , new Long( 1 ) ) );
			this.jSpinnerTimeOutManual.setFont(new Font("Dialog", Font.BOLD, 12));

			this.jSpinnerTimeOutManual.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerTimeOutManual.addChangeListener( new ChangeListener()
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});			

			this.jSpinnerTimeOutManual.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerTimeOutManual );
		}

		return this.jSpinnerTimeOutManual;
	}

	private JLabel getJLabelTimeMemory()
	{
		if( this.jLabelTimeMemory == null )
		{
			this.jLabelTimeMemory = new JLabel();
			//this.jLabelTimeMemory.setText( " Pre-answer Timer (s)");
			this.jLabelTimeMemory.setText( "Non-main-task List (s)");
		}

		return this.jLabelTimeMemory;
	}

	/*
	private JSpinner getJSpinnerSecondaryTimer() 
	{
		if ( this.jSpinnerSecondaryTimer == null) 
		{
			final String ID = ConfigApp.PRE_ANSWER_TIME;

			this.jSpinnerSecondaryTimer = new JSpinner(  );
			this.jSpinnerSecondaryTimer.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			this.jSpinnerSecondaryTimer.setModel(new SpinnerNumberModel( (Long)ConfigApp.getProperty( ID ),
																		new Long(0), null, new Long(1)));

			this.jSpinnerSecondaryTimer.setFont(new Font("Dialog", Font.BOLD, 12));
			this.jSpinnerSecondaryTimer.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerSecondaryTimer.addChangeListener( new ChangeListener()
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});			

			this.jSpinnerSecondaryTimer.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerSecondaryTimer );
		}


		return this.jSpinnerSecondaryTimer;
	}
	 */

	private JCheckBox getJCheckBoxPreTimerCountDown()
	{
		if( this.jCheckBoxPreTimerCountdownSound == null )
		{
			final String ID = ConfigApp.TIME_PRERUN_SOUND; 

			this.jCheckBoxPreTimerCountdownSound = new JCheckBox( "Sound" );
			this.jCheckBoxPreTimerCountdownSound.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxPreTimerCountdownSound.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, jCheckBoxPreTimerCountdownSound );
		}

		return this.jCheckBoxPreTimerCountdownSound;
	}

	private JCheckBox getJCheckBoxPostTimerCountDown()
	{
		if( this.jCheckBoxPostTimerCountdownSound == null )
		{
			final String ID = ConfigApp.TIME_POSRUN_SOUND; 

			this.jCheckBoxPostTimerCountdownSound = new JCheckBox( "Sound" );
			this.jCheckBoxPostTimerCountdownSound.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxPostTimerCountdownSound.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, jCheckBoxPostTimerCountdownSound );
		}

		return this.jCheckBoxPostTimerCountdownSound;
	}

	private JCheckBox getJCheckboxPrePosBlackBackground()
	{
		if( this.jCheckBoxPrePosBlackBackground == null )
		{
			final String ID = ConfigApp.PREPOS_BLACK_BACKGROUND; 

			this.jCheckBoxPrePosBlackBackground = new JCheckBox( "Black background" );

			this.jCheckBoxPrePosBlackBackground.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxPrePosBlackBackground );
		}

		return this.jCheckBoxPrePosBlackBackground;
	}

	private JSpinner getJSpinnerPreRun() 
	{
		if ( this.jSpinnerPreRun == null) 
		{
			final String ID = ConfigApp.TIME_PRERUN;

			NumberFormatter f = new NumberFormatter();
			f.setMinimum( new Long( 0 ));

			this.jSpinnerPreRun = new JSpinner( );
			FontMetrics fm = jSpinnerPreRun.getFontMetrics( this.jSpinnerPreRun.getFont());
			this.jSpinnerPreRun.setPreferredSize( new Dimension( fm.stringWidth("1")*7, this.jSpinnerPreRun.getPreferredSize().height ) );
			this.jSpinnerPreRun.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );

			this.jSpinnerPreRun.setModel( new SpinnerNumberModel( (Long)ConfigApp.getProperty( ID ), 
					new Long( 0 ), null, new Long( 1 ) ) );

			this.jSpinnerPreRun.setFont(new Font("Dialog", Font.BOLD, 12));
			this.jSpinnerPreRun.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerPreRun.addChangeListener( new ChangeListener()
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});

			this.jSpinnerPreRun.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerPreRun );
		}

		return this.jSpinnerPreRun;
	}

	private JSpinner getJSpinnerPosRun() 
	{
		if ( this.jSpinnerPosRun == null) 
		{			
			final String ID = ConfigApp.TIME_POSRUN;

			NumberFormatter f = new NumberFormatter();
			f.setMinimum( new Long( 0 ));

			this.jSpinnerPosRun = new JSpinner(  );
			FontMetrics fm = this.jSpinnerPreRun.getFontMetrics( this.jSpinnerPosRun.getFont());

			this.jSpinnerPreRun.setPreferredSize( new Dimension( fm.stringWidth("1")*7, this.jSpinnerPosRun.getPreferredSize().height ) );
			this.jSpinnerPosRun.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			this.jSpinnerPosRun.setModel(new SpinnerNumberModel( (Long)ConfigApp.getProperty( ID ),
					new Long(0), null , new Long(1)));

			this.jSpinnerPosRun.setFont(new Font("Dialog", Font.BOLD, 12));
			this.jSpinnerPosRun.addFocusListener( new FocusAdapter() 
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

			this.jSpinnerPosRun.addChangeListener( new ChangeListener()
			{				
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					ConfigApp.setProperty( ID, ((JSpinner)e.getSource()).getValue() );					
				}
			});		

			this.jSpinnerPosRun.addMouseWheelListener( new MouseWheelListener() 
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

			this.parameters.put( ID, this.jSpinnerPosRun );
		}

		return jSpinnerPosRun;
	}

	private JCheckBox getJCheckBoxchckbxLog()
	{
		if( this.chckbxLog == null )
		{
			final String ID = ConfigApp.IS_LOG_ACTIVE;

			this.chckbxLog = new JCheckBox( "Log: " );
			this.chckbxLog.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.chckbxLog.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.chckbxLog );
		}

		return this.chckbxLog;
	}

	private JButton getJButtonLogFilePath() 
	{
		if ( this.jButtonSelectLogFile == null) 
		{
			this.jButtonSelectLogFile = new JButton();
			//this.jButtonSelectLogFile.setText(" ... ");
			this.jButtonSelectLogFile.setIcon( GeneralAppIcon.Folder( 32, 22, Color.BLACK, this.colorIconLoadFolder) );
			this.jButtonSelectLogFile.setBorder( new LineBorder( Color.black ));
			
			this.jButtonSelectLogFile.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					String path[] = guiManager.getInstance().selectUserFile( ConfigApp.defaultPathFile, false, false, JFileChooser.DIRECTORIES_ONLY, "text", null );
					if( path != null )
					{
						getJTextField_OutputFolder().setText( path[ 0 ] );
					}
				}
			});
		}
		return this.jButtonSelectLogFile;
	}

	private JCheckBox getJCheckBoxEntrenamiento() 
	{
		if ( this.jCheckEntrenamiento == null) 
		{
			final String ID = ConfigApp.IS_TRAINING;

			this.jCheckEntrenamiento = new JCheckBox();
			this.jCheckEntrenamiento.setText("Training");
			this.jCheckEntrenamiento.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckEntrenamiento.setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED));			

			this.jCheckEntrenamiento.addItemListener( new ItemListener() 
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{	
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );

					appUI.getInstance().getJPanelComparacion().setVisible( !((JCheckBox)e.getSource()).isSelected() );
					//ctrUI.getInstance().showBarraComparacion();
				}
			});

			this.parameters.put( ID, this.jCheckEntrenamiento );
		}
		return this.jCheckEntrenamiento;
	}

	private JCheckBox getJCheckBoxFullScreen() 
	{
		if ( this.jCheckBoxFullScreen == null) 
		{
			final String ID = ConfigApp.IS_FULLSCREEN;

			this.jCheckBoxFullScreen = new JCheckBox();

			this.jCheckBoxFullScreen.setText("FullScreen");
			this.jCheckBoxFullScreen.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxFullScreen.addItemListener( new ItemListener( )
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );					
				}
			});

			this.parameters.put( ID, this.jCheckBoxFullScreen );
		}

		return this.jCheckBoxFullScreen;
	}

	private JCheckBox getJCheckBoxShowTrialTimer() 
	{
		if ( this.jCheckBoxShowTrialTimer == null) 
		{
			final String ID = ConfigApp.IS_SHOW_TIMER_ACTIVE;

			this.jCheckBoxShowTrialTimer = new JCheckBox("Show Trial Timer");
			this.jCheckBoxShowTrialTimer.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxShowTrialTimer.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{	
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );

					appUI.getInstance().getJProgressBarTiempo().setVisible( ((JCheckBox)e.getSource()).isSelected() );
					//ctrUI.getInstance().showTimerBar( );					
				}
			});

			this.parameters.put( ID, this.jCheckBoxShowTrialTimer );
		}

		return this.jCheckBoxShowTrialTimer;
	}

	private JCheckBox getJCheckBoxCountdownSound() 
	{
		if ( this.jCheckBoxCountdownSound == null) 
		{
			final String ID = ConfigApp.IS_COUNTDOWN_SOUND_ACTIVE;

			this.jCheckBoxCountdownSound = new JCheckBox("Countdown Sound");
			this.jCheckBoxCountdownSound.setSelected( (Boolean)ConfigApp.getProperty( ID ) );

			this.jCheckBoxCountdownSound.addItemListener( new ItemListener()
			{				
				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					ConfigApp.setProperty( ID, ((JCheckBox)e.getSource()).isSelected() );
				}
			});

			this.parameters.put( ID, this.jCheckBoxCountdownSound );
		}

		return this.jCheckBoxCountdownSound;
	}

	private JTextField getJTextFieldPathTimeFile() 
	{
		if ( this.jTextFieldPathTimeFile == null) 
		{
			final String ID = ConfigApp.PATH_FILE_TIME_OUT_AUTO;

			this.jTextFieldPathTimeFile = new JTextField( ConfigApp.getProperty( ID ).toString() );
			this.jTextFieldPathTimeFile.setFont( new Font( Font.DIALOG, Font.BOLD, 12));
			this.jTextFieldPathTimeFile.setToolTipText( this.jTextFieldPathTimeFile.getText() );			
			this.jTextFieldPathTimeFile.setColumns( 8 );		

			this.jTextFieldPathTimeFile.getDocument().addDocumentListener( new DocumentListener()
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
						ConfigApp.setProperty( ID, e.getDocument().getText( 0, e.getDocument().getLength() ) );
					} 
					catch (BadLocationException e1) 
					{
						e1.printStackTrace();
					}
				}
			});

			this.parameters.put( ID, this.jTextFieldPathTimeFile );
		}

		return this.jTextFieldPathTimeFile;
	}

	private JButton getJButtonSelectFile() 
	{
		if ( this.jButtonSelectFile == null) 
		{
			this.jButtonSelectFile = new JButton();
			//this.jButtonSelectFile.setText(" ... ");
			this.jButtonSelectFile.setIcon( GeneralAppIcon.Folder( 32, 22, Color.BLACK, this.colorIconLoadFolder ) );
			this.jButtonSelectFile.setBorder( new LineBorder( Color.black ));

			this.jButtonSelectFile.addActionListener(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{					
					String[] path = guiManager.getInstance().selectUserFile( ConfigApp.defaultNameFileConfigResults, true
																			, false, JFileChooser.FILES_ONLY
																			, "results (*." + ConfigApp.defaultNameFileConfigExtension + ")"
																			, new String[] { ConfigApp.defaultNameFileConfigExtension } );
					if( path != null )
					{
						getJTextFieldPathTimeFile().setText( path[ 0 ] );
						getJTextFieldPathTimeFile().requestFocusInWindow();

						try 
						{
							Robot robot = new Robot();
							robot.keyPress( KeyEvent.VK_ENTER );
						} 
						catch (AWTException e1) 
						{
						}
					}
				}
			});
		}

		return this.jButtonSelectFile;
	}

	private MultipleNumberSpinner getMultipleNumberSpinner()
	{
		if( this.jMNSpinner == null )
		{
			final String ID = ConfigApp.NON_ANSWER_TIMERS;

			this.jMNSpinner = new MultipleNumberSpinner( -1L, null, 30L, 1L );
			this.jMNSpinner.setNumberList( (List)ConfigApp.getProperty( ID ) );			

			this.jMNSpinner.addFocusListener( new FocusListener() 
			{				
				@Override
				public void focusLost(FocusEvent e) 
				{					
					ConfigApp.setProperty( ID, jMNSpinner.getNumberList() );
				}

				@Override
				public void focusGained(FocusEvent e) 
				{
				}
			});

			this.jMNSpinner.addMouseWheelListener( new MouseWheelListener() 
			{	
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) 
				{
					ConfigApp.setProperty( ID, jMNSpinner.getNumberList() );
				}
			});

			this.parameters.put( ID, this.jMNSpinner );
		}

		return this.jMNSpinner;
	}
	
}
