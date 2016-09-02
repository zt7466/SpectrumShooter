package src;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Projectile.java
 * class used for the basic shot
 * @author Zachary
 *
 */
public class Projectile 
{
	private float distance, maxDistance;
	private Boolean isVisible;
	private Color color;
	private BodyDef bodyDef;
	private float WIDTH_RESOLUTION;
	private float HEIGHT_RESOLUTION;
	private final float DIAMETER = 6; 
	private final short CATEGORY_PROJECTILE = 0x0001;
	private Fixture fixture;
	private final float SPEED_MULTIPLYER = 3;
	
	/**
	 * Constructor to make a Projectile
	 * @param speed speed of the bullet
	 * @param deg degree it is shot at
	 * @param maxDistance max distance it can go
	 * @param world the world for box2d
	 * @param color color of the bullet
	 */
	public Projectile(float speed, float deg, float maxDistance, Color color, World world)
	{
		WIDTH_RESOLUTION = colorgame.getInstance().WIDTH_RESOLUTION;
		HEIGHT_RESOLUTION = colorgame.getInstance().HEIGHT_RESOLUTION;
		
		isVisible = true;
		distance = 0;
		this.maxDistance = maxDistance;
		this.color = color;
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(WIDTH_RESOLUTION  / 2 + distance * MathUtils.sinDeg(deg), HEIGHT_RESOLUTION / 2 + distance * MathUtils.cosDeg(deg));
		
		Body body = world.createBody(bodyDef);
		body.setLinearVelocity(MathUtils.sinDeg(deg) * speed  * SPEED_MULTIPLYER, - MathUtils.cosDeg(deg) * speed * SPEED_MULTIPLYER);

		CircleShape circle = new CircleShape();
		circle.setRadius(DIAMETER / 10);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.filter.categoryBits = CATEGORY_PROJECTILE;
		fixtureDef.filter.maskBits = ~CATEGORY_PROJECTILE;
		
		fixture = body.createFixture(fixtureDef);	
		fixture.setUserData(this);
		circle.dispose();
	}
	
	/**
	 * updates the bullet and checks if it reached its max range
	 * @param delta
	 */
	public void update(float delta)
	{
		float x = Math.abs(WIDTH_RESOLUTION  / 2 - getX());
		float y = Math.abs(HEIGHT_RESOLUTION  / 2 - getY());
		float distance = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		if(distance > maxDistance)
		{
			isVisible = false;
		}
	}
	
	/**
	 * returns the position on the x plane
	 * @return position on the x plane
	 */
	public float getX()
	{
		return fixture.getBody().getPosition().x;
	}
	

	/**
	 * returns the position on the y plane
	 * @return position on the y plane
	 */
	public float getY()
	{
		return fixture.getBody().getPosition().y;
	}
	
	/**
	 * returns if the projectile should be visible
	 * @return isVisible
	 */
	public boolean getVisible()
	{
		return isVisible;
	}
	
	/**
	 * returns the color of the bullet
	 * @return color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * returns the diameter of the bullet 
	 * @return DIAMETER
	 */
	public float getDiameter() 
	{
		return DIAMETER;
	}
	
	/**
	 * called when projectile hits something
	 */
	public void hit()
	{
		isVisible = false;
	}

	/**
	 * returns the fixture of the bullet, used to delete the bullet
	 * @return fixture
	 */
	public Fixture getFixture()
	{
		return fixture;
	}
	
	
}
