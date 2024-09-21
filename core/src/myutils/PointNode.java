package myutils;

import java.util.List;

import org.opencv.core.Point;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PointNode {
	public PointNode next;
	public PointNode prev;
	public double x;
	public double y;
	public Sprite p_spr;
	public Sprite l_spr;
	
	public PointNode(TextureRegion t) {
		p_spr = new Sprite(t);
		p_spr.setSize(8f,8f);
		p_spr.setCenter(0, 0);
	}
	public PointNode(double x,double y){
		this.x = x;
		this.y = y;
	}
	public PointNode(PointNode b){
		this.x = b.x;
		this.y = b.y;
	}
	public PointNode() {
		// TODO Auto-generated constructor stub
	}
	void copy(PointNode b) {
		x = b.x;
		y = b.y;
	}
	public void set(double x,double y) {
		this.x = x;
		this.y = y;
		if(p_spr!=null) p_spr.setPosition((float)x, (float)y);
	}
	public void setCenter(double x,double y) {
		p_spr.setCenter((float)x, (float)y);
		this.x = p_spr.getX();
		this.y = p_spr.getY();
	}
	public void neg() {
		x = -x;
		y = -y;
	}
	public void sub(PointNode a) {
		x -= a.x;
		y -= a.y;
	}
}
