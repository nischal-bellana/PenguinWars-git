package com.penguin_wars.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Abody{
	public boolean incon = false;
	public boolean res = false;
	public boolean des = false;
	private int groundContacts = 0;
	public Sprite playersp;
	private TextureRegion strt;
	public float health = 100;
	public int deaths = 0;
	public float score = 0;
	public Sprite healthbar;
	public Sprite scorebar;
	public void increaseGroundContacts() {
	    groundContacts++;
	    incon = true; // Player is on the ground
	}

	public void decreaseGroundContacts() {
	    groundContacts--;
	    if(groundContacts <= 0) {
	        incon = false; // Player is no longer on the ground
	        groundContacts = 0; // Ensure it doesn't go negative
	    }
	}
	    
	public Player(World world,BodyDef bdef,FixtureDef fdef,TextureRegion strt,TextureRegion hb) {
		body = world.createBody(bdef);
		body.setUserData(this);
		PolygonShape shp = new PolygonShape();
		shp.setAsBox(0.25f, 0.5f);
		fdef.shape = shp;
		body.createFixture(fdef).setUserData(this);
		fdef.isSensor = true;
		Vector2 vec = new Vector2();
		vec.set(0, -0.5f);
		shp.setAsBox(0.2f, 0.12f, vec, 0);
		body.createFixture(fdef).setUserData(this);
		texcen = new float[2];
		texcen[0] = texcen[1] = 0;
		id = "";
		playersp = new Sprite(strt);
		this.strt = strt;
		playersp.setSize(1,1f);
		playersp.setCenter(texcen[0], texcen[1]);
		shp.dispose();
		healthbar = new Sprite(hb);
		healthbar.setSize(1.5f, 0.12f);
		healthbar.setCenter(getPosition().x,getPosition().y+0.8f);
		scorebar = new Sprite(hb);
		scorebar.setSize(0.01f,0.12f);
		scorebar.setColor(Color.BLACK);
	}
	public void sprUpd() {
		playersp.setCenter(getPosition().x+texcen[0],getPosition().y+texcen[1]);
		healthbar.setCenter(getPosition().x,getPosition().y+0.8f);
		scorebar.setCenter(getPosition().x,getPosition().y+1f);
	}
	public void makestatic() {
		playersp.setRegion(strt);
		incon = true;
	}
	public void updateHb() {
		if(health==0) {
//			deaths++;
//			if(deaths==3) {
//				des = true;
//				return;
//			}
			health = 100;
			res = true;
		}
		healthbar.setSize(1.5f*(health/100f),0.12f);
	}
	public void updateSb() {
		scorebar.setSize(1.5f*(score/100f),0.12f);
	}
	public boolean respawn() {
		if(!body.getWorld().isLocked()) {
			setLinearVelocity(0, 0);
			setTransform(-10, 25, 0);
			return true;
			}
		return false;
	}
	public boolean destroy() {
		if(!body.getWorld().isLocked()) {
			body.getWorld().destroyBody(body);
			return true;
		}
		return false;
	}
	@Override
	public Vector2 getPosition() {
		// TODO Auto-generated method stub
		return super.getPosition();
	}

	@Override
	public void setLinearVelocity(float x, float y) {
		// TODO Auto-generated method stub
		super.setLinearVelocity(x, y);
	}

	@Override
	public void setTransform(float x, float y, float angle) {
		// TODO Auto-generated method stub
		super.setTransform(x, y, angle);
	}

	@Override
	public void applyForceToCenter(int i, int j, boolean b) {
		// TODO Auto-generated method stub
		super.applyForceToCenter(i, j, b);
	}
	
}
