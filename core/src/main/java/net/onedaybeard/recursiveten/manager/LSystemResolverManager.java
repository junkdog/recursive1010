package net.onedaybeard.recursiveten.manager;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import lombok.ArtemisManager;
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;
import net.onedaybeard.keyflection.sort.ShortcutComparator;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.MaxTextureDimension;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.TurtleProcessor.CommandBinding;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.lsystem.LSystem;
import net.onedaybeard.recursiveten.lsystem.LSystemUtil;
import net.onedaybeard.recursiveten.lsystem.SizeTurtle;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.lsystem.TurtleInterpreter;
import net.onedaybeard.recursiveten.system.event.EventSystem;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

@ArtemisManager(
	requires={AnchorPoint.class, DeterministicLSystem.class, MaxTextureDimension.class, Size.class, TurtleProcessor.class},
	systems=EventSystem.class)
public class LSystemResolverManager extends Manager
{
	private SizeTurtle turtle;
	private TurtleInterpreter interpreter;
	
	private Set<Entity> entities;

	private KeyflectionInputProcessor keyflectionInputProcessor;


	@Override
	protected void initialize() 
	{
		entities = new HashSet<Entity>();
		turtle = new SizeTurtle();
		interpreter = new TurtleInterpreter(turtle);
	}

	@Override
	public void added(Entity e)
	{
		process(e);
	}
	
	@Override
	public void changed(Entity e)
	{
		process(e);
	}

	private void process(Entity e)
	{
		entities.add(e);
		
		DeterministicLSystem ls = deterministicLSystemMapper.get(e);
		if (!ls.requestUpdate)
			return;
		
		ls.iteration = Math.max(0, ls.iteration);
		
		LSystem lSystem = LSystemUtil.toLSystem(ls);
		ls.result = lSystem.getIteration(ls.iteration).toCharArray();
		
		TurtleProcessor processor = turtleProcessorMapper.get(e);
		calculateSize(ls.result, processor, maxTextureDimensionMapper.get(e).value);
		Rectangle turtleSize = turtle.getSize();
		Size size = sizeMapper.get(e);
		size.width = turtleSize.width;
		size.height = turtleSize.height;
		
		System.out.printf("w=%.0f h=%.0f offset-x=%.0f offset-y=%.0f\n", turtleSize.width, turtleSize.height, turtleSize.x, turtleSize.y);
		
		AnchorPoint anchor = anchorPointMapper.get(e);
		anchor.calculated.x = turtleSize.width * anchor.point.x;
		anchor.calculated.y = turtleSize.height * anchor.point.y;
		anchor.offset.x = turtleSize.x;
		anchor.offset.y = turtleSize.y;
	}

	private void calculateSize(char[] commands, TurtleProcessor processor, float max)
	{
		turtle.reset();
		for (int i = 0; commands.length > i; i++)
			interpreter.execute(parseCommand(commands[i], processor.commands), processor);
		
		Rectangle turtleSize = turtle.getSize();
		if (max > 0 && turtleSize.width == 0 && turtleSize.height == 0)
			return;
		
		System.out.println(turtleSize);
		
		float dim = Math.max(turtleSize.width, turtleSize.height);
		if (max > dim && (dim / max) > 0.95f)
			return;
		
		processor.moveAmount /= 1 / ((max * 0.98f) / dim);
		calculateSize(commands, processor, max);
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
	
	public KeyflectionInputProcessor getInputProcessor()
	{
		if (keyflectionInputProcessor == null)
			keyflectionInputProcessor = new KeyflectionInputProcessor(new Shortcuts());
		
		return keyflectionInputProcessor;
	}
	
	public class Shortcuts implements CommandController
	{
		@Override
		public String groupName()
		{
			return "Entity control";
		}

		@Override
		public Comparator<Method> commandComparator()
		{
			return new ShortcutComparator();
		}
		
		@Command(name="increment iteration count", bindings=@Shortcut({Keys.ALT_LEFT, Keys.UP}))
		public void incrementIteration()
		{
			eventSystem.send(CommandEvent.Type.CHANGE_ITERATION_COUNT, 1);
			for (Entity e : entities)
			{
				DeterministicLSystem ls = deterministicLSystemMapper.get(e);
				ls.requestUpdate = true;
				ls.iteration++;
				e.changedInWorld();
			}
		}
		
		@Command(name="decrement iteration count", bindings=@Shortcut({Keys.ALT_LEFT, Keys.DOWN}))
		public void decrementIteration()
		{
			for (Entity e : entities)
			{
				DeterministicLSystem ls = deterministicLSystemMapper.get(e);
				ls.requestUpdate = true;
				ls.iteration--;
				e.changedInWorld();
			}
			eventSystem.send(CommandEvent.Type.CHANGE_ITERATION_COUNT, -1);
		}
	}
}
