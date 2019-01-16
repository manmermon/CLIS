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

package GUI.MyComponents;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class componentTitleBorder implements Border, MouseListener, SwingConstants
{ 
		        int offset = 5; 
		     
		        Component comp; 
		        JComponent container; 
		        Rectangle rect; 
		        Border border; 
		        Point loc;
		        
		       public componentTitleBorder(Component comp, JComponent container, Border border)
		       { 
		            this.comp = comp; 
		            
		            if( comp != null )
		            {
		            	loc = comp.getLocation();
		            }
		            else
		            {
		            	loc = new Point();
		            }
		            
		            this.container = container; 
		            this.border = border; 
		            container.addMouseListener( this ); 
		        } 
		     
		        public boolean isBorderOpaque(){ 
		            return true; 
		        } 
		     
		        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
		        { 
		            Insets borderInsets = border.getBorderInsets(c);
		            
		            Insets insets = getBorderInsets( c );
		            
		            int temp = (insets.top - borderInsets.top) / 2;
		            
		            border.paintBorder(c, g, x, y+temp, width, height-temp); 
		            
		            Dimension size = comp.getPreferredSize(); 
		            
		            rect = new Rectangle(offset + loc.x, loc.y, size.width, size.height); 
		            
		            SwingUtilities.paintComponent( g, comp, (Container)c, rect ); 
		        } 
		     
		        public Insets getBorderInsets(Component c)
		        { 
		            Dimension size = comp.getPreferredSize(); 
		            Insets insets = border.getBorderInsets(c); 
		            insets.top = Math.max(insets.top, size.height); 
		            return insets; 
		        } 
		     
		        private void dispatchEvent(MouseEvent me)
		        { 
		            if(rect!=null && rect.contains(me.getX(), me.getY()))
		            { 
		                Point pt = me.getPoint(); 
		                pt.translate(-offset - loc.x, 0); 
		                comp.setBounds( rect ); 
		                comp.dispatchEvent(
		                		new MouseEvent(
		                						comp, me.getID() 
		                						, me.getWhen(), me.getModifiers() 
		                						, pt.x, pt.y, me.getClickCount() 
		                						, me.isPopupTrigger(), me.getButton())); 
		                if(!comp.isValid()) 
		                    container.repaint(); 
		            } 
		        } 
		     
		        public void mouseClicked(MouseEvent me){ 
		            dispatchEvent(me); 
		        } 
		     
		        public void mouseEntered(MouseEvent me){ 
		            dispatchEvent(me); 
		        } 
		     
		        public void mouseExited(MouseEvent me){ 
		            dispatchEvent(me); 
		        } 
		     
		        public void mousePressed(MouseEvent me){ 
		            dispatchEvent(me); 
		        } 
		     
		        public void mouseReleased(MouseEvent me){ 
		            dispatchEvent(me); 
		        }
}
