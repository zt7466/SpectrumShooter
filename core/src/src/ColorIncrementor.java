package src;

import com.badlogic.gdx.graphics.Color;

public class ColorIncrementor
{
	private char currentChange = 'r';
	private boolean add = true;
	private final float maxValue = .75f;
	private final float minValue = 0.0f;
	private final float changevalue = .005f;
	private Color backgroundColor = new Color(maxValue,minValue,maxValue, maxValue);
	
	/**
	 * increments the color placed as the background
	 */
	public Color incrementColor() 
	{
		if(currentChange == 'r')
		{
			if(add)
			{
				backgroundColor.b = backgroundColor.b - changevalue;
				if(Math.abs(backgroundColor.b - minValue) < changevalue)
				{
					add = false;
				}
			}
			else
			{
				backgroundColor.g = backgroundColor.g + changevalue;
				if(Math.abs(backgroundColor.g - maxValue) < changevalue)
				{
					currentChange = 'g';
					add = true;
				}
			}	
		}
		else if(currentChange == 'g')
		{
			if(add)
			{
				backgroundColor.r = backgroundColor.r - changevalue;
				if(Math.abs(backgroundColor.r - minValue) < changevalue)
				{
					add = false;
				}
			}
			else
			{
				backgroundColor.b = backgroundColor.b + changevalue;
				if(Math.abs(backgroundColor.b - maxValue) < changevalue)
				{
					currentChange = 'b';
					add = true;
				}
			}	
		}
		else if(currentChange == 'b')
		{
			if(add)
			{
				backgroundColor.g = backgroundColor.g - changevalue;		
				if(Math.abs(backgroundColor.g - minValue) < changevalue)
				{
					add = false;
				}
			}
			else
			{
				backgroundColor.r = backgroundColor.r + changevalue;
				if(Math.abs(backgroundColor.r - maxValue) < changevalue)
				{
					currentChange = 'r';
					add = true;
				}
			}	
		}	
		return new Color(backgroundColor.r,backgroundColor.g,backgroundColor.b,backgroundColor.a);
	}

	public Color getInverse() 
	{	
		return new Color(maxValue - backgroundColor.r, maxValue - backgroundColor.g, maxValue - backgroundColor.b, 1);
	}
}
