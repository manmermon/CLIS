/*
 * Copyright 2011-2016 by Manuel Merino Monge <manmermon@dte.us.es>
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

import Config.ConfigApp;
import Config.Language.Language;
import Controls.coreControl;
import GUI.MyComponents.DisabledGlassPane;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.indicadorNivel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Locale;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Activities.ActivityRegister;

public class appUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static appUI ui = null;

	// progressBar
	private JProgressBar jProgressBarTiempo = null;

	// indicadorNivel
	private indicadorNivel indicadorCalificacion = null;

	// slider
	private JSlider jSliderOtros = null;

	// panel
	private JPanel jPanelOperaciones = null;
	private JPanel jPanelOper = null;
	private JPanel jPanelComparacion = null;
	private JPanel jPanelOtros = null;
	private JPanel jPanelCalificacion = null;
	private JPanel jContentPane = null;
	private JPanel jPanelActividad = null;
	private JPanel jPanelObjetivo = null;
	private JPanel jPanelTarea = null;
	private JPanel jPanelTiempo = null;
	private JPanel jPanelMenu = null;
	private JPanel jPanelAppState = null;
	
	// button
	private JButton jButtonCorrecto = null;

	// Separator
	private JSeparator separator;

	// menuBar
	private JMenuBar jJMenuBar = null;

	// menuItem
	private JMenuItem jMenuPlayStop = null;
	private JMenuItem jMenuConfig = null;
	private JMenuItem jMenuAcercaDe = null;
	private JMenuItem jGNUGLP = null;
	
	// textField
	private JTextField appTextState = null;

	/**
	 * This is the default constructor
	 */
	 private appUI()
	 {
		 initialize();
	 }

	 public static appUI getInstance()
	 {
		 if (ui == null)
		 {
			 ui = new appUI();
		 }

		 return ui;
	 }

		/**
		 * This method initializes this
		 * 
		 * @return void
		 */
	 private void initialize()
	 {		 
		 DisabledGlassPane glass = new DisabledGlassPane();
		 glass.activate( Language.getCaption( Language.MSG_WINDOW_CLOSE, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ) );
		 super.setGlassPane( glass );
		 super.getGlassPane().setVisible( false );

		 super.setJMenuBar( getJJMenuBar() );
		 super.setContentPane( getJContentPane() );

		 InputMap inputMap = getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );

		 ActionMap actionMap = getRootPane().getActionMap(); 

		 inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.ALT_DOWN_MASK ), "actionPlayStop" );		 
		 actionMap.put( "actionPlayStop", keyActions.getButtonClickAction( "actionPlayStop", getJMenuPlay() ) );
		

		 super.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		 
		 this.addWindowListener(new WindowAdapter()
		 {
			 public void windowClosing(WindowEvent e)
			 {
				 try
				 {					 
					 if(  !getGlassPane().isVisible( ) )
					 {
						 if( coreControl.getInstance().isDoingSomething() )
						 {						 
							 String[] opts = { UIManager.getString( "OptionPane.yesButtonText" ), 
									 UIManager.getString( "OptionPane.noButtonText" ) };
	
							 int actionDialog = JOptionPane.showOptionDialog( ui, Language.getCaption( Language.MSG_APP_STATE, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() )
									 												+ getTextState().getText() + "."
							 														+ "\n" + Language.getCaption( Language.MSG_INTERRUPT, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ) 
							 														+ "?", 
							 		 Language.getCaption( Language.MSG_WARNING, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() )
							 		 , JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
									 null, opts, opts[1]);
	
							 if ( actionDialog == JOptionPane.YES_OPTION )
							 {								 
								 coreControl.getInstance().stopWorking( false );
								 
								 getGlassPane().setVisible( true );
								 								 
								 coreControl.getInstance().closeWhenDoingNothing( true );
							 }
						 }
						 else
						 {
							 System.exit( 0 );
						 }
					 }
				 }
				 catch (Exception e1)
				 {
					 e1.printStackTrace();
					 String msg = e1.getMessage();
					 if ((msg == null) || (msg.isEmpty()))
					 {
						 msg = "" + e1.getCause();
					 }

					 ErrorException.showErrorException(appUI.ui, msg, Language.getCaption( Language.DIALOG_ERROR, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ), ErrorException.ERROR_MESSAGE  );
				 }				 
			 }

		 });
		 
		 this.addComponentListener(new ComponentAdapter()
		 {
			 public void componentResized(ComponentEvent arg0)
			 {
				 appUI ui = (appUI)arg0.getSource();

				 ui.getJPanelComparacion().setVisible(false);

				 Dimension d = new Dimension(ui.getSize());
				 d.width /= 10;
				 if (d.width < 50)
				 {
					 d.width = 50;
				 }

				 ui.getJButtonCorrecto().setPreferredSize(d);

				 int wd = d.width - ui.getJButtonCorrecto().getInsets().left - ui.getJButtonCorrecto().getInsets().right;

				 ImageIcon icono = (ImageIcon)ui.getJButtonCorrecto().getIcon();
				 if (icono != null)
				 {
					 icono = new ImageIcon(icono.getImage().getScaledInstance(wd, wd, 4));
				 }

				 ui.getJButtonCorrecto().setIcon(icono);


				 ui.getJPanelComparacion().setVisible(true);
			 }

		 });
		 
		 this.getJProgressBarTiempo().setValue( 0 );		 
	 }
	 
	 public void checkConfig()
	 {
		 if( !(Boolean)ConfigApp.getProperty( ConfigApp.IS_SHOW_TIMER_ACTIVE ) )
		 {
			 this.getJProgressBarTiempo().setVisible( false );
		 }
		 		 
		 if( ActivityRegister.getNumberOfActivities() > 0 )
		 {
			 int task = (Integer)ConfigApp.getProperty( ConfigApp.TASK_INDEX_SELECTED );

			 if( !ActivityRegister.isChallegeActivity( ActivityRegister.getActivityID( task ) ) )
			 {
				 appUI.getInstance().getJPanelObjetivo().setVisible( false );
			 }
		 }
	 }

		/**
		 * This method initializes jContentPane
		 * 
		 * @return javax.swing.JPanel
		 */
	 protected JPanel getJContentPane()
	 {
		 if (this.jContentPane == null)
		 {
			 this.jContentPane = new JPanel();
			 this.jContentPane.setLayout(new BorderLayout());


			 this.jContentPane.add(getJPanelActividad(),  BorderLayout.CENTER );
		 }

		 return this.jContentPane;
	 }

		/**
		 * This method initializes jPanelActividad	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 private JPanel getJPanelActividad()
	 {
		 if (this.jPanelActividad == null)
		 {
			 this.jPanelActividad = new JPanel();
			 this.jPanelActividad.setLayout(new BorderLayout());
			 this.jPanelActividad.setBorder(new SoftBevelBorder( SoftBevelBorder.LOWERED ));
			 this.jPanelActividad.add(getJPanelTarea(), BorderLayout.CENTER );
		 }
		 return this.jPanelActividad;
	 }

		/**
		 * This method initializes jPanelObjetivo	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 protected JPanel getJPanelObjetivo()
	 {
		 if (this.jPanelObjetivo == null)
		 {
			 this.jPanelObjetivo = new JPanel();
			 this.jPanelObjetivo.setLayout(new BorderLayout());
			 this.jPanelObjetivo.add(getJPanelComparacion(), BorderLayout.CENTER);
			 this.jPanelObjetivo.add(getJButtonCorrecto(), BorderLayout.WEST);
		 }
		 return this.jPanelObjetivo;
	 }

		/**
		 * This method initializes jPanelTarea	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 private JPanel getJPanelTarea()
	 {
		 if (this.jPanelTarea == null)
		 {
			 this.jPanelTarea = new JPanel();
			 this.jPanelTarea.setLayout(new BorderLayout());
			 this.jPanelTarea.add(getJPanelTiempo(), BorderLayout.NORTH);
			 this.jPanelTarea.add(getJPanelOperaciones(), BorderLayout.CENTER);
		 }
		 return this.jPanelTarea;
	 }


		/**
		 * This method initializes jPanelTiempo	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 private JPanel getJPanelTiempo()
	 {
		 if (this.jPanelTiempo == null)
		 {
			 BorderLayout gridLayout1 = new BorderLayout();

			 this.jPanelTiempo = new JPanel();
			 this.jPanelTiempo.setLayout(gridLayout1);
			 this.jPanelTiempo.add(getJProgressBarTiempo(), BorderLayout.CENTER);
		 }

		 return this.jPanelTiempo;
	 }

		/**
		 * This method initializes jProgressBarTiempo	
		 * 	
		 * @return javax.swing.JProgressBar	
		 */
	 public JProgressBar getJProgressBarTiempo()
	 {
		 if (this.jProgressBarTiempo == null)
		 {
			 this.jProgressBarTiempo = new JProgressBar();
			 this.jProgressBarTiempo.setForeground(Color.magenta);
			 this.jProgressBarTiempo.setStringPainted(true);
			 this.jProgressBarTiempo.setString("00:00");
			 this.jProgressBarTiempo.setValue(0);
		 }
		 return this.jProgressBarTiempo;
	 }

		/**
		 * This method initializes jPanelComparaci�n	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 protected JPanel getJPanelComparacion()
	 {
		 if (this.jPanelComparacion == null)
		 {
			 GridLayout gridLayout2 = new GridLayout();
			 gridLayout2.setColumns(2);
			 this.jPanelComparacion = new JPanel();
			 this.jPanelComparacion.setLayout(gridLayout2);
			 this.jPanelComparacion.add(getJPanelOtros(), null);
			 this.jPanelComparacion.add(getJPanelCalificacion(), null);
		 }

		 return this.jPanelComparacion;
	 }

		/**
		 * This method initializes jPanelOtros	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 private JPanel getJPanelOtros()
	 {
		 if (this.jPanelOtros == null)
		 {
			 GridLayout gridLayout4 = new GridLayout();
			 gridLayout4.setRows(1);
			 this.jPanelOtros = new JPanel();
			 this.jPanelOtros.setLayout(gridLayout4);
			 this.jPanelOtros.add(getJSliderOtros(), null);
		 }
		 return this.jPanelOtros;
	 }

		/**
		 * This method initializes jPanelCalificacion	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 private JPanel getJPanelCalificacion()
	 {
		 if (this.jPanelCalificacion == null)
		 {
			 GridLayout gridLayout3 = new GridLayout();
			 gridLayout3.setRows(1);
			 this.jPanelCalificacion = new JPanel();
			 this.jPanelCalificacion.setLayout(gridLayout3);
			 this.jPanelCalificacion.add(getIndicadorNivelCalificacion(), null);
		 }
		 return this.jPanelCalificacion;
	 }


		/**
		 * This method initializes jProgressBarCalificacion	
		 * 	
		 * @return javax.swing.JProgressBar	
		 */
	 protected indicadorNivel getIndicadorNivelCalificacion()
	 {
		 if (this.indicadorCalificacion == null)
		 {
			 this.indicadorCalificacion = new indicadorNivel(3);

			 Color[] colors = { Color.green, Color.yellow, Color.red };
			 this.indicadorCalificacion.setColorLevels(colors);
			 this.indicadorCalificacion.setMaximum(100);
			 this.indicadorCalificacion.setMinimum(0);
			 int[] levels = { 60, 80 };

			 this.indicadorCalificacion.setOrientation( indicadorNivel.VERTICAL_OR );
			 this.indicadorCalificacion.setLevels(levels);
			 this.indicadorCalificacion.setEditable(false);
			 this.indicadorCalificacion.setPaintedString(false);
		 }
		 return this.indicadorCalificacion;
	 }

		/**
		 * This method initializes jSliderOtros	
		 * 	
		 * @return javax.swing.JSlider	
		 */
	 protected JSlider getJSliderOtros()
	 {
		 if (this.jSliderOtros == null)
		 {
			 this.jSliderOtros = new JSlider();
			 this.jSliderOtros.setPaintTrack(false);
			 this.jSliderOtros.setEnabled(false);
			 this.jSliderOtros.setOrientation( JSlider.VERTICAL );
			 this.jSliderOtros.setValue(90);
		 }
		 return this.jSliderOtros;
	 }

		/**
		 * This method initializes jPanelOperaciones	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 protected JPanel getJPanelOperaciones()
	 {
		 if (this.jPanelOperaciones == null)
		 {
			 this.jPanelOperaciones = new JPanel();
			 this.jPanelOperaciones.setLayout(new BorderLayout());

			 this.jPanelOperaciones.add(getJPanelObjetivo(), BorderLayout.WEST);
			 this.jPanelOperaciones.add(getJPanelOper(), BorderLayout.CENTER);
		 }

		 return this.jPanelOperaciones;
	 }

		/**
		 * This method initializes jTextFieldCorrecto	
		 * 	
		 * @return javax.swing.JTextField	
		 */
	 protected JButton getJButtonCorrecto()
	 {
		 if (this.jButtonCorrecto == null)
		 {
			 this.jButtonCorrecto = new JButton();
			 this.jButtonCorrecto.setBackground(Color.white);

			 this.jButtonCorrecto.setName("Blanco");
			 this.jButtonCorrecto.setForeground(new Color(255, 102, 0));
			 this.jButtonCorrecto.setFont(new Font( Font.DIALOG, Font.BOLD, 24));
			 this.jButtonCorrecto.setFocusable(false);
			 this.jButtonCorrecto.setFocusPainted(false);
			 this.jButtonCorrecto.setFocusCycleRoot(false);
		 }
		 return this.jButtonCorrecto;
	 }

		/**
		 * This method initializes jPanelOper	
		 * 	
		 * @return javax.swing.JPanel	
		 */
	 protected JPanel getJPanelOper()
	 {
		 if (this.jPanelOper == null)
		 {
			 this.jPanelOper = new JPanel();
			 this.jPanelOper.setLayout(new BorderLayout());
			 this.jPanelOper.setBackground(Color.white);
			 this.jPanelOper.setFocusable(false);
			 this.jPanelOper.setFocusCycleRoot(false);

			 DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT, Locale.getDefault() );

			 String txt = ConfigApp.shortNameApp + " " + ConfigApp.version + " (" + df.format( ConfigApp.buildDate.getTime() ) + ")";
			 txt += " Copyright (C) " + ConfigApp.appDateRange + " Manuel Merino Moge <manmermon@dte.us.es>\n\n";
			 txt += "Software Registration in Andalusia Government in Seville (Spain). ";
			 txt += "Register Number: 201199901443345. Date: October 2011.\n\n";
			 txt += "This program comes with ABSOLUTELY NO WARRANTY; for details type `Alt-w'.\n";
			 txt += "This is free software, and you are welcome to redistribute it";
			 txt += "under certain conditions; type `Alt-w' for details.";
			 txt += "\n\nSource code available on https://github.com/manmermon/CLIS";

			 JTextPane textArea = new JTextPane();
			 textArea.setText(txt);
			 textArea.setEditable(false);

			 StyledDocument doc = textArea.getStyledDocument();
			 SimpleAttributeSet center = new SimpleAttributeSet();
			 StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER );
			 doc.setParagraphAttributes(0, doc.getLength(), center, false);

			 this.jPanelOper.add(textArea, BorderLayout.CENTER);
		 }

		 return this.jPanelOper;
	 }

		/**
		 * This method initializes jJMenuBar	
		 * 	
		 * @return javax.swing.JMenuBar	
		 */
	 private JMenuBar getJJMenuBar()
	 {
		 if (this.jJMenuBar == null)
		 {
			 this.jJMenuBar = new JMenuBar( );
			 
			 /*
			 FlowLayout ly = new FlowLayout();
			 ly.setAlignment( FlowLayout.LEFT  );
			 ly.setHgap( 0 );
			 ly.setVgap( 0 );
			 

			 this.jJMenuBar.setLayout( ly );
			 this.jJMenuBar.setBackground( Color.LIGHT_GRAY.brighter() );
			 
			 this.jJMenuBar.add( this.getJMenuPlay() );
			 this.jJMenuBar.add( this.getSeparator() );
			 this.jJMenuBar.add( this.getJMenuConfig() );
			 this.jJMenuBar.add( this.getJMenuAcercaDe() );
			 this.jJMenuBar.add( this.getJMenuGNUGPL() );
			 */
			 
			 this.jJMenuBar.setLayout( new BorderLayout() );
			 
			 JScrollPane scr =  new JScrollPane( this.getMenuPanel() );
			 scr.setBorder( BorderFactory.createEmptyBorder() );
			 scr.getHorizontalScrollBar().setPreferredSize(  new Dimension( 0, 10 ) );
			 scr.getVerticalScrollBar().setPreferredSize(  new Dimension( 10, 0 ) ); 
			 
			 this.jJMenuBar.add( scr, BorderLayout.CENTER );
			 this.jJMenuBar.add( this.getAppStatePanel(), BorderLayout.EAST );
			 
			 this.setBackgroundContainer( this.jJMenuBar,Color.LIGHT_GRAY.brighter() );
		 }
		 return this.jJMenuBar;
	 }
	 
	 private void setBackgroundContainer( Container cont, Color bg )
	 {
		 if( cont != null )
		 {
			 for( Component c : cont.getComponents() )
			 {
				 c.setBackground( bg );
				 
				 if( c instanceof Container )
				 {
					 this.setBackgroundContainer( (Container)c, bg );
				 }
			 }
		 }
	 }
	 
	 private JPanel getMenuPanel()
	 {
		if( this.jPanelMenu == null)
		{
			this.jPanelMenu = new JPanel();
						
			 FlowLayout ly = new FlowLayout();
			 ly.setAlignment( FlowLayout.LEFT  );
			 ly.setHgap( 0 );
			 ly.setVgap( 0 );
			 
			 this.jPanelMenu.setLayout( ly );
			 //this.jPanelMenu.setBackground( Color.LIGHT_GRAY.brighter() );
			 
			 this.jPanelMenu.add( this.getJMenuPlay() );
			 this.jPanelMenu.add( this.getSeparator() );
			 this.jPanelMenu.add( this.getJMenuConfig() );
			 this.jPanelMenu.add( this.getJMenuAcercaDe() );
			 this.jPanelMenu.add( this.getJMenuGNUGPL() );			 
		}
		
		return this.jPanelMenu; 
	 }

	
	 private JPanel getAppStatePanel()
	 {
		 if( this.jPanelAppState == null )
		 {
			 this.jPanelAppState = new JPanel( new BorderLayout() );
			 
			 JLabel lbstate = new JLabel( Language.getCaption( Language.INFO_STATE_LABEL, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ) + ": " );
			 
			 lbstate.setFont( new Font( Font.DIALOG, Font.PLAIN, 12 ) );
			 FontMetrics fm = lbstate.getFontMetrics( lbstate.getFont() );
			 
			 this.jPanelAppState.setPreferredSize( new Dimension( 50 + fm.stringWidth( lbstate.getText() ), 0 ) );
			 
			 this.jPanelAppState.add( lbstate, BorderLayout.WEST );
			 this.jPanelAppState.add( this.getTextState(), BorderLayout.CENTER );
		 }
		 
		 return this.jPanelAppState;
	 }
	 
	 protected JTextField getTextState()
	 {
		 if( this.appTextState == null ) 
		 {
			 this.appTextState = new JTextField();
			 
			 //this.appTextState.setBorder( BorderFactory.createEmptyBorder() );
			 this.appTextState.setEditable( false );
			 Font f = this.appTextState.getFont();
			 this.appTextState.setFont( new Font( f.getName(), Font.BOLD, f.getSize() ) );
		 }
		 
		 return this.appTextState;
	 }
	 
	 /**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	 protected JMenuItem getJMenuPlay()
	 {
		 if (this.jMenuPlayStop == null)
		 {
			 this.jMenuPlayStop = new JMenuItem();

			 String lang = ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString();
			 this.jMenuPlayStop.setText(Language.getCaption( Language.ACTION_PLAY, lang));

			 this.jMenuPlayStop.setIcon(guiManager.START_ICO);
			 this.jMenuPlayStop.setSelectedIcon(guiManager.STOP_ICO);

			 this.jMenuPlayStop.setFocusable(false);
			 this.jMenuPlayStop.setFocusPainted(false);
			 this.jMenuPlayStop.setFocusCycleRoot(false);
			 this.jMenuPlayStop.setForeground(null);

			 this.jMenuPlayStop.setBackground(this.jJMenuBar.getBackground());

			 KeyStroke k = KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.ALT_MASK );
			 this.jMenuPlayStop.setAccelerator(k);


			 Dimension d = this.jMenuPlayStop.getPreferredSize(); 
			 d.width *= 1.40;
			this.jMenuPlayStop.setPreferredSize( d );

			 this.jMenuPlayStop.addActionListener(new ActionListener()
			 {
				 public void actionPerformed(ActionEvent arg0)
				 {
					 String lang = ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString();
					 String actText = Language.getCaption( Language.ACTION_PLAY, lang);

					 if (jMenuPlayStop.getText().equals(actText))
					 {
						 jMenuPlayStop.setText(Language.getCaption( Language.ACTION_STOP, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString()));
						 Icon aux = jMenuPlayStop.getIcon();
						 jMenuPlayStop.setIcon( jMenuPlayStop.getSelectedIcon());
						 jMenuPlayStop.setSelectedIcon(aux);

						 guiManager.getInstance().startTest();

					 }
					 else
					 {
						 jMenuPlayStop.setText(Language.getCaption( Language.ACTION_PLAY, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString()));
						 Icon aux = jMenuPlayStop.getSelectedIcon();
						 jMenuPlayStop.setSelectedIcon(jMenuPlayStop.getIcon());
						 jMenuPlayStop.setIcon(aux);

						 guiManager.getInstance().stopTest();


						 jMenuPlayStop.transferFocus();
					 }

				 }
			 });
			 
			 this.jMenuPlayStop.addMouseListener(new MouseAdapter()
			 {

				 public void mousePressed(MouseEvent arg0)
				 {
					 jMenuPlayStop.setArmed(true);
				 }

				 public void mouseReleased(MouseEvent e)
				 {
					 jMenuPlayStop.setArmed(false);
				 }
			 });
		 }


		 return this.jMenuPlayStop;
	 }

		/**
		 * This method initializes jMenuConfig	
		 * 	
		 * @return javax.swing.JMenu	
		 */
	 protected JMenuItem getJMenuConfig()
	 {
		 if (this.jMenuConfig == null)
		 {
			 this.jMenuConfig = new JMenuItem();
			 this.jMenuConfig.setText( Language.getCaption( Language.ACTION_CONFIG, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ) );
			 this.jMenuConfig.setIcon(new ImageIcon(GeneralAppIcon.Config().getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH )));

			 this.jMenuConfig.setFocusable(false);
			 this.jMenuConfig.setFocusPainted(false);
			 this.jMenuConfig.setFocusCycleRoot(false);
			 this.jMenuConfig.setEnabled(true);
			 this.jMenuConfig.setForeground(Color.black);
			 this.jMenuConfig.setBackground(this.jJMenuBar.getBackground());

			 KeyStroke k = KeyStroke.getKeyStroke( KeyEvent.VK_C, InputEvent.ALT_MASK );
			 this.jMenuConfig.setAccelerator(k);

			 FontMetrics fm = jMenuConfig.getFontMetrics( jMenuConfig.getFont() );
				Dimension d = jMenuConfig.getPreferredSize();
				d.width = jMenuConfig.getIcon().getIconWidth() + fm.stringWidth( jMenuConfig.getText()+ " " + k.getKeyChar()+"-"+InputEvent.getModifiersExText( k.getModifiers() ) );
				d.width *= 1.30;
				jMenuConfig.setPreferredSize( d );

			 this.jMenuConfig.addActionListener(new ActionListener()
			 {
				 public void actionPerformed(ActionEvent e)
				 {
					 JMenuItem m = (JMenuItem)e.getSource();
					 JDialog jDialogConfig = new settingMenu_TaskSetting(SwingUtilities.getWindowAncestor(m), m.getText());

					 jDialogConfig.setVisible(false);
					 jDialogConfig.pack();
					 jDialogConfig.validate();

					 Point l = appUI.ui.getLocation();
					 Dimension d = appUI.ui.getSize();
					 Dimension dd = jDialogConfig.getSize();

					 Point loc = new Point(l.x + d.width / 2 - dd.width / 2, l.y + d.height / 2 - dd.height / 2);

					 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

					 Insets ssooPAD = Toolkit.getDefaultToolkit().getScreenInsets(appUI.ui.getGraphicsConfiguration());
					 if (loc.x < ssooPAD.left + 1)
					 {
						 loc.x = (ssooPAD.left + 1);
					 }

					 if (loc.y < ssooPAD.top + 1)
					 {
						 loc.y = (ssooPAD.top + 1);
					 }

					 dd.width *= 1.02;
						if( dd.width > screenSize.width )
						{
							dd.width = screenSize.width;
						}

					 
					 dd.height = 750;
					 if (dd.height > screenSize.width)
					 {
						 dd.height = screenSize.width;
					 }

					 jDialogConfig.setLocation(loc);
					 jDialogConfig.setSize(dd);
					 jDialogConfig.setResizable(true);
					 jDialogConfig.setVisible(true);
				 }

			 });
			 
			 this.jMenuConfig.addMouseListener(new MouseAdapter()
			 {

				 public void mousePressed(MouseEvent arg0)
				 {
					 jMenuConfig.setArmed(true);
				 }

				 public void mouseReleased(MouseEvent e)
				 {
					 jMenuConfig.setArmed(false);
				 }
			 });
		 }

		 return this.jMenuConfig;
	 }

		/**
		 * This method initializes jMenuAcercaDe	
		 * 	
		 * @return javax.swing.JMenu	
		 */
	 protected JMenuItem getJMenuGNUGPL()
	 {
		 if (this.jGNUGLP == null)
		 {
			 this.jGNUGLP = new JMenuItem();
			 this.jGNUGLP.setHorizontalTextPosition(2);
			 this.jGNUGLP.setText( Language.getCaption( Language.ACTION_GNU_GPL, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() ));

			 KeyStroke k = KeyStroke.getKeyStroke( KeyEvent.VK_W, InputEvent.ALT_MASK );
			 this.jGNUGLP.setAccelerator(k);

			 this.jGNUGLP.setFocusable(false);
			 this.jGNUGLP.setFocusPainted(false);
			 this.jGNUGLP.setFocusCycleRoot(false);
			 this.jGNUGLP.setEnabled(true);
			 this.jGNUGLP.setForeground(Color.black);
			 this.jGNUGLP.setBackground(this.jJMenuBar.getBackground());

			 this.jGNUGLP.addActionListener(new ActionListener()
			 {
				 public void actionPerformed(ActionEvent e)
				 {
					 if (jGNUGLP.isEnabled())
					 {
						 JDialog jDialogGPL = new menuWindow_GNUGLPLicence(appUI.getInstance());

						 jDialogGPL.setVisible(false);
						 jDialogGPL.pack();
						 jDialogGPL.validate();

						 Dimension dd = Toolkit.getDefaultToolkit().getScreenSize();
						 dd.width /= 4;
						 dd.height /= 4;
						 jDialogGPL.setSize(dd);

						 Point l = appUI.ui.getLocation();
						 Dimension d = appUI.ui.getSize();
						 Point loc = new Point(l.x + d.width / 2 - dd.width / 2, l.y + d.height / 2 - dd.height / 2);

						 Insets ssooPAD = Toolkit.getDefaultToolkit().getScreenInsets(appUI.ui.getGraphicsConfiguration());
						 if (loc.x < ssooPAD.left + 1)
						 {
							 loc.x = (ssooPAD.left + 1);
						 }

						 if (loc.y < ssooPAD.top + 1)
						 {
							 loc.y = (ssooPAD.top + 1);
						 }

						 jDialogGPL.setLocation(loc);

						 jDialogGPL.setResizable(true);
						 jDialogGPL.setVisible(true);
					 }

				 }

			 });
			 this.jGNUGLP.addMouseListener(new MouseAdapter()
			 {

				 public void mousePressed(MouseEvent arg0)
				 {
					 jGNUGLP.setArmed(true);
				 }

				 public void mouseReleased(MouseEvent e)
				 {
					 jGNUGLP.setArmed(false);
				 }
			 });
		 }

		 return this.jGNUGLP;
	 }

		/**
		 * This method initializes jMenuAcercaDe	
		 * 	
		 * @return javax.swing.JMenu	
		 */
	 protected JMenuItem getJMenuAcercaDe()
	 {
		 if (this.jMenuAcercaDe == null)
		 {
			 this.jMenuAcercaDe = new JMenuItem();
			 this.jMenuAcercaDe.setHorizontalTextPosition( SwingConstants.LEFT );
			 this.jMenuAcercaDe.setText( Language.getCaption( Language.ACTION_ABOUT, ConfigApp.getProperty( ConfigApp.LANGUAGE ).toString() )+ " " + ConfigApp.shortNameApp);

			 KeyStroke k = KeyStroke.getKeyStroke( KeyEvent.VK_H, InputEvent.ALT_MASK );
			 this.jMenuAcercaDe.setAccelerator(k);

			 this.jMenuAcercaDe.setFocusable(false);
			 this.jMenuAcercaDe.setFocusPainted(false);
			 this.jMenuAcercaDe.setFocusCycleRoot(false);
			 this.jMenuAcercaDe.setEnabled(true);
			 this.jMenuAcercaDe.setForeground(Color.black);
			 this.jMenuAcercaDe.setBackground(this.jJMenuBar.getBackground());

			 this.jMenuAcercaDe.addActionListener(new ActionListener()
			 {
				 public void actionPerformed(ActionEvent e)
				 {
					 if (jMenuAcercaDe.isEnabled())
					 {
						 JDialog jDialogAcercaDe = new menuWindow_AboutApp(appUI.getInstance());
						 jDialogAcercaDe.setVisible(false);
						 jDialogAcercaDe.pack();
						 jDialogAcercaDe.validate();

						 Dimension dd = Toolkit.getDefaultToolkit().getScreenSize();
						 dd.width /= 4;
						 dd.height /= 2;
						 jDialogAcercaDe.setSize(dd);

						 Point l = appUI.ui.getLocation();
						 Dimension d = appUI.ui.getSize();
						 Point loc = new Point(l.x + d.width / 2 - dd.width / 2, l.y + d.height / 2 - dd.height / 2);

						 Insets ssooPAD = Toolkit.getDefaultToolkit().getScreenInsets(appUI.ui.getGraphicsConfiguration());
						 if (loc.x < ssooPAD.left + 1)
						 {
							 loc.x = (ssooPAD.left + 1);
						 }

						 if (loc.y < ssooPAD.top + 1)
						 {
							 loc.y = (ssooPAD.top + 1);
						 }

						 jDialogAcercaDe.setLocation(loc);

						 jDialogAcercaDe.setResizable(true);
						 jDialogAcercaDe.setVisible(true);
					 }

				 }
			 });
			 this.jMenuAcercaDe.addMouseListener(new MouseAdapter()
			 {

				 public void mousePressed(MouseEvent arg0)
				 {
					 jMenuAcercaDe.setArmed(true);
				 }

				 public void mouseReleased(MouseEvent e)
				 {
					 jMenuAcercaDe.setArmed(false);
				 }
			 });
		 }

		 return this.jMenuAcercaDe;
	 }

	 private JSeparator getSeparator()
	 {
		 if (this.separator == null)
		 {
			 this.separator = new JSeparator();
			 this.separator.setOrientation( JSeparator.VERTICAL );
			 this.separator.setPreferredSize(new Dimension(2, 16));
		 }
		 return this.separator;
	 }
}