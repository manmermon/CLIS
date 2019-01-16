package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import Auxiliar.EmotionParameter;
import Config.ConfigApp;
import GUI.MyComponents.GeneralAppIcon;

import javax.swing.BorderFactory;
import javax.swing.Icon;

public class settingMenu_SAMEditor extends JDialog 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8342629954922322312L;
	
	// Panels
	private JPanel panelValence;
	private JPanel panelArousal;
	private JPanel panelDominance;
	private JPanel panelValenceSize;
	private JPanel panelArousalSize;
	private JPanel panelDominanceSize;
	private JPanel panelValenceIcons;
	private JPanel panelArousalIcons;
	private JPanel panelDominanceIcons;
	private JPanel panelEmotionsIcons;
	
	
	// JSpinner
	private JSpinner valenceSize;
	private JSpinner arousalSize; 
	private JSpinner dominanceSize;
	
	// button
	/*
	private JButton applyValence;
	private JButton applyArousal;
	private JButton applyDominance;
	*/
	
	/*
	public static void main(String[] args) {
		try {
			settingMenu_SAMEditor dialog = new settingMenu_SAMEditor( null, "T");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	
	/**
	 * Create the dialog.
	 */
	public settingMenu_SAMEditor( Window owner, String title ) 
	{
		super( owner );
		
		super.setTitle( title );
		
		super.getRootPane().registerKeyboardAction( keyActions.getEscapeCloseWindows( "EscapeCloseWindow" ), 
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		GridLayout ly = new GridLayout( 4, 1 );
		super.getContentPane().setLayout( ly );
					
		super.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		super.setBounds(100, 100, 600, 600);
		
		super.getContentPane().add( this.getPanelValenceSize() );
		super.getContentPane().add( this.getPanelArousalSize() );
		super.getContentPane().add( this.getPanelDominanceSize() );
		super.getContentPane().add( this.getJPanelEmotions() );
		
		/*
		this.getApplyValence().doClick();
		this.getApplyArousal().doClick();
		this.getApplyDominance().doClick();
		*/
		this.applyScale( 0, (Integer)this.getJSpinnerValenceSize().getValue(), this.getPanelValenceIcons() );
		this.applyScale( 1, (Integer)this.getJSpinnerArousalSize().getValue(), this.getPanelArousalIcons() );
		this.applyScale( 2, (Integer)this.getJSpinnerDominanceSize().getValue(), this.getPanelDominanceIcons() );		
		
	}
	
	private JPanel getJPanelEmotions()
	{
		if( this.panelEmotionsIcons == null )
		{
			final String ID = ConfigApp.SAM_EMOTION_SET;
			
			List< EmotionParameter > emotionList = (List< EmotionParameter >)ConfigApp.getProperty( ID ); 
			
			this.panelEmotionsIcons = new JPanel();
			
			this.panelEmotionsIcons.setBorder( BorderFactory.createTitledBorder( "Emotions" ) );
			this.panelEmotionsIcons.setLayout( new GridLayout() );
			
			for( int i = 1; i < 8; i++ )
			{
				final EmotionParameter emotion = emotionList.get( i - 1 );
				
				JPanel emoPanel = new JPanel( new BorderLayout() );
				
				JButton emo = new JButton();
									
				Dimension d = emo.getPreferredSize();
				int side = d.width;
				if (side > d.height)
				{
					side = d.height;
				}
				
				final int level = i;
				
				Icon ico = GeneralAppIcon.getBasicEmotion( level, side, Color.BLACK, Color.WHITE, "", emo.getFontMetrics( emo.getFont() ) );				
				emo.setIcon( ico );
				
				emo.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent arg0) 
					{
						JButton b = (JButton)arg0.getSource();
						Dimension d = b.getSize();

						int side = d.width;
						if (side > d.height)
						{
							side = d.height;
						}

						b.setIcon( GeneralAppIcon.getBasicEmotion( level, side, Color.BLACK, Color.WHITE, "", emo.getFontMetrics( emo.getFont() ) ) );

						b.setPreferredSize( b.getSize() );
					}
				});
				
				JTextField emoText = new JTextField();
				emoText.setText( emotion.getText() );
				
				emoText.getDocument().addDocumentListener(new DocumentListener() 
				{
					public void changedUpdate(DocumentEvent e) 
					{
						update( e );
					}

					public void removeUpdate(DocumentEvent e) 
					{
						update( e );
					}

					public void insertUpdate(DocumentEvent e) 
					{

						update( e );
					}

					public void update( DocumentEvent e ) 
					{
						try 
						{
							String txt = e.getDocument().getText( 0, e.getDocument().getLength() );
							emotion.setText( txt );
						}
						catch (BadLocationException e1) 
						{
							e1.printStackTrace();
						}						 
					}
				});
				
				JCheckBox select = new JCheckBox( "Select" );
								
				select.addItemListener( new ItemListener() 
				{					
					@Override
					public void itemStateChanged(ItemEvent e) 
					{
						JCheckBox sel = (JCheckBox)e.getSource();
						
						if( sel.isSelected() )
						{
							sel.setText( "Select" );
						}
						else
						{
							sel.setText( "Unselect");
						}
						
						emotion.setSelect( sel.isSelected() );
					}
				});
				
				select.setSelected( !emotion.isSelect() );
				select.doClick();
				
				emoPanel.add( select, BorderLayout.NORTH );
				emoPanel.add( emo, BorderLayout.CENTER );
				emoPanel.add( emoText, BorderLayout.SOUTH );
				
				this.panelEmotionsIcons.add( emoPanel );
			}
		}
		
		return this.panelEmotionsIcons;
	}
	
	private JPanel getPanelValenceSize()
	{
		if( this.panelValence == null )
		{
			this.panelValence = new JPanel();
			
			this.panelValence.setLayout( new BorderLayout());
			
			this.panelValence.add( this.getPanelValence(), BorderLayout.NORTH );
			this.panelValence.add( this.getPanelValenceIcons(), BorderLayout.CENTER );
		}
		
		return this.panelValence;
	}
	
	private JPanel getPanelValence()
	{
		if( this.panelValenceSize == null )
		{
			this.panelValenceSize = new JPanel();
			
			this.panelValenceSize.setLayout( new FlowLayout( FlowLayout.LEFT ) );
			
			this.panelValenceSize.add( new JLabel( "Valence range: " ) );
			this.panelValenceSize.add( this.getJSpinnerValenceSize()  );
			//this.panelValenceSize.add( this.getApplyValence()  );
		}
		
		return this.panelValenceSize;
	}
	
	/*
	private JButton getApplyValence()
	{
		if( this.applyValence == null )
		{
			this.applyValence = new JButton( "Apply" );
			
			this.applyValence.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JSpinner sp = getJSpinnerValenceSize();
					Integer v = (Integer)sp.getValue();
					
					JPanel p = getPanelValenceIcons();
					
					p.setVisible( false );
					p.removeAll();
										
					float step = 1.0F / ( v - 1 );
					if( v - 1 <= 0 )
					{
						step = 1F;
					}
					
					for( int i = 1; i <= v; i++  )
					{
						JButton b = new JButton();
						
						Dimension d = b.getPreferredSize();
						int side = d.width;
						if (side > d.height)
						{
							side = d.height;
						}
						
						final float propSize = ( i - 1 ) * step;
						
						b.setIcon( GeneralAppIcon.getSAMValence( propSize,  side, Color.BLACK, Color.WHITE ) );
												
						b.addComponentListener(new ComponentAdapter()
						{
							public void componentResized(ComponentEvent arg0) 
							{
								JButton b = (JButton)arg0.getSource();
								Dimension d = b.getSize();

								int side = d.width;
								if (side > d.height)
								{
									side = d.height;
								}

								b.setIcon(GeneralAppIcon.getSAMValence( propSize, side, Color.BLACK, Color.WHITE));
								
								b.setPreferredSize( b.getSize() );
							}
						});
						
						p.add( b );
					}
					
					p.setVisible( true );
				}
			});
			
		}
		
		return this.applyValence;
	}
	*/
	
	private JSpinner getJSpinnerValenceSize()
	{
		if( this.valenceSize == null )
		{
			final String ID = ConfigApp.SAM_VALENCE_SCALE;
			
			Integer scale = (Integer)ConfigApp.getProperty( ID );
			
			this.valenceSize = new JSpinner();
			
			this.valenceSize.setModel(new SpinnerNumberModel( new Integer( 9 ), new Integer(2), new Integer( 9 ) , new Integer(1)));
			
			try
			{
				this.valenceSize.setValue( scale );
			}
			catch( IllegalArgumentException ex )
			{
				
			}
			
			this.valenceSize.addMouseWheelListener( new MouseWheelListener() 
			{				
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) 
				{
					if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
					{
						try
						{
							JSpinner sp = (JSpinner)e.getSource();

							int d = e.getWheelRotation();

							if( d > 0 )
							{
								sp.setValue( sp.getModel().getPreviousValue() );
							}
							else
							{
								sp.setValue( sp.getModel().getNextValue() );
							}
							
							ConfigApp.setProperty( ID, sp.getValue() );
							
							Integer v = (Integer)sp.getValue();
							
							JPanel p = getPanelValenceIcons();
							
							applyScale( 0, v, p );
						}
						catch( IllegalArgumentException ex )
						{

						}
					}
				}
			});
		}
		
		return this.valenceSize;
	}
	
	private void applyScale( final int type, int scale, JPanel p )
	{	
		p.setVisible( false );
		p.removeAll();
							
		float step = 1.0F / ( scale - 1 );
		if( scale - 1 <= 0 )
		{
			step = 1F;
		}
		
		for( int i = 1; i <= scale; i++  )
		{
			JButton b = new JButton();
			
			Dimension dim = b.getPreferredSize();
			int side = dim.width;
			if (side > dim.height)
			{
				side = dim.height;
			}
			
			final float propSize = ( i - 1 ) * step;
			
			if( type == 0 )
			{			
				b.setIcon( GeneralAppIcon.getSAMValence( propSize,  side, Color.BLACK, Color.WHITE ) );
			}
			else if (type == 1 )
			{
				b.setIcon( GeneralAppIcon.getSAMArousal( propSize,  side, Color.BLACK, Color.WHITE ) );
			}
			else
			{
				b.setIcon( GeneralAppIcon.getSAMDominance( propSize,  side, Color.BLACK, Color.WHITE ) );
			}
									
			b.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent arg0) 
				{
					JButton b = (JButton)arg0.getSource();
					Dimension d = b.getSize();

					int side = d.width;
					if (side > d.height)
					{
						side = d.height;
					}

					if( type == 0 )
					{			
						b.setIcon( GeneralAppIcon.getSAMValence( propSize,  side, Color.BLACK, Color.WHITE ) );
					}
					else if (type == 1 )
					{
						b.setIcon( GeneralAppIcon.getSAMArousal( propSize,  side, Color.BLACK, Color.WHITE ) );
					}
					else
					{
						b.setIcon( GeneralAppIcon.getSAMDominance( propSize,  side, Color.BLACK, Color.WHITE ) );
					}
					
					b.setPreferredSize( b.getSize() );
				}
			});
			
			p.add( b );
		}
		
		p.setVisible( true );
	}
	
	private JPanel getPanelValenceIcons()
	{
		if( this.panelValenceIcons == null )
		{
			this.panelValenceIcons = new JPanel();
			this.panelValenceIcons.setLayout(new GridLayout() );
			
		}
		
		return this.panelValenceIcons;
	}
	
	private JPanel getPanelArousalSize()
	{
		if( this.panelArousal == null )
		{
			this.panelArousal = new JPanel();
			
			this.panelArousal.setLayout( new BorderLayout());
			
			this.panelArousal.add( this.getPanelArousal(), BorderLayout.NORTH );
			this.panelArousal.add( this.getPanelArousalIcons(), BorderLayout.CENTER );
		}
		
		return this.panelArousal;
	}
	
	private JPanel getPanelArousal()
	{
		if( this.panelArousalSize == null )
		{
			this.panelArousalSize = new JPanel();
			
			this.panelArousalSize.setLayout( new FlowLayout( FlowLayout.LEFT ) );
			
			this.panelArousalSize.add( new JLabel( "Arousal range: " ) );
			this.panelArousalSize.add( this.getJSpinnerArousalSize()  );
			//this.panelArousalSize.add( this.getApplyArousal() );
		}
		
		return this.panelArousalSize;
	}
	
	/*
	private JButton getApplyArousal()
	{
		if( this.applyArousal == null )
		{
			this.applyArousal = new JButton( "Apply" );
			
			this.applyArousal.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JSpinner sp = getJSpinnerArousalSize();
					Integer v = (Integer)sp.getValue();
					
					JPanel p = getPanelArousalIcons();
					
					p.setVisible( false );
					p.removeAll();
										
					float step = 1.0F / ( v - 1 );
					if( v - 1 <= 0 )
					{
						step = 1F;
					}
					
					for( int i = 1; i <= v; i++  )
					{
						JButton b = new JButton();
						
						Dimension d = b.getPreferredSize();
						int side = d.width;
						if (side > d.height)
						{
							side = d.height;
						}
						
						final float propSize = ( i - 1 ) * step;
						
						b.setIcon( GeneralAppIcon.getSAMArousal( propSize,  side, Color.BLACK, Color.WHITE ) );
												
						b.addComponentListener(new ComponentAdapter()
						{
							public void componentResized(ComponentEvent arg0) 
							{
								JButton b = (JButton)arg0.getSource();
								Dimension d = b.getSize();

								int side = d.width;
								if (side > d.height)
								{
									side = d.height;
								}

								b.setIcon(GeneralAppIcon.getSAMArousal( propSize, side, Color.BLACK, Color.WHITE));
								
								b.setPreferredSize( b.getSize() );
							}
						});
						
						p.add( b );
					}
					
					p.setVisible( true );
				}
			});
		}
		
		return this.applyArousal;
	}
	*/
	
	private JSpinner getJSpinnerArousalSize()
	{
		if( this.arousalSize == null )
		{
			final String ID = ConfigApp.SAM_AROUSAL_SCALE;
			
			Integer scale = (Integer)ConfigApp.getProperty( ID );
			
			this.arousalSize = new JSpinner();
			
			this.arousalSize.setModel(new SpinnerNumberModel( new Integer( 9 ), new Integer(2), new Integer( 9 ) , new Integer(1)));
			
			try
			{
				this.arousalSize.setValue( scale );
			}
			catch( IllegalArgumentException ex )
			{
				
			}
			
			this.arousalSize.addMouseWheelListener( new MouseWheelListener() 
			{				
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) 
				{
					if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
					{
						try
						{
							JSpinner sp = (JSpinner)e.getSource();

							int d = e.getWheelRotation();

							if( d > 0 )
							{
								sp.setValue( sp.getModel().getPreviousValue() );
							}
							else
							{
								sp.setValue( sp.getModel().getNextValue() );
							}
							
							ConfigApp.setProperty( ID, sp.getValue() );
							
							Integer v = (Integer)sp.getValue();
							
							JPanel p = getPanelArousalIcons();
							
							applyScale( 1, v, p );
						}
						catch( IllegalArgumentException ex )
						{

						}
					}
				}
			});
		}
		
		return this.arousalSize;
	}
	
	private JPanel getPanelArousalIcons()
	{
		if( this.panelArousalIcons == null )
		{
			this.panelArousalIcons = new JPanel();
			this.panelArousalIcons.setLayout(new GridLayout() );
		}
		
		return this.panelArousalIcons;
	}
			
	private JPanel getPanelDominanceSize()
	{
		if( this.panelDominance == null )
		{
			this.panelDominance = new JPanel();
			
			this.panelDominance.setLayout( new BorderLayout());
			
			this.panelDominance.add( this.getPanelDominance(), BorderLayout.NORTH );
			this.panelDominance.add( this.getPanelDominanceIcons(), BorderLayout.CENTER );
		}
		
		return this.panelDominance;
	}
	
	private JPanel getPanelDominance()
	{
		if( this.panelDominanceSize == null )
		{
			this.panelDominanceSize = new JPanel();
			
			this.panelDominanceSize.setLayout( new FlowLayout( FlowLayout.LEFT ) );
			
			this.panelDominanceSize.add( new JLabel( "Dominance range: " ) );
			this.panelDominanceSize.add( this.getJSpinnerDominanceSize()  );
			//this.panelDominanceSize.add( this.getApplyDominance() );
		}
		
		return this.panelDominanceSize;
	}
	
	/*
	private JButton getApplyDominance()
	{
		if( this.applyDominance == null )
		{
			this.applyDominance = new JButton( "Apply" );
		
			this.applyDominance.addActionListener( new ActionListener() 
			{				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JSpinner sp = getJSpinnerDominanceSize();
					Integer v = (Integer)sp.getValue();
					
					JPanel p = getPanelDominanceIcons();
					
					p.setVisible( false );
					p.removeAll();
										
					float step = 1.0F / ( v - 1 );
					if( v - 1 <= 0 )
					{
						step = 1F;
					}
					
					for( int i = 1; i <= v; i++  )
					{
						JButton b = new JButton();
						
						Dimension d = b.getPreferredSize();
						int side = d.width;
						if (side > d.height)
						{
							side = d.height;
						}
						
						final float propSize = ( i - 1 ) * step;
						
						b.setIcon( GeneralAppIcon.getSAMDominance( propSize,  side, Color.BLACK, Color.WHITE ) );
												
						b.addComponentListener(new ComponentAdapter()
						{
							public void componentResized(ComponentEvent arg0) 
							{
								JButton b = (JButton)arg0.getSource();
								Dimension d = b.getSize();

								int side = d.width;
								if (side > d.height)
								{
									side = d.height;
								}

								b.setIcon(GeneralAppIcon.getSAMDominance( propSize, side, Color.BLACK, Color.WHITE));
								
								b.setPreferredSize( b.getSize() );
							}
						});
						
						p.add( b );
					}
					
					p.setVisible( true );
				}
			});
		}
		
		return this.applyDominance;
	}
	*/
	
	private JSpinner getJSpinnerDominanceSize()
	{
		if( this.dominanceSize == null )
		{
			final String ID = ConfigApp.SAM_DOMINANCE_SCALE;
			
			Integer scale = (Integer)ConfigApp.getProperty( ID );
			
			this.dominanceSize = new JSpinner();
			
			this.dominanceSize.setModel(new SpinnerNumberModel( new Integer( 9 ), new Integer(2), new Integer( 9 ) , new Integer(1)));
			
			try
			{
				this.dominanceSize.setValue( scale );
			}
			catch( IllegalArgumentException ex )
			{
				
			}
			
			this.dominanceSize.addMouseWheelListener( new MouseWheelListener() 
			{				
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) 
				{
					if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
					{
						try
						{
							JSpinner sp = (JSpinner)e.getSource();

							int d = e.getWheelRotation();

							if( d > 0 )
							{
								sp.setValue( sp.getModel().getPreviousValue() );
							}
							else
							{
								sp.setValue( sp.getModel().getNextValue() );
							}
							
							ConfigApp.setProperty( ID, sp.getValue() );
							
							Integer v = (Integer)sp.getValue();
							
							JPanel p = getPanelDominanceIcons();
							
							applyScale( 2, v, p );
							
						}
						catch( IllegalArgumentException ex )
						{

						}
					}
				}
			});
		}
		
		return this.dominanceSize;
	}
	
	private JPanel getPanelDominanceIcons()
	{
		if( this.panelDominanceIcons == null )
		{
			this.panelDominanceIcons = new JPanel();
			this.panelDominanceIcons.setLayout(new GridLayout() );
		}
		
		return this.panelDominanceIcons;
	}
	
}
