package com.penguin_wars.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import myutils.polygonGen;

public class Abody{
	public float[] texcen;
	public Body body;
	public String id;
	public Abody() {
		
	}
	public Abody(World world,String datapath,BodyDef bdef,FixtureDef fdef,float scale,float w,float h){
		
		float[] bound = new float[4];
		bound[0] = bound[2] = Float.MAX_VALUE;
		bound[1] = bound[3] = Float.MIN_VALUE;
		body = world.createBody(bdef);
		body.setUserData(this);
		body = polygonGen.createImgb(world,datapath,body,fdef,scale,bound);
		texcen = new float[2];
		texcen[0] = ((w)/2 - bound[0])*scale;
		texcen[1] = ((h)/2 - bound[1])*scale;
		id = "";
	}
	public Vector2 getPosition() {
		return body.getPosition();
	}
	public void setLinearVelocity(float x,float y) {
		body.setLinearVelocity(x,y);
	}
	public void setTransform(float x, float y, float angle) {
		body.setTransform(x, y,angle);
	}
	public void applyForceToCenter(int i, int j, boolean b) {
		body.applyForceToCenter(i, j, b);
	}
	
}