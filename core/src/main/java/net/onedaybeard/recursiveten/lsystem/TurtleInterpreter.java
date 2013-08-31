package net.onedaybeard.recursiveten.lsystem;

import java.util.Random;

import net.onedaybeard.recursiveten.component.TurtleProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @ToString
public class TurtleInterpreter
{
	@Getter private final BaseTurtle turtle;
	
	public void execute(TurtleCommand command, TurtleProcessor processor)
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
}
