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

public class Colores
{
  private String Etiqueta = "";
  private Color color = null;
  
  public Colores(String et, Color c)
  {
    if ((et == null) || (c == null))
    {
      throw new IllegalArgumentException( "Inputs null" );
    }
    
    this.Etiqueta = et;
    this.color = c;
  }
  
  public String getEtiqueta()
  {
    return this.Etiqueta;
  }
  
  public Color getColor()
  {
    return this.color;
  }
  
  public void setColor(Color c)
  {
    this.color = c;
  }
  
  public void setEtiqueta(String et)
  {
    this.Etiqueta = et;
  }
  
  public boolean equal(Colores c)
  {
    return (this.Etiqueta.equals(c.getEtiqueta())) && (this.color.equals(c.getColor()));
  }
}
