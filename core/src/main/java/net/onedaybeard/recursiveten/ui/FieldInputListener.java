package net.onedaybeard.recursiveten.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.recursiveten.Director;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.event.CommandEvent.Type;

import com.artemis.Entity;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

@RequiredArgsConstructor
public class FieldInputListener extends InputListener
{
	private final Reflex reflex;
	@Getter private final Object instance;
	private final String field;
	
	private Type eventType = null;
	private int entityId;

	@Override
	public boolean keyDown(InputEvent event, int keycode)
	{
		if (keycode == Keys.ESCAPE || keycode == Keys.ENTER)
		{
			TextField text = (TextField)event.getListenerActor();
			
			boolean success = reflex.on(instance, field).set(text.getText());
			text.getStyle().fontColor = (success ? Color.WHITE : TextFieldFactory.errorColor);
			if (success)
				text.setText(reflex.on(instance, field).getAsString());
			
			assert(eventType != null);
			Director.instance.send(eventType, entityId);
		}
		if (keycode == Keys.ESCAPE)
		{
			Stage stage = event.getListenerActor().getStage();
			stage.setKeyboardFocus(event.getListenerActor().getParent());
			event.cancel();
			
			TextField text = (TextField)event.getListenerActor();
			text.setText(reflex.on(instance, field).getAsString());
		}
		
		return event.isCancelled();
	}
	
	public FieldInputListener eventOnChange(CommandEvent.Type type)
	{
		eventType = type;
		return this;
	}
	

	public FieldInputListener entityId(Entity e)
	{
		this.entityId = e.getId();
		return this;
	}
}