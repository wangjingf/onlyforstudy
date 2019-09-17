package study.statemachine;

public class SwitchScene {
    State currentState;
    public void changeState(Event event){
        if (currentState == State.OFF){
            if(event == Event.POWER){
                currentState = State.RUNNING;
            }
        }else if(currentState == State.RUNNING){
            if(event == Event.POWER){
                currentState = State.OFF;
            }else{
                currentState = State.COOL;
            }
        }else{
            if(event == Event.POWER){
                currentState = State.OFF;
            }
        }
        throw new RuntimeException("invalid transition");
    }
}
