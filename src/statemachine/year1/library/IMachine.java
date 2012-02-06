package statemachine.year1.library;

import java.util.Observer;

public interface IMachine {

    public void initialize();

    public void processEvent(Event event);

    public void addObserver(Observer observer);

    public String getStateName();
}