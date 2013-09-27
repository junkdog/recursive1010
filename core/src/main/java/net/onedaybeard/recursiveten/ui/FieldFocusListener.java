package net.onedaybeard.recursiveten.ui;

import lombok.RequiredArgsConstructor;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.recursiveten.Director;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.event.CommandEvent.Type;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

@RequiredArgsConstructor
public final class FieldFocusListener extends FocusListener
{
	private final Reflex reflex;
	private final Object instance;
	private final String field;
	private Type eventType;
	private int entityId;
	
	@Override
	public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
	{
		TextField text = (TextField)event.getListenerActor();
		text.setText(reflex.on(instance, field).getAsString());

		boolean success = reflex.on(instance, field).set(text.getText());
		text.getStyle().fontColor = (success ? Color.WHITE : TextFieldFactory.errorColor);
		
		assert(eventType != null);
		Director.instance.send(eventType, entityId);
	}
	
	public FieldFocusListener eventOnChange(CommandEvent.Type type)
	{
		this.eventType = type;
		return this;
	}
	
	public FieldFocusListener entityId(Entity e)
	{
		this.entityId = e.getId();
		return this;
	}
}