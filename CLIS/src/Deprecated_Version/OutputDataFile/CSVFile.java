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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVFile implements OutputDataFileWriter
{
	private PrintWriter writer = null;
	private List<String> txtData = null;

	private String preHeader = "";
	private String header = "";

	private int numCols;
	private String separator = "\t";
	private String endLine = "\n";
	private String fileName;

	public CSVFile(String file) throws Exception
	{
		this.writer = new PrintWriter(new File(file));
		this.txtData = new ArrayList< String >();
		this.numCols = 0;
		this.fileName = file;
	}

	private void fillingEndingRowWith(String value, int rep, int row)
	{
		String txt = (String)this.txtData.get(row);
		for (int i = 0; i < rep; i++)
		{
			txt = txt + value + this.separator;
		}

		this.txtData.set(row, txt);
	}

	private void checkFillingEmptyData(int newVarRow, int newVarCols)
	{
		if (newVarRow < this.txtData.size())
		{
			for (int i = newVarRow; i < this.txtData.size(); i++)
			{
				fillingEndingRowWith(" ", newVarCols, i);
			}

		} else if (newVarRow > this.txtData.size())
		{
			for (int i = newVarRow - this.txtData.size(); i > 0; i--)
			{
				this.txtData.add("");
				fillingEndingRowWith(" ", this.numCols, this.txtData.size() - 1);
			}
		}
	}

	private void addHeader(String varName, int nCols)
	{
		for (int i = 0; i < nCols; i++)
		{
			this.header = (this.header + varName + "_" + i + this.separator);
		}
	}

	private String addToData(Object[][] values)
	{
		String data = "";

		for (int r = 0; r < values.length; r++)
		{
			String d = "";
			for (int c = 0; c < values[0].length; c++)
			{
				d = d + values[r][c].toString() + this.separator;
			}

			String dat = (String)this.txtData.get(r) + d;
			this.txtData.set(r, dat);
		}

		return data;
	}

	private void addToArray(Object[][] array, int r, int c, Object value)
	{
		array[r][c] = value;
	}

	public void saveData(String text, int[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		checkFillingEmptyData(nRows, ncols);

		addHeader(text, ncols);

		this.numCols += ncols;

		int row = 0;
		int iCol = 0;
		Integer[][] d = new Integer[nRows][ncols];
		for (int i = 0; i < data.length; i++)
		{
			addToArray(d, row, iCol,  data[i] );
			iCol++;
			
			if (iCol > ncols - 1)
			{
				row++;
				iCol = 0;
			}
		}

		addToData(d);
	}

	public void saveData(String text, long[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		checkFillingEmptyData(nRows, ncols);

		addHeader(text, ncols);

		this.numCols += ncols;

		int row = 0;
		int iCol = 0;
		Long[][] d = new Long[nRows][ncols];
		for (int i = 0; i < data.length; i++)
		{
			addToArray(d, row, iCol, data[ i ] );
			iCol++;
			
			if (iCol > ncols - 1)
			{
				row++;
				iCol = 0;
			}
		}

		addToData(d);
	}

	public void saveData(String text, double[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		checkFillingEmptyData(nRows, ncols);

		addHeader(text, ncols);

		this.numCols += ncols;

		int row = 0;
		int iCol = 0;
		Double[][] d = new Double[nRows][ncols];
		for (int i = 0; i < data.length; i++)
		{
			addToArray(d, row, iCol,  data[i] );
			iCol++;			

			if (iCol > ncols - 1)
			{
				row++;
				iCol = 0;
			}
		}

		addToData(d);
	}

	public void saveData(String text, float[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		checkFillingEmptyData(nRows, ncols);

		addHeader(text, ncols);

		this.numCols += ncols;

		int row = 0;
		int iCol = 0;
		Float[][] d = new Float[nRows][ncols];
		for (int i = 0; i < data.length; i++)
		{
			addToArray(d, row, iCol, data[i] );
			iCol++;
			
			if (iCol > ncols)
			{
				row++;
				iCol = 0;
			}
		}

		addToData(d);
	}

	public void saveData(String text, char[] data) throws Exception
	{
		int nRows = data.length;
		int ncols = 1;

		this.checkFillingEmptyData(nRows, ncols);

		this.addHeader(text, ncols);

		this.numCols += ncols;

		String[][] d = new String[nRows][ncols];
		for (int i = 0; i < data.length; i++)
		{			
			d[ i ][ ncols - 1 ] = data[ i ] + "";
		}

		addToData( d );
	}

	public void saveData(String text, String data) throws Exception
	{
		this.saveData( text, data.toCharArray() );
	}

	public void closeFile() throws Exception
	{
		this.writer.print( this.preHeader.trim() + this.endLine );
		this.writer.print( this.header.trim() + this.endLine );
		for (String data : this.txtData)
		{
			this.writer.print(data + this.endLine);
		}

		this.writer.close();
	}

	public void addHeader(String id, String text) throws Exception
	{
		this.preHeader = (this.preHeader + id + " = " + text + this.endLine);
	}

	public String getFileName()
	{
		return this.fileName;
	}
}