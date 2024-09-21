package com.penguin_wars.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import myutils.myConLis;

public class Bomb extends Abody{
	public boolean des = false;
	public Sprite spr;
	public Player pl;
	public Bomb(World world, String datapath,TextureRegion tex, BodyDef bdef, FixtureDef fdef, float scale, float w, float h) {
		super(world, datapath, bdef, fdef, scale, w, h);
		spr = new Sprite(tex);
		spr.setSize(1.024f, 1.024f);
		spr.setCenter(body.getPosition().x+texcen[0],body.getPosition().y+texcen[1]);
		spr.setOriginCenter();
		spr.setRotation((float)Math.toDegrees(body.getAngle()));
	}
	public void updateSprite() {
		spr.setCenter(body.getPosition().x+texcen[0],body.getPosition().y+texcen[1]);
		spr.setOriginCenter();
		spr.setRotation((float)Math.toDegrees(body.getAngle()));
	}
	public boolean destroy() {
		if(!body.getWorld().isLocked()) {
			body.getWorld().destroyBody(body);
			return true;
		}
		return false;
	}
}
