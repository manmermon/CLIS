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

package Config.Results;

import java.io.Serializable;

public class ConfigPropertiesResults implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String IDENT = null;
	
	private long[] answer_times = null;
	private long[] test_times = null; 
	private int[] correct_answers = null;
	private int[] num_tests = null;	
	private boolean[] defaultValue = null;	
		
	public ConfigPropertiesResults( String ident, int numLevels )
	{
		this.IDENT = ident;
		
		this.answer_times = new long[ numLevels ];
		this.test_times = new long[ numLevels ];
		this.correct_answers  = new int[ numLevels ];
		this.num_tests = new int[ numLevels ];
		this.defaultValue = new boolean[ numLevels ];
	}
			
	public void addTime( int dificult, long value )
	{
		this.comprobarParametroDificultad( dificult );
		
		if( value < 0 )
		{
			throw new IllegalArgumentException( "Error: segundo parametro <0.");
		}
			
		long v = this.answer_times[ dificult ] + value;
		
		if( this.defaultValue[ dificult] )
		{
			v = value;			
			this.defaultValue[ dificult ] = false;
			this.num_tests[ dificult ] = 0;
			this.test_times[ dificult ] = 0;
			this.correct_answers[ dificult ] = 0;
		}
			
		this.answer_times[ dificult ] = v;
		this.correct_answers[ dificult ]++;		
	}
	
	public void increaseNumberTests( int dificult, long test_time )
	{
		this.comprobarParametroDificultad( dificult );
		if( test_time < 0 )
		{
			throw new IllegalArgumentException( "Error: segundo parametro <0.");
		}
		
		if( this.defaultValue[ dificult ])
		{
			this.num_tests[ dificult ] = 0;
			this.test_times[ dificult ] = 0;
		}
		
		//System.out.println("ConfigPropertiesResults.increaseNumberTests(): test_time = "+test_time);
		this.num_tests[ dificult ]++;
		this.test_times[ dificult ] += test_time;
	}
	
	public String getIdent( )
	{
		return this.IDENT;
	}
	
	public long getAnswerTime( int dificult )
	{
		this.comprobarParametroDificultad( dificult );
		
		return this.answer_times[ dificult ];
	}
	
	public int getNumberCorrectAnswer( int dificult )
	{
		this.comprobarParametroDificultad( dificult );
		
		return this.correct_answers[ dificult ];
	}
	
	public int getNumberTest( int dificult )
	{
		this.comprobarParametroDificultad( dificult );
		
		return this.num_tests[ dificult ];
	}
	
	public long getTestTime( int dificult )
	{
		this.comprobarParametroDificultad( dificult );
		
		return this.test_times[ dificult ];
	}

	public long[] getAnswerTimes()
	{
		return this.answer_times;
	}
	
	public long[] getTestTimes()
	{
		return this.test_times;
	}
	
	public int[] getNumberCorrectAnswers()
	{
		return this.correct_answers;
	}
	
	public int[] getNumberTests()
	{
		return this.num_tests;
	}

	public boolean[] getIsDefaultVaule()
	{
		return this.defaultValue;
	}
	
	private void comprobarParametroDificultad( int dificult )
	{
		if( dificult >= answer_times.length || dificult < 0 )
		{
			throw new IllegalArgumentException( "Error: dificultad="+dificult+" fuera de rango.");
		}
	}

}
