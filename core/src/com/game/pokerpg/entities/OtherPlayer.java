package com.game.pokerpg.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class OtherPlayer extends Sprite {
	
	private String playerId;
	private Vector2 velocity = new Vector2();
	private float speed = 60 * 2, gravity = 0 ;
	
	public OtherPlayer(Sprite sprite){
		super(sprite);
	}
	
	public void draw(Batch spriteBatch){
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	private void update(float delta) {
		//apply gravity
		velocity.y -= gravity *delta;
		
		//clamp velocity
		if(velocity.y > speed){
			velocity.y = speed;
		} else if(velocity.y < -speed) {
			velocity.y = -speed;
		}
		
		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocityX(int velocity) {
		this.velocity.x = velocity;
	}
	
	public void setVelocityY(int velocity) {
		this.velocity.y = velocity;
	}
	
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
}
