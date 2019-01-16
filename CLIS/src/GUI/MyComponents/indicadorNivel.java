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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class indicadorNivel extends JComponent 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int HORIZONTAL_OR = 0;
	public static final int VERTICAL_OR = 1;
	
	private int numNiveles = 2;
	private Color[] levelsColors = { Color.blue, new Color( 238, 238, 238)};
	private Insets inset = null;
	private int widtStick = 11;
	private int value = 0;
	
	private int maximo = 100;
	private int minimo = 0;
	
	private boolean invertido = false;
	private boolean editable = false;
	private boolean paintValue = true;
	
	private int orientacion = HORIZONTAL_OR;
	
	private int[] niveles = null;
	
	public indicadorNivel( )
	{
		super();
		
		this.numNiveles = 2;
		
		this.niveles = new int[ this.numNiveles - 1 ];
		int n = this.minimo;
		for( int i = 0; i < this.numNiveles - 1; i++ )
		{
			this.niveles[ i ] = n + ( this.maximo - this.minimo ) / this.numNiveles;
		}
		//this.ins = new Insets( 20, 0, 20, 0);
		
		this.addInteractions();
	}
	
	public indicadorNivel( int levels )
	{
		super();
		
		if( levels < 2 )
		{
			throw new IllegalArgumentException( "El minimo numero de divisiones es 2.");
		}
		
		//this.ins = new Insets( 20, 0, 20, 0);
		
		this.numNiveles = levels;
		this.levelsColors = new Color[ levels ];
		
		this.niveles = new int[ levels - 1 ];
		int n = this.minimo;
		for( int i = 0; i < levels - 1; i++ )
		{
			n = n + ( this.maximo - this.minimo ) / levels;
			this.niveles[ i ] = n;
		}
		int c = 0;
		for( int i = 0; i < levels; i++ )
		{	
			c = Color.HSBtoRGB( i * 1.0F / levels , 1, 1);
			this.levelsColors[ i ] = new Color( c );
		}		
		
		this.addInteractions();
	}	
	
	public indicadorNivel( int[] levels )
	{
		if( levels.length == 0 )
		{
			throw new IllegalArgumentException( "Array vacio.");
		}
			
		this.numNiveles = levels.length;
		System.arraycopy( levels, 0, this.niveles, 0, levels.length );
		this.levelsColors = new Color[ this.numNiveles + 1];	
	
		int c = 0;
		for( int i = 0; i < this.numNiveles; i++ )
		{	
			c = Color.HSBtoRGB( i * 1.0F / this.numNiveles , 1, 1);
			this.levelsColors[ i ] = new Color( c );
		}		
		
		this.addInteractions();
	}
	
	public int[] getLevels( )
	{
		return this.niveles;
	}
	
	public void setLevels( int[] levels )
	{
		if( levels.length != this.niveles.length )
		{
			throw new IllegalArgumentException( "El tamanho array debe ser = " + this.niveles.length + ".");
		}
		
		System.arraycopy( levels, 0, this.niveles, 0, levels.length );
	}
	
	public void setColorLevels( Color[] colors)
	{
		if( colors.length != this.levelsColors.length )
		{
			throw new IllegalArgumentException( "Tamanho del array de colores distinto al número de niveles.");
		}
		
		for( int i = 0; i < colors.length; i++)
		{
			Color c = colors[ i ];
			if( c == null )
			{
				throw new IllegalArgumentException( "Color Null");
			}
			
			this.levelsColors[ i ] = c;
		}
	}
		
	public void setOrientation( int orientation )
	{
		if( orientation != HORIZONTAL_OR && orientation != VERTICAL_OR )
		{
			this.orientacion = HORIZONTAL_OR;
		}
		else
		{
			this.orientacion = orientation;
		}
	}
	
	public void setInverted( boolean invert)
	{
		this.invertido = invert;
	}
	
	public void setValue( int val )
	{
		int v = this.value;
		this.value = val;
		
		if( this.value < this.minimo )
		{
			this.value = this.minimo;
		}
		else if( this.value > this.maximo )
		{
			this.value = this.maximo;
		}
		
		if( v != this.value )
		{
			this.repaint();
		}
	}
	
	public void setMaximum( int max )
	{	
		if( (this.maximo - this.minimo) != 0 )
		{
			double escala = (1.0 * Math.abs( max - this.minimo ) / Math.abs( this.maximo - this.minimo));
			for( int i = 0; i < this.niveles.length; i++ )
			{
				this.niveles[ i ] = this.minimo + (int)( this.niveles[ i ] * escala); 
			}
			
			this.setValue( this.minimo + (int)( this.value * escala ) );
		}
		
		this.maximo = max;		
	}
	
	public void setMinimum( int min )
	{
		if( (this.maximo - this.minimo) != 0 )
		{	
			double escala = (1.0 * Math.abs( this.maximo - min ) / Math.abs( this.maximo - this.minimo));
			
			for( int i = 0; i < this.niveles.length; i++ )
			{
				this.niveles[ i ] = min + (int)( this.niveles[ i ] * escala);
			}
			
			this.setValue( min + (int)( this.value * escala ) );
		}
		
		this.minimo = min;		
	}
	
	public void setPaintedString( boolean s )
	{
		this.paintValue = s;
	}
	
	public void setEditable( boolean edit )
	{
		this.editable = edit;
	}
	
	public Insets getInsets()
	{
		return this.inset;
	}	
	
	public int getNumLevels()
	{
		return this.numNiveles;
	}

	public int getValue( )
	{
		return this.value;
	}
	
	public int getMaximum()
	{
		return this.maximo;
	}
	
	public int getMinimum()
	{
		return this.minimo;
	}
	
	public boolean isEditable()
	{
		return this.editable;
	}
			
	public void repaint()
	{
		this.autoDefinirInsets();
		
		super.repaint();
	}
	
	protected void paintComponent( Graphics g )
	{
		super.paintComponents( g );
		//System.out.println("indicadorNivel.paintComponent(): size = "+super.getSize());
		this.autoDefinirInsets();		
	
		Point loc = new Point( this.inset.left, this.inset.top );
		int Width = getWidth();
		int Heigh = getHeight();
		
		if( super.isEnabled() )
		{
			if( this.orientacion == HORIZONTAL_OR )
			{
				int[] levels = new int[ this.niveles.length ];
				
				int ini = 0;
				int fin = levels.length - 1;
				
				int pad = this.widtStick / this.numNiveles;
				if( pad == 0 )
				{
					pad = 1;
				}
				int relleno = 0;
				
				if( this.invertido )
				{				
					for( int z = this.niveles.length - 1; z >= 0; z-- )
					{					
						levels[ this.niveles.length - 1 - z ] = this.niveles[ z ];
					}
				}
				else
				{
					for( int z = 0; z < this.niveles.length; z++ )
					{					
						levels[ z ] = this.niveles[ z ];
					}
				}
				
				for( int i = ini; i <= fin; i++ )
				{
					Point p = this.valuePositionPixel( levels[ i ] * 1.0);					
					Color c = this.levelsColors[ i ];
					if( this.invertido )
					{
						c = this.levelsColors[ levelsColors.length - 1 - i ];
					}
					
					this.drawRectDegradado(g, loc.x, loc.y, Math.abs( loc.x - (p.x + pad * ( i + 1)) ), Heigh - this.inset.top - this.inset.bottom, c, false);
					//System.out.println("indicadorNivel.paintComponent(): width = " + Math.abs( loc.x - (p.x + pad* ( i + 1))));
					
					loc.x = p.x + pad * ( i + 1);				
					loc.y = p.y;	
					
					relleno += pad;
					if( relleno >= this.widtStick )
					{
						pad = 0;
					}
				}
				
				Color c = this.levelsColors[ this.numNiveles - 1 ];
				if( this.invertido )
				{
					c = this.levelsColors[ 0 ];
				}
				
				pad = this.widtStick - relleno;
				if( pad < 0 )
				{
					pad = 0;
				}			
				int w = Width - loc.x - this.inset.right;
				
				this.drawRectDegradado(g, loc.x, loc.y, w , Heigh - this.inset.top - this.inset.bottom, c, false);
				//System.out.println("indicadorNivel.paintComponent(): width = "+w);
			}
			else
			{
				int[] levels = new int[ this.niveles.length ];
				
				int ini = 0;
				int fin = levels.length - 1;
				
				int pad = this.widtStick / this.numNiveles;
				if( pad == 0 )
				{
					pad = 1;
				}
				int relleno = 0;
				
				if( !this.invertido )
				{				
					for( int z = this.niveles.length - 1; z >= 0; z-- )
					{					
						levels[ this.niveles.length - 1 - z ] = this.niveles[ z ];
					}
				}
				else
				{
					for( int z = 0; z < this.niveles.length; z++ )
					{					
						levels[ z ] = this.niveles[ z ];
					}
				}
				
				for( int i = ini; i <= fin; i++ )
				{
					Point p = this.valuePositionPixel( levels[ i ] * 1.0);
					Color c = this.levelsColors[ i ];
					if( this.invertido )
					{
						c = this.levelsColors[ levelsColors.length - 1 - i ];
					}
					
					this.drawRectDegradado(g, loc.x, loc.y, Width - this.inset.left - this.inset.right, Math.abs( loc.y - (p.y + pad * ( i + 1)) ), c, true);
					//System.out.println("indicadorNivel.paintComponent(): width = " + Math.abs( loc.y - (p.y + pad* ( i + 1))));
					
					loc.x = p.x;
					loc.y = p.y + pad * ( i + 1);	
					
					relleno += pad;
					if( relleno >= this.widtStick )
					{
						pad = 0;
					}
				}
				
				Color c = this.levelsColors[ this.numNiveles - 1 ];
				if( this.invertido )
				{
					c = this.levelsColors[ 0 ];
				}
				
				pad = this.widtStick - relleno;
				if( pad < 0 )
				{
					pad = 0;
				}			
				int h = Heigh - loc.y - this.inset.top;
				
				this.drawRectDegradado(g, loc.x, loc.y, Width - this.inset.left - this.inset.right, h, c, true);
				//System.out.println("indicadorNivel.paintComponent(): width = " + h);
			}
		}
		else
		{
			boolean ver = false;
			if( this.orientacion == VERTICAL_OR )
			{
				ver = true;
			}
			this.drawRectDegradado( g , this.inset.left, this.inset.top, Width - this.inset.left - this.inset.right, Heigh - this.inset.top - this.inset.bottom, Color.lightGray, ver);
		}
		Color c = Color.black;
		if( !isEnabled() )
		{
			c = Color.gray;
		}		
		g.setColor( c );		
		g.drawRect( this.inset.left, this.inset.top, getWidth() - this.inset.left - this.inset.right, getHeight() - this.inset.top - this.inset.bottom );
		
		this.drawStick( g );
		
		if( this.paintValue )
		{
			this.drawValue( g );
		}
	}
	

	private void addInteractions()
	{
		super.addMouseMotionListener( new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent arg0) 
			{
				if( editable && isEnabled() )
				{
					if( orientacion == HORIZONTAL_OR )
					{
						int pxStick = arg0.getX();
						Insets i = getInsets();
						
						if( pxStick < i.left )
						{
							pxStick = i.left;
						}
						else if( pxStick > getWidth() - i.right - widtStick  )
						{
							pxStick = getWidth() - i.right - widtStick ;
						}
						
						setValue( getValuePx( pxStick ));					
					}
					else
					{
						int pyStick = arg0.getY();
						Insets i = getInsets();
						
						if( pyStick < i.top )
						{
							pyStick = i.top;
						}
						else if( pyStick > getHeight() - i.bottom - widtStick)
						{
							pyStick = getHeight() - i.bottom - widtStick;
						}
						
						setValue( getValuePx( pyStick ));
					}	
				}
			}

			public void mouseMoved(MouseEvent arg0) {
 
				
			}
			
		});
	}
		
	private void setInsets( int top, int left, int bottom, int right )
	{
		this.inset = new Insets( top, left, bottom, right);
	}
	
	private void autoDefinirInsets()
	{
		int w = this.getWidth(), h = super.getHeight();
		
		int top = ( int) ( h * 0.1);
		int bottom = top;
		int left = 0, right = 0;
		if( orientacion == VERTICAL_OR )
		{
			left = right = ( int) ( w * 0.1);;
			top = bottom = 0;
		} 
		
		if( top > 20 )
		{
			top = bottom = 20;
		}
		
		if( left > 20 )
		{
			left = right = 20;
		}
		
		this.setInsets( top, left, bottom, right);
	}
	
	private void drawStick( Graphics g )
	{
		Point coord = this.stickPosition( this.value );
		
		int w = this.widtStick;
		int h = super.getHeight();
		boolean vertical = true;
		
		if( this.orientacion == VERTICAL_OR )
		{
			w = super.getWidth();
			h = this.widtStick;
			vertical = false;
		}
		
		Color c = Color.blue;
		if( !isEnabled() )
		{
			c = Color.lightGray;
		}
		this.drawRectDegradado(g, coord.x, coord.y, w, h, c, vertical);
		
		c = Color.black;
		if( !isEnabled() )
		{
			c = Color.gray;
		}
		g.setColor( c );
		g.drawRect( coord.x, coord.y, w, h);

	}
	
	private void drawRectDegradado( Graphics g, int x, int y, int width, int height, Color c, boolean vertical )	
	{ 
		if( isEnabled() )
		{
			float[] hsb = Color.RGBtoHSB( c.getRed(), c.getGreen(), c.getBlue(), null);
			
			float minSat = 0.2F; 
			if( vertical )
			{
				for( int i = 0; i < width; i++ )
				{
					float s = (i * ( 1.0F - minSat) / width) + minSat;
					g.setColor( new Color( Color.HSBtoRGB( hsb[ 0 ], s, hsb[ 2])));
					g.drawLine( x + i, y, x + i, y + height);
				}
			}
			else
			{
				for( int i = 0; i < height; i++ )
				{
					float s = (i * ( 1.0F - minSat) / height) + minSat;
					g.setColor( new Color( Color.HSBtoRGB( hsb[ 0 ], s, hsb[ 2])));
					g.drawLine( x, y + i, x + width, y + i);
				}
			}
		}
		else
		{
			g.setColor( c );
			g.fillRect( x, y, width, height);
		}
	}

	private void drawValue( Graphics g)
	{
		Color c = Color.black;
		if( !isEnabled() )
		{
			c = Color.gray;
		}
		
		g.setFont( new Font( Font.DIALOG, Font.BOLD, 12));
		g.setColor( c );			
		String cad = "" + this.value;
		int xs = ( getWidth() / 2 ) - cad.length() * 3 ;
		int ys = ( getHeight() / 2 ) + 3;
		
		if( xs < 0 )
		{
			xs = 0;
		}
		
		if( ys < 0 )
		{
			ys = 0;
		}
		
		g.drawString( cad, xs , ys);
		
		xs = xs + 2;
		
		g.drawString( cad, xs , ys);
		
		ys = ys + 2;
		g.drawString( cad, xs , ys);			
		
		xs = xs - 2;
		g.drawString( cad, xs , ys);
		
		xs = xs + 1;
		ys = ys - 1;
		
		c = Color.white;
		if( !isEnabled() )
		{
			c = Color.lightGray;
		}
		
		g.setColor( c );						
		g.drawString( cad, xs , ys);
	}
	
	private Point valuePositionPixel( double val )
	{
		Point loc = new Point(); 
		
		loc.x = this.inset.left + (int)(( super.getWidth() - this.inset.left - this.inset.right - this.widtStick) * ( ( val - this.minimo ) / ( this.maximo - this.minimo)));
		loc.y = this.inset.top;
		
		if( this.invertido )
		{	
			loc.x = this.inset.left + (int)(( super.getWidth() - this.inset.left - this.inset.right - this.widtStick) * ( ( this.maximo - val ) / ( this.maximo - this.minimo)));
		}
		
		if( loc.x < this.inset.left )
		{
			loc.x = this.inset.left;
		}
		else if( loc.x > super.getWidth() - this.inset.right - this.widtStick )
		{
			  loc.x = super.getWidth() - this.inset.right - this.widtStick;
		}
				 		
		if( this.orientacion == VERTICAL_OR )
		{
			loc.x = this.inset.left;
			loc.y = this.inset.top + (int)(( super.getHeight() - this.inset.top - this.inset.bottom - this.widtStick) * ( (val - this.minimo) / ( this.maximo - this.minimo)));
			//loc.y = super.getHeight() - this.ins.bottom  - this.widtStick - (int)(( super.getHeight() - this.ins.top - this.ins.bottom - this.widtStick) * ( val / ( this.max - this.min)));
			
			if( !this.invertido )
			{
				loc.y = this.inset.top + (int)(( super.getHeight() - this.inset.top - this.inset.bottom - this.widtStick) * ( (this.maximo - val) / ( this.maximo - this.minimo)));
			}
			
			if( loc.y > super.getHeight() - this.inset.bottom - this.widtStick )
			{
				loc.y = super.getHeight() - this.inset.bottom - this.widtStick;
			}
			else if( loc.y < this.inset.top )
			{
				  loc.y = this.inset.top;
			}
		}
		
		return loc;
	}
	
	private Point stickPosition( double val )
	{	
		Point loc = this.valuePositionPixel( val );
		
		if( this.orientacion == HORIZONTAL_OR )
		{
			loc.y = 0;
		}
		else
		{
			loc.x = 0;
		}		
		
		return loc;
	}
	
	private int getValuePx( int coord )
	{
		int w = super.getWidth();
		int h = super.getHeight();
		
		double val =  1.0 * ( coord - this.inset.left) / ( w - this.inset.left - this.inset.right - this.widtStick ); 
		val = this.minimo + val * ( this.maximo - this.minimo ) ;		
				
		if( coord > w - this.inset.right )
		{
			val = this.maximo;
		}
		else if( coord < this.inset.left )
		{
			val = this.minimo;
		}
		
		if( this.invertido )
		{
			val =  this.maximo - (val - this.minimo);
			
			if( coord > w - this.inset.right - this.widtStick)
			{
				val = this.minimo;
			}
			else if( coord < this.inset.left )
			{
				val = this.maximo;
			}
		}
		
		if( this.orientacion == VERTICAL_OR )
		{
			val =  1.0 * ( coord - this.inset.top ) / ( h - this.inset.bottom - this.inset.top - this.widtStick ); 
			val = maximo - val * ( this.maximo - this.minimo );
			
			if( coord < this.inset.top )
			{
				val = this.maximo;
			}
			else if( coord > h - this.inset.bottom - this.widtStick )
			{
				val = this.minimo;
			}
			
			if( this.invertido )
			{
				val = minimo + maximo - val;
				
				if( coord < this.inset.top )
				{
					val = this.minimo;
				}
				else if( coord > h - this.inset.bottom - this.widtStick )
				{
					val = this.maximo;
				}
			}
		}

		//System.out.println(".mouseDragged() -> val="+val);
		
		return (int)Math.floor( val );
	}
}
