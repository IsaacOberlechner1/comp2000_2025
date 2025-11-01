import java.awt.Color;

public interface Subject {
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObservers(WeatherData data, Color weatheColor);
}
