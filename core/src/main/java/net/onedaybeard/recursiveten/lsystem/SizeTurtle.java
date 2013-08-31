package net.onedaybeard.recursiveten.lsystem;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@NoArgsConstructor @ToString
public class SizeTurtle extends BaseTurtle
{
	private float minX, maxX;
	private float minY, maxY;
	
	@Override
	protected void forward(float amount, boolean draw)
	{
		TurtleData td = getTurtle();
		
		Vector2 length = vector2(amount, 0).rotate(td.angle);
		Vector2 newPosition = vector2(td.position).add(length);

		td.position.set(newPosition);
		
		if (td.position.x > maxX) maxX = td.position.x;
		if (td.position.x < minX) minX = td.position.x;
		if (td.position.y > maxY) maxY = td.position.y;
		if (td.position.y < minY) minY = td.position.y;
		
		free(newPosition);
		free(length);
	}
	
	public Rectangle getSize()
	{
		return new Rectangle(minX, minY, (maxX - minX), (maxY - minY));
	}
}
