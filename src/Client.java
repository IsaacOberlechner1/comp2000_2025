import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
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
                            .forEach( ( pieces -> {
                                /*
                                * This will be the logic to check the weather type, cell location, and strenght of the weather event
                                */
                                Grid g = new Grid();
                                Cell c = g.cellAtColRow(Integer.parseInt(pieces[2]), Integer.parseInt(pieces[3])).get();

                                //System.out.println(pieces[1].equals("rain"));

                                if(Float.parseFloat(pieces[4]) > 0.5){
                                    System.out.println("Strong weather at..." + c.col + c.row);
                                    //System.out.println("Rain? " + pieces[1].equals("rain"));
                                    if(pieces[1].equals("rain")){
                                        System.out.println("It's raining at " + c.col + c.row);
                                    } else if (pieces[1].equals("windy") || pieces[1].equals("windx")) {
                                        System.out.println("It's windy at " + c.col + c.row);
                                    } else if (pieces[1].equals("temp")) {
                                        System.out.println("It's hot at " + c.col + c.row);
                                    } else {
                                        // do nothing
                                    }
                                    System.out.println("");
                                }
                                // if (pieces[1].equals("rain") && Float.parseFloat(pieces[4]) > 0.5) {
                                //     System.out.println("It's raining heavy");
                                // } else if ((pieces[1] == "windx" || pieces[1] == "windy"  ) && Float.parseFloat(pieces[4]) > 0.5) {
                                //     System.out.println("High winds occuring");
                                // } else if (pieces[1] == "temp" && Float.parseFloat(pieces[4]) > 0.5) {
                                //     System.out.println("Heat wave");
                                // } else {
                                //     System.out.println(Float.parseFloat(pieces[4]) > 0.5);
                                    // System.out.println(pieces[1].equals("windx"));
                                    // System.out.println(pieces[1].equals("windy"));
                                    // System.out.println(pieces[1].equals("temp"));
                                //}
                        }) );
                    } catch (IOException e) {
                        System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
                    }
                })
                .join(); // Wait for the async operation to complete
    }

    }
