package net.onedaybeard.recursiveten.system.event;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem()
public final class EventSystem extends VoidEntitySystem
{
	private final Array<Event> events;
	private final Array<Actor> receivers;
	
	public EventSystem()
	{
		super();
		events = new Array<Event>();
		receivers = new Array<Actor>();
	}

	public void addReceiver(Actor receiver)
	{
		receivers.add(receiver);
	}
	
	public void addReceiver(EventListener receiver)
	{
		Actor actor = new Actor();
		actor.addListener(receiver);
		addReceiver(actor);
	}
	
	public void send(CommandEvent.Type type, float value)
	{
		CommandEvent e = Pools.obtain(CommandEvent.class);
		e.setType(type);
		e.setValue(value);
		
		send(e);
	}
	
	public void send(CommandEvent.Type type, int value)
	{
		CommandEvent e = Pools.obtain(CommandEvent.class);
		e.setType(type);
		e.setValue(value);
		
		send(e);
	}
	
	public void send(Event e)
	{
		assert receivers.size > 0  : "No receivers listening";
		
		events.add(e);
	}
	
	@Override
	protected void processSystem()
	{
		for (int i = 0; receivers.size > i; i++)
		{
			for (int j = 0; events.size > j; j++)
			{
				receivers.get(i).fire(events.get(j));
			}
		}
	}

	@Override
	protected void end()
	{
		if (events.size > 0)
		{
			Pools.freeAll(events);
			events.clear();
		}
	}
}
