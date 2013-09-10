package net.onedaybeard.recursiveten.ui;

import lombok.AllArgsConstructor;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.dominatrix.tuple.Tuple;
import net.onedaybeard.dominatrix.tuple.Tuple2;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.ui.LSystemEditorHud.FieldFocusListener;
import net.onedaybeard.recursiveten.ui.LSystemEditorHud.FieldInputListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

@AllArgsConstructor
public class TextFieldFactory
{
	private final Table table;
	private final Table productionsTable;
	private final Table commandsTable;
	private final Skin skin;
	private final Reflex reflex;
	
	public void insertField(String field, FieldInputListener fieldInputListener, FieldFocusListener fieldFocusListener)
	{
		table.add(new Label(field, skin)).align(Align.left);
		TextField textField = addTextField(fieldInputListener, fieldFocusListener);
		textField.setText(reflex.on(fieldInputListener.getInstance(), field).getAsString());
		table.row();
	}
	
	public void insertProductions(DeterministicLSystem ls)
	{
		productionsTable.add(new Label("productions", skin)).align(Align.left);
		productionsTable.row();
		for (int i = 0; ls.productions.length > i; i++)
		{
			Tuple2<String,String> production = getProductionTuple(ls.productions[i]);
			productionsTable.add(new Label(production.a, skin)).expandX().align(Align.right);
			
			TextField textField = new TextField(production.b, getTextFieldStyle());
			productionsTable.add(textField).expandX().align(Align.right);
			
			productionsTable.row();
		}
		table.add(productionsTable).colspan(2).expandX().fillX();
	}
	
	private Tuple2<String,String> getProductionTuple(String production)
	{
		return Tuple.create(
			production.substring(0, 2),
			production.substring(2));
	}
	
	public void insertCommands(TurtleProcessor tp)
	{
		commandsTable.add(new Label("commands", skin)).align(Align.left);
		commandsTable.row();
		for (int i = 0; tp.commands.length > i; i++)
		{
			commandsTable.add(new Label(String.valueOf(tp.commands[i].key), skin)).align(Align.right);
			
			TextField textField = new TextField(tp.commands[i].command.toString(), getTextFieldStyle());
			commandsTable.add(textField).expandX().align(Align.right);
			
			commandsTable.row();
		}
		table.add(commandsTable).colspan(2).expandX().fillX();
	}
	
	private TextField addTextField(FieldInputListener fieldInputListener,
		FieldFocusListener fieldFocusListener)
	{
		TextField textField = new TextField("", getTextFieldStyle());
		table.add(textField).expandX().align(Align.right);
		textField.addCaptureListener(fieldInputListener);
		textField.addListener(fieldFocusListener);
		return textField;
	}
	
	private TextFieldStyle getTextFieldStyle()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.font = skin.getFont("default-font");
		style.fontColor = Color.WHITE;
		style.cursor = skin.getDrawable("cursor");
		return style;
	}
}
