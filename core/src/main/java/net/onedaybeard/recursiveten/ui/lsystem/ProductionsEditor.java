package net.onedaybeard.recursiveten.ui.lsystem;

import static net.onedaybeard.recursiveten.ui.UiUtil.BUTTON_WIDTH;
import static net.onedaybeard.recursiveten.ui.UiUtil.getTextFieldStyle;

import java.util.Arrays;
import java.util.List;

import net.onedaybeard.dominatrix.tuple.Tuple;
import net.onedaybeard.dominatrix.tuple.Tuple2;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.ui.UiUtil;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	
	ProductionsEditor(Skin skin)
	{
		this.skin = skin;
		table = UiUtil.createTable(skin);
	}
	
	Table insertProductions(DeterministicLSystem ls)
	{
		table.clear();
		table.add(new Label("PRODUCTIONS", skin)).align(Align.left).colspan(2);
		
		Button addButton = new TextButton("add", skin);
		table.add(addButton).width(BUTTON_WIDTH).align(Align.right);
		
		table.row();
		for (int i = 0; ls.productions.length > i; i++)
		{
			Tuple2<String,String> production = productionTuple(ls.productions[i]);
			table.add(new Label(production.a, skin));
			
			TextField textField = new TextField(production.b, getTextFieldStyle(skin));
			table.add(textField).expandX().fillX();
			
			Button removeButton = new TextButton("del", skin);
			removeButton.addCaptureListener(new ClickListener(Buttons.LEFT)
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					System.out.println("clicked");
				}
			});
			table.add(removeButton).width(BUTTON_WIDTH).align(Align.right);
			
			table.row();
		}
		return table;
	}
	
	void update(DeterministicLSystem ls)
	{
		@SuppressWarnings("rawtypes")
		List<Cell> cells = table.getCells();
		System.out.println(Arrays.toString(ls.productions));
		for (int i = 0, s = cells.size(); s > i; i++)
		{
			System.out.println(cells.get(i).getWidget());
		}
	}
	
	private Tuple2<String,String> productionTuple(String production)
	{
		return Tuple.create(
			production.substring(0, 2),
			production.substring(2));
	}
	
	void debug()
	{
		table.debug();
	}
}
