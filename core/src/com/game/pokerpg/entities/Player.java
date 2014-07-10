package com.game.pokerpg.entities;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Player extends Sprite  {

	private String trainername = null;
	
	//Socket Objects
	private static final String EXCHANGE_NAME = "pokeCom";
	private ConnectionFactory factory = new ConnectionFactory();
	private Channel channel;
	private Connection connection;
	
	/** the movement velocity */
	private Vector2 velocity = new Vector2();
	
	private float speed = 60 * 2, gravity = 0 ;
	
	private TiledMapTileLayer collisionLayer;
	
	public Player(Sprite sprite, TiledMapTileLayer collisionLayer, String trainername){
		super(sprite);
		this.collisionLayer = collisionLayer;
		this.trainername = trainername;

	}
	
	@Override
	public void draw(Batch spriteBatch){
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}
	
	
	public void update(float delta){
		//apply gravity
		velocity.y -= gravity *delta;
		
		//clamp velocity
		if(velocity.y > speed){
			velocity.y = speed;
		} else if(velocity.y < -speed) {
			velocity.y = -speed;
		}
		
		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionX = false, collisionY = false;
		
		//move on x
		setX(getX() + velocity.x * delta);
		if(velocity.x < 0){ //going left
			// top left
			collisionX = collisionLayer.getCell((int) (getX() / tileWidth),(int) ((getY() + getHeight()) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
			// middle left
			if(!collisionX)
			collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
			// bottom left
			if(!collisionX)
			collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
		}else if(velocity.x > 0){ //going right
			
			// top right
			collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth),(int) ((getY() + getHeight()) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
			// middle right
			if(!collisionX)
			collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth),(int) ((getY() + getHeight() / 2) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
			// bottom right
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth),(int) (getY()/ tileHeight))
						.getTile().getProperties().containsKey("blocked");
		}
		
		//react to a x collision
		if(collisionX){
			setX(oldX);
			velocity.x = 0;
		}
		
		
		//move on y
		setY(getY() + velocity.y * delta);
		
		if(velocity.y < 0){
			//bottom left
				collisionY = collisionLayer.getCell((int) (getX() / tileWidth),(int) (getY() / tileHeight))
						.getTile().getProperties().containsKey("blocked");
			//bottom middle
			if(!collisionY)
				collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth),(int) (getY() / tileHeight))
						.getTile().getProperties().containsKey("blocked");
			//bottom right
			if(!collisionY)
				collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth),(int) (getY() / tileHeight))
						.getTile().getProperties().containsKey("blocked");
		}else if(velocity.y > 0){
			// top left
			collisionY = collisionLayer.getCell((int) (getX() / tileWidth),(int) ((getY() + getHeight()) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
			// top middle
			if(!collisionY)
			collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth),(int) ((getY() + getHeight()) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
			//top right
			if(!collisionY)
			collisionY = collisionLayer.getCell((int) ((getX()+ getWidth()) / tileWidth),(int) ((getY() + getHeight()) / tileHeight))
					.getTile().getProperties().containsKey("blocked");
		}
		
		//react on y collision
		if(collisionY){
			setY(oldY);
			velocity.y = 0;
		}
			
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocityX(int velocity) {
		this.velocity.x = velocity;
		//sendMovement();
	}
	
	public void setVelocityY(int velocity) {
		this.velocity.y = velocity;
		//sendMovement();
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}	
	

	public String getTrainername() {
		return trainername;
	}

}
