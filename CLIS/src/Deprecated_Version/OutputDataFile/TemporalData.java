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

package Deprecated_Version.OutputDataFile;

import java.util.List;

public class TemporalData
{
	private List<Object> data;
	private List<Double> timeStampData;
	private int dataType;
	private String streamName;
	private String lslXML;
	private int CountChannels;
	private String outFileName = "./data" + DataFileFormat.getSupportedFileExtension( DataFileFormat.CLIS );
	private String outFileFormat = DataFileFormat.CLIS;

	public TemporalData( List< Object > d, List< Double > time, int type, int nChannels, String name
						, String xml, String outName, String outputFormat )
	{
		this.data = d;
		this.timeStampData = time;
		this.dataType = type;
		this.CountChannels = nChannels;
		this.streamName = name;
		this.lslXML = xml;
		this.outFileName = outName;
		this.outFileFormat = outputFormat;
	}

	public String getOutputFileFormat()
	{
		return this.outFileFormat;
	}
	
	public String getOutputFileName()
	{
		return this.outFileName;
	}
	
	public List<Object> getData()
	{
		return this.data;
	}

	public List<Double> getTimeStamp()
	{
		return this.timeStampData;
	}

	public int getDataType()
	{
		return this.dataType;
	}

	public int getNumberOfChannels()
	{
		return this.CountChannels;
	}

	public String getStreamingName()
	{
		return this.streamName;
	}

	public String getLslXml()
	{
		return this.lslXML;
	}
}