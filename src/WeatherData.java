import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {
    List<Observer> observers = new ArrayList<>();
    private int time;
    private String event;
}
