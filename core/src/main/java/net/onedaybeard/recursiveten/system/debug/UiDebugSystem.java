package net.onedaybeard.recursiveten.system.debug;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;

import lombok.ArtemisSystem;
import lombok.Getter;
import lombok.Profile;
import net.onedaybeard.dominatrix.artemis.EntityFactoryManager;
import net.onedaybeard.dominatrix.artemis.JsonComponentFactory;
import net.onedaybeard.dominatrix.artemis.JsonComponentFactory.FactoryInstance;
import net.onedaybeard.dominatrix.experimental.ui.ComponentReflexHud;
import net.onedaybeard.dominatrix.experimental.ui.ComponentsHud;
import net.onedaybeard.dominatrix.experimental.ui.ComponentsHud.OnEntityChangedListener;
import net.onedaybeard.dominatrix.experimental.ui.EntityInspectorHud;
import net.onedaybeard.dominatrix.experimental.ui.EntityInspectorHud.JsonKeyResolver;
import net.onedaybeard.dominatrix.experimental.ui.NotificationHud;
import net.onedaybeard.dominatrix.experimental.ui.SystemsHud;
import net.onedaybeard.dominatrix.reflect.FieldTypeWriter;
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;
import net.onedaybeard.keyflection.sort.ShortcutComparator;
import net.onedaybeard.recursiveten.Assets;
import net.onedaybeard.recursiveten.Director;
import net.onedaybeard.recursiveten.component.JsonKey;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.event.CommandEvent.Type;
import net.onedaybeard.recursiveten.event.CommandEventListener;
import net.onedaybeard.recursiveten.manager.ShaderEffectsManager;
import net.onedaybeard.recursiveten.profile.Profiler;
import net.onedaybeard.recursiveten.system.event.EventSystem;
import net.onedaybeard.recursiveten.ui.CommandHelpOverlay;
import net.onedaybeard.recursiveten.ui.lsystem.LSystemEditorHud;
import net.onedaybeard.recursiveten.ui.shader.ShaderEditorHud;
import net.onedaybeard.recursiveten.util.UnitCoordinates;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	managers={EntityFactoryManager.class, ShaderEffectsManager.class},
	systems=EventSystem.class)
public final class UiDebugSystem extends VoidEntitySystem
{
	
	@Getter private Stage stage;
	private EntityInspectorHud inspectorHud;
	private ComponentReflexHud reflexHud;
	private CommandHelpOverlay helpOverlay;
	private NotificationHud notificationHud;
	private ComponentsHud componentsHud;
	private LSystemEditorHud lsystemHud;
	private ShaderEditorHud shaderHud;
	private SystemsHud systemsHud;

	@Getter private InputMultiplexer multiplexer;
	private OrthographicCamera camera;
	
	public UiDebugSystem(OrthographicCamera camera)
	{
		super();
		this.camera = camera;
	}

	@Override
	protected void initialize()
	{
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		this.stage = new Stage(w, h, false, Director.instance.getSpriteBatch());
		
		Skin skin = Assets.getAssetManager().get("gfx/ui/uiskin.json", Skin.class);
		
		inspectorHud = new EntityInspectorHud(skin, stage, world);
		inspectorHud.setJsonKeyResolver(new JsonKeyResolver()
		{
			@Override
			public String getKey(Entity e)
			{
				JsonKey json = e.getComponent(JsonKey.class);
				return json != null ? json.name() : null;
			}
		});
		
		reflexHud = new ComponentReflexHud(skin, stage);
		reflexHud.addParser(new StringArrayFieldWriter());
		helpOverlay = new CommandHelpOverlay(skin, stage);
		notificationHud = new NotificationHud(skin, stage);
		systemsHud = new SystemsHud(skin, stage, world);
		componentsHud = new ComponentsHud(skin, stage, JsonKey.class);
		componentsHud.setOnEntityChangedListener(new OnEntityChangedListener()
		{
			@Override
			public void onComponentAdded(int entityId, Class<? extends Component> addedType)
			{
				setEntity(null);
				setEntity(world.getEntity(entityId));
			}
		});
		lsystemHud = new LSystemEditorHud(skin, stage, world);
		shaderHud = new ShaderEditorHud(skin, stage, world);
		
		inspectorHud.setVisible(true);
		initInput();
	}
	
	private void initInput()
	{
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new KeyflectionInputProcessor(new Shortcuts()));
		multiplexer.addProcessor(stage);
		
		eventSystem.addReceiver(new CommandEventListener()
		{
			@Override
			protected boolean onReceive(CommandEvent event, Type type)
			{
				switch (type)
				{
					case ENTITY_SELECTED:
						setEntity((event.getIntValue() != -1)
							? world.getEntity(event.getIntValue()) : null);
						break;
					case ENTITY_HOVERED:
						if (event.getIntValue() != -1)
							inspectorHud.setEntity(world.getEntity(event.getIntValue()));
						break;
					case NO_HOVERED_ENTITY:
						inspectorHud.setEntity(null);
						break;
					case ENTITY_CHANGED_IN_WORLD:
						setEntity(null);
						setEntity(world.getEntity(event.getIntValue()));
						break;
					case SHOW_LSYSTEM:
						lsystemHud.setVisible(true);
						shaderHud.setVisible(false);
						break;
					case SHOW_SHADER_LIST:
						lsystemHud.setVisible(false);
						shaderHud.setVisible(true);
						break;
					case SHOW_SHADER_SETTINGS:
						shaderHud.setEntity(world.getEntity(event.getIntValue()));
						lsystemHud.setVisible(false);
						shaderHud.setVisible(true);
						break;
						
					default:
						break;
				}
				
				return false;
			}
		});
	}
	
	private void setEntity(Entity e)
	{
		inspectorHud.setEntity(e);
		reflexHud.setEntity(e);
		lsystemHud.setEntity(e);
	}

	@Override
	protected void processSystem()
	{
		stage.act();
		stage.draw();
		
//		lsystemHud.enableDebug();
//		Table.drawDebug(stage);
	}

	protected class Shortcuts implements CommandController
	{
		@Command(name="quit/close component window", bindings=@Shortcut({Keys.CONTROL_LEFT, Keys.Q}))
		public void exit()
		{
			if (componentsHud.isVisible())
				componentsHud.setVisible(false);
			else if (reflexHud.isVisible())
				reflexHud.setVisible(false);
			else
				Gdx.app.exit();
		}
		
		@Command(name="help!", bindings=@Shortcut(Keys.F1))
		public void toggleHelp()
		{
			helpOverlay.toggle();
		}
		
		@Command(name="cycle entity inspector view", bindings=@Shortcut(Keys.F2))
		public void toggleEntityInspectorView()
		{
			inspectorHud.cycleInspectorView();
		}
		
		@Command(name="entity inspector", bindings=@Shortcut({Keys.SHIFT_LEFT, Keys.F2}))
		public void toggleEntityInspector()
		{
			inspectorHud.toggle();
		}
		
		@Command(name="component editor", bindings=@Shortcut(Keys.F3))
		public void toggleComponentEditor()
		{
			reflexHud.toggle();
		}
		
		@Command(name="add component(s)", bindings=@Shortcut(Keys.F4))
		public void addComponentsToEditor()
		{
			if (componentsHud.isVisible())
			{
				componentsHud.setVisible(false);
				return;
			}
			
			Entity selected = reflexHud.getEntity();
			if (selected != null)
				componentsHud.showFor(selected);
		}
		
		@Command(name="system", bindings=@Shortcut(Keys.F5))
		public void toggleSystems()
		{
			systemsHud.toggle();
		}
		
		@Command(name="shaders", bindings=@Shortcut(Keys.F8))
		public void toggleShaderList()
		{
			Director.instance.send(Type.SHOW_SHADER_SETTINGS, shaderEffectsManager.getActive().get(0).getId());
		}
		
		@Command(name="lsystem", bindings=@Shortcut(Keys.F9))
		public void toggleLSystemEditor()
		{
			Director.instance.send(Type.SHOW_LSYSTEM);
		}
		
		@Command(name="recalculate lsystems", bindings=@Shortcut(Keys.F10))
		public void recalculateLSystems()
		{
			lsystemHud.updateEntity();
		}
		
		@Command(name="copy json (entity)", bindings=@Shortcut({Keys.CONTROL_LEFT, Keys.C}))
		public void copyHoveredEntity()
		{
			String json = inspectorHud.getJsonForHovered();
			if (json != null)
			{
				notificationHud.setText("%s copied to clipboard.", reflexHud.getEntity());
				Gdx.app.getClipboard().setContents(json);
			}
			else
			{
				notificationHud.setText("No entity to copy to clipboard.");
			}
		}
		
		@Command(name="paste entity at cursor", bindings={
			@Shortcut({Keys.CONTROL_LEFT, Keys.V})})
		public void pasteEntity()
		{
			try
			{
				FactoryInstance factoryInstance = JsonComponentFactory.from(
					"{\n" + Gdx.app.getClipboard().getContents() + "\n}",
					JsonKey.class.getPackage().getName());
				
				String type = factoryInstance.getEntityTypes().first();
				Array<Component> components = factoryInstance.getComponents(type);
				Entity entity = entityFactoryManager.create(components);

				int x = Gdx.input.getX();
				int y = Gdx.input.getY();
				UnitCoordinates coordinates = new UnitCoordinates(camera);
				
				Position position = new Position(coordinates.coordinateAtPixel(x, y));
				entity.addComponent(position);
				
				Director.instance.send(CommandEvent.Type.ENTITY_SELECTED, entity.getId());
			}
			catch (Exception e)
			{
				notificationHud.setText("Error while parsing clipboard entity data. See stack trace.");
				e.printStackTrace();
			}
		}
		
		@Command(name="delete selected (entity)", bindings=@Shortcut({Keys.CONTROL_LEFT, Keys.D}))
		public void deleteSelectedEntity()
		{
			if (reflexHud.getEntity() != null)
			{
				notificationHud.setText("Deleted entity %s.", reflexHud.getEntity());
				world.deleteEntity(reflexHud.getEntity());
				Director.instance.send(CommandEvent.Type.ENTITY_SELECTED, -1);
			}
			else
			{
				notificationHud.setText("No entity selected. Unable to delete.");
			}
		}

		@Override
		public String groupName()
		{
			return "Artemis";
		}

		@Override
		public Comparator<Method> commandComparator()
		{
			return new ShortcutComparator();
		}
	}
	
	private static class StringArrayFieldWriter implements FieldTypeWriter
	{

		@Override
		public Class<?> getType()
		{
			return String[].class;
		}

		@Override
		public Object parse(String value, Field reference)
		{
			assert value.startsWith("[") && value.endsWith("]");
			String s = value.substring(1, value.length() - 2);
			return s.split(", ");
		}
		
	}
}
