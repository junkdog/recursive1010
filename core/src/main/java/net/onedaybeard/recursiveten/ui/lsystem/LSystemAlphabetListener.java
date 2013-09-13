package net.onedaybeard.recursiveten.ui.lsystem;

import java.util.regex.Pattern;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;

final class LSystemAlphabetListener implements TextFieldListener
{
	private static final Pattern valid = Pattern.compile("[A-Za-z+-_]"); 
	@Override
	public void keyTyped(TextField textField, char key)
	{
		if (valid.matcher(Character.toString(key)).matches())
			textField.setText(Character.toString(key));
	}
}