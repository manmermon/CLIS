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

import Config.ConfigApp;
import Config.Language.Language;
import Controls.coreControl;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.imagenPoligono2D;
import StoppableThread.IStoppableThread;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class guiManager
{
	public static String language = Language.defaultLanguage;

	public static final Icon START_ICO = new ImageIcon( imagenPoligono2D.crearImagenTriangulo(10, 1.0F, Color.BLACK, Color.GREEN, imagenPoligono2D.EAST ) );
	public static final Icon STOP_ICO = new ImageIcon( imagenPoligono2D.crearImagenRectangulo(10, 10, 1.0F, Color.BLACK, Color.RED ) );

	public static final int ANSWER_ICO_INCORRECT = 0;  
	public static final int ANSWER_ICO_CORRECT = 1;
	public static final int ANSWER_ICO_TIME_OUT = 2;

	private static appUI ui = null;
	private static guiManager ctr = null;

	private BufferedImage iconCorrect = null;
	private BufferedImage iconIncorrect = null;
	private BufferedImage iconTimeOut = null;

	private guiParameters parameters = new guiParameters();

	private JFrame appGUIFullScreen = null;
	private mouseTracking autoHideMenu = null;

	private Point prevGUILocation = null;

	private guiManager()
	{
		ui = appUI.getInstance();

		Image imgCorrect = GeneralAppIcon.Correct().getImage();
		Image imgIncorrect = GeneralAppIcon.Incorrect().getImage();
		Image imgTimeOut = GeneralAppIcon.Clock().getImage();

		this.iconCorrect = new BufferedImage(imgCorrect.getWidth(null), imgCorrect.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		this.iconIncorrect = new BufferedImage(imgIncorrect.getWidth(null), imgIncorrect.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		this.iconTimeOut = new BufferedImage(imgTimeOut.getWidth(null), imgTimeOut.getHeight(null), BufferedImage.TYPE_INT_ARGB);


		Graphics2D bGr = this.iconCorrect.createGraphics();
		bGr.drawImage(imgCorrect, 0, 0, null);
		bGr.dispose();

		bGr = this.iconIncorrect.createGraphics();
		bGr.drawImage(imgIncorrect, 0, 0, null);
		bGr.dispose();

		bGr = this.iconTimeOut.createGraphics();
		bGr.drawImage(imgTimeOut, 0, 0, null);
		bGr.dispose();
	}

	public static guiManager getInstance()
	{
		if (ctr == null)
		{
			ctr = new guiManager();
		}

		return ctr;
	}

	public appUI getAppUI()
	{
		return ui;
	}

	protected void saveFileConfig()
	{
		File[] f = selectFile(ConfigApp.defaultNameFileConfig
				, Language.getCaption( Language.DIALOG_SAVE, language )
				, JFileChooser.SAVE_DIALOG, false, JFileChooser.FILES_ONLY
				, "config (*." + ConfigApp.defaultNameFileConfigExtension + ")"
				, new String[] { ConfigApp.defaultNameFileConfigExtension });

		if ((f != null) && (f[0].exists()))
		{
			String[] opts = { UIManager.getString("OptionPane.yesButtonText"), 
					UIManager.getString("OptionPane.noButtonText") };

			int actionDialog = JOptionPane.showOptionDialog( ui, Language.getCaption( Language.DIALOG_REPLACE_FILE_MESSAGE, language )
					, Language.getCaption( Language.DIALOG_REPLACE_FILE_TITLE, language ),
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
					null, opts, opts[ 1 ] );

			if (actionDialog == JOptionPane.NO_OPTION ||
					actionDialog == JOptionPane.CLOSED_OPTION )
			{
				f = null;
			}
		}

		if (f != null)
		{
			try
			{
				ConfigApp.saveConfig(f[0]);
			}
			catch (Exception e)
			{
				ErrorException.showErrorException( ui, e.getMessage(), Language.getCaption( Language.DIALOG_ERROR, language )
						, ErrorException.ERROR_MESSAGE );
			}
		}
	}

	protected void loadFileConfig()
	{
		File[] f = this.selectFile( ConfigApp.defaultNameFileConfig
				, Language.getCaption( Language.DIALOG_LOAD, language )
				, JFileChooser.OPEN_DIALOG, false, JFileChooser.FILES_ONLY
				, "config (*." + ConfigApp.defaultNameFileConfigExtension + ")"
				, new String[] { ConfigApp.defaultNameFileConfigExtension } );

		if ((f != null) && ( f[0].exists() ) )
		{
			loadValueConfig( f[0] );
		}
		else if ((f != null) && (!f[0].exists()))
		{
			ErrorException.showErrorException( ui, "File not exist.", "Error Config File.", ErrorException.WARNING_MESSAGE );
		}
	}

	protected String[] selectUserFile(String defaultName, boolean mustExist, boolean multiSelection, int selectionModel, String descrFilter, String[] filterExtensions )
	{
		File[] f = selectFile(defaultName, "Select result user file", JFileChooser.OPEN_DIALOG
								, multiSelection, selectionModel, descrFilter, filterExtensions );

		int N = 1;
		
		if( f != null && f.length > 0 )
		{
			N = f.length;
		}
		
		String[] path = null;
				
		if (f != null)
		{			
			boolean allFileExist = true;
			for( int iF = 0; iF < N && allFileExist; iF++ )
			{
				allFileExist = f[ iF ].exists();				
			}
						
			
			if ( mustExist && !allFileExist )
			{
				path = null;
				ErrorException.showErrorException(ui, "File(s) not exist.", "Error result user file.", ErrorException.WARNING_MESSAGE );
			}
			else
			{
				//path = f[0].getAbsolutePath();
				path = new String[ N ];
				
				for( int iF = 0; iF < N && allFileExist; iF++ )
				{
					path[ iF ] = f[ iF ].getAbsolutePath();					
				}
			}
		}		

		return path;
	}
	
	private File[] selectFile(String defaulName, String titleDialog, int typeDialog, boolean multiSelection, int selectionModel, String descrFilter, String[] filterExtensions )
	{		
		FileNameExtensionFilter filter = null;
				
		if( filterExtensions != null && filterExtensions.length > 0 )
		{
			filter = new FileNameExtensionFilter( descrFilter, filterExtensions );
		}
		
		
		File[] file = null;

		JFileChooser jfc = null;

		jfc = new JFileChooser(System.getProperty("user.dir"));

		jfc.setMultiSelectionEnabled(multiSelection);

		jfc.setDialogTitle(titleDialog);
		jfc.setDialogType(typeDialog);
		jfc.setFileSelectionMode(selectionModel);
		jfc.setSelectedFile(new File(defaulName));
		
		if( filter != null )
		{
			jfc.setFileFilter( filter );
		}

		int returnVal = jfc.showDialog(ui, null);

		if (returnVal == JFileChooser.APPROVE_OPTION )
		{
			if (multiSelection)
			{
				file = jfc.getSelectedFiles();
			}
			else
			{
				file = new File[1];
				file[0] = jfc.getSelectedFile();
			}
		}

		return file;
	}
	
	private void loadValueConfig(File f)
	{
		try
		{	
			ConfigApp.loadConfig( f );
		}
		catch (Exception e)
		{
			ErrorException.showErrorException(ui, e.getMessage(), "Warning", ErrorException.WARNING_MESSAGE );
		}
	}

	public void setAnswerIcon(int type)
	{
		if (ui.getJPanelObjetivo().isVisible())
		{
			int wd = ui.getJButtonCorrecto().getWidth() - ui.getJButtonCorrecto().getInsets().left - ui.getJButtonCorrecto().getInsets().right;
			ImageIcon ico = new ImageIcon();

			if (type == ANSWER_ICO_CORRECT )
			{
				ico.setImage(this.iconCorrect.getScaledInstance(wd, wd, Image.SCALE_SMOOTH ));
			}
			else if (type == ANSWER_ICO_INCORRECT )
			{
				ico.setImage(this.iconIncorrect.getScaledInstance(wd, wd, Image.SCALE_SMOOTH ));
			}
			else if (type == ANSWER_ICO_TIME_OUT )
			{
				ico.setImage(this.iconTimeOut.getScaledInstance(wd, wd, Image.SCALE_SMOOTH ));
			}

			ui.getJButtonCorrecto().setIcon(ico);
		}
	}

	public void sumResulteLevelIndicator(int sum)
	{
		if ((ui.getJPanelComparacion().isVisible()) && 
				(ui.getJPanelObjetivo().isVisible()))
		{
			ui.getIndicadorNivelCalificacion().setValue(ui.getIndicadorNivelCalificacion().getValue() + sum);
		}
	}

	public boolean isGreenLevelIndicator()
	{
		boolean level = false;

		if ((ui.getJPanelComparacion().isVisible()) && 
				(ui.getJPanelObjetivo().isVisible()))
		{
			level = ui.getIndicadorNivelCalificacion().getValue() >= ui.getIndicadorNivelCalificacion().getLevels()[1];
		}

		return level;
	}

	public void showActity(JPanel activityPanel)
	{
		this.removeActivity();
		
		ui.getJPanelOper().setVisible( false );
		ui.getJPanelOper().add( activityPanel, null);
		ui.getJPanelOper().setVisible( true );
		
		ui.getJPanelOper().transferFocus();
	}

	public void setPrePostMessage(String msg, boolean blackBackground)
	{
		final JButton bt = new JButton();
		bt.setVisible(true);
		bt.setFocusable(false);
		bt.setFocusPainted(false);
		bt.setText(msg);
		bt.setBackground(Color.WHITE);

		if (blackBackground)
		{
			bt.setBackground(Color.BLACK);
			bt.setForeground(Color.LIGHT_GRAY);
		}

		bt.addComponentListener(new ComponentAdapter()
		{

			public void componentResized(ComponentEvent arg0)
			{
				FontMetrics fm = null;
				int div = 2;
				do
				{
					fm = bt.getFontMetrics(new Font( Font.DIALOG, Font.BOLD, bt.getHeight() / div));
					div += 2;
				}
				while ((fm.stringWidth(bt.getText()) > 0) && (fm.stringWidth(bt.getText()) >= bt.getSize().width - bt.getInsets().left - bt.getInsets().right));

				bt.setFont(fm.getFont());
				bt.setText(bt.getText());
			}

		});

		ui.getJPanelOper().setVisible(false);
		ui.getJPanelOper().removeAll();
		ui.getJPanelOper().add(bt);
		ui.getJPanelOper().setVisible(true);
	}

	public void removeActivity()
	{
		ui.getJPanelOper().setVisible(false);

		ui.getJPanelOper().removeAll();

		ui.getJPanelOper().setVisible(true);
	}

	public void cleanTestGUI()
	{
		this.setTestClock( 0L, "00:00" );
		this.removeActivity();
	}

	public void setTestClock(long newValue, String timerString)
	{
		if (this.parameters.isVisibleTestClock())
		{
			ui.getJProgressBarTiempo().setValue((int)newValue);
			ui.getJProgressBarTiempo().setString(timerString);
		}
	}

	public void restoreGUI()
	{
		JMenuItem menuItem = ui.getJMenuPlay();

		menuItem.setText( Language.getCaption( Language.ACTION_PLAY, language ) );		
		menuItem.setIcon( START_ICO );
		menuItem.setSelectedIcon( STOP_ICO );

		ui.getJMenuAcercaDe().setEnabled(true);
		ui.getJMenuConfig().setEnabled(true);
		ui.getJMenuGNUGPL().setEnabled(true);

		if (this.appGUIFullScreen != null)
		{
			if (this.autoHideMenu != null)
			{
				this.autoHideMenu.stopThread( IStoppableThread.ForcedStop );
				this.autoHideMenu = null;
			}

			this.appGUIFullScreen.setVisible(false);

			ui.setVisible(false);
			ui.setContentPane(this.appGUIFullScreen.getContentPane());
			ui.setJMenuBar(this.appGUIFullScreen.getJMenuBar());

			this.appGUIFullScreen.dispose();
			this.appGUIFullScreen = null;

			ui.getJMenuBar().setVisible(true);
			ui.setVisible(true);


		}
		/*
		else if ( ui.getExtendedState() == JFrame.MAXIMIZED_BOTH)
		{
			ui.setExtendedState( 0 );
			if (this.prevGUILocation != null)
			{
				ui.setLocation(this.prevGUILocation);
			}      

		}
		*/
		else if (this.prevGUILocation != null)
		{
			ui.setLocation(this.prevGUILocation);
		}
	}

	public Dimension getActivityPanelSize()
	{
		return ui.getJPanelOper().getSize();
	}

	public void startTest()
	{
		ui.getJMenuConfig().setPreferredSize(ui.getJMenuConfig().getSize());
		ui.getJMenuConfig().setEnabled(false);
		ui.getJMenuAcercaDe().setPreferredSize(ui.getJMenuAcercaDe().getSize());
		ui.getJMenuAcercaDe().setEnabled(false);
		ui.getJMenuGNUGPL().setPreferredSize(ui.getJMenuGNUGPL().getSize());
		ui.getJMenuGNUGPL().setEnabled(false);

		ui.getJButtonCorrecto().setIcon(null);

		try
		{
			coreControl.getInstance().startWorking( coreControl.CORE_START_WORKING_FIRST );
		}
		catch (Exception e)
		{
			restoreGUI();

			stopTest();

			ErrorException.showErrorException( ui, "Excecution problem:" + e.getCause().getMessage(),
					"Error", ErrorException.ERROR_MESSAGE);
		}
	}
	
	public void stopTest()
	{
		try
		{
			coreControl.getInstance().stopWorking( false );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorException.showErrorException( ui, "Excecution problem:" + e.getCause(),
					"Error", ErrorException.ERROR_MESSAGE);
		}
	}

	public void setAppState( String msg )
	{
		JTextField statePanel = ui.getTextState();
				
		statePanel.setText( msg );
		statePanel.setCaretPosition( 0 );
		
		statePanel.setToolTipText( msg );
	}
	
	public void applyGUIParameter(guiParameters pars) throws Exception
	{
		if (pars == null)
		{
			throw new IllegalArgumentException("Parameters null.");
		}

		this.parameters = pars;

		if ((ui.getJPanelComparacion().isVisible()) && 
				(ui.getJPanelObjetivo().isVisible()))
		{
			ui.getJPanelComparacion().setVisible(false);

			int target = pars.getUserAnswerTarget();

			ui.getIndicadorNivelCalificacion().setMaximum(target);
			ui.getIndicadorNivelCalificacion().setValue(pars.getInitialAnswerLevel());

			int[] lvls = { (int)(target * 0.6D), (int)(target * 0.8D) };
			ui.getIndicadorNivelCalificacion().setLevels(lvls);

			ui.getJSliderOtros().setMaximum(target);
			ui.getJSliderOtros().setValue((int)(target * 0.9D));

			ui.getJPanelComparacion().setVisible(true);
		}

		if (ui.getJProgressBarTiempo().isVisible())
		{
			ui.getJProgressBarTiempo().setMaximum(pars.getTimeValue());
		}

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		GraphicsDevice gd = null;

		if (gs.length == 0)
		{
			throw new RuntimeException("No screens found.");
		}
		else
		{
			gd = gs[ 0 ];
			int screen = pars.getIdScreen();
			if( screen > 1 && screen <= gs.length )
			{
				gd = gs[ screen - 1 ];
			}
		}


		if (!pars.isFullScreen())
		{
			int maxiFrame = ui.getExtendedState();

			Rectangle r = gd.getDefaultConfiguration().getBounds();

			if (maxiFrame != JFrame.MAXIMIZED_BOTH )
			{
				this.prevGUILocation = ui.getLocation();

				if (!r.contains(ui.getLocation()))
				{
					Point loc = new Point(r.x, r.y);

					Dimension s = new Dimension(ui.getSize());

					if (ui.getWidth() > r.width)
					{
						s.width = r.width;
					}

					if (ui.getHeight() > r.height)
					{
						s.height = r.height;
					}

					ui.setSize(s);

					ui.setLocation(loc);
				}
			}
			else
			{
				ui.setExtendedState(0);
				ui.setLocation(r.x, r.y);

				if (maxiFrame == JFrame.MAXIMIZED_BOTH)
				{
					ui.setExtendedState( JFrame.MAXIMIZED_BOTH );
				}
			}
		}
		else
		{
			ui.setVisible(false);

			this.appGUIFullScreen = new JFrame();
			this.appGUIFullScreen.setUndecorated(true);
			this.appGUIFullScreen.setJMenuBar(ui.getJMenuBar());
			this.appGUIFullScreen.getJMenuBar().setVisible(false);
			this.appGUIFullScreen.setContentPane(ui.getContentPane());
			this.appGUIFullScreen.setResizable(false);
			this.appGUIFullScreen.setAlwaysOnTop(true);
			this.appGUIFullScreen.setLocation(0, 0);
			this.appGUIFullScreen.setIconImage(ui.getIconImage());
			this.appGUIFullScreen.setDefaultCloseOperation( ui.DISPOSE_ON_CLOSE );

			this.appGUIFullScreen.addFocusListener(new FocusAdapter()
			{

				public void focusGained(FocusEvent e)
				{
					JFrame jf = (JFrame)e.getSource();
					jf.toFront();
				}
			});

			if (gd.isFullScreenSupported())
			{
				gd.setFullScreenWindow(this.appGUIFullScreen);
			}
			else
			{
				Rectangle bounds = gd.getDefaultConfiguration().getBounds();

				this.appGUIFullScreen.setSize(bounds.width, bounds.height);
			}

			this.appGUIFullScreen.setVisible(true);
			this.appGUIFullScreen.toFront();

			this.appGUIFullScreen.addMouseMotionListener(new MouseAdapter()
			{

				public void mouseMoved(MouseEvent e)
				{
					JFrame jf = (JFrame)e.getSource();
					JMenuBar menuBar = jf.getJMenuBar();

					if (menuBar != null)
					{
						menuBar.setVisible(e.getY() < 15);
					}

				}
			});

			this.autoHideMenu = new mouseTracking(this.appGUIFullScreen);

			try
			{
				this.autoHideMenu.startThread();
			}
			catch (Exception e1)
			{
				JMenuBar menuBar = this.appGUIFullScreen.getJMenuBar();
				if (menuBar != null)
				{
					menuBar.setVisible(true);
				}

				this.autoHideMenu.stopThread( IStoppableThread.ForcedStop );
				this.autoHideMenu = null;
			}
		}
	}  

	public void showWaitingStartCommand()
	{
		//-->Start Command MESSAGE
		ui.getJPanelOper().setVisible(false);
		ui.getJPanelOper().removeAll();

		final JButton wait = new JButton();
		wait.setVisible(true);
		wait.setFocusable(false);
		wait.setFocusPainted(false);
		wait.setText( "Waiting for Start Command" );
		wait.setBackground(Color.WHITE);
		wait.addComponentListener(new ComponentAdapter()
		{

			public void componentResized(ComponentEvent arg0)
			{
				FontMetrics fm = null;
				int div = 2;
				do
				{
					fm = wait.getFontMetrics(new Font( Font.DIALOG, Font.BOLD, wait.getHeight() / div));
					div += 2;
				}
				while ((fm.stringWidth(wait.getText()) > 0) && (fm.stringWidth(wait.getText()) >= wait.getSize().width - wait.getInsets().left - wait.getInsets().right));

				wait.setFont(fm.getFont());
				wait.setText(wait.getText());
			}

		});
		ui.getJPanelOper().add(wait, null);

		ui.getJPanelOper().setVisible(true);
		//<--Start Command MESSAGE
	}

	/*
	protected void loadEmotionalItem(boolean loadImage, JList list)
	{
		String title = "Load image(s)";
		String tipo = "image";
		if (!loadImage)
		{
			title = "Load sound(s)";
			tipo = "audio";
		}

		File[] f = this.selectFile( "", title, JFileChooser.OPEN_DIALOG, true, JFileChooser.FILES_ONLY
									, "*.wav", new String[] { "wav" } );

		if (f != null)
		{
			list.setVisible(false);
			boolean error = false;

			for (int i = 0; i < f.length; i++)
			{
				try
				{
					String type = "";

					MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
					mtftp.addMimeTypes("image png tif jpg jpeg bmp");
					mtftp.addMimeTypes("audio wav");

					String mimetype = mtftp.getContentType(f[i]);
					if (mimetype != null)
					{
						type = mimetype.split("/")[0];
					}

					if (type.equals(tipo))
					{
						((DefaultListModel)list.getModel()).addElement(f[i].getCanonicalPath());
					}
					else
					{
						error = true;
					}
				}
				catch (Exception e)
				{
					ErrorException.showErrorException( ui, e.getMessage(), "Error", ErrorException.ERROR_MESSAGE );
				}
			}

			if (error)
			{
				ErrorException.showErrorException( ui, "Some files are not "+ tipo +" or compatible.", "Warning!", ErrorException.WARNING_MESSAGE );
			}

			list.setVisible(true);
		}
	}
	*/

	protected List<String> loadEmotionalItem(boolean loadImage)
	{
		String title = "Load image(s)";
		String tipo = "image";
		
		String descrFilter = "Image";
		String[] filters = ImageIO.getReaderFormatNames();
		
		if (!loadImage)
		{
			title = "Load sound(s)";
			tipo = "audio";
			
			descrFilter = "*.wav";
			filters = new String[] { "wav" };
		}

		File[] f = this.selectFile( "", title, JFileChooser.OPEN_DIALOG, true, JFileChooser.FILES_ONLY
									, descrFilter, filters );

		List<String> files = new ArrayList< String >();

		if (f != null)
		{
			boolean error = false;

			for (int i = 0; i < f.length; i++)
			{
				try
				{
					String type = "";

					MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
					mtftp.addMimeTypes("image png tif jpg jpeg bmp");
					mtftp.addMimeTypes("audio wav");

					String mimetype = mtftp.getContentType(f[i]);
					if (mimetype != null)
					{
						type = mimetype.split("/")[0];
					}

					if (type.equals(tipo))
					{
						files.add(f[i].getCanonicalPath());
					}
					else
					{
						error = true;
					}
				}
				catch (Exception e)
				{
					ErrorException.showErrorException( ui, e.getMessage(), "Error", ErrorException.ERROR_MESSAGE );
				}
			}

			if (error)
			{
				files.clear();
				ErrorException.showErrorException( ui, "Some files are not "+ tipo +" or compatible.", "Warning!", ErrorException.WARNING_MESSAGE );				
			}
		}

		return files;
	}
}