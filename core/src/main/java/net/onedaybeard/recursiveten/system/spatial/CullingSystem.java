package net.onedaybeard.recursiveten.system.spatial;

import lombok.ArtemisSystem;
import net.onedaybeard.dominatrix.pool.Vector2Pool;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.Cullable;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Size;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@ArtemisSystem(
	requires={AnchorPoint.class, Cullable.class, Position.class, Size.class})
public final class CullingSystem extends EntityProcessingSystem
{
	private OrthographicCamera camera;

	private float left, right;
	private float up, down;
	
	private Rectangle bounds;

	public CullingSystem(OrthographicCamera camera)
	{
		super(null);
		this.camera = camera;
		bounds = new Rectangle();
	}

	@Override
	protected void begin()
	{
//		float w = camera.viewportWidth * camera.zoom * 0.4f;
//		left = camera.position.x - (w / 2);
//		right = left + w;
//		
//		float h = camera.viewportHeight * camera.zoom * 0.4f;
//		up = camera.position.y - (h / 2);
//		down = up + h;
		bounds.set(camera.position.x, camera.position.y, camera.viewportWidth * .75f, camera.viewportHeight * .75f);
	}

	@Override
	protected void process(Entity e)
	{
		Cullable cullable = cullableMapper.get(e);
		Size size = sizeMapper.get(e);
		
		Vector2 pos = Vector2Pool.vector2(positionMapper.get(e).pos);
		Vector2 offset = anchorPointMapper.get(e).calculated;
//		offset.y *= -1;
		pos.add(offset);
//		pos.sub(offset);
		
		
//		boolean widthCulled = !(left < (pos.x + size.width) && (pos.x - size.width) < right); 
//		boolean heightCulled = !(up < (pos.y - size.height) && (pos.y + size.height) > down); 
//		heightCulled = true;
//		heightCulled = widthCulled;
		
//		heightCulled = false;
//		cullable.culled = (widthCulled || heightCulled);
//		cullable.culled = heightCulled;
//		cullable.culled = up < (pos.y + size.height);
//		cullable.culled = up < (pos.y - size.height);
//		cullable.culled = !(left < (pos.x + (size.width / 2)));
//		cullable.culled = !((pos.x + (size.width / 2)) < right);
		cullable.culled =
			!bounds.contains(pos.x, pos.y);
//			pos.x <= camera.position.x && right >= x && this.y <= y && this.y + this.height >= y;
//			this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;

		Vector2Pool.free(pos);
	}
}
