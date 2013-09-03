package net.onedaybeard.recursiveten.manager;

import lombok.ArtemisManager;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.Scale;
import net.onedaybeard.recursiveten.component.Size;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.math.Vector2;

@ArtemisManager(
	requires={Size.class},
	optional={AnchorPoint.class, Scale.class})
public final class AnchorPointManager extends Manager
{
	@Override
	protected void initialize() {}
	
	@Override
	public void added(Entity e)
	{
		updateAnchorPoint(getAnchorPoint(e), sizeMapper.get(e));
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
	
	private static void updateAnchorPoint(AnchorPoint anchorPoint, Size size)
	{
		Vector2 anchor = anchorPoint.point;
		anchorPoint.calculated.set(size.width * anchor.x, size.height * anchor.y);
	}
}
