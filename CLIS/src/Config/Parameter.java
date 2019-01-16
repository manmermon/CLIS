package Config;

import Config.Language.Caption;
import Config.Language.Language;

import java.util.ArrayList;
import java.util.List;

public class Parameter extends Caption implements IParameter
{
	private Object value = null;

	/**
	 * Create a parameter
	 * 
	 * @param id: parameter identifier. This is used as default caption.
	 * @param defaultValue: parameter value.
	 * @throws IllegalArgumentException: 
	 * 			if id == null || id.trim().isempty() 
	 * 				|| defaultValue == null || !AdmitedType.checkType( defaultValue.getClass() ) 
	 */
	public Parameter(String id, Object defaultValue) throws IllegalArgumentException
	{
		super( id, Language.defaultLanguage, id);

		if ( (defaultValue == null) 
				|| (!AdmitedType.checkType(defaultValue.getClass() ) ) )
		{
			throw new IllegalArgumentException( "Parameter type is not correct. Admited type: " 
					+ AdmitedType.getAdmitedType() );
		}

		this.value = defaultValue;
	}

	/**
	 * 
	 * Set parameter value
	 * 
	 * @param newValue: new value
	 * @return previous value
	 * @throws ClassCastException: Class new value is different
	 */
	public Object setValue(Object newValue) throws ClassCastException
	{
		String parClass = this.value.getClass().getCanonicalName();
		if (!parClass.equals(newValue.getClass().getCanonicalName()))
		{
			throw new ClassCastException("New value class is different to parameter class: " 
					+ parClass );
		}

		Object prev = this.value;
		this.value = newValue;

		return prev;
	}

	/**
	 * 
	 * @return parameter value
	 */
	public Object getValue()
	{
		return this.value;
	}

	///////////////////////////////
	//
	// Check type parameters
	//
	public static class AdmitedType
	{
		private static List<String> admitedType = null;

		static
		{
			admitedType = new ArrayList< String >();
			admitedType.add( String.class.getCanonicalName() );
			admitedType.add( Boolean.class.getCanonicalName() );
			admitedType.add( Byte.class.getCanonicalName() );
			admitedType.add( Short.class.getCanonicalName() );
			admitedType.add( Integer.class.getCanonicalName( ));
			admitedType.add( Long.class.getCanonicalName() );
			admitedType.add( Float.class.getCanonicalName() );
			admitedType.add( Double.class.getCanonicalName() );
		}

		public static boolean checkType( Class type )
		{
			boolean ok = false;

			if (type != null)
			{
				ok = admitedType.contains( type.getCanonicalName() );
			}

			return ok;
		}

		public static String getAdmitedType()
		{
			return admitedType.toString();
		}
	}
}