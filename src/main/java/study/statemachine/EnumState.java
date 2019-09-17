package study.statemachine;

public class EnumState {
    public static class EnumContext{
        StateEnum state;

        public EnumContext(StateEnum state) {
            this.state = state;
        }

        public StateEnum getState() {
            return state;
        }

        public void setState(StateEnum state) {
            this.state = state;
        }
        public void transition(Event event){
            state.handle(this,event);
        }
    }
    public static enum StateEnum{
        OFF{
            @Override
            void handle(EnumContext context, Event event) {

            }
        },
        RUNNING{
            @Override
            void handle(EnumContext context, Event event) {

            }
        },
        COOL{
            @Override
            void handle(EnumContext context, Event event) {

            }
        };
        abstract  void handle(EnumContext context,Event event);
    }
}
