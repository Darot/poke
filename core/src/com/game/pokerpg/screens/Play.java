package com.game.pokerpg.screens;

import java.util.HashMap;
import java.util.Map;

import com.game.pokerpg.entities.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.game.pokerpg.network.PublisherSocket;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class Play implements Screen, InputProcessor{
	
	private String mapName = "adminmap";
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private Player player;
	private Vector2 playerPosition;
	//Map for the other players on the map
	Map<String, OtherPlayer> players = new HashMap<String, OtherPlayer>();
	
	//UI Elements
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private Table table;
	private TextButton buttonExit, buttonOptions, buttonChat, buttonTeam, buttonPokedex, buttonFriends;
	
	//SocketObjects RECV
	private static final String EXCHANGE_NAME = "pokeCom2";
	private ConnectionFactory factory = new ConnectionFactory();
	private Connection connection;
	private Channel channel;
	private String serverIP = "localhost";
	
	//Publisher Socket
	PublisherSocket pubSocket = new PublisherSocket();
	
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
		playerPosition = new Vector2(17 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getTileHeight()) * player.getCollisionLayer().getTileHeight());
		player.setPosition(playerPosition.x, playerPosition.y );
		System.out.println(playerPosition);
		
		
		//SetInputProcessor to this Screen
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage); //add stage an input processor to access UI elements
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		//create a receiver socket
		new Thread(new Runnable(){
			@Override
			public void run(){
				factory.setHost(serverIP);
				String mapTopic = "map." + mapName;
				
				try{
					//create the socket
					connection = factory.newConnection();
					channel = connection.createChannel();
					channel.exchangeDeclare(EXCHANGE_NAME, "topic");
					String queueName = channel.queueDeclare().getQueue();
					
					//listen to the folowing topics
					channel.queueBind(queueName, EXCHANGE_NAME, "player.movement");
					channel.queueBind(queueName, EXCHANGE_NAME, mapTopic);
					
					QueueingConsumer consumer = new QueueingConsumer(channel);
					channel.basicConsume(queueName, true, consumer);
					
					
					while(true){
						QueueingConsumer.Delivery delivery = consumer.nextDelivery();
						String message = new String(delivery.getBody());
						JSONArray msgArray = null;
						JSONObject msg = null;
						//System.out.println(message);
						
						//Check if single information or array of information
						if(message.contains("[")){
							msgArray = new JSONArray(message);
						}else {
							msg = new JSONObject(message);
						}
						
						String routingKey = delivery.getEnvelope().getRoutingKey();
						if(routingKey.contains("map")){
							routingKey = "map";
						}
						
						//Handle messages
						switch(routingKey){
						case("map"):
							updatePlayersOnMap(msgArray);
							break;
						case("player.movement"):
							//moveOtherPlayer(msg);
							break;
						}
					}
					
				}catch(Exception e){
					System.out.println(e.getStackTrace());
					System.out.println("creepy shit happened!");
				}			
			}
		}).start();
		
		pubSocket.createSocket();
		
		//Get all players on the map
		try {
			pubSocket.getPlayers(mapName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updatePlayersOnMap(JSONArray players){
		
	}
	
	public void updateOtherPlayer(JSONObject movement){
		
	}
	
	public void moveOtherPlayer(JSONObject movement) throws JSONException{
		if( !(movement.get("playerId").equals(player.getPlayerId())) ){
			
		}else {
			System.out.println("equals");
		}
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
	}
	
	//Input Processor methods:
	public void sendMovement() throws JSONException{
		JSONObject msg = new JSONObject();
		msg.put("velocityX", player.getVelocity().x);
		msg.put("velocityY", player.getVelocity().y);
		msg.put("playerId", player.getPlayerId());
		
		pubSocket.sendPlayerMovement(msg);
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		//Vector2 vector = new Vector2();
		switch(keycode){
		case Keys.W:
			player.setVelocityY((int)player.getSpeed());
			try {
				sendMovement();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case Keys.S:
			player.setVelocityY(- (int)player.getSpeed());
			try {
				sendMovement();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Keys.A:
			player.setVelocityX(- (int)player.getSpeed());
			try {
				sendMovement();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Keys.D:
			player.setVelocityX((int)player.getSpeed());
			try {
				sendMovement();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				sendMovement();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Keys.W:
		case Keys.S:
			player.setVelocityY(0);
			try {
				sendMovement();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
