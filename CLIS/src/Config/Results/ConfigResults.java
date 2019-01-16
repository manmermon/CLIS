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

package Config.Results;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Activities.ActivityRegister;
import Activities.IActivity;
import Config.ConfigApp;

public class ConfigResults
{	
	private List< ConfigPropertiesResults > cfgProp = null;	
	 	
	public static final String ANSWER_TIME = "Answer Times";
	public static final String NUM_CORRECT_ANSWER = "Correct Answers";
	public static final String NUM_TESTS = "Number Tests";
	private static final String DEFAULT_VALUE = "Default Value";
	public static final String TEST_TIME = "Test Times";
		
	public static final long timeAnswerDefault = 30000; //30s = 30000ms
	public static final int numCorrectAnswerDefault = 1;
	public static final int numTestDefault = 1;
	public static final long timeTestDefault = 2 * timeAnswerDefault; // 60 = 2 * 30 = 2 * 30 * 1000 ms 
	
	private String[] task = null;
	
	private boolean loadDataOk = true;
	
	public ConfigResults( String pathFile)
	{	
		/*
		this.task = new String[ IActivity.activities.length - 1 ];
		for( int i = 0; i < IActivity.activities.length - 1; i++ )
		{
			this.task[ i ] = IActivity.activities[ i ];
		}
		
		this.cfgProp = new ConfigPropertiesResults[ this.task.length ];
		
		for( int i = 0; i < IActivity.activities.length - 1; i++ )
		{
			this.cfgProp[ i ] = new ConfigPropertiesResults( this.task[ i ], IActivity.NUMBER_DIFICUL_LEVELS );
		}
				
		*/
		
		this.task = ActivityRegister.getActivitiesID();
		
		
		this.cfgProp = new ArrayList< ConfigPropertiesResults >();
		
		//for( int i = 0; i < IActivity.activities.length - 1; i++ )
		for( int i = 0; i < this.task.length; i++ )
		{
			this.cfgProp.add( new ConfigPropertiesResults( this.task[ i ], IActivity.NUMBER_DIFICUL_LEVELS ) );
		}
		
		
		this.loadResultProperties( pathFile );
	}
		
	private void loadResultProperties( String pathFile )
	{
		boolean ok = true;
		//Properties prop = new Properties();
		//FileInputStream propFileIn = null;
		ObjectInputStream ois = null;
		
		try 
		{				
			File f = new File( pathFile );			
			
			if( f != null && f.exists() )
			{
				 ois = new ObjectInputStream (new FileInputStream( f ) );
				
				List< ConfigPropertiesResults > props = ( ( List< ConfigPropertiesResults > )ois.readObject() );
				
				for( ConfigPropertiesResults cf : props )
				{
					boolean enc = false;
					Iterator< ConfigPropertiesResults > it = this.cfgProp.iterator();
					ConfigPropertiesResults c = null;
					int i = -1;
					while( it.hasNext() && !enc )
					{
						i++;
						c = it.next();
						enc = c.getIdent().equals( cf.getIdent() );						
					}
										
					if( enc )
					{
						this.cfgProp.set( i, cf );
					}
					else
					{						
						this.cfgProp.add( cf );
					}
				}			
			}
			else
			{
				ok = false;
			}
			
			//propFileIn = new FileInputStream( f );
			
			//prop.load( propFileIn );
			
			this.checkProperties( );
			
		} 
		catch (Exception e) 
		{	
			loadDefaultProperties();
			ok = false;
		}
		finally
		{
			if( ois != null )
			{				
				try 
				{
					ois.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		this.loadDataOk = ok;
	}
	
	public boolean LoadDataOK()
	{
		return this.loadDataOk;
	}
	
	public void addTimeOutProperties( long time, int dificultad, String identTask )
	{
		ConfigPropertiesResults c = this.getConfigProperties( identTask );
		
		
		
		c.addTime( dificultad, time );		
	}
	
	public void increaseNumberTests( String identTask, int dificult, long test_time )
	{
		ConfigPropertiesResults c = this.getConfigProperties( identTask );
		if( c != null )
		{
			c.increaseNumberTests( dificult, test_time );
		}
	}
	
	public void saveConfigResults( String pathFile ) throws Exception
	{
		try 
		{
			File f = new File( pathFile );
			
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( f ) );			
			oos.writeObject( this.cfgProp );
			oos.close();
			
			//FileOutputStream fOut = new FileOutputStream( f );
			//prop.store( fOut, "");
			//fOut.close();
		}
		catch( Exception e )
		{
			String msg1 = e.getMessage();
			String msg2 = "Datos guardado en ruta por defecto: " + ConfigApp.defaultPathFile + ConfigApp.defaultNameFileConfigResults;
			
			File f = new File( ConfigApp.defaultPathFile );
			if( !f.exists() )
			{
				f.mkdirs();
			}
			f = new File( ConfigApp.defaultPathFile + ConfigApp.defaultNameFileConfigResults );
			
			//FileOutputStream fOut;
			
			try 
			{
				ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( f ) );
				oos.writeObject( this.cfgProp );
				oos.close();
				
				//fOut = new FileOutputStream( f );
				//prop.store( fOut, "");
				//fOut.close();
			}
			catch (FileNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				msg2 = e1.getMessage();
				
			}
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				msg2 = e1.getMessage();
			}
			
			throw new Exception( msg1 + ". " + msg2 );
		}
	}
	
	private void loadDefaultProperties()
	{	
		//for( int i = 0; i < IActivity.activities.length - 1; i++ )
		for( int i = 0; i < task.length; i++ )
		{
			ConfigPropertiesResults c = this.getConfigProperties( this.task[ i ] );
			
			long[] times = c.getAnswerTimes();
			long[] test_times= c.getTestTimes();
			int[] ncanswer = c.getNumberCorrectAnswers();
			int[] ntests = c.getNumberTests();
			boolean[] dv = c.getIsDefaultVaule();
		
			for( int j = 0; j < times.length; j++ )
			{
				times[ j ] =  timeAnswerDefault;
				ncanswer[ j ] = numCorrectAnswerDefault;
				ntests[ j ] =  numTestDefault;
				dv[ j ] = true;
				test_times[ j ] = timeTestDefault; //5mints = 5 * 60 s = 5 * 60 * 1000 ms
			}
		}
	}

	private void checkProperties( /*Properties prop*/ )
	{		
		for( ConfigPropertiesResults c : this.cfgProp )
		{	
			for( int j = 0; j < c.getAnswerTimes().length; j++ )
			{
				long[] times = c.getAnswerTimes();
				long[] test_times = c.getTestTimes();
				int[] ncanswer = c.getNumberCorrectAnswers();
				int[] ntests = c.getNumberTests();
				boolean[] dv = c.getIsDefaultVaule();
				
				if( times[ j ] < 1000 || ncanswer[ j ] < 1 //1000ms = 1s
						|| ntests[ j ] < 1 || test_times[ j ] < 1000) //1mint = 60*1000ms = 60s 
				{
					times[ j ] = timeAnswerDefault;
					test_times[ j ] = 60 * 1000;
					ncanswer[ j ] = 1;
					ntests[ j ] = 1;
					dv[ j ] = true;
				}
			}			
		}			
	}
	
	public ConfigPropertiesResults getConfigProperties( String task )
	{
		ConfigPropertiesResults c = null;
		boolean enc = false;
		Iterator< ConfigPropertiesResults > it = this.cfgProp.iterator();
		
		while( it.hasNext() && !enc )
		{
			c = it.next();
			enc = c.getIdent().equals( task );
		}
		
		if( !enc )
		{
			c = null;
		}
		
		return c;
	}
}
