package com.penguin_wars.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import myutils.PNadv;
import myutils.PointNode;


public class cookie extends Abody{
	public Array<Fixture> destroyqueue;
	public Array<PNadv> createqueue;
	public Array<float[]> polysfinal;
	FixtureDef fdeff;
	public cookie() {
		super();
		destroyqueue = new Array<Fixture>();
		createqueue = new Array<PNadv>();
		polysfinal = new Array<float[]>();
		fdeff = new FixtureDef();
	}
	public cookie(World world, String datapath, BodyDef bdef, FixtureDef fdef, float scale, float w, float h) {
		super(world, datapath, bdef, fdef, scale, w, h);
		destroyqueue = new Array<Fixture>();
		createqueue = new Array<PNadv>();
		polysfinal = new Array<float[]>();
		this.fdeff = fdef;
		// TODO Auto-generated constructor stub
	}

	public boolean create(float[] arr) {
		if(!body.getWorld().isLocked()) {
			PolygonShape shpe = new PolygonShape();
			if(!validarr(arr)) {
				shpe.dispose();
				return true;
			}
			shpe.set(arr);
			fdeff.shape = shpe;
			Fixture f = body.createFixture(fdeff);
			f.setUserData(this);
			shpe.dispose();
			return true;
		}
		return false;
	}
	public boolean destroy(Fixture f) {
		if(!body.getWorld().isLocked()) {
			body.destroyFixture(f);
			return true;
		}
		return false;
	}
	public void createfixtures() {
		for(int i=0;i<polysfinal.size;i++) {
			float[] vert = polysfinal.get(i);
			if(create(vert)) {
				polysfinal.removeIndex(i);
				i--;
			}
		}
	}
	public static float[] PNtoarr(PNadv head) {
		float[] vert = new float[2*head.vert_count];
		PNadv cur = head;
		boolean first = true;
		int i=0;
		while(cur!=head || first) {
			first = false;
			vert[i] = (float)cur.x;
			vert[i+1] = (float)cur.y;
			i+=2;
			cur = (PNadv)cur.next;
		}
		return vert;
	}
	public static int countverts(PNadv head) {
		int count = 0;
		PNadv cur = head;
		boolean first = true;
		while(cur!=head || first) {
			first = false;
			count++;
			cur = (PNadv)cur.next;
		}
		return count;
	}
	public static boolean validarr(float[] arr) {
		if(arr.length<6 || arr.length>16|| arr.length%2==1 || aofPy(arr)<0.01) return false;
		return true;
	}
	public static double aofTr(float x1,float y1, float x2,float y2, float x3,float y3) {
		double area = Math.abs(x1*(y2 - y3) + x2*(y3 - y1) + x3*(y1 - y2))/2;   //A = (1/2) |x1(y2 − y3) + x2(y3 − y1) + x3(y1 − y2)|
		return area;
	}
	public static double aofPy(float[] arr) {
		double area = 0;
		float x1 = arr[0];
		float y1 = arr[1];
		float x2 = arr[2];
		float y2 = arr[3];
		float x3,y3=0;
		for(int i=4;i<arr.length;i+=2) {
			x3 = arr[i];
			y3 = arr[i+1];
			area += aofTr(x1,y1,x2,y2,x3,y3);
			x2 = x3;
			y2 = y3;
		}
		return area;
	}
	public static double minlen(float[] arr) {
		Vector2 vec = new Vector2();
		vec.set(arr[0]-arr[arr.length-2],arr[1]-arr[arr.length-1]);
		double min = vec.len();
		for(int i=0;i<arr.length-2;i+=2) {
			vec.set(arr[i]-arr[i+2],arr[i+1]-arr[i+3]);
			min = Math.min(min, vec.len());
		}
		return min;
	}
}
