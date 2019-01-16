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

package Auxiliar;

public class BufferAnswers
{
	public static final int Correcta = 1;
	public static final int Incorrecta = -1;

	private int[] buffer = null;
	private int it = 0;

	public BufferAnswers(int size)
	{
		if (size <= 0)
		{
			throw new IllegalArgumentException("Size must be greater than 0." );
		}

		this.buffer = new int[size];
	}

	public void addAnswer(int answer)
	{
		if (this.it < this.buffer.length)
		{
			this.buffer[this.it] = answer;
			this.it += 1;
		}
		else
		{
			for (int i = 1; i < this.buffer.length; i++)
			{
				this.buffer[(i - 1)] = this.buffer[i];
			}

			this.buffer[(this.it - 1)] = answer;
		}
	}

	public void clearBuffer()
	{
		this.buffer = new int[this.buffer.length];
		this.it = 0;
	}

	public int[] getBuffer()
	{
		return this.buffer;
	}

	public int getSumAnswer()
	{
		int sum = 0;
		for (int i = 0; i < this.it; i++)
		{
			sum += this.buffer[i];
		}

		return sum;
	}
}