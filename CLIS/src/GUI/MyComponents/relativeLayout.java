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
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class relativeLayout implements LayoutManager2 
{	
	private List< Point2D.Double > pos = null;
	private List< Component > comps = null;	
	private Dimension prefDimension = null;
	
	public relativeLayout( )
	{
		this.pos = new ArrayList< Point2D.Double >();
		this.comps = new ArrayList<Component>();
	}
	
	public void addLayoutComponent(String loc, Component comp) 
	{
		Point2D.Double p = new Point2D.Double( 0, 0);
		
		this.pos.add( p );
		this.comps.add( comp );		
		this.prefDimension = null;
	}
	
	public void addLayoutComponent(Component comp, Object constraints ) 
	{		 
		if( constraints instanceof Point2D.Double )
		{
			this.pos.add( (Point2D.Double)constraints );
			this.comps.add( comp );
			this.prefDimension = null;
		}		
		else
		{
			this.addLayoutComponent( "", comp );
		}
	}

	
	public void layoutContainer(Container parent) 
	{	
		Dimension d = parent.getSize();
		Insets ins = parent.getInsets();		
		
		for( int i = 0; i < this.comps.size(); i++ )
		{
			Component c = this.comps.get( i );			
			Point2D.Double p = this.pos.get( i );
			
			int x = (int)( p.x * ( d.width - ins.left - ins.right ) + ins.left );
			int y = (int)( p.y * ( d.height - ins.top - ins.bottom ) + ins.top );
			
			c.setLocation( x, y );
		}		
	}

	
	public Dimension minimumLayoutSize(Container parent) 
	{   
        return preferredLayoutSize( parent );
	}

	
	public Dimension preferredLayoutSize(Container parent) 
	{	
		if( this.prefDimension == null) 
		{
			int width = 0;
			int height = 0;
			
			for( int i = 0; i < this.comps.size(); i++ )
			{
				Point2D.Double p = this.pos.get( i );
				Component c = this.comps.get( i );
				
				width += (int)( p.x * c.getPreferredSize().width );
				height += (int)( ( p.y ) * c.getPreferredSize().height ); 
			}
			
			this.prefDimension = new Dimension( width, height );
        }
		
        return new Dimension( this.prefDimension );
	}

	
	public void removeLayoutComponent(Component c ) 
	{
		int i = this.comps.indexOf( c );
		if( i >= 0 )
		{
			this.comps.remove( i );
			this.pos.remove( i );
		}
	}

	
	public float getLayoutAlignmentX(Container target) 
	{
		return 0;
	}

	
	public float getLayoutAlignmentY(Container target) 
	{
		return 0;
	}

	
	public void invalidateLayout(Container target) 
	{
		this.prefDimension = null;				
	}

	
	public Dimension maximumLayoutSize(Container target) 
	{
		return new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );
	}

}
