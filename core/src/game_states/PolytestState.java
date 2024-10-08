package game_states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener.FocusEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import myutils.Clipping;
import myutils.Decomp;
import myutils.PNadv;
import myutils.phelp;
import myutils.PNadv;

public class PolytestState extends State{
	//Spritebatch and camera
	SpriteBatch batch;
	OrthographicCamera camera;
	//Texture Regions
	TextureAtlas atlas;
	AtlasRegion red;
	AtlasRegion cross;
	AtlasRegion viol;
	AtlasRegion blackcross;
	AtlasRegion cursor;
	AtlasRegion buttonup;
	AtlasRegion buttondown;
	//Fonts
	BitmapFont font20;
	//Scene2d
		//all modes
		//mode1
	Stage stage1;
	Table table1;
	Label label1;
	Label label3;
		//mode 2
	Stage stage2;
	Table table2;
	Label label2;
	Label label4;
		//mode 3
	Stage stage3;
	Table table3;
	//Sprites
		//all modes
	Array<Sprite> cmn_spr;
		//mode 1
	Array<Sprite> md1_spr;
	//Points and polys
		//all modes
	Array<PNadv> polys;
	PNadv curPoly;
		//mode 1
	PNadv curs;
	PNadv prep;
		//mode 2
	PNadv curs2;
	//Extra
		//mode1
	Vector2 vec;
	Vector2 prev;
	//primitives
		//all modes
	int mode = 0;
	boolean asc = true;
		//mode 1
	boolean md1_i = false;
	boolean polyst = false;
	boolean validadd = true;
	int polys_size = 0;
	boolean md1inplock = false;
		//mode 2
	boolean md2_i = false;
	boolean navpoly = false;
		//mode 3
	boolean md3_i = false;
	//Constructors
	public PolytestState() {
		create();
	}
	@Override
	public void create() {
		//batch and camera
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//fonts
		fontsInit();
		//Texture regions
		txrgInit();
		//Sprites
		sprInit();
		//all modes init
		cmnInit();
		//default console actions
		defConsole();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batchRen();
		stageRen(Gdx.graphics.getDeltaTime());
		inpUpd();
		modeUpd();
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
		if(stage2!=null) stage2.dispose();
		if(stage1!=null) stage1.dispose();
		if(stage3!=null) stage3.dispose();
		font20.dispose();
	}
	public void txrgInit() {
		atlas = new TextureAtlas("game-imgs-packed/pack.atlas");
		red = atlas.findRegion("red");
		cross = atlas.findRegion("cross");
		viol = atlas.findRegion("viol");
		blackcross = atlas.findRegion("blackcross");
		cursor = atlas.findRegion("cursor");
		buttonup = atlas.findRegion("buttonup");
		buttondown = atlas.findRegion("buttondown");
	}
	public void sprInit() {
		//all modes
		cmn_spr = new Array<Sprite>();
		//mode 1
		md1_spr = new Array<Sprite>();
	}
	public void cmnInit() {
		polys = new Array<PNadv>();
	}
	public void defConsole() {
		System.out.println("default mode");	}
	public void batchRen(){
		batch.begin();
		switch(mode) {
		case 1:
			for(Sprite sp: md1_spr) {
				sp.draw(batch);
			}
			drawpoly(curPoly);
			curs.p_spr.draw(batch);
			break;
		default:
		}
		for(PNadv poly:polys) {
			drawpoly(poly);
		}
		for(Sprite sp: cmn_spr) {
			sp.draw(batch);
		}
		batch.end();
	}
	public void inpUpd() {
		if(asc && mode!=3 && Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			System.out.println("mode 3");
			mode = 3;
			if(!md3_i) {
				md3Init();
				md3_i = true;
			}
			Gdx.input.setInputProcessor(stage3);
		}
		if(asc && mode!=2 && Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			System.out.println("mode 2");
			mode = 2;
			if(!md2_i) {
				md2Init();
				md2_i = true;
			}
			if(polys.size!=0) curs2 = curPoly = polys.first();
			navpoly = false;
			Gdx.input.setInputProcessor(stage2);
		}
		if(asc && mode!=1 && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			System.out.println("mode 1");
			mode = 1;
			if(!md1_i) {
				md1Init();
				md1_i = true;
			}
			curPoly = null;
			Gdx.input.setInputProcessor(stage1);
		}
		if(asc && mode!=0 && Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
			System.out.println("default mode");
			mode = 0;
		}
		
		switch(mode) {
			case 1:
				if(!md1inplock) md1Inp();
				break;
			case 2:
				md2Inp();	
				break;
			case 3:
				md3Inp();
				break;
		}
	}
	private void md3Init() {
		stage3 = new Stage();
		
		table3 = new Table();
		stage3.addActor(table3);
		table3.padBottom(100);
		table3.bottom();
		table3.setFillParent(true);
		table3.setDebug(true);
		table3.setTouchable(Touchable.enabled);
		
		TextFieldStyle tfsty = new TextFieldStyle();
		tfsty.font = font20;
		tfsty.fontColor = Color.CYAN;
		tfsty.cursor = new TextureRegionDrawable(cursor);
		
		TextField tf = new TextField("",tfsty);
		table3.add(tf);
		
		TextButtonStyle tbsty = new TextButtonStyle();
		tbsty.font = font20;
		tbsty.up = new TextureRegionDrawable(buttonup);
		tbsty.down = new TextureRegionDrawable(buttondown);
		
		TextButton tb = new TextButton("gen Circle",tbsty);
		table3.row();
		table3.add(tb);
		
		table3.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				if(event.getTarget()!= stage3.getKeyboardFocus()) {
					asc = true;
					stage3.unfocus(stage3.getKeyboardFocus());
				}
				if(event.getTarget() instanceof TextField) {
					asc = false;
				}
				return false;
			}
			
		});
		tb.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				try {
					float f = Float.parseFloat(tf.getText());
					if(f>0) {
						Vector2 pos = new Vector2();
						pos.set(600,450);
						PNadv head = Clipping.genCir(new Vector2(),pos, f);
						if(polys.size>0) {
						PNadv last = polys.get(polys.size-1);
						last.nextPoly = head;
						head.prevPoly = last;
						}
						polys.add(head);
					}	
				}
				catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	private void md3Inp() {
	}
	private void md2Inp() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			navpoly = !navpoly;
			curs2 = curPoly;
		}
		if(navpoly) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.K) && curPoly!=null) {
				curs2 = (PNadv)curs2.next;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.J) && curPoly!=null) {
				curs2 = (PNadv)curs2.prev;
			}
			return;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.K) && curPoly!=null) {
			if(curPoly.nextPoly!=null) {
				curPoly = curPoly.nextPoly;
				curs2 = curPoly;
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.J) && curPoly!=null) {
			if(curPoly.prevPoly!=null) {
				curPoly = curPoly.prevPoly;
				curs2 = curPoly;
			}
		}
		
	}
	public void md1Init(){
		curs = new PNadv(cross);
		stage1 = new Stage();
		
		table1 = new Table();
		stage1.addActor(table1);
		table1.setFillParent(true);
		table1.setDebug(false);
		table1.addListener(new ClickListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				// TODO Auto-generated method stub
				md1inplock = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				// TODO Auto-generated method stub
				md1inplock = false;
			}
			
		});
		
		LabelStyle lsty = new LabelStyle();
		lsty.font = font20;
		lsty.fontColor = Color.GOLD;
		
		Label label = new Label("angle: ",lsty);
		table1.add(label).left();
		
		label1 = new Label("",lsty);
		table1.add(label1).left();
		table1.bottom().padBottom(50);
		vec = new Vector2();
		prev = new Vector2();
		
		Label label2 = new Label("Clockwise: ",lsty);
		table1.row();
		table1.add(label2).left();
		
		label3 = new Label("No Polys",lsty);
		label3.setColor(Color.RED);
		table1.add(label3).left();
		
		TextButtonStyle tbsty = new TextButtonStyle();
		tbsty.font = font20;
		tbsty.up = new TextureRegionDrawable(buttonup);
		tbsty.down = new TextureRegionDrawable(buttondown);
		tbsty.fontColor = Color.BLACK;
		
		TextButton button = new TextButton("Decomp",tbsty);
		table1.row();
		table1.add(button).padLeft(100);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				if(polys.size!=0) {
					PNadv head = polys.removeIndex(polys.size-1);
					refresh(head);
					Decomp.decomp(head, polys);
				}
			}
			
		});
	}
	protected void refresh(PNadv head) {
		// TODO Auto-generated method stub
		PNadv cur = head;
		boolean first = true;
		while(cur!=head||first) {
			first = false;
			cur.l_spr = null;
			cur = (PNadv)cur.next;
		}
	}
	public void modeUpd() {
		switch(mode) {
		case 1:
			curs.setCenter(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY());
			if(!md1_spr.isEmpty()) md1_spr.removeRange(0, md1_spr.size-1);
			if(polyst) {
			Vector2 vec = new Vector2();
			vec.set((float)(curs.x-prep.x), (float)(curs.y-prep.y));
			prep.l_spr.setSize(vec.len(), 3);
			prep.l_spr.setRotation(vec.angleDeg());
			validadd = vec.len()>40;
			checkadd();
			prep.l_spr.setRegion(validadd?red:viol);
			}
			else validadd = true;
			break;
		case 2:
			
			break;
		default:
		}
	}
	public void checkadd() {
		if(curPoly.vert_count>1) {
			Vector2 vec1 = new Vector2();
			 vec1.set((float)(prep.x-prep.prev.x),(float)(prep.y-prep.prev.y));
			 Vector2 vec2 = new Vector2();
			 vec2.set((float)(curs.x-prep.x),(float)(curs.y-prep.y));
			 float d = Math.abs(vec1.angleDeg()-vec2.angleDeg());
			 if(d<5 || Math.abs(d-180)<5 || Math.abs(d-360)<5) validadd = false;
		}
		if(curPoly.vert_count<3) return;
		PNadv cur = curPoly;
		while(cur!=prep.prev) {
			double[] alpha = Clipping.ins(cur, cur.next, prep, curs);
			if(alpha[0]==-1) {
				cur = (PNadv)cur.next;
				continue;
			}
			validadd = false;
			Sprite spr = new Sprite(blackcross);
			spr.setBounds((float)(cur.x+alpha[0]*(cur.next.x-cur.x)), (float)(cur.y+alpha[0]*(cur.next.y-cur.y)), 6, 6);
			md1_spr.add(spr);
			cur = (PNadv)cur.next;
		}
	}
	public boolean checkclose() {
		if(curPoly.vert_count==3) return true;
		PNadv cur = (PNadv)curPoly.next;
		while(cur!=prep.prev) {
			double[] alpha = Clipping.ins(cur, cur.next, prep, curPoly);
			if(alpha[0]==-1) {
				cur = (PNadv)cur.next;
				continue;
			}
			return false;
		}
		return true;
	}
	public void drawpoly(PNadv head) {
		PNadv cur = head;
		boolean first = true;
		while(cur!=null && (cur!=head||first)) {
			first = false;
			if(cur.l_spr==null) {
				Sprite sp = new Sprite(red);
				Vector2 vec = new Vector2();
				vec.set((float)(cur.next.x-cur.x), (float)(cur.next.y-cur.y));
				sp.setSize(vec.len(), 3);
				sp.setOrigin(0, 1.5f);
				sp.setRotation(vec.angleDeg());
				sp.setPosition((float)cur.x+4,(float) cur.y+2.5f);
				cur.l_spr = sp;
			}
			if(mode==2) {
				if(curPoly==head) {
					cur.l_spr.setRegion(viol);
					cur.l_spr.setAlpha(1);
				}
				else {
					cur.l_spr.setRegion(red);
					cur.l_spr.setAlpha(0.3f);
				}
			}
			else {
				if(curPoly != head) {
				cur.l_spr.setRegion(red);
				cur.l_spr.setAlpha(1);
				}
			}
			cur.l_spr.draw(batch);
			cur = (PNadv)cur.next;
		}
		cur = head;
		first = true;
		while(cur!=null && (cur!=head||first)) {
			first = false;
			if(cur.p_spr==null) {
				Sprite sp = new Sprite(cross);
				sp.setSize(8, 8);
				sp.setPosition((float)cur.x,(float) cur.y );
				cur.p_spr = sp;
			}
			if(mode==2) {
				cur.p_spr.setAlpha(curPoly==head?0.4f:0.1f);
				if(curs2==cur)
					cur.p_spr.setAlpha(1);
			}
			else {
				cur.p_spr.setAlpha(1);
			}
			cur.p_spr.draw(batch);
			cur = (PNadv)cur.next;
		}
	}
	public void md1Inp() {
		if(Gdx.input.justTouched()) {
			//closing polygon
			if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				if(polyst) {
					if(curPoly.vert_count<3) return;
					if(!checkclose()) return;
					if(!polys.isEmpty()) {
						curPoly.prevPoly = polys.get(polys.size-1);
						curPoly.prevPoly.nextPoly = curPoly;
					}
					polys.add(curPoly);
					
					prep.next = curPoly;
					curPoly.prev = prep;
					
					Vector2 vec = new Vector2();
					vec.set((float)(curPoly.x-prep.x), (float)(curPoly.y-prep.y));
					prep.l_spr.setRegion(red);
					prep.l_spr.setSize(vec.len(), 3);
					prep.l_spr.setRotation(vec.angleDeg());
					prep.l_spr.setAlpha(1);
										
					curPoly = null;
					prep = null;
					asc = true;
					polyst = false;
				}
				return;
			}
			if(!validadd) return;
			//New Point
			PNadv newpoint = new PNadv(cross);
			newpoint.set(curs.x, curs.y);
			//New Side
			newpoint.l_spr = new Sprite(red);
			newpoint.l_spr.setAlpha(0.5f);
			newpoint.l_spr.setPosition((float)curs.x+4,(float) curs.y+2.5f);
			newpoint.l_spr.setOrigin(0, 1.5f);
			//Polygon start Dependent
			if(!polyst) {
				//New Polygon register
				curPoly = newpoint;
				curPoly.ishead = true;
				curPoly.xmin = curPoly.xmax = curPoly.x;
				curPoly.ymin = curPoly.ymax = curPoly.y;
				curPoly.vert_count = 1;
				polyst = true;
				asc = false;
			}
			else {
				//Linking Nodes and locking side
				prep.next = newpoint;
				newpoint.prev = prep;
				if(newpoint.x<curPoly.xmin) {
					curPoly.xmin = newpoint.x;
				}
				if(newpoint.x>curPoly.xmax) {
					curPoly.xmax = newpoint.x;
				}
				if(newpoint.y<curPoly.ymin) {
					curPoly.ymin = newpoint.y;
				}
				if(newpoint.y>curPoly.ymax) {
					curPoly.ymax = newpoint.y;
				}
				curPoly.vert_count++;
				prep.l_spr.setAlpha(1);
			}
			//updating previous newpoint
			prep = newpoint;
		}
	}
	public void md2Init(){
		stage2 = new Stage();
		
		table2 = new Table();
		stage2.addActor(table2);
		table2.bottom().padBottom(200);
		table2.setFillParent(true);
		table2.setDebug(false);
		
		LabelStyle sty = new LabelStyle();
		sty.font = font20;
		sty.fontColor = Color.CYAN;
		
		TextFieldStyle tsty = new TextFieldStyle();
		tsty.font = font20;
		tsty.fontColor = Color.WHITE;
		tsty.cursor = new TextureRegionDrawable(cursor);
		
		Label label = new Label("Vert count: ",sty);
		table2.add(label).left().padRight(100);
		
		label2 = new Label("25",sty);
		table2.add(label2).left();
		
		table2.row();
		Label label3 = new Label("Y: ",sty);
		table2.add(label3).left();
		
		label4 = new Label("25",sty);
		table2.add(label4).left();
		
		table2.setTouchable(Touchable.enabled);
		table2.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(event.getTarget()!=stage2.getKeyboardFocus()) stage2.unfocus(stage2.getKeyboardFocus());
				return false;
			}
		});
		
	}
	public void fontsInit() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rebellion-squad-font/RebellionSquad-ZpprZ.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		font20 = generator.generateFont(parameter); // font size 20 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
	}
	public void stageRen(float delta){
		switch(mode) {
		case 1:
			if(curPoly!=null && curPoly.vert_count>1) {
				label1.setColor(Color.GOLD);
				vec.set((float)(curs.x-prep.x),(float)(curs.y-prep.y));
				prev.set((float)(prep.prev.x-prep.x),(float)(prep.prev.y-prep.y));
				label1.setText((int)(vec.angleDeg()-prev.angleDeg()));
			}
			else {
				label1.setColor(Color.RED);
				label1.setText("Not enough!");
			}
			if(polys.size!=polys_size && polys.size!=0) {
				PNadv head = polys.get(polys.size-1);
				label3.setColor(Color.GOLD);
				label3.setText(phelp.isclw(head)?"True":"False");
				polys_size = polys.size;
			}
			stage1.act(delta);
			stage1.draw();
			break;
		case 2:
			label2.setText(curPoly!=null?curPoly.vert_count:0);
			stage2.act(delta);
			stage2.draw();
			break;
		case 3:
			stage3.act(delta);
			stage3.draw();
			break;
		default:
		}
	}
}
