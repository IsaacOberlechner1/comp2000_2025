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
    List<Observer> observers = new ArrayList<>();
    Grid placeholderGrid = new Grid();

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

    public void parseData(String[] stream, Color weatherColour) {

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
        System.out.println("Step 2. Colour is " + weatherColour);
        notifyObservers(data, weatherColour);
    }

    public void pullStream() {
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
                        .filter(pieces -> !pieces[3].contains("-") && !pieces[2].contains("-"))
                        .limit(100)
                        
                        .forEach( ( pieces -> {
                            if(Float.parseFloat(pieces[4]) > 0.1){
                                if(pieces[1].equals("rain")){
                                    //System.out.println("Step 1. rain coming");
                                    parseData(pieces, Color.BLUE.darker());
                                } else if (pieces[1].equals("windy") || pieces[1].equals("windx")) {
                                    //System.out.println("Step 1. wind coming");
                                    parseData(pieces, Color.GRAY.darker());
                                } else if (pieces[1].equals("temp")) {
                                    //System.out.println("Step 1. heat coming");
                                    parseData(pieces, Color.ORANGE.darker());
                                } else {
                                    // do nothing
                                }
                            }
                        }) );
                    } catch (IOException e) {
                        System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
                    }
                });
    }
}
