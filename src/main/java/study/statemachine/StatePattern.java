package study.statemachine;

import rx.internal.util.unsafe.Pow2;

public class StatePattern {
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
        public void transition(Event event){
            state.handle(this,event);
        }
    }
    public interface State{
        void handle(Context context,Event event);
    }
    public static class OffState implements State{

        @Override
        public void handle(Context context, Event event) {
            if(event == Event.POWER){
                context.setState(new RunningState());
            }
        }
    }
    public static class RunningState implements State{

        @Override
        public void handle(Context context, Event event) {
            if(event == Event.POWER){
                context.setState(new OffState());
            }else{
                context.setState(new CoolState());
            }
        }
    }
    public static class CoolState implements State{

        @Override
        public void handle(Context context, Event event) {
            if(event==Event.POWER){
                context.setState(new OffState());
            }
        }
    }
}
