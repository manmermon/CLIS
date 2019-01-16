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

import Activities.Operaciones.opMemoria;
import Auxiliar.WarningMessage;
import GUI.MyComponents.imagenPoligono2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.SoftBevelBorder;

public class MemoryTask extends Activity
{
	private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, true);

	public static final String ID = "Memory";
	
	public static void registerActivity()
	{
		activityID.setID( ID );
		activityID.setActivityClass(MemoryTask.class);
		ActivityRegister.registerActivity(activityID);
	}

	private opMemoria op = null;

	private int[] tablaMemoriaCorrecta = null;
	private int[] tablaPosicionesRespuestas = null;

	private JPopupMenu listaOpciones = null;

	private MemoryTask()
	{
		super.setName( this.getClass().getCanonicalName());

		setPanelTask();

		super.reportActive = true;

		super.info = new ActivityInfo(2, 2, 2, true, true);

		super.isEndedTask = false;
	}

	@Override
	protected void setPanelTask()
	{
		if (this.panelTask == null)
		{
			this.panelTask = new JPanel();
		}
	}

	public static Activity getInstance()
	{
		if (task == null)
		{
			task = new MemoryTask();
		}

		return task;
	}


	@Override
	protected void makeSpecificTask(int dificultad) throws Exception
	{
		this.isEndedTask = false;


		int f = 4;int c = 4;
		switch (dificultad)
		{

		case 0: 
			f = 2;
			c = 2;
			break;


		case 1: 
			f = 2;
			c = 3;
			break;


		case 2: 
			f = 3;
			c = 3;
			break;


		case 3: 
			f = 4;
			c = 3;
		}



		this.op = new opMemoria(f, c);

		this.op.generarTarea();

		this.panelTask.setVisible(false);
		this.panelTask.removeAll();

		GridLayout gb = new GridLayout();
		gb.setRows(f);
		gb.setColumns(c);

		this.panelTask.setLayout(gb);

		final int[] t = this.op.getTarea();
		this.tablaMemoriaCorrecta = t;
		this.tablaPosicionesRespuestas = new int[f * c];

		String target = "[";
		for (int i = 0; i < f * c; i++)
		{
			target = target + t[i];
			if (i < f * c - 1)
			{
				target = target + ",";
			}

			final JButton b = new JButton();

			b.setBackground(Color.WHITE);

			b.setBorder(new SoftBevelBorder(0));
			b.setFont(new Font( Font.DIALOG, Font.BOLD, 36));

			b.setFocusable(false);
			b.setFocusPainted(false);

			b.setName( t[i] + "" );

			final int indI = i;
			b.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent e)
				{
					b.setIcon(getFigura(b.getSize(), t[indI]));
				}

			});
			this.panelTask.add(b, null);
		}

		target = target + "]";

		this.reportActivity = target;
		this.reportAnswer = "NaN";

		if (super.isAnswerPhase())
		{
			answerPhase();
		}

		this.panelTask.setVisible(true);
	}

	/**
	 * This method initializes jPopupMenuOpcionesMemoria	
	 * 	
	 * @return javax.swing.JPopupMenu	
	 */
	private JPopupMenu getJPopupMenuOpcionesMemoria(final JButton c, final int indice)
	{
		final JPopupMenu jPopupMenuOpcionesMemoria = new JPopupMenu();

		int[] figMemorizar = opMemoria.getConjuntoMemoria();

		int nc = 2;
		int nf = figMemorizar.length / nc;
		nf += figMemorizar.length - nf * nc;

		GridLayout ly = new GridLayout(nf, nc);
		jPopupMenuOpcionesMemoria.setLayout(ly);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int)(d.width * 0.04D);
		int h = (int)(d.height * 0.04D);

		for (int i = 0; i < figMemorizar.length; i++)
		{
			final JButton b = new JButton();
			b.setBackground(Color.white);
			b.setPreferredSize(new Dimension(w, h));

			b.setName(figMemorizar[i] + "" );

			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String v = b.getName();

					c.setIcon( getFigura(c.getSize(), new Integer(v).intValue()));
					c.setName(b.getName());


					tablaPosicionesRespuestas[indice] = 1;
					comprobarRespuestaMemoria();

					jPopupMenuOpcionesMemoria.setVisible(false);
				}

			});

			final int indI = i;
			b.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent e)
				{
					b.setIcon(getFigura(b.getSize(), indI));
				}


			});
			jPopupMenuOpcionesMemoria.add(b);
		}

		return jPopupMenuOpcionesMemoria;
	}

	private void comprobarRespuestaMemoria()
	{
		boolean answer = true;

		int f = this.tablaMemoriaCorrecta.length;
		int nRes = 0;

		for (int i = 0; i < f; i++)
		{
			nRes += this.tablaPosicionesRespuestas[i];
		}

		if (nRes == f)
		{
			Component[] matriz = super.panelTask.getComponents();

			String userAnswer = "[";
			int i = 0;
			for ( ; (i < f) && (answer); i++)
			{
				int resp = new Integer(matriz[i].getName()).intValue();
				int corr = this.tablaMemoriaCorrecta[i];

				userAnswer = userAnswer + resp;

				if (i < f - 1)
				{
					userAnswer = userAnswer + ",";
				}

				answer = corr == resp;
			}

			if (!answer) 
			{
				for ( ; i < f; i++)
				{
					userAnswer = userAnswer + new Integer(matriz[i].getName()).intValue();

					if (i < f - 1)
					{
						userAnswer = userAnswer + ",";
					}
				}
			}

			userAnswer = userAnswer + "]";

			super.reportAnswer = userAnswer;

			activityEnded();

			super.notifiedAnswer(answer);
		}
	}

	public boolean requestPreTimer()
	{
		return true;
	}

	private Icon getFigura(Dimension d, int fig)
	{
		ImageIcon icon = null;

		Color colorFig = Color.WHITE;
		if (fig % 2 == opMemoria.NEGRO )
		{
			fig--;
			colorFig = Color.BLACK;
		}

		int w = d.width;int h = d.height;
		int l = w;
		if (w > h)
		{
			l = h;
		}

		l /= 2;

		switch (fig)
		{
		case opMemoria.CIRCULO:
		{
			icon = new ImageIcon(imagenPoligono2D.crearImagenCircunferencia(0, 0, l, 1.0F, Color.BLACK, 
					imagenPoligono2D.crearImagenCirculo(0, 0, l, colorFig, null)));
			break;

		}
		case opMemoria.CUADRADO:
		{
			icon = new ImageIcon(imagenPoligono2D.crearImagenRectangulo(l, l, 1.0F, Color.BLACK, colorFig));
			break;

		}
		case opMemoria.ROMBO:
		{
			icon = new ImageIcon(imagenPoligono2D.crearImagenRombo(l, 1.0F, Color.BLACK, colorFig));
			break;
		} 
		default: 
		{
			icon = new ImageIcon(imagenPoligono2D.crearImagenTriangulo(l, 1.0F, Color.BLACK, colorFig, 0));
		}
		}



		return icon;
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
		if (isMainPhase())
		{
			this.answerPhase();
		}
	}

	private void answerPhase()
	{
		this.panelTask.setVisible(false);

		Component[] nFigMem = this.panelTask.getComponents();
		this.panelTask.removeAll();
		for (int i = 0; i < nFigMem.length; i++)
		{
			final JButton b = new JButton();
			b.setName(((JButton)nFigMem[i]).getName());

			b.setBackground(Color.WHITE);

			b.setBorder(new SoftBevelBorder( SoftBevelBorder.RAISED));
			b.setFont(new Font( Font.DIALOG, Font.BOLD, 36));

			b.setFocusable(false);
			b.setFocusPainted(false);

			b.setIcon(null);

			final int indI = i;
			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Point pos = MouseInfo.getPointerInfo().getLocation();
					if (listaOpciones != null)
					{
						listaOpciones.setVisible(false);
					}
					listaOpciones = getJPopupMenuOpcionesMemoria(b, indI);
					listaOpciones.setLocation(pos);
					listaOpciones.setVisible(true);
				}

			});
			this.panelTask.add(b, null);
		}

		this.panelTask.setVisible(true);
	}


	@Override
	public void stopActivity()
	{
		super.stopActivity();

		if (this.listaOpciones != null)
		{
			this.listaOpciones.setVisible(false);
			this.listaOpciones = null;
		}
	}

	@Override
	protected void preSuspendActivity() {}

	protected WarningMessage checkSpecifyParameters()
	{
		return new WarningMessage();
	}
}