import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {
    List<Observer> observers = new ArrayList<>();
    private int time;
    private String event;

    public void registerObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(){
        for(Observer observer: observers) {
            observer.update();
        }
    }
}
