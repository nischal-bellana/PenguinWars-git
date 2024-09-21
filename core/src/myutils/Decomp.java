package myutils;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Decomp {
	public static void decomp(PNadv head,Array<PNadv> polys) {
		if(head.vert_count<3) return;
		if(head.vert_count==3) {
			if(polys.size!=0) {
				PNadv last = polys.get(polys.size-1);
				last.nextPoly = head;
				head.prevPoly = last;
			}
			polys.add(head);
			return;
		}
		if(!phelp.isclw(head)) {
			phelp.reverse(head);
		}
		if(head.vert_count==4) {
			PointNode cur = head;
			boolean first = true;
			boolean fnd = false;
			while(cur!=head || first) {
				first = false;
				if(phelp.isref2(cur)) {
					fnd = true;
					break;
				}
				cur = cur.next;
			}
			if(fnd) {
				head.ishead = false;
				PNadv cur2= (PNadv)cur.next.next;
				cutpoly((PNadv)cur,cur2,3,4,polys);
				if(polys.size!=0) {
					PNadv last = polys.get(polys.size-1);
					last.nextPoly = (PNadv) cur;
					((PNadv)cur).prevPoly = last;
				}
				polys.add((PNadv)cur);
				return;
			}
			if(polys.size!=0) {
				PNadv last = polys.get(polys.size-1);
				last.nextPoly = head;
				head.prevPoly = last;
			}
			polys.add(head);
			return;
		}
		head.ishead = false;
		PointNode cur = head;
		while(phelp.isref(cur) || !phelp.chincRt(cur.prev,cur,cur.next)) {
			cur = cur.next;
		}
		PointNode right = cur.next;
		PointNode left = cur.prev;
		int count = 3;
		
		double th = 0;
		double dth = 0;
		Vector2 vec = new Vector2();
		vec.set((float)(cur.x-left.x),(float)(cur.y-left.y));
		Vector2 prev = new Vector2();
		prev.set(vec);
		vec.set((float)(right.x-left.x),(float)(right.y-left.y));
		dth = vec.angleDeg()-prev.angleDeg();
		if(dth>180) dth-=360;
		else if(dth<-180) dth+=360;
		th+=dth;
		prev.set(vec);
		vec.set((float)(right.next.x-left.x),(float)(right.next.y-left.y));
		dth = vec.angleDeg()-prev.angleDeg();
		if(dth>180) dth-=360;
		else if(dth<-180) dth+=360;
		th+=dth;
		
		while(count <8 && right.next!=left && !phelp.isref2(right) && Math.abs(th)<180  && phelp.chincRt(left,right,right.next)) {
			count++;
			right = right.next;
			prev.set(vec);
			vec.set((float)(right.next.x-left.x),(float)(right.next.y-left.y));
			dth = vec.angleDeg()-prev.angleDeg();
			if(dth>180) dth-=360;
			else if(dth<-180) dth+=360;
			th+=dth;
		}
		th=0;
		dth=0;
		prev.set((float)(right.prev.x-right.x),(float)(right.prev.y-right.y));
		vec.set((float)(left.x-right.x),(float)(left.y-right.y));
		dth = vec.angleDeg()-prev.angleDeg();
		if(dth>180) dth-=360;
		else if(dth<-180) dth+=360;
		th+=dth;
		prev.set(vec);
		vec.set((float)(left.prev.x-right.x),(float)(left.prev.y-right.y));
		dth = vec.angleDeg()-prev.angleDeg();
		if(dth>180) dth-=360;
		else if(dth<-180) dth+=360;
		th+=dth;
		while(count <8 && left.prev!=right && !phelp.isref2(left) && Math.abs(th)<180  && phelp.chincLt(left.prev,left,right)) {
			count++;
			left = left.prev;
			prev.set(vec);
			vec.set((float)(left.prev.x-right.x),(float)(left.prev.y-right.y));
			dth = vec.angleDeg()-prev.angleDeg();
			if(dth>180) dth-=360;
			else if(dth<-180) dth+=360;
			th+=dth;
		}
		cutpoly((PNadv)left,(PNadv)right,count,head.vert_count,polys);
		decomp((PNadv)left,polys);
	}
	public static void cutpoly(PNadv cur, PNadv cur2,int count,int vert_count,Array<PNadv>polys) {
		PNadv curcpy = new PNadv(cur);
		PNadv cur2cpy = new PNadv(cur2);
		
		curcpy.next = cur.next;
		curcpy.prev = cur2cpy;
		cur2cpy.next = curcpy;
		cur.next.prev = curcpy;
		cur2cpy.prev = cur2.prev;
		cur2.prev.next = cur2cpy;
		
		cur.next = cur2;
		cur2.prev = cur;
		
		curcpy.ishead = true;
		curcpy.vert_count = count;
		
		cur.ishead = true;
		cur.vert_count = vert_count-count+2;
		
		if(polys.size!=0) {
			PNadv last = polys.get(polys.size-1);
			last.nextPoly = curcpy;
			curcpy.prevPoly = last;
		}
		polys.add(curcpy);
	}
}

