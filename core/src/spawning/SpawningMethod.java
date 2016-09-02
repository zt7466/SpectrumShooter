package spawning;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;

import enemies.Enemies;

public interface SpawningMethod 
{
	
	public  ArrayList<Enemies> update(float delta, ArrayList<Enemies> array, World world);
	
}
