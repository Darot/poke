package com.game.pokerpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.game.pokerpg.entities.Player;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class Play implements Screen, InputProcessor{
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private Player player;
	private Player player2;
	
	//UI Elements
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private Table table;
	private TextButton buttonExit, buttonOptions, buttonChat, buttonTeam, buttonPokedex, buttonFriends;
	
	private InputMultiplexer inputMultiplexer;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0 , 0 , 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
		camera.update();
		
		renderer.setView(camera);
		
		
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("ground"));
		player.draw(renderer.getSpriteBatch()); // render the Player
		player2.draw(renderer.getSpriteBatch()); // render the Player
		renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("foreground"));
		renderer.getSpriteBatch().end();
		
		table.drawDebug(stage);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 3;
		camera.viewportHeight = height / 3;
		camera.update();	
		}

	@Override
	public void show() {
		//Setup UserInterface
		stage = new Stage();
		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);
		
		//Buttons
		buttonExit = new TextButton("Exit", skin);
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		
		buttonOptions = new TextButton("Options", skin);
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("ButtonClicked!");
			}
		});
		
		buttonChat = new TextButton("Chat", skin);
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("ButtonClicked!");
			}
		});
		
		buttonTeam = new TextButton("Team", skin);
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("ButtonClicked!");
			}
		});
		
		buttonPokedex = new TextButton("Pokedex", skin);
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("ButtonClicked!");
			}
		});
		
		buttonFriends = new TextButton("Friends", skin);
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("ButtonClicked!");
			}
		});
		
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), buttonExit.getHeight());
		
		table.add(buttonChat);
		table.add(buttonPokedex);
		table.add(buttonTeam);
		table.add(buttonFriends);
		table.add(buttonOptions);
		table.add(buttonExit);
		table.debug();
		stage.addActor(table);
		
		//load The map
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("maps/adminmap.tmx");
		
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();
		
		//SetupPlayers
		player = new Player(new Sprite(new Texture("img/left1.png")), (TiledMapTileLayer) map.getLayers().get(0) );
		player2 = new Player(new Sprite(new Texture("img/left1.png")), (TiledMapTileLayer) map.getLayers().get(0) );
		player.setPosition(17 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getTileHeight()) * player.getCollisionLayer().getTileHeight() );
		player2.setPosition(18 * player2.getCollisionLayer().getTileWidth(), (player2.getCollisionLayer().getTileHeight()) * player2.getCollisionLayer().getTileHeight() );
		
		//SetInputProcessor to this Screen
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage); //add stage an input processor to access UI elements
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		//create a receiver socket
		new Thread(new Runnable(){
			@Override
			public void run(){
				//factory.setHost("localhost")
			}
		}).start();
	}
	

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		stage.dispose();
		player.dispose();
	}
	
	//Input Processor methods:
	
	@Override
	public boolean keyDown(int keycode) {
		//Vector2 vector = new Vector2();
		switch(keycode){
		case Keys.W:
			player.setVelocityY((int)player.getSpeed());
			break;
		case Keys.S:
			player.setVelocityY(- (int)player.getSpeed());
			break;
		case Keys.A:
			player.setVelocityX(- (int)player.getSpeed());
			break;
		case Keys.D:
			player.setVelocityX((int)player.getSpeed());
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		//Vector2 vector = new Vector2(0, 0);
		switch(keycode){
		case Keys.A:
		case Keys.D:
			player.setVelocityX(0);
			break;
		case Keys.W:
		case Keys.S:
			player.setVelocityY(0);
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
