package net.onedaybeard.recursiveten.component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

@ToString @NoArgsConstructor @AllArgsConstructor
public final class BackgroundColor extends Component
{
	public Color value;
}
