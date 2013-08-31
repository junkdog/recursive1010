package net.onedaybeard.recursiveten.artemis;

import com.artemis.Aspect;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;

/**
 * Modified to use raw frame time.
 * <p/>
 * A system that processes entities at a interval in milliseconds.
 * A typical usage would be a collision system or physics system.
 * 
 * @author Arni Arent
 */
public abstract class TimerIntervalEntitySystem extends EntitySystem
{
	private float acc;
	private float interval;

	public TimerIntervalEntitySystem(Aspect aspect, float interval)
	{
		super(aspect);
		this.interval = interval;
	}

	@Override
	protected boolean checkProcessing()
	{
		acc += Gdx.graphics.getDeltaTime();
		if (acc >= interval)
		{
			acc -= interval;
			return true;
		}
		return false;
	}

}
