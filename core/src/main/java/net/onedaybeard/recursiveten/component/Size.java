package net.onedaybeard.recursiveten.component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.artemis.Component;

@ToString @NoArgsConstructor @AllArgsConstructor
public final class Size extends Component
{
	public float width, height;
}
