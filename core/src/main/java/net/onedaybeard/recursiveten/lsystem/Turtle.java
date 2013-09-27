package net.onedaybeard.recursiveten.lsystem;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;
import lombok.Setter;
import lombok.ToString;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

@ToString
public class Turtle extends BaseTurtle
{
	@Setter private ShapeRenderer renderer;
	
	public Turtle(ShapeRenderer renderer)
	{
		super();
		this.renderer = renderer;
	}
	
	@Override
	protected void forward(float amount, boolean draw)
	{
		TurtleData td = getTurtle();
		
		Vector2 length = vector2(amount, 0).rotate(td.angle);
		Vector2 newPosition = vector2(td.position).add(length);

		if (draw) renderer.line(td.position.x, td.position.y, newPosition.x, newPosition.y);
		
		td.position.set(newPosition);
		
		free(newPosition);
		free(length);
	}
}
