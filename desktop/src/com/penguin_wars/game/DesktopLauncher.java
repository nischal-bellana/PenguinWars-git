package com.penguin_wars.game;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.penguin_wars.game.penguin_wars;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) throws IOException{
//		Texture Packer code
//		Settings settings = new Settings();
//		settings.maxWidth = 4096;
//		settings.maxHeight = 4096;
//		TexturePacker.process(settings,"game-imgs", "game-imgs-packed", "pack");
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("penguin wars");
		config.setWindowedMode(1200, 1000);
		config.useVsync(true);
		new Lwjgl3Application(new penguin_wars(), config);
	}
}
