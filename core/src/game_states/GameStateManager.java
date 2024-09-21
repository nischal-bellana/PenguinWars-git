package game_states;

public class GameStateManager {
	public State st;
	public State next_st;
	public GameStateManager() {
		boolean x = false;
		if(x) {
			st = new StartState(this);
		}
		else {
			st = new GameDevState(this);
		}
		st.gsm = this;
	}
	public GameStateManager(State st) {
		this.st = st;
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
}
