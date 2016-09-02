package src.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import src.colorgame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Spectrum Shooter";
		config.width = 1366;
		config.height = 768;
		new LwjglApplication(new colorgame(), config);
	}
}
