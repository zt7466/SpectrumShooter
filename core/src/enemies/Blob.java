package enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import src.GameScreen;
import src.colorgame;

/**
 * Blob.java
 * Class that represents the blob enemies
 * @author Zachary 
 *
 */
public class Blob implements Enemies
{
	private int health;
	private int maxHealth;
	private float deg;
	private float speed;
	private Color color;
	private Color damageColor;
	private float diameter;
	private Boolean isDead;
	private int WIDTH_RESOLUTION;
	private int HEIGHT_RESOLUTION;
	private final short CATEGORY_BLOB = 0x0002;
	Fixture fixture;
	
	/**
	 * Constructor for creating a Blob
	 * @param Health of the blob
	 * @param deg degree the blob moves on
	 * @param speed the speed a blob moves
	 * @param distance the distance the blob will move
	 * @param diameter diameter of the blob
	 * @param color color of the blob
	 * @param world the world for box2d
	 */
	public Blob(int Health, float deg, float speed, float distance, float diameter, Color color, World world)
	{
		WIDTH_RESOLUTION = colorgame.getInstance().WIDTH_RESOLUTION;
		HEIGHT_RESOLUTION = colorgame.getInstance().HEIGHT_RESOLUTION;
		
		this.health = Health;
		this.maxHealth = Health;
		this.deg = deg;
		this.speed = speed;
		this.color = color;
		this.damageColor = color;
		this.diameter = diameter;
		
		isDead = false;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(WIDTH_RESOLUTION  / 2 + distance * MathUtils.sinDeg(deg), HEIGHT_RESOLUTION / 2 + distance * MathUtils.cosDeg(deg));
		
		Body body = world.createBody(bodyDef);
		body.setLinearVelocity(-MathUtils.sinDeg(deg) * speed, - MathUtils.cosDeg(deg) * speed);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(diameter / 10);
	
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.filter.categoryBits = CATEGORY_BLOB;
		fixtureDef.filter.maskBits = ~CATEGORY_BLOB;
		
		fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);
		circle.dispose();
	}
	
	/**
	 * method for updating a blob
	 * @param delta
	 */
	public void update(float delta)
	{
		fixture.getBody().setLinearVelocity(-MathUtils.sinDeg(deg) * speed, - MathUtils.cosDeg(deg) * speed);
		
		float x = Math.abs(WIDTH_RESOLUTION  / 2 - getX());
		float y = Math.abs(HEIGHT_RESOLUTION  / 2 - getY());
		float distance = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		if(distance < 4.5)
		{
			isDead = true;
			GameScreen.getInstance().takeDamage(13);
		}
	}
	
	public void SpecialUpdate()
	{
		float x = Math.abs(WIDTH_RESOLUTION  / 2 - getX());
		float y = Math.abs(HEIGHT_RESOLUTION  / 2 - getY());
		float distance = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		if(distance < 1)
		{
			isDead = true;
		}
	}
	
	/**
	 * Checks if a Blob is dead, if it is kill it
	 * @return
	 */
	public Boolean checkIfDead()
	{
		return isDead;
	}
	
	/**
	 * returns the degree the blob is moving on
	 * @return
	 */
	public float getDeg()
	{
		return deg;
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
	 * returns the diameter
	 * @return diameter of the blob
	 */
	public float getDiameter()
	{
		return diameter;
	}
	
	/**
	 * returns the color of the blob
	 * @return color
	 */
	public Color getColor()
	{
		return color;
	}
	
	public Color getDamageColor()
	{
		return damageColor;
	}
	
	/**
	 * Called when the blob is hit 
	 * @param hitColor the color of the Projectile that hit the blob
	 */
	public void takeHit(Color hitColor, int damage)
	{
		//Knockback
		fixture.getBody().setLinearVelocity(MathUtils.sinDeg(deg) * speed * 10,  MathUtils.cosDeg(deg) * speed * 10);
		
		Double damageMultiplier = Math.sqrt(Math.pow(hitColor.r - color.r, 2) + Math.pow(hitColor.g - color.g, 2) 
			+ Math.pow(hitColor.b - color.b, 2));
	
		health = health - (int) Math.floor(damage * (1 + 3 * (1 - damageMultiplier)));
		
		if(health <= 0)
		{
			isDead = true;
			GameScreen.getInstance().addDeadCount();
		}
		
		float healthPercentage = (float) health / (float) maxHealth; 
		
		float r = ((1 - color.r) * (1-healthPercentage)) + color.r;
		float g = ((1 - color.g) * (1-healthPercentage)) + color.g;
		float b = ((1 - color.b) * (1-healthPercentage)) + color.b;
		
		damageColor = new Color(r,g,b,1);
	}
	
	/**
	 * returns the fixture for the blob to use to remove
	 * @return fixture
	 */
	public Fixture getFixture()
	{
		return fixture;
	}
}
