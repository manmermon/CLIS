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

import Activities.Operaciones.opUnirPuntos;
import Auxiliar.WarningMessage;
import GUI.MyComponents.CircleBoton;
import GUI.MyComponents.relativeLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Arrays;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class SortedDotsTask extends Activity
{
  private static ActivityRegInfo activityID = new ActivityRegInfo("Activity", Activity.class, true);
  
  public static final String ID = "Sorted dots";
  
  public static void registerActivity()
  {
    activityID.setID( ID );
    activityID.setActivityClass(SortedDotsTask.class);
    ActivityRegister.registerActivity(activityID);
  }
  
  private opUnirPuntos op = null;
  
  private CircleBoton previousSelectedBoton = null;
  
  private Point2D.Double[] ptos = null;
  
  private SortedDotsTask()
  {
    super.setName( this.getClass().getCanonicalName());
    
    super.reportActive = true;
    
    this.setPanelTask();
  }
  
  public static Activity getInstance()
  {
    if (task == null)
    {
      task = new SortedDotsTask();
    }
    
    return task;
  }
  
  protected void makeSpecificTask(int dificultad) throws Exception
  {
    this.previousSelectedBoton = null;
    
    makeSpecificTaskAux(dificultad);
  }
  
  private void makeSpecificTaskAux(int dificultad)
  {
    this.panelTask.setVisible(false);
    this.panelTask.removeAll();
    
    this.dificultad = dificultad;
    
    int numPtos = 6 * (dificultad + 1);
    
  //Normalizamos el plano a un cuadrado de lado = 1.0

    this.op = new opUnirPuntos(numPtos);
    
    this.op.generarTarea();
    this.ptos = this.op.getTarea();
    
    this.reportActivity = Arrays.deepToString(this.ptos);
    this.reportActivity = this.reportActivity.replace("Point2D.Double", "");
    this.reportAnswer = "NaN";
    
    updateDotPosition();
    
    this.panelTask.setVisible(true);
  }
  
  private void updateDotPosition()
  {
    if (this.ptos != null)
    {
      final int numPtos = this.ptos.length;
      
      final int dificultad = this.dificultad;
      
      double radio = 1.0D / Math.sqrt(numPtos * 2) / 2.0D;
      
      this.panelTask.removeAll();
      
      Dimension d = (Dimension)this.parameters.getExtraParamters().get(  Activity.ID_ACTIVITY_PANEL_SIZE );
      
      int r = (int)Math.round(d.width * radio);
      if ((int)(d.height * radio) < r)
      {
        r = (int)(d.height * radio);
      }
      
      for (int i = 0; i < this.ptos.length; i++)
      {
        Point2D.Double pto = this.ptos[i];
        final int num = i;
        final CircleBoton b = new CircleBoton( num + "" );
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(new LineBorder(Color.BLACK));
        b.setBackground(Color.BLUE);
        b.setForeground(Color.WHITE);
        b.setFont(new Font(Font.DIALOG, Font.BOLD, r / 2));
        b.setSize(r, r);
        b.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            b.setBackground(Color.BLUE.darker().darker());
            
            if (num > 0)
            {
              if (previousSelectedBoton != null)
              {
                if (previousSelectedBoton.getText().equals( (num - 1) + "" ) )
                {
                  if (num != numPtos - 1)
                  {
                    if (dificultad < 3)
                    {
                      Component c = previousSelectedBoton;
                      
                      Graphics2D g = (Graphics2D)panelTask.getGraphics();
                      
                      g.setColor(Color.BLUE);
                      Stroke org = g.getStroke();
                      g.setStroke(new BasicStroke(3.0F));
                      g.drawLine(c.getX() + c.getWidth() / 2, c.getY() + c.getHeight() / 2, 
                        b.getX() + b.getWidth() / 2, b.getY() + b.getHeight() / 2);
                      g.setStroke(org);
                      panelTask.paintComponents(g);
                    }
                    
                    previousSelectedBoton = b;
                  }
                  else
                  {
                    reportAnswer = "true";
                    
                    activityEnded();
                    notifiedAnswer(true);
                  }
                }
                else if (!previousSelectedBoton.getText().equals( num + "" ) )
                {
                  reportAnswer = "false";
                  
                  activityEnded();
                  notifiedAnswer(false);
                }
              }
              else
              {
                reportAnswer = "false";
                
                activityEnded();
                notifiedAnswer(false);
              }
              
            }
            else {
              previousSelectedBoton = b;
            }
            
          }
        });
        this.panelTask.add(b, pto);
      }
    }
  }

	@Override
  protected void setPanelTask()
  {
    if (this.panelTask == null)
    {
      this.panelTask = new JPanel();
      
      this.panelTask.setVisible(false);
      
      this.panelTask.removeAll();
      
      this.panelTask.setLayout(new relativeLayout());
      
      this.panelTask.addComponentListener(new ComponentAdapter()
      {
        Timer controlTimer = null;
        int DELAY = 100;
        

        public void componentResized(ComponentEvent e)
        {
          if (op != null)
          {
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
                    
                    panelTask.setVisible(false);
                    
                    Dimension size = panelTask.getSize();
                    
                    parameters.addExtraParameter( Activity.ID_ACTIVITY_PANEL_SIZE, size);
                    
                    updateDotPosition();
                    
                    panelTask.setVisible(true);
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
    
    this.panelTask.setVisible(true);
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
  

	@Override
  protected WarningMessage checkSpecifyParameters()
  {
    return new WarningMessage();
  }
}
