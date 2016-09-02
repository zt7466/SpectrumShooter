package spawning;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;

import enemies.Blob;
import enemies.Enemies;

public class RampingMode implements SpawningMethod 
{
	private final float SPAWNDELAY = 10;
	private float spawnDelay = 0;
					
	private final float maxSpeed = 3;
	private final float minSpeed = .5f;
	private final float minSize = 15;
	private final float possibleSize = 15;
	private float circleWidth;
	private float maxPower;
	
	public RampingMode(int maxPower, float circleWidth)
	{
		this.maxPower = maxPower;
		this.circleWidth = circleWidth;
	}
	
	@Override
	public ArrayList<Enemies> update(float delta, ArrayList<Enemies> array, World world) 
	{
		for (int i = 0; i < array.size(); i++) 
		{
			array.get(i).update(delta);
			if (array.get(i).checkIfDead())
			{
				array.add(generateBlob(world));
			}
		}
		
		if(spawnDelay < 0)
		{
			array.add(generateBlob(world));
			spawnDelay = SPAWNDELAY;
		}
		spawnDelay = spawnDelay - delta;
		return array;
	}

	public Blob generateBlob(World world)
	{
		float red = (float)Math.random() * 100;
		float green = 0;
		float blue = 0;
		float remainder = maxPower - red;
		
		if(remainder < 100)
		{
			green = (float) Math.random() * remainder;
		}
		else
		{
			green = (float) Math.random() * 100;
		}
		remainder = remainder - green;
		if(remainder < 100)
		{
			blue =  remainder;
		}
		else
		{
			blue = 100;
		}
		
		
		Color generatedColor = new Color((float) red / 100, (float) green / 100, (float) blue / 100, 1);
		
		Blob newBlob = new Blob(100, (float) Math.random() * 360f, (float) Math.random() * maxSpeed + minSpeed, circleWidth, 
				(float) Math.random() * possibleSize + minSize, generatedColor, world);
		return newBlob;
	}
}
