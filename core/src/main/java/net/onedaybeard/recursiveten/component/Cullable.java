package net.onedaybeard.recursiveten.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Objects outside the frustrum don't have their position updated or graphics rendered.
 * Negative width or height implies that said dimension should be excluded from cull checks.
 */
@ToString @EqualsAndHashCode(callSuper=false)
public final class Cullable extends Component
{
	public boolean culled;
	public final Vector2 size;
	
	public Cullable()
	{
		this(0, 0);
	}
	
	public Cullable(float width, float height)
	{
		size = new Vector2(width, height);
	}
}
