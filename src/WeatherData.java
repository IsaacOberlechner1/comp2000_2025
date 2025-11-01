public class WeatherData {
    int time;
    String event;
    Cell location;
    float strength;

    public WeatherData(int time, String event, Cell location, float strength) {
        this.time = time;
        this.event = event;
        this.location = location;
        this.strength = strength;
    }
}
