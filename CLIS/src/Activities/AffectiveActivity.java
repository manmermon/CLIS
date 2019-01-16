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
package Activities;

import Activities.Operaciones.opEmotionalTask;
import Auxiliar.AffectiveObject;
import Auxiliar.EmotionParameter;
import Auxiliar.Tasks.INotificationTask;
import Auxiliar.Tasks.ITaskMonitor;
import Auxiliar.Tasks.beepPlayTask;
import Config.ConfigApp;
import Auxiliar.WarningMessage;
import Controls.eventInfo;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.NoneSelectedButtonGroup;
import GUI.MyComponents.clipSoundThread;
import GUI.MyComponents.imagenPoligono2D;
import StoppableThread.IStoppableThread;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

public class AffectiveActivity extends Activity implements ITaskMonitor
{
	private Iterator<AffectiveObject> itImgs = null;
	private Iterator<AffectiveObject> itsound = null;

	private clipSoundThread clip = null;
	private clipSoundThread nextClip = null;

	private AffectiveObject currentImgAffObj = null;
	private AffectiveObject currentoSoundAffObj = null;

	private BufferedImage img = null;
	private BufferedImage nextImg = null;

	private JPanel samPanel = null;

	private JToggleButton[] samValues = null;
	private JButton sendedButton = null;
	private String[] headerSAM = null;
	private Font fQuestions = new Font( Font.DIALOG, Font.BOLD, 22);

	//private String[] basicEmotions = new String[]{ "Sad", "Surprise", "Anger", "Disgusted", "Fear", "Happy", "Neutral" };

	private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, false);

	private int samSetSize = 2;
	private int countSetSize = 0;
	
	private int samValenceRange = 9;
	private int samArousalRange = 9;	
	private int samDominanceRange = 9;
	private List< EmotionParameter > samEmotionSet;

	private String reportAffectiveInfo = "";

	//private boolean beepOn = false;	
	private beepPlayTask beepThread;

	private boolean samPhase = false;

	private boolean samDominance = false;
	
	private boolean samBeep = false;

	public static final String ID = "Affective";
	
	public static void registerActivity()
	{
		activityID.setID( ID );
		activityID.setActivityClass(AffectiveActivity.class);
		ActivityRegister.registerActivity(activityID);
	}
	
	private AffectiveActivity()
	{
		super();	  
		super.setName( this.getClass().getCanonicalName());

		//super.currentPhase = 0;		
		this.setPanelTask();

		this.info = new ActivityInfo(3, 2, 3, false, false);

		this.samValues = new JToggleButton[3];
		this.headerSAM = new String[] { "How pleasant are you feeling now?", 
										"How excited are you feeling now?", 
										"How are your feeling?" };
		
		this.reportHeader = "";
		this.reportAnswer = "- - -";
	}

	public static Activity getInstance()
	{
		if (task == null)
		{
			task = new AffectiveActivity();
		}

		return task;
	}

	@Override
	protected void setPanelTask()
	{
		if (this.panelTask == null)
		{
			this.panelTask = new JPanel()
			{
				private static final long serialVersionUID = 1L;

				protected void paintComponent( Graphics g )
				{
					super.paintComponent( g );

					Dimension size = AffectiveActivity.this.panelTask.getSize();

					if ((AffectiveActivity.this.img != null) && 
							(AffectiveActivity.this.isMainPhase()))
					{
						int imgW = AffectiveActivity.this.img.getWidth();
						int imgH = AffectiveActivity.this.img.getHeight();

						if ((imgW != size.width) || (imgH != size.height))
						{
							Image imagen = AffectiveActivity.this.img.getScaledInstance(size.width, 
									size.height, 
									2);


							AffectiveActivity.this.img = new BufferedImage(size.width, 
									size.height, 
									AffectiveActivity.this.img.getType());

							Graphics2D gr = AffectiveActivity.this.img.createGraphics();
							gr.drawImage(imagen, 0, 0, null);
							gr.dispose();
						}

						g.drawImage( AffectiveActivity.this.img, 0, 0, null );
					}
					else
					{
						FontMetrics fm = AffectiveActivity.this.panelTask.getFontMetrics(AffectiveActivity.this.panelTask.getFont());
						Image cross = imagenPoligono2D.crearImagenTexto(0, 0, "+", fm, Color.GRAY, Color.GRAY, null);

						g.drawImage( cross, (AffectiveActivity.this.panelTask.getWidth() - cross.getWidth(null)) / 2, 
										(AffectiveActivity.this.panelTask.getHeight() - cross.getHeight(null)) / 2, 
										null);
					}

					g.dispose();
					getToolkit().sync();
					
					super.revalidate();
				}

			};
			
			this.panelTask.addComponentListener(new ComponentAdapter()
			{
				Timer controlTimer = null;
				int DELAY = 100;

				@Override
				public void componentResized(ComponentEvent e)
				{
					final JPanel panel = (JPanel)e.getSource();

					if (this.controlTimer == null)
					{
						this.controlTimer = new Timer(this.DELAY, new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								if (e.getSource() == controlTimer)
								{
									controlTimer.stop();
									controlTimer = null;

									Dimension size = panel.getSize();

									AffectiveActivity.this.parameters.addExtraParameter( Activity.ID_ACTIVITY_PANEL_SIZE, size);

									if ((size.width > 0) && (size.height > 0))
									{
										if (AffectiveActivity.this.img != null)
										{
											Image imagen = AffectiveActivity.this.img.getScaledInstance(size.width, 
													size.height, 
													Image.SCALE_FAST);

											AffectiveActivity.this.img = new BufferedImage(size.width, 
													size.height, 
													img.getType());

											Graphics2D gr = img.createGraphics();
											gr.drawImage(imagen, 0, 0, null);
											gr.dispose();
										}

										if ( nextImg != null)
										{
											Image imagen = nextImg.getScaledInstance(size.width, 
													size.height, 
													Image.SCALE_FAST);

											nextImg = new BufferedImage(size.width, 
													size.height, 
													img.getType());


											Graphics2D gr = nextImg.createGraphics();
											gr.drawImage(imagen, 0, 0, null);
											gr.dispose();
										}
									}
									panel.repaint();
								}
							}
						});
						this.controlTimer.start();
					}
					else
					{
						this.controlTimer.restart();
					}

					FontMetrics fm = null;
					String txt = "+";
					int div = 2;
					do
					{
						fm = panel.getFontMetrics(new Font( Font.DIALOG, Font.PLAIN, panel.getHeight() / div));
						div += 2;
					}
					while ((fm.stringWidth(txt) > 0) && (fm.stringWidth(txt) >= panel.getSize().width - panel.getInsets().left - panel.getInsets().right));

					panel.setFont(fm.getFont());
				}

			});
			
			this.panelTask.setLayout( new BorderLayout() );
			this.panelTask.setDoubleBuffered( true);
			this.panelTask.setLayout( new BorderLayout() );
			
			this.panelTask.setBackground( Color.BLACK );
			this.panelTask.setForeground(Color.BLACK );
			
			this.panelTask.setVisible( true );
		}
	}

	@Override
	public void addParameter(short parameterID, Object parameter)
			throws Exception
	{
		super.addParameter(parameterID, parameter);

		opEmotionalTask.getInstance().clear();

		List<AffectiveObject> files = (List< AffectiveObject >)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SLIDES );
		if (files != null)
		{
			opEmotionalTask.getInstance().addPathImages(files);

			this.reportHeader = ("PictureFile PictureGroup PictureBlock " + this.reportHeader);
		}

		files = (List< AffectiveObject >)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SOUND_CLIPS );
		if (files != null)
		{
			opEmotionalTask.getInstance().addSounds(files);

			this.reportHeader = ("SoundFile SoundGroup SoundBlock " + this.reportHeader);
		}

		opEmotionalTask.getInstance().setOrderPresentation((Integer)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_RANDOM_SAMPLES ), 
				(Boolean)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_PRESERVE_SLIDE_SOUND_ORDER ), 
				(Boolean)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SLIDE_GROUP_MAIN ) );

		opEmotionalTask.getInstance().applyOrder();

		this.samDominance = (Boolean)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_DOMINANCE );


		this.samValenceRange = (Integer)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_VALENCE_RANGE );
		this.samArousalRange = (Integer)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_AROUSAL_RANGE );
		this.samDominanceRange = (Integer)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_DOMINANCE_RANGE );
		
		this.reportHeader += " Valence(" + this.samValenceRange + ") Arousal(" + this.samArousalRange + ") ";
		
		
		if (this.samDominance)
		{
			this.reportHeader += "Dominance(" + this.samDominanceRange + ")";
			this.headerSAM[(this.headerSAM.length - 1)] = "How submissiveness level  are you feeling now?";
		}
		else
		{
			this.reportHeader += "Emotion";
		}

		this.samSetSize = (Integer)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_SET_SIZE );

		if (this.samSetSize < 1)
		{
			this.samSetSize = 1;
		}
		this.countSetSize = this.samSetSize;
									
		this.samEmotionSet = (List< EmotionParameter >)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_EMOTION_SET );
		
		this.samBeep = (Boolean)super.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SAM_BEEP );
	}

	@Override
	protected void preStart()
			throws Exception
	{
		super.preStart();

		this.beepThread = new beepPlayTask();
		this.beepThread.startThread();		
	}

	@Override
	protected void makeSpecificTask(int dificultad) throws Exception
	{
		this.reportActivity = "";

		this.reportAffectiveInfo = "";

		this.panelTask.repaint();

		this.samPhase = false;
		this.isShowActivityPhase = true;

		this.img = this.nextImg;

		if (this.img == null)
		{
			this.img = getNextImage();
		}

		if( this.clip != null )
		{
			this.clip.stopThread( IStoppableThread.ForcedStop );
		}
		
		this.clip = this.nextClip;
		if (this.clip == null)
		{
			this.clip = getNextClip();
		}

		this.fillReportAffectiveInfoFile();

		boolean beepOn = false;
		if (this.currentImgAffObj != null)
		{
			beepOn = (beepOn) || (this.currentImgAffObj.isBeepActive());
		}

		if (this.currentoSoundAffObj != null)
		{
			beepOn = (beepOn) || (this.currentoSoundAffObj.isBeepActive());
		}

		if (beepOn)
		{
			this.beepThread.startTask();
		}

		if (checkCountSam())
		{
			this.countSetSize -= 1;
		}
	}

	protected void postMakeActivity(int difficutly) throws Exception
	{
		super.postMakeActivity(difficutly);

		this.nextImg = getNextImage();
		this.nextClip = getNextClip();
	}

	private boolean checkCountSam()
	{
		boolean count = false;

		if ((this.currentImgAffObj == null) && (this.currentoSoundAffObj == null))
		{
			count = true;
		}
		else
		{
			if (this.currentImgAffObj != null)
			{
				count = (count) || (this.currentImgAffObj.isSAM());
			}


			if (this.currentoSoundAffObj != null)
			{
				count = (count) || (this.currentoSoundAffObj.isSAM());
			}
		}

		return count;
	}

	private clipSoundThread getNextClip() throws Exception
	{
		if ((this.itsound == null) || (!this.itsound.hasNext()))
		{
			opEmotionalTask.getInstance().resetIteratorSounds();
			this.itsound = opEmotionalTask.getInstance().getSounds();
		}

		clipSoundThread clip = null;
		if (this.itsound.hasNext())
		{
			this.currentoSoundAffObj = this.itsound.next();

			clip = new clipSoundThread();
			clip.addParameter( clipSoundThread.ID_SOUND_PARAMETER, this.currentoSoundAffObj.getPathFile());
			clip.taskMonitor(this);
			clip.createTask();
		}

		return clip;
	}

	private BufferedImage getNextImage() throws Exception
	{
		if ((this.itImgs == null) || (!this.itImgs.hasNext()))
		{
			opEmotionalTask.getInstance().resetIteratorImgs();
			this.itImgs = opEmotionalTask.getInstance().getImages();
		}

		BufferedImage image = null;
		if (this.itImgs.hasNext())
		{
			this.currentImgAffObj = this.itImgs.next();

			Dimension d1 = (Dimension)this.parameters.getExtraParamters().get( Activity.ID_ACTIVITY_PANEL_SIZE );
			if (d1 == null)
			{
				d1 = Toolkit.getDefaultToolkit().getScreenSize();
			}

			image = new BufferedImage(d1.width, d1.height, BufferedImage.TYPE_INT_RGB );
			image.getGraphics().drawImage(ImageIO.read(new File(this.currentImgAffObj.getPathFile())).getScaledInstance(d1.width, d1.height, Image.SCALE_FAST ), 0, 0, null);
		}

		return image;
	}

	@Override
	public void stopActivity()
	{
		for (int i = 0; i < this.samValues.length; i++)
		{
			if (this.samValues[i] != null)
			{
				this.samValues[i].setSelected(false);
				this.samValues[i] = null;
			}
		}

		getSendedButton().setEnabled( false );
		
		this.reportAnswer = "- - -";
		this.reportAffectiveInfo = "";

		this.panelTask.removeAll();
		this.currentPhase = -1;
	}

	@Override
	protected void preStopThread(int friendliness) throws Exception
	{
		super.preStopThread(friendliness);

		super.stopActivity();

		this.reset();
	}

	public void reset()
	{
		opEmotionalTask.getInstance().resetIterators();

		if (this.clip != null)
		{
			this.clip.stopThread( IStoppableThread.ForcedStop );
			this.clip = null;
		}

		if (this.nextClip != null)
		{
			this.nextClip.stopThread( IStoppableThread.ForcedStop );
			this.nextClip = null;
		}

		this.img = null;
		this.nextImg = null;

		this.itImgs = null;
		this.itsound = null;
		this.currentPhase = 0;

		System.gc();
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.Activities.Activity#activityEnded()
	 */
	@Override
	protected void activityEnded()
	{
		this.isEndedTask = true;
	}

	@Override
	protected void preSuspendActivity()
	{
		if ((this.clip != null) && 
				(this.clip.isPlay()))
		{
			this.clip.stopSound();
		}
	}

	@Override
	public void suspendActivity()
	{
		preSuspendActivity();
		this.activitySupend = true;
	}

	@Override
	protected void nextState() throws Exception
	{
		if ( super.isAnswerPhase() )
		{
			Point loc = this.panelTask.getLocationOnScreen();
			this.panelTask.setVisible(false);
			this.panelTask.removeAll();

			if (this.countSetSize <= 0)
			{
				this.samPhase = true;
				this.isShowActivityPhase = true;
				this.countSetSize = this.samSetSize;

				try
				{
					Robot r = new Robot();
					Dimension d = this.panelTask.getSize();

					r.mouseMove(loc.x + d.width / 2, loc.y + d.height / 2);
				}
				catch (Exception ex) 
				{        	
				}

				
				this.getSAMPanel().setVisible( false );
				
				this.panelTask.add( this.getSAMPanel() );
				
				this.getSAMPanel().setVisible( true );

				this.panelTask.setVisible( true);
				
				if( this.samBeep )
				{
					this.beepThread.startTask();
				}
			}
			else
			{
				this.samPhase = false;
				this.isShowActivityPhase = false;

				this.activityEnded();
				this.notifiedAnswer( true );
			}
		}
		else if ( this.currentPhase > 0 )
		{
			if ((this.clip != null) && 
					(!this.clip.isStart()))
			{
				this.clip.startTask();
			}

			super.panelTask.repaint();
		}
	}
	
	public void startTask()
			throws Exception
	{
		this.nextState();
		super.startTask();
	}

	protected void notifiedActivityEnd()
	{
		if (this.samPhase)
		{
			try
			{
				Robot r = new Robot();
				Dimension d = this.panelTask.getSize();

				r.mouseMove(d.width * 2, d.height * 2);
			}
			catch (Exception localException) {}
		}



		super.notifiedActivityEnd();
	}


	protected boolean selectNextEnablePhase()
	{
		boolean ena = super.selectNextEnablePhase();

		if (this.currentPhase == 2)
		{
			ena = (ena) && (this.countSetSize <= 0);
		}

		this.isShowActivityPhase = ena;

		return ena;
	}

	private void fillReportAffectiveInfoFile()
	{
		if (this.currentImgAffObj != null)
		{
			this.reportAffectiveInfo =     
					(this.currentImgAffObj.getPathFile() + " " + this.currentImgAffObj.getGroup() + " " + this.currentImgAffObj.getBlock() + " " + this.reportAffectiveInfo);
		}

		if (this.currentoSoundAffObj != null)
		{
			this.reportAffectiveInfo = 

					(this.currentoSoundAffObj.getPathFile() + " " + this.currentoSoundAffObj.getGroup() + " " + this.currentoSoundAffObj.getBlock() + " " + this.reportAffectiveInfo);
		}
	}


	protected void nextPhase()
	{
		super.nextPhase();

		this.isShowNextActivityPhase = true;

		int nextPhase = this.currentPhase + 1;
		if ((nextPhase == 2) && 
				(this.parameters.getEnablePhases()[nextPhase] ))
		{
			this.isShowNextActivityPhase = (this.countSetSize < 1);
		}
	}

	@Override
	public void taskDone(INotificationTask task)
			throws Exception
	{
		List<eventInfo> events = task.getResult();

		boolean end = false;
		for (eventInfo event : events)
		{
			end = (end) || ((event.getEventType().equals( clipSoundThread.EVENT_SOUND_END)) && (Boolean)this.parameters.getExtraParamters().get( ID_PARAMETER_WAIT_SOUND_END ) );
		}

		if (end)
		{
			System.gc();

			super.notifiedAnswer(true);
		}
	}

	private JPanel getSAMPanel()
	{
		if (this.samPanel == null)
		{
			this.samPanel = new JPanel();
			this.samPanel.setLayout(new GridLayout(this.samValues.length + 1, 1));

			for (int i = 0; i < this.samValues.length; i++)
			{
				int size = this.samValenceRange;
				
				switch ( i ) 
				{
					case 1:
						size = this.samArousalRange;
						break;
	
					case 2:
						size = this.samDominanceRange;
						break;
				}
				

				if ((i > 1) && (!this.samDominance))
				{
					size = this.samEmotionSet.size();
				}

				this.samPanel.add(getSAMPictsPanel(i, size));
			}

			this.samPanel.add(getSendedButton());
		}

		return this.samPanel;
	}

	private JPanel getSAMPictsPanel(int samType, int size)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
				this.headerSAM[samType], 
				TitledBorder.DEFAULT_JUSTIFICATION , 
				TitledBorder.LEFT, this.fQuestions));

		NoneSelectedButtonGroup gr = new NoneSelectedButtonGroup();

		float step = 1.0F / ( size - 1 );
		if( size - 1 <= 0 )
		{
			step = 1F;
		}
		
		for (int j = 1; j <= size; j++)
		{
			JToggleButton bt = new JToggleButton();
			bt.setName( j + "" );

			String btTxt = "";

			Dimension d = bt.getPreferredSize();
			int side = d.width;
			if (side > d.height)
			{
				side = d.height;
			}

			if (samType == 0)
			{
				bt.setIcon(GeneralAppIcon.getSAMValence( ( j - 1 ) * step, side, Color.BLACK, Color.WHITE));
			}
			else if (samType == 1)
			{
				bt.setIcon(GeneralAppIcon.getSAMArousal( ( j - 1 ) * step, side, Color.BLACK, Color.WHITE));


			}
			else if (this.samDominance)
			{
				bt.setIcon(GeneralAppIcon.getSAMDominance( ( j - 1 ) * step, side, Color.BLACK, Color.WHITE));
			}
			else
			{
				EmotionParameter emo = this.samEmotionSet.get( j - 1 );
				
				if( emo.isSelect() )
				{
					btTxt = emo.getText();
					bt.setIcon(GeneralAppIcon.getBasicEmotion( emo.getType(), side, Color.BLACK, Color.WHITE, btTxt, bt.getFontMetrics(bt.getFont())));
				}
				else
				{
					bt = null;
				}
			}

			
			if( bt != null )
			{
				final int samPos = samType;
				bt.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) 
					{

					}
				});

				bt.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						JToggleButton b = (JToggleButton)e.getSource();

						if (b.isSelected())
						{
							AffectiveActivity.this.samValues[samPos] = b;

							if (!getSendedButton().isEnabled())
							{
								boolean enable = true;

								for (int i = 0; (i < AffectiveActivity.this.samValues.length) && (enable); i++)
								{
									enable = AffectiveActivity.this.samValues[i] != null;
								}

								getSendedButton().setEnabled(enable);
							}

							imagenPoligono2D.changeColorPixels(Color.WHITE, Color.LIGHT_GRAY, ((ImageIcon)b.getIcon()).getImage());
						}
						else
						{
							samValues[samPos] = null;
							getSendedButton().setEnabled(false);

							imagenPoligono2D.changeColorPixels(Color.LIGHT_GRAY, Color.WHITE, ((ImageIcon)b.getIcon()).getImage());
						}
					}
				});

				final String bTxt = btTxt;
				final int samAxis = samType;
				final float stepAux = step;
				bt.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent arg0) 
					{
						JToggleButton b = (JToggleButton)arg0.getSource();
						Dimension d = b.getSize();

						int side = d.width;
						if (side > d.height)
						{
							side = d.height;
						}

						if (samAxis == 0)
						{
							b.setIcon(GeneralAppIcon.getSAMValence( ( new Integer(b.getName()) - 1 ) * stepAux, side, Color.BLACK, Color.WHITE));
						}
						else if (samAxis == 1)
						{
							b.setIcon(GeneralAppIcon.getSAMArousal( ( new Integer(b.getName() ) - 1 ) * stepAux, side, Color.BLACK, Color.WHITE));


						}
						else if (AffectiveActivity.this.samDominance)
						{
							b.setIcon(GeneralAppIcon.getSAMDominance( ( new Integer( b.getName() ) - 1 ) * stepAux, side, Color.BLACK, Color.WHITE));
						}
						else
						{
							Font f = new Font( Font.DIALOG, Font.BOLD, 18);
							FontMetrics fm = b.getFontMetrics(f);

							b.setIcon(GeneralAppIcon.getBasicEmotion(new Integer(b.getName()), side, Color.BLACK, Color.WHITE, bTxt, fm));
						}

						if (b.getIcon() == null)
						{
							Font f = new Font( Font.DIALOG, Font.BOLD, d.height / 2);
							FontMetrics fm = b.getFontMetrics(f);
							Insets pad = b.getInsets();

							while( fm.stringWidth( b.getText() ) >  d.width - pad.left - pad.right
									&& fm.getHeight() < d.height)
							{
								f = new Font( f.getName(), f.getStyle(), f.getSize() - 1 );
								fm = b.getFontMetrics( f );
							}

							while( fm.stringWidth( b.getText() ) <  d.width - pad.left - pad.right
									&& fm.getHeight() < d.height)
							{
								f = new Font( f.getName(), f.getStyle(), f.getSize() + 1 );
								fm = b.getFontMetrics( f );
							}

							if( fm.stringWidth( b.getText() ) >  d.width - pad.left - pad.right
									&& fm.getHeight() < d.height)
							{
								f = new Font( f.getName(), f.getStyle(), f.getSize() - 1 );					
							}

							b.setFont(f);
						}

					}

				});
				gr.add(bt);
				panel.add(bt);
			}
		}

		return panel;
	}


	public String getReport()
	{
		this.reportAnswer = "";

		for( JToggleButton bt : this.samValues )
		{
			if( bt == null )
			{
				super.reportAnswer += "-" + super.separator;
			}
			else
			{
				super.reportAnswer += bt.getName() + super.separator;
			}
		}

		return this.reportActivity + this.separator + this.reportAffectiveInfo + this.separator + this.reportAnswer;
	}

	private JButton getSendedButton()
	{
		if (this.sendedButton == null)
		{
			this.sendedButton = new JButton("Enter");
			this.sendedButton.setEnabled(false);
			this.sendedButton.setBackground(Color.WHITE);

			this.sendedButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						Robot r = new Robot();
						Dimension d = panelTask.getSize();

						r.mouseMove( d.width * 2, d.height * 2);
					}					
					catch (Exception ex) 
					{
					}

					activityEnded();
					notifiedAnswer(true);
				}

			});
			this.sendedButton.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent arg0)
				{
					JButton b = (JButton)arg0.getSource();
					Dimension d = b.getSize();
					Font f = new Font( Font.DIALOG, Font.BOLD, d.height / 2);
					FontMetrics fm = b.getFontMetrics(f);
					Insets pad = b.getInsets();          

					while( fm.stringWidth( b.getText() ) >  d.width - pad.left - pad.right 
							&& fm.getHeight() < d.height )
					{
						f = new Font( f.getName(), f.getStyle(), f.getSize() - 1 );
						fm = b.getFontMetrics( f );
					}

					while( fm.stringWidth( b.getText() ) <  d.width - pad.left - pad.right
							&& fm.getHeight() < d.height )
					{
						f = new Font( f.getName(), f.getStyle(), f.getSize() + 1 );
						fm = b.getFontMetrics( f );
					}

					if( fm.stringWidth( b.getText() ) >  d.width - pad.left - pad.right 
							&& fm.getHeight() < d.height )
					{
						f = new Font( f.getName(), f.getStyle(), f.getSize() - 1 );					
					}          

					b.setFont(f);
				}
			});
		}


		return this.sendedButton;
	}

	protected void cleanUp()
			throws Exception
	{
		super.cleanUp();
		
		if( this.clip != null )
		{
			this.clip.stopThread( IStoppableThread.ForcedStop );
			this.clip = null;
		}
		
		if( this.nextClip != null )
		{
			this.nextClip.stopThread( IStoppableThread.ForcedStop );
			this.nextClip = null;
		}
		
		if( this.beepThread != null )
		{
			this.beepThread.stopThread( IStoppableThread.ForcedStop );
			this.beepThread = null;
		}
		
		System.gc();
	}


	protected WarningMessage checkSpecifyParameters()
	{
		WarningMessage msg = new WarningMessage();
		
		if( !(Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_OUT_MANUAL_ACTIVE ) 
				&& !(Boolean)ConfigApp.getProperty( ConfigApp.IS_TIME_OUT_AUTO_ACTIVE ) )
		{
			msg.setMessage( "Main timeout is not selected. Picture and/or sound will remain on the screen indefinitely.", WarningMessage.WARNING_MESSAGE );
		}
		
		List<AffectiveObject> files = (List<AffectiveObject>)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SLIDES );

		boolean affectSelected = false;
		
		if (files != null)
		{
			affectSelected = !files.isEmpty();
			
			for (AffectiveObject aff : files)
			{
				String m = "";

				if (aff == null)
				{
					m = "Null image\n";
				}
				else
				{
					try
					{
						File f = new File(aff.getPathFile());
						String mimetype = new MimetypesFileTypeMap().getContentType(f);
						String type = mimetype.split("/")[0];
						if (!type.equals("image"))
						{
							m = "File " + aff.getPathFile() + " is not a picture\n";
						}
					}
					catch (Exception e)
					{
						m = "Error in picture file " + aff.getPathFile() + ": " + e.getMessage() + "\n";
					}
				}


				if (!m.isEmpty())
				{
					msg.setMessage(msg.getMessage() + m, -1);
				}
			}
		}

		files = (List)this.parameters.getExtraParamters().get( Activity.ID_PARAMETER_SOUND_CLIPS );
		if (files != null)
		{
			affectSelected = affectSelected || !files.isEmpty();
			
			for (AffectiveObject aff : files)
			{
				String m = "";

				if (aff == null)
				{
					m = "Null image\n";
				}
				else
				{
					try
					{
						File f = new File(aff.getPathFile());
						if (!f.exists())
						{
							m = "Audio file " + aff.getPathFile() + " non found\n";
						}
					}
					catch (Exception e)
					{
						m = "Audio file null\n";
					}
				}


				if (!m.isEmpty())
				{
					msg.setMessage( msg.getMessage() + m, WarningMessage.ERROR_MESSAGE);
				}
			}
		}
		
		if( !affectSelected )
		{
			msg.setMessage( msg.getMessage() + "Slides and sounds are not selected.\n", WarningMessage.ERROR_MESSAGE );
		}

		return msg;
	}
}