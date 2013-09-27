package net.onedaybeard.recursiveten.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public final class UiActions
{
	public static float hideShowDuration = 0.35f;
	
	private UiActions() {}
	
	public static SequenceAction showEastTable(Table table)
	{
		return sequence(
			parallel(
				moveTo(table.getStage().getWidth(), table.getY(), hideShowDuration, Interpolation.pow2),
				fadeOut(hideShowDuration, Interpolation.pow2)), visible(false));
	}
	
	public static SequenceAction hideEastTable(Table table)
	{
		return sequence(fadeOut(0), visible(true), moveTo(table.getStage().getWidth(), table.getY()), 
			parallel(
				moveTo(((table.getStage().getWidth()) - table.getWidth()), table.getY(), hideShowDuration, Interpolation.pow2), 
				fadeIn(hideShowDuration, Interpolation.pow2)));
	}
}
