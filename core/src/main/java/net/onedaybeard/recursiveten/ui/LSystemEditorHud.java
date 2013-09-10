package net.onedaybeard.recursiveten.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.onedaybeard.dominatrix.experimental.ui.BackgroundTexture;
import net.onedaybeard.dominatrix.reflect.ColorWriter;
import net.onedaybeard.dominatrix.reflect.FieldTypeWriter;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.dominatrix.reflect.Vector2Writer;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.MaxTextureDimension;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.TurtleProcessor.CommandBinding;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener.FocusEvent;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Cell;


public final class LSystemEditorHud
{
	private final Skin skin;
	@Getter private Entity entity;
	private Table table;
	private Table productionsTable;
	private Table commandsTable;
	
	private final Reflex reflex;
	
	private final Color errorColor;
	
	private static final int PADDING = 5;
	private static final int WIDTH_TYPE = 100;
	private static final int WIDTH_LABEL = 200;
	private static final int WIDTH_VALUE = 250;
	
	private final ComponentMapper<MaxTextureDimension> maxTextureDimension;
	private final ComponentMapper<DeterministicLSystem> lsystemMapper;
	private final ComponentMapper<TurtleProcessor> tutrteProcessorMapper;
	
	private final TextFieldFactory textFieldFactory;
	
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
		maxTextureDimension = world.getMapper(MaxTextureDimension.class);
		tutrteProcessorMapper = world.getMapper(TurtleProcessor.class);
		
		textFieldFactory = new TextFieldFactory(table, productionsTable, commandsTable, skin, reflex);
		
	}
	
	public void addParser(FieldTypeWriter fieldTypeWriter)
	{
		reflex.addParser(fieldTypeWriter);
	}
	
	private void initUi(Stage ui, Skin skin)
	{
		table = new Table();
		table.setBackground(BackgroundTexture.getDrawable());
		table.defaults().pad(PADDING);
		table.align(Align.top | Align.left);
		
		productionsTable = new Table();
		productionsTable.setBackground(BackgroundTexture.getDrawable());
		productionsTable.defaults().pad(PADDING);
		productionsTable.align(Align.top | Align.left);
		
		commandsTable = new Table();
		commandsTable.setBackground(BackgroundTexture.getDrawable());
		commandsTable.defaults().pad(PADDING);
		commandsTable.align(Align.top | Align.left);
		
		table.setVisible(false);
	}
	
	public void setEntity(Entity e)
	{
		if (e == entity)
			return;
		
		table.clear();
		productionsTable.clear();
		commandsTable.clear();
//		tree.clearChildren();
		this.entity = e;
		
		if (e == null)
			return;
		
		DeterministicLSystem ls = lsystemMapper.get(e);
		table.add(new Label("L-SYSTEM", skin)).expandX().align(Align.left).maxWidth(300);
		table.row();
//		table.add(new Label("axiom:", skin)).align(Align.right);
		insertField(ls, "iteration");
		insertField(ls, "axiom");
		textFieldFactory.insertProductions(ls);
		
		table.row();
		
		TurtleProcessor processor = tutrteProcessorMapper.get(e);
		table.add(new Label("TURTLE", skin)).expandX().align(Align.left);
		table.row();
		insertField(processor, "turnAmount");
		textFieldFactory.insertCommands(processor);
		
		
//		addHeader("L-SYSTEM", ls);
//		Node lsystem = createHeader("L-SYSTEM", ls);
//		createSubHeader(lsystem, "axiom");
//		createSubHeader(lsystem, "iterations");
//		Node productions = createSubHeader(lsystem, "productions");
//		for (String production : ls.productions)
//		{
//			createSubHeader(productions, production);
//		}
		
//		Node turtle = createHeader("TURTLE", processor);
//		createSubHeader(turtle, "turnAmount");
//		Node commands = createSubHeader(turtle, "commands");
//		for (CommandBinding command : processor.commands)
//		{
//			createSubHeader(commands, command.toString());
//		}
		
		packTable();
//		tree.expandAll();
	}

	private void insertField(Component component, String label)
	{
		textFieldFactory.insertField(label,
			new FieldInputListener(component, label),
			new FieldFocusListener(component, label));
	}
	
	public void drawDebug()
	{
		table.debug();
		productionsTable.debug();
		commandsTable.debug();
	}

	private Cell addField(final Object instance, final Field field)
	{
		String value = reflex.on(instance, field).getAsString();
		if (reflex.isEditable(field))
		{
			final TextFieldStyle style = new TextFieldStyle();
			style.font = skin.getFont("default-font");
			style.fontColor = Color.WHITE;
			style.cursor = skin.getDrawable("cursor");
			
			TextField textField = new TextField(value, style);
			textField.addCaptureListener(new InputListener()
			{
				@Override
				public boolean keyDown(InputEvent event, int keycode)
				{
					if (keycode == Keys.ESCAPE || keycode == Keys.ENTER)
					{
						TextField text = (TextField)event.getListenerActor();
						
						boolean success = reflex.on(instance, field).set(text.getText());
						style.fontColor = (success ? Color.WHITE : errorColor);
						if (success)
							text.setText(reflex.on(instance, field).getAsString());
						
						entity.changedInWorld();
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
			});
			textField.addListener(new FocusListener()
			{
				@Override
				public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
				{
					TextField text = (TextField)event.getListenerActor();
					text.setText(reflex.on(instance, field).getAsString());
					
					boolean success = reflex.on(instance, field).set(text.getText());
					style.fontColor = (success ? Color.WHITE : errorColor);
				}
			});
			table.add(textField);
		}
		
		return null;
	}
	
	private void addHeader(String string, DeterministicLSystem ls)
	{
		// TODO Auto-generated method stub
		
	}
	
	private void addField(String label, String field)
	{
//		new Field
	}

	private void packTable()
	{
		Stage stage = table.getStage();
		if (stage == null)
			return;
		
		table.setWidth(PADDING + WIDTH_TYPE + WIDTH_LABEL + WIDTH_VALUE + PADDING + 35);
		table.setHeight(stage.getHeight() - 200 - PADDING * 2);
		
		float x = (stage.getWidth() - table.getWidth()) / 2;
		float y = stage.getHeight() - 5 - table.getHeight();
		
		table.setPosition(x, y);
	}
	
	private Array<Node> getFieldNodes(Component component)
	{
		Array<Node> nodes = new Array<Tree.Node>();
		Field[] fields = component.getClass().getDeclaredFields();
		for (int i = 0, s = fields.length; s > i; i++)
		{
			Label label = new Label(fields[i].getName(), skin);
			label.getColor().mul(new Color(1f, 0.5f, 0.5f, 1f));
			Node fieldNode = new Node(getFieldActor(component, fields[i]));
			fieldNode.setObject(fields[i]);
			
			nodes.add(fieldNode);
		}
		
		return nodes;
	}
	
	private Table getFieldActor(final Component instance, final Field field)
	{
		Table t = new Table(skin);
		Color color = reflex.isEditable(field)
			? Color.WHITE
			: errorColor;
		
		Label typeLabel = new Label(field.getType().getSimpleName(), skin);
		typeLabel.setColor(color);
		t.add(typeLabel).minWidth(WIDTH_TYPE);
		
		Label fieldLabel = new Label(field.getName(), skin);
		fieldLabel.setColor(color);
		t.add(fieldLabel).minWidth(WIDTH_LABEL);
		
		String value = reflex.on(instance, field).getAsString();
		if (reflex.isEditable(field))
		{
			final TextFieldStyle style = new TextFieldStyle();
			style.font = skin.getFont("default-font");
			style.fontColor = Color.WHITE;
			style.cursor = skin.getDrawable("cursor");
			TextField textField = new TextField(value, style);
			textField.addCaptureListener(new InputListener()
			{
				@Override
				public boolean keyDown(InputEvent event, int keycode)
				{
					if (keycode == Keys.ESCAPE || keycode == Keys.ENTER)
					{
						TextField text = (TextField)event.getListenerActor();
						
						boolean success = reflex.on(instance, field).set(text.getText());
						style.fontColor = (success ? Color.WHITE : errorColor);
						if (success)
							text.setText(reflex.on(instance, field).getAsString());
						
						entity.changedInWorld();
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
			});
			textField.addListener(new FocusListener()
			{
				@Override
				public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
				{
					TextField text = (TextField)event.getListenerActor();
					text.setText(reflex.on(instance, field).getAsString());
					
					boolean success = reflex.on(instance, field).set(text.getText());
					style.fontColor = (success ? Color.WHITE : errorColor);
				}
			});
			t.add(textField);
		}
		else
		{
			if (value.length() > 30)
				value = value.substring(0, 28) + "...";
			
			Label valueLabel = new Label(value, skin);
			valueLabel.setColor(color);
			t.add(valueLabel).minWidth(WIDTH_VALUE);
		}
		
		return t;
	}
	
	public void toggle()
	{
		table.clearActions();
		if (table.isVisible())
			table.addAction(sequence(fadeOut(0.35f, Interpolation.pow2), visible(false)));
		else
			table.addAction(sequence(visible(true), fadeIn(0.35f, Interpolation.pow2)));
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
				
				entity.changedInWorld();
				lsystemMapper.get(entity).requestUpdate = true;
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
			lsystemMapper.get(entity).requestUpdate = true;
		}
	}
}
