package src;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class colorgame extends Game 
{	
	
	public int WIDTH_RESOLUTION = 136;
	public int HEIGHT_RESOLUTION = 77;
	
	private static colorgame instance = null; 
	@Override
	public void create () 
	{
		instance = this;
		Gdx.graphics.setDisplayMode(WIDTH_RESOLUTION*10, HEIGHT_RESOLUTION*10, true);
		this.setScreen(new MainMenuScreen());
		
	}

	@Override
	public void render () {
		super.render();
	}

	public void close()
	{
		Gdx.app.exit();
	}
	
	public static colorgame getInstance()
	{
		return instance;
	}	
	
	public void toggleFullScreen()
	{
		if(!Gdx.graphics.isFullscreen())
		{
			Gdx.graphics.setDisplayMode(WIDTH_RESOLUTION*10, HEIGHT_RESOLUTION*10, true);
		}
		else
		{
			Gdx.graphics.setDisplayMode(WIDTH_RESOLUTION*10, HEIGHT_RESOLUTION*10, false);
		}
	}
}
