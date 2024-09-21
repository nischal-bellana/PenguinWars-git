package com.penguin_wars.game;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import myutils.XMLHelper;
import myutils.polygonGen;

public class clipCL extends Abody{
	public int frame_count = 0;
	public boolean des = false;
	public Player pl;
	public clipCL(Vector2 pos,Array<Sprite> clipsp,TextureRegion tex,World world)  {
		super();
		id = "clip";
		Sprite sp = new Sprite(tex);
		sp.setSize(4.096f, 4.096f);
		sp.setCenter(pos.x, pos.y);
		clipsp.add(sp);
		BodyDef bdef = new BodyDef();
		bdef.position.set(pos);
		bdef.type = BodyType.DynamicBody;
		bdef.fixedRotation = true;
		body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		fdef.density = 0.00001f;
		fdef.friction = 1;
		fdef.restitution = 1;
		fdef.isSensor = true;
		PolygonShape shp = new PolygonShape();
		shp.setAsBox(2, 2);
		fdef.shape = shp;
		body.createFixture(fdef).setUserData(this);
		shp.dispose();
	}
	public boolean destroy() {
		if(!body.getWorld().isLocked()) {
			body.getWorld().destroyBody(body);
			return true;
		}
		return false;
	}
}
