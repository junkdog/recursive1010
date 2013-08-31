package net.onedaybeard.recursiveten.manager;

import lombok.ArtemisManager;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.Scale;
import net.onedaybeard.recursiveten.component.Size;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

@ArtemisManager(
	requires={Size.class},
	optional={AnchorPoint.class, Scale.class})
public final class AnchorPointManager extends Manager
{
	private AnchorPoint defaultAnchor;
	
	@Override
	protected void initialize()
	{
		defaultAnchor = new AnchorPoint(0.5f, 0.5f);
	}
	
	@Override
	public void added(Entity e)
	{
		AnchorPoint anchor = getAnchorPoint(e);
		Scale scale = getScale(e);
		
		updateAnchorPoint(anchor, sizeMapper.get(e));
	}

	private AnchorPoint getAnchorPoint(Entity e)
	{
		if (anchorPointMapper.has(e))
			return anchorPointMapper.get(e);
		
		AnchorPoint anchor = new AnchorPoint(0.5f, 0.5f);
		e.addComponent(anchor);
		e.changedInWorld();
		
		return anchor;
	}
	
	private Scale getScale(Entity e)
	{
		if (scaleMapper.has(e))
			return scaleMapper.get(e);
		
		Scale scale = new Scale();
		e.addComponent(scale);
		e.changedInWorld();
		
		return scale;
	}
	
	private static void updateSpriteScale(Sprite sprite, float scale)
	{
		sprite.setScale(scale);
	}

	private static void updateAnchorPoint(AnchorPoint AnchorPoint, Size size)
	{
		Vector2 anchorPoint = AnchorPoint.calculated;
		anchorPoint.set(size.width * anchorPoint.x, size.height * anchorPoint.y);
	}
}
