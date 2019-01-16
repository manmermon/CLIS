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

public class opSearchCount
{
  private int[][] matriz = null;
  private int filas;
  private int col;
  private int objetivo; 
  private int numRep = 0;
  
  private final int min = 1;
  private final int max = 60;
  
  public opSearchCount(int f, int c)
  {
    this.filas = f;
    this.col = c;
    
    this.matriz = new int[f][c];
    generarTarea();
  }
  
  public void generarTarea()
  {
    this.objetivo = ((int)Math.round(Math.random() * ( max - min) + min ));
    
    int count = 0;
    for (int f = 0; f < this.filas; f++)
    {
      for (int c = 0; c < this.col; c++)
      {
        int n = (int)Math.round(Math.random() * ( max - min) + min);
        this.matriz[f][c] = n;
        
        if (n == this.objetivo)
        {
          count++;
        }
      }
    }
    
    this.numRep = (int)(Math.round(Math.random() * (this.filas * this.col / 2)));
    
    if (this.numRep < count)
    {
      this.numRep = count;
    }
    else
    {
      for (int n = 0; n < Math.abs(this.numRep - count); n++)
      {
        int fi = (int)Math.round(Math.random() * (this.filas - 1));
        int ci = (int)Math.round(Math.random() * (this.col - 1));
        
        while (this.matriz[fi][ci] == this.objetivo)
        {
          fi = (int)Math.round(Math.random() * (this.filas - 1));
          ci = (int)Math.round(Math.random() * (this.col - 1));
        }
        
        this.matriz[fi][ci] = this.objetivo;
      }
    }
  }
  
  public int getObjetivo()
  {
    return this.objetivo;
  }
  
  public int getNumApareciones()
  {
    return this.numRep;
  }
  
  public int[][] getMatriz()
  {
    return this.matriz;
  }
}