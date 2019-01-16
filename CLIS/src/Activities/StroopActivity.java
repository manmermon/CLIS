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

import Activities.Operaciones.Colores;
import Activities.Operaciones.opColorWord;
import Auxiliar.WarningMessage;
import GUI.MyComponents.clipSoundThread;
import StoppableThread.IStoppableThread;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

public class StroopActivity extends Activity
{
	private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, true);

	public static final String ID = "Stroop test";
	
	public static void registerActivity()
	{
		activityID.setID( ID );
		activityID.setActivityClass(StroopActivity.class);
		ActivityRegister.registerActivity(activityID);
	}

	private JTextField target = null;
	private JPanel panelWordColor = null;

	private String audioFolder = "./Sonidos/";
	private String audioFileExt = ".wav";

	private clipSoundThread sonido = null;

	private opColorWord op = null;

	private Thread preActivityThread = null;

	private StroopActivity()
	{
		super.setName(getClass().getCanonicalName());

		this.reportActive = true;

		setPanelTask();
	}

	public static Activity getInstance()
	{
		if (task == null)
		{
			task = new StroopActivity();
		}

		return task;
	}

	@Override
	protected void preStart() throws Exception
	{
		if (this.reportActive)
		{
			this.reportHeader = 
					("TextColor" + this.separator + "ColorText" + this.separator + "Target" + this.separator + "AnswerTextColor" + this.separator + "AnswerColorText");
		}
	}

	protected void makeSpecificTask(int dificultad) throws Exception
	{
		if (this.op == null)
		{
			this.op = new opColorWord(dificultad);
		}

		this.op.setDificultad(dificultad);

		this.op.generarTarea();

		this.target.setVisible(false);
		this.target.setText(this.op.getObjetivo().getEtiqueta());
		this.target.setForeground(this.op.getObjetivo().getColor());

		if (this.sonido != null)
		{
			this.sonido.stopThread( IStoppableThread.ForcedStop );
			this.sonido = null;
		}

		if (dificultad == 4)
		{
			String etColor = this.op.getObjetivo().getEtiqueta();
			InputStream path = getClass().getResourceAsStream("/Sonidos/" + etColor + ".wav");

			this.sonido = new clipSoundThread();
			this.sonido.addParameter( clipSoundThread.ID_SOUND_PARAMETER, path);
			this.sonido.createTask();
		}
		else if (dificultad < 3)
		{
			Color c = this.op.getObjetivo().getColor();

			Color[] cs = this.op.getColorPalette();

			boolean enc = false;
			int i = 0;
			while ((i < cs.length) && (!enc))
			{
				enc = c.equals(cs[i]);
				if (!enc)
				{
					i++;
				}
			}

			String etColor = this.op.getColorLabels()[i];


			String path = this.audioFolder + etColor + this.audioFileExt;

			this.sonido = new clipSoundThread();
			this.sonido.addParameter(clipSoundThread.ID_SOUND_PARAMETER, path);
			this.sonido.createTask();
		}


		Color c = this.target.getForeground();
		this.reportActivity = c.toString();

		Color[] cs = this.op.getColorPalette();
		int j = 0; 
		for ( ; j < cs.length; j++)
		{
			if (cs[j].equals(c)) 
			{
				break;
			}
		}


		if (j < cs.length)
		{
			this.reportActivity = this.op.getColorLabels()[j];
		}

		this.reportActivity = (this.reportActivity + this.separator + this.target.getText() + this.separator);
		this.reportActivity += this.op.getObjetivo().getEtiqueta();

		this.reportAnswer = ("-" + this.separator + "-");

		this.panelWordColor.setVisible(false);

		if (this.panelWordColor.getComponentCount() == 0)
		{
			for (int i = 0; i < 5; i++)
			{
				final JButton b = new JButton();
				b.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				b.setName(this.op.getObjetivo().getEtiqueta());
				b.setFont(new Font( Font.DIALOG, Font.BOLD, 24));

				String etColor = this.op.getOpciones()[i].getEtiqueta();
				b.setText(etColor);
				b.setBackground(Color.WHITE);
				b.setForeground(this.op.getOpciones()[i].getColor());


				b.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent arg0)
					{
						Dimension d = b.getSize();

						Font f = b.getFont();
						FontMetrics fm = b.getFontMetrics(f);

						Insets pad = b.getInsets();
						pad.bottom = 0;
						pad.left = 0;
						pad.right = 0;
						pad.top = 0;
						b.getInsets(pad);

						String txt = b.getText();

						if (!txt.isEmpty())
						{
							while ((fm.stringWidth(txt) > d.width - pad.left - pad.right) 
									|| ( fm.getHeight() > d.height - pad.bottom - pad.top))
							{
								f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
								fm = b.getFontMetrics(f);
							}

							while ((fm.stringWidth(txt) < d.width - pad.left - pad.right) 
									&& ( fm.getHeight() < d.height - pad.bottom - pad.top))
							{
								f = new Font(f.getName(), f.getStyle(), f.getSize() + 1);
								fm = b.getFontMetrics(f);
							}

							if ((fm.stringWidth(txt) > d.width - pad.left - pad.right) 
									||  (fm.getHeight() > d.height - pad.bottom - pad.top))
							{
								f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
							}
						}

						b.setFont(f);
					}

				});

				b.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JButton b = (JButton)e.getSource();

						Color[] cs = op.getColorPalette();
						Color c = b.getForeground();
						int i = 0;
						for ( ; i < cs.length; i++)
						{
							if (cs[i].equals(c)) 
							{
								break;
							}
						}

						if (i < cs.length)
						{
							reportAnswer = op.getColorLabels()[i];
						}
						else
						{
							reportAnswer = c.toString();
						}

						reportAnswer +=  separator + b.getText();

						boolean answer = b.getForeground().equals(target.getForeground());

						activityEnded();
						notifiedAnswer(answer);
					}

				});

				this.panelWordColor.add(b, null);
			}

		}
		else 
		{
			for (int i = 0; i < 5; i++)
			{
				JButton b = (JButton)this.panelWordColor.getComponent(i);
				b.setVisible(false);
				b.setName(this.op.getObjetivo().getEtiqueta());

				String etColor = this.op.getOpciones()[i].getEtiqueta();
				b.setText(etColor);
				b.setBackground(Color.WHITE);
				b.setForeground(this.op.getOpciones()[i].getColor());

				b.addComponentListener(new ComponentAdapter()
				{
					public void componentHidden(ComponentEvent e)
					{
						JButton b = (JButton)e.getSource();
						Dimension d = b.getSize();

						Font f = b.getFont();
						FontMetrics fm = b.getFontMetrics(f);

						Insets pad = b.getInsets();
						pad.bottom = 0;
						pad.left = 0;
						pad.right = 0;
						pad.top = 0;
						b.getInsets(pad);

						String txt = b.getText();

						if (!txt.isEmpty())
						{
							while ((fm.stringWidth(txt) > d.width - pad.left - pad.right) 
									|| (fm.getHeight() > d.height - pad.bottom - pad.top))
							{
								f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
								fm = b.getFontMetrics(f);
							}

							while ((fm.stringWidth(txt) < d.width - pad.left - pad.right) 
									&& (fm.getHeight() < d.height - pad.bottom - pad.top))
							{
								f = new Font(f.getName(), f.getStyle(), f.getSize() + 1);
								fm = b.getFontMetrics(f);
							}

							if ((fm.stringWidth(txt) > d.width - pad.left - pad.right)
									|| (fm.getHeight() > d.height - pad.bottom - pad.top))
							{
								f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
							}
						}

						b.setFont(f);
					}

				});

				b.setVisible(true);
			}
		}
	}

	protected void setPanelTask()
	{
		if (this.panelTask == null)
		{
			this.panelTask = new JPanel();
			this.panelTask.setVisible(false);

			this.panelTask.removeAll();

			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);

			this.panelTask.setLayout(gridLayout);
			this.panelTask.add(getJTextFieldWord(), null);
			this.panelTask.add(getJPanelColores(), null);

			this.panelTask.setVisible(true);
		}
	}

	/**
	 * This method initializes jTextFieldWord	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldWord()
	{
		if (this.target == null)
		{
			this.target = new JTextField();
			this.target.setBackground(Color.white);

			this.target.setHorizontalAlignment( JTextField.CENTER );

			this.target.setEditable(false);
			this.target.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

			this.target.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent arg0)
				{
					Dimension d = target.getSize();
					target.setFont(new Font( Font.DIALOG, Font.PLAIN, d.height / 2));
				}
			});
		}

		return this.target;
	}

	/**
	 * This method initializes jPanelColores	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelColores()
	{
		if (this.panelWordColor == null)
		{
			this.panelWordColor = new JPanel();


			GridLayout gridLayout6 = new GridLayout();
			gridLayout6.setRows(1);
			this.panelWordColor.setLayout(gridLayout6);
			this.panelWordColor.setBackground(Color.white);
		}

		return this.panelWordColor;
	}


	@Override
	public void startTask() throws Exception
	{
		if (this.sonido != null)
		{
			if (this.preActivityThread != null)
			{
				this.preActivityThread.interrupt();
			}

			this.preActivityThread = new Thread()
			{
				public void run()
				{
					try
					{
						if( panelTask != null )
						{
							panelTask.setVisible( false );
							Thread.sleep( 1200 );
						}
					}
					catch( Exception e )
					{}
					finally
					{
						if( panelTask != null )
						{
							panelTask.setVisible( true );
						}
					}
				}
			};
			this.preActivityThread.start();

			this.sonido.startTask();
		}

		if (this.target != null)
		{
			this.target.setVisible(true);
		}

		if (this.panelWordColor != null)
		{
			this.panelWordColor.setVisible(true);
		}

		super.startTask();
	}

	@Override
	public void stopActivity()
	{
		super.stopActivity();

		if (this.sonido != null)
		{
			this.sonido.stopThread(1);
		}

		if (this.preActivityThread != null)
		{
			this.preActivityThread.interrupt();
			this.preActivityThread = null;
		}
	}

	@Override
	protected void preStopThread(int friendliness) throws Exception
	{
		super.preStopThread(friendliness);

		this.stopActivity();
		this.sonido = null;
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
	protected void nextState()
			throws Exception
	{
		if (this.sonido != null)
		{
			this.sonido.continueSound();
		}
	}

	@Override
	protected void preSuspendActivity()
	{
		if (this.sonido != null)
		{
			this.sonido.stopSound();
		}
	}

	@Override
	protected WarningMessage checkSpecifyParameters()
	{
		WarningMessage msg = new WarningMessage();

		opColorWord opcw = new opColorWord( 0 );
		String[] LBLS = opcw.getColorLabels();

		for (int i = 0; i < LBLS.length; i++) 
		{ 
			String lbl = LBLS[i];

			String path = this.audioFolder + lbl + this.audioFileExt;
			File f = new File(path);

			if (!f.exists())
			{
				msg.setMessage(msg.getMessage() + "Audio file " + path + " non found\n", WarningMessage.ERROR_MESSAGE );
			}
		}

		return msg;
	}
}