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
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

import GUI.MyComponents.ArrayIndexComparator;

public class opTrajectory 
{
	private int dificultad = 0;
	
	private double threshAngle = Math.PI * 35 / 180;
	private double threshDistance = Math.sin( threshAngle )  / 10;
	private long limitTime = 700;
	
	public opTrajectory( int difficulty )
	{
		if( difficulty <= 0 )
		{
			this.dificultad = 0;
		}
		else if( difficulty >= 4 )
		{
			this.dificultad = 4;
		}
		else
		{
			this.dificultad = difficulty;
		}		
	}
	
	public void setDificultad( int difficulty )
	{
		if( difficulty <= 0 )
		{
			this.dificultad = 0;
		}
		else if( difficulty >= 4 )
		{
			this.dificultad = 4;
		}
		else
		{
			this.dificultad = difficulty;
		}
	}
	
	public void setThresholdAngle( double tA )
	{
		this.threshAngle = tA;
	}
	
	public void setThresholdDistance( double tD )
	{
		this.threshDistance = tD;
	}
	
	public int getDificultad()
	{
		return this.dificultad;
	}
	
	public double[][] creatingTrayectory()
	{
		int nPoints = 15 + 15 * this.dificultad;
		
		List< Point2D.Double > points = new ArrayList<Point2D.Double>();
		
		boolean end = false;
		
		while( !end )
		{
			points.clear();
			
			Point2D.Double point;			
			for( int i = 0; i < 2; i++ )
			{
				point = new Point2D.Double( Math.random(), Math.random() );
				points.add( point );
			}
			
			long t = System.currentTimeMillis();
			do
			{
				point = new Point2D.Double( Math.random(), Math.random() );
			}
			while( point.distance( points.get( 0 ) ) < this.threshDistance * 2 );
		
			int i = 3;
			for( ; i < nPoints && ( System.currentTimeMillis() - t ) <= this.limitTime; i++ )
			{
				do
				{
					point = new Point2D.Double( Math.random(), Math.random() );
				}
				while( ( System.currentTimeMillis() - t ) <= this.limitTime && 
						( point.distance( points.get( 0 ) ) < this.threshDistance * 2 
						  || point.distance( points.get( points.size() - 1 ) ) < this.threshDistance * 2) );
				
				//Double minDistance = Double.MAX_VALUE;
				Integer[] indexDistanceSegments = this.getDistancesSegments( point, points );
				boolean cross = true;
				boolean okAngle = true;
				boolean okDistance = true;
				int j = 0;
				while( j < indexDistanceSegments.length
						&& ( cross || !okAngle || !okDistance  )
						&& ( System.currentTimeMillis() - t ) <= this.limitTime )
				{		
					int index = indexDistanceSegments[ j ] + 1;
					points.add( index, point );
										
					int aux = index + 2;
					if( aux >= points.size() )
					{
						aux = points.size() - 1;
					}
					
					for( int k = aux; k > aux - 3 
										&& k >= 2 
										&& okAngle; k-- )
					{
						okAngle = minSegmentAngles( points.get( k - 2), points.get( k - 1 ), points.get( k ), this.threshAngle );
					}
									
					if( okAngle )
					{
						cross = checkCrossSegments( points );
						
						if( !cross )
						{
							okDistance = checkMinDistanceSegments( points, this.threshDistance );
						}
					}
					
					if( !okAngle || cross || !okDistance )
					{
						points.remove( index );
					}
					
					j++;
				}
								
				if( !okAngle || cross || !okDistance )
				{	
					i--;
					//System.out.println("Not OK.");
				}
			}
			
			end = i >= nPoints;			
		}
		
		points = duplicateTrajectory( points );
		
		double[][] trajectory = new double[ points.size() ][ 2 ];
		Iterator< Point2D.Double > it = points.iterator();
		int i = 0;
		while( it.hasNext() )
		{
			Point2D.Double p = it.next();
			trajectory[ i ][ 0 ] = p.x;
			trajectory[ i ][ 1 ] = p.y;
			//System.out.println( "[ "+p.x+", "+p.y+" ]");
			i++;
		}	
		
		this.normalTrajectory( trajectory );
			
		return trajectory;
	}
	
	private List< Point2D.Double > duplicateTrajectory( List< Point2D.Double > pts )
	{	
		List< Point2D.Double > copyPts = new ArrayList<Point2D.Double>( );
		
		Iterator< Point2D.Double > it = pts.iterator();
				
		double P1_P2;
		double P2_P3;
		double P1_P3;
		double sum;
		double x, y, x2, y2, norm;
		double tdistance = this.threshDistance * 0.5;
		Point2D.Double newP2 = null, tmp = null;
		Point2D.Double previousNewPoint = null;
		//Point2D.Double previousNewPoint2 = null;		
		Point2D.Double p0 = null, p1 = null, p2 = null, p3 = null;
		
		if( it.hasNext() )
		{
			p1 = it.next();
			previousNewPoint = p1;
			p0 = p1;
		}
		
		if( it.hasNext() )
		{
			p2 = it.next();
		}
		
		//Normal vector
		x = 1;
		y = p2.getY() - p1.getY();
		if( y != 0 )
		{
			y = -( p2.getX() - p1.getX() ) / y; 
		}
		else
		{
			x = 0; 
			y = 1; 
		}
		
		x = 1 / Math.sqrt( x + y * y );
		y = y * x * tdistance;
		x *= tdistance;
		
		previousNewPoint = new Point2D.Double( p1.getX() + x, p1.getY() + y );
		copyPts.add( previousNewPoint );
		
		while( it.hasNext() )
		{	
			p3 = it.next();
			
			P1_P2 = p1.distance( p2 );
			P2_P3 = p2.distance( p3 );
			P1_P3 = p1.distance( p3 );
			sum = P1_P2 + P2_P3 + P1_P3;
						
			x = ( p3.getX() * P1_P2 + p1.getX() * P2_P3 + p2.getX() * P1_P3 ) / sum;
			y = ( p3.getY() * P1_P2 + p1.getY() * P2_P3 + p2.getY() * P1_P3 ) / sum;
			
			x = p2.getX() - x;
			y = p2.getY() - y;
			
			norm = Math.sqrt( x * x + y * y );
			x = ( x / norm ) * tdistance;
			y = ( y / norm ) * tdistance;
			tmp = new Point2D.Double( p2.getX() + x, p2.getY() + y );
			
			x2 = p2.x - p1.x;
			y2 = p2.y - p1.y;
			
			newP2 = crossPoint( p2, tmp, 
								previousNewPoint, 
								new Point2D.Double( previousNewPoint.x + x2, 
													previousNewPoint.y + y2) );
			
			if( crossCheck( previousNewPoint, newP2, p1, p2 )
					|| crossCheck( previousNewPoint, newP2, p2, p3 ) 
					|| crossCheck( previousNewPoint, newP2, p0, p1 ) )
			{
				tmp.setLocation( p2.getX() - x, p2.getY() - y);
				
				newP2 = crossPoint( p2, tmp, 
									previousNewPoint, 
									new Point2D.Double( previousNewPoint.x + x2, 
														previousNewPoint.y + y2) );
				/*
				if( previousNewPoint2 != null 
						&&
						( crossCheck( previousNewPoint, newP2, p1, p2 )
							|| crossCheck( previousNewPoint, newP2, p2, p3 ) 
							|| crossCheck( previousNewPoint, newP2, p0, p1 ) ) )
				{
					System.out.println("opTrajectory.duplicateTrajectory() CROSS");
					
					newP2.setLocation( p2.getX() + x, p2.getY() + y);
					
					x2 = p2.getX() - p1.getX();
					y2 = p2.getY() - p1.getY();
					
					Point2D.Double cross = crossPoint( newP2, new Point2D.Double( newP2.getX() + x2, newP2.getY() + y2 ), 
														previousNewPoint2, previousNewPoint );
					double xmin = previousNewPoint2.getX();
					double xmax = previousNewPoint.getX(); 
					if( xmin > xmax )
					{
						xmin = xmax;
						xmax = previousNewPoint2.getX();
					}
					
					if( cross.getX() >= xmin && cross.getX() <= xmax )
					{
						newP2.setLocation( p2.getX() - x, p2.getY() - y);
												
						cross = crossPoint( newP2, new Point2D.Double( newP2.getX() + x2, newP2.getY() + y2 ), 
															previousNewPoint2, previousNewPoint );
					}
					
					copyPts.set( copyPts.size() - 1, cross );
					
					previousNewPoint = cross;
				}
				*/
			}
			
			//previousNewPoint2 = previousNewPoint;
			previousNewPoint = newP2;
						
			copyPts.add( newP2 );
			
			if( it.hasNext() )
			{
				p0 = p1;
				p1 = p2;
				p2 = p3;
			}
		}
		
		//Normal vector
		x = p3.getX() - p2.getX();
		y = p3.getY() - p2.getY();
		
		x2 = 1;
		y2 = ( p3.getY() - p2.getY() );
		if( y2 != 0 )
		{
			y2 = -(p3.getX() - p2.getX() ) / y2;  
		}
		else
		{
			x2 = 0; 
			y2 = 1; 
		}
		
		copyPts.add( crossPoint( previousNewPoint, 
									new Point2D.Double( previousNewPoint.getX() + x, previousNewPoint.getY() + y ),
									p3, 
									new Point2D.Double( p3.getX() + x2, p3.getY() + y2 ) ) ); 
		/*
		//Normal vector
		y = ( p3.getX() - p2.getY() ) / ( p3.getY() - p2.getY() ); 
		x = 1 / Math.sqrt( 1 + y * y );
		y = y * x * ( this.threshDistance * 0.75 );
		x *= ( this.threshDistance * 0.75 );
		
		newP2 = new Point2D.Double( p1.getX() + x, p1.getY() + y );
		if( areCroosSegments( previousNewPoint, newP2, p1, p2 ) 
				|| areCroosSegments( previousNewPoint, newP2, p2, p3 ) )
		{
			newP2.setLocation( newP2.getX() - 2 * x , newP2.getY() - 2 * y);
		}
		copyPts.add( newP2 );		
		*/
		
		for( int i = pts.size() - 1; i >= 0; i-- )
		{
			copyPts.add( pts.get( i ) );
		}
		
		return copyPts;
	}
	
	private Integer[] getDistancesSegments( Point2D.Double point, List< Point2D.Double > points )
	{
		Integer[] order = new Integer[ points.size() - 1 ];
		
		if( !points.isEmpty() )
		{
			double[] distances = new double[ points.size() -1 ];
			
			for( int i = 1; i < points.size(); i++ )
			{
				Point2D.Double segmentPoints1 = points.get( i - 1 );
				Point2D.Double segmentPoints2 = points.get( i );
				
				distances[ i -1 ] = this.getDistanceSegment( point , segmentPoints1, segmentPoints2 );
				order[ i -1 ] = i -1;
			}
			
			ArrayIndexComparator comparator = new ArrayIndexComparator( distances );
			Arrays.sort( order, comparator );
		}
		
		return order;
	}
	
	private double getDistanceSegment( Point2D.Double point, 
										Point2D.Double segmentPoints1,  
										Point2D.Double segmentPoints2   )
	{
		double distance = -1;
		
		double x1S = segmentPoints1.x;
		double y1S = segmentPoints1.y;
		double x2S = segmentPoints2.x;
		double y2S = segmentPoints2.y;
		
		if( x1S > x2S )
		{
			double aux = x1S;
			x1S = x2S;
			x2S = aux;
			
			aux = y1S;
			y1S = y2S;
			y2S = aux;
		}
		
		double m1 = ( y1S - y2S ) / ( x1S - x2S );
		double n1 = y1S - m1 * x1S;
		
		double m2 = - 1.0 / m1;
		double n2 = point.y - m2 * point.x;
		
		double xCutPoint = (n2 - n1) / ( m1 - m2 );
		double yCutPoint = m2 * xCutPoint + n2;
		
		if( xCutPoint >= x1S && xCutPoint <= x2S)
		{
			distance = point.distance( new Point2D.Double( xCutPoint, yCutPoint ) );
		}
		else
		{
			distance = point.distance( segmentPoints1 );
			double distance2 = point.distance( segmentPoints2 );
			if( distance > distance2 )
			{
				distance = distance2;
			}
		}
		
		return distance;		
	}
	
	private boolean checkCrossSegments( List< Point2D.Double > points  )
	{
		boolean cross = false;
		
		for( int i = 1; i < points.size() && !cross; i++ )
		{
			Point2D.Double p1S1 = points.get( i -1 );
			Point2D.Double p2S1 = points.get( i );
			
			/*
			double x1S1 = p1S1.x;
			double y1S1 = p1S1.y;
			double x2S1 = p2S1.x;
			double y2S1 = p2S1.y;
			
			if( x1S1 > x2S1 )
			{
				double aux = x1S1;
				x1S1 = x2S1;
				x2S1 = aux;
				
				aux = y1S1;
				y1S1 = y2S1;
				y2S1 = aux;
			}
			*/
			
			for( int j = i + 1; j < points.size() && !cross; j++ )
			{
				Point2D.Double p1S2 = points.get( j -1 );
				Point2D.Double p2S2 = points.get( j );
				
				cross = crossCheck( p1S1, p2S1, p1S2, p2S2 );
				
				/*
				double x1S2 = p1S2.x;
				double y1S2 = p1S2.y;
					
				double x2S2 = p2S2.x;
				double y2S2 = p2S2.y;
					
				double m1 = ( y1S1 - y2S1 ) / ( x1S1 - x2S1 );
				double n1 = y1S1 - m1 * x1S1;
					
				double m2 = ( y1S2 - y2S2 ) / ( x1S2 - x2S2 );
				double n2 = y1S2 - m2 * x1S2;
					
				double xCutPoint = (n2 - n1) / ( m1 - m2 );
					
				if( x1S2 > x2S2 )
				{
					double aux = x1S2;
					x1S2 = x2S2;
					x2S2 = aux;
				}
					
					
				cross = ( ( xCutPoint >= x1S1 ) && ( xCutPoint <= x2S1 )
								&& ( xCutPoint >= x1S2 ) && ( xCutPoint <= x2S2 ) );
				*/
			}
		}
		
		return cross;
	}
	
	private boolean crossCheck(Point2D.Double p1S1,
								Point2D.Double p2S1,
								Point2D.Double p1S2,
								Point2D.Double p2S2 )
	{
		double xCutPoint = crossPoint( p1S1, p2S1, p1S2, p2S2 ).getX();
		
		double x1S1 = p1S1.x;
		double x2S1 = p2S1.x;
		
		if( x1S1 > x2S1 )
		{
			double aux = x1S1;
			x1S1 = x2S1;
			x2S1 = aux;			
		}
		
		double x1S2 = p1S2.x;			
		double x2S2 = p2S2.x;
			
		if( x1S2 > x2S2 )
		{
			double aux = x1S2;
			x1S2 = x2S2;
			x2S2 = aux;
		}
		
		return ( xCutPoint >= x1S1 ) && ( xCutPoint <= x2S1 )
					&& ( xCutPoint >= x1S2 ) && ( xCutPoint <= x2S2 );
	}
	
	private Point2D.Double crossPoint( Point2D.Double p1S1,
										Point2D.Double p2S1,
										Point2D.Double p1S2,
										Point2D.Double p2S2 )
	{
		double x1S1 = p1S1.x;
		double y1S1 = p1S1.y;
		double x2S1 = p2S1.x;
		double y2S1 = p2S1.y;
		
		if( x1S1 > x2S1 )
		{
			double aux = x1S1;
			x1S1 = x2S1;
			x2S1 = aux;
			
			aux = y1S1;
			y1S1 = y2S1;
			y2S1 = aux;
		}
		
		double x1S2 = p1S2.x;
		double y1S2 = p1S2.y;
			
		double x2S2 = p2S2.x;
		double y2S2 = p2S2.y;
			
		double m1 = ( y1S1 - y2S1 ) / ( x1S1 - x2S1 );
		double n1 = y1S1 - m1 * x1S1;
			
		double m2 = ( y1S2 - y2S2 ) / ( x1S2 - x2S2 );
		double n2 = y1S2 - m2 * x1S2;
			
		double xCutPoint = (n2 - n1) / ( m1 - m2 );
			
		if( x1S2 > x2S2 )
		{
			double aux = x1S2;
			x1S2 = x2S2;
			x2S2 = aux;
		}
			
		return new Point2D.Double( xCutPoint, m1 * xCutPoint + n1 );
	}
	
	private boolean minSegmentAngles(Point2D.Double segmentPoints1, 
									Point2D.Double segmentPoints2,
									Point2D.Double segmentPoints3,
									double angle  )
	{		
		double x1 = segmentPoints1.x;
		double y1 = segmentPoints1.y;
		
		double x2 = segmentPoints2.x;
		double y2 = segmentPoints2.y;
		
		double x3 = segmentPoints3.x;
		double y3 = segmentPoints3.y;
		
		x1 -= x2;
		y1 -= y2;
			
		x3 -= x2;
		y3 -= y2;
			
		double a = Math.acos( ( x1 * x3 + y1 * y3  ) 
					/ ( Math.sqrt( x1 * x1 + y1 * y1 ) *  Math.sqrt( x3 * x3 + y3 * y3 ) ) );
		
		return angle < a;
	}

	private boolean checkMinDistanceSegments( List< Point2D.Double > points, double thresh )
	{
		boolean ok = true;
		
		for( int i = 1; i < points.size() && ok; i++ )
		{
			Point2D.Double p1S1 = points.get( i -1 );
			Point2D.Double p2S1 = points.get( i );			
			
			for( int j = i + 2; j < points.size() && ok; j++ )
			{
				Point2D.Double p1S2 = points.get( j -1 );
				Point2D.Double p2S2 = points.get( j );
				
				double dist = this.getDistanceSegment( p1S2, p1S1, p2S1 );
				double dist2 = this.getDistanceSegment( p2S2, p1S1, p2S1 );
				
				if( dist2 < dist )
				{
					dist = dist2;
				}
				
				dist2 = this.getDistanceSegment( p1S1, p1S2, p2S2 );
				
				if( dist2 < dist )
				{
					dist = dist2;
				}
				
				dist2 = this.getDistanceSegment( p2S1, p1S2, p2S2 );
				
				if( dist2 < dist )
				{
					dist = dist2;
				}
				
				ok = dist >= thresh;
			}
		}
		
		return ok;
	}
	
	/*
	private boolean orderPoints( List< Point2D.Double > orderedPoints, List< Point2D.Double > noOrderPoints )
	{
		boolean ok = noOrderPoints.isEmpty();
		
		if( !ok )
		{
			Point2D.Double pnt = orderedPoints.get( orderedPoints.size() - 1 );
			
			Integer[] closer = { new Integer( 0 ) };
			if( noOrderPoints.size() > 1 )
			{
				closer = getCloserPoints( pnt, noOrderPoints );
			}
			
			int i = 0;
			while( !ok && i < closer.length )
			{
				Point2D.Double candidate = noOrderPoints.get( closer[ i ] );
				ok = checkCrossSegments( candidate, orderedPoints );
				
				//ok = ok && minSegmentAngles( candidate, orderedPoints, Math.PI * 10 / 180 );
				
				if( ok )
				{
					orderedPoints.add( candidate );
					noOrderPoints.remove( closer[ i ].intValue() );
					
					ok = orderPoints( orderedPoints, noOrderPoints ); 
					
					if( !ok )
					{
						orderedPoints.remove( orderedPoints.size() - 1 );
						noOrderPoints.add( closer[ i ].intValue(), candidate );
					}
				}
				
				i++;
			}
		}
		
		return ok;
	}
	*/
	
	/*
	private Integer[] getCloserPoints( Point2D.Double point, List< Point2D.Double > orderedPoints )
	{
		Integer[] order = new Integer[ orderedPoints.size() ];
		
		if( !orderedPoints.isEmpty() )
		{
			double[] distances = new double[ orderedPoints.size() ];
			
			Iterator< Point2D.Double > it = orderedPoints.iterator();
			
			int i = -1;
			while( it.hasNext() )
			{
				i++;
				distances[ i ] = point.distance( it.next() );
				order[ i ] = i;
			}
			
			ArrayIndexComparator comparator = new ArrayIndexComparator( distances );
			Arrays.sort( order, comparator );
		}
		
		return order;
	}
	*/
/*
	private boolean minDistance( Point2D.Double point, List< Point2D.Double > points, double dist )
	{
		boolean ok = true;
		
		Iterator< Point2D.Double > it = points.iterator();
		
		while( ok && it.hasNext() )
		{
			ok = ( it.next() ).distance( point ) >= dist;
		}
		
		return ok;		
	}
*/
	/*
	private boolean minSegmentAngles(Point2D.Double point, List< Point2D.Double > segmentPoints, double angle  )
	{
		boolean ok = true;
		
		Point2D.Double p2 = segmentPoints.get( segmentPoints.size() - 1 );
		
		double x1S1 = p2.x;
		double y1S1 = p2.y;
		double x2S1 = point.x;
		double y2S1 = point.y;
		
		for( int i = 1; i < ( segmentPoints.size() ) && ok; i++ )
		{
			Point2D.Double p21 = segmentPoints.get( i - 1 );
			double x1S2 = p21.x;
			double y1S2 = p21.y;
			
			Point2D.Double p22 = segmentPoints.get( i );
			double x2S2 = p22.x;
			double y2S2 = p22.y;
		
			x1S2 -= x2S2;
			y1S2 -= y2S2;
			
			x2S1 -= x2S2;
			y2S1 -= y2S2;
			
			x2S2 = 0;
			y2S2 = 0;
			
			double a = Math.acos( ( x1S2 * x2S1 + y1S2 * y2S1  ) / ( Math.sqrt( x1S2 * x1S2 + y1S2 * y1S2 ) *  Math.sqrt( x2S1 * x2S1 + y2S1 * y2S1 ) ) );
			ok = angle < a;
		}
		
		return ok;
	}
	*/
	
	/*
	private boolean crossPreviousSegment( double x1P, double y1P , 
											double x1S2, double y1S2, double x2S2, double y2S2 )
	{
		double m2 = ( y1S2 - y2S2 ) / ( x1S2 - x2S2 );
		double n2 = y1S2 - m2 * x1S2;
		
		return y1P == m2 * x1P + n2;
	}
	*/
	private void normalTrajectory( double[][] t )
	{	
		double minX = java.lang.Double.MAX_VALUE;
		double minY = java.lang.Double.MAX_VALUE;
		double maxX = java.lang.Double.MIN_VALUE;
		double maxY = java.lang.Double.MIN_VALUE;
		
		for( int i = 0; i < t.length; i++ )
		{
			if( minX > t[ i ][ 0 ] )
			{
				minX = t[ i ][ 0 ];
			}
			
			if( maxX < t[ i ][ 0 ] )
			{
				maxX = t[ i ][ 0 ];
			}
			
			if( minY > t[ i ][ 1 ] )
			{
				minY = t[ i ][ 1 ];
			}
			
			if( maxY < t[ i ][ 1 ] )
			{
				maxY = t[ i ][ 1 ];
			}
		}
		
		for( int i = 0; i < t.length; i++ )
		{
			t[ i ][ 0 ] = ( t[ i ][ 0 ] - minX ) / ( maxX - minX );
			t[ i ][ 1 ] = ( t[ i ][ 1 ] - minY ) / ( maxY - minY );
		}
	}
	
}
