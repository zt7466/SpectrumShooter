package src;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen 
{
	private Stage gameOverStage;
	private Color BackgroundColor = Color.BLACK;
	private ColorIncrementor colorIncrementor;
	private LabelStyle titleLabelStyle;
	private float countdownValue = 20;
	
	public GameOverScreen(int value)
	{
		colorIncrementor = new ColorIncrementor();
		int WIDTH_RESOLUTION = colorgame.getInstance().WIDTH_RESOLUTION;
		int HEIGHT_RESOLUTION = colorgame.getInstance().HEIGHT_RESOLUTION;
		
		OrthographicCamera pixCamera = new OrthographicCamera();
		pixCamera.setToOrtho(false, WIDTH_RESOLUTION * 10, HEIGHT_RESOLUTION * 10);
		
		Viewport pixViewport = new FitViewport(WIDTH_RESOLUTION * 10, HEIGHT_RESOLUTION * 10);
		gameOverStage = new Stage(pixViewport, new SpriteBatch());
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("master_of_break.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = colorgame.getInstance().HEIGHT_RESOLUTION;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		
		Table menuTable = new Table();
		menuTable.setFillParent(true);
		titleLabelStyle = new LabelStyle(generator.generateFont(parameter), new Color(0.255f, 0.412f, 0.882f, 1f));
		Label TitleLable = new Label("Game Over", titleLabelStyle);
		menuTable.add(TitleLable).padBottom(30).row();
		
		Table middleTable = new Table();
		parameter.size = (int) Math.floor(colorgame.getInstance().HEIGHT_RESOLUTION * 2);
		LabelStyle killedStyle = new LabelStyle(generator.generateFont(parameter), Color.WHITE);
		middleTable.add(new Label("" + value, killedStyle));
		middleTable.add(new Label(" Destroyed", titleLabelStyle));
		
		menuTable.add(middleTable).row();
		
		parameter.size = colorgame.getInstance().HEIGHT_RESOLUTION / 2;
		TextButtonStyle textStyle = new TextButtonStyle();
		textStyle.font = generator.generateFont(parameter);
		
		
		TextButton newGameButton = new TextButton("Play Again?", textStyle);
		menuTable.add(newGameButton);
		
		newGameButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				colorgame.getInstance().setScreen(new GameScreen());
			}
		});
		
		gameOverStage.addActor(menuTable);
		generator.dispose();
		
		OptionsTable.createOptionsTable(gameOverStage);
		OptionsTable.changeOptionColor(Color.WHITE);
	}
	
	@Override
	public void show() 
	{
		
	}

	@Override
	public void render(float delta) 
	{
		BackgroundColor = colorIncrementor.incrementColor();
		Gdx.gl.glClearColor(BackgroundColor.r, BackgroundColor.g, BackgroundColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		titleLabelStyle.fontColor = colorIncrementor.getInverse();
		
		gameOverStage.draw();
		countdownValue = countdownValue - delta;
		
		if(countdownValue < 0)
		{
			colorgame.getInstance().setScreen(new MainMenuScreen());
		}
	}

	@Override
	public void resize(int width, int height)
	{
		
	}

	@Override
	public void pause() 
	{	
		
	}

	@Override
	public void resume() 
	{
		
	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void dispose()
	{
		
	}

}
