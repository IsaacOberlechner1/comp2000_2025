import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherStation implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private Grid placeholderGrid = new Grid();
    private float threshold = 0.3f;

    public void registerObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(WeatherData data, Color weatherColour){
        for(Observer observer: observers) {
            observer.update(data, weatherColour);
        }
    }

    // turns weather stream into a WeatherData object and passes the weather colour
    public void parseData(String[] stream, Color weatherColour) {

        // convert the weather stream into appropriate types
        int time = Integer.parseInt(stream[0]);
        int locX = Integer.parseInt(stream[2]);
        int locY = Integer.parseInt(stream[3]);
        Cell locCell = new Cell(
            placeholderGrid.cellAtColRow(locX, locY).get().col, 
            placeholderGrid.cellAtColRow(locX, locY).get().row, 
            placeholderGrid.cellAtColRow(locX, locY).get().x, 
            placeholderGrid.cellAtColRow(locX, locY).get().y
        );
        float strength = Float.parseFloat(stream[4]);

        WeatherData data = new WeatherData(time, stream[1], locCell, strength);
        notifyObservers(data, weatherColour);
    }

    public void pullStream() {
        // String[] proxyData = {"1028352386", "rain", "5", "5", "0.45"};
        // String[] proxyData2 = {"1028352386", "windX", "2", "15", "0.50"};
        // String[] proxyData3 = {"1028352386", "windY", "3", "7", "0.55"};
        // String[] proxyData4 = {"1028352386", "temp", "8", "13", "0.60"};
        // String[] proxyData5 = {"1028352386", "temp", "5", "5", "0.55"};
        // parseData(proxyData, Color.BLUE.darker());
        // parseData(proxyData2, Color.GRAY.darker());
        // parseData(proxyData3, Color.GRAY.darker());
        // parseData(proxyData4, Color.ORANGE.darker());
        // parseData(proxyData5, Color.ORANGE);


        HttpClient client = HttpClient.newHttpClient(); // creating a HTTP client
        HttpRequest request = HttpRequest.newBuilder() // creating a request to the server
                .uri(URI.create("http://13.238.167.130/weather"))
                .header("Accept", "text/event-stream")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()) // sending the request
            .thenApply(HttpResponse::body) // get the data
            .thenAccept(inputStream -> { // partition the stream
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) { // read the input stream and turn it into characters
                    reader.lines()
                        .map( s -> s.split(" "))
                        .limit(100) // limit the stream by 100
                        .forEach( ( pieces -> { 
                            // turn negative x & y coordinates into positive ones
                            int locationX = Integer.parseInt(pieces[2]);
                            int locationY = Integer.parseInt(pieces[3]);
                            if(locationX < 0) {
                                locationX *= -1;
                                pieces[2] = "" + locationX;
                            }

                            if(locationY < 0) {
                                locationY *= -1;
                                pieces[3] = "" + locationY;
                            }

                            // send weather event if the strength is over the threshold (tunable parameter)
                            //if(Float.parseFloat(pieces[4]) > threshold){
                                switch (pieces[1]) {
                                    case "rain": // it's raining
                                        pieces[1] = "raining";
                                        parseData(pieces, Color.BLUE.darker());
                                        break;
                                    case "windy": // it's windy
                                    case "windx":
                                        pieces[1] = "windy";
                                        parseData(pieces, Color.GRAY.darker());
                                        break;
                                    case "temp": // it's hot
                                        pieces[1] = "hot";
                                        parseData(pieces, Color.ORANGE.darker());
                                        break;
                                // do nothing
                                    default:
                                        break;
                                }
                            //}
                        }) );
                    } catch (IOException e) {
                        System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
                    }
                });
    }
}
