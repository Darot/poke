package com.game.pokerpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.pokerpg.screens.Play;
import com.game.pokerpg.screens.Splash;

public class PokeRPG extends Game {

	public static final String TITLE = "PokeRPG", VERSION = "0.0.0.0.reallyEarly";
	
	@Override
	public void create () {
		setScreen(new Splash());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose (){
		super.dispose();
	}
	
	@Override
	public void resize (int width, int height){
		super.resize(width, height);
	}
	
	@Override
	public void pause(){
		super.pause();
	}
	
	@Override
	public void resume(){
		super.resume();
	}
	
}
