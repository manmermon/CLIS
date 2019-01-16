package GUI.MyComponents;

public class Tuple<X, Y> 
{
	public final X x;
	public final Y y;
	
	public Tuple( X x, Y y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static boolean isCorrectTypes( Object obj, Class xClass, Class yClass )
	{
		boolean correct = false;
		
		if( obj != null && xClass != null && yClass != null )
		{
			if( obj instanceof Tuple )
			{
				Tuple t = (Tuple)obj;
				correct = ( xClass.isInstance( t.x ) ) && ( yClass.isInstance( t.y ) );
			}
		}
		
		return correct;
	}
	
	public static boolean isCorrectTypes( Object obj, Class cl )
	{	
		return isCorrectTypes( obj, cl, cl);
	}
	
	@Override
	public boolean equals( Object obj ) 
	{
		boolean eq = ( obj instanceof Tuple );
		
		if( eq )
		{
			Tuple tin = (Tuple)obj;
			
			eq = tin.x.equals( this.x ) && tin.y.equals( this.y );
		}
				
		return eq;		
	}
	
	@Override
	public String toString() 
	{
		String out = "<";
		if( x != null )
		{
			out += x;
		}
		
		out += ",";
		
		if( y != null )
		{
			out += " " + y; 
		}
		
		out += ">";
		
		return out;
	}  
}
