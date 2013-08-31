package net.onedaybeard.recursiveten.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class CommandEventListener implements EventListener
{
	@Override
	public final boolean handle(Event event)
	{
		if (!(event instanceof CommandEvent))
			return false;
		
		CommandEvent e = (CommandEvent)event;
		return isAccepting(e, e.getType()) ? onReceive(e, e.getType()) : false;
	}
	
	protected boolean isAccepting(CommandEvent event, CommandEvent.Type type)
	{
		return true;
	}
	
	protected abstract boolean onReceive(CommandEvent event, CommandEvent.Type type);
}
