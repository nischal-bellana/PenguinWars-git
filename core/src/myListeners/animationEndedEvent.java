package myListeners;

import com.badlogic.gdx.scenes.scene2d.Event;

public class animationEndedEvent extends Event{
	private String message;
	public animationEndedEvent(String message) {
		this.message = message;
	}
	public String getmessage() {
		return message;
	}
}
