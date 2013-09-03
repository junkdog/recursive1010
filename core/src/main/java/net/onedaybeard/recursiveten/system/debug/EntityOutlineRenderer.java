package net.onedaybeard.recursiveten.system.debug;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Scale;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.event.CommandEvent.Type;
import net.onedaybeard.recursiveten.event.CommandEventListener;
import net.onedaybeard.recursiveten.profile.Profiler;
import net.onedaybeard.recursiveten.system.event.EventSystem;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Position.class, Size.class, AnchorPoint.class, Scale.class},
	managers=TagManager.class,
	systems=EventSystem.class)
public final class EntityOutlineRenderer extends EntityProcessingSystem
{
	private final ShapeRenderer debugRender;
	private final OrthographicCamera camera;
	
	private Entity selected;

	public EntityOutlineRenderer(OrthographicCamera camera)
	{
		super(null);
		debugRender = new ShapeRenderer();
		this.camera = camera;
	}
	
	@Override
	protected void initialize()
	{
		eventSystem.addReceiver(new CommandEventListener()
		{
			@Override
			protected boolean onReceive(CommandEvent event, Type type)
			{
				if (event.getIntValue() != -1)
					selected = world.getEntity(event.getIntValue());
				else
					selected = null;
				
				return false;
			}

			@Override
			protected boolean isAccepting(CommandEvent event, Type type)
			{
				return type == Type.ENTITY_SELECTED;
			}
		});
	}

	@Override
	protected void begin()
	{
		debugRender.setProjectionMatrix(camera.combined);
		
		debugRender.begin(ShapeType.Line);
		debugRender.setColor(0, 1, 1, 1);
	}

	@Override
	protected void process(Entity e)
	{
		renderBoundingRectangle(positionMapper.get(e).pos, sizeMapper.get(e));
	}

	private void renderBoundingRectangle(Vector2 position, Size size)
	{
		debugRender.rect(
			position.x - (size.width / 2),
			position.y - (size.height / 2),
			size.width, size.height);
	}
	
	@Override
	protected void end()
	{
		drawSelectedEntity();
		debugRender.end();
	}

	private void drawSelectedEntity()
	{
		if (selected == null)
			return;
		
		debugRender.setColor(0, 1, 0, 1);
		renderBoundingRectangle(positionMapper.get(selected).pos, sizeMapper.get(selected));
	}
}
