package net.onedaybeard.recursiveten.ui.lsystem;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.onedaybeard.dominatrix.reflect.ColorWriter;
import net.onedaybeard.dominatrix.reflect.FieldTypeWriter;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.dominatrix.reflect.Vector2Writer;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.ui.UiUtil;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;


public final class LSystemEditorHud
{
	private final Skin skin;
	@Getter private Entity entity;
	private Table table;
	
	private final Reflex reflex;
	
	private final Color errorColor;
	
	private static final int PADDING = 5;
	private static final int WIDTH_LABEL = 200;
	private static final int WIDTH_VALUE = 150;
	
	private final ComponentMapper<DeterministicLSystem> lsystemMapper;
	private final ComponentMapper<TurtleProcessor> turtleProcessorMapper;
	
	private final TextFieldFactory textFieldFactory;
	private ProductionsEditor productionsEditor;
	private CommandsEditor commandsEditor;
	
	public LSystemEditorHud(Skin skin, Stage ui, World world)
	{
		this.skin = skin;
		
		reflex = new Reflex();
		reflex.addParser(new Vector2Writer());
		reflex.addParser(new ColorWriter());
		
		errorColor = new Color(1f, 0.5f, 0.5f, 1f);
		
		initUi(ui, skin);
		ui.addActor(table);
		
		lsystemMapper = world.getMapper(DeterministicLSystem.class);
		turtleProcessorMapper = world.getMapper(TurtleProcessor.class);
		
		textFieldFactory = new TextFieldFactory(table, skin, reflex);
		
	}
	
	public void addParser(FieldTypeWriter fieldTypeWriter)
	{
		reflex.addParser(fieldTypeWriter);
	}
	
	private void initUi(Stage ui, Skin skin)
	{
		table = UiUtil.createTable(skin);
		
		productionsEditor = new ProductionsEditor(skin);
		commandsEditor = new CommandsEditor(skin);
		
		table.setVisible(false);
	}
	
	public void setEntity(Entity e)
	{
		if (e == entity)
			return;
		
		table.clear();
		this.entity = e;
		
		if (e == null)
			return;
		
		DeterministicLSystem ls = lsystemMapper.get(e);
		table.add(new Label("L-SYSTEM", skin)).expandX().align(Align.left).maxWidth(300);
		table.row();
		insertField(ls, "iteration");
		insertField(ls, "axiom");
		table.add(productionsEditor.insertProductions(ls.productions)).colspan(2).expandX().fillX();
		
		table.row();
		
		TurtleProcessor processor = turtleProcessorMapper.get(e);
		table.add(new Label("TURTLE", skin)).expandX().align(Align.left);
		table.row();
		insertField(processor, "turnAmount");
		table.add(commandsEditor.insertCommands(processor.commands)).colspan(2).fillX().expandX();
		
		packTable();
	}

	private void insertField(Component component, String label)
	{
		textFieldFactory.insertField(label,
			new FieldInputListener(component, label),
			new FieldFocusListener(component, label));
	}
	
	public void enableDebug()
	{
		table.debug();
		productionsEditor.debug();
		commandsEditor.debug();
	}

	private void packTable()
	{
		Stage stage = table.getStage();
		if (stage == null)
			return;
		
		table.setWidth(PADDING + WIDTH_LABEL + WIDTH_VALUE + PADDING + 35);
		table.setHeight(table.getPrefHeight());
		System.out.println("pref height: " + table.getPrefHeight());
		
		float y = stage.getHeight() - table.getHeight();
		
		table.setPosition(stage.getWidth(), y);
	}
	
	
	public void toggle()
	{
		table.clearActions();
		float duration = 0.35f;
		float width = table.getStage().getWidth();
		
		if (table.isVisible())
			table.addAction(sequence(
				parallel(
					moveTo(width, table.getY(), duration, Interpolation.pow2),
					fadeOut(duration, Interpolation.pow2)), visible(false)));
		else
			table.addAction(sequence(fadeOut(0), visible(true), moveTo(width, table.getY()), 
				parallel(
					moveTo((width - table.getWidth()), table.getY(), duration, Interpolation.pow2), 
					fadeIn(duration, Interpolation.pow2))));
	}
	
	public boolean isVisible()
	{
		return table.isVisible();
	}

	public void setVisible(boolean visible)
	{
		if (table.isVisible() != visible)
			toggle();
	}
	
	public void updateEntity()
	{
		if (entity == null)
			return;
		
		productionsEditor.update(lsystemMapper.get(entity));
		commandsEditor.update(turtleProcessorMapper.get(entity));
		lsystemMapper.get(entity).requestUpdate = true;
		entity.changedInWorld();
	}

	@AllArgsConstructor
	class FieldInputListener extends InputListener
	{
		@Getter private final Object instance;
		private final String field;

		@Override
		public boolean keyDown(InputEvent event, int keycode)
		{
			if (keycode == Keys.ESCAPE || keycode == Keys.ENTER)
			{
				TextField text = (TextField)event.getListenerActor();
				
				boolean success = reflex.on(instance, field).set(text.getText());
				text.getStyle().fontColor = (success ? Color.WHITE : errorColor);
				if (success)
					text.setText(reflex.on(instance, field).getAsString());
				
				updateEntity();
			}
			if (keycode == Keys.ESCAPE)
			{
				Stage stage = event.getListenerActor().getStage();
				stage.setKeyboardFocus(event.getListenerActor().getParent());
				event.cancel();
				
				TextField text = (TextField)event.getListenerActor();
				text.setText(reflex.on(instance, field).getAsString());
				lsystemMapper.get(entity).requestUpdate = true;
			}
			
			return event.isCancelled();
		}
	}
	
	
	@AllArgsConstructor
	final class FieldFocusListener extends FocusListener
	{
		private final Object instance;
		private final String field;
		
		@Override
		public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
		{
			TextField text = (TextField)event.getListenerActor();
			text.setText(reflex.on(instance, field).getAsString());

			boolean success = reflex.on(instance, field).set(text.getText());
			text.getStyle().fontColor = (success ? Color.WHITE : errorColor);
			updateEntity();
		}
	}
}
