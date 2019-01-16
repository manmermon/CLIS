package GUI.MyComponents;

public class NumberRange 
{
    private final double min;
    private final double max;

    public NumberRange( Number min, Number max ) 
    {
        this.min = min.doubleValue();
        this.max = max.doubleValue();
    }

    public boolean within( Number value ) 
    {
    	double v = value.doubleValue();
    	
        return this.min <= v &&  v <= max ;
    }
}