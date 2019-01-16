package Auxiliar;

public class EmotionParameter 
{
	public static final int TYPE_SADNESS = 1;
	public static final int TYPE_SURPIRSE = 2;
	public static final int TYPE_ANGER = 3;
	public static final int TYPE_DISGUST = 4;
	public static final int TYPE_FEAR = 5;
	public static final int TYPE_HAPPINESS = 6;
	public static final int TYPE_NEUTRAL = 7;			
	
	public static final String DEFAULT_TEXT_SADNESS = "Sadness";
	public static final String DEFAULT_TEXT_SURPRISE = "Surprise";
	public static final String DEFAULT_TEXT_ANGER = "Anger";
	public static final String DEFAULT_TEXT_DISGUST = "Disgust";
	public static final String DEFAULT_TEXT_FEAR = "Fear";
	public static final String DEFAULT_TEXT_HAPPINESS = "Happiness";
	public static final String DEFAULT_TEXT_NEUTRAL = "Neutral";			
	
	
	private boolean select;
	private int type;
	private String text;
	
	public EmotionParameter( int type ) 
	{		
		String t = "";
		
		switch ( type ) 
		{
			case TYPE_SADNESS:
			{
				t = DEFAULT_TEXT_SADNESS;
				break;
			}
			case TYPE_SURPIRSE:
			{
				t = DEFAULT_TEXT_SURPRISE;
				break;
			}
			case TYPE_ANGER:
			{
				t = DEFAULT_TEXT_ANGER;
				break;
			}
			case TYPE_DISGUST:
			{
				t = DEFAULT_TEXT_DISGUST;
				break;
			}
			case TYPE_FEAR:
			{
				t = DEFAULT_TEXT_FEAR;
				break;
			}
			case TYPE_HAPPINESS:
			{
				t = DEFAULT_TEXT_HAPPINESS;
				break;
			}
			case TYPE_NEUTRAL:
			{
				t = DEFAULT_TEXT_NEUTRAL;
				break;
			}
		}
		
		this.type = type;
		this.text = t;
		this.select = true;
	}
	
	public EmotionParameter( int type, boolean sel )
	{
		this( type );
		this.select = sel;
	}
	
	public EmotionParameter( int type, String text, boolean sel ) 
	{
		this.type = type;
		this.text = text;
		this.select = sel;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public boolean isSelect()
	{
		return this.select;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public void setType( int type )
	{
		this.type = type;
	}
	
	public void setSelect( boolean sel )
	{
		this.select = sel;
	}
	
	public void setText( String text )
	{
		this.text = text;
	}
	
	@Override
	public String toString() 
	{
		return "<" + this.type + "," + this.text + "," + this.select + ">";
	}
}
