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

import Controls.coreControl;
import GUI.MyComponents.GeneralAppIcon;
import GUI.MyComponents.TextAreaPrintStream;
import GUI.ErrorException;
import GUI.appUI;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import Config.ConfigApp;

public class mainCLIS
{
	/*
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			//UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
			createApplication();
			
			// load configuration
			try
			{
				if( args.length > 1 )
				{
					if( args[0].equals( "-c" ) )
					{
						ConfigApp.loadConfig( new File( args[ 1 ] ) );
						
						appUI.getInstance().checkConfig();
					}
				}
			}
			catch( Throwable e)
			{
				new Thread() 
				{
					public void run() 
					{
						ErrorException.showErrorException( null, e.getMessage(), "Config Error", ErrorException.WARNING_MESSAGE );
					};
				}.start();

			}
		}
		catch (Throwable e1)
		{
			showError(e1);
		}
		finally
		{}
	}


	private static void createApplication() throws Throwable
	{
		createAppGUI();
		createAppCoreControl();
	}

	private static void createAppCoreControl()
	{
		try
		{
			coreControl ctrl = coreControl.getInstance();
			ctrl.start();
		}
		catch (Exception e)
		{
			showError(e);
		}
	}

	private static appUI createAppGUI()
	{
		appUI ui = appUI.getInstance();
		ui.setIconImage(GeneralAppIcon.getIconoAplicacion(64, 64).getImage());
		ui.setTitle(  ConfigApp.fullNameApp );
		//ui.setTitle( System.getProperty( "user.dir"));
		//ImageIcon icono = new ImageIcon( referenciaImagenes.getAppIcon32());
		//ImageIcon icono = new ImageIcon(  );

		ui.setBackground(SystemColor.info);

		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension dm = t.getScreenSize();
		Insets pad = t.getScreenInsets(ui.getGraphicsConfiguration());

		dm.width = (dm.width / 2 - (pad.left + pad.right));
		dm.height = (dm.height / 2 - (pad.top + pad.bottom));

		ui.setSize(dm);
		//ui.setSize( 820, 550 );
		//ui.setSize( t.getScreenSize() );
		//ui.setExtendedState( JFrame.MAXIMIZED_BOTH );


		ui.toFront();
		Dimension d = new Dimension(dm);
		d.width /= 5;
		//ui.getJButtonCorrecto().setPreferredSize( d );

		//ui.setLocationByPlatform( true );	
		//ui.setLocation( 0, 0 );

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
		ui.setLocation(insets.left + 1, insets.top + 1);
		//ui.setBounds( 0, 0, dm.width, dm.height );

		//this.loadValueConfig( null ); //Carga los valores por defecto;
		ui.setVisible(true);

		return ui;
	}

	private static void showError(Throwable e)
	{
		JTextArea jta = new JTextArea();
		jta.setAutoscrolls(true);
		jta.setEditable(false);
		jta.setLineWrap(true);
		jta.setTabSize(0);

		TextAreaPrintStream log = new TextAreaPrintStream(jta, new ByteArrayOutputStream());

		e.printStackTrace(log);

		String[] lineas = jta.getText().split("\n");
		int wd = Integer.MIN_VALUE;
		FontMetrics fm = jta.getFontMetrics(jta.getFont());
		for (int i = 0; i < lineas.length; i++)
		{
			if (wd < fm.stringWidth(lineas[i]))
			{
				wd = fm.stringWidth(lineas[i]);
			}
		}

		JDialog p = new JDialog();

		Icon icono = UIManager.getIcon("OptionPane.warningIcon");
		int w = icono.getIconWidth();int h = icono.getIconHeight();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage img = gc.createCompatibleImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		icono.paintIcon(null, g, 0, 0);
		p.setIconImage(img);

		p.setTitle("Fatal Fail");
		Dimension d = new Dimension((int)(wd * 1.25D), fm.getHeight() * 10);
		p.setSize(d);

		Point pos = ge.getCenterPoint();
		pos.x -= d.width / 2;
		pos.y -= d.height / 2;
		p.setLocation(pos);

		p.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}

		});
		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}

		});
		p.add(new JScrollPane(jta), BorderLayout.CENTER );
		p.add(cerrar, BorderLayout.SOUTH);
		p.setVisible(true);
	}
}
