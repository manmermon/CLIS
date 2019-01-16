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

package Activities.Operaciones;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JPanel;

public class panelImage extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image = null;
	private boolean maintainProportions = false;
	private boolean imageCenter = false;
	


	public panelImage( )
	{
		super( );
	}
	
	public panelImage( Image img )
	{
		this( );
		
		this.image = img;
	}
	


	public panelImage( boolean isDoubleBuffered )
	{
		super( isDoubleBuffered );
	}
	
	public panelImage( Image img, boolean isDoubleBuffered )
	{
		super( isDoubleBuffered );
		
		this.image = img;
	}
	
	public void setImage( Image img )
	{
		this.image = img;
		
		super.repaint();
	}
	
	public Image getImage()
	{
		return this.image;
	}
	
	public void setImageCenter( Boolean center )
	{
		this.imageCenter = center;
	}
	
	public void setMaintainProportions( boolean maintain )
	{
		this.maintainProportions = maintain;
	}
	
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		if( this.image != null )
		{
			double wImg = this.image.getWidth( null );
			double hImg = this.image.getHeight( null );
			
			Insets pad = super.getInsets();
			
			double proporcion = 1;
			if( this.maintainProportions )
			{
				proporcion = wImg / hImg;
			}
			
			Dimension d1 = new Dimension( super.getSize() );
			d1.width = d1.width - ( pad.left + pad.right );
			d1.height = d1.height - ( pad.top + pad.bottom );
			Dimension d2 = new Dimension( (int)( d1.height * proporcion ), d1.height  );
			if( proporcion > 1)
			{
				d2.width = d1.width;
				d2.height = (int)( d1.width / proporcion );
			}
			
			int x = pad.left;
			int y = pad.top;
			
			if( this.imageCenter )
			{
				x = x + ( int) (( d1.width - d2.width ) / 2.0 );
				y = y + ( int) (( d1.height - d2.height ) / 2.0 );
			}
			
			g.drawImage( this.image, x, y, d2.width, d2.height, null );
		}
	}
}


																			  
								  
							   
   