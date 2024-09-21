package myutils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.penguin_wars.game.cookie;

public class Clipping {
	public static double[] ins(PointNode p1,PointNode p2, PointNode p3, PointNode p4) {
		double[] alph  = new double[2];
		double a1 = (p2.x-p1.x);
		double b1 = (p4.x-p3.x);
		double c1 = (p1.x-p3.x);
		double a2 = (p2.y-p1.y);
		double b2 = (p4.y-p3.y);
		double c2 = (p1.y-p3.y);
		alph[0] = alph[1] = -1;
		if(Math.abs(a2*b2 - b1*a2) < 0.0001) {
			return alph;
		}
		double b = ((b1*c2-c1*b2)/(a1*b2-a2*b1));
		if(b<=0 || b>=1) {
			return alph;
		}
		alph[0] = b;
		b = ((a2*c1-a1*c2)/(b1*a2-b2*a1));
		if(b<=0 || b>=1) {
			alph[0] = -1;
			return alph;
		}
		alph[1] = b;

		return alph;
	}

	public static void clip(Fixture a, Vector2 pos1,Vector2 pos2) {
		cookie ck = (cookie)a.getUserData();
		PolygonShape shp =  (PolygonShape)a.getShape();
		PNadv subPN = shptoPN(shp,pos1);
		PNadv clipPN = genCir(pos2,2);
		int intersectcount = phase1(subPN,clipPN);
		if(intersectcount%2==1) return;
		if(intersectcount==0) {
			if(phelp.is_in(subPN, clipPN)) {
				ck.destroyqueue.add(a);
				return;
			}
			return;
		}
		phase2(subPN,clipPN);
		PNadv res = phase3(subPN,clipPN);
		if(res==null) return;
		ck.destroyqueue.add(a);
		PNadv curPoly = res;
		while(curPoly!=null) {
			int count = cookie.countverts(curPoly);
			if(curPoly.vert_count!=count) {
				curPoly.vert_count = count;
			}
			curPoly = curPoly.nextPoly;
		}
		ck.createqueue.add(res);
	}
	public static PNadv genCir(Vector2 pos,double r) {
		Vector2 p = new Vector2();
		p.set((float)r,0);
		PNadv head = new PNadv();
		head.vert_count = 30;
		float dth = 12;
		head.ishead = true;
		PNadv cur = head;
		for(int i=0;i<30;i++) {
			cur.set(pos.x+p.x,pos.y+p.y);
			cur.next = new PNadv();
			cur.next.prev = cur;
			cur = (PNadv)cur.next;
			p.rotateDeg(dth);
		}
		cur = (PNadv)cur.prev;
		cur.next = head;
		head.prev = cur;
		return head;
	}
	public static PNadv shptoPN(PolygonShape shp,Vector2 pos) {
		PNadv head = new PNadv();
		head.ishead = true;
		head.vert_count = shp.getVertexCount();
		PNadv cur = head;
		Vector2 vec = new Vector2();
		for(int i=0;i<head.vert_count;i++) {
			shp.getVertex(i, vec);
			cur.set(pos.x + vec.x,pos.y + vec.y);
			cur.next = new PNadv();
			cur.next.prev = cur;
			cur = (PNadv)cur.next;
		}
		cur = (PNadv)cur.prev;
		cur.next = head;
		head.prev = cur;
		return head;
	}
	static int phase1(PNadv subPN,PNadv clipPN) {
		int intersectcount = 0;
		PNadv cursl = subPN;
		PNadv cursr = (PNadv)subPN.next;
		boolean first = true;
		while(cursl!=subPN || first) {
			if(first) first = false;
			PNadv curcl = clipPN;
			PNadv curcr = (PNadv)clipPN.next;
			while(curcr.intersect) {
				curcr = (PNadv)curcr.next;
			}
			boolean first2 = true;
			while(curcl!=clipPN||first2) {
				if(first2) first2 = false;
				double[] alpha = ins(cursl,cursr,curcl,curcr);
				if(alpha[0]!=-1) {
					intersectcount++;
					PNadv inss;
					inss = new PNadv(cursl.x+alpha[0]*(cursr.x-cursl.x),cursl.y+alpha[0]*(cursr.y-cursl.y));
					PNadv insc;
					insc = new PNadv(curcl.x+alpha[1]*(curcr.x-curcl.x),curcl.y+alpha[1]*(curcr.y-curcl.y));
					inss.intersect = insc.intersect = true;
					inss.alpha = alpha[0];
					insc.alpha = alpha[1];
					inss.neighbor = insc;
					insc.neighbor = inss;
					insert(cursl,cursr,inss);
					insert(curcl,curcr,insc);
				}
				curcl = curcr;
				curcr = (PNadv)curcr.next;
				while(curcr.intersect) {
					curcr = (PNadv)curcr.next;
				}
			}
			cursl = cursr;
			cursr = (PNadv)cursr.next;
		}
		return intersectcount;
	}
	static void phase2(PNadv subPN,PNadv clipPN) {
		boolean en = false;
		if(phelp.is_in(subPN,clipPN)) {
			en = true;
		}
		PNadv cur = (PNadv)subPN.next;
		while(cur!=subPN) {
			if(cur.intersect) {
				cur.en_ex = en;
				en = !en;
			}
			cur = (PNadv) cur.next;
		}
		en = true;
		if(phelp.is_in(clipPN,subPN)) {
			en = false;
		}
		cur = (PNadv)clipPN.next;
		while(cur!=clipPN) {
			if(cur.intersect) {
				cur.en_ex = en;
				en = !en;
			}
			cur = (PNadv)cur.next;
		}
	}
	static PNadv phase3(PNadv subPN,PNadv clipPN) {
		PNadv headPoly = null;
		PNadv curPoly = null;
		PNadv cur = (PNadv)subPN.next;
		while(cur!=subPN) {
			if(cur.intersect) {
				if(headPoly==null) {
					curPoly=headPoly = new PNadv(cur);
					curPoly.vert_count = 0;
					curPoly.ishead = true;
				}
				else {
					curPoly.nextPoly = new PNadv(cur);
					curPoly.nextPoly.prevPoly = curPoly;
					curPoly = curPoly.nextPoly;
					curPoly.vert_count = 0;
					curPoly.ishead = true;
				}
				cur.intersect = false;
				PNadv polycur = curPoly;
				PNadv cur2 = cur;
				boolean first = true;
				while(cur2!=cur||first){
					first = false;
					if(cur2.en_ex) {
						cur2 = (PNadv)cur2.next;
						polycur.next = new PNadv(cur2);
						polycur.next.prev = polycur;
						polycur = (PNadv)polycur.next;
						curPoly.vert_count++;
						PNadv start = cur2;
						boolean first2 = true;
						while(!cur2.intersect) {
							if(!first2 && cur2==start) {
								return null;
							}
							first2 = false;
							cur2 = (PNadv)cur2.next;
							polycur.next = new PNadv(cur2);
							polycur.next.prev = polycur;
							polycur = (PNadv)polycur.next;
							curPoly.vert_count++;
						}
						cur2.intersect = false;
					}
					else {
						cur2 = (PNadv)cur2.prev;
						polycur.next = new PNadv(cur2);
						polycur.next.prev = polycur;
						polycur = (PNadv)polycur.next;
						curPoly.vert_count++;
						PNadv start = cur2;
						boolean first2 = true;
						while(!cur2.intersect) {
							if(!first2 && cur2==start) {
								return null;
							}
							first2 = false;
							cur2 = (PNadv)cur2.prev;
							polycur.next = new PNadv(cur2);
							polycur.next.prev = polycur;
							polycur = (PNadv)polycur.next;
							curPoly.vert_count++;
						}
						cur2.intersect = false;
					}
					cur2 = cur2.neighbor;
					cur2.intersect = false;
				}
				polycur = (PNadv)polycur.prev;
				polycur.next = curPoly;
				curPoly.prev = polycur;
			}
			cur = (PNadv)cur.next;
		}
		if(curPoly==null) {
			System.out.println("anom!");
			return subPN;
		}
		return headPoly;
	}
	static void insert(PNadv endl,PNadv endr, PNadv inp) {
		PNadv cur = (PNadv)endl.next;
		while(cur!=endr && cur.alpha<inp.alpha) {
			cur = (PNadv)cur.next;
		}
		PNadv left= (PNadv)cur.prev;
		left.next = inp;
		inp.prev = left;
		inp.next = cur;
		cur.prev = inp;
	}

}
