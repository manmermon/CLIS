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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class opColorWord
{
  private Color[] colores = { new Color(102, 153, 255), Color.lightGray, Color.green, 
								Color.orange, Color.pink, Color.red };
  private String[] etColores = { "azul", "gris", "verde", "amarillo", "rosa", "rojo" };
  
  private final int tamArray = 5;
  private Colores target = null;
  private Colores[] opciones = new Colores[ this.tamArray ];
  
  private int dificult = 0;
  
  public opColorWord(int dif)
  {
    if (dif <= 0)
    {
      this.dificult = 0;
    }
    else if (dif >= 4)
    {
      this.dificult = 4;
    }
    else
    {
      this.dificult = dif;
    }
  }
  
  public void setDificultad(int dif)
  {
    if (dif <= 0)
    {
      this.dificult = 0;
    }
    else if (dif >= 4)
    {
      this.dificult = 4;
    }
    else
    {
      this.dificult = dif;
    }
  }
  
  public int getDificultad()
  {
    return this.dificult;
  }
  
  public void generarTarea()
  {
    int indC = (int)(Math.round(Math.random() * (this.colores.length - 1)));
    Color tg = this.colores[indC];
    int indEt = (int)(Math.round(Math.random() * (this.colores.length - 1)));
    
    if (this.dificult < 2)
    {
      indEt = indC;
    }
    
    this.target = new Colores(this.etColores[indEt], tg);
    
    List<Integer> listEt = new ArrayList<Integer>();
    List<Integer> listC = new ArrayList<Integer>();
    for (int i = 0; i < this.colores.length; i++)
    {
      if (i != indEt)
      {
        listEt.add( i );
        listC.add( i );
      }
    }
    
    for (int j = 0; j < this.tamArray; j++)
    {
      int k = (int)(Math.round(Math.random() * (listEt.size() - 1)));
      int kc = (int)(Math.round(Math.random() * (listC.size() - 1)));
      
      if (this.dificult == 0)
      {
        kc = k;
      }
      
      int iet = listEt.get( k );
      int ic = listC.get( kc );
      
      this.opciones[j] = new Colores(this.etColores[iet], this.colores[ic]);
      
      listEt.remove(k);
      listC.remove(kc);
    }
    

    int k = (int)(Math.round(Math.random() * ( this.tamArray - 1)));
    if (this.dificult == 0)
    {
      this.opciones[k] = this.target;

    }
    else
    {
      this.opciones[k].setEtiqueta(this.target.getEtiqueta());
    }
    
    if (this.dificult > 0)
    {

      do
      {	
        k = (int)Math.round(Math.random() * ( this.tamArray - 1));
      }
      while (this.opciones[k].getColor().equals(this.colores[indC]));
      
      this.opciones[k].setColor(this.colores[indEt]);
    }
  }
  
  public Colores getObjetivo()
  {
    return this.target;
  }
  
  public Colores[] getOpciones()
  {
    return this.opciones;
  }
  
  public Color[] getColorPalette()
  {
    return this.colores;
  }
  
  public String[] getColorLabels()
  {
    return this.etColores;
  }
}