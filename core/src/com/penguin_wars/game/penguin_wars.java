package com.penguin_wars.game;





import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;

import game_states.GameStateManager;

public class penguin_wars extends ApplicationAdapter {
	
	public static GameStateManager gsm;
	@Override
	public void create () {
		gsm = new GameStateManager();
	}
	@Override
	public void render () {
		gsm.render();
	}
	@Override
	public void dispose () {
		gsm.dispose();
	}

}
