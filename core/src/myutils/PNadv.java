package myutils;

import java.util.List;

import org.opencv.core.Point;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PNadv extends PointNode{
	//algo fields
	public boolean intersect = false;
	public boolean en_ex = false;
	public boolean ishead = false;
	public int vert_count = 0;
	public double xmin,xmax,ymin,ymax;
	public PNadv neighbor;
	public PNadv nextPoly;
	public PNadv prevPoly;
	public double alpha;
	public PNadv(TextureRegion t) {
		super(t);
	}
	public PNadv(double x,double y){
		super(x,y);
	}
	public PNadv() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PNadv(PointNode b) {
		super(b);
		// TODO Auto-generated constructor stub
	}
	public static PNadv listoNode(List<Point> polygonV,double height) {
		PNadv cur = new PNadv(polygonV.get(0).x,height - polygonV.get(0).y);
		PNadv head = cur;
		head.ishead = true;
		head.vert_count = polygonV.size();
		for(int i=1;i<polygonV.size();i++) {
			cur.next = new PNadv(polygonV.get(i).x,height - polygonV.get(i).y);
			cur.next.prev = cur;
			cur = (PNadv)cur.next;
		}
		cur.next = head;
		head.prev = cur;
		return head;
	}
}
