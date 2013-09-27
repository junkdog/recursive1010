package net.onedaybeard.recursiveten.ui.shader;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.onedaybeard.dominatrix.reflect.ColorWriter;
import net.onedaybeard.dominatrix.reflect.FieldTypeWriter;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.dominatrix.reflect.Vector2Writer;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.Shader;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.event.CommandEvent.Type;
import net.onedaybeard.recursiveten.ui.FieldFocusListener;
import net.onedaybeard.recursiveten.ui.FieldInputListener;
import net.onedaybeard.recursiveten.ui.TextFieldFactory;
import net.onedaybeard.recursiveten.ui.UiActions;
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
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.bitfire.postprocessing.effects.EffectSettings;

public final class ShaderEditorHud
{
	private final Skin skin;
	@Getter
	private Entity entity;
	private Table table;

	private final Reflex reflex;

	private static final int PADDING = 5;
	private static final int WIDTH_LABEL = 200;
	private static final int WIDTH_VALUE = 150;

	private final ComponentMapper<Shader> shaderMapper;

	private final TextFieldFactory textFieldFactory;

	public ShaderEditorHud(Skin skin, Stage ui, World world)
	{
		this.skin = skin;

		reflex = new Reflex();
		reflex.addParser(new Vector2Writer());
		reflex.addParser(new ColorWriter());

		initUi(ui, skin);
		ui.addActor(table);

		textFieldFactory = new TextFieldFactory(table, skin, reflex);

		shaderMapper = world.getMapper(Shader.class);
	}

	public void addParser(FieldTypeWriter fieldTypeWriter)
	{
		reflex.addParser(fieldTypeWriter);
	}

	private void initUi(Stage ui, Skin skin)
	{
		table = UiUtil.createTable(skin);

		// productionsEditor = new ProductionsEditor(skin);
		// commandsEditor = new CommandsEditor(skin);
		//
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
		
		Shader shader = shaderMapper.get(e);
		EffectSettings settings = shader.settings;
		Field[] fields = settings.getClass().getFields();
//		insertField(component, label)
		for (Field f : fields)
		{
			insertField(e, settings, f.getName());
		}
		
//		DeterministicLSystem ls = lsystemMapper.get(e);
//		table.add(new Label("L-SYSTEM", skin)).expandX().align(Align.left).maxWidth(300);
//		table.row();
//		insertField(ls, "iteration");
//		insertField(ls, "axiom");
//		table.add(productionsEditor.insertProductions(ls.productions)).colspan(2).expandX().fillX();
		
//		table.row();
//		
//		TurtleProcessor processor = turtleProcessorMapper.get(e);
//		table.add(new Label("TURTLE", skin)).expandX().align(Align.left);
//		table.row();
//		insertField(processor, "turnAmount");
//		table.add(commandsEditor.insertCommands(processor.commands)).colspan(2).fillX().expandX();
		
		packTable();
	}

	private void insertField(Entity e, EffectSettings settings, String label)
	{
		textFieldFactory.insertField(label,
			new FieldInputListener(reflex, settings, label).eventOnChange(Type.ENTITY_CHANGED_IN_WORLD).entityId(e),
			new FieldFocusListener(reflex, settings, label).eventOnChange(Type.ENTITY_CHANGED_IN_WORLD).entityId(e));
	}

	public void enableDebug()
	{
		table.debug();
		// productionsEditor.debug();
		// commandsEditor.debug();
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

		if (table.isVisible())
			table.addAction(UiActions.showEastTable(table));
		else
			table.addAction(UiActions.hideEastTable(table));
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

		// productionsEditor.update(lsystemMapper.get(entity));
		// commandsEditor.update(turtleProcessorMapper.get(entity));
//		lsystemMapper.get(entity).requestUpdate = true;
		entity.changedInWorld();
	}
}
