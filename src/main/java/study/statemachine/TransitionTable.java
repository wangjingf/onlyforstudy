package study.statemachine;

import java.util.ArrayList;
import java.util.List;

public class TransitionTable {
    List<Transfor> transforTable = new ArrayList<>();
    public void init(){
        transforTable.add(Transfor.of(State.RUNNING,Event.POWER,State.OFF,null));
        transforTable.add(Transfor.of(State.RUNNING,Event.COOL,State.COOL,null));
        transforTable.add(Transfor.of(State.OFF,Event.POWER,State.RUNNING,null));
        transforTable.add(Transfor.of(State.COOL,Event.POWER,State.OFF,null));
    }
    public static class Transfor{
        State startState;
        Event event;
        State nextState;
        Runnable doAction;

        public static Transfor of(State startState, Event event, State nextState, Runnable doAction) {
            Transfor ret = new Transfor();
            ret.startState = startState;
            ret.event = event;
            ret.nextState = nextState;
            ret.doAction = doAction;
            return ret;
        }
    }
}
