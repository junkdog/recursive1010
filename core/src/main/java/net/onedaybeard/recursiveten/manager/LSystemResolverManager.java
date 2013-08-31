package net.onedaybeard.recursiveten.manager;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lombok.ArtemisManager;
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;
import net.onedaybeard.keyflection.sort.ShortcutComparator;
import net.onedaybeard.recursiveten.component.AnchorPoint;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.TurtleProcessor.CommandBinding;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.lsystem.LSystem;
import net.onedaybeard.recursiveten.lsystem.LSystemUtil;
import net.onedaybeard.recursiveten.lsystem.SizeTurtle;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.system.event.EventSystem;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

@ArtemisManager(
	requires={DeterministicLSystem.class, Size.class, TurtleProcessor.class},
	optional=AnchorPoint.class,
	systems=EventSystem.class)
public class LSystemResolverManager extends Manager
{
	private SizeTurtle turtle;
	
	private Set<Entity> entities = new HashSet<Entity>();

	private KeyflectionInputProcessor keyflectionInputProcessor;

	@Override
	protected void initialize() 
	{
		turtle = new SizeTurtle();
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
		
		LSystem lSystem = LSystemUtil.toLSystem(ls);
		ls.result = lSystem.getIteration(ls.iteration);
		
		char[] commands = ls.result.toCharArray();
		
		TurtleProcessor processor = turtleProcessorMapper.get(e);
		turtle.reset();
		for (int i = 0; commands.length > i; i++)
		{
			execute(parseCommand(commands[i], processor.commands), processor);
		}
		Rectangle turtleSize = turtle.getSize();
		Size size = sizeMapper.get(e);
		size.width = turtleSize.width;
		size.height = turtleSize.height;

		ls.requestUpdate = false;
	}
	
	@Override
	public void deleted(Entity e)
	{
		entities.remove(e);
	}

	private void execute(TurtleCommand command, TurtleProcessor processor)
	{
		Random random = new Random(420);
		float randomizer = (processor.turnDeviation != 0)
			? (random.nextFloat() * (processor.turnDeviation)) - processor.turnDeviation
			: 0;
		
		switch(command)
		{
			case DRAW_FORWARD:
				turtle.drawForward(processor.moveAmount);
				break;
			case MOVE_FORWARD:
				turtle.moveForward(processor.moveAmount);
				break;
			case NO_OPERATION:
				break;
			case POP:
				turtle.pop();
				break;
			case POP_AND_TURN_LEFT:
				turtle.pop();
				turtle.rotate(-processor.turnAmount + randomizer);
				break;
			case POP_AND_TURN_RIGHT:
				turtle.pop();
				turtle.rotate(processor.turnAmount + randomizer);
				break;
			case PUSH:
				turtle.push();
				break;
			case PUSH_AND_TURN_LEFT:
				turtle.push();
				turtle.rotate(-processor.turnAmount + randomizer);
				break;
			case PUSH_AND_TURN_RIGHT:
				turtle.push();
				turtle.rotate(processor.turnAmount + randomizer);
				break;
			case TURN_LEFT:
				turtle.rotate(-processor.turnAmount + randomizer);
				break;
			case TURN_RIGHT:
				turtle.rotate(processor.turnAmount + randomizer);
				break;
			default:
				throw new RuntimeException("Missing command: " + command);
			
		}
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
		
		@Command(name="help!", bindings=@Shortcut(Keys.PLUS))
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
		
		@Command(name="help!", bindings=@Shortcut(Keys.MINUS))
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
