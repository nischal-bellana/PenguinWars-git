package game_states;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class StartState extends State {
	
	//Batch, Camera
	SpriteBatch batch;
	OrthographicCamera camera;
	
	//Texture Regions
	TextureAtlas atlas;
	Animation<TextureRegion> anim;
	AtlasRegion playup;//<a href="https://www.flaticon.com/free-icons/play-button" title="play button icons">Play button icons created by Alfredo Hernandez - Flaticon</a>
	AtlasRegion playdown;
	AtlasRegion pink;
	AtlasRegion green;
	AtlasRegion yellow;
	AtlasRegion buttondown;
	AtlasRegion buttonup;
	
	//scene2d
	Stage stage;
	Table table;
	Label label1;
	Label label2;
	Label label3;
	Slider sl;
	Slider sl2;
	
	//fonts
	BitmapFont font30;
	
	//primitives
	boolean animfinished = false;
	float state_time=0;
	public int num = 2;
	public int game_time = 1;
	
	StartState(){
		create();
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		//SpriteBatch and camera
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,1200, 900);
		
		//Viewport
		vp = new FitViewport(1200, 900,camera);
		vp.update(1400,980);
		
		//Texture Atlas
		atlas = new TextureAtlas("game-imgs-packed//pack.atlas"); //Sprite sprite = atlas.createSprite("otherimagename");
		texInit();
		
		//fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rebellion-squad-font/RebellionSquad-ZpprZ.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		font30 = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		//Stage and widgets
		stageInit();
		
	    anim = new Animation<TextureRegion>(0.0833f, atlas.findRegions("anim/missile"), PlayMode.NORMAL);
	}

	@Override
	protected void resize(int width,int height) {
		// TODO Auto-generated method stub
		vp.update(width, height,true);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 1);//default background
		
		float delta = Gdx.graphics.getDeltaTime();
		
		state_time += delta;
		TextureRegion frame = anim.getKeyFrame(state_time, false);
		if(!animfinished && anim.isAnimationFinished(state_time) ) {
			animfinished = true;
			table.setVisible(true);
		}
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batchRen(frame);
		
		stageUpd(delta);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		font30.dispose();
		if(gsm.next_st!=null)  return;
		batch.dispose();
		atlas.dispose();
	}
	
	private void texInit() {
		playup = atlas.findRegion("playup");
		playdown = atlas.findRegion("playdown");
		pink = atlas.findRegion("pink");
		buttondown = atlas.findRegion("buttondown");
		buttonup = atlas.findRegion("buttonup");
		green = atlas.findRegion("green");
		yellow = atlas.findRegion("yellow");
	}
	
	public void stageInit() {
		//Stage
		stage = new Stage();
		stage.setViewport(vp);
		Gdx.input.setInputProcessor(stage);
		
		//root Table
		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.setDebug(false);
		//Button
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(playup);
		style.over = style.down = new TextureRegionDrawable(playdown);
		style.font = font30;
		style.fontColor = Color.GRAY;
		
		TextButton button = new TextButton("PLAY", style);
		table.add(button).padLeft(100);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				changest();
			}
		});
		LabelStyle lsty = new LabelStyle();
		lsty.font = font30;
		lsty.fontColor = Color.WHITE;
		
		label1 = new Label("No of Players",lsty);
		table.row();
		table.add(label1).padTop(100);
		
		SliderStyle slsty = new SliderStyle();
		slsty.background = new TextureRegionDrawable(buttonup);
		slsty.backgroundDown = new TextureRegionDrawable(buttondown);
		slsty.backgroundOver = new TextureRegionDrawable(green);
		slsty.knob = new TextureRegionDrawable(pink);
		slsty.knobAfter = new TextureRegionDrawable(yellow);
		
		sl = new Slider(2, 5, 1, false, slsty);
		table.row();
		table.add(sl).width(300);
		
		label2 = new Label("2",lsty);
		table.add(label2).padLeft(20);
		
		table.row();
		table.add(new Label("Game Time",lsty));
		
		table.row();
		sl2 = new Slider(1,30,1,false,slsty);
		table.add(sl2).width(300);
		
		label3 = new Label("1",lsty);
		table.add(label3).padLeft(20);
		table.add(new Label(" mins",lsty));
		
		table.setVisible(false);
	}
	
	private void stageUpd(float delta){
		num = (int)sl.getValue();
		label2.setText(num);
		
		game_time = (int)sl2.getValue();
		label3.setText(game_time);
		
		stage.act(delta);
		stage.draw();
	}
	
	private void batchRen(TextureRegion frame) {
		batch.begin();
		batch.draw(frame,0,0);
		batch.end();
	}
	
	private void changest() {
		gsm.next_st = new GameDevState(this);
	}
}
