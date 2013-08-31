/**
 * Store sprite anchor point
 * This is an offset that determines the exact center of a sprite - in percent.
 * 
 *  0,0 = bottom left
 *  1,1 = top right
 *  .5,.5 = center
 * 
 * Most sprites are already centered and do not require this component, but sprites without a proper anchor point might need this.
 */
package net.onedaybeard.recursiveten.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@ToString @EqualsAndHashCode(callSuper = false)
public final class AnchorPoint extends Component
{
	public Vector2 offset;
	public final transient Vector2 calculated;
	
	public AnchorPoint()
	{
		this(0, 0);
	}
	
	public AnchorPoint(float x, float y)
	{
		offset = new Vector2(x, y);
		calculated = new Vector2();
	}
}
