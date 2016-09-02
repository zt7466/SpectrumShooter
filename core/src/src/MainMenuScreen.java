package src;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import enemies.Blob;
import enemies.Enemies;

/**
 * MainMenuScreen.java
 * Screen for the main menu
 * @author Zachary Thompson
 *
 */
public class MainMenuScreen implements Screen
{
	Viewport viewport;
	Stage stage;
	Stage backgroundStage;
	private SpriteBatch batch;
	OrthographicCamera camera;
	
	LabelStyle titleLabelStyle;

	TextButton newGameButton;
	
	//Textures used in the mainmenu's creation 
	Texture ButtonTexture;
	
	char currentChange = 'r';
	boolean add = true;
	Color backgroundColor;
	final float maxValue = .75f;
	final float minValue = 0.0f;
	final float changevalue = .005f;
	
	private static float CircleWidth;
	private final float maxSpeed = 3;
	private final float minSpeed = 1;
	private final float minSize = 15;
	private final float possibleSize = 15;
	private float circleWidth;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	TextButtonStyle textStyle;
	Label TitleLable;
	ColorIncrementor colorIncrementor;
	World world;
	private ArrayList<Enemies> enemies = new ArrayList<Enemies>();
	
	/**
	 * Constructor for the MainMenu Screen
	 */
	public MainMenuScreen() 
	{	
		colorIncrementor = new ColorIncrementor();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, colorgame.getInstance().WIDTH_RESOLUTION * 10, colorgame.getInstance().HEIGHT_RESOLUTION * 10);
		
		viewport = new FitViewport(colorgame.getInstance().WIDTH_RESOLUTION * 10, colorgame.getInstance().HEIGHT_RESOLUTION*10);
		batch = new SpriteBatch();
		
		stage = new Stage(viewport, batch);
		backgroundStage = new Stage(viewport, batch);
		
		Table table = new Table();
		table.setFillParent(true);
		Sprite circleSprite = (new Sprite(new Texture(Gdx.files.local("whiteCircle.png"))));
		table.add(new Image(circleSprite));
		backgroundStage.addActor(table);
		
		CircleWidth = circleSprite.getWidth() / 2 / 10;
		world = new World(new Vector2(0, 0), true);
		
		for (int i = 0; i < 30; i++) {
			Color blobColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
			enemies.add(new Blob(30, (float) Math.random() * 360f, (float)( Math.random() * maxSpeed) + minSpeed, CircleWidth,
					(float) Math.random() * possibleSize + minSize, blobColor, world));
		}

		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("master_of_break.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = colorgame.getInstance().HEIGHT_RESOLUTION;
		parameter.borderWidth = 2;
		parameter.borderColor = Color.BLACK;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		
		Table menuTable = new Table();
		menuTable.setFillParent(true);
		titleLabelStyle = new LabelStyle(generator.generateFont(parameter), new Color(0.255f, 0.412f, 0.882f, 1f));
		TitleLable = new Label("Spectrum Shooter", titleLabelStyle);
		menuTable.add(TitleLable).padBottom(30);
		menuTable.row();
		
		
		parameter.size = colorgame.getInstance().HEIGHT_RESOLUTION / 2;
		textStyle = new TextButtonStyle();
		textStyle.font = generator.generateFont(parameter);
		textStyle.fontColor = Color.WHITE;
		generator.dispose();
		
		newGameButton = new TextButton("New Game", textStyle);
		newGameButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				colorgame.getInstance().setScreen(new GameScreen());
				dispose();
			}
		});
		
		menuTable.add(newGameButton).size(200, 75);
		menuTable.row();
		
		OptionsTable.createOptionsTable(stage);
		stage.addActor(menuTable);
	}

	/**
	 * Method is called every method to continue to render
	 */
	@Override
	public void render(float delta) 
	{
		backgroundColor = colorIncrementor.incrementColor();
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Color Inverse = colorIncrementor.getInverse();
				
		titleLabelStyle.fontColor = Inverse;
		camera.update();
		
		world.step(1 / 30f, 9, 2);
		
		backgroundStage.draw();
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).SpecialUpdate();
			if (enemies.get(i).checkIfDead()) {
				world.destroyBody(enemies.get(i).getFixture().getBody());
				enemies.remove(i);
				Color blobColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
				enemies.add(new Blob(30, (float) Math.random() * 360f, ((float) Math.random() * maxSpeed) + minSpeed, CircleWidth,
						(float) Math.random() * possibleSize + minSize, blobColor, world));
				i--;
			}
		}
		

		for (int i = 0; i < enemies.size(); i++) {
			shapeRenderer.begin(ShapeType.Filled);

			shapeRenderer.setColor(enemies.get(i).getColor());

			shapeRenderer.circle(enemies.get(i).getX() * 10, enemies.get(i).getY() * 10, enemies.get(i).getDiameter());

			shapeRenderer.end();
		}
		
		stage.draw();
	}



	/**
	 * called when the screen is resized, would not recommend resizing screen however and it get weird and cramped
	 */
	@Override
	public void resize(int width, int height) 
	{
		stage.getViewport().setScreenSize(width, height);
	}
	
	/**
	 * Should not be called
	 */
	@Override
	public void show() 
	{
		
	}
	
	/**
	 * Should not be called
	 */
	@Override
	public void pause()
	{
		newGameButton.setDisabled(true);
	}

	/**
	 * Should not be called
	 */
	@Override
	public void resume() 
	{
		newGameButton.setDisabled(false);
	}

	/**
	 * should not be called
	 */
	@Override
	public void hide() 
	{
		
	}

	/**
	 * Called when the screen is changed to save resources 
	 */
	@Override
	public void dispose() 
	{
	stage.dispose();	
	}
	

	
}
