package net.onedaybeard.recursiveten.ui.lsystem;

import static net.onedaybeard.recursiveten.ui.UiUtil.BUTTON_WIDTH;
import static net.onedaybeard.recursiveten.ui.UiUtil.getTextFieldStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.onedaybeard.dominatrix.tuple.Tuple;
import net.onedaybeard.dominatrix.tuple.Tuple2;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.ui.UiUtil;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;

public class ProductionsEditor
{
	private final Table table;
	private final Skin skin;
	private DeterministicLSystem ls;
	
	ProductionsEditor(Skin skin)
	{
		this.skin = skin;
		table = UiUtil.createTable(skin);
	}
	
	Table insertProductions(DeterministicLSystem ls)
	{
		table.clear();
		table.add(new Label("PRODUCTIONS", skin)).align(Align.left).colspan(3);
		
		Button addButton = new TextButton("add", skin);
		addButton.addCaptureListener(new ClickListener(Buttons.LEFT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				addRow(Tuple.create("", ""));
			}
		});
		table.add(addButton).width(BUTTON_WIDTH).align(Align.right);
		
		table.row();
		for (int i = 0; ls.productions.length > i; i++)
			addRow(productionTuple(ls.productions[i]));
		
		return table;
	}

	private void addRow(Tuple2<String, String> production)
	{
		TextField prerequisiteField = new TextField(production.a, getTextFieldStyle(skin));
		prerequisiteField.setRightAligned(true);
		prerequisiteField.setTextFieldListener(new LSystemAlphabetListener());
		table.add(prerequisiteField);
		
		table.add(new Label("=", skin));
		
		TextField ruleField = new TextField(production.b, getTextFieldStyle(skin));
		table.add(ruleField).expandX().fillX();
		
		
		final Button removeButton = new TextButton("del", skin);
		removeButton.addCaptureListener(new ClickListener(Buttons.LEFT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				ls.productions = getProductions();
				int index = table.getCells().indexOf(table.getCell(removeButton));
				index -= 2 - 3; // global offset plus local
				List<String> productions = new ArrayList<String>(Arrays.asList(getProductions()));
				assert index % 4 == 0;
				productions.remove(index / 4 - 1);
				ls.productions = productions.toArray(new String[0]);
				insertProductions(ls);
			}
		});
		table.add(removeButton).width(BUTTON_WIDTH).align(Align.right);
		
		table.row();
	}
	
	void update(DeterministicLSystem ls)
	{
		this.ls = ls;
		
		@SuppressWarnings("rawtypes")
		List<Cell> cells = table.getCells();
		System.out.println(Arrays.toString(ls.productions));
		for (int i = 0, s = cells.size(); s > i; i++)
		{
			System.out.println(i + ": " + cells.get(i).getWidget());
		}
	}
	
	String[] getProductions()
	{
		@SuppressWarnings("rawtypes")
		List<Cell> cells = table.getCells();
		
		int cellOffset = 2;
		String[] productions = new String[(cells.size() - cellOffset) / 4];
		for (int i = 0; productions.length > i; i++)
		{
			int index = cellOffset + (i * 4);
			TextField ruleField = (TextField)cells.get(index).getWidget();
			TextField productionField = (TextField)cells.get(index + 2).getWidget();
			productions[i] = ruleField.getText().trim() + "=" + productionField.getText().trim();
		}
		return productions;
	}
	
	private Tuple2<String,String> productionTuple(String production)
	{
		production = production.trim();
		
		if (production.equals("="))
			return Tuple.create("", "");
		
		assert production.charAt(1) == '=';
		return Tuple.create(
			production.substring(0, 1),
			production.substring(2));
	}
	
	void debug()
	{
		table.debug();
	}
}
