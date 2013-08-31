package net.onedaybeard.recursiveten.system.render;

import java.util.Random;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.TurtleProcessor.CommandBinding;
import net.onedaybeard.recursiveten.lsystem.Turtle;
import net.onedaybeard.recursiveten.lsystem.Turtle;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={TurtleProcessor.class, DeterministicLSystem.class, Position.class})
public final class TurtleSystem extends EntityProcessingSystem
{
	private OrthographicCamera camera;
	private ShapeRenderer renderer;
	
	private final Turtle turtle;
	
	public TurtleSystem(OrthographicCamera camera)
	{
		super(null);
		this.camera = camera;
		
		renderer = new ShapeRenderer();
		
		turtle = new Turtle(renderer);
	}
	
	@Override
	protected void begin()
	{
		renderer.begin(ShapeType.Line);
		renderer.setProjectionMatrix(camera.combined);
		renderer.setColor(Color.RED);
	}
	
	@Override
	protected void end()
	{
		renderer.end();
	}
	
	@Override
	protected void process(Entity e)
	{
		DeterministicLSystem ls = deterministicLSystemMapper.get(e);
		TurtleProcessor processor = turtleProcessorMapper.get(e);
		
		char[] commands = ls.result.toCharArray();
		
		turtle.reset();
		turtle.setPosition(positionMapper.get(e).pos);
		for (int i = 0; commands.length > i; i++)
		{
			execute(parseCommand(commands[i], processor.commands), processor);
		}
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

	public static TurtleCommand parseCommand(char data, CommandBinding[] bindings)
	{
		for (int i = 0; bindings.length > i; i++)
			if (bindings[i].key == data) return bindings[i].command;
		
		return TurtleCommand.NO_OPERATION;
	}
}
