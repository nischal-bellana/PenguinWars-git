package game_states;

public class GameStateManager {
	public State st;
	public State next_st;
	public GameStateManager() {
		boolean x = true;
		if(x) {
			st = new StartState();
		}
		else {
			st = new GameDevState();
		}
		st.gsm = this;
	}
	public GameStateManager(State st) {
		this.st = st;
		st.gsm = this;
	}
	
	public void render() {
		if(next_st==null) {
			st.render();
		}
		else {
			st.dispose();
			st = next_st;
			next_st = null;
		}
	}
	public void dispose() {
		st.dispose();
		if(next_st!=null) next_st.dispose();
	}
	
	public void resize(int width,int height) {
		st.resize(width, height);
	}
}
