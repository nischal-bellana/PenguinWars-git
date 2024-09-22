package game_states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.penguin_wars.game.Abody;
import com.penguin_wars.game.Bomb;
import com.penguin_wars.game.Constants;
import com.penguin_wars.game.Player;
import com.penguin_wars.game.clipCL;
import com.penguin_wars.game.cookie;

import myutils.Clipping;
import myutils.Decomp;
import myutils.PNadv;
import myutils.myConLis;
import myutils.PointNode;
import myutils.phelp;
import myutils.polygonGen;

public class GameDevState extends State {
	// Fields
		//batch, camera...
		SpriteBatch batch;
		OrthographicCamera camera;
		World world;
		Box2DDebugRenderer debugRenderer;
		//Abodies
		public Player player;
		Abody grnd;
		Array<Bomb> bombs;
		Array<clipCL> clips;
		Array<Player> players;
		//Texture Regions (atlas)
		AtlasRegion grndrg;
		AtlasRegion strt;
		AtlasRegion bombtex;
		AtlasRegion clip;
		AtlasRegion[] pow;
		AtlasRegion[] lefty;
		AtlasRegion[] rghty;
		AtlasRegion red;
		AtlasRegion viol;
		AtlasRegion pink;
		AtlasRegion yellow;
		AtlasRegion green;
		AtlasRegion[] hbs ;
		AtlasRegion cross;
		AtlasRegion blackcross;
		AtlasRegion back;
		TextureAtlas atlas;
		//Sprites
		Sprite grndsp;
		Sprite powsp;
		Sprite watersp;
		Array<Sprite> clipsp;
		//Scene2d
		Stage stage;
		Table table;
		Label turntime;
		Label playernum;
		Label gametimesec;
		Label gametimemin;
		Label colon;
		LabelStyle lsty;
		LabelStyle lsty2;
		BitmapFont font30;
		//Prim fields
		int l = 0;
		int r = 0;
		boolean isr = false;
		boolean bshow = false; 
		boolean lchd = false;
		float power = 0;
		float game_timer = 300;
		int[] real_game_timer = {5,0};
		float turntimer = 0;
		private float accumulator = 0;
		private float[] spwpos = {-10.8125f,13.0f,12.25f,13.5f,-5.53125f,5.03125f,11.03125f,3.5f,-0.46875f,5.78125f};
		private int num = 0;
		private boolean ended = false;
		private int winner = 1;
		Color[] plclr = {Color.RED,Color.BLUE,Color.YELLOW,Color.PINK,Color.GREEN};
		String[] plnames = {"Red","Blue","Yellow","Pink","Green"};
		private ShapeRenderer shapeRenderer;
		//Constructors
		protected GameDevState(GameStateManager gsm){
			//Sprite Batch and Camera
			this.gsm = gsm;
			batch = new SpriteBatch();
			camera = new OrthographicCamera();
			camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			atlas = new TextureAtlas("game-imgs-packed//pack.atlas"); //Sprite sprite = atlas.createSprite("otherimagename");
			num = 3;
			create();
		}
		public GameDevState(StartState startState) {
			// TODO Auto-generated constructor stub
			gsm = startState.gsm;
			batch = startState.batch;
			camera = startState.camera;
			atlas = startState.atlas;
			num = startState.num;
			game_timer = 60*startState.game_time;
			create();
		}
		@Override
		public void create () {
			//Box2D initializing
			Box2D.init();
//			polygonGen.trcImg("game-imgs/grnd3.png", "output.xml"); "-Djava.library.path=C:/Users/nisch/eclipse-workspace/Penguin Wars/libs/native"
			//Textures
			texInit();
			//The World
			world = new World(new Vector2(0,-9.8f),true); 
			//Abodies
			createPlatform();
			abodsInit();
			//Sprites
			sprInit();
			//Scene 2d
			fontInit();
			s2dInit();
			//ContactListener
			world.setContactListener(new myConLis());
			myConLis.world = world;
			//Debug Renderer
			debugRenderer = new Box2DDebugRenderer();
			
		    shapeRenderer = new ShapeRenderer();
		    shapeRenderer.setAutoShapeType(true);
		    
		    Vector3 pos = camera.position;
		    pos.x = 0;
		    pos.y = 0;
		    camera.update();
		}
		@Override
		public void render () {
			ScreenUtils.clear(0, 0.5f, 0.5f, 1);//default background
			cameraUpdate();//centering camera on player
			batch.setProjectionMatrix(camera.combined.scl(Constants.PPM));	//batch proj matrix
			float delta = Gdx.graphics.getDeltaTime();
			debugRenderer.render(world, camera.combined);//rendering using box2D debug renderer
			batchRender();//rendering sprites using sprite batch
			s2drender(delta);
			if(ended) return;
			worldUpdate(delta);//world update
		}
		@Override
		public void dispose () {
			batch.dispose();
			world.dispose();
			atlas.dispose();
			shapeRenderer.dispose();
			stage.dispose();
		}
		//Inititializing methods here
		private void texInit() {
			hbs = new AtlasRegion[5];
			back = atlas.findRegion("back");
			hbs[0] = red = atlas.findRegion("red");
			hbs[1] = viol = atlas.findRegion("viol");
			hbs[2] = yellow = atlas.findRegion("yellow");
			hbs[3] = pink = atlas.findRegion("pink");
			hbs[4] = green = atlas.findRegion("green");
			cross = atlas.findRegion("cross");
			strt = atlas.findRegion("strt");
			lefty = new AtlasRegion[3];
			lefty[0] = atlas.findRegion("left");
			lefty[1] = atlas.findRegion("left1");
			lefty[2] = atlas.findRegion("left2");
			rghty = new AtlasRegion[3];
			rghty[0] = atlas.findRegion("rght");
			rghty[1] = atlas.findRegion("rght1");
			rghty[2] = atlas.findRegion("rght2");
			clip = atlas.findRegion("clip");
			pow = new AtlasRegion[10];
			pow[0] = atlas.findRegion("pow");
			for(int i=2;i<=10;i++) {
				pow[i-1] = atlas.findRegion("pow" + i);
			}
			grndrg = atlas.findRegion("grnd3");
			bombtex = atlas.findRegion("bomb");
			blackcross = atlas.findRegion("blackcross");
		}
		private void abodsInit() {
			grnd = createGrnd();
			bombs = new Array<Bomb>();
			clips = new Array<clipCL>();
			players = new Array<Player>();
			for(int i=0;i<num;i++) createPlayer();
			player = players.first();
		}
		private void sprInit() {
			grndsp = new Sprite(grndrg);
			grndsp.setSize(1200/32f,900/32f);
			grndsp.setCenter(grnd.texcen[0], grnd.texcen[1]);
			
			powsp = new Sprite(pow[0]);
			powsp.setSize(2*1.024f,2*1.024f);
			powsp.setCenter(0, 0);
			watersp = new Sprite(viol);
			watersp.setSize(40,5);
			watersp.setCenter(0, -13.5f);
			watersp.setAlpha(0.3f);
			clipsp = new Array<Sprite>();
		}
		private void fontInit() {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rebellion-squad-font/RebellionSquad-ZpprZ.ttf"));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = 30;
			font30 = generator.generateFont(parameter); // font size 30 pixels
			generator.dispose(); // don't forget to dispose to avoid memory leaks!
		}
		private void s2dInit() {
			stage = new Stage();
			
			table = new Table();
			stage.addActor(table);
			table.setFillParent(true);
			table.setDebug(false);
			table.top().padTop(20).left();
			
			lsty = new LabelStyle();
			lsty.font = font30;
			lsty.fontColor = Color.YELLOW;
			
			Label label1 = new Label("Game Timer ",lsty);
			table.add(label1).padLeft(50);
			gametimemin = new Label("5",lsty);
			table.add(gametimemin);
			colon = new Label(":0",lsty);
			table.add(colon);
			gametimesec = new Label("0",lsty);
			table.add(gametimesec);
			
			Label label2 = new Label("Turn ends in ",lsty);
			table.add(label2).padLeft(200);
			
			turntime = new Label("",lsty);
			table.add(turntime);
			
			lsty2 = new LabelStyle(lsty);
			lsty2.fontColor = Color.RED;
			playernum = new Label("Red",lsty2);
			table.add(playernum).padLeft(100);
			
			table.add(new Label("'s Turn",lsty2));
			
		}
		//Inside render method
		private void cameraUpdate() {
//			Vector3 pos = camera.position;
//			pos.x = player.getPosition().x*Constants.PPM;
//			pos.y = player.getPosition().y*Constants.PPM;
			camera.update();
		}
	 	private void batchRender() {
	 		batch.begin();
	 		batch.draw(back,-600/32f ,-450/32f, 1200/32f,900/32f);
	 		batch.end();
	 		drawgrnd();
	 		batch.begin();
			for(int i=0;i<players.size;i++) {
				Player pl = players.get(i);
				pl.playersp.draw(batch);
				pl.healthbar.draw(batch);
				pl.scorebar.draw(batch);
			}
			for(int i=0;i<bombs.size;i++) {
				Bomb b = bombs.get(i);
				b.spr.draw(batch);
			}
			if(bshow) powsp.draw(batch);
			watersp.draw(batch);
			batch.end();
	 	}
	 	private void s2drender(float delta) {
	 		turntime.setText(20-(int)turntimer);
	 		gametimemin.setText(real_game_timer[0]);
	 		gametimesec.setText(real_game_timer[1]);
	 		colon.setText(real_game_timer[1]/10==0?":0":":");
	 		
	 		playernum.setText(plnames[players.indexOf(player, true)]);
	 		lsty2.fontColor = plclr[players.indexOf(player, true)];
	 		
	 		stage.act(delta);
	 		stage.draw();
	 	}
	 	private void worldUpdate(float deltaTime) {
	 		timerUpd(deltaTime);
	 		if(ended) return;
	 		player.playersp.setRegion(strt);
			inputUpdate();
			if(!player.incon)
				jumpUpdate();
			bombupd();
			if(clips.size!=0)
				clipsupdate();
			cookie ck = (cookie)grnd;
			if(ck.polysfinal.size!=0||ck.destroyqueue.size!=0||ck.createqueue.size!=0) {
				clipeff();
			}
			doPhysicsStep(deltaTime);
			plUpdate();
		}
	 	// Other methods
	 	private void timerUpd(float delta) {
	 		if(turntimer>20) {
	 			turntimer=0;
	 			bshow = false;
	 			power = 0;
	 			isr = false;
	 			lchd = false;
	 			player.makestatic();
	 			int i = players.indexOf(player, true);
	 			player = players.get(i<players.size-1?i+1:0);
	 		}
	 		game_timer-=delta;
	 		if(game_timer<0) game_timer=0;
	 		real_game_timer[0] = ((int)game_timer)/60;
	 		real_game_timer[1] = ((int)game_timer)%60;
	 		if(game_timer==0) {
	 			ended = true;
	 			int max = 0;
	 			for(int i=0;i<players.size;i++) {
	 				Player pl = players.get(i);
	 				if(pl.score>max) {
	 					winner = i+1;
	 				}
	 			}
	 			for(Actor a:table.getChildren()) {
	 				a.setVisible(false);
	 			}
	 			table.row();
	 			table.add(new Label("Winner is ",lsty)).padLeft(550).padTop(400);
	 			LabelStyle lsty2 = new LabelStyle(lsty);
	 			lsty2.fontColor = plclr[winner-1];
	 			Label result = new Label(plnames[winner-1],lsty2);
	 			table.add(result).bottom();
	 		}
	 		turntimer+=delta;
	 	}
		private void clipsupdate() {
			for(int i=0;i<clips.size;i++) {
				clipCL cl = clips.get(i);
				if(cl.des) {
					if(cl.destroy()) {
						clips.removeIndex(i);
						i--;
					}
				}
				else {
					if(cl.frame_count<10) {
						cl.frame_count++;
					}
					else {
						cl.des = true;
					}
				}
			}
		}
		private void plUpdate() {
			for(int i=0;i<players.size;i++) {
				Player pl = players.get(i);
				if(pl.res||pl.des) {
					if(pl.res) {
						pl.res = !pl.respawn();
						pl.makestatic();
						if(pl == player) {
							pl.incon = false;
						}
					}
					else {
						if(pl.destroy()) {
							while(!players.removeValue(pl,true));
							if(pl==player) {
								turntimer=0;
					 			bshow = false;
					 			power = 0;
					 			isr = false;
					 			player = players.get(i<players.size?i:0);
							}
							i--;
				 			continue;
						}
					}
				}
				if(Math.abs(pl.getPosition().x) >21) {
					pl.setLinearVelocity(pl.getPosition().x<0?2:-2, 0);
				}
				if(pl.getPosition().y<-10) {
					if(pl.health>3/60f) pl.health -= 3/60f;
					else pl.health = 0;
					pl.body.applyForceToCenter(0,Math.min(0.65f,(-10-pl.getPosition().y)*0.25f)*9.8f, true);
					if(pl.body.getLinearDamping()<1) pl.body.setLinearDamping(1);
					pl.updateHb();
				}
				else {
					if(pl.body.getLinearDamping()==1) pl.body.setLinearDamping(0.3f);
				}
				pl.sprUpd();
			}
			if(bshow) {
				Vector2 vec = new Vector2(Gdx.input.getX()-(Gdx.graphics.getWidth()/2),(Gdx.graphics.getHeight()/2)-Gdx.input.getY());
				vec.sub(player.getPosition().x*Constants.PPM,player.getPosition().y*Constants.PPM);
				vec.scl(1/32f);
				vec.scl(1.5f/vec.len());
				float an = vec.angleDeg();
				vec.add(player.getPosition());
				powsp.setCenter(vec.x ,vec.y);
				powsp.setOriginCenter();
				powsp.setRotation(an);
			}
		}
		public void jumpUpdate() {
			if(isr) {
				player.playersp.setRegion(rghty[r/6]);
				r++;
				r%=18;
			}
			else{
				player.playersp.setRegion(lefty[l/6]);
				l++;
				l%=18;
			}
		}
		private void inputUpdate() {
//			if(Gdx.input.justTouched()) {
//				int x = Gdx.input.getX();
//				int y = 900 - Gdx.input.getY();
//				x-=600;
//				y-=450;
//				System.out.print((x/32f)+"f,"+(y/32f)+"f,");
//			}
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
				player.applyForceToCenter(-2,0, true);
				player.playersp.setRegion(lefty[0]);
				isr = false;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				player.applyForceToCenter(2,0, true);
				player.playersp.setRegion(rghty[0]);
				isr = true;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
				if(player.incon) {
				player.setLinearVelocity(0,10);
				player.incon = false;
				}
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
				player.applyForceToCenter(0,-1, true);
				player.incon = true;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				bshow = !bshow;
			}
			if(bshow && !lchd) {
				if(Gdx.input.isTouched()) {
					if(power<19) {
					power+=0.4f;
					powsp.setRegion(pow[((int)power)/2]);				
					}
				}
				else {
					if(power>0) {
						LaunchBomb();
						turntimer +=8;
						lchd = true;
						if(turntimer>20) turntimer = 20;
						power = 0;
						powsp.setRegion(pow[0]);
					}
				}
			}
		}

		private void clipeff() {
			cookie ck = (cookie)grnd;
			for(int i=0;i<ck.destroyqueue.size;i++) {
				if(ck.destroy(ck.destroyqueue.get(i))) {
					ck.destroyqueue.removeIndex(i);
					i--;
				}
			}
			Array<float[]> polysfinal = ck.polysfinal;
			Array<PNadv> polys = new Array<PNadv>();
			for(int i=0;i<ck.createqueue.size;i++) {
				PNadv curPoly = ck.createqueue.get(i);
				while(curPoly!=null) {
					PNadv nextPoly = curPoly.nextPoly;
					Decomp.decomp(curPoly, polys);
					curPoly = nextPoly;
				}
			}
			if(ck.createqueue.size!=0) ck.createqueue.removeRange(0, ck.createqueue.size-1);
			for(int i=0;i<polys.size;i++) {
				PNadv poly = polys.get(i);
				polysfinal.add(cookie.PNtoarr(poly));
			}
			ck.createfixtures();
		}
		private void bombupd() {
			for(int i=0;i<bombs.size;i++) {
				Bomb b = bombs.get(i);
				if(b.des) {
					Vector2 vec = b.getPosition().cpy();
					if(b.destroy()) {
						bombs.removeIndex(i);
						i--;
						clipCL cl = new clipCL(vec,clipsp,clip,world);
						cl.pl = b.pl;
						clips.add(cl);
					}
				}
				else {
					Vector2 pos = b.getPosition();
					if(pos.x<-50 || pos.x>100|| pos.y<-50) {
						b.des = true;
					}
					Vector2 vec = b.body.getLinearVelocity().cpy();
					vec.scl(1/vec.len());
					Vector2 vec2 = new Vector2(1,0);
					vec2.setAngleRad(b.body.getAngle());
					b.body.applyTorque(vec2.crs(vec), true);
					b.updateSprite();
				}
			}
		}
		private Player createPlayer() {
			if(players.size==5) {
				return player;
			}
			BodyDef def = new BodyDef();
			def.type = BodyType.DynamicBody;
			def.position.set(spwpos[2*players.size],spwpos[2*players.size+1]);
			def.fixedRotation = true;
			FixtureDef fdef = createFixdef(0.5f,1.3f,0.2f);
			Player pl = new Player(world,def, fdef,strt,hbs[players.size]);
			pl.id = "player";
			players.add(pl);
			pl.body.setLinearDamping(0.3f);
			return pl;
		}

		private void LaunchBomb() {
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;
			Vector2 vec = new Vector2(powsp.getWidth()/2,powsp.getHeight()/2);
			vec.add(powsp.getX(),powsp.getY());
			bdef.position.set(vec);
			bdef.angle = (float)Math.toRadians(powsp.getRotation());
			bdef.fixedRotation = false;
			FixtureDef fdef = createFixdef(1f,0f,1f);
			Bomb bomb = new Bomb(world,"bomb.xml",bombtex,bdef, fdef, 0.001f,1024,1024);
			bomb.id = "bomb";
			bomb.body.setAngularDamping(10);
			vec.sub(player.getPosition());
			bomb.body.setLinearVelocity(vec.scl(0.7f*power));
			bomb.pl = player;
			bombs.add(bomb);
		}
		private void createPlatform() {
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.StaticBody;
			bdef.fixedRotation = true;
			bdef.position.set(0,-20);
			FixtureDef fdef = createFixdef(1f,0f,0.1f);
			PolygonShape shp = new PolygonShape();
			shp.setAsBox(100, 2);
			fdef.shape = shp;
			world.createBody(bdef).createFixture(fdef);
			shp.dispose();
		}
		private Abody createGrnd() {
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.StaticBody;
			bdef.position.x = 0;
			bdef.position.y = 0;
			FixtureDef fdef = createFixdef(1,1.3f,0.3f);
			cookie ck =  new cookie(world,"output2.xml",bdef,fdef,1/32f,1200,900);
			ck.id = "grnd";
			return ck;
		}
		private FixtureDef createFixdef(float den,float fric,float rest) {
			FixtureDef fdef = new FixtureDef();
			fdef.density = den;
			fdef.friction = fric;
			fdef.restitution = rest;
			return fdef;
		}

		private void drawgrnd() {
			Gdx.gl.glEnable(GL20.GL_DEPTH_TEST); 
			createMask();
		    /* Enable RGBA color writing. */
		    Gdx.gl.glColorMask(true, true, true, true);

		    /* Set the depth function to EQUAL. */
		    Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

		    /* Render masked elements. */
		    batch.begin();
		    grndsp.draw(batch);
		    batch.end();
		    
		    /* Disable depth writing. */
		    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		    
		}
		private float[] pstovert(PolygonShape shp) {
			int len = shp.getVertexCount();
			float[] vert = new float[2*len];
			Vector2 vec = new Vector2();
			for(int i=0;i<len;i++) {
				shp.getVertex(i, vec);
				vert[2*i] = vec.x;
				vert[2*i+1] = vec.y;
			}
			return vert;
		}
		
		private void createMask() {
			/* Clear our depth buffer info from previous frame. */
		    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		    /* Set the depth function to LESS. */
		    Gdx.gl.glDepthFunc(GL20.GL_LESS);

		    /* Enable depth writing. */
		    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		    /* Disable RGBA color writing. */
		    Gdx.gl.glColorMask(false, false, false,false);
		    /* Render mask elements. */
		    shapeRenderer.setProjectionMatrix(camera.combined);
		    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		    // Draw the mask (e.g., a circular mask or polygon mask)
		    for(Fixture f : grnd.body.getFixtureList()) {
		        PolygonShape shp = (PolygonShape)f.getShape();
		        float[] vert = pstovert(shp);
		        int i = 4;
		        while (i < vert.length) {
		            shapeRenderer.triangle(vert[0], vert[1], vert[i-2], vert[i-1], vert[i], vert[i+1]);
		            i += 2;
		        }
		    }
		    
		    shapeRenderer.end();
		}
		private void doPhysicsStep(float deltaTime) {
		    // fixed time step
		    // max frame time to avoid spiral of death (on slow devices)
		    float frameTime = Math.min(deltaTime, 0.25f);
		    accumulator += frameTime;
		    while (accumulator >= 1/60f) {
		        world.step(1/60f, 6, 2);
		        accumulator -= 1/60f;
		    }
		}
}
