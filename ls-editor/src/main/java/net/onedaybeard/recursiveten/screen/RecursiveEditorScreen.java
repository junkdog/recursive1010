package net.onedaybeard.recursiveten.screen;

import net.onedaybeard.dominatrix.artemis.EntityFactoryManager;
import net.onedaybeard.dominatrix.artemis.JsonEntitySerializer;
import net.onedaybeard.keyflection.CommandManager;
import net.onedaybeard.recursiveten.Assets;
import net.onedaybeard.recursiveten.Director;
import net.onedaybeard.recursiveten.component.Cullable;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.JsonKey;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.Velocity;
import net.onedaybeard.recursiveten.input.EntityController;
import net.onedaybeard.recursiveten.input.EntityHandler;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.manager.AnchorPointManager;
import net.onedaybeard.recursiveten.manager.EntityTracker;
import net.onedaybeard.recursiveten.manager.LSystemResolverManager;
import net.onedaybeard.recursiveten.manager.LSystemSpriteGenerator;
import net.onedaybeard.recursiveten.manager.LSystemUpdateCompletionManager;
import net.onedaybeard.recursiveten.system.debug.AnchorPointRenderer;
import net.onedaybeard.recursiveten.system.debug.CameraInfoSystem;
import net.onedaybeard.recursiveten.system.debug.EntityOutlineRenderer;
import net.onedaybeard.recursiveten.system.debug.InputHoverSystem;
import net.onedaybeard.recursiveten.system.debug.MouseInfoSystem;
import net.onedaybeard.recursiveten.system.debug.UiDebugSystem;
import net.onedaybeard.recursiveten.system.event.EventSystem;
import net.onedaybeard.recursiveten.system.input.CameraController;
import net.onedaybeard.recursiveten.system.input.InputHandlerSystem;
import net.onedaybeard.recursiveten.system.render.BackgroundRenderSystem;
import net.onedaybeard.recursiveten.system.render.SpriteRenderSystem;
import net.onedaybeard.recursiveten.system.spatial.PositionUpdateSystem;
import net.onedaybeard.recursiveten.system.spatial.SpritePositionUpdateSystem;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class RecursiveEditorScreen implements Screen
{
	private static final float MIN_DELTA = 1 / 24f;

	private boolean running = true;

	private final Stage ui;

	private float maxAxisValue = 1500;

	private final World world;

	private static EntityFactoryManager entityFactory;

	// private TemporalMultiplier timeMultiplier;

	public RecursiveEditorScreen()
	{
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, Director.instance.getSpriteBatch());

		OrthographicCamera camera = createCamera();
		world = initializeArtemis(camera, ui, getWorldBoundaries());

		initializeEntities(world);

		initInputProcessing(ui, camera, world);
	}

	private static OrthographicCamera createCamera()
	{
		Vector2 screen = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// screen.mul(SCREEN_HEIGHT_UNITS / screen.y);
		//
		// log(TAG, "Camera size: %.2fx%.2f", screen.x, screen.y);

		OrthographicCamera cam = new OrthographicCamera(screen.x, screen.y);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		return cam;
	}
	
	private void initializeEntities(World world)
	{
		// Entity background = world.createEntity();
		// background.addComponent(new BackgroundColor(Color.WHITE));
		// background.addComponent(new JsonKey("BACKGROUND"));
		// world.addEntity(background);
		//
		// JsonEntitySerializer json = new JsonEntitySerializer(OutputType.minimal);
		// System.out.println(json.toJson(background, null));
		entityFactory.create("BACKGROUND");
//		entityFactory.create("LS_DRAGON");
//		entityFactory.create("LS_TREE");
		Entity e = entityFactory.create("LS_BUSH");
//		world.getManager(GroupManager.class).add(e, "level");

		DeterministicLSystem lSystem = new DeterministicLSystem();
		lSystem.axiom = "0";

		lSystem.productions = new String[]{"0=1[0]0", "1=11"};
		lSystem.iteration = 5;
		lSystem.requestUpdate = false;

		Entity e2 = world.createEntity()
			.addComponent(new Position(1, 1))
			.addComponent(new Cullable())
			.addComponent(new Size())
			.addComponent(new Velocity(2, 1))
			.addComponent(lSystem)
			.addComponent(new TurtleProcessor()
				.put('0', TurtleCommand.DRAW_FORWARD)
				.put('1', TurtleCommand.DRAW_FORWARD)
				.put('[', TurtleCommand.PUSH_AND_TURN_LEFT)
				.put(']', TurtleCommand.POP_AND_TURN_RIGHT)
			);

		JsonEntitySerializer serializer = new JsonEntitySerializer(OutputType.json);
//		System.out.println(serializer.toJson(e, "LS_TREE"));
//		System.out.println();
//		System.out.println(serializer.toJson(lsTree, "LS_TREE_2"));

	}

	private static void initInputProcessing(Stage ui, OrthographicCamera camera, World world)
	{
		CommandManager.instance.setSingleModifierKeys(true);
		
//		InputHandler inputHandler = new InputHandler(new CameraController(camera));

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(ui);
//		inputMultiplexer.addProcessor(inputHandler);
//		inputMultiplexer.addProcessor(world.getSystem(InputHoverSystem.class).getInputProcessor());
		inputMultiplexer.addProcessor(world.getManager(LSystemResolverManager.class).getInputProcessor());
		inputMultiplexer.addProcessor(world.getSystem(InputHandlerSystem.class));
		inputMultiplexer.addProcessor(new EntityHandler(new EntityController(camera, world), null));
		inputMultiplexer.addProcessor(world.getSystem(UiDebugSystem.class).getMultiplexer());
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	private static World initializeArtemis(OrthographicCamera camera, Stage ui, Rectangle worldBoundaries)
	{
		World world = new World();
		SpriteBatch spriteBatch = Director.instance.getSpriteBatch();

		world.setManager(new LSystemResolverManager());
		world.setManager(new AnchorPointManager());
		world.setManager(new LSystemSpriteGenerator());
		world.setManager(new LSystemUpdateCompletionManager());
		world.setManager(new EntityTracker());

		entityFactory = world.setManager(
			EntityFactoryManager.from(Assets.getFileHandle("data/game-objects.json"), JsonKey.class));

		Director.instance.setEventSystem(world.setSystem(new EventSystem()));
		world.setSystem(new InputHandlerSystem(camera));
		world.setSystem(new SpritePositionUpdateSystem());
		world.setSystem(new BackgroundRenderSystem());
		world.setSystem(new SpriteRenderSystem(spriteBatch, camera));
		world.setSystem(new EntityOutlineRenderer(camera));
		world.setSystem(new AnchorPointRenderer(camera));
		world.setSystem(new CameraInfoSystem(camera, spriteBatch));
		world.setSystem(new MouseInfoSystem(camera, spriteBatch));
		world.setSystem(new InputHoverSystem(camera));
		world.setSystem(new UiDebugSystem());

		world.initialize();

		return world;
	}

	private Rectangle getWorldBoundaries()
	{
		Rectangle boundaries = new Rectangle(-maxAxisValue, -maxAxisValue, maxAxisValue * 2, maxAxisValue * 2);
		return boundaries;
	}

	@Override
	public void render(float delta)
	{
		if (!running)
			delta = 0;

		if (delta < MIN_DELTA) delta = MIN_DELTA;

		entityFactory.addNewToWorld();

		world.setDelta(delta);
		world.process();
	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void show()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void pause()
	{
		this.running = false;
	}

	@Override
	public void resume()
	{
		this.running = true;
	}

	@Override
	public void dispose()
	{
		ui.dispose();
		Gdx.input.setInputProcessor(null);
	}
}
