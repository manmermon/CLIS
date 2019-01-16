package GUI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.jfree.chart.axis.Tick;
import org.jgrasstools.gears.utils.math.interpolation.splines.Bspline;

import com.vividsolutions.jts.geom.Coordinate;

public class GeneralAppIcon
{
	public static final int SMALL_SIZE_ICON = 16;
	public static final int REGULAR_SIZE_ICON = 32;
	public static final int BIG_SIZE_ICON = 48;
	public static final int BIG_PLUS_SIZE_ICON = 64;
	public static final int HUGE_SIZE_ICON = 128;

	public static ImageIcon WindowMax( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 8;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null );

			imagenPoligono2D.crearImagenLineaPoligonal( new int[ ]{ size / 2 - thick, thick, thick, size - thick/2, size - thick/2 }, 
					new int[ ]{ thick/2, thick/2, size - thick/2, size - thick/2, size / 2 + thick}, 
					thick, color, 
					img);

			imagenPoligono2D.crearImagenPoligonoRelleno( new int[]{ size / 2, size, size }, 
					new int[]{ 0, 0, size / 2 }, 
					color, img);

			ico = new ImageIcon( img );
		}

		return ico;
	}
	
	public static ImageIcon WindowMinimize( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 8;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null );

			imagenPoligono2D.crearImagenLinea( 0, size - thick, size, size - thick, thick, color, img);

			Image imgTr = imagenPoligono2D.crearImagenTriangulo( size / 3 + thick, 1, color, color, imagenPoligono2D.SOUTH );
			imagenPoligono2D.componerImagen( img, ( size - imgTr.getWidth( null ) ) / 2 , size - imgTr.getHeight( null ) - thick, imgTr );

			imagenPoligono2D.crearImagenLinea( size / 2 - thick / 2 , 0, size / 2 - thick / 2, size / 2, thick, color, img );
			
			ico = new ImageIcon( img );
		}

		return ico;
	}
	
	public static ImageIcon WindowUndoMaximize( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 8;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null );

			Image r = imagenPoligono2D.crearImagenRectangulo( ( 3 * size ) / 4, ( 3 * size ) / 4, thick, color, null );

			imagenPoligono2D.componerImagen( img, 0, size - r.getHeight( null), r );
			imagenPoligono2D.crearImagenLinea( size / 2 - thick / 2, size - r.getHeight( null), size / 2 - thick / 2, 0, thick, color, img );
			imagenPoligono2D.crearImagenLinea( size / 2 - thick / 2, thick / 2, size, thick / 2, thick, color, img );
			imagenPoligono2D.crearImagenLinea( size - thick / 2, 0, size - thick / 2, size / 2, thick, color, img );
			imagenPoligono2D.crearImagenLinea( r.getWidth( null ), size / 2, size, size / 2, thick, color, img );
			
			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon NewFile( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearImagenRectangulo( size, size, thick, color, null );

			imagenPoligono2D.crearImagenLinea( size / 4, size / 4, size - size / 4, size / 4, thick, color, img );
			imagenPoligono2D.crearImagenLinea( size / 4, size / 2, size - size / 4, size / 2, thick, color, img );
			imagenPoligono2D.crearImagenLinea( size / 4, ( 3 * size ) / 4, size - size / 4, ( 3 * size ) / 4, thick, color, img );

			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon LoadFile( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			int w = size / 2;

			Image img = imagenPoligono2D.componerImagen( 
					imagenPoligono2D.crearLienzoVacio( size, size, null ), 
					0, 0,  
					imagenPoligono2D.crearImagenRectangulo( w - thick, size - thick, thick, color, null ) );

			imagenPoligono2D.crearImagenLinea( w / 4, size / 4, w - w / 4 - thick, size / 4, thick, color, img );
			imagenPoligono2D.crearImagenLinea( w / 4, size / 2, w - w / 4 - thick, size / 2, thick, color, img );
			imagenPoligono2D.crearImagenLinea( w / 4, ( 3 * size ) / 4, w - w / 4 - thick, ( 3 * size ) / 4, thick, color, img );

			imagenPoligono2D.crearImagenPoligonoRelleno( new int[]{ w + thick, w + thick, size }, 
					new int[]{ size / 4, size - size / 4, size / 2 }, 
					color, img);

			ico = new ImageIcon( img );

		}

		return ico;
	}

	public static ImageIcon LoadFolder( int width, int height, Color borderColor, Color fillColor )
	{
		int w = width - 2;
		int h = height - 2;

		if( borderColor == null && fillColor == null )
		{
			borderColor = Color.BLACK;
		}

		Image img = imagenPoligono2D.crearLienzoVacio( w, h, null );

		Image imgAux = Folder( 2 * w / 3, h, borderColor, fillColor ).getImage();
		Image imgAux2 = imagenPoligono2D.crearImagenTriangulo( w / 3, 1, borderColor, borderColor, imagenPoligono2D.EAST );

		imagenPoligono2D.componerImagen( img, 0, 0, imgAux );
		return new ImageIcon( imagenPoligono2D.componerImagen( img, 2 * w / 3, h / 2 - w / 6, imgAux2 ) );
	}

	public static ImageIcon SaveFile( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			int w = size / 2;

			Image img = imagenPoligono2D.componerImagen( 
					imagenPoligono2D.crearLienzoVacio( size, size, null ), 
					0, 0,  
					imagenPoligono2D.crearImagenRectangulo( w - thick, size - thick, thick, color, null ) );

			imagenPoligono2D.crearImagenLinea( w / 4, size / 4, w - w / 4 - thick, size / 4, thick, color, img );
			imagenPoligono2D.crearImagenLinea( w / 4, size / 2, w - w / 4 - thick, size / 2, thick, color, img );
			imagenPoligono2D.crearImagenLinea( w / 4, ( 3 * size ) / 4, w - w / 4 - thick, ( 3 * size ) / 4, thick, color, img );

			imagenPoligono2D.crearImagenPoligonoRelleno( new int[]{ size, size, w + thick }, 
					new int[]{ size / 4, size - size / 4, size / 2 }, 
					color, img);

			ico = new ImageIcon( img );

		}

		return ico;
	}

	public static ImageIcon Error( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null );

			imagenPoligono2D.crearImagenLinea( 0, 0, size, size, thick, color, img );
			imagenPoligono2D.crearImagenLinea( 0, size, size, 0, thick, color, img );

			imagenPoligono2D.componerImagen( img, size / 4, size / 4, 
					imagenPoligono2D.crearImagenRectangulo( size / 2, size / 2, thick, color, null ) );

			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon Question( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			//Image img = imagenPoligono2D.crearLienzoVacio( size, size, null );

			Image img = imagenPoligono2D.crearImagenRectangulo( size-thick/2, size-thick/2, thick, color, null );

			Font f = new Font( Font.DIALOG, Font.BOLD, 12 );
			FontMetrics fm = img.getGraphics().getFontMetrics( f );			
			while( fm.getHeight() < size )
			{
				f = new Font( f.getName(), Font.BOLD, f.getSize() + 1 );
				fm = img.getGraphics().getFontMetrics( f );
			}

			Image imgTxt = imagenPoligono2D.crearImagenTexto( 0, 0, "?", fm, color, color, null );
			int x = (int)Math.round( ( size - imgTxt.getWidth( null ) ) / 2.0 );
			int y = 0;
			img = imagenPoligono2D.componerImagen( img, x, y, imgTxt );

			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon Warning( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{
			int thick = size / 8;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null );

			imagenPoligono2D.crearImagenPoligonoPerfil( new int[]{ thick - 1, size / 2, size - thick + 1},
					new int[]{ size - thick/2, thick, size -thick/2},
					thick, color, img );

			Font f = new Font( Font.DIALOG, Font.PLAIN, 12 );
			FontMetrics fm = img.getGraphics().getFontMetrics( f );			
			while( fm.getHeight() < ( size * 3 ) / 4 )
			{
				f = new Font( f.getName(), Font.PLAIN, f.getSize() + 1 );
				fm = img.getGraphics().getFontMetrics( f );
			}

			while( fm.getHeight() > ( size * 3 ) / 4 )
			{
				f = new Font( f.getName(), Font.PLAIN, f.getSize() - 1 );
				fm = img.getGraphics().getFontMetrics( f );
			}

			Image imgTxt = imagenPoligono2D.crearImagenTexto( 0, 0, "!", fm, color, color, null );
			int x = (int)Math.round( ( size - imgTxt.getWidth( null ) ) / 2.0 );
			int y = size - imgTxt.getHeight( null ) + 1;
			img = imagenPoligono2D.componerImagen( img, x, y, imgTxt );

			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon ImageIcon( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{	
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearImagenRectangulo( size, size, 1, color, null);

			int[] x = new int[ size ];
			int[] y = new int[ size ];
			int h = (int)( size * 0.75 );
			for( int i = 0; i < size; i++ )
			{
				x[ i ] = i;
				y[ i ] = (int)(( ( Math.sin( ( i * 4 * Math.PI ) / size ) + 1 ) / 2 ) * h ) + ( size - h ) / 2;
			}

			imagenPoligono2D.crearImagenLineaPoligonal( x, y, thick, color, img );

			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon Close( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{	
			int thick = size / 3;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null  );

			imagenPoligono2D.crearImagenLinea( 0, 0, size, size, thick, color, img );
			imagenPoligono2D.crearImagenLinea( 0, size, size, 0, thick, color, img );

			ico = new ImageIcon( img );
		}

		return ico;
	}

	public static ImageIcon Pencil( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{	
			int thick = size / 16;

			if( thick < 1 )
			{
				thick = 1;
			}

			int shiftThick = thick / 2;
			if( shiftThick == 0 )
			{
				shiftThick = 1;
			}

			Image img = imagenPoligono2D.crearLienzoVacio( size, size, null  );

			double div = size / 10.0;
			int[] xs = new int[]{ size / 2 , size - shiftThick, size / 2, shiftThick  };			
			int[] ys = new int[]{ shiftThick, size / 2, size - shiftThick, size / 2  };

			imagenPoligono2D.crearImagenPoligonoPerfil(xs, ys, thick, color, img );

			imagenPoligono2D.crearImagenLinea( (int)( div * 1.5 ) + shiftThick, (int)( div * 6.5 ) - shiftThick, (int)( div * 6.5 ) - shiftThick, (int)( div * 1.5 ) + shiftThick, thick, color, img);
			imagenPoligono2D.crearImagenLinea( (int)( div * 3.5 ) + shiftThick, (int)( div * 8.5 ) - shiftThick, (int)( div * 8.5) - shiftThick, (int)( div * 3.5 ) + shiftThick, thick, color, img);

			imagenPoligono2D.crearImagenLinea( shiftThick, size / 2 + shiftThick, shiftThick, size, thick, color, img);
			imagenPoligono2D.crearImagenLinea( 0, size - shiftThick, size / 2 - shiftThick, size - shiftThick, thick, color, img);

			xs = new int[]{ shiftThick, shiftThick, (int)( 2.5 * div ) };
			ys = new int[]{ (int)( 7.5 * div ), size - shiftThick, size - shiftThick };

			imagenPoligono2D.crearImagenPoligonoRelleno(xs, ys, color, img );

			ico = new ImageIcon( img );			
		}

		return ico;
	}

	public static ImageIcon Config(  )
	{
		Image icono = null, aux = null;		
		Color colorRelleno = Color.LIGHT_GRAY;
		Color colorBorde = Color.BLACK;
		int width = 100,  height = 100;
		icono = imagenPoligono2D.crearLienzoVacio( width, height, null );

		int xs[] = { 0, 0, width / 6, width / 6, 2 * width / 6 , 2 * width / 6, width / 2, width / 2 };
		int ys[] = { height / 4, 0, 0, height / 4, height / 4, 0, 0, height / 4 };

		imagenPoligono2D.crearImagenPoligonoRelleno( xs, ys, colorRelleno, icono );
		imagenPoligono2D.crearImagenPoligonoPerfil( xs, ys, 1.0F, colorBorde, icono );		

		aux = imagenPoligono2D.crearImagenRectangulo( width / 6 , height / 2 -1, 1.0F, colorBorde, colorRelleno );		
		imagenPoligono2D.componerImagen( icono, width / 6 , height / 2 , aux );

		imagenPoligono2D.crearImagenArco( 0, 0, width / 2, height / 2, 
				180, 180, 1.0F, colorBorde, colorRelleno, icono );
		imagenPoligono2D.crearImagenLinea( width / 6, height / 4, 2 * width / 6, height / 4 , 1.0F, colorBorde, icono );
		xs = new int[ 8 ];
		ys = new int[ 8 ];		
		xs[ 0 ] = width / 2 + width / 6; xs[ 1 ] = xs[ 0 ]; xs[ 2 ] = xs[ 1 ] - width / 12; xs[ 3 ] = xs[ 0 ]; xs[ 4 ] = xs[ 3 ] + width / 6 ; xs[ 5 ] = xs[ 4 ] + width / 12; xs[ 6 ] = xs[ 4 ]; xs[ 7 ] = xs[ 6 ];
		ys[ 0 ] = height / 2; ys[ 1 ] = ys[ 0 ] - height / 6; ys[ 2 ] = ys[ 1 ] - height / 4; ys[ 3 ] = 0; ys[ 4 ] = ys[ 3 ]; ys[ 5 ] = ys[ 2 ]; ys[ 6 ] = ys[ 1 ]; ys[ 7 ] = ys[ 0 ];

		imagenPoligono2D.crearImagenPoligonoRelleno( xs, ys, colorRelleno, icono );
		imagenPoligono2D.crearImagenPoligonoPerfil( xs, ys, 1.0F, colorBorde, icono );


		aux = imagenPoligono2D.crearImagenRectangulo( width / 6 + 4, height / 2, 1.0F, colorBorde, Color.RED);
		imagenPoligono2D.componerImagen( icono, width / 2 + width / 6 - 2, height / 2, aux );

		return new ImageIcon( icono );
	}

	public static ImageIcon getIconoAplicacion( int width, int height  )
	{
		Image icono = null;
		Color colorBorde = Color.BLACK;
		//int width = 100, height = 100;
		float thicknessBorder = 1.5F;

		icono = imagenPoligono2D.crearLienzoVacio( width, height, null );

		imagenPoligono2D.crearImagenArco( 0, 0, width - 1 , height - 1, 270, 90, thicknessBorder, colorBorde, Color.CYAN , icono );
		imagenPoligono2D.crearImagenArco( 0, 0, width - 1 , height - 1, 0, 90, thicknessBorder, colorBorde, Color.MAGENTA , icono );		
		imagenPoligono2D.crearImagenArco( 0, 0, width-1, height-1 , 90, 90, thicknessBorder, colorBorde, Color.ORANGE, icono );
		imagenPoligono2D.crearImagenArco( 0, 0, width-1, height-1 , 180, 90, thicknessBorder, colorBorde, Color.GREEN, icono );

		//imagenPoligono2D.crearImagenLinea( width / 2, 0, width / 2, height, colorBorde, icono );
		imagenPoligono2D.crearImagenLinea( 0, height / 2, width / 3, height / 2, thicknessBorder, colorBorde, icono );
		imagenPoligono2D.crearImagenLinea( width / 3 , height / 2, width / 3 + width / 12, height / 4, thicknessBorder, colorBorde, icono );
		imagenPoligono2D.crearImagenLinea( width / 3 + width / 12, height / 4, width / 3 + 3*width / 12, 3*height/4, thicknessBorder, colorBorde, icono );
		imagenPoligono2D.crearImagenLinea( width / 3 + 3*width / 12, 3*height/4, 2*width/3, height/2, thicknessBorder,  colorBorde, icono );
		imagenPoligono2D.crearImagenLinea( 2*width / 3, height / 2, width, height / 2, thicknessBorder, colorBorde, icono );

		return new ImageIcon( icono );
	}

	public static ImageIcon Incorrect(  )
	{
		Image icono = null;

		Color colorBorder = Color.BLACK;
		Color colorRelleno = Color.RED;
		int width = 100, height = 100;
		float thickness = width / 4.0F;

		icono = imagenPoligono2D.crearLienzoVacio( width, height, null );

		imagenPoligono2D.crearImagenLinea( 0+(int)thickness, 0+(int)thickness, width-(int)thickness, height-(int)thickness, thickness+2.0F, colorBorder, icono );
		imagenPoligono2D.crearImagenLinea( width-(int)thickness, 0+(int)thickness, 0+(int)thickness, height-(int)thickness, thickness+2.0F, colorBorder, icono );
		imagenPoligono2D.crearImagenLinea( 0+(int)thickness, 0+(int)thickness, width-(int)thickness, height-(int)thickness, thickness, colorRelleno, icono );
		imagenPoligono2D.crearImagenLinea( width-(int)thickness, 0+(int)thickness, 0+(int)thickness, height-(int)thickness, thickness, colorRelleno, icono );

		return new ImageIcon( icono );
	}

	public static ImageIcon Correct(  )
	{
		Image icono = null;		
		int width = 100, height = 100;
		Color colorBorder = Color.BLACK;
		Color colorRelleno = Color.GREEN;
		float thickness = 1.0F;

		icono = imagenPoligono2D.crearLienzoVacio( width, height, null );

		int x = (int)thickness, y = height / 2 + (int)thickness;

		int xs[ ] = { x, x + width / 4, width - (int)thickness, x + width / 4 };
		int ys[ ] = { y, y + height / 4 - (int)thickness, 0 + (int)thickness,  height - (int)thickness };  

		imagenPoligono2D.crearImagenPoligonoRelleno( xs, ys, colorRelleno, icono );
		imagenPoligono2D.crearImagenPoligonoPerfil( xs, ys, thickness, colorBorder, icono );

		return new ImageIcon( icono );
	}

	public static ImageIcon Clock(  )
	{
		Image icono = null;		
		Image aux = null;
		int width = 100;
		int height = 100;

		Color colorBorder = Color.RED;
		float thickness = 2.5F;

		int x = (int)thickness, y = Math.abs( width - height ) / 2;
		int diametro = width - (int)Math.round( thickness );
		if( width > height )
		{
			diametro = height- (int)Math.round( thickness ); 
			x = y;
			y = (int)thickness;
		}

		icono = imagenPoligono2D.crearLienzoVacio( width, height, null );

		aux = imagenPoligono2D.crearImagenRectangulo(  5 * width / 8, height - (int)(thickness * 2 ), thickness, colorBorder, colorBorder );
		imagenPoligono2D.componerImagen( icono, ( width - aux.getWidth( null ) ) / 2 , ( height - aux.getHeight( null ) ) / 2 , aux); 

		imagenPoligono2D.crearImagenCirculo( x, y, diametro, Color.WHITE, icono );
		imagenPoligono2D.crearImagenCircunferencia( x, y, diametro, thickness, colorBorder, icono );

		aux = imagenPoligono2D.crearImagenRectangulo( diametro / 12, diametro / 12, thickness, Color.BLACK, Color.BLACK );		
		imagenPoligono2D.componerImagen( icono, width / 2 - aux.getWidth( null ) / 2, y + (int)(thickness * 2 ), aux );
		imagenPoligono2D.componerImagen( icono, width / 2 - aux.getWidth( null ) / 2, y + diametro - (int)(thickness * 2 ) - aux.getHeight( null ), aux );
		imagenPoligono2D.componerImagen( icono, 0 + (int)(thickness * 2 ), height / 2 - aux.getHeight( null ) / 2, aux );
		imagenPoligono2D.componerImagen( icono, width - (int)(thickness * 2 ) - aux.getWidth( null ), height / 2 - aux.getHeight( null ) / 2, aux );

		imagenPoligono2D.crearImagenLinea( width / 2, height / 2, width / 2 , height / 2 -  3 * ( diametro / 2 ) / 4, thickness*1.5F, colorBorder, icono );
		imagenPoligono2D.crearImagenLinea( width / 2, height / 2, width / 2 + diametro / 4, height / 2, thickness*1.5F, colorBorder, icono );		

		return new ImageIcon( icono );
	}

	public static ImageIcon Socket( Color colorIn, Color colorOut )
	{
		Image imgIn = imagenPoligono2D.crearImagenTriangulo( 14,  1, 
				Color.BLACK,
				colorIn,
				imagenPoligono2D.SOUTH );

		Image imgOut = imagenPoligono2D.crearImagenTriangulo( 14,  1, 
				Color.BLACK,
				colorOut,
				imagenPoligono2D.NORTH );

		Image img = imagenPoligono2D.crearLienzoVacio( 30,15, null );
		img = imagenPoligono2D.componerImagen( img, 0, 0, imgIn );

		return new ImageIcon( imagenPoligono2D.componerImagen( img, 12, 0, imgOut ) );
	}

	public static ImageIcon Folder( int width, int height, Color borderColor, Color fillColor )
	{
		int w = width - 2;
		int h = height - 2;
		int thickness = 1;

		if( borderColor == null && fillColor == null )
		{
			borderColor = Color.BLACK;
		}

		Image img = imagenPoligono2D.crearLienzoVacio( w, h, null );

		//Image imgAux = imagenPoligono2D.crearImagenRectangulo( w / 2, h / 5, thickness, borderColor, fillColor );
		int x[] = { thickness, 3 * w / 7, 3 * w / 7, w - thickness, w - thickness, thickness };
		int y[] = { thickness, thickness, h / 5, h / 5, 2 * h / 5, 2 * h / 5 };

		if( fillColor != null )
		{
			imagenPoligono2D.crearImagenPoligonoRelleno( x, y, fillColor, img );
		}

		if( borderColor != null )
		{
			imagenPoligono2D.crearImagenPoligonoPerfil( x, y, thickness, borderColor, img );
		}
		//imagenPoligono2D.componerImagen( img, 0, 0, imgAux );

		int xs[] = { thickness, w - thickness, w -thickness, thickness };
		int ys[] = { 2 * h / 5, 2 * h / 5, h - thickness, h - thickness };

		if( fillColor != null )
		{
			imagenPoligono2D.crearImagenPoligonoRelleno( xs, ys, fillColor, img );
		}

		if( borderColor != null )
		{
			imagenPoligono2D.crearImagenPoligonoPerfil( xs, ys, thickness, borderColor, img );
		}

		//Image imgAux = imagenPoligono2D.crearImagenRectangulo( w - thickness, 3 * h / 5, thickness, borderColor, fillColor );
		//imagenPoligono2D.componerImagen( img, 0, 2 * h / 5, imgAux );

		return new ImageIcon( img );
	}

	public static ImageIcon Refresh( int width, int height, Color borderColor, Color fillColor )
	{
		int w = width - 2;
		int h = height - 2;
		int l = (int)( Math.min( w, h ) / 3 );

		int thickness = l / 3;

		if( thickness < 2 )
		{
			thickness = 2;
		}


		Image img = imagenPoligono2D.crearLienzoVacio( w, h, null );

		if( borderColor == null )
		{
			borderColor = Color.BLACK;
		}

		Image imgAux = imagenPoligono2D.crearImagenTriangulo( l, 1, borderColor, borderColor, imagenPoligono2D.EAST );

		imagenPoligono2D.componerImagen( img, w / 2, 0, imgAux );

		imagenPoligono2D.crearImagenArco( 0, 0, w - (thickness * 3 ) / 2 , h - 2 * l / 3 - thickness, 0, -270, thickness, borderColor, fillColor, img );

		return new ImageIcon( img );		
	}

	public static ImageIcon Plot( int size, Color color )
	{
		ImageIcon ico = null;

		if( size > 0 )
		{	
			int thick = size / 16;
			if( thick < 1 )
			{
				thick = 1;
			}

			Image img = imagenPoligono2D.crearImagenRectangulo( size, size, 1, color, null);

			int[] x = new int[ size ];
			int[] y = new int[ size ];
			int h = (int)( size * 0.75 );
			for( int i = 0; i < size; i++ )
			{
				x[ i ] = i;
				y[ i ] = (int)(( ( Math.sin( ( i * 4 * Math.PI ) / size ) + 1 ) / 2 ) * h ) + ( size - h ) / 2;
			}

			imagenPoligono2D.crearImagenLineaPoligonal( x, y, thick, color, img );

			ico = new ImageIcon( img );
		}

		return ico;		
	}
	
	public static ImageIcon getSAMValence( int level, int side, Color borderColor, Color fillColor )
	{
		int thicknessBorder = 2;

		Image img = imagenPoligono2D.crearImagenRectangulo( side, side, thicknessBorder, borderColor, fillColor );

		Image eye = imagenPoligono2D.crearImagenCirculo( 0, 0, side/6, borderColor, null );

		Image mouth = getSmile( level, side, thicknessBorder, borderColor, fillColor );

		int w = mouth.getWidth( null );
		int hg = mouth.getHeight( null );

		img = imagenPoligono2D.componerImagen( img, side/6, side/6 , eye );
		img = imagenPoligono2D.componerImagen( img, side - side/3, side/6 , eye );
		//img = imagenPoligono2D.componerImagen( img, ( 5 * side ) / 12, ( 5 * side) / 12, nose );

		img = imagenPoligono2D.componerImagen( img, ( side - w ) / 2 , side - hg*2, mouth );

		return new ImageIcon( img );
		//return new ImageIcon( mouth );
	}

	private static Image getSmile( int level, int imgWidth, int thickness, Color borderColor, Color fillColor )
	{
		double x1 = 1;
		double x2 = ( 8 * imgWidth ) / 24.0 + x1, x3 = ( 8 * imgWidth ) / 12.0 + x1;
		double y1 = imgWidth / 6.0;
		double y2 = y1, y3 = y1;

		double h = ( imgWidth / 3.0 ) / 8.0; 

		int val = level - 5;

		y1 += -val * h;
		y3 = y1;
		y2 += val * h;

		if( y1 < 0 )
		{
			y1 = 0;
			y3 = y1;
			y2 = h * 8;
		}
		else if( y1 > h * 8 )
		{
			y2 = 0;
			y1 = h * 8;
			y3 = y1;
		}

		Bspline bspline = new Bspline();

		bspline.addPoint( -1, y1 );
		bspline.addPoint( 0, y1 );
		bspline.addPoint( x1, y1 );
		bspline.addPoint( x2, y2 );
		bspline.addPoint( x3, y3 );
		bspline.addPoint( x3, y3 );
		bspline.addPoint( x2, y2 );
		bspline.addPoint( x1, y1 );
		bspline.addPoint( 0, y1 );
		bspline.addPoint( -1, y1 );

		List< Coordinate > coords = bspline.getInterpolated();

		int n = coords.size();
		int[] xs = new int[ n ];
		int[] ys = new int[ xs.length ];

		int i = 0;
		int w = 0;
		int hg = 0;
		for( Coordinate c : coords )
		{
			xs[ i ] = (int)c.x;
			ys[ i ] = (int)c.y;

			if( w < xs[ i ] )
			{
				w = xs[ i ];
			}

			if( hg < ys[ i ] )
			{
				hg = ys[ i ];
			}

			i += 1;			
		}

		Image mouth = imagenPoligono2D.crearLienzoVacio( w + 1, hg + 1, fillColor );

		for( i = 1; i < xs.length; i++ )
		{
			imagenPoligono2D.crearImagenLinea( xs[i-1], ys[i-1]
					, xs[i], ys[i]
							, thickness
							, borderColor, mouth );
		}

		return mouth;
	}

	public static ImageIcon getSAMValence( float level, int side, Color borderColor, Color fillColor )
	{
		int thicknessBorder = 2;

		Image img = imagenPoligono2D.crearImagenRectangulo( side, side, thicknessBorder, borderColor, fillColor );

		Image eye = imagenPoligono2D.crearImagenCirculo( 0, 0, side/6, borderColor, null );

		Image mouth = getSmile( level, side, thicknessBorder, borderColor, fillColor );

		int w = mouth.getWidth( null );
		int hg = mouth.getHeight( null );

		img = imagenPoligono2D.componerImagen( img, side/6, side/6 , eye );
		img = imagenPoligono2D.componerImagen( img, side - side/3, side/6 , eye );
		//img = imagenPoligono2D.componerImagen( img, ( 5 * side ) / 12, ( 5 * side) / 12, nose );

		img = imagenPoligono2D.componerImagen( img, ( side - w ) / 2 , side - hg*2, mouth );

		return new ImageIcon( img );
		//return new ImageIcon( mouth );
	}
	
	private static Image getSmile( float level, int imgWidth, int thickness, Color borderColor, Color fillColor )
	{
		double x1 = 1;
		double x2 = ( 8 * imgWidth ) / 24.0 + x1, x3 = ( 8 * imgWidth ) / 12.0 + x1;
		double y1 = imgWidth / 6.0;
		double y2 = y1, y3 = y1;

		double h = ( imgWidth / 3.0 ) / 8.0; 

		if( level < 0 || level > 1 )
		{
			level = 1F;
		}
		
		int val = Math.round( level * 9 ) - 5;

		y1 += -val * h;
		y3 = y1;
		y2 += val * h;

		if( y1 < 0 )
		{
			y1 = 0;
			y3 = y1;
			y2 = h * 8;
		}
		else if( y1 > h * 8 )
		{
			y2 = 0;
			y1 = h * 8;
			y3 = y1;
		}

		Bspline bspline = new Bspline();

		bspline.addPoint( -1, y1 );
		bspline.addPoint( 0, y1 );
		bspline.addPoint( x1, y1 );
		bspline.addPoint( x2, y2 );
		bspline.addPoint( x3, y3 );
		bspline.addPoint( x3, y3 );
		bspline.addPoint( x2, y2 );
		bspline.addPoint( x1, y1 );
		bspline.addPoint( 0, y1 );
		bspline.addPoint( -1, y1 );

		List< Coordinate > coords = bspline.getInterpolated();

		int n = coords.size();
		int[] xs = new int[ n ];
		int[] ys = new int[ xs.length ];

		int i = 0;
		int w = 0;
		int hg = 0;
		for( Coordinate c : coords )
		{
			xs[ i ] = (int)c.x;
			ys[ i ] = (int)c.y;

			if( w < xs[ i ] )
			{
				w = xs[ i ];
			}

			if( hg < ys[ i ] )
			{
				hg = ys[ i ];
			}

			i += 1;			
		}

		Image mouth = imagenPoligono2D.crearLienzoVacio( w + 1, hg + 1, fillColor );

		for( i = 1; i < xs.length; i++ )
		{
			imagenPoligono2D.crearImagenLinea( xs[i-1], ys[i-1]
					, xs[i], ys[i]
							, thickness
							, borderColor, mouth );
		}

		return mouth;
	}
	public static ImageIcon getSAMArousal( int level, int side, Color borderColor, Color fillColor )
	{
		int l = level;
		if( l < 0 )
		{
			l = 1;
		}
		else if( l > 9 )
		{
			l = 9;
		}


		int[] xs = { 0, 3, 4, 5, 8, 5, 8, 5, 7, 4, 3, 3, 1, 3, 0 };
		int[] ys = { 0, 2, 0, 2, 0, 3, 5, 5, 8, 6, 8, 5, 7, 4, 0 };

		for( int i = 0; i < xs.length; i++ )
		{
			xs[ i ] = (int)( ( l / 9.0 ) * (2.0 / 3 ) * side * xs[ i ] / 8.0 );
			ys[ i ] = (int)( ( l / 9.0 ) * (2.0 / 3 ) *  side * ys[ i ] / 8.0 );
		}

		Image doll = getDoll( side, side, borderColor, fillColor, fillColor ).getImage();
		Image cloud = imagenPoligono2D.crearImagenPoligonoRelleno( xs, ys, fillColor, null ); 
		imagenPoligono2D.crearImagenPoligonoPerfil( xs, ys, 2, borderColor, cloud );
		Image mov = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 180, -90
				, 2, borderColor, null, null );

		Image mov2 = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 90, -90
				, 2, borderColor, null, null );

		Image mov3 = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 0, -90
				, 2, borderColor, null, null );

		Image mov4 = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 270, -90
				, 2, borderColor, null, null );												

		int w = cloud.getWidth( null ) ;
		int h = cloud.getHeight( null );

		imagenPoligono2D.componerImagen( doll, (side - w) / 2, (side - h ) / 2, cloud );

		if( l > 6 )
		{
			int gap = 5;
			w = mov.getWidth( null );
			h = mov.getHeight( null )/2;

			imagenPoligono2D.componerImagen( doll, w / 2 + gap, h + gap, mov );
			if( l > 7 )
			{

				imagenPoligono2D.componerImagen( doll, gap, gap, mov );
			}

			w = mov2.getWidth( null );
			h = mov2.getHeight( null )/2;

			imagenPoligono2D.componerImagen( doll, side - w - w / 2 - gap, h + gap, mov2 );
			if( l > 7 )
			{
				imagenPoligono2D.componerImagen( doll, side - w - gap, gap , mov2 );
			}

			if( l > 8 )
			{
				w = mov4.getWidth( null );
				h = mov4.getHeight( null )/2;

				imagenPoligono2D.componerImagen( doll, gap, side - 2 * h - gap, mov4 );
				imagenPoligono2D.componerImagen( doll, w  / 2 + gap, side - 2 * h - h/2 - gap, mov4 );

				w = mov3.getWidth( null );
				h = mov3.getHeight( null )/2;

				imagenPoligono2D.componerImagen( doll, side - w - gap, side - 2 * h - gap, mov3 );
				imagenPoligono2D.componerImagen( doll, side - w - w / 2 - gap, side - 2 * h - h/2 - gap, mov3 );
			}
		}	

		return new ImageIcon( doll );
	}

	public static ImageIcon getSAMArousal( double level, int side, Color borderColor, Color fillColor )
	{
		double l = level;
		if( l < 0 )
		{
			l = 1;
		}
		else if( l > 1 )
		{
			l = 1;
		}


		int[] xs = { 0, 3, 4, 5, 8, 5, 8, 5, 7, 4, 3, 3, 1, 3, 0 };
		int[] ys = { 0, 2, 0, 2, 0, 3, 5, 5, 8, 6, 8, 5, 7, 4, 0 };

		for( int i = 0; i < xs.length; i++ )
		{
			xs[ i ] = (int)( l * (2.0 / 3 ) * side * xs[ i ] / 8.0 );
			ys[ i ] = (int)( l * (2.0 / 3 ) *  side * ys[ i ] / 8.0 );
		}

		Image doll = getDoll( side, side, borderColor, fillColor, fillColor ).getImage();
		Image cloud = imagenPoligono2D.crearImagenPoligonoRelleno( xs, ys, fillColor, null ); 
		imagenPoligono2D.crearImagenPoligonoPerfil( xs, ys, 2, borderColor, cloud );
		Image mov = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 180, -90
				, 2, borderColor, null, null );

		Image mov2 = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 90, -90
				, 2, borderColor, null, null );

		Image mov3 = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 0, -90
				, 2, borderColor, null, null );

		Image mov4 = imagenPoligono2D.crearImagenArco( 0, 0, (int)( side * .25 / 2)
				, (int)( side * .25 / 2), 270, -90
				, 2, borderColor, null, null );												

		int w = cloud.getWidth( null ) ;
		int h = cloud.getHeight( null );

		imagenPoligono2D.componerImagen( doll, (side - w) / 2, (side - h ) / 2, cloud );

		if( l > 6 / 9.0D )
		{
			int gap = 5;
			w = mov.getWidth( null );
			h = mov.getHeight( null )/2;

			imagenPoligono2D.componerImagen( doll, w / 2 + gap, h + gap, mov );
			if( l > 7 )
			{

				imagenPoligono2D.componerImagen( doll, gap, gap, mov );
			}

			w = mov2.getWidth( null );
			h = mov2.getHeight( null )/2;

			imagenPoligono2D.componerImagen( doll, side - w - w / 2 - gap, h + gap, mov2 );
			if( l > 7 / 9.0D )
			{
				imagenPoligono2D.componerImagen( doll, side - w - gap, gap , mov2 );
			}

			if( l > 8 / 9.0D )
			{
				w = mov4.getWidth( null );
				h = mov4.getHeight( null )/2;

				imagenPoligono2D.componerImagen( doll, gap, side - 2 * h - gap, mov4 );
				imagenPoligono2D.componerImagen( doll, w  / 2 + gap, side - 2 * h - h/2 - gap, mov4 );

				w = mov3.getWidth( null );
				h = mov3.getHeight( null )/2;

				imagenPoligono2D.componerImagen( doll, side - w - gap, side - 2 * h - gap, mov3 );
				imagenPoligono2D.componerImagen( doll, side - w - w / 2 - gap, side - 2 * h - h/2 - gap, mov3 );
			}
		}	

		return new ImageIcon( doll );
	}
	
	public static ImageIcon getSAMDominance( double level, int side, Color borderColor, Color fillColor )
	{
		double l = level;
		if( l < 0 )
		{
			l = 1;
		}
		else if( l > 1 )
		{
			l = 1;
		}


		double w = 0.2 + l * 0.8 ; 
		w *= side;

		return getDoll((int)w , (int)w, borderColor, fillColor, fillColor ); //getSAMValence( 5, (int)w, borderColor, fillColor );
	}
	
	
	public static ImageIcon getSAMDominance( int level, int side, Color borderColor, Color fillColor )
	{
		int l = level;
		if( l < 0 )
		{
			l = 1;
		}
		else if( l > 9 )
		{
			l = 9;
		}


		double w = 0.2 + l * 0.8 / 9 ; 
		w *= side;

		return getDoll((int)w , (int)w, borderColor, fillColor, fillColor ); //getSAMValence( 5, (int)w, borderColor, fillColor );
	}

	public static ImageIcon getBasicEmotion(int level, int side, Color borderColor, Color fillColor, String text, FontMetrics fm)
	{
		int l = level;
		if (l < 1)
		{
			l = 1;
		}
		else if (l > 7)
		{
			l = 7;
		}

		int thicknessBorder = 3;

		int h = side;
		int hText = 0;
		Image imgText = null;

		if ((text != null) && (!text.isEmpty()))
		{
			imgText = imagenPoligono2D.crearImagenTexto(0, 0, text, fm, fillColor, borderColor, null);

			hText = imgText.getHeight(null);
			h -= hText;
		}

		if (h > 0)
		{
			side = h;
		}

		Image img = imagenPoligono2D.crearImagenRectangulo(side, h, thicknessBorder, borderColor, fillColor);
		Image eye = imagenPoligono2D.crearImagenCirculo(0, 0, side / 6, borderColor, null);

		if (l == 1)
		{
			// Sadness

			img = getSAMValence( 1, side, borderColor, fillColor).getImage();	
			if( h > 0)
			{
				img = img.getScaledInstance( img.getWidth( null ), h,  BufferedImage.SCALE_SMOOTH );
			}
		}
		else if (l == 2)
		{
			// Surprise	

			Image eyebrow = imagenPoligono2D.crearLienzoVacio( eye.getWidth( null ), thicknessBorder, borderColor );
			Image mouth = imagenPoligono2D.crearImagenCirculo( 0, 0, side/3, borderColor, null );

			img = imagenPoligono2D.componerImagen( img, side/6, side/6 , eye );
			img = imagenPoligono2D.componerImagen( img, side/6, side/6 - eye.getHeight( null )/4, eyebrow );

			img = imagenPoligono2D.componerImagen( img, side - side/3, side/6 , eye );
			img = imagenPoligono2D.componerImagen( img, side - side/3, side/6 - eye.getHeight( null )/4, eyebrow );

			img = imagenPoligono2D.componerImagen( img, img.getWidth( null ) / 2 - mouth.getWidth( null ) /2
					, img.getHeight( null ) - (int)( 1.25 * mouth.getHeight( null ) ), mouth );
		}
		else if (l == 3)
		{
			// Anger

			img = getSAMValence( 4, side, borderColor, fillColor).getImage();

			//Image mouth = getSmile( 5, side, thicknessBorder, borderColor, fillColor );					
			Image rightEyebrow = imagenPoligono2D.crearImagenLinea( 0, 0
					, eye.getWidth(null ), side/10
					, thicknessBorder, borderColor, null );

			Image leftEyebrow = imagenPoligono2D.crearImagenLinea( 0, side/10
					, eye.getWidth(null ), 0 
					, thicknessBorder, borderColor, null );

			/*
			img = imagenPoligono2D.componerImagen( img, side/6, side/5 , eye );
			img = imagenPoligono2D.componerImagen( img, side/6, side/5 - rightEyebrow.getHeight( null ), rightEyebrow );

			img = imagenPoligono2D.componerImagen( img, side - side/3, side/5 , eye );
			img = imagenPoligono2D.componerImagen( img, side - side/3, side/5 - leftEyebrow.getHeight( null ), leftEyebrow );

			img = imagenPoligono2D.componerImagen( img, side/2 - mouth.getWidth( null ) / 2 , side - side / 3 , mouth );
			 */		

			img = imagenPoligono2D.componerImagen( img, side/6, side/5 - (int)( 1.05 * rightEyebrow.getHeight( null )), rightEyebrow );			
			img = imagenPoligono2D.componerImagen( img, side - leftEyebrow.getWidth( null ) - side/6, side/5 - (int)(1.05 * leftEyebrow.getHeight( null )), leftEyebrow );

			if( h > 0)
			{
				img = img.getScaledInstance( side, h,  BufferedImage.SCALE_SMOOTH );
			}
		}
		else if (l == 4)
		{
			// Disgust

			Image rigthEye = imagenPoligono2D.crearLienzoVacio(eye.getWidth(null), eye.getHeight(null), fillColor);
			imagenPoligono2D.crearImagenLinea(0, 0, eye.getWidth(null), eye.getHeight(null) / 2, thicknessBorder, borderColor, rigthEye);
			imagenPoligono2D.crearImagenLinea(eye.getWidth(null), eye.getHeight(null) / 2, 0, eye.getHeight(null), thicknessBorder, borderColor, rigthEye);

			Image leftEye = imagenPoligono2D.crearLienzoVacio(eye.getWidth(null), eye.getHeight(null), fillColor);
			imagenPoligono2D.crearImagenLinea(eye.getWidth(null), 0, 0, eye.getHeight(null) / 2, thicknessBorder, borderColor, leftEye);
			imagenPoligono2D.crearImagenLinea(eye.getWidth(null), eye.getHeight(null), 0, eye.getHeight(null) / 2, thicknessBorder, borderColor, leftEye);

			Image mouth = imagenPoligono2D.crearImagenArco(0, 0, (int)( side / 2.5 ), (int)( side / 2.5 ), 0, 180, thicknessBorder, borderColor, borderColor, null);

			int tongueW = mouth.getWidth(null) / 2;

			int x = thicknessBorder / 2;
			int y = thicknessBorder / 2;

			int w = tongueW - 2 * thicknessBorder;
			if (w < 1)
			{
				w = tongueW;
			}

			Image tongueTip = imagenPoligono2D.crearImagenArco(0, 0, tongueW, tongueW - thicknessBorder, 0, -180, thicknessBorder, borderColor, fillColor, null);

			Image tongueBody = imagenPoligono2D.crearLienzoVacio(tongueTip.getWidth(null), tongueTip.getHeight(null), fillColor);

			imagenPoligono2D.crearImagenLinea(x, y, tongueBody.getWidth(null), y, thicknessBorder, borderColor, tongueBody);
			imagenPoligono2D.crearImagenLinea(x, y, x, tongueBody.getHeight(null), thicknessBorder, borderColor, tongueBody);
			imagenPoligono2D.crearImagenLinea(tongueBody.getWidth(null) / 2 - x, y, tongueBody.getWidth(null) / 2 - x, tongueBody.getHeight(null), thicknessBorder, borderColor, tongueBody);
			imagenPoligono2D.crearImagenLinea(tongueBody.getWidth(null) - x - 1, y, tongueBody.getWidth(null) - x - 1, tongueBody.getHeight(null), thicknessBorder, borderColor, tongueBody);

			Image tongue = imagenPoligono2D.crearLienzoVacio(tongueTip.getWidth(null), tongueTip.getHeight(null) / 2 + tongueBody.getHeight(null), fillColor);

			tongue = imagenPoligono2D.componerImagen(tongue, 0, 0, tongueBody);
			tongue = imagenPoligono2D.componerImagen(tongue, 0, tongueBody.getHeight(null) / 2, tongueTip);


			Image mouthTongue = imagenPoligono2D.crearLienzoVacio(mouth.getWidth(null), mouth.getHeight(null) / 2 + tongue.getHeight(null) - mouth.getHeight(null) / 6, null);
			mouthTongue = imagenPoligono2D.componerImagen(mouthTongue, 0, 0, mouth);
			mouthTongue = imagenPoligono2D.componerImagen(mouthTongue, 
					mouthTongue.getWidth(null) / 2 - tongue.getWidth(null) / 2, 
					mouth.getHeight(null) / 3, 
					tongue);


			img = imagenPoligono2D.componerImagen(img, side - side / 3 - leftEye.getWidth(null) / 4, side / 6, leftEye);


			img = imagenPoligono2D.componerImagen(img, side / 6 + rigthEye.getWidth(null) / 4, side / 6, rigthEye);


			img = imagenPoligono2D.componerImagen(img, (img.getWidth(null) - mouthTongue.getWidth(null)) / 2, 
					img.getHeight(null) - (int)(1.05D * mouthTongue.getHeight(null)), 
					mouthTongue);
		}
		else if (l == 5)
		{
			// Fear

			int smileW = side - side / 3;
			int smileH = side / 9;

			Image mouth = imagenPoligono2D.crearLienzoVacio(smileW, smileH + 2 * thicknessBorder, fillColor);

			double stepSin = (4  * Math.PI ) / smileW;

			List<Integer> yVal = new ArrayList< Integer >();
			for (double xVal = 0.0D; xVal <= 4 * Math.PI; xVal += stepSin)
			{
				yVal.add( ( int )( smileH * ( Math.sin( xVal ) + 1 ) / 2 ) + thicknessBorder );
			}

			for (int i = 1; i < yVal.size(); i++)
			{
				mouth = imagenPoligono2D.crearImagenLinea(i - 1, ((Integer)yVal.get(i - 1)).intValue(), i, ((Integer)yVal.get(i)).intValue(), thicknessBorder, borderColor, mouth);
			}

			Image leftEyebrow = imagenPoligono2D.crearImagenLinea( 0, 0
					, eye.getWidth(null ), side/6
					, thicknessBorder, borderColor, null );

			Image rigthEyebrow = imagenPoligono2D.crearImagenLinea( 0, side/6
					, eye.getWidth(null ), 0 
					, thicknessBorder, borderColor, null );

			img = imagenPoligono2D.componerImagen( img, side/6, side/6 , eye );
			img = imagenPoligono2D.componerImagen( img, side/6 - eye.getWidth( null ) / 2, side/6 - rigthEyebrow.getHeight( null ) / 2, rigthEyebrow );

			img = imagenPoligono2D.componerImagen( img, side - side/3, side/6 , eye );
			img = imagenPoligono2D.componerImagen( img, side - side/3 + eye.getWidth( null ) / 2, side/6 - leftEyebrow.getHeight( null ) / 2, leftEyebrow );

			img = imagenPoligono2D.componerImagen( img, side/2 - mouth.getWidth( null ) / 2 , img.getHeight( null ) - img.getHeight( null ) / 3  , mouth );
		}
		else if (l == 6)
		{
			// Happiness

			img = getSAMValence(9, side, borderColor, fillColor).getImage();
			if (h > 0)
			{
				img = img.getScaledInstance(img.getWidth(null), h,  BufferedImage.SCALE_SMOOTH );
			}

		}
		else // Neutral
		{
			img = getSAMValence(5, side, borderColor, fillColor).getImage();
			if (h > 0)
			{
				img = img.getScaledInstance(img.getWidth(null), h,  BufferedImage.SCALE_SMOOTH );
			}
		}

		Image out = imagenPoligono2D.crearLienzoVacio(img.getWidth(null), img.getHeight(null) + hText, fillColor);
		imagenPoligono2D.componerImagen(out, 0, 0, img);

		if (imgText != null)
		{
			imagenPoligono2D.componerImagen(out, (img.getWidth(null) - imgText.getWidth(null)) / 2, img.getHeight(null), imgText);
		}

		return new ImageIcon(out);
	}

	public static ImageIcon getDoll(int width, int height, Color borderColor, Color fillColor, Color bgColor)
	{
		Image base = imagenPoligono2D.crearLienzoVacio(width, height, bgColor);

		double[] x = { 0.625D, 0.625D, 0.375D, 0.375D, 
				0.875D, 0.875D, 0.75D, 0.75D, 0.75D, 
				0.5D, 0.5D, 0.5D, 0.25D, 0.25D, 0.25D, 0.125D, 0.125D, 0.5D };

		double[] y = { 0.25D, 0.0D, 0.0D, 0.25D, 0.25D, 
				0.625D, 0.625D, 0.5D, 0.99D, 
				0.99D, 0.625D, 0.99D, 0.99D, 0.5D, 0.625D, 
				0.625D, 0.25D, 0.25D };

		int[] xs = new int[x.length];
		int[] ys = new int[y.length];

		for (int i = 0; i < xs.length; i++)
		{
			xs[i] = ((int)(x[i] * width));
			ys[i] = ((int)(y[i] * height));
		}


		imagenPoligono2D.crearImagenPoligonoRelleno(xs, ys, fillColor, base);
		imagenPoligono2D.crearImagenPoligonoPerfil(xs, ys, 2.0F, borderColor, base);

		return new ImageIcon(base);
	}
	
	public static ImageIcon getQuaver( int width, int height, Color fillColor, Color bgColor)
	{
		Image base = imagenPoligono2D.crearLienzoVacio( width, height, bgColor );
		
		int s = Math.min( width, height );
		
		int t = Math.max( width, height ) / 16;
		
		if( t < 1 )
		{
			t = 1;
		}
		
		int r = s / 4;
		int x = r / 2; 
		int x2 = width - x - r;
		int y = height - r - height / 8; 
		
		imagenPoligono2D.crearImagenCirculo( x, y, r, fillColor, base );
		imagenPoligono2D.crearImagenCirculo( x2, y, r, fillColor, base );
		
		imagenPoligono2D.crearImagenLinea( x + r - t / 2, ( 3 * height ) / 4, x + r - t / 2, height / 4, t, fillColor, base );
		imagenPoligono2D.crearImagenLinea( x2 + r - t / 2, ( 3 * height ) / 4, x2 + r - t / 2, height / 4, t, fillColor, base );
		
		imagenPoligono2D.crearImagenLinea( x + r, height / 4, x2 + r - t, height / 4, t * 2, fillColor, base );
		
		return new ImageIcon( base );
	}
	
	public static ImageIcon Sound( int width, int height, Color fillColor, Color bgColor)
	{
		Image base = imagenPoligono2D.crearLienzoVacio( width, height, bgColor );
		
		int x[]  = new int[] { width / 8, width / 4, width / 2, width / 2, width / 4, width / 8 };
		int y[]  = new int[] { ( 3 * height ) / 8, ( 3 * height ) / 8, height / 4, ( 3 * height ) / 4, ( 5 * height ) / 8, ( 5 * height ) / 8 };

		int h = y[ 3 ] - y[ 2 ];
		
		int t = Math.max( width, height ) / 32;
		
		if( t < 1 )
		{
			t = 1;
		}
		
		imagenPoligono2D.crearImagenPoligonoRelleno( x, y, fillColor, base ); 
		
		int w =  width / 8;
		
		imagenPoligono2D.crearImagenArco( width / 2, height / 2 - h / 8 - t, w / 2, h / 4, -90, 180, t, fillColor, null, base );
		
		if( width >= 32 )
		{		
			imagenPoligono2D.crearImagenArco( width / 2, height / 2 - h / 4 - t, w, h / 2, -90, 180, t, fillColor, null, base );
		}
		
		imagenPoligono2D.crearImagenArco( width  / 2, height / 2 - ( ( 4 * h ) / 5 ) /2 - t, ( 3 * w ) / 2, ( 4 * h ) / 5, -90, 180, t, fillColor, null, base );
		
		return new ImageIcon( base );
	}
	
	public static ImageIcon InterleavedIcon( int width, int height, Color border, Color fill, Color background )
	{
		Image base = imagenPoligono2D.crearLienzoVacio( width, height, background );
		
		int thick = Math.min( width, height) / 16;
		if( thick < 1 )
		{
			thick = 1;
		}
		
		Color c = fill;
		if( c == null )
		{
			c = border;			
			
			if( c == null )
			{
				c = background;
			}
			
			if( c == null )
			{
				c = Color.RED;
			}
			else
			{			
				c = new Color( ~c.getRGB() );
			}
		}
					
		Image rect = imagenPoligono2D.crearImagenRectangulo( ( 3 * width ) / 10, ( 3 * height ) / 5, thick, border, c );
		
		int x = rect.getWidth( null ) / 3;
		int y = rect.getHeight( null ) / 3;
		imagenPoligono2D.componerImagen( base, x * 2 + thick, thick, rect );
		imagenPoligono2D.componerImagen( base, x + thick, y, rect );
		imagenPoligono2D.componerImagen( base, thick, 2 * y, rect );
		
		x = rect.getWidth( null ) / 3;
		y = rect.getHeight( null ) / 3;
		imagenPoligono2D.componerImagen( base, width / 2 + x * 2 - thick, thick, rect );
		imagenPoligono2D.componerImagen( base, width / 2 + x - thick, y, rect );
		imagenPoligono2D.componerImagen( base, width / 2 - thick, 2 * y, rect );
		
		if( fill == null )
		{
			Color n = new Color( c.getRed() / 255F, c.getGreen() / 255F , c.getBlue() / 255F, 0F );
			
			imagenPoligono2D.changeColorPixels( c, n, base );
		}
		
		
		return new ImageIcon( base );		
	}
}