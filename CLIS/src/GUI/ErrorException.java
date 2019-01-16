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

package GUI;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorException 
{
	public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
	public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
	public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
	public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
	
	public static void showErrorException( Component parentComponent, String msgError, String title, int typeMessage  )
	{
		Thread t = new Thread()
		{
			@Override
			public void run() 
			{
				JOptionPane.showMessageDialog( parentComponent, msgError, title, typeMessage);
			}
		};
		
		t.start();				
	}
	
}
