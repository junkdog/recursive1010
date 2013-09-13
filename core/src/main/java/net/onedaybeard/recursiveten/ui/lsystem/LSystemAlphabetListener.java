package net.onedaybeard.recursiveten.ui.lsystem;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;

final class LSystemAlphabetListener implements TextFieldListener
{
	@Override
	public void keyTyped(TextField textField, char key)
	{
		textField.setText(Character.toString(key));
	}
}