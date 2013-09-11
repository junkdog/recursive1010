package net.onedaybeard.recursiveten.ui.lsystem;

import static net.onedaybeard.recursiveten.ui.UiUtil.BUTTON_WIDTH;
import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.ui.UiUtil;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class CommandsEditor
{
	private final Skin skin;
	private final Table table;

	CommandsEditor(Skin skin)
	{
		this.skin = skin;
		table = UiUtil.createTable(skin);
	}
	
	public Table insertCommands(TurtleProcessor tp)
	{
		table.clear();
		table.add(new Label("COMMANDS", skin)).align(Align.left).colspan(2);
		
		Button addButton = new TextButton("add", skin);
		table.add(addButton).width(BUTTON_WIDTH).align(Align.right);
		
		table.row();
		for (int i = 0; tp.commands.length > i; i++)
		{
			table.add(new Label(String.valueOf(tp.commands[i].key), skin));
			
			SelectBox commands = new SelectBox(TurtleCommand.values(), skin);
			commands.setSelection(tp.commands[i].command.ordinal());
			table.add(commands).expandX().fillX();
			
			Button removeButton = new TextButton("del", skin);
			table.add(removeButton).width(BUTTON_WIDTH).align(Align.right);
			
			table.row();
		}
		return table;
	}
	
	public void debug()
	{
		table.debug();
	}
}
