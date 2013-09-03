package net.onedaybeard.recursiveten.manager;

import java.util.HashSet;
import java.util.Set;

import lombok.ArtemisManager;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.Renderable;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.TurtleProcessor.CommandBinding;
import net.onedaybeard.recursiveten.lsystem.LSystem;
import net.onedaybeard.recursiveten.lsystem.LSystemUtil;
import net.onedaybeard.recursiveten.lsystem.PixmapTurtle;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.lsystem.TurtleInterpreter;
import net.onedaybeard.recursiveten.system.event.EventSystem;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

@ArtemisManager(
	requires={AnchorPoint.class, DeterministicLSystem.class, Size.class, TurtleProcessor.class},
	systems=EventSystem.class)
public class LSystemSpriteGenerator extends Manager
{
	private Set<Entity> entities = new HashSet<Entity>();

	@Override
	protected void initialize() {}

	@Override
	public void added(Entity e)
	{
		process(e);
	}
	
	@Override
	public void changed(Entity e)
	{
		if (deterministicLSystemMapper.get(e).requestUpdate)
			process(e);
	}

	private void process(Entity e)
	{
		entities.add(e);
		
		DeterministicLSystem ls = deterministicLSystemMapper.get(e);
		if (ls.result == null) return;

		Size size = sizeMapper.get(e);
		Vector2 displace = anchorPointMapper.get(e).offset;
		
		PixmapTurtle turtle = new PixmapTurtle(size.width, size.height, displace.x, displace.y);
		TurtleInterpreter interpreter = new TurtleInterpreter(turtle);
		
		
		TurtleProcessor processor = turtleProcessorMapper.get(e);

		
		for (int i = 0; ls.result.length > i; i++)
		{
			interpreter.execute(parseCommand(ls.result[i], processor.commands), processor);
		}
		
		Vector2 offset = anchorPointMapper.get(e).calculated;
		
		Sprite sprite = turtle.getSprite();
		sprite.setOrigin(offset.x, offset.y);
		
		e.addComponent(new Renderable(sprite));
		e.changedInWorld();
	}

	@Override
	public void deleted(Entity e)
	{
		entities.remove(e);
	}

	private static TurtleCommand parseCommand(char data, CommandBinding[] bindings)
	{
		for (int i = 0; bindings.length > i; i++)
			if (bindings[i].key == data) return bindings[i].command;
		
		return TurtleCommand.NO_OPERATION;
	}
}
