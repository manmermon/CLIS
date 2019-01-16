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

import Activities.Operaciones.opTrajectory;
import Auxiliar.WarningMessage;
import GUI.MyComponents.Point2D;
import GUI.MyComponents.imagenPoligono2D;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;
import com.vividsolutions.jts.geom.Coordinate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import org.jgrasstools.gears.utils.math.interpolation.splines.Bspline;

public class TrajectoryTask extends Activity
{
	private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, true);

	public static final String ID = "Trajectory";
	
	public static void registerActivity()
	{
		activityID.setID( ID );
		activityID.setActivityClass(TrajectoryTask.class);
		ActivityRegister.registerActivity(activityID);
	}

	private opTrajectory op = null;

	private JLabel canvas = null;
	private float thickness = 60.0F;
	private int pad = (int)(1.25D * this.thickness);

	private Shape fig = null;

	private Point mousePosition = null;
	private Point pointerPosition = null;

	private Shape finalLoc = null;
	private Shape initLoc = null;

	private boolean start = false;

	private MouseAdapter mouse_event = null;
	private Image imgError;
	private Image imgCorrect;
	private Image imgBase;
	private double[][] trajectory = null;
	private double[][] nextTrajectory = null;

	private Rectangle pointer = null;
	private Dimension pointerSize = new Dimension(6, 6);
	private boolean active = false;

	private AbstractStoppableThread errorThread = null;

	private IStoppableThread nextTrajectoryThread = null;

	private int errorCount = 0;

	private final int ANSWER_STATE = 1;
	private final int NON_ANSWER_STATE = 0;

	private int state = NON_ANSWER_STATE;

	private TrajectoryTask()
	{
		super();

		super.setName( this.getClass().getCanonicalName());

		super.reportActive = true;
		super.reportActivity = "";

		this.setPanelTask();

		super.isEndedTask = false;

		createMouseEventListener();
	}

	public static Activity getInstance()
	{
		if (task == null)
		{
			task = new TrajectoryTask();
		}

		return task;
	}

	@Override
	protected void setPanelTask()
	{
		if (this.panelTask == null)
		{
			this.panelTask = new JPanel();

			this.panelTask.setVisible(false);

			this.panelTask.setBackground(Color.WHITE);
			this.panelTask.setLayout(new BorderLayout());

			this.panelTask.addComponentListener(new ComponentAdapter()
			{
				Timer controlTimer = null;
				int DELAY = 100;

				public void componentResized(ComponentEvent e)
				{
					if (op != null)
					{
						final JPanel panel = (JPanel)e.getSource();

						if (this.controlTimer == null)
						{
							this.controlTimer = new Timer(this.DELAY, new ActionListener()
							{

								public void actionPerformed(ActionEvent e)
								{
									if (e.getSource() == controlTimer)
									{
										controlTimer.stop();
										controlTimer = null;

										Dimension size = panel.getSize();

										parameters.addExtraParameter( Activity.ID_ACTIVITY_PANEL_SIZE, size);

										updateTrajectory();
									}
								}
							});

							this.controlTimer.start();
						}
						else
						{
							this.controlTimer.restart();
						}

					}
				}
			});

			this.panelTask.setVisible(true);
		}
	}


	@Override
	protected void preStart() throws Exception
	{
		if (this.reportActive)
		{
			this.reportHeader = "Errors";
		}
	}


	@Override
	protected void makeSpecificTask(int dificultad) throws Exception
	{
		if (this.op == null)
		{
			this.op = new opTrajectory(dificultad);
		}
		else if (this.op.getDificultad() != dificultad)
		{
			this.op.setDificultad(dificultad);
		}

		this.reportAnswer = "NaN";

		this.errorCount = 0;

		this.mousePosition = null;
		this.pointerPosition = null;


		this.finalLoc = null;
		this.initLoc = null;


		this.panelTask.setVisible(false);
		this.panelTask.removeAll();

		this.canvas = new JLabel();
		this.canvas.setLayout(null);

		this.pointer = null;

		this.start = false;

		this.trajectory = this.nextTrajectory;
		if (this.trajectory == null)
		{
			this.trajectory = this.op.creatingTrayectory();
		}

		this.canvas.addMouseListener(this.mouse_event);
		this.canvas.addMouseMotionListener(this.mouse_event);

		this.panelTask.add(this.canvas, BorderLayout.CENTER );
		this.panelTask.setBorder(new LineBorder(Color.black));

		this.panelTask.setVisible(true);

		this.isEndedTask = false;
	}

	@Override
	protected void postMakeActivity(int difficutly) throws Exception
	{
		this.nextTrajectory = this.op.creatingTrayectory();
	}

	private void updateTrajectory()
	{
		Dimension d = (Dimension)this.parameters.getExtraParamters().get( Activity.ID_ACTIVITY_PANEL_SIZE );

		if ((d.width > 0) && (d.height > 0))
		{
			int dificultad = this.op.getDificultad();
			int usedPad = this.pad - 5 * dificultad;

			Bspline bspline = new Bspline();
			for (int i = 0; i < this.trajectory.length; i++)
			{
				double xt = this.trajectory[i][0] * (d.width - usedPad) + usedPad / 2.0D;
				double yt = this.trajectory[i][1] * (d.height - usedPad) + usedPad / 2.0D;

				bspline.addPoint(xt, yt);
			}


			List<Coordinate> coords = bspline.getInterpolated();
			int[] xs = new int[coords.size()];
			int[] ys = new int[xs.length];
			Iterator<Coordinate> it = coords.iterator();
			int j = 0;
			while (it.hasNext())
			{
				Coordinate c = (Coordinate)it.next();

				xs[j] = ((int)c.x);
				ys[j] = ((int)c.y);

				j++;
			}

			Point2D aux1 = new Point2D(xs[(xs.length / 2)], ys[(xs.length / 2)]);
			Point2D aux2 = new Point2D(xs[(xs.length / 2 - 1)], ys[(xs.length / 2 - 1)]);
			Point2D auxF = new Point2D((aux1.x() + aux2.x()) / 2.0D, (aux1.y() + aux2.y()) / 2.0D);
			double r = 30.0D;

			if (aux1.distanceTo(aux2) > r)
			{
				r = aux1.distanceTo(aux2);
			}

			aux1 = new Point2D(xs[0], ys[0]);
			aux2 = new Point2D(xs[(xs.length - 1)], ys[(xs.length - 1)]);
			Point2D auxI = new Point2D((aux1.x() + aux2.x()) / 2.0D, (aux1.y() + aux2.y()) / 2.0D);

			if (aux1.distanceTo(aux2) > r)
			{
				r = aux1.distanceTo(aux2);
			}

			this.finalLoc = new Ellipse2D.Double(auxF.x() - r / 2.0D, auxF.y() - r / 2.0D, r, r);
			this.initLoc = new Ellipse2D.Double(auxI.x() - r / 2.0D, auxI.y() - r / 2.0D, r, r);

			Area areaFig = new Area(new Polygon(xs, ys, xs.length));
			areaFig.add(new Area(this.initLoc));
			areaFig.add(new Area(this.finalLoc));

			this.fig = areaFig;

			this.imgCorrect = imagenPoligono2D.crearLienzoVacio(d.width, d.height, Color.WHITE);

			imagenPoligono2D.crearImagenDibujarPerfilFigura(0, 0, this.fig, Color.BLACK, 2.0F, this.imgCorrect);

			this.imgError = imagenPoligono2D.changeColorPixels(Color.BLACK, Color.RED, imagenPoligono2D.copyImage(this.imgCorrect));

			FontMetrics fm = this.canvas.getFontMetrics(this.canvas.getFont());
			int fh = fm.getHeight();
			int fw = fm.stringWidth("A");

			imagenPoligono2D.crearImagenDibujarFigura(0, 0, this.initLoc, Color.ORANGE, this.imgCorrect);

			imagenPoligono2D.crearImagenTexto((int)(this.initLoc.getBounds().x + this.initLoc.getBounds().width / 2 - fw / 2.0D), 
					(int)(this.initLoc.getBounds().y + this.initLoc.getBounds().height / 2 - fh / 2.0D), 
					"A", fm, null, Color.BLACK, this.imgCorrect);

			imagenPoligono2D.crearImagenTexto((int)(this.finalLoc.getBounds().x + this.finalLoc.getBounds().width / 2 - fw / 2.0D), 
					(int)(this.finalLoc.getBounds().y + this.finalLoc.getBounds().height / 2 - fh / 2.0D), 
					"B", fm, null, Color.BLACK, this.imgCorrect);

			this.imgBase = this.imgCorrect;

			if (this.pointer != null)
			{
				this.pointer.setLocation((int)this.initLoc.getBounds().getCenterX(), 
						(int)this.initLoc.getBounds().getCenterY());
			}

			updateWayImage();
		}
	}

	private void createMouseEventListener()
	{
		if (this.mouse_event == null)
		{
			this.mouse_event = new MouseAdapter()
			{
				@Override
				public void mouseMoved(MouseEvent e)
				{
					if ((pointer != null) && (active))
					{
						imgBase = imgCorrect;

						Dimension shift = mouseShifting( e );

						pointer.setLocation(pointer.x + shift.width, pointer.y + shift.height);


						if (!fig.contains(pointer.getCenterX(), pointer.getCenterY()))
						{
							imgBase = imgError;

							pointer.setLocation(pointerPosition);

							if (errorThread == null)
							{
								state = ANSWER_STATE;
								notifiedAnswer(false);

								errorThread = new AbstractStoppableThread()
								{
									protected void runInLoop()
											throws Exception
									{
										active = false;

										imgBase = imgError;
										updateWayImage();

										Thread.sleep(750L);

										imgBase = imgCorrect;
										updateWayImage();
									}

									public void targetDone() throws Exception
									{
										errorCount += 1;
										this.stopThread = true;
									}

									@Override
									protected void runExceptionManager(Exception e) 
									{}

									@Override
									protected void preStopThread(int friendliness) throws Exception
									{}

									@Override
									protected void postStopThread(int friendliness) throws Exception
									{}

									@Override
									protected void cleanUp() throws Exception
									{
										super.cleanUp();
										errorThread = null;
									}
								};

								try
								{
									errorThread.startThread();
								}
								catch (Exception e1)
								{
									e1.printStackTrace();
								}
							}
							else
							{
								errorThread.interrupt();
							}
						}
						else
						{
							mousePosition = e.getPoint();
							pointerPosition = pointer.getLocation();

							if (finalLoc.contains(pointer.getCenterX(), pointer.getCenterY()))
							{
								start = false;

								activityEnded();

								pointer = null;

								if (errorThread != null)
								{
									errorThread.stopThread( IStoppableThread.ForcedStop );
								}

								reportAnswer = errorCount + "";

								notifiedAnswer(true);
							}
							else
							{
								updateWayImage();
							}
						}
					}
				}

				@Override
				public void mouseDragged(MouseEvent e)
				{
					if (pointer != null)
					{
						if ((pointer.contains(e.getX(), e.getY())) && 
								(!active))
						{
							pointer.setLocation(e.getX() - pointer.width / 2, e.getY() - pointer.height / 2);
							mousePosition = e.getPoint();
							active = true;
						}

						mouseMoved(e);
					}
				}

				@Override
				public void mouseReleased(MouseEvent e)
				{
					active = false;
				}

				@Override
				public void mousePressed(MouseEvent e)
				{
					if (!start)
					{
						if (initLoc.contains(e.getX(), e.getY()))
						{
							pointer = new Rectangle(pointerSize);
							mousePosition = e.getPoint();
							pointerPosition = new Point(e.getX() - pointerSize.width / 2, e.getY() - pointerSize.height / 2);
							pointer.setLocation(pointerPosition);

							start = true;
							active = true;

							updateWayImage();
						}
					}
					else if ((pointer != null) 
							&& (pointer.contains(e.getX(), e.getY())))
					{
						mousePosition = e.getPoint();
						active = true;
					}
				}
			};
		}
	}

	private void updateWayImage()
	{
		Image img = imagenPoligono2D.copyImage(this.imgBase);

		if (this.pointer != null)
		{
			imagenPoligono2D.crearImagenLinea(this.pointer.x, 
					(int)this.pointer.getMaxY(), 
					(int)this.pointer.getMaxX(), 
					this.pointer.y, 
					3.0F, Color.BLACK, img);

			imagenPoligono2D.crearImagenLinea(this.pointer.x, 
					this.pointer.y, 
					(int)this.pointer.getMaxX(), 
					(int)this.pointer.getMaxY(), 
					3.0F, Color.BLACK, img);
		}

		this.canvas.setVisible(false);
		this.canvas.setIcon(new ImageIcon(img));
		this.canvas.setVisible(true);
	}

	private Dimension mouseShifting(MouseEvent e)
	{
		int x = e.getX() - this.mousePosition.x;
		int y = e.getY() - this.mousePosition.y;

		Map<String, Object> axes = this.parameters.getExtraParamters();

		if ((Boolean)axes.get( ID_PARAMETER_X_AXIS ) )
		{
			x *= -1;
		}

		if ((Boolean)axes.get( ID_PARAMETER_Y_AXIS ) )
		{
			y *= -1;
		}

		return new Dimension(x, y);
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

	/*
	 * (non-Javadoc)
	 * @see Activities.Activity#selectNextEnablePhase()
	 */
	@Override
	protected boolean selectNextEnablePhase()
	{
		boolean next = (this.state != this.ANSWER_STATE) 
				|| (super.isEndedTask);

		this.state = this.NON_ANSWER_STATE;

		if (next)
		{
			next = super.selectNextEnablePhase();
		}
		else
		{
			next = !next;
		}

		return next;
	}

	/*
	 * 
	 */
	@Override
	public void stopThread(int friendliness)
	{
		if (this.nextTrajectoryThread != null)
		{
			this.nextTrajectoryThread.stopThread( IStoppableThread.ForcedStop );
			this.nextTrajectoryThread = null;
		}

		super.stopThread(friendliness);
	}

	@Override
	protected void preSuspendActivity() {}

	@Override
	protected WarningMessage checkSpecifyParameters()
	{
		return new WarningMessage();
	}
}