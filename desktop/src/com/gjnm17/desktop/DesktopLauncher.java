package com.gjnm17.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gjnm17.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = Main.WIDTH/2;
		config.height = Main.HEIGHT/2;
		
		new LwjglApplication(new Main(), config);
	}
}
