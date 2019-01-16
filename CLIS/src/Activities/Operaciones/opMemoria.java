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
package Activities.Operaciones;

//import Imagenes.referenciaImagenes;

public class opMemoria
{
	public static final int CIRCULO = 0;
	public static final int CUADRADO = 2;
	public static final int ROMBO = 4;
	public static final int TRIANGULO = 6;
	public static final int BLANCO = 0;
	public static final int NEGRO = 1;

	private int[] matriz = null;

	public opMemoria(int f, int c)
	{
		this.matriz = new int[f * c];
	}

	public void generarTarea()
	{
		for (int i = 0; i < this.matriz.length; i++)
		{

			int color = (int)Math.round(Math.random()); //0 -> BLANCO; 1 -> NEGRO
			int figura = (int)Math.floor(Math.random() * 4.0D);
			if (figura == 4)
			{
				figura = 3;
			}

			this.matriz[i] = (2 * figura + color);
			//0->circulo blanco; 1->circulo negro; 2 ->cuadrado blanco; 3 -> cuadrado negro, etc.
		}
	}

	public int[] getTarea()
	{
		return this.matriz;
	}

	public static int[] getConjuntoMemoria()
	{
		int[] memoria = new int[8];

		for (int i = 0; i < memoria.length; i++)
		{
			memoria[i] = i;
		}

		return memoria;
	}
}