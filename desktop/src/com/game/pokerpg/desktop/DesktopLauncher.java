package com.game.pokerpg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.pokerpg.PokeRPG;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PokeRPG(), config);
		config.width = 1280;
		config.height = 720;
		config.useGL30 = true;
		config.title = "pokeRPG";
	}
}
