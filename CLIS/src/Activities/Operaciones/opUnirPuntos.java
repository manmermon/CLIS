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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class opUnirPuntos 
{
	private Point2D.Double[] ptos = null;
	private double radio = 0.1;
	
	public opUnirPuntos( int numPtos )
	{	
		if( numPtos <= 0)
		{
			throw new IllegalArgumentException( "El numero de puntos debe ser >0." );
		}
		
		
		this.ptos = new Point2D.Double[ numPtos ];		
		this.radio = ( 1.0 / Math.sqrt( numPtos * 2 ) ) / 2.0;
	}
	
	public void generarTarea()
	{	
		int numCeldasFilas = (int)Math.round( 1.0 / this.radio );		
		List< Point2D.Double > malla = this.generarMalla( this.radio );		
		List< Integer > indicesMalla = new ArrayList< Integer >();
		for( int i = 0; i < malla.size(); i++ )
		{
			indicesMalla.add( i );
		}
		
		for( int i = 0; i < this.ptos.length; i++ )
		{
			int posIndice = (int)Math.round(  Math.random() * indicesMalla.size() );
			if( posIndice == indicesMalla.size() )
			{
				posIndice--;
			}
			
			int posMalla = indicesMalla.get( posIndice );
			
			this.ptos[ i ] = malla.get( posMalla );
			
			//Borramos los puntos vecinos.
			int f = (int)( posMalla / numCeldasFilas );
			int c = posMalla - f * numCeldasFilas;
			
			for( int j = 1; j > -2; j-- )
			{
				for( int k = 1; k > -2; k-- )
				{
					int filaBorrado = f + j;
					int colBorrado = c + k;
					
					if( filaBorrado >= 0 && colBorrado >= 0 
							&& filaBorrado < numCeldasFilas && colBorrado < numCeldasFilas )
					{
						int p = indicesMalla.indexOf( filaBorrado * numCeldasFilas + colBorrado );
						
						if( p > -1 )
						{
							indicesMalla.remove( p );
						}
					}
				}
			}			
		}			
	}
	
	public Point2D.Double[] getTarea()
	{
		return this.ptos;
	}
	
	private List< Point2D.Double > generarMalla( double tamCelda )
	{
		if( tamCelda <= 0.0 || tamCelda > 1.0 )
		{
			throw new IllegalArgumentException( "La dimension de las celdas debe ser >0 y <=1.0." );
		}
		
		int numCeldas = (int)Math.round( 1.0 / tamCelda );
		
		List< Point2D.Double > malla = new ArrayList< Point2D.Double >(); 
		
		for( int i = 0; i < numCeldas; i++ )
		{
			double x = tamCelda * i;
			for( int j = 0; j < numCeldas; j++ )
			{
				malla.add( new Point2D.Double( x, j * tamCelda ) );
			}
		}
		
		return malla;
	}
	
}
