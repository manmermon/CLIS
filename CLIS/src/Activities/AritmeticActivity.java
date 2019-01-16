/*
 * Copyright 2011-2013 by Manuel Merino Monge <manmermon@dte.us.es>
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

import Activities.Operaciones.opAritmetica;
import Auxiliar.WarningMessage;
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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class AritmeticActivity extends Activity
{
	private JTextField jTextFieldOperacion = null;
	private opAritmetica op = null;

	private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, true);

	public static final String ID = "Mathematic";
	
	public static void registerActivity()
	{
		activityID.setID( ID );
		activityID.setActivityClass(AritmeticActivity.class);
		ActivityRegister.registerActivity(activityID);
	}

	private AritmeticActivity()
	{
		super();
		this.reportActive = true;

		this.setPanelTask();
		super.setName( this.getClass().getCanonicalName());
	}

	public static Activity getInstance()
	{
		if (task == null)
		{
			task = new AritmeticActivity();
		}

		return task;
	}

	protected void makeSpecificTask(int dificultad) throws Exception
	{
		if (this.op == null)
		{
			this.op = new opAritmetica(dificultad);
		}
		else if (this.op.getDificultad() != dificultad)
		{
			this.op.setDificultad(dificultad);
		}

		this.op.generarOperacion();

		this.reportActivity = this.op.getOperacion() + "=" + this.op.getResultado();
		this.reportAnswer = "NaN";

		this.jTextFieldOperacion.setText(this.op.getOperacion());
	}

	@Override
	protected void setPanelTask()
	{
		if (this.panelTask == null)
		{
			this.panelTask = new JPanel();

			this.panelTask.setVisible(false);

			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);

			this.panelTask.setLayout(gridLayout);
			this.panelTask.add(getJTextFieldOperacion(), null);
			this.panelTask.add(getJPanelResultado(), null);
			this.panelTask.setVisible(true);
		}
	}  

	/**
	 * This method initializes jTextFieldOperacion	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldOperacion()
	{
		this.jTextFieldOperacion = new JTextField();
		this.jTextFieldOperacion.setEditable(false);
		this.jTextFieldOperacion.setBackground(Color.white);
		this.jTextFieldOperacion.setHorizontalAlignment( JTextField.CENTER );
		this.jTextFieldOperacion.setText("");
		this.jTextFieldOperacion.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		this.jTextFieldOperacion.getDocument().addDocumentListener(new DocumentListener()
		{
			public void removeUpdate(DocumentEvent e) 
			{}

			public void insertUpdate(DocumentEvent e)
			{
				Font f = jTextFieldOperacion.getFont();
				FontMetrics fm = jTextFieldOperacion.getFontMetrics(f);
				Dimension d = jTextFieldOperacion.getSize();
				Insets pad = jTextFieldOperacion.getInsets();
				String txt = jTextFieldOperacion.getText();

				while (fm.stringWidth(txt) > d.width - pad.left - pad.right)
				{
					f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
					fm = jTextFieldOperacion.getFontMetrics(f);
				}

				while (fm.stringWidth(txt) < d.width - pad.left - pad.right)
				{
					f = new Font(f.getName(), f.getStyle(), f.getSize() + 1);
					fm = jTextFieldOperacion.getFontMetrics(f);
				}

				if (fm.stringWidth(txt) > d.width - pad.left - pad.right)
				{
					f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
					fm = jTextFieldOperacion.getFontMetrics(f);
				}

				jTextFieldOperacion.setFont(f);
			}

			public void changedUpdate(DocumentEvent e) 
			{}

		});

		this.jTextFieldOperacion.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent arg0)
			{
				Dimension d = jTextFieldOperacion.getSize();
				Font f = new Font( Font.DIALOG, Font.PLAIN, d.height / 2);
				FontMetrics fm = jTextFieldOperacion.getFontMetrics(f);
				Insets pad = jTextFieldOperacion.getInsets();

				while (fm.stringWidth("( 100 * ( 100 * ( 100 * 100 ) ) ) = ?") > d.width - pad.left - pad.right)
				{
					f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
					fm = jTextFieldOperacion.getFontMetrics(f);
				}

				while (fm.stringWidth("( 100 * ( 100 * ( 100 * 100 ) ) ) = ?") < d.width - pad.left - pad.right)
				{
					f = new Font(f.getName(), f.getStyle(), f.getSize() + 1);
					fm = jTextFieldOperacion.getFontMetrics(f);
				}

				if (fm.stringWidth("( 100 * ( 100 * ( 100 * 100 ) ) ) = ?") > d.width - pad.left - pad.right)
				{
					f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
				}

				jTextFieldOperacion.setFont(f);
				jTextFieldOperacion.setText(jTextFieldOperacion.getText());
			}
		});
		return this.jTextFieldOperacion;
	}

	/**
	 * This method initializes jPanelResultado	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelResultado()
	{
		JPanel jPanelResultado = new JPanel();
		GridLayout gridLayout7 = new GridLayout();
		gridLayout7.setRows(1);
		jPanelResultado = new JPanel();
		jPanelResultado.setLayout(gridLayout7);
		jPanelResultado.setBackground(Color.white);

		for (int i = 0; i < 10; i++)
		{
			final JButton b = new JButton();
			b.setText(i + "");

			b.setFocusable( true );
			b.setBorder(new SoftBevelBorder( SoftBevelBorder.RAISED ));
			b.getInsets().left = 0;
			b.getInsets().right = 0;

			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					int target = op.getResultado();
					String userAnswer = b.getText();

					boolean answer = userAnswer.equals( target + "");

					reportAnswer = userAnswer;

					activityEnded();
					notifiedAnswer(answer);
				}        
			});

			b.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent arg0)
				{
					Dimension d = b.getSize();
					Font f = new Font( Font.DIALOG, Font.BOLD, d.height / 2);
					FontMetrics fm = b.getFontMetrics(f);
					Insets pad = b.getInsets();

					while (fm.stringWidth(b.getText()) > d.width - pad.left - pad.right)
					{
						f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
						fm = b.getFontMetrics(f);
					}

					while (fm.stringWidth(b.getText()) < d.width - pad.left - pad.right)
					{
						f = new Font(f.getName(), f.getStyle(), f.getSize() + 1);
						fm = b.getFontMetrics(f);
					}

					if (fm.stringWidth(b.getText()) > d.width - pad.left - pad.right)
					{
						f = new Font(f.getName(), f.getStyle(), f.getSize() - 1);
					}

					b.setFont(f);
				}


			});
			jPanelResultado.add(b, null);
		}

		return jPanelResultado;
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