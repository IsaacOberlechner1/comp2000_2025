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
                            .filter(pieces -> pieces[1].equals("rain"))
                            .forEach( ( pieces -> {
                                /*
                                * This will be the logic to check the weather type, cell location, and strenght of the weather event
                                */
                                System.out.println("-- New Weather Event --");
                                System.out.println("    Time: " + pieces[0]);
                                System.out.println("    Event: " + pieces[1]);
                                System.out.println("    Location: " + " X = " + pieces[2] + ", Y = " + pieces[3]);
                                System.out.println("    Strength: " + pieces[4]);
                                System.out.println(" ");
                        }) );
                    } catch (IOException e) {
                        System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
                    }
                })
                .join(); // Wait for the async operation to complete
    }
}

