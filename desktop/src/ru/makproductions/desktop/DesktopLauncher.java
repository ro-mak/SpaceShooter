package ru.makproductions.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.makproductions.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//float aspect = 9f/16f;
		float aspect = 3f/4f;
		config.height = 900;
		config.width = (int)(config.height * aspect);
		new LwjglApplication(new StarGame(), config);
		
	}

}
