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

import Auxiliar.Tasks.ITask;
import Auxiliar.Tasks.ITaskMonitor;
import Controls.eventInfo;
import StoppableThread.AbstractStoppableThread;
import StoppableThread.IStoppableThread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;

public class clipSoundThread extends AbstractStoppableThread implements ITask
{
	public static final String EVENT_SOUND_END = "event sound end";
	public static final short ID_SOUND_PARAMETER = 0;
	private SourceDataLine sound = null;
	private AudioInputStream soundIn = null;
	private InputStream stream = null;
	private BufferedInputStream inStream = null;
	private final int BUFFER_SIZE = 128000;
	private byte[] soundBuffer = null;
	private int readBytes = 0;

	private boolean play = true;
	private boolean isStart = false;
	private boolean isPause = false;

	private File soundFile = null;

	private ITaskMonitor monitor = null;
	private List<eventInfo> events = null;


	public clipSoundThread() throws Exception
	{
		this.soundIn = SoundUtils.getTone(1500, 150, 500.0D);

		this.events = new ArrayList< eventInfo >();
	}

	private void getFile() throws Exception
	{
		this.stream = new FileInputStream(this.soundFile);
		this.inStream = new BufferedInputStream(this.stream);

		this.soundIn = AudioSystem.getAudioInputStream(this.inStream);
		AudioFormat format = this.soundIn.getFormat();


		if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
			format = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, 
					format.getSampleRate(), 
					format.getSampleSizeInBits() * 2, 
					format.getChannels(), 
					format.getFrameSize() * 2, 
					format.getFrameRate(), 
					true);
			this.soundIn = AudioSystem.getAudioInputStream(format, this.soundIn);
		}

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,  this.soundIn.getFormat());

		this.sound = (SourceDataLine)AudioSystem.getLine(info);
		this.sound.addLineListener(new LineListener()
		{

			public void update(LineEvent event)
			{
				if ((event.getType().equals(LineEvent.Type.STOP)) 
						&& (!isPause))
				{
					events.add(new eventInfo( EVENT_SOUND_END, null));

					try
					{
						notifyToMonitor();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}

	public boolean isPlay()
	{
		boolean play = false;

		if (this.sound != null)
		{
			play = this.sound.isRunning();
		}    

		return play;
	}

	public void reset() throws Exception
	{
		if ((this.sound != null) 
				&& ( (this.sound.isOpen()) 
						|| (this.sound.isActive()) 
						|| (this.sound.isRunning())))
		{
			this.sound.stop();
			this.sound.drain();
			this.sound.flush();
			while (this.sound.isOpen())
			{
				this.sound.close();
				if (this.sound.isOpen())
				{
					try
					{
						Thread.sleep(10L);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		if (this.soundIn != null)
		{
			this.soundIn.close();
		}

		this.sound = null;
		this.soundIn = null;

		getFile();

		this.play = true;

		this.prepareAudio();

		synchronized (this)
		{
			if (super.getState().equals(Thread.State.WAITING))
			{
				super.notify();
			}
			else
			{
				super.interrupt();
			}
		}
	}

	public boolean isStart()
	{
		return this.isStart;
	}

	public void stopSound()
	{
		if (this.sound != null)
		{
			this.isPause = true;
			this.sound.stop();
			this.sound.drain();
			this.sound.flush();
		}
	}

	public void continueSound()
	{
		if ((this.sound != null) && (this.isPause))
		{
			this.isPause = false;
			this.sound.start();

			synchronized (this)
			{
				this.notify();
			}
		}
	}

	public void addParameter(short parameterID, Object parameter) throws Exception
	{
		this.setParameter(parameterID, parameter);
	}

	public void setParameter(short parameterID, Object parameter) throws Exception
	{		
		this.soundFile = new File( parameter.toString() );
		
		super.setName( this.getClass().getSimpleName() + "-" + this.soundFile.getName() );
	}


	public void clearParameters()
	{
		this.stopSound();
		this.sound = null;
	}


	public void taskMonitor(ITaskMonitor m)
	{
		this.monitor = m;
	}

	public void createTask() throws Exception
	{
		try
		{
			this.getFile();
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Sound file incorrect: " + this.soundFile.getAbsolutePath());
		}
	}

	public void startTask()
			throws Exception
	{
		if (this.soundBuffer == null)
		{
			this.soundBuffer = new byte[ this.BUFFER_SIZE ];
		}

		if (super.getState().equals(Thread.State.WAITING))
		{
			super.notify();
		}
		else if ( this.isPlay())
		{
			this.reset();
		}
		else
		{
			super.startThread();
		}
	}


	public List<eventInfo> getResult()
	{
		return this.events;
	}


	public void clearResult()
	{
		this.events.clear();
	}

	protected void startUp() throws Exception
	{
		super.startUp();

		this.isStart = true;

		this.prepareAudio();
	}

	private void prepareAudio() throws Exception
	{
		if (this.sound != null)
		{

			if (!this.sound.isOpen())
			{
				this.sound.open(this.soundIn.getFormat());
			}

			if (!this.sound.isRunning())
			{
				this.sound.start();
			}
		}
		else
		{
			this.stopThread = true;
		}
	}

	protected void preStopThread(int friendliness) throws Exception
	{
		if (this.sound != null)
		{
			if ((this.sound.isActive()) 
					|| (this.sound.isOpen()) 
					|| (this.sound.isRunning()))
			{
				this.sound.stop();
				this.sound.drain();
				this.sound.flush();
				while (this.sound.isOpen())
				{
					this.sound.close();
					if (this.sound.isOpen())
					{
						try
						{
							Thread.sleep(10L);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}


		if (this.soundIn != null)
		{
			this.soundIn.close();
		}

		if (this.inStream != null)
		{
			this.inStream.close();
		}

		if (this.stream != null)
		{
			this.stream.close();
		}
	}


	protected void postStopThread(int friendliness) throws Exception
	{}

	protected void runInLoop()
			throws Exception
	{
		synchronized (this)
		{
			if (!this.isPause)
			{
				this.readBytes = this.soundIn.read(this.soundBuffer, 0, this.BUFFER_SIZE);

				// Es posible que se pare el hilo y aun no se haya salido del bucle por lo que puede dar una excepcion NulPointerException
				if ((this.readBytes != -1) && (this.sound != null))        
				{
					this.sound.write(this.soundBuffer, 0, this.readBytes);
				}
			}
		}
	}


	protected void targetDone() throws Exception
	{
		if (this.readBytes == -1)
		{
			this.play = false;

			this.sound.stop();

			if (!this.stopThread)
			{
				super.wait();
			}
		}
		else if (this.isPause)
		{
			super.wait();
		}
	}

	private void notifyToMonitor() throws Exception
	{
		if (this.monitor != null)
		{
			this.monitor.taskDone(this);
		}
	}

	protected void cleanUp() throws Exception
	{
		super.cleanUp();

		this.sound = null;
		this.soundIn = null;
		this.inStream = null;
		this.soundFile = null;
		this.soundBuffer = null;
		this.stream = null;

		System.gc();
	}


	protected void runExceptionManager(Exception e)
	{
		this.stopThread = true;

		try
		{
			this.preStopThread( IStoppableThread.ForcedStop );
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		if (!(e instanceof InterruptedException))
		{
			if (!(e instanceof IOException))
			{
				e.printStackTrace();
			}
			else if (!this.isStart)
			{
				e.printStackTrace();
			}
		}

		this.events.add(new eventInfo( EVENT_SOUND_END, null));

		try
		{
			notifyToMonitor();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected void finallyManager() {}
}