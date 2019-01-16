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

import Activities.Operaciones.opSearchCount;
import Auxiliar.WarningMessage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class SearchCountTask extends Activity
{
	private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, true);

	public static final String ID = "Search-Count";
	
	public static void registerActivity()
	{
		activityID.setID( ID );
		activityID.setActivityClass(SearchCountTask.class);
		ActivityRegister.registerActivity(activityID);
	}

	private JButton botonTarget = null;
	private JTextField respueta = null;
	private JPanel matriz = null;

	private opSearchCount op = null;

	private SearchCountTask()
	{
		super();

		super.setName(getClass().getCanonicalName());

		super.reportActive = true;

		this.setPanelTask();
	}

	public static Activity getInstance()
	{
		if (task == null)
		{
			task = new SearchCountTask();
		}

		return task;
	}

	@Override
	protected void preStart() throws Exception
	{
		if (this.reportActive)
		{
			this.reportHeader = ("Target" + this.separator + "Num. Target" + this.separator + "Answer");
		}
	}

	protected void makeSpecificTask(int dificultad) throws Exception
	{
		this.matriz.setVisible(false);


		GridLayout gb = new GridLayout();
		int f = 8;int c = 6;
		switch (dificultad)
		{
		case 0:
		{
			f = 5;
			c = 5;
			break;
		}
		case 1:
		{
			f = 6;
			c = 5;
			break;
		}
		case 2:
		{
			f = 6;
			c = 6;
			break;
		}
		case 3:
		{
			f = 7;
			c = 6;
		}
		}

		gb.setRows(f);
		gb.setColumns(c);

		this.matriz.setLayout(gb);

		this.op = new opSearchCount(f, c);
		this.botonTarget.setText("Target: " + this.op.getObjetivo());

		int[][] m = this.op.getMatriz();

		if (this.matriz.getComponentCount() == f * c)
		{
			for (int fi = 0; fi < f; fi++)
			{
				for (int ci = 0; ci < c; ci++)
				{
					JButton b = (JButton)this.matriz.getComponent(c * fi + ci);
					b.setText( m[fi][ci] + "" );
				}
			}
		}
		else
		{
			this.matriz.removeAll();
			for (int fi = 0; fi < f; fi++)
			{
				for (int ci = 0; ci < c; ci++)
				{
					final JButton b = new JButton( m[fi][ci] + "" );

					b.setFont(new Font( Font.DIALOG, Font.BOLD, 36));
					b.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

					b.getInsets().left = 0;
					b.getInsets().right = 0;

					b.setFocusable(false);
					b.setFocusPainted(false);

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
										|| (fm.getHeight() > d.height - pad.bottom - pad.top))
								{
									f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
								}
							}

							b.setFont(f);
						}


					});
					this.matriz.add(b, null);
				}
			}
		}

		this.reportActivity = (this.op.getObjetivo() + this.separator + this.op.getNumApareciones());
		this.reportAnswer = "NaN";

		this.matriz.setVisible(true);

		this.respueta.setText("");
		this.respueta.requestFocus();
	}

	@Override
	protected void setPanelTask()
	{
		if (this.panelTask == null)
		{
			this.panelTask = new JPanel();
			this.panelTask.setLayout(new BorderLayout());

			this.panelTask.setVisible(false);

			this.panelTask.add(getJButtonTarget(), "North");
			this.panelTask.add(getJTextFieldCuenta(), "South");
			this.panelTask.add(getJPanelMatriz(), "Center");

			this.panelTask.setVisible(true);
		}
	}

	/**
	 * This method initializes jButtonTarget	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonTarget()
	{
		if (this.botonTarget == null)
		{
			this.botonTarget = new JButton();
			this.botonTarget.setBackground(Color.white);
			this.botonTarget.setFocusable(false);
			this.botonTarget.setFocusPainted(false);
			this.botonTarget.setFocusCycleRoot(false);

			Dimension d = this.botonTarget.getPreferredSize();
			d.height *= 5;
			this.botonTarget.setPreferredSize(d);

			this.botonTarget.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent arg0)
				{
					Dimension d = botonTarget.getSize();

					Font f = new Font( Font.DIALOG, Font.BOLD, 22);
					FontMetrics fm = botonTarget.getFontMetrics(f);
					botonTarget.getInsets().bottom = 0;
					botonTarget.getInsets().left = 0;
					botonTarget.getInsets().right = 0;
					botonTarget.getInsets().top = 0;
					Insets pad = botonTarget.getInsets();
					String txt = botonTarget.getText();

					if (!txt.isEmpty())
					{
						while (fm.getHeight() > d.height - pad.bottom - pad.top)
						{
							f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
							fm = botonTarget.getFontMetrics(f);
						}

						while (fm.getHeight() < d.height - pad.bottom - pad.top)
						{
							f = new Font(f.getName(), f.getStyle(), f.getSize() + 1);
							fm = botonTarget.getFontMetrics(f);
						}
					}

					botonTarget.setFont(f);
					getJTextFieldCuenta().setFont(f);
				}
			});
		}

		return this.botonTarget;
	}

	/**
	 * This method initializes jTextFieldCuenta	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCuenta()
	{
		if (this.respueta == null)
		{
			this.respueta = new JTextField();

			this.respueta.setHorizontalAlignment( JTextField.CENTER );
			this.respueta.setFont(new Font( Font.DIALOG, Font.BOLD, 20));
			this.respueta.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

			this.respueta.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String res = respueta.getText();

					if (!res.isEmpty())
					{
						boolean answer = res.equals( op.getNumApareciones() + "" );

						reportAnswer = res;

						activityEnded();
						notifiedAnswer(answer);
					}

				}
			});
			this.respueta.addFocusListener(new FocusAdapter()
			{
				public void focusLost(FocusEvent arg0)
				{
					respueta.requestFocus();
				}

			});

			((AbstractDocument)this.respueta.getDocument()).setDocumentFilter(new DocumentFilter()
			{
				public void insertString(DocumentFilter.FilterBypass fp, int offset
						, String string, AttributeSet aset)
								throws BadLocationException
				{
					int len = string.length();
					boolean isValidInteger = true;

					for (int i = 0; i < len; i++)
					{
						if (!Character.isDigit(string.charAt(i)))
						{
							isValidInteger = false;
							break;
						}
					}

					if (isValidInteger)
					{
						super.insertString(fp, offset, string, aset);
					}
					else
					{
						Toolkit.getDefaultToolkit().beep();
					}
				}


				public void replace(DocumentFilter.FilterBypass fp, int offset
						, int length, String string, AttributeSet aset)
								throws BadLocationException
				{
					int len = string.length();
					boolean isValidInteger = true;

					for (int i = 0; i < len; i++)
					{
						if (!Character.isDigit(string.charAt(i)))
						{
							isValidInteger = false;
							break;
						}
					}

					if (isValidInteger)
					{
						super.replace(fp, offset, length, string, aset);
					}
					else
					{
						Toolkit.getDefaultToolkit().beep();
					}

				}
			});
			this.respueta.setFont(getJButtonTarget().getFont());
			this.respueta.setPreferredSize(getJButtonTarget().getPreferredSize());
		}

		return this.respueta;
	}

	/**
	 * This method initializes jPanelMatriz	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelMatriz()
	{
		if (this.matriz == null)
		{
			this.matriz = new JPanel();
		}
		return this.matriz;
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
	protected void preSuspendActivity() {}

	protected WarningMessage checkSpecifyParameters()
	{
		return new WarningMessage();
	}
}