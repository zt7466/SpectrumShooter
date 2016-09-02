package enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface Enemies 
{
	public void update(float delta);
	public Boolean checkIfDead();
	public void takeHit(Color hitColor, int damage);
	public Fixture getFixture();
	public float getX();
	public float getY();
	public float getDiameter();
	public Color getColor();
	public Color getDamageColor();
	public void SpecialUpdate();
}
