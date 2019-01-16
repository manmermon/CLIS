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

import java.util.Comparator;

public class ArrayIndexComparator implements Comparator< Integer >
{
    private final Double[] array;

    public ArrayIndexComparator( double[] a)
    {
    	if( a == null )
    	{
    		throw new IllegalArgumentException( "Null Parameter." );
    	}
    	
        this.array = new Double[ a.length ];
        
        for( int i = 0; i < a.length; i++ )
        {
        	this.array[ i ] = new Double( a[ i ] );
        }		
    }
    
    public ArrayIndexComparator( Double[] a)
    {
    	if( a == null )
    	{
    		throw new IllegalArgumentException( "Null Parameter." );
    	}
    	
        this.array = a;		
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[ array.length ];
        for ( int i = 0; i < array.length; i++ )
        {
            indexes[ i ] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        return array[ index1 ].compareTo( array[ index2 ] );
    }
}