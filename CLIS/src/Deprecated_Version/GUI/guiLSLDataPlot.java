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

package Deprecated_Version.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.xy.DefaultXYDataset;

import GUI.MyComponents.NumberRange;
import GUI.MyComponents.infoDialog;

public class guiLSLDataPlot extends JPanel
{
	private static final long serialVersionUID = 9117600627145108594L;
	
	private JPanel panelPlots = null;
	private JPanel filterRangePanel = null;

	private int queueLength = 100;
	private List<Queue<Double>> xy = new ArrayList<Queue<Double>>();
	private List<String> plotNames = new ArrayList< String >();


	private List<Double> minY = new ArrayList< Double >();
	private List<Double> maxY = new ArrayList< Double >();

	private List<Integer> ignoredCols = null;

	private Semaphore sem = new Semaphore(1, true);

	private JTextField filterRangeText = null;
	
	private JButton applyFilters = null;
	private JButton infoFilters = null;
	
	private String plotName = "";
	private boolean newPlotName = false;

	//private boolean stopRun = false;

	private JScrollPane scrollpanel;

	private HashMap< String, NumberRange > filters = null;
	
	//private List<List<Double>> XY;

	/**
	 * Create the dialog.
	 */
	public guiLSLDataPlot(int dataLength)
	{
		super.setName( this.getClass().getName() );

		this.queueLength = dataLength;

		this.ignoredCols = new ArrayList< Integer >();

		super.setVisible(true);

		setBackground(Color.WHITE);
		setDoubleBuffered(true);

		setLayout(new BorderLayout(0, 0));

		super.add( this.getJPanelFilterRange(), BorderLayout.NORTH );
		super.add( this.getJScrollPanelPlot(), BorderLayout.CENTER );		
		
		this.clearFilters();
	}
	
	public void clearFilters()
	{
		if( this.filters == null )
		{
			this.filters = new HashMap< String, NumberRange >();
		}
		else
		{
			this.filters.clear();
		}
	}
	
	public void setPlotName(String name)
	{
		if (name != null)
		{
			if (!this.newPlotName)
			{
				this.newPlotName = (!this.plotName.equals(name));
			}

			this.plotName = name;
		}
	}

	private JPanel getJPanelFilterRange()
	{
		if( this.filterRangePanel == null )
		{
			this.filterRangePanel = new JPanel();
			this.filterRangePanel.setLayout( new BoxLayout( this.filterRangePanel, BoxLayout.X_AXIS ) );
			
			this.filterRangePanel.add( this.getJButtonInfoFilters() );
			this.filterRangePanel.add( Box.createRigidArea( new Dimension(2, 0) ) );
			this.filterRangePanel.add( new JLabel( "Filters:" ) );
			this.filterRangePanel.add( Box.createRigidArea( new Dimension(2, 0) ) );
			this.filterRangePanel.add( this.getFilterRangeText() );
			this.filterRangePanel.add( Box.createRigidArea( new Dimension(2, 0) ) );
			this.filterRangePanel.add( this.getJButtonApplyFilters() );
		}
		
		return this.filterRangePanel;
	}
	
	private JTextField getFilterRangeText()
	{
		if( this.filterRangeText == null )
		{
			this.filterRangeText = new JTextField();
			
			this.filterRangeText.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JTextField jt = (JTextField)e.getSource();
					String filterText = jt.getText();
					
					defineFilters( filterText );
				}
			});
		}
		
		return this.filterRangeText;
	}
	
	private JButton getJButtonApplyFilters()
	{
		if( this.applyFilters == null )
		{
			this.applyFilters = new JButton( "Apply" );
			this.applyFilters.setBorder( BorderFactory.createSoftBevelBorder( SoftBevelBorder.RAISED ) );
			
			this.applyFilters.addActionListener(new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{					
					String filterText = getFilterRangeText().getText();
					
					defineFilters( filterText );
				}
			});
		}
		
		return this.applyFilters;
	}
	
	private void defineFilters( String filterText )
	{
		if( filterText == null || filterText.isEmpty() )
		{
			clearFilters();
		}
		else
		{
			filterText = filterText.replace( " ", "" );
			String[] FILTERS = filterText.split( ";" );
			
			for( String filter : FILTERS )
			{
				try
				{
					String[] filParts = filter.split( ":" );
					if( filParts.length == 2 )
					{
						int plotNo = new Integer( filParts[ 0 ] );
						
						NumberRange range = null;
						
						String interval = filParts[ 1 ];
						if( interval.charAt( 0 ) == '(' && interval.charAt( interval.length() - 1 ) == ')' )
						{
							String[] values = interval.replace( "(", "" ).replace( ")", "").split( "," );
							
							if( values.length == 2 )
							{							
								String a = values[ 0 ];
								String b = values[ 1 ];
								
								double min = Double.NEGATIVE_INFINITY;
								if( !a.toLowerCase().equals( "-inf" ) )
								{
									min = new Double( a );
								}
								
								double max = Double.POSITIVE_INFINITY;
								if( !b.toLowerCase().equals( "inf" ) )
								{
									max = new Double( b );
								}								
								
								range = new NumberRange( min, max );
							}
						}
						
						if( range != null )
						{
							filters.put( "" + plotNo, range );
						}
					}
				}
				catch( Exception ex )
				{
					
				}
			}
		}
	}
	
	private JButton getJButtonInfoFilters()
	{
		if( this.infoFilters == null )
		{
			this.infoFilters = new JButton( "?" );
			this.infoFilters.setBackground( Color.YELLOW.darker() );
			this.infoFilters.setBorder( BorderFactory.createSoftBevelBorder( SoftBevelBorder.RAISED ) );
			
			this.infoFilters.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JButton b = (JButton)e.getSource();
					settingMenu_TaskSetting setTask = (settingMenu_TaskSetting) SwingUtilities.getWindowAncestor( b );
										
					infoDialog w = new infoDialog( setTask, getFilterTextInfo() );
					
					w.setSize( 300, 200 );
					Dimension size = w.getSize();
					Point pos = b.getLocationOnScreen();
					
					Point loc = new Point( pos.x - size.width, pos.y ); 
					
					w.setLocation( loc );
					w.toFront();
					w.setVisible( true );
				}
			});
		}			
		
		return this.infoFilters;
	}
	
	private String getFilterTextInfo()
	{
		return "Filter format: N1:(A1,B1);N2:(A2,B2);N3:(A3,B3);...\n"
				+ "where\n"
				+ ">> Nx is plot number. Plot numbering starts from 1.\n"
				+ ">> Ax and Bx are interval limits.\n"
				+ "Espacial value:\n"
				+ ">> -Inf or Inf means infinity.\n"
				+ "\nIncorrect filter format are ignored.";
	}
	
	private JScrollPane getJScrollPanelPlot()
	{
		if( this.scrollpanel == null )
		{
			this.scrollpanel = new JScrollPane( this.getJPanelPlots() );
			this.scrollpanel.setVisible( true );
			this.scrollpanel.getVerticalScrollBar().setUnitIncrement( 16 );
		}
		
		return this.scrollpanel;
	}
	
	private JPanel getJPanelPlots()
	{
		if (this.panelPlots == null)
		{
			this.panelPlots = new JPanel();
			//this.panelPlots.setPreferredSize( new Dimension(500,500));
			this.panelPlots.setVisible(true);
			this.panelPlots.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));

			//this.panelPlots.setLayout(new GridLayout(0, 1, 0, 0));
			this.panelPlots.setLayout( new BoxLayout( this.panelPlots, BoxLayout.Y_AXIS ) );

			this.panelPlots.addComponentListener(new ComponentAdapter()
			{
				Timer controlTimer = null;
				int DELAY = 20;

				public void componentResized(ComponentEvent e)
				{
					JPanel p = (JPanel)e.getSource();
					Container c = p.getParent();
										
					if( c != null )
					{
						if( p.getSize().equals( c.getSize() ) )
						{
							p.setSize( c.getSize() );							
						}
						
						Component[] PLOTs = p.getComponents();
						
						Dimension plotDim = getDimensionPlots( p.getSize() );
						int aj = 10;
								
						if( PLOTs.length > 0 )
						{
							aj /= PLOTs.length;
						}
						
						if( aj < 1 )
						{
							aj = 1;
						}
						plotDim.width -= 10 ;
						plotDim.height -= aj;
						
						for( Component plot : PLOTs )
						{
							plot.setVisible( false );
							plot.setPreferredSize( plotDim );
							plot.setSize( plotDim );
							plot.setVisible( true );
						}
					}
					
					update();
				}

				private void update()
				{
					if (this.controlTimer == null)
					{
						this.controlTimer = new Timer(this.DELAY, 
								new ActionListener()
						{

							public void actionPerformed(ActionEvent ac)
							{
								if (ac.getSource() == controlTimer)
								{
									controlTimer.stop();
									controlTimer = null;

									updatePlot();
								}								                  
							}
						});
						this.controlTimer.start();
					}
					else
					{
						this.controlTimer.restart();
					}
				}
			});
		}

		return this.panelPlots;
	}

	private Dimension getDimensionPlots( Dimension containerDim )
	{
		int plotWidth = 100;
		int plotHeigh= 100;
		
		if( !this.xy.isEmpty() )
		{
			plotWidth = containerDim.width;
			plotHeigh = containerDim.height / this.xy.size();
			
			if( plotHeigh < 100 )
			{
				plotHeigh = 100;
			}
		}
		
		return new Dimension( plotWidth, plotHeigh );
	}
	
	private void updatePlot()
	{
		try 
		{	
			this.sem.acquire( this.sem.availablePermits() );
						
			if( !this.xy.isEmpty() )
			{
				
				Dimension plotDim = this.getDimensionPlots( this.getJPanelPlots().getSize() );
				int plotWidth = plotDim.width;
				int plotHeigh = plotDim.height;
				
				//List< Queue< Double > > this.xy = new ArrayList< Queue < Double > >( this.xy );
				if( this.xy.size() != this.panelPlots.getComponentCount() )
				{
					this.panelPlots.setVisible( false );
					//this.panelPlots.removeAll();

					while( this.xy.size() < this.panelPlots.getComponentCount() )
					{
						this.panelPlots.remove( this.panelPlots.getComponentCount() - 1 );
					}

					int i = this.panelPlots.getComponentCount();
					
					while( this.xy.size() >  i )
					{			
						PLOT plot = new PLOT( );
						plot.setPreferredSize( new Dimension( plotWidth, plotHeigh ) );
						
						plot.setBackground( Color.WHITE );						
						
						plot.setVisible( true );

						plot.setName( this.plotNames.get( i )  );
						//plot.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.BLACK ), "Plot " + ( i + 1 ) ) );
						plot.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.BLACK ), "Plot " + ( i + 1 ) + " - " + this.plotName ) );
						i++;

						this.panelPlots.add( plot );
					}

					this.panelPlots.setVisible( true );
				}

				if( this.newPlotName )
				{
					Component[] plots = this.panelPlots.getComponents();

					for( int i = 0; i < plots.length; i++ )
					{
						PLOT plot = (PLOT)plots[ i ];

						plot.setVisible( false );

						plot.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.BLACK ), "Plot " + ( i + 1 ) + " - " + this.plotName ) );

						plot.setVisible( true ); 
					}

					this.newPlotName = false;
				}
				
				for( int i = 0 ; i < this.xy.size(); i++ )
				{
					PLOT plot = (PLOT)this.panelPlots.getComponent( i );
										
					Queue< Double > data = this.xy.get( i );
					
					
					double[][] d = new double[ 2 ][ data.size() ];

					int j = 0;
					for( Double value : data )
					{
						d[ 0 ][ j ] = j;
						d[ 1 ][ j ] = value;
						j++;
					}

					DefaultXYDataset xyValues = new DefaultXYDataset();
					xyValues.addSeries( plot.getName(), d );
					final JFreeChart chart = ChartFactory.createXYLineChart( null, null, null, xyValues  );
					chart.setAntiAlias( true );				
					chart.setBackgroundPaint( Color.WHITE );
					chart.getXYPlot().setBackgroundPaint( Color.WHITE );
					chart.getXYPlot().setRangeGridlinePaint( Color.BLACK );
					chart.getXYPlot().setDomainGridlinePaint( Color.BLACK );
					ValueAxis yaxis = chart.getXYPlot().getRangeAxis();

					try
					{
						double mean = ( this.maxY.get( i ) + this.minY.get( i ) ) / 2;
						double amp = ( this.maxY.get( i ) - this.minY.get( i ) ) / 2;
						if( amp == 0 )
						{
							amp = mean / 2;
						}

						if( amp < 1e-15 )
						{
							amp = 1e-15;		
						}

						yaxis.setRange( mean - amp, mean + amp );	
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}

					chart.getXYPlot().setRangeAxis( yaxis );				
					chart.clearSubtitles();
					plot.setChart( chart );
					
					if( !plot.getSize().equals( plot.getPreferredSize() ) )
					{
						plot.setSize( plot.getPreferredSize() );	
					}
					
					if( !plot.getSize().equals( new Dimension() ) )
					{
						drawPlot( chart, plot );
					}
				}
			}
			else
			{
				this.panelPlots.setVisible( false );
				this.panelPlots.removeAll();
				this.panelPlots.setVisible( true );
			}
		} 
		catch (InterruptedException e1) 
		{
			//e1.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{	
			if( this.sem.availablePermits() < 1 )
			{
				this.sem.release( );
			}
		}
	}

	private void drawPlot(JFreeChart chart, JLabel canva)
	{
		if ((chart != null) && (canva != null))
		{
			Rectangle r = canva.getBounds();
			Insets pad = canva.getBorder().getBorderInsets(canva);
			int w = r.width - pad.left - pad.right;
			int h = r.height - pad.top - pad.bottom;
			if ((w > 0) && (h > 0))
			{
				Image img = chart.createBufferedImage(w, h);


				chart.draw( (Graphics2D)img.getGraphics(), 
						new Rectangle2D.Double( 0, 0, img.getWidth( null ), img.getHeight( null ) ) );

				canva.setIcon(new ImageIcon(img));
			}
		}
	}


	public boolean addXYData(List<List<Double>> XY)
	{
		boolean adding = this.sem.tryAcquire(this.sem.availablePermits());

		if (adding)
		{
			Iterator<List<Double>> it = XY.iterator();

			this.plotNames.clear();
			int inputDataIndex = 0;
			int plotIndex = 0;
			while (it.hasNext())
			{
				List<Double> xyValues = it.next();

				if (!this.ignoredCols.contains( inputDataIndex ) )
				{
					Queue<Double> queue = null;

					if (plotIndex >= this.xy.size())
					{
						queue = new LinkedList< Double >();
						this.minY.add( Double.MAX_VALUE );
						this.maxY.add( Double.MIN_VALUE );

						this.xy.add(queue);
					}
					else
					{
						queue = this.xy.get( plotIndex );
					}

					this.plotNames.add( "" + inputDataIndex);

					queue.addAll( xyValues );
					if (this.queueLength > 0)
					{
						while ( queue.size() > this.queueLength )
						{
							queue.poll();
						}
					}
					
					NumberRange rng = this.filters.get( "" + ( plotIndex + 1 ) );
					if( rng != null )
					{
						Iterator< Double > itDouble = queue.iterator();
						
						while( itDouble.hasNext() )
						{
							double val = itDouble.next();
							if( !rng.within( val) )
							{
								itDouble.remove();
							}
						}
					}

					double min = Double.MAX_VALUE;
					double max = Double.MIN_VALUE;

					for (Double v : queue)
					{
						if ( (v.doubleValue() != Double.NaN )
								&& (v.doubleValue() > max))
						{
							max = v.doubleValue();
						}

						if ((v.doubleValue() != Double.NaN ) 
								&&  (v.doubleValue() < min) )
						{
							min = v.doubleValue();
						}
					}

					if (plotIndex >= this.minY.size())
					{
						this.minY.add( min );
						this.maxY.add( max );
					}
					else
					{
						this.minY.set(plotIndex, min );
						this.maxY.set(plotIndex, max );
					}

					plotIndex++;
				}

				inputDataIndex++;
			}

			while (this.xy.size() > inputDataIndex - this.ignoredCols.size())
			{
				this.xy.remove(this.xy.size() - 1);
			}


			this.sem.release();

			this.updatePlot();
		}

		return adding;
	}

	public void setDataLength(int length)
	{
		this.queueLength = length;
	}

	public void clearData()
	{
		try
		{
			this.sem.acquire( this.sem.availablePermits() );
						
			this.xy.clear();
			
			JPanel p = this.getJPanelPlots();
			p.setVisible( false );
			p.removeAll();
			p.setVisible( true );
						
			if( this.sem.availablePermits() < 1 )
			{
				this.sem.release();
			}
		}
		catch( Exception ex )
		{		
			
		}
	}

	//////////////////////////////////////
	/////////////////////////////////////
	//////////////////////////////////////
	private class PLOT extends JLabel
	{
		private static final long serialVersionUID = 1L;

		private JFreeChart chart = null;

		public PLOT() 
		{
			super();
			
			super.addComponentListener( new ComponentAdapter()
			{				
				@Override
				public void componentResized(ComponentEvent e) 
				{
					JLabel canvas = (JLabel)e.getSource();
					JFreeChart jfChart = getChart();
					
					drawPlot( jfChart, canvas );
				}
			});
		}

		public JFreeChart getChart()
		{
			return this.chart;
		}

		public void setChart(JFreeChart c)
		{
			this.chart = c;
		}
	}
}
