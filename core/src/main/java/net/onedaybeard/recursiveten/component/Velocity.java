package net.onedaybeard.recursiveten.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@ToString @Accessors(fluent=true) @EqualsAndHashCode(callSuper=false)
public final class Velocity extends Component
{
	public final Vector2 vector;

	public Velocity()
	{
		this(0, 0);
	}

	public Velocity(float x, float y)
	{
		vector = new Vector2(x, y);
	}

	public Velocity(Vector2 velocity)
	{
		vector = velocity.cpy();
	}
}
