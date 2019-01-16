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

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class CircleBoton extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Shape figura;
	
	public CircleBoton( )
	{
		super( );
		//Igualamos las dimensiones para que el botón sea un círculo en vez de
	    // una elipse
	    Dimension tamano = getPreferredSize();
	    tamano.width = tamano.height = Math.max( tamano.width,tamano.height );
	    setPreferredSize( tamano );

	    // Hacemos que el JButton no pinte su fondo, de este modo podremos
	    // nosotros hacer que el color de fondo que se salga de la figura sea
	    // del mismo color que el fondo de la ventana
	    setContentAreaFilled( false );		
	}
	
	public CircleBoton( String text )
	{
		this( );
		
		super.setText( text );	
	}
	
	protected void paintComponent( Graphics g )
	{
		if( getModel().isArmed() )
		{			
			g.setColor( super.getBackground().darker() );
		}
		else
		{
			g.setColor( super.getBackground() );
		}
		
		g.fillOval( 0, 0, getSize().width-1, getSize().height-1 );
		
		super.paintComponent( g );
		
		//g.drawString(str, x, y)
	}
	
	protected void paintBorder( Graphics g )
	{
		g.setColor( Color.BLACK );
		g.drawOval( 0, 0, getSize().width - 1, getSize().height - 1 );
	}
	
	public boolean contains( int x, int y )
	{
		if( figura == null || !figura.getBounds().equals( super.getBounds() ) )
		{
			figura = new Ellipse2D.Double( 0, 0, getWidth(), getHeight() );
		}
		
		return figura.contains(x,y);
	}
}
