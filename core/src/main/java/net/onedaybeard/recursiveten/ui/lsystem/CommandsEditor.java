package net.onedaybeard.recursiveten.ui.lsystem;

import static net.onedaybeard.recursiveten.ui.UiUtil.BUTTON_WIDTH;
import static net.onedaybeard.recursiveten.ui.UiUtil.getTextFieldStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.onedaybeard.recursiveten.component.TurtleProcessor;
import net.onedaybeard.recursiveten.component.TurtleProcessor.CommandBinding;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;
import net.onedaybeard.recursiveten.ui.UiUtil;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;

public class CommandsEditor
{
	private final Skin skin;
	private final Table table;
	
	CommandsEditor(Skin skin)
	{
		this.skin = skin;
		table = UiUtil.createTable(skin);
	}
	
	public Table insertCommands(CommandBinding[] commands)
	{
		table.clear();
		table.add(new Label("COMMANDS", skin)).align(Align.left).colspan(2);
		
		Button addButton = new TextButton("add", skin);
		addButton.addCaptureListener(new ClickListener(Buttons.LEFT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				addRow(new CommandBinding('_', TurtleCommand.NO_OPERATION));
				event.handle();
			}
		});
		table.add(addButton).width(BUTTON_WIDTH).align(Align.right);
		
		table.row();
		for (int i = 0; commands.length > i; i++)
			addRow(commands[i]);
	
		return table;
	}
	
	private void addRow(CommandBinding command)
	{
		TextField alphabetField = new TextField(Character.toString(command.key), getTextFieldStyle(skin));
		alphabetField.setRightAligned(true);
		alphabetField.setTextFieldListener(new LSystemAlphabetListener());
		table.add(alphabetField);
		
		SelectBox commands = new SelectBox(TurtleCommand.values(), skin);
		commands.setSelection(command.command.ordinal());
		table.add(commands).expandX().fillX();
		
		final Button removeButton = new TextButton("del", skin);
		removeButton.addCaptureListener(new ClickListener(Buttons.LEFT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				CommandBinding[] bindings = getCommandBindings();
				int index = table.getCells().indexOf(table.getCell(removeButton));
				index -= 2 - 2; // global offset plus local
				List<CommandBinding> commandList = new ArrayList<CommandBinding>(Arrays.asList(bindings));
				assert index % 3 == 0;
				commandList.remove(index / 3 - 1);
				bindings = commandList.toArray(new CommandBinding[0]);
				insertCommands(bindings);
			}
		});
		table.add(removeButton).width(BUTTON_WIDTH).align(Align.right);
		
		table.row();
	}
	
	void update(TurtleProcessor tp)
	{
		tp.commands = getCommandBindings();
	}
	
	private CommandBinding[] getCommandBindings()
	{
		@SuppressWarnings("rawtypes")
		List<Cell> cells = table.getCells();
		
		int cellOffset = 2;
		CommandBinding[] bindings = new CommandBinding[(cells.size() - cellOffset) / 3];
		for (int i = 0; bindings.length > i; i++)
		{
			int index = cellOffset + (i * 3);
			TextField ruleField = (TextField)cells.get(index).getWidget();
			SelectBox commandBox = (SelectBox)cells.get(index + 1).getWidget();
			
			bindings[i] = new CommandBinding(ruleField.getText().charAt(0), 
				TurtleCommand.values()[commandBox.getSelectionIndex()]);
		}
		
		return bindings;
	}
	
	public void debug()
	{
		table.debug();
	}
}
