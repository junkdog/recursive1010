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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Position.class, Size.class, AnchorPoint.class, Scale.class},
	managers=TagManager.class)
public final class EntityOutlineRenderer extends EntityProcessingSystem
{
	private final ShapeRenderer debugRender;
	private final OrthographicCamera camera;

	public EntityOutlineRenderer(OrthographicCamera camera)
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
//			position.y - (size.height / 2),
			0,
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
//		Entity entity = tagManager.getEntity(EntityTag.SELECTED_ENTITY);
		Entity entity = null;
		if (entity == null)
			return;
		
		debugRender.setColor(0, 1, 0, 1);
		renderBoundingRectangle(positionMapper.get(entity).pos, sizeMapper.get(entity));
	}
}
