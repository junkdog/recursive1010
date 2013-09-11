package net.onedaybeard.recursiveten.ui;

import net.onedaybeard.dominatrix.experimental.ui.BackgroundTexture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public final class UiUtil
{
	private static final int PADDING = 5;
	public static final int BUTTON_WIDTH = 40;
	
	private UiUtil() {}
	
	public static Table createTable(Skin skin)
	{
		Table table = new Table();
		table.setBackground(BackgroundTexture.getDrawable());
		table.defaults().pad(PADDING);
		table.align(Align.top | Align.left);
		
		return table;
	}
	
	public static TextFieldStyle getTextFieldStyle(Skin skin)
	{
		TextFieldStyle style = new TextFieldStyle();
		style.font = skin.getFont("default-font");
		style.fontColor = Color.WHITE;
		style.cursor = skin.getDrawable("cursor");
		return style;
	}
}
