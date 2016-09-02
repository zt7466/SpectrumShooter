package src;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import enemies.Blob;
import enemies.Enemies;
import spawning.RampingMode;
import spawning.SpawningMethod;

public class GameScreen implements Screen {
	private Viewport viewport;
	private Viewport pixViewport;

	private Stage stage;
	private Stage pixStage;
	private Stage startStage;
	
	private SpriteBatch batch;

	private OrthographicCamera camera;
	private OrthographicCamera pixCamera;

	private ShapeRenderer shapeRenderer;

	private ArrayList<Enemies> enemies = new ArrayList<Enemies>();
	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	private static float CircleWidth;
	private final static float bulletSpeed = 3;
	private static float fireDelay = .1f;
	private final static float FIREDELAY = .1f;
	private int WIDTH_RESOLUTION;
	private int HEIGHT_RESOLUTION;
	private Color playerColor = new Color(0.33f, 0.33f, 0.33f, 1);
	private Color BackgroundColor = Color.BLUE;

	private ProgressBar redBar;
	private ProgressBar greenBar;
	private ProgressBar blueBar;

	private ProgressBar healthBar;

	private final int MAX_COMBINATION = 125;

	private Sprite circleSprite = new Sprite(new Texture(Gdx.files.local("whiteCircle.png")));
	private Sprite bar;
	private ProgressBarStyle healthBarStyle;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	private World world;
	private SpawningMethod spawningMethod;
	private ColorIncrementor colorIncrementor = new ColorIncrementor();
	
	private int count = 0;
	private static GameScreen instance;
	
	private boolean isSpawning = false;

	public GameScreen() 
	{
		instance = this;
		WIDTH_RESOLUTION = colorgame.getInstance().WIDTH_RESOLUTION;
		HEIGHT_RESOLUTION = colorgame.getInstance().HEIGHT_RESOLUTION;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH_RESOLUTION, HEIGHT_RESOLUTION);

		pixCamera = new OrthographicCamera();
		pixCamera.setToOrtho(false, WIDTH_RESOLUTION * 10, HEIGHT_RESOLUTION * 10);

		viewport = new FitViewport(WIDTH_RESOLUTION, HEIGHT_RESOLUTION);
		pixViewport = new FitViewport(WIDTH_RESOLUTION * 10, HEIGHT_RESOLUTION * 10);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		stage = new Stage(viewport, batch);
		pixStage = new Stage(pixViewport, batch);
		startStage = new Stage(pixViewport, batch);
		
		CircleWidth = circleSprite.getWidth() / 2 / 10;

		world = new World(new Vector2(0, 0), true);

		spawningMethod = new RampingMode(MAX_COMBINATION, CircleWidth);

		world.setContactListener(new ListenClass());

		Image image = new Image(circleSprite);

		Table gameTable = new Table();
		gameTable.setFillParent(true);
		gameTable.add(image).size(image.getWidth() / 10);
		stage.addActor(gameTable);

		createColorBars();

		Texture progressTexture = new Texture(Gdx.files.local("sideRectangle.png"));
		Sprite backgroundSprite = new Sprite(progressTexture);
		backgroundSprite.setSize(25, 3);
		SpriteDrawable backgroundSpriteDrawable = new SpriteDrawable(backgroundSprite);
		bar = new Sprite(progressTexture);
		bar.setColor(Color.RED);
		bar.setSize(3, 3);
		SpriteDrawable barDrawable = new SpriteDrawable(bar);

		healthBarStyle = new ProgressBarStyle(backgroundSpriteDrawable, barDrawable);
		healthBarStyle.knobBefore = barDrawable;
		healthBar = new ProgressBar(0, 100, 1, false, healthBarStyle);
		healthBar.setValue(100);
		Table healthBarTable = new Table();
		healthBarTable.setFillParent(true);
		healthBarTable.add(healthBar).minHeight(3).maxWidth(50).padTop(CircleWidth * 2);
		stage.addActor(healthBarTable);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("master_of_break.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = colorgame.getInstance().HEIGHT_RESOLUTION / 5;
		parameter.borderWidth = 2;
		parameter.borderColor = Color.BLACK;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		LabelStyle textStyle = new LabelStyle();
		textStyle.font = generator.generateFont(parameter);
		textStyle.fontColor = Color.WHITE;
		generator.dispose();
		
		Table spawningTable = new Table();
		spawningTable.setFillParent(true);
	
		spawningTable.add(new Label("Pregame Mode: Take a Moment to Mess with the Controls", textStyle)).row();
		spawningTable.add(new Label("Right Click to Start Spawning", textStyle)).padBottom(CircleWidth * 20);
		startStage.addActor(spawningTable);
		
		OptionsTable.createOptionsTable(pixStage);
		OptionsTable.changeOptionColor(Color.WHITE);
	}

	private void createColorBars() {
		Texture BackgroundTexture = new Texture(Gdx.files.local("colorBar.png"));
		Texture progressFill = new Texture(Gdx.files.local("barFill.png"));

		Sprite redBackground = new Sprite(BackgroundTexture);
		redBackground.setSize(BackgroundTexture.getWidth() / 10, BackgroundTexture.getHeight() / 10);
		SpriteDrawable redPicture1 = new SpriteDrawable(redBackground);
		Sprite redFill = new Sprite(progressFill);
		redFill.setColor(Color.RED);
		redFill.setSize(3, 3);
		SpriteDrawable redPicture2 = new SpriteDrawable(redFill);

		ProgressBarStyle redStyle = new ProgressBarStyle(redPicture1, redPicture2);
		redStyle.knobBefore = redPicture2;

		redBar = new ProgressBar(0, 100, 1, true, redStyle);
		redBar.setValue(playerColor.r * 100);

		Sprite greenBackground = new Sprite(BackgroundTexture);
		greenBackground.setSize(BackgroundTexture.getWidth() / 10, BackgroundTexture.getHeight() / 10);
		SpriteDrawable greenPicture1 = new SpriteDrawable(new Sprite(greenBackground));
		Sprite greenKnob = new Sprite(progressFill);
		greenKnob.setColor(Color.GREEN);
		greenKnob.setSize(3, 3);
		SpriteDrawable greenPicture2 = new SpriteDrawable(greenKnob);

		ProgressBarStyle greenStyle = new ProgressBarStyle(greenPicture1, greenPicture2);
		greenStyle.knobBefore = greenPicture2;

		greenBar = new ProgressBar(0, 100, 1, true, greenStyle);
		greenBar.setValue(playerColor.g * 100);

		Sprite blueBackground = new Sprite(BackgroundTexture);
		blueBackground.setSize(BackgroundTexture.getWidth() / 10, BackgroundTexture.getHeight() / 10);
		SpriteDrawable bluePicture1 = new SpriteDrawable(blueBackground);
		Sprite blueKnob = new Sprite(progressFill);
		blueKnob.setSize(3, 3);
		blueKnob.setColor(Color.BLUE);
		SpriteDrawable bluePicture2 = new SpriteDrawable(blueKnob);

		ProgressBarStyle blueStyle = new ProgressBarStyle(bluePicture1, bluePicture2);
		blueStyle.knobBefore = bluePicture2;

		blueBar = new ProgressBar(0, 100, 1, true, blueStyle);
		blueBar.setValue(playerColor.r * 100);

		Sprite background = new Sprite(progressFill);
		background.setColor(Color.DARK_GRAY);
		SpriteDrawable backgroundDrawable = new SpriteDrawable(background);

		Table colorSelectors = new Table();
		colorSelectors.add(redBar).pad(1).minHeight(50);
		colorSelectors.add(greenBar).pad(1).minHeight(50);
		colorSelectors.add(blueBar).pad(1).minHeight(50);
		colorSelectors.setBackground(backgroundDrawable);
		Table extraTable = new Table();
		extraTable.setFillParent(true);
		extraTable.add(colorSelectors).size(15, 52).padRight(CircleWidth * 3);

		stage.addActor(extraTable);
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isButtonPressed(Buttons.RIGHT))
		{
			isSpawning = true;
		}
		
		BackgroundColor = colorIncrementor.incrementColor();
		bar.setColor(colorIncrementor.getInverse());
		SpriteDrawable colorChange = new SpriteDrawable(bar);
		healthBarStyle.knob = colorChange;
		healthBarStyle.knobBefore = colorChange;
		
		Gdx.gl.glClearColor(BackgroundColor.r, BackgroundColor.g, BackgroundColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(1 / 30f, 9, 2);

		if(isSpawning)
		{
		enemies = spawningMethod.update(delta, enemies, world);
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).checkIfDead()) {
				world.destroyBody(enemies.get(i).getFixture().getBody());
				enemies.remove(i);
				i--;
			}
		}

		for (int j = 0; j < projectiles.size(); j++) {
			projectiles.get(j).update(delta);
			if (!projectiles.get(j).getVisible()) {
				world.destroyBody(projectiles.get(j).getFixture().getBody());
				projectiles.remove(j);
				j--;
			}
		}

		if (fireDelay != 0) {
			fireDelay = fireDelay - delta;
			if (fireDelay < 0) {
				fireDelay = 0;
			}
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			float xValue = Gdx.input.getX() / 10 - WIDTH_RESOLUTION / 2;
			float yValue = Gdx.input.getY() / 10 - HEIGHT_RESOLUTION / 2;
			float deg = MathUtils.radiansToDegrees * MathUtils.atan2(xValue, yValue);
			Shoot(deg, Color.BLACK);
		}

		incrementPlayerColor();

		stage.draw();
		pixStage.draw();

		for (int i = 0; i < enemies.size(); i++) {
			shapeRenderer.begin(ShapeType.Filled);

			shapeRenderer.setColor(enemies.get(i).getColor());

			shapeRenderer.circle(enemies.get(i).getX() * 10, enemies.get(i).getY() * 10, enemies.get(i).getDiameter());

			shapeRenderer.setColor(enemies.get(i).getDamageColor());
			shapeRenderer.circle(enemies.get(i).getX() * 10, enemies.get(i).getY() * 10,
					enemies.get(i).getDiameter() / 2);
			shapeRenderer.end();
		}

		for (int j = 0; j < projectiles.size(); j++) {
			shapeRenderer.begin(ShapeType.Filled);

			shapeRenderer.setColor(projectiles.get(j).getColor());

			shapeRenderer.circle(projectiles.get(j).getX() * 10, projectiles.get(j).getY() * 10,
					projectiles.get(j).getDiameter());

			shapeRenderer.end();
		}

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(playerColor);
		shapeRenderer.circle(WIDTH_RESOLUTION / 2 * 10, HEIGHT_RESOLUTION / 2 * 10, 25);
		shapeRenderer.end();

		if(!isSpawning)
		{
			startStage.draw();
		}
		
		// debugRenderer.render(world, camera.combined);
	}

	private void incrementPlayerColor() {
		boolean aPressed = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean sPressed = Gdx.input.isKeyPressed(Input.Keys.S);
		boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.D);

		if (aPressed || sPressed || dPressed) {
			int count = 0;
			if (aPressed)
				count++;
			if (sPressed)
				count++;
			if (dPressed)
				count++;
			double maxValue = MAX_COMBINATION / count;

			int red = (int) Math.round((playerColor.r * 100));
			int green = (int) Math.round((playerColor.g * 100));
			int blue = (int) Math.round((playerColor.b * 100));

			if (aPressed && red < maxValue) {
				red = red + 2;
				if (red > maxValue)
					red = (int) maxValue;

				boolean gDrainable = (sPressed && green > maxValue) || (!sPressed && green > 0);
				boolean bDrainable = (dPressed && blue > maxValue) || (!dPressed && blue > 0);

				if (gDrainable && bDrainable) {
					green--;
					blue--;
				} else if (gDrainable) {
					green = green - 2;
				} else if (bDrainable) {
					blue = blue - 2;
				}
			}

			if (sPressed && green < maxValue) {
				green = green + 2;
				if (green > maxValue)
					green = (int) maxValue;

				boolean rDrainable = (aPressed && red > maxValue) || (!aPressed && red > 0);
				boolean bDrainable = (dPressed && blue > maxValue) || (!dPressed && blue > 0);

				if (rDrainable && bDrainable) {
					red--;
					blue--;
				} else if (rDrainable) {
					red = red - 2;
				} else if (bDrainable) {
					blue = blue - 2;
				}
			}

			if (dPressed && blue < maxValue) {
				blue = blue + 2;
				if (blue > maxValue)
					blue = (int) maxValue;

				boolean rDrainable = (aPressed && red > maxValue) || (!aPressed && red > 0);
				boolean gDrainable = (sPressed && green > maxValue) || (!sPressed && green > 0);

				if (rDrainable && gDrainable) {
					red--;
					green--;
				} else if (rDrainable) {
					red = red - 2;
				} else if (gDrainable) {
					green = green - 2;
				}
			}

			redBar.setValue(red);
			greenBar.setValue(green);
			blueBar.setValue(blue);
			playerColor = new Color((float) red / 100, (float) green / 100, (float) blue / 100, 1);
		}
	}

	public void Shoot(float deg, Color color) {
		if (fireDelay == 0) {
			projectiles.add(new Projectile(bulletSpeed, deg, CircleWidth, playerColor, world));

			fireDelay = FIREDELAY;
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() 
	{
		while(projectiles.size() != 0)
		{
			try
			{
				world.destroyBody(projectiles.get(0).getFixture().getBody());
			}
			catch(Exception exception)
			{
			
			}	
			projectiles.remove(0);
		}
		
		while(enemies.size() != 0)
		{
			try
			{
				world.destroyBody(enemies.get(0).getFixture().getBody());
			}
			catch(Exception exception)
			{
				
			}
			enemies.remove(0);
		}
	}

	public static GameScreen getInstance() {
		return instance;
	}

	public void addDeadCount()
	{
		count++;
	}
	
	public void takeDamage(int damage) {
		healthBar.setValue(healthBar.getValue() - damage);

		if (healthBar.getValue() <= 0) {
			dispose();
			colorgame.getInstance().setScreen(new GameOverScreen(count));
		}
	}
}

class ListenClass implements ContactListener {

	@Override
	public void beginContact(Contact contact) {

	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		if (b.getUserData().getClass() == Blob.class && a.getUserData().getClass() == Projectile.class) {
			b = contact.getFixtureA();
			a = contact.getFixtureB();
		}

		if (a.getUserData().getClass() == Blob.class && b.getUserData().getClass() == Projectile.class) {
			Blob blob = (Blob) a.getUserData();
			Projectile projectile = (Projectile) b.getUserData();

			blob.takeHit(projectile.getColor(), 10);
			projectile.hit();
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
