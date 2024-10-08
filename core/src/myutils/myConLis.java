package myutils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.penguin_wars.game.Abody;
import com.penguin_wars.game.Bomb;
import com.penguin_wars.game.Player;
import com.penguin_wars.game.clipCL;
import com.penguin_wars.game.cookie;
import com.penguin_wars.game.penguin_wars;

import game_states.GameDevState;

public class myConLis implements ContactListener{
	public static World world;
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fixa = contact.getFixtureA();
		Fixture fixb = contact.getFixtureB();
		Abody abodya = (Abody)fixa.getUserData();
		Abody abodyb = (Abody)fixb.getUserData();
		
		if(abodya!=null && abodya instanceof Bomb) {
			Bomb b = (Bomb)abodya;
			b.des = true;
		}
		
		if(abodyb!=null && abodyb instanceof Bomb) {
			Bomb b = (Bomb)abodyb;
			b.des = true;
		}
		
		if(abodya==null||abodyb==null) return;
		
		if((abodya instanceof Player && fixa.isSensor())||(abodyb instanceof Player && fixb.isSensor())) {
			if((abodya instanceof cookie||abodyb instanceof cookie)) {
				if(abodya instanceof Player) {
					Player pl = (Player)abodya;
					pl.increaseGroundContacts();
				}
				else {
					Player pl = (Player)abodyb;
					pl.increaseGroundContacts();
				}
			}
		}
		else {
			if(abodya instanceof clipCL || abodyb instanceof clipCL) {
				if(abodya instanceof cookie) {
					Clipping.clip(fixa,abodya.getPosition(),abodyb.getPosition());
					clipCL cl = (clipCL)abodyb;
					cl.des = true;
				}
				else if(abodyb instanceof cookie) {
					Clipping.clip(fixb,abodyb.getPosition(),abodya.getPosition());
					clipCL cl = (clipCL)abodya;
					cl.des = true;
				}
				else if(abodya instanceof Player) {
					Vector2 vec = new Vector2();
					vec.set(abodya.getPosition());
					vec.sub(abodyb.getPosition());
					float impact = (5.66f-vec.len())/5.66f;
					if(impact>=1) {
						System.out.println("impact>1!");
						return;
					}
					System.out.println(impact);
					abodya.body.applyLinearImpulse(2*impact*vec.x/vec.len(), 2*impact*vec.y/vec.len(), abodya.getPosition().x, abodya.getPosition().y, true);
					Player pl = (Player)abodya;
					clipCL cl = (clipCL)abodyb;
					if(cl.pl==pl) return;
					float damage = 15*impact;
					cl.pl.score+=damage;
					cl.pl.updateSb();
					if(damage>pl.health) pl.health = 0;
					else pl.health-=damage;
					pl.updateHb();
				}
				else if(abodyb instanceof Player) {
					Vector2 vec = new Vector2();
					vec.set(abodyb.getPosition());
					vec.sub(abodya.getPosition());
					float impact = (5.66f-vec.len())/5.66f;
					if(impact>=1) {
						System.out.println("impact>1!");
						return;
					}
					System.out.println(impact);
					abodyb.body.applyLinearImpulse(2*impact*vec.x/vec.len(), 2*impact*vec.y/vec.len(), abodyb.getPosition().x, abodyb.getPosition().y, true);
					Player pl = (Player)abodyb;
					clipCL cl = (clipCL)abodya;
					if(cl.pl==pl) return;
					float damage = 15*impact;
					cl.pl.score+=damage;
					cl.pl.updateSb();
					if(damage>pl.health) pl.health = 0;
					else pl.health-=damage;
					pl.updateHb();
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fixa = contact.getFixtureA();
		Fixture fixb = contact.getFixtureB();
		Abody abodya = (Abody)contact.getFixtureA().getUserData();
		Abody abodyb = (Abody)contact.getFixtureB().getUserData();
		if(abodya==null||abodyb==null) return;
		if((abodya instanceof Player && fixa.isSensor())||(abodyb instanceof Player && fixb.isSensor())) {
			if((abodya instanceof cookie||abodyb instanceof cookie)) {
				if(abodya instanceof Player) {
					Player pl = (Player)abodya;
					pl.decreaseGroundContacts();
				}
				else {
					Player pl = (Player)abodyb;
					pl.decreaseGroundContacts();
				}
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
