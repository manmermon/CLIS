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

import Activities.Operaciones.opEmotionalTask;
import Activities.Operaciones.panelImage;
import Auxiliar.AffectiveObject;
import Config.ConfigApp;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.SpinnerNumberCellEditor;
import GUI.MyComponents.clipSoundThread;
import GUI.MyComponents.imagenPoligono2D;
import StoppableThread.IStoppableThread;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class settingMenu_AffectiveSetting extends JDialog
{
	private static final long serialVersionUID = 3725925839434670267L;

	//MENUBARS
	private JMenuBar jmenubarEmotionTask;

	//MENUITEMS
	private JMenuItem jmenuItemSlideMoveUP;
	private JMenuItem jmenuItemSlideMoveDOWN;
	private JMenuItem jmenuItemSlideMoveDel;
	private JMenuItem jBtnInfo;

	//MENUS
	private JMenu jmenuRandomSort;
	private JMenu jmenuRandomSortType;

	//PANELS
	private JPanel jPanelSlideMenu;
	private JPanel jPanelSlideShowImgs;
	private JPanel jPanelAddJListSlides;
	private JPanel jPanelCtrlLists;
	private JPanel jPanelContentJListSoundClips;
	private JPanel jPanelContentJListSlides;
	private JPanel jPanelAddJListSoundClips;
	private JPanel jPanelMain;
	private JPanel jPanelBtnCtrl;
	private JPanel jPanelMenuBarOpts;
	private JPanel jPanelMenuBarActWinBt;

	//SCROLLPANELS
	private JScrollPane jScrollPanelSlideList;
	private JScrollPane jScrollPanelSoundList;
	private JScrollPane jScrollMenuBar;

	//SPLITPANELS
	private JSplitPane jPanelSlideImg;
	private JSplitPane jSplitPaneCtrlLists;

	//BUTTONS
	private JButton jButtonSlideAddImg;
	private JButton jButtonSoundAdd;
	private JButton jButtonMaxWin;

	//LABELS
	private JLabel jLabelNumberSlideImgs;
	private JLabel jLabelNumberSounds;
	private JLabel jLableNumGroups;
	private JLabel jLableNumBlocks;

	//clipSoundThread
	private clipSoundThread sound;

	//JSPINNER
	private JSpinner NumGroups;
	private JSpinner NumBlocks;

	//JTABLES
	private JTable jTableImgs;
	private JTable jTableSounds;

	//JCheckbox
	private JCheckBox jChkbxPreserverSlideSoundCorrespondence;
	private JCheckBox jChkbxSlideMainGroup;

	//JList
	private JList<String> jListRandomOrder;

	//JDIALOG
	private JDialog winInfo;

	//JTEXTPANE
	private JTextPane textAreaInfo;

	//
	private boolean isOpeningDialog = true;
	//private boolean isSelecting = false;

	private boolean soundLoadError = false;

	private final int affectiveFileColumn = 4;

	public settingMenu_AffectiveSetting( Window owner, String title )
	{
		super( owner );

		init( title );
	}

	private void init( String title )
	{
		super.setTitle( title );

		super.getRootPane().registerKeyboardAction( keyActions.getEscapeCloseWindows( "EscapeCloseWindow" ), 
													KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), 
													JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		this.isOpeningDialog = true;

		super.setContentPane( this.getJPanelSlideMenu() );

		this.isOpeningDialog = false;

		this.updateNumberOfGroupsAndBlocks();

		super.addWindowListener(new WindowAdapter()
		{
			public void windowDeactivated(WindowEvent e) 
			{}

			public void windowClosing(WindowEvent e)
			{
				finish(e, true);
			}


			public void windowClosed(WindowEvent e)
			{
				finish(e, true);
			}


			public void windowLostFocus(WindowEvent e)
			{
				finish(e, true);
			}

			private void finish(WindowEvent e, boolean close)
			{
				getSlideTable().clearSelection();
				getSoundTable().clearSelection();
				showSelectedEmotionalElement( getSoundTable(), getJPanelSlideShowImgs(), false );

				if (close)
				{
					((JDialog)e.getSource()).dispose();
				}
			}
		});
	}

	private void loadListElemets(List<AffectiveObject> list, JTable table)
	{
		for (AffectiveObject values : list)
		{
			updateAffectiveTable(table, values.getPathFile(), values.getGroup(), 
					values.getBlock(), values.isFixPosition(), 
					values.isSAM(), values.isBeepActive(), true);
		}

		int grp = (Integer)ConfigApp.getProperty( ConfigApp.AFFECTIVE_GROUPS );
		if ((grp > table.getRowCount()) && (table.getRowCount() > 0))
		{
			this.getJSpinnerNumGroups().setValue( table.getRowCount() );
		}

		int block = (Integer)ConfigApp.getProperty( ConfigApp.AFFECTIVE_BLOCKS );
		if ((block > table.getRowCount()) && (table.getRowCount() > 0))
		{
			this.getJSpinnerNumBlocks().setValue( table.getRowCount() );
		}
	}

	private void updateListProperty(JTable table, final String ID, boolean isSlide)
	{
		int affType = AffectiveObject.IS_PICTURE;
		if( !isSlide )
		{
			affType = AffectiveObject.IS_SOUND;
		}

		List<AffectiveObject> l = new ArrayList<AffectiveObject>();
		for( int i = 0; i < table.getRowCount(); i++ )
		{
			AffectiveObject values = new AffectiveObject( table.getValueAt( i, this.affectiveFileColumn ).toString()
					, (Integer)table.getValueAt( i, 5 )
					, (Integer)table.getValueAt( i, 6 )
					, (Boolean)table.getValueAt( i, 1 )
					, (Boolean)table.getValueAt( i,  2 )
					, (Boolean)table.getValueAt( i, 3 )
					, affType );

			l.add( values );
		}

		ConfigApp.setProperty( ID, l );
	}

	private JLabel getJLabelNumberSounds()
	{
		if (this.jLabelNumberSounds == null)
		{
			this.jLabelNumberSounds = new JLabel("No. clips: 0");
		}

		return this.jLabelNumberSounds;
	}

	private JPanel getJPanelSlideMenu()
	{
		if (this.jPanelSlideMenu == null)
		{
			this.jPanelSlideMenu = new JPanel();
			this.jPanelSlideMenu.setLayout(new BorderLayout(0, 0));

			this.jPanelSlideMenu.add( this.getJPanelMenuBarOpts(), BorderLayout.NORTH );
			this.jPanelSlideMenu.add( this.getJPanelMain(), BorderLayout.CENTER);
		}

		return this.jPanelSlideMenu;
	}

	private JPanel getJPanelAddJListSlides()
	{
		if (this.jPanelAddJListSlides == null)
		{
			this.jPanelAddJListSlides = new JPanel();
			this.jPanelAddJListSlides.setLayout(new BoxLayout(this.jPanelAddJListSlides, BoxLayout.X_AXIS));

			this.jPanelAddJListSlides.add( this.getJButtonSlideAddImg());
			this.jPanelAddJListSlides.add( this.getJLabelNumberSlideImgs());
		}

		return this.jPanelAddJListSlides;
	}

	private JButton getJButtonSlideAddImg()
	{
		if (this.jButtonSlideAddImg == null)
		{
			this.jButtonSlideAddImg = new JButton(" + ");
			this.jButtonSlideAddImg.setBorder(BorderFactory.createEtchedBorder());

			Font font = this.jButtonSlideAddImg.getFont();
			this.jButtonSlideAddImg.setFont(new Font(font.getFontName(), font.getStyle(), (int)(font.getSize() * 1.5D)));

			this.jButtonSlideAddImg.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					List<String> imgFiles = guiManager.getInstance().loadEmotionalItem( true );          

					//isSelecting = true;

					JTable t = getSlideTable();
					for (String img : imgFiles)
					{
						updateAffectiveTable(t, img, 1, 1, false, true, false, true);
					}

					//isSelecting = false;

					updateNumberOfGroupsAndBlocks();
				}
			});
		}
		return this.jButtonSlideAddImg;
	}

	private void updateNumberOfGroupsAndBlocks()
	{
		if ( !this.isOpeningDialog )
		{
			JTable imgT = getSlideTable();
			JTable soundT = getSoundTable();

			int rowImg = imgT.getRowCount();
			int rowSound = soundT.getRowCount();

			int groupLimit = rowImg;
			if( rowImg > 0 && rowSound > 0 )
			{
				groupLimit = Math.min( rowImg, rowSound );
			}
			else if(  rowImg == 0 && rowSound == 0 )
			{
				groupLimit = 1;
			}
			else
			{
				if( groupLimit == 0 )
				{
					groupLimit = rowSound;
				}
			}      

			// Groups
			SpinnerNumberModel sp = (SpinnerNumberModel)getJSpinnerNumGroups().getModel();

			int val = ((Integer)sp.getValue()).intValue();

			if (val > groupLimit)
			{
				val = groupLimit;
			}

			sp.setMaximum(Integer.valueOf(groupLimit));
			sp.setValue(Integer.valueOf(val));


			// Blocks
			sp = (SpinnerNumberModel)getJSpinnerNumBlocks().getModel();

			val = ((Integer)sp.getValue()).intValue();

			if (val > groupLimit)
			{
				val = groupLimit;
			}

			sp.setMaximum(Integer.valueOf(groupLimit));
			sp.setValue(Integer.valueOf(val));
		}
	}

	private JScrollPane getJScrollPanelSlideList()
	{
		if (this.jScrollPanelSlideList == null)
		{
			this.jScrollPanelSlideList = new JScrollPane();

			this.jScrollPanelSlideList.setViewportView(getSlideTable());
		}

		return this.jScrollPanelSlideList;
	}

	private JLabel getJLabelNumberSlideImgs()
	{
		if (this.jLabelNumberSlideImgs == null)
		{
			this.jLabelNumberSlideImgs = new JLabel("No. Imgs: 0");
		}

		return this.jLabelNumberSlideImgs;
	}

	private JPanel getJPanelMain()
	{
		if (this.jPanelMain == null)
		{
			this.jPanelMain = new JPanel();

			this.jPanelMain.setLayout(new BorderLayout());

			this.jPanelMain.add( this.getJPanelSlideImg(), BorderLayout.CENTER );
		}

		return this.jPanelMain;
	}

	private JPanel getJPanelBtnCtrl()
	{
		if (this.jPanelBtnCtrl == null)
		{
			this.jPanelBtnCtrl = new JPanel();

			this.jPanelBtnCtrl.setLayout(new FlowLayout( FlowLayout.LEFT ));

			this.jPanelBtnCtrl.add( this.getJButtonSlideMoveUP() );
			this.jPanelBtnCtrl.add( this.getJButtonSlideMoveDOWN() );
			this.jPanelBtnCtrl.add( this.getJButtonSlideMoveDel() );
		}

		return this.jPanelBtnCtrl;
	}

	private JSplitPane getJPanelSlideImg()
	{
		if (this.jPanelSlideImg == null)
		{
			this.jPanelSlideImg = new JSplitPane();

			this.jPanelSlideImg.setResizeWeight(0.5D);
			this.jPanelSlideImg.setDividerLocation(0.5D);
			this.jPanelSlideImg.setOrientation( JSplitPane.VERTICAL_SPLIT );
			this.jPanelSlideImg.setLeftComponent( this.getPanel_21());
			this.jPanelSlideImg.setRightComponent( this.getJPanelSlideShowImgs());
		}
		return this.jPanelSlideImg;
	}

	private JPanel getPanel_21()
	{
		if (this.jPanelCtrlLists == null)
		{
			this.jPanelCtrlLists = new JPanel();

			this.jPanelCtrlLists.setLayout(new BorderLayout(0, 0));

			this.jPanelCtrlLists.add( this.getPanel_22(), BorderLayout.CENTER);
		}

		return this.jPanelCtrlLists;
	}

	private JSplitPane getPanel_22()
	{
		if (this.jSplitPaneCtrlLists == null)
		{
			this.jSplitPaneCtrlLists = new JSplitPane();
			this.jSplitPaneCtrlLists.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

			this.jSplitPaneCtrlLists.setResizeWeight(0.5D);
			this.jSplitPaneCtrlLists.setDividerLocation(0.5D);
			this.jSplitPaneCtrlLists.setDividerSize(0);

			this.jSplitPaneCtrlLists.setEnabled(false);
			this.jSplitPaneCtrlLists.setLeftComponent( this.getJPanelContentJListSlides());
			this.jSplitPaneCtrlLists.setRightComponent( this.getJPanelContentJListSoundClips());
		}
		return this.jSplitPaneCtrlLists;
	}

	private JPanel getJPanelContentJListSlides()
	{
		if (this.jPanelContentJListSlides == null)
		{
			this.jPanelContentJListSlides = new JPanel();
			this.jPanelContentJListSlides.setLayout(new BorderLayout());
			this.jPanelContentJListSlides.setBorder( new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Images", TitledBorder.CENTER, TitledBorder.TOP, null, null)  );

			this.jPanelContentJListSlides.add( getJPanelAddJListSlides(), BorderLayout.NORTH );
			this.jPanelContentJListSlides.add( getJScrollPanelSlideList(), BorderLayout.CENTER );
		}

		return this.jPanelContentJListSlides;
	}

	private JPanel getJPanelContentJListSoundClips()
	{
		if (this.jPanelContentJListSoundClips == null)
		{
			this.jPanelContentJListSoundClips = new JPanel();
			this.jPanelContentJListSoundClips.setLayout(new BorderLayout());
			jPanelContentJListSoundClips.setBorder( new TitledBorder( new LineBorder( SystemColor.inactiveCaption, 2), "Sounds", TitledBorder.CENTER, TitledBorder.TOP, null, null) );			

			jPanelContentJListSoundClips.add( this.getJPanelAddJListSoundClips(), BorderLayout.NORTH );
			jPanelContentJListSoundClips.add( this.getJScrollPanelSoundList(), BorderLayout.CENTER );
		}

		return this.jPanelContentJListSoundClips;
	}

	private JPanel getJPanelAddJListSoundClips()
	{
		if (this.jPanelAddJListSoundClips == null)
		{
			this.jPanelAddJListSoundClips = new JPanel();
			this.jPanelAddJListSoundClips.setLayout( new BoxLayout( this.jPanelAddJListSoundClips, BoxLayout.X_AXIS) );

			this.jPanelAddJListSoundClips.add( this.getJButtonSoundAdd());
			this.jPanelAddJListSoundClips.add( this.getJLabelNumberSounds());
		}

		return this.jPanelAddJListSoundClips;
	}

	private JButton getJButtonSoundAdd()
	{
		if (this.jButtonSoundAdd == null)
		{
			this.jButtonSoundAdd = new JButton(" + ");
			Font font = this.jButtonSoundAdd.getFont();
			this.jButtonSoundAdd.setFont(new Font(font.getFontName(), font.getStyle(), (int)(font.getSize() * 1.5D)));
			this.jButtonSoundAdd.setBorder(BorderFactory.createEtchedBorder());

			this.jButtonSoundAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					List<String> soundFiles = guiManager.getInstance().loadEmotionalItem(false);

					//isSelecting = true;

					JTable t = getSoundTable();
					for (String sound : soundFiles)
					{
						updateAffectiveTable(t, sound, 1, 1, false, true, false, true);
					}

					//isSelecting = false;

					updateNumberOfGroupsAndBlocks();
				}
			});
		}

		return this.jButtonSoundAdd;
	}

	private JScrollPane getJScrollPanelSoundList()
	{
		if (this.jScrollPanelSoundList == null)
		{
			this.jScrollPanelSoundList = new JScrollPane();

			this.jScrollPanelSoundList.setViewportView(getSoundTable());
		}

		return this.jScrollPanelSoundList;
	}

	private JPanel getJPanelSlideShowImgs()
	{
		if (this.jPanelSlideShowImgs == null)
		{
			this.jPanelSlideShowImgs = new JPanel();
			this.jPanelSlideShowImgs.setName("SlideMostrarImgs");
			this.jPanelSlideShowImgs.setLayout(new BorderLayout(0, 0));
		}
		return this.jPanelSlideShowImgs;
	}

	private JPanel getJPanelMenuBarOpts()
	{
		if( this.jPanelMenuBarOpts == null )
		{
			this.jPanelMenuBarOpts = new JPanel( new BorderLayout() );
			
			
			
			this.jPanelMenuBarOpts.add( this.getJScrollMenuBar(), BorderLayout.CENTER );
			this.jPanelMenuBarOpts.add( this.getJPanelMenuBarActWinBt(), BorderLayout.EAST );
		}
		
		return this.jPanelMenuBarOpts;
	}
	
	private JScrollPane getJScrollMenuBar()
	{
		if( this.jScrollMenuBar == null )
		{
			this.jScrollMenuBar = new JScrollPane( this.getJMenuBarEmotionTask() );
			this.jScrollMenuBar.setBorder( BorderFactory.createEmptyBorder() );
			
			this.jScrollMenuBar.getVerticalScrollBar().setPreferredSize( new Dimension( 10, 0 ) );
			this.jScrollMenuBar.getHorizontalScrollBar().setPreferredSize( new Dimension( 0, 10 ) );
		}
		
		return this.jScrollMenuBar;
	}
	
	private JMenuBar getJMenuBarEmotionTask()
	{
		if (this.jmenubarEmotionTask == null)
		{
			this.jmenubarEmotionTask = new JMenuBar();
			this.jmenubarEmotionTask.setBorder( BorderFactory.createEmptyBorder(1, 1, 0, 0) );

			FlowLayout ly = new FlowLayout();
			ly.setAlignment( FlowLayout.LEFT );
			ly.setHgap(0);
			ly.setVgap(0);

			this.jmenubarEmotionTask.setLayout(ly);
			this.jmenubarEmotionTask.setBackground( SystemColor.menu );

			this.jmenubarEmotionTask.add( this.getJPanelBtnCtrl() );
			this.jmenubarEmotionTask.add( this.getJMenuRandomSort() );
			this.jmenubarEmotionTask.add( this.getJLabelNumGroups() );
			this.jmenubarEmotionTask.add( this.getJSpinnerNumGroups() );
			this.jmenubarEmotionTask.add( this.getJLabelNumBlocks() );
			this.jmenubarEmotionTask.add( this.getJSpinnerNumBlocks() );			
		}

		return this.jmenubarEmotionTask;
	}

	private JPanel getJPanelMenuBarActWinBt()
	{
		if( this.jPanelMenuBarActWinBt == null )
		{
			this.jPanelMenuBarActWinBt = new JPanel();
			
			GridLayout ly = new GridLayout( 1, 0 );
			this.jPanelMenuBarActWinBt.setLayout( ly );		
			this.jPanelMenuBarActWinBt.setBackground( SystemColor.menu );		
			
			this.jPanelMenuBarActWinBt.add( this.getJButtonMaxWin() );
		}
		
		return  this.jPanelMenuBarActWinBt;
	}
	
	private JButton getJButtonMaxWin()
	{
		if( this.jButtonMaxWin == null )
		{
			this.jButtonMaxWin = new JButton();	
						
			this.jButtonMaxWin.setFocusable( false );
			this.jButtonMaxWin.setBorder( BorderFactory.createEmptyBorder() );
			this.jButtonMaxWin.setBackground( SystemColor.menu );			
			this.jButtonMaxWin.setPreferredSize( new Dimension( 30, 26 ) );			
			
			Icon ic = GeneralAppIcon.WindowMax( 16, Color.BLACK );
			
			if( ic == null )
			{
				this.jButtonMaxWin.setText( "+" );
			}
			else
			{
				this.jButtonMaxWin.setIcon( ic );
			}
			
			this.jButtonMaxWin.addMouseListener( new MouseAdapter() 
			{					
				@Override
				public void mouseExited(MouseEvent e) 
				{
					JButton b = (JButton)e.getSource();
					
					b.setBackground( b.getBackground().brighter() );
				}
				
				@Override
				public void mouseEntered(MouseEvent e) 
				{
					JButton b = (JButton)e.getSource();
					
					b.setBackground( b.getBackground().darker() );
				}
			});
			
			this.jButtonMaxWin.addActionListener( new ActionListener() 
			{	
				private Rectangle dialogSize = null;
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JButton b = (JButton)e.getSource();
					
					settingMenu_AffectiveSetting dialog = (settingMenu_AffectiveSetting)SwingUtilities.getRoot( b );
					
					if( dialogSize == null )
					{					
						dialogSize = dialog.getBounds();
						
						Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
						
						dialog.setBounds( r );
						
						Icon ic = GeneralAppIcon.WindowUndoMaximize( 16, Color.BLACK );
						
						if( ic == null )
						{
							b.setText( "-" );
						}
						else
						{
							b.setIcon( ic );
						}
						
						b.setIcon( ic );
					}
					else
					{						
						dialog.setBounds( dialogSize );

						Icon ic = GeneralAppIcon.WindowMax( 16, Color.BLACK );
						
						if( ic == null )
						{
							b.setText( "+" );
						}
						else
						{
							b.setIcon( ic );
						}
						
						dialogSize = null;
					}
				}
			});
		}
		
		return this.jButtonMaxWin;
	}
	
	private JMenu getJMenuRandomSort()
	{
		if (this.jmenuRandomSort == null)
		{
			this.jmenuRandomSort = new JMenu("Random Sort");

			this.jmenuRandomSort.setIcon(new ImageIcon(imagenPoligono2D.crearImagenTriangulo(6, 1.0F, Color.DARK_GRAY, Color.DARK_GRAY, imagenPoligono2D.SOUTH )));
			this.jmenuRandomSort.setBorder(BorderFactory.createEtchedBorder());

			this.jmenuRandomSort.add( this.getJMenuRandomSortType());

			this.jmenuRandomSort.add( this.getJChkbxPreserverSlideSoundCorrespondence());
			this.jmenuRandomSort.add( this.getJChkbxSlideMainGroup());
			this.jmenuRandomSort.add( new JSeparator());
			this.jmenuRandomSort.add( this.getJButtonInfo());
		}

		return this.jmenuRandomSort;
	}

	private JMenu getJMenuRandomSortType()
	{
		if (this.jmenuRandomSortType == null)
		{
			this.jmenuRandomSortType = new JMenu("Type");
			this.jmenuRandomSortType.add( this.getJComboboxRandomSampleTypes());
		}

		return this.jmenuRandomSortType;
	}

	private JLabel getJLabelNumGroups()
	{
		if (this.jLableNumGroups == null)
		{
			this.jLableNumGroups = new JLabel("  No. Groups: ");
		}

		return this.jLableNumGroups;
	}

	private JLabel getJLabelNumBlocks()
	{
		if (this.jLableNumBlocks == null)
		{
			this.jLableNumBlocks = new JLabel("  No. Blocks: ");
		}

		return this.jLableNumBlocks;
	}

	private JMenuItem getJButtonSlideMoveUP()
	{
		if (this.jmenuItemSlideMoveUP == null)
		{
			String txt = "Up";
			this.jmenuItemSlideMoveUP = new JMenuItem(txt);
			this.jmenuItemSlideMoveUP.setFocusable(false);

			this.jmenuItemSlideMoveUP.setBorder(BorderFactory.createEtchedBorder());

			this.jmenuItemSlideMoveUP.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JTable list = getSlideTable();
					if (getSoundTable().isFocusOwner())
					{
						list = getSoundTable();
					}

					moveSlideSound(list, -1);
					list.requestFocus();
				}

			});

			this.jmenuItemSlideMoveUP.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent arg0)
				{
					((JMenuItem)arg0.getSource()).setArmed(true);
				}

				public void mouseReleased(MouseEvent e)
				{
					((JMenuItem)e.getSource()).setArmed(false);
				}

			});

			this.jmenuItemSlideMoveUP.setIcon(new ImageIcon(imagenPoligono2D.crearImagenTriangulo(10, 1.0F, Color.BLACK, Color.BLUE, imagenPoligono2D.NORTH )));
		}
		return this.jmenuItemSlideMoveUP;
	}

	private JMenuItem getJButtonSlideMoveDOWN()
	{
		if (this.jmenuItemSlideMoveDOWN == null)
		{
			String txt = "Down";
			this.jmenuItemSlideMoveDOWN = new JMenuItem(txt);
			this.jmenuItemSlideMoveDOWN.setIcon(new ImageIcon(imagenPoligono2D.crearImagenTriangulo(10, 1.0F, Color.BLACK, Color.BLUE, imagenPoligono2D.SOUTH)));
			this.jmenuItemSlideMoveDOWN.setFocusable(false);

			this.jmenuItemSlideMoveDOWN.setBorder(BorderFactory.createEtchedBorder());

			this.jmenuItemSlideMoveDOWN.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JTable list = getSlideTable();
					if (getSoundTable().isFocusOwner())
					{
						list = getSoundTable();
					}

					moveSlideSound(list, 1);
					list.requestFocus();
				}

			});

			this.jmenuItemSlideMoveDOWN.addMouseListener(new MouseAdapter()
			{

				public void mousePressed(MouseEvent arg0)
				{
					((JMenuItem)arg0.getSource()).setArmed(true);
				}

				public void mouseReleased(MouseEvent e)
				{
					((JMenuItem)e.getSource()).setArmed(false);
				}
			});

		}

		return this.jmenuItemSlideMoveDOWN;
	}

	private JMenuItem getJButtonSlideMoveDel()
	{
		if (this.jmenuItemSlideMoveDel == null)
		{
			String txt = "Delete";
			this.jmenuItemSlideMoveDel = new JMenuItem(txt);
			this.jmenuItemSlideMoveDel.setIcon(new ImageIcon(imagenPoligono2D.crearImagenTexto(1, 0, "x", 
					this.jmenuItemSlideMoveDel.getFontMetrics(this.jmenuItemSlideMoveDel.getFont()), 
					Color.BLACK, Color.RED, null)));

			this.jmenuItemSlideMoveDel.setBorder(BorderFactory.createEtchedBorder());

			this.jmenuItemSlideMoveDel.setText(txt);

			this.jmenuItemSlideMoveDel.setFocusable(false);

			this.jmenuItemSlideMoveDel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JTable list = getSlideTable();
					if (getSoundTable().isFocusOwner())
					{
						list = getSoundTable();
					}

					deleteEmotionalElement(list);
				}

			});

			this.jmenuItemSlideMoveDel.addMouseListener(new MouseAdapter()
			{

				public void mousePressed(MouseEvent arg0)
				{
					((JMenuItem)arg0.getSource()).setArmed(true);
				}

				public void mouseReleased(MouseEvent e)
				{
					((JMenuItem)e.getSource()).setArmed(false);
				}
			});

		}
		return this.jmenuItemSlideMoveDel;
	}

	private void showSelectedEmotionalElement(JTable jTableAffective, JPanel jPanelSlideShowImgs, boolean isImage)
	{
		if( this.sound != null )
		{
			this.sound.stopThread( IStoppableThread.ForcedStop );
			this.sound = null;
		}

		if( jTableAffective.getSelectedRow() > -1 )
		{	
			final String item = jTableAffective.getValueAt( jTableAffective.getSelectedRow(), this.affectiveFileColumn ).toString();

			if( !jPanelSlideShowImgs.getName().equals( item ) )
			{
				final panelImage b2 = new panelImage();
				b2.setName( item );
				b2.setBorder( new EmptyBorder( 5, 0, 5, 0 ) );
				b2.setBackground( jPanelSlideShowImgs.getBackground() );
				b2.setFocusable( false );

				try 
				{	
					final Image img[] = new Image[ 1 ];
					img[ 0 ] = imagenPoligono2D.crearImagenTriangulo( 700
							, 1
							, Color.BLACK
							, Color.GREEN
							,  imagenPoligono2D.EAST );
					if( isImage )
					{
						img[ 0 ] = ImageIO.read( new File( item ) );
					}

					b2.setImage( img[0] );

					if( !isImage && !this.soundLoadError )
					{
						try 
						{
							this.sound = new clipSoundThread( );							
							this.sound.addParameter( clipSoundThread.ID_SOUND_PARAMETER, item );
							this.sound.createTask();
						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							//e.printStackTrace();
							ErrorException.showErrorException( appUI.getInstance(), 
									e.getMessage(), "Error", 
									ErrorException.ERROR_MESSAGE );
							this.sound = null;
							this.soundLoadError = true; 
						}
					}

					final clipSoundThread soundAux = this.sound;
					b2.addMouseListener( new MouseAdapter()
					{
						public void mousePressed( MouseEvent e )
						{
							if( soundAux != null )
							{
								JPanel p = ((JPanel)e.getSource());
								p.setBackground( p.getBackground().darker() );
							}
						}

						public void mouseReleased( MouseEvent e )
						{
							if( soundAux != null )
							{
								JPanel p = ((JPanel)e.getSource());
								p.setBackground( p.getBackground().brighter() );
							}
						}

						@Override
						public void mouseClicked(MouseEvent arg0) 
						{
							if( soundAux != null )
							{
								panelImage b3 = (panelImage)arg0.getSource();

								if( soundAux.isPlay() )
								{
									soundAux.stopSound();
									Image img = b3.getImage();
									Image auxImg = imagenPoligono2D.crearImagenRectangulo( img.getWidth( null ) / 3
											, img.getHeight( null )
											, 1.0F
											, Color.ORANGE
											, Color.ORANGE );

									Image newImage = imagenPoligono2D.crearLienzoVacio( img.getWidth( null )
											, img.getHeight( null )
											, null );
									imagenPoligono2D.componerImagen( newImage, 0, 0, auxImg );
									imagenPoligono2D.componerImagen( newImage, ( img.getWidth( null ) * 2 ) / 3, 0, auxImg );

									b3.setImage( newImage );
								}
								else
								{
									try 
									{
										//soundAux.reset();
										b3.setImage( img[ 0 ] );
										soundAux.continueSound();

										if( !soundAux.isStart() )
										{
											soundAux.startTask();
										}
									} 
									catch (Exception e) 
									{
										// TODO Auto-generated catch block
										//e.printStackTrace();
										ErrorException.showErrorException( appUI.getInstance(), e.getMessage(), 
												"Error", ErrorException.ERROR_MESSAGE );
									}									
								}
							}
						}
					});

				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//new ErrorException( jPanelSlideShowImgs.getRootPane(), e.getMessage(), "File Error", ErrorException.ERROR_MESSAGE, false );
					b2.add( new JButton( e.getMessage() ) );
				}
				finally
				{
					b2.setImageCenter( true );

					jPanelSlideShowImgs.setVisible( false );
					jPanelSlideShowImgs.removeAll();

					jPanelSlideShowImgs.setLayout( new BorderLayout() );
					jPanelSlideShowImgs.setName( item + jTableAffective.getSelectedRow() );

					jPanelSlideShowImgs.add( b2, BorderLayout.CENTER );

					jPanelSlideShowImgs.setVisible( true );
				}
			}	
		}
		else
		{
			jPanelSlideShowImgs.setVisible( false );
			jPanelSlideShowImgs.removeAll();
			jPanelSlideShowImgs.setVisible( true );
		}
	}

	private void moveSlideSound(JTable list, int relativePos)
	{
		int indexRow = list.getSelectedRow();
		int newIndexRow = indexRow + relativePos;

		if ((indexRow > -1) && 
				(newIndexRow > -1) && 
				(newIndexRow < list.getRowCount()))
		{
			DefaultTableModel model = (DefaultTableModel)list.getModel();

			Object v1 = model.getValueAt(newIndexRow, 0);
			Object v2 = model.getValueAt(indexRow, 0);

			model.setValueAt(v2, newIndexRow, 0);
			model.setValueAt(v1, indexRow, 0);

			model.moveRow(indexRow, indexRow, newIndexRow);

			list.setRowSelectionInterval(newIndexRow, newIndexRow);
		}
	}

	private void deleteEmotionalElement(JTable list)
	{
		int[] index = list.getSelectedRows();

		if (index.length > 0)
		{
			DefaultTableModel model = (DefaultTableModel)list.getModel();

			int minRowIndex = Integer.MAX_VALUE;
			for (int i = index.length - 1; i >= 0; i--)
			{
				model.removeRow(index[i]);

				if (index[i] < minRowIndex)
				{
					minRowIndex = index[i];
				}
			}

			for (int i = minRowIndex; i < model.getRowCount(); i++)
			{
				model.setValueAt(Integer.valueOf(i + 1), i, 0);
			}

			if (list.getRowCount() > 0)
			{
				if (index.length > 1)
				{
					list.setRowSelectionInterval(0, 0);


				}
				else if (index[0] < list.getRowCount())
				{
					list.setRowSelectionInterval(index[0], index[0]);
				}
				else
				{
					list.setRowSelectionInterval(0, 0);
				}

			}
			else
			{
				getJPanelSlideShowImgs().setVisible(false);
				getJPanelSlideShowImgs().removeAll();
				getJPanelSlideShowImgs().setVisible(true);
			}

			updateNumberOfGroupsAndBlocks();
		}
	}

	private JSpinner getJSpinnerNumGroups()
	{
		if (this.NumGroups == null)
		{
			final String ID = ConfigApp.AFFECTIVE_GROUPS;

			this.NumGroups = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
			Dimension d = this.NumGroups.getPreferredSize();
			d.width = 60;
			this.NumGroups.setPreferredSize(d);

			this.NumGroups.setEditor(new JSpinner.NumberEditor(this.NumGroups, "#"));
			this.NumGroups.addMouseWheelListener(new MouseWheelListener()
			{

				public void mouseWheelMoved(MouseWheelEvent e)
				{
					if (e.getScrollType() == 0)
					{
						try
						{
							JSpinner sp = (JSpinner)e.getSource();

							int d = e.getWheelRotation();

							if (d > 0)
							{
								sp.setValue(sp.getModel().getPreviousValue());
							}
							else
							{
								sp.setValue(sp.getModel().getNextValue());
							}


						}
						catch (IllegalArgumentException localIllegalArgumentException) {}
					}

				}
			});

			this.NumGroups.addChangeListener(new ChangeListener()
			{

				public void stateChanged(ChangeEvent e)
				{
					int nG = (Integer)NumGroups.getValue();

					ConfigApp.setProperty( ID, nG );

					List<JTable> ts = new ArrayList< JTable >();

					ts.add(getSlideTable());
					ts.add(getSoundTable());
					for( JTable t : ts )
					{
						int grCol = t.getColumnCount() - 2;

						for( int i = 0; i < t.getRowCount(); i++ )
						{		
							SpinnerNumberCellEditor cell = (SpinnerNumberCellEditor)t.getCellEditor( i, grCol );
							cell.stopCellEditing();
							JSpinner sp2 = (JSpinner)cell.getTableCellEditorComponent( t, cell.getCellEditorValue(), true, i,  grCol );									
							((SpinnerNumberModel)sp2.getModel()).setMaximum( nG );

							int cellValue =  ( Integer )t.getValueAt( i, grCol ); 

							if( cellValue > nG )
							{										
								t.setValueAt( nG, i, grCol );
								sp2.setValue( nG );
							}
						}
					}

				}
			});

			int nG = (Integer)ConfigApp.getProperty( ConfigApp.AFFECTIVE_GROUPS );

			this.NumGroups.setValue( nG );
		}

		return this.NumGroups;
	}

	private JSpinner getJSpinnerNumBlocks()
	{
		if (this.NumBlocks == null)
		{
			final String ID = ConfigApp.AFFECTIVE_BLOCKS;

			this.NumBlocks = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
			Dimension d = this.NumBlocks.getPreferredSize();
			d.width = 60;
			this.NumBlocks.setPreferredSize(d);

			this.NumBlocks.setEditor(new JSpinner.NumberEditor(this.NumBlocks, "#"));
			this.NumBlocks.addMouseWheelListener(new MouseWheelListener()
			{

				public void mouseWheelMoved(MouseWheelEvent e)
				{
					if (e.getScrollType() == 0)
					{
						try
						{
							JSpinner sp = (JSpinner)e.getSource();

							int d = e.getWheelRotation();

							if (d > 0)
							{
								sp.setValue(sp.getModel().getPreviousValue());
							}
							else
							{
								sp.setValue(sp.getModel().getNextValue());
							}


						}
						catch (IllegalArgumentException localIllegalArgumentException) {}
					}

				}
			});

			this.NumBlocks.addChangeListener(new ChangeListener()
			{

				public void stateChanged(ChangeEvent e)
				{
					int nB = (Integer)NumBlocks.getValue();

					ConfigApp.setProperty( ID, nB );

					List<JTable> ts = new ArrayList< JTable >();

					ts.add(getSlideTable());
					ts.add(getSoundTable());

					for( JTable t : ts )
					{
						int blCol = t.getColumnCount() - 1;

						for( int i = 0; i < t.getRowCount(); i++ )
						{		
							SpinnerNumberCellEditor cell = (SpinnerNumberCellEditor)t.getCellEditor( i, blCol );
							cell.stopCellEditing();
							JSpinner sp2 = (JSpinner)cell.getTableCellEditorComponent( t, cell.getCellEditorValue(), true, i,  blCol );									
							((SpinnerNumberModel)sp2.getModel()).setMaximum( nB );

							int cellValue =  ( Integer )t.getValueAt( i, blCol ); 

							if( cellValue > nB )
							{										
								t.setValueAt( nB, i, blCol );
								sp2.setValue( nB );
							}
						}
					}
				}
			});

			int nB = (Integer)ConfigApp.getProperty( ConfigApp.AFFECTIVE_BLOCKS );

			this.NumBlocks.setValue( nB );
		}

		return this.NumBlocks;
	}

	private JTable getSlideTable()
	{
		if (this.jTableImgs == null)
		{
			final String propertyID = ConfigApp.PATH_SLIDES;

			this.jTableImgs = getAffectiveTable(null);

			this.jTableImgs.setRowSelectionAllowed(true);
			this.jTableImgs.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

			this.jTableImgs.getModel().addTableModelListener(new TableModelListener()
			{
				public void tableChanged(TableModelEvent e)
				{
					updateListProperty( jTableImgs, propertyID, true);

					getJLabelNumberSlideImgs().setText("No. Imgs: " + jTableImgs.getRowCount());
				}

			});

			this.jTableImgs.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				private int selectedIndex = -1;

				public void valueChanged(ListSelectionEvent arg0)
				{
					DefaultListSelectionModel lst = (DefaultListSelectionModel)arg0.getSource();
					if (!lst.isSelectionEmpty())
					{

						if ((jTableImgs.getSelectedRow() != this.selectedIndex) || 
								(this.selectedIndex == -1))
						{
							this.selectedIndex = jTableImgs.getSelectedRow();
							showSelectedEmotionalElement( jTableImgs, getJPanelSlideShowImgs(), true );
						}
					}
					else
					{
						this.selectedIndex = -1;
						jTableImgs.setVisible(false);
						jTableImgs.removeAll();
						jTableImgs.setVisible(true);
					}

				}
			});

			this.jTableImgs.addFocusListener(new FocusAdapter()
			{
				private int oldIndex = -1;

				public void focusGained(FocusEvent arg0)
				{
					int newIndex = jTableImgs.getSelectedRow();
					if ((newIndex > -1) && (newIndex == this.oldIndex))
					{
						showSelectedEmotionalElement( jTableImgs, getJPanelSlideShowImgs(), true );
					}
				}

				public void focusLost(FocusEvent arg0)
				{
					this.oldIndex = jTableImgs.getSelectedRow();
				}
			});

			loadListElemets( (List< AffectiveObject >)ConfigApp.getProperty( propertyID ), jTableImgs) ;
		}

		return this.jTableImgs;
	}

	private JTable getSoundTable()
	{
		if (this.jTableSounds == null)
		{
			final String propertyID = ConfigApp.PATH_SOUNDS;

			this.jTableSounds = getAffectiveTable(null);

			this.jTableSounds.setRowSelectionAllowed(true);
			this.jTableSounds.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

			this.jTableSounds.getModel().addTableModelListener(new TableModelListener()
			{

				public void tableChanged(TableModelEvent e)
				{
					updateListProperty(jTableSounds, "PATH_SOUNDS", false);

					getJLabelNumberSounds().setText("No. clips: " + jTableSounds.getRowCount());
				}

			});
			
			this.jTableSounds.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				private Object oldSelectedItem = null;


				public void valueChanged(ListSelectionEvent arg0)
				{
					JPanel canvas = getJPanelSlideShowImgs();

					if (jTableSounds.getSelectedRow() > -1)
					{
						Object newSelectedItem = jTableSounds.getValueAt( jTableSounds.getSelectedRow(), 4 );
						if ((this.oldSelectedItem == null) || (!this.oldSelectedItem.equals( newSelectedItem ) ) )
						{
							this.oldSelectedItem = newSelectedItem;
							soundLoadError = false;
							showSelectedEmotionalElement( jTableSounds, canvas, false);
						}
					}
					else
					{
						canvas.setVisible(false);
						canvas.removeAll();
						canvas.setVisible(true);
					}

				}
			});

			this.jTableSounds.addFocusListener(new FocusAdapter()
			{
				private int oldIndex = -1;

				public void focusGained(FocusEvent arg0) 
				{
					int newIndex = jTableSounds.getSelectedRow();
					if ((newIndex > -1) && (newIndex == this.oldIndex))
					{
						showSelectedEmotionalElement( jTableSounds, getJPanelSlideShowImgs(), false );
					}
				}

				public void focusLost(FocusEvent arg0)
				{
					this.oldIndex = jTableSounds.getSelectedRow();
				}

			});

			loadListElemets( (List< AffectiveObject >)ConfigApp.getProperty( propertyID ), jTableSounds) ;
		}

		return this.jTableSounds;
	}

	private JTable getAffectiveTable(Object[][] t)
	{
		JTable table = getCreateJTable();
		table.putClientProperty("terminateEditOnFocusLost", Boolean.valueOf(true));

		FontMetrics fm = table.getFontMetrics( table.getFont() );
		
		table.setBackground(Color.WHITE);
		table.setRowSelectionAllowed(true);

		table.setModel( this.createAffectiveTableModel( t ) );

		int iCol = 0;
		String colHeader = table.getColumnModel().getColumn( iCol ).getHeaderValue().toString();
		int s = fm.stringWidth( " " + colHeader + " " );		
		if( colHeader.isEmpty() )
		{
			s = fm.stringWidth( " 9999" );
		}		
		table.getColumnModel().getColumn( iCol ).setResizable( false );
		table.getColumnModel().getColumn( iCol ).setPreferredWidth( s );
		table.getColumnModel().getColumn( iCol ).setMaxWidth( s );
		table.getColumnModel().getColumn( iCol ).setMinWidth( s );

		iCol = 1;
		colHeader = table.getColumnModel().getColumn( iCol ).getHeaderValue().toString();
		s = fm.stringWidth( " " + colHeader + " " );
		table.getColumnModel().getColumn( iCol ).setPreferredWidth( s );
		table.getColumnModel().getColumn( iCol ).setMaxWidth( s );
		table.getColumnModel().getColumn( iCol ).setMinWidth( s );

		iCol = 2; 
		colHeader = table.getColumnModel().getColumn( iCol ).getHeaderValue().toString();
		s = fm.stringWidth( " " + colHeader + " " );
		table.getColumnModel().getColumn( iCol ).setPreferredWidth( s );
		table.getColumnModel().getColumn( iCol ).setMaxWidth( s );
		table.getColumnModel().getColumn( iCol ).setMinWidth( s );

		iCol = 3; 
		colHeader = table.getColumnModel().getColumn( iCol ).getHeaderValue().toString();
		s = fm.stringWidth( " " + colHeader + " " );
		table.getColumnModel().getColumn( iCol ).setPreferredWidth( s );
		table.getColumnModel().getColumn( iCol ).setMaxWidth( s );
		table.getColumnModel().getColumn( iCol ).setMinWidth( s );

		table.getColumnModel().getColumn(4).setPreferredWidth(125);

		iCol = 5; 
		colHeader = table.getColumnModel().getColumn( iCol ).getHeaderValue().toString();
		s = fm.stringWidth( " " + colHeader + " " );
		table.getColumnModel().getColumn( iCol ).setPreferredWidth( s );
		table.getColumnModel().getColumn( iCol ).setMaxWidth( s );
		table.getColumnModel().getColumn( iCol ).setMinWidth( s );

		iCol = 6; 
		colHeader = table.getColumnModel().getColumn( iCol ).getHeaderValue().toString();
		s = fm.stringWidth( " " + colHeader + " " );
		table.getColumnModel().getColumn( iCol ).setPreferredWidth( s );
		table.getColumnModel().getColumn( iCol ).setMaxWidth( s );
		table.getColumnModel().getColumn( iCol ).setMinWidth( s );

		// Groups

		TableColumn columnType = table.getColumnModel().getColumn(5);
		JSpinner group = new JSpinner(new SpinnerNumberModel( 1, 1, ((SpinnerNumberModel)getJSpinnerNumGroups().getModel()).getMaximum(), 1 ) );
		group.setEditor(new JSpinner.NumberEditor(group, "#"));
		group.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
				{
					try
					{
						JSpinner sp = (JSpinner)e.getSource();

						int d = e.getWheelRotation();

						if (d > 0)
						{
							sp.setValue(sp.getModel().getPreviousValue());
						}
						else
						{
							sp.setValue(sp.getModel().getNextValue());
						}
					}
					catch (IllegalArgumentException localIllegalArgumentException) 
					{}
				}        
			}
		});

		columnType.setCellEditor(new SpinnerNumberCellEditor(group));

		// Blocks

		columnType = table.getColumnModel().getColumn(6);
		group = new JSpinner( new SpinnerNumberModel( 1, 1, ((SpinnerNumberModel)this.getJSpinnerNumBlocks().getModel()).getMaximum(), 1) );		
		group.setEditor(new JSpinner.NumberEditor(group, "#"));
		group.addMouseWheelListener(new MouseWheelListener()
		{

			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
				{
					try
					{
						JSpinner sp = (JSpinner)e.getSource();

						int d = e.getWheelRotation();

						if (d > 0)
						{
							sp.setValue(sp.getModel().getPreviousValue());
						}
						else
						{
							sp.setValue(sp.getModel().getNextValue());
						}


					}
					catch (IllegalArgumentException localIllegalArgumentException) {}
				}

			}
		});

		columnType.setCellEditor(new SpinnerNumberCellEditor(group));

		table.addMouseWheelListener(new MouseWheelListener()
		{

			public void mouseWheelMoved(MouseWheelEvent evt)
			{
				JTable t = (JTable)evt.getSource();
				int d = evt.getWheelRotation();

				if( evt.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
				{
					int row = t.rowAtPoint( evt.getPoint() );
					int col = t.columnAtPoint( evt.getPoint() );

					if (row >= 0 && col >= t.getColumnCount() - 2 ) 
					{
						Integer value = (Integer)t.getValueAt( row, col );

						try
						{	
							SpinnerNumberCellEditor cellSp = (SpinnerNumberCellEditor)t.getCellEditor( row, col );		
							cellSp.stopCellEditing();

							JSpinner sp = (JSpinner)cellSp.getTableCellEditorComponent( t, cellSp.getCellEditorValue(), false, row, col );
							SpinnerNumberModel spM = (SpinnerNumberModel)sp.getModel();

							value -= d;

							if( value < (Integer)spM.getMinimum() )
							{
								value = (Integer)spM.getMinimum();
							}
							else if( value > (Integer)spM.getMaximum() )
							{
								value = (Integer)spM.getMaximum();
							}
						}
						catch( IllegalArgumentException ex )
						{
						}
						finally 
						{
							t.setValueAt( value, row, col );
						}
					}
					else
					{
						Component jv = t.getParent();
						if( jv != null )
						{
							Object js = jv.getParent();

							if( js != null && ( js instanceof JScrollPane ) )
							{				    			
								int step = ((JScrollPane)js).getVerticalScrollBar().getVisibleAmount() / 2;

								if( d < 0 )
								{
									step *= -1;
								}

								int pos = ((JScrollPane)js).getVerticalScrollBar().getValue() + step;
								/*
			    			if( pos < 0 )
			    			{
			    				pos = 0;
			    			}
								 */

								((JScrollPane)js).getVerticalScrollBar().setValue( pos );
							}
						}
					}
				}
			}
		});

		final JTable auxTable = table;
		table.getTableHeader().addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				int col = auxTable.columnAtPoint(e.getPoint());

				if (col == 0)
				{
					auxTable.selectAll();
				}

			}
		});

		return table;
	}

	private JTable getCreateJTable()
	{
		JTable t = new JTable()
		{
			private static final long serialVersionUID = 1L;


			public String getToolTipText(MouseEvent e)
			{
				String tip = null;
				Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);

				try
				{
					tip = getValueAt(rowIndex, colIndex).toString();
				}
				catch (RuntimeException localRuntimeException) 
				{}

				return tip;
			}      
		};

		t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (!table.isCellEditable(row, column))
				{
					cellComponent.setBackground(new Color(255, 255, 224));
				}
				else
				{
					cellComponent.setBackground(Color.WHITE);
				}

				return cellComponent;
			}

		});

		t.getTableHeader().setReorderingAllowed(false);

		return t;
	}

	private TableModel createAffectiveTableModel(Object[][] table)
	{
		DefaultTableModel model = new DefaultTableModel(table, new String[] { "", "Fix", "S.A.M.", "Beep", "Path file", "Group", "Block" })
		{
			boolean[] editableCols = { false, true, true, true, false, true, true };

			Class[] columnTypes = { Integer.class, Boolean.class, Boolean.class, Boolean.class, String.class, Integer.class, 
					Integer.class };

			public Class getColumnClass(int columnIndex)
			{
				return this.columnTypes[columnIndex];
			}


			public boolean isCellEditable(int row, int column)
			{
				boolean edit = (column > -1) && (column < this.editableCols.length);

				if (edit)
				{
					edit = this.editableCols[column];
				}

				return edit;
			}
		};

		return model;
	}

	private void updateAffectiveTable(JTable table, String path, int grp, int block, boolean fix, boolean sam, boolean beep, boolean add)
	{
		DefaultTableModel m = (DefaultTableModel)table.getModel();

		if (add)
		{
			int num = table.getRowCount() + 1;

			Object[] rt = new Object[]{ num, fix, sam, beep, path, grp, block };

			m.addRow(rt);
		}
		else
		{
			int[] rs = table.getSelectedRows();
			Arrays.sort(rs);

			for (int i = rs.length - 1; i >= 0; i--)
			{
				m.removeRow(rs[i]);
			}
		}
	}

	private JList<String> getJComboboxRandomSampleTypes()
	{
		if (this.jListRandomOrder == null)
		{
			final String ID = ConfigApp.RANDOM_AFFECTIVE_SAMPLES_TYPE;

			String[] val = opEmotionalTask.RANDOM_SORT_TEXT_TYPE;

			this.jListRandomOrder = new JList< String >(val);

			this.jListRandomOrder.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

			int sel = (Integer)ConfigApp.getProperty( ConfigApp.RANDOM_AFFECTIVE_SAMPLES_TYPE );

			if ((sel > val.length) || (sel < 0))
			{
				sel = 0;
			}

			this.jListRandomOrder.setSelectedIndex(sel);

			this.jListRandomOrder.addListSelectionListener(new ListSelectionListener()
			{

				public void valueChanged(ListSelectionEvent e)
				{
					JList<String> c = (JList< String >)e.getSource();
					ConfigApp.setProperty( ID,  c.getSelectedIndex() );
				}
			});
		}

		return this.jListRandomOrder;
	}

	private JCheckBox getJChkbxPreserverSlideSoundCorrespondence()
	{
		if( this.jChkbxPreserverSlideSoundCorrespondence == null )
		{
			final String ID = ConfigApp.IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE;

			this.jChkbxPreserverSlideSoundCorrespondence = new JCheckBox( "Preserve slide-sound link");

			this.jChkbxPreserverSlideSoundCorrespondence.setSelected( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECTED_PRESERVER_SLIDE_SOUND_CORRESPONDENCE  ) );

			this.jChkbxPreserverSlideSoundCorrespondence.addItemListener(new ItemListener() 
			{
				public void itemStateChanged(ItemEvent arg0) 
				{
					JCheckBox c = (JCheckBox)arg0.getSource();

					ConfigApp.setProperty( ID, c.isSelected() );
				}
			});
		}

		return this.jChkbxPreserverSlideSoundCorrespondence ;
	}

	private JCheckBox getJChkbxSlideMainGroup()
	{
		if( this.jChkbxSlideMainGroup == null )
		{
			final String ID = ConfigApp.IS_SELECTED_SLIDE_MAIN_GROUP;

			this.jChkbxSlideMainGroup = new JCheckBox( "Use slide's groups and blocks as main");
			this.jChkbxSlideMainGroup.setToolTipText( "The slide's groups and blocks are used to sort randomly if it is preserved slide-sound link.");

			this.jChkbxSlideMainGroup.setSelected( (Boolean)ConfigApp.getProperty( ConfigApp.IS_SELECTED_SLIDE_MAIN_GROUP  ) );

			this.jChkbxSlideMainGroup.addItemListener(new ItemListener() 
			{
				public void itemStateChanged(ItemEvent arg0) 
				{
					JCheckBox c = (JCheckBox)arg0.getSource();

					ConfigApp.setProperty( ID, c.isSelected() );
				}
			});
		}

		return this.jChkbxSlideMainGroup ;
	}

	private JMenuItem getJButtonInfo()
	{
		if (this.jBtnInfo == null)
		{
			Dimension d = new Dimension(16, 16);

			this.jBtnInfo = new JMenuItem();

			this.jBtnInfo.setText("Help");
			this.jBtnInfo.setIcon(new ImageIcon(imagenPoligono2D.crearImagenTexto(0, 0, "?", 
					this.jBtnInfo.getFontMetrics(this.jBtnInfo.getFont()), 
					Color.BLACK, Color.CYAN.darker(), null)));


			this.jBtnInfo.setPreferredSize(d);

			this.jBtnInfo.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					JMenuItem b = (JMenuItem)e.getSource();
					JMenu m = (JMenu)((JPopupMenu)b.getParent()).getInvoker();
					m.doClick();

					JDialog w = getInfoWindow();

					w.setSize(350, 310);
					Point pos = b.getParent().getLocationOnScreen();

					Point loc = new Point(pos.x + b.getSize().width, pos.y);

					w.setLocation(loc);

					w.setVisible(true);
				}
			});
		}

		return this.jBtnInfo;
	}

	private JDialog getInfoWindow()
	{
		if (this.winInfo == null)
		{
			this.winInfo = new JDialog(this);
			this.winInfo.setUndecorated(true);
			this.winInfo.setLayout(new BorderLayout());
			this.winInfo.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

			this.winInfo.add(new JScrollPane(getTextAreaInfo()));

			this.winInfo.addWindowFocusListener(new WindowFocusListener()
			{

				public void windowLostFocus(WindowEvent e)
				{
					((JDialog)e.getSource()).dispose();
				}

				public void windowGainedFocus(WindowEvent e) 
				{}

			});
		}    

		return this.winInfo;
	}

	private JTextPane getTextAreaInfo()
	{
		if (this.textAreaInfo == null)
		{
			this.textAreaInfo = new JTextPane();
			this.textAreaInfo.setEnabled(true);
			this.textAreaInfo.setEditable(false);

			this.textAreaInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

			this.randomSortOrderLegends(this.textAreaInfo);
		}

		return this.textAreaInfo;
	}

	private void randomSortOrderLegends(JTextPane jPanelCommandLegends)
	{
		Map<String, String> legends = new HashMap<String, String>();

		String[] type = opEmotionalTask.RANDOM_SORT_TEXT_TYPE;
		String[] descr = opEmotionalTask.RANDOM_SORT_BEHAVOIR_DESCRIPTOR;

		for (int i = 0; (i < type.length) && (i < descr.length); i++)
		{
			legends.put(type[i], descr[i]);
		}

		SimpleAttributeSet at = new SimpleAttributeSet();
		StyleConstants.setFontFamily(at, jPanelCommandLegends.getFont().getFamily());
		StyleConstants.setUnderline(at, true);

		String text = "";

		for (String cm : legends.keySet())
		{
			String lg = (String)legends.get(cm);
			text = text + cm.toUpperCase() + ": ";
			text = text + lg + "\n";
		}

		FontMetrics fm = jPanelCommandLegends.getFontMetrics(jPanelCommandLegends.getFont());

		MutableAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setSpaceBelow( set, fm.getHeight() * 0.5F);

		jPanelCommandLegends.setParagraphAttributes( set, false);

		jPanelCommandLegends.setText(text.substring(0, text.length() - 2));
	}
}