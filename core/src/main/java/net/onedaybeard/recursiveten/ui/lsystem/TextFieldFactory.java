package net.onedaybeard.recursiveten.ui.lsystem;

import static net.onedaybeard.recursiveten.ui.UiUtil.BUTTON_WIDTH;
import static net.onedaybeard.recursiveten.ui.UiUtil.getTextFieldStyle;
import lombok.AllArgsConstructor;
import net.onedaybeard.dominatrix.reflect.Reflex;
import net.onedaybeard.recursiveten.ui.lsystem.LSystemEditorHud.FieldFocusListener;
import net.onedaybeard.recursiveten.ui.lsystem.LSystemEditorHud.FieldInputListener;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

@AllArgsConstructor
public class TextFieldFactory
{
	private final Table table;
	private final Skin skin;
	private final Reflex reflex;
	
	public void insertField(String field, FieldInputListener fieldInputListener, FieldFocusListener fieldFocusListener)
	{
		table.add(new Label(field, skin)).align(Align.left);
		TextField textField = addTextField(fieldInputListener, fieldFocusListener);
		textField.setText(reflex.on(fieldInputListener.getInstance(), field).getAsString());
		table.row();
	}
	
	private TextField addTextField(FieldInputListener fieldInputListener,
		FieldFocusListener fieldFocusListener)
	{
		TextField textField = new TextField("", getTextFieldStyle(skin));
		textField.setRightAligned(true);
		table.add(textField).expandX().fillX().align(Align.right).padRight(BUTTON_WIDTH + 30);
		textField.addCaptureListener(fieldInputListener);
		textField.addListener(fieldFocusListener);
		return textField;
	}
}
