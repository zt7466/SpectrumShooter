package src;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


/**
 * OptionsTable.java
 * Class to hold the creation of the options table which should be used on every screen
 * @author Zachary
 *
 */
public class OptionsTable
{
	private static TextButtonStyle optionButtons = new TextButtonStyle();
	public static void createOptionsTable(Stage stage) {

		Table optionTable = new Table();
		optionTable.setFillParent(true);
		optionTable.bottom();
		
		Gdx.input.setInputProcessor(stage);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("master_of_break.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		optionButtons.font = font12;
		optionButtons.fontColor = com.badlogic.gdx.graphics.Color.WHITE;
		TextButton fullScreenButton = new TextButton("Toggle Fullscreen", optionButtons);
		TextButton closeButton = new TextButton("Close", optionButtons);
		TextButton resetButton = new TextButton("Reset", optionButtons);
		
		optionTable.add(resetButton).padRight(30);
		//optionTable.add(fullScreenButton).padRight(30f);
		optionTable.add(closeButton).padBottom(5);
		
		stage.addActor(optionTable);
	
		resetButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				colorgame.getInstance().getScreen().dispose();
				colorgame.getInstance().setScreen(new MainMenuScreen());
				
			}
			
		});
		
		
		closeButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				colorgame.getInstance().close();
			}
		});
		
		fullScreenButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				colorgame.getInstance().toggleFullScreen();	
			}
		});	
	}
	
	/**
	 * Changes the color of the option tables font from the outside
	 * @param color to change to 
	 */
	public static void changeOptionColor(com.badlogic.gdx.graphics.Color color)
	{
		optionButtons.fontColor = color;
	}
}
