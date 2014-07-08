package com.game.pokerpg.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PublisherSocket {
	private static final String EXCHANGE_NAME = "pokeCom";
	
	private ConnectionFactory factory = new ConnectionFactory();
	private Channel channel;
	private Connection connection;
	
	public void createSocket(){
		factory.setHost("localhost");
		try{
			//Socket setup
			connection = factory.newConnection();
			channel = connection.createChannel();
			
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			
		}catch(Exception e){
			System.out.println("Something went horribly wrong!");
		}
	}
	
	/*
	 * Sending information to the server
	 */
	
	public void sendPlayerMovement(JSONObject msg){
		/*
		 * This method sends a JSONObject with the current movement information
		 * to the server:
		 * OBJECT:
		 * {
		 * "velocityX":0.0,
		 * "velocityY":120.0,
		 * "playerId":"admin123"
		 * }
		 */
		try{
			channel.basicPublish(EXCHANGE_NAME, "player.movement", null, msg.toString().getBytes());
		}catch(Exception e){
			System.out.println("no Connection!!!");
		}
	}
	
	
	public void sendPlayerJoined(String playerId, String Mapname) throws JSONException{
		JSONObject msg = new JSONObject();
		msg.put("playerId", playerId);
		msg.put("mapName", Mapname);
		try{
			channel.basicPublish(EXCHANGE_NAME, "player.join", null, msg.toString().getBytes());
		}catch(Exception e){
			System.out.println("no Connection!!!");
		}
	}
	
	
	/*
	 * Requesting information from the server
	 */
	public void getPlayers(String mapName) throws JSONException{
		JSONObject mapname = new JSONObject();
		mapname.put("mapName", mapName);
		try{
			channel.basicPublish(EXCHANGE_NAME, "player.get.all", null, mapname.toString().getBytes());
		}catch(Exception e){
			System.out.println("no Connection!!!");
		}
	}
	
	
	
	
	/*
	 * MISC
	 */
	
	public void closeConnection(){
		try{
			connection.close();
		}catch(Exception e){
			System.out.println("No connection to close!");
		}
	}
}