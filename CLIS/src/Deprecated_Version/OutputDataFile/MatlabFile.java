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

import com.jmatio.io.MatFileIncrementalWriter;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLInt64;



public class MatlabFile
implements OutputDataFileWriter
{
	private MatFileIncrementalWriter writer = null;
	private String fileName;

	public MatlabFile(String file) throws Exception
	{
		this.writer = new MatFileIncrementalWriter(file);
		this.fileName = file;
	}

	public void saveData(String varName, float[] data, int nRows) throws Exception
	{
		double[] d = new double[data.length];
		for (int i = 0; i < d.length; i++)
		{
			d[i] = data[i];
		}

		saveData(varName, d, nRows);
	}

	public void saveData(String varName, char[] data) throws Exception
	{
		if ((data != null) && (data.length > 0))
		{
			String txt = "";

			for (int i = 0; i < data.length; i++)
			{
				txt = txt + data[i];
			}

			MLChar d = new MLChar(varName, txt);

			this.writer.write(d);
		}
	}

	public void saveData(String varName, String data) throws Exception
	{
		if ((data != null) && (data.length() > 0))
		{
			MLChar d = new MLChar(varName, data);

			this.writer.write(d);
		}
	}

	public void saveData(String varName, int[] data, int nRows) throws Exception
	{
		long[] aux = new long[data.length];

		for (int i = 0; i < data.length; i++)
		{
			aux[i] = data[i];
		}

		saveData(varName, aux, nRows);
	}

	public void saveData(String varName, long[] data, int nRows) throws Exception
	{
		if ((data != null) && (data.length > 0))
		{
			int m = (int)Math.floor(data.length / nRows);

			MLInt64 d = new MLInt64(varName, data, m);
			this.writer.write(d);
		}
	}

	public void saveData(String varName, double[] data, int nRows) throws Exception
	{
		if ((data != null) && (data.length > 0))
		{
			int m = (int)Math.floor(data.length / nRows);
			MLDouble md = new MLDouble(varName, data, m);
			this.writer.write(md);
		}
	}

	public void closeFile() throws Exception
	{
		if (this.writer != null)
		{
			this.writer.close();
		}
	}

	public void addHeader(String id, String text) throws Exception
	{
		saveData(id, text);
	}


	public String getFileName()
	{
		return this.fileName;
	}
}