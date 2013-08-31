package net.onedaybeard.recursiveten.component;

import lombok.ToString;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@ToString
public final class Position extends Component
{
	public final Vector2 pos;

	public Position(float x, float y)
	{
		pos = new Vector2(x, y);
	}

	public Position(Vector2 position)
	{
		pos = position.cpy();
	}
	
	public Position()
	{
		this(0, 0);
	}
}
