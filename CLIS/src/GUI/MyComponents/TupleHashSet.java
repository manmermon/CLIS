package GUI.MyComponents;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class TupleHashSet extends HashSet< Tuple > 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add( Tuple t) 
	{
		boolean found = false;
				
		Iterator< Tuple > it = super.iterator();
	
		while( it.hasNext() && !found )
		{
			found = it.next().equals( t );
		}
		
		if( !found )
		{
			found = super.add( t );
		}
		else
		{
			found = false;
		}
		
		// TODO Auto-generated method stub
		return found;
	}

	@Override
	public boolean addAll( Collection c) 
	{
		boolean ok = true;
		
		Iterator it = c.iterator();
		while( it.hasNext() && ok )
		{	
			ok = ( it.next() instanceof Tuple );			
		}
		
		if( ok )
		{
			it = c.iterator();
			while( it.hasNext() )
			{
				ok = ok && this.add( (Tuple)it.next() );
			}
		}
				
		return ok;
	}
	
	@Override
	public boolean remove( Object o ) 
	{
		boolean ok = true;
		
		if( o instanceof Tuple )
		{
			Iterator< Tuple > it = super.iterator();
			ok = false;
			while( it.hasNext() && !ok )
			{
				if( it.next().equals( o ) )
				{
					it.remove();
					ok = true;
				}
			}
		}
		
		return ok;
	}
	
	@Override
	public boolean removeAll( Collection c ) 
	{
		boolean ok = true;
		
		Iterator it = c.iterator();
		while( it.hasNext() && ok )
		{
			ok = ( it.next() instanceof Tuple);
		}
		
		if( ok )
		{
			it = c.iterator();
			while( it.hasNext() )
			{
				ok = ok && this.remove( it.next() );
			}
		}
	
		return ok;
	}
	
	@Override
	public boolean contains(Object o) 
	{
		boolean cont = false;
		
		if( o instanceof Tuple )
		{
			Iterator< Tuple > it = super.iterator();
			while( it.hasNext() && !cont )
			{
				cont = it.next().equals( o );
			}
		}
		
		return cont;
	}
	
	@Override
	public boolean equals( Object o ) 
	{
		boolean eq = o instanceof Set;
		
		if( eq )
		{
			Set objs = (Set)o;
			Iterator it = objs.iterator();
			
			while( it.hasNext() && eq )
			{
				eq = this.contains( it.next() );
			}
		}
		return eq;
	}
}
