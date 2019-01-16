/*
 * Copyright 2011-2016 by Manuel Merino Monge <manmermon@dte.us.es>
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class CLISDataFile implements OutputDataFileWriter
{
	private String fileName;
	private String headerInfo = "";
	private String header = "";

	private String endLine = "\n";

	private String GrpSep = ";";
	private String fieldSep = ",";

	private FileOutputStream fStream;
	private String compressAlg = "GZIP";

	private boolean addHeader = false;
	private List<byte[]> compressDataList;

	public CLISDataFile(String file) throws Exception
	{
		this.fStream = new FileOutputStream(new File(file));

		this.fileName = file;
		this.headerInfo = ("ver=1.0" + this.GrpSep + "compress=" + this.compressAlg + this.GrpSep);

		this.compressDataList = new ArrayList< byte[] >();
	}

	private void addHeaderInfo(String name, String type, int typeBytes, int numBytes, int col, int row)
	{
		this.headerInfo = (this.headerInfo + name + this.fieldSep + type + this.fieldSep + typeBytes + this.fieldSep + numBytes + this.fieldSep + col + this.fieldSep + row + this.GrpSep);
	}

	public void addHeader(String id, String text) throws Exception
	{
		this.addHeader = true;

		id = id.replace("\n", "").replace("\r", "");
		text = text.replace("\n", "").replace("\r", "");

		this.header = (id + "=" + text);
	}

	public void saveData(String text, int[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		int typeBytes = Integer.BYTES;
		int nBytes = data.length * typeBytes;
		byte[] dataToSave = new byte[nBytes];

		ByteBuffer bb = ByteBuffer.wrap(dataToSave);
		IntBuffer buf = bb.asIntBuffer();
		buf.put(data);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(nBytes);
		GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);

		zipStream.write(dataToSave);
		zipStream.close();

		byteStream.close();

		byte[] compressData = byteStream.toByteArray();
		saveCompressedData(compressData, text, "int", typeBytes, ncols, nRows);
	}

	public void saveData(String text, long[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		int typeBytes = Long.BYTES;
		int nBytes = data.length * typeBytes;
		byte[] dataToSave = new byte[nBytes];

		ByteBuffer bb = ByteBuffer.wrap(dataToSave);
		LongBuffer buf = bb.asLongBuffer();
		buf.put(data);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(nBytes);
		GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);

		zipStream.write(dataToSave);
		zipStream.close();

		byteStream.close();

		byte[] compressData = byteStream.toByteArray();
		saveCompressedData(compressData, text, "int", typeBytes, ncols, nRows);
	}

	public void saveData(String text, double[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		int typeBytes = Double.BYTES;
		int nBytes = data.length * typeBytes;
		byte[] dataToSave = new byte[nBytes];

		ByteBuffer bb = ByteBuffer.wrap(dataToSave);
		DoubleBuffer buf = bb.asDoubleBuffer();
		buf.put(data);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(nBytes);
		GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);

		zipStream.write(dataToSave);
		zipStream.close();

		byteStream.close();

		byte[] compressData = byteStream.toByteArray();
		saveCompressedData(compressData, text, "float", typeBytes, ncols, nRows);
	}

	public void saveData(String text, float[] data, int nRows) throws Exception
	{
		int ncols = data.length / nRows;

		int typeBytes = Float.BYTES;
		int nBytes = data.length * typeBytes;
		byte[] dataToSave = new byte[nBytes];

		ByteBuffer bb = ByteBuffer.wrap(dataToSave);
		FloatBuffer buf = bb.asFloatBuffer();
		buf.put(data);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(nBytes);
		GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);

		zipStream.write(dataToSave);
		zipStream.close();

		byteStream.close();

		byte[] compressData = byteStream.toByteArray();
		saveCompressedData(compressData, text, "float", typeBytes, ncols, nRows);
	}

	public void saveData(String text, char[] data)
			throws Exception
	{
		int nRows = data.length;
		int ncols = 1;

		int typeBytes = Character.BYTES;
		int nBytes = data.length * typeBytes;
		byte[] dataToSave = new byte[nBytes];

		ByteBuffer bb = ByteBuffer.wrap(dataToSave);
		CharBuffer buf = bb.asCharBuffer();
		buf.put(data);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(nBytes);
		GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);

		zipStream.write(dataToSave);
		zipStream.close();

		byteStream.close();

		byte[] compressData = byteStream.toByteArray();
		saveCompressedData(compressData, text, "char", typeBytes, ncols, nRows);
	}

	public void saveData(String text, String data) throws Exception
	{
		saveData(text, data.toCharArray());
	}

	public String getFileName()
	{
		return this.fileName;
	}

	public void closeFile() throws Exception
	{
		String head = this.headerInfo + "header," + this.addHeader + this.GrpSep + this.endLine;

		CharBuffer charBuffer = CharBuffer.wrap( head.toCharArray() );
		ByteBuffer byteBuffer = Charset.forName( "UTF-8" ).encode( charBuffer );
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());

		this.fStream.write(bytes);

		if (this.addHeader)
		{
			charBuffer = CharBuffer.wrap(this.header.trim() + this.endLine);
			byteBuffer = Charset.forName( "UTF-8" ).encode(charBuffer);
			bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
			this.fStream.write(bytes);
		}

		for (byte[] dat : this.compressDataList)
		{
			this.fStream.write(dat);
		}

		this.fStream.close();
		this.fStream = null;
	}

	private void saveCompressedData(byte[] compressData, String varName, String dataType, int typeBytes, int ncols, int nRows) throws Exception
	{
		addHeaderInfo(varName, dataType, typeBytes, compressData.length, ncols, nRows);
		this.compressDataList.add(compressData);
	}
}