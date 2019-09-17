package study.statemachine;

public class MultiMethods {
    public static class Context{
        private State state;
        public Context(State state){
            this.state = state;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }
        public void cool(){
            state.cool(this);
        }
        public void power(){
            state.power(this);
        }
    }
    public static interface State{
        public void cool(Context context);
        public void power(Context context);
    }
}
