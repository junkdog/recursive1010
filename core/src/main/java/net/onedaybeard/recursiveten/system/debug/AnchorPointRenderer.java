package net.onedaybeard.recursiveten.system.debug;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Scale;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Position.class},
	managers=TagManager.class)
public final class AnchorPointRenderer extends EntityProcessingSystem
{
	private final ShapeRenderer debugRender;
	private final OrthographicCamera camera;

	public AnchorPointRenderer(OrthographicCamera camera)
	{
		super(null);
		debugRender = new ShapeRenderer();
		this.camera = camera;
	}

	@Override
	protected void begin()
	{
		debugRender.setProjectionMatrix(camera.combined);
		
		debugRender.begin(ShapeType.Line);
		debugRender.setColor(Color.CYAN);
	}

	@Override
	protected void process(Entity e)
	{
		renderBoundingRectangle(positionMapper.get(e).pos, 20);
	}

	private void renderBoundingRectangle(Vector2 position, float radius)
	{
		debugRender.circle(position.x, position.y, radius, 4);
	}
	
	@Override
	protected void end()
	{
		drawSelectedEntity();
		debugRender.end();
	}

	private void drawSelectedEntity()
	{
//		Entity entity = tagManager.getEntity(EntityTag.SELECTED_ENTITY);
		Entity entity = null;
		if (entity == null)
			return;
		
		debugRender.setColor(Color.CYAN);
		renderBoundingRectangle(positionMapper.get(entity).pos, 20);
	}
}
