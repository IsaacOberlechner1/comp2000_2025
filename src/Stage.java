import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
import java.util.Optional;

public class Stage {
  Grid grid;
  List<Actor> listOfPlayers;
  List<Cell> cellOverlay;
  Optional<Actor> playerInAction;

  GameState currentState;
  Beat beat;

  public Stage() {
    grid = new Grid();
    listOfPlayers = new ArrayList<Actor>();
    cellOverlay = new ArrayList<Cell>();
    playerInAction = Optional.empty();
    currentState = new ChoosingActor();
    beat = new AnimationBeat();
  }

  public void addPlayer(Actor player) {
    listOfPlayers.add(player);
    if(player.isBot()) {
      beat.punchIn(player);
    }
  }

  public void paint(Graphics g, Point mouseLoc) {
    // do we have bot moves to make?
    currentState.paint(g, this);
    grid.paint(g, mouseLoc);
    // Blue cell selection overlay with 50% transparency
    grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

    beat.ticktock();
    for(Actor player: listOfPlayers) {
      player.paint(g);
    }
    draw_sidepanel(g, mouseLoc);
  }

  private void draw_sidepanel(Graphics g, Point mouseLoc) {
    // lots of magic numbers here
    // they are used to calculate the coordinates of where to draw on the information panel
    final int hTab = 10;
    final int blockVT = 35;
    final int margin = 21*blockVT;
    int yLoc = 20;

    // state display
    g.setColor(Color.DARK_GRAY);
    g.drawString(currentState.toString(), margin, yLoc);
    yLoc = yLoc + blockVT;
    Optional<Cell> underMouse = grid.cellAtPoint(mouseLoc);
    if(underMouse.isPresent()) {
      Cell hoverCell = underMouse.get();
      g.setColor(Color.DARK_GRAY);
      String coord = String.valueOf(hoverCell.col) + String.valueOf(hoverCell.row);
      g.drawString(coord, margin, yLoc);
    }

    // agent display
    final int vTab = 15;
    final int labelIndent = margin + hTab;
    final int valueIndent = margin + 3*blockVT;
    yLoc = yLoc + 2*blockVT;
    for(int i = 0; i < listOfPlayers.size(); i++){
      Actor a = listOfPlayers.get(i);
      yLoc = yLoc + 2*blockVT;
      g.drawString(a.getClass().getName(), margin, yLoc);
      g.drawString("location:", labelIndent, yLoc+vTab);
      g.drawString(Character.toString(a.loc.col) + Integer.toString(a.loc.row), valueIndent, yLoc+vTab);
      g.drawString("player type:", labelIndent, yLoc+2*vTab);
      g.drawString(a.isBot() ? "Bot" : "Human", valueIndent, yLoc+2*vTab);
      if(a.isBot() && a.mover != null) {
        g.drawString("mover:", labelIndent, yLoc+3*vTab);
        g.drawString(a.mover.getClass().getName(), valueIndent, yLoc+3*vTab);
      }
    }    
  }

  public List<Cell> getClearRadius(Cell from, int size) {
    List<Cell> init = grid.getRadius(from, size);
    for(Actor player: listOfPlayers) {
      init.remove(player.loc);
    }
    return init;
  }

  public void mouseClicked(int x, int y) {
    currentState.mouseClick(x, y, this);
  }

  // public List<String> pullEvent() throws IOException, InterruptedException {
  //       List<String> event = new ArrayList<>(); // the event

  //       HttpClient client = HttpClient.newHttpClient(); // creating a HTTP client
  //       HttpRequest request = HttpRequest.newBuilder() // creating a request to the server
  //               .uri(URI.create("http://13.238.167.130/weather"))
  //               .header("Accept", "text/event-stream")
  //               .build();

  //       client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()) // sending the request
  //               .thenApply(HttpResponse::body) // get the data
  //               .thenAccept(inputStream -> { // partition the stream
  //                   try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) { // read the input stream and turn it into characters
  //                       reader.lines().map( s -> s.split(" ")).limit(4).forEach( ( pieces -> {
  //                           /*
  //                            * This will be the logic to check the weather type, cell location, and strenght of the weather event
  //                            */
  //                           event.add(pieces[1]);
  //                           System.out.println(pieces[1] + " added to events");
  //                           System.out.println("");
                            
  //                       }) );
  //                   } catch (IOException e) {
  //                       System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
  //                   }
  //               })
  //               .join(); // Wait for the async operation to complete
  //               return event;
  //   }


  //   public List<Float> pullStrength() throws IOException, InterruptedException {
  //       List<Float> strength = new ArrayList<>(); // the event

  //       HttpClient client = HttpClient.newHttpClient(); // creating a HTTP client
  //       HttpRequest request = HttpRequest.newBuilder() // creating a request to the server
  //               .uri(URI.create("http://13.238.167.130/weather"))
  //               .header("Accept", "text/event-stream")
  //               .build();

  //       client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()) // sending the request
  //               .thenApply(HttpResponse::body) // get the data
  //               .thenAccept(inputStream -> { // partition the stream
  //                   try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) { // read the input stream and turn it into characters
  //                       reader.lines().map( s -> s.split(" "))
  //                         .limit(4)
  //                         .forEach( ( pieces -> {
  //                           /*
  //                            * This will be the logic to check the weather type, cell location, and strenght of the weather event
  //                            */
  //                           float value = Float.parseFloat(pieces[4]);
  //                           strength.add(value);
  //                           System.out.println(pieces[4] + " added to strength");
  //                           System.out.println("");
                            
  //                       }) );
  //                   } catch (IOException e) {
  //                       System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
  //                   }
  //               })
  //               .join(); // Wait for the async operation to complete
  //               return strength;
  //   }

  // public List<Cell> pullCell() throws IOException, InterruptedException {
  //       List<Cell> weatherCells = new ArrayList<>();

  //       HttpClient client = HttpClient.newHttpClient(); // creating a HTTP client
  //       HttpRequest request = HttpRequest.newBuilder() // creating a request to the server
  //               .uri(URI.create("http://13.238.167.130/weather"))
  //               .header("Accept", "text/event-stream")
  //               .build();

  //       client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()) // sending the request
  //               .thenApply(HttpResponse::body) // get the data
  //               .thenAccept(inputStream -> { // partition the stream
  //                   try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) { // read the input stream and turn it into characters
  //                       reader.lines().map( s -> s.split(" ")).limit(4).forEach( ( pieces -> {
  //                           /*
  //                            * This will be the logic to check the weather type, cell location, and strenght of the weather event
  //                            */
  //                           int row = Integer.parseInt(pieces[2]);
  //                           if(row < 0) {
  //                               row *= -1;
  //                           }
  //                           int column = Integer.parseInt(pieces[3]);
  //                           if(column < 0) {
  //                               column *= -1;
  //                           }
                            
  //                           weatherCells.add(grid.cellAtColRow(column, row).get());

  //                           System.out.println(pieces[4] + " added to cells");
  //                           System.out.println("");
                            
  //                       }) );
  //                   } catch (IOException e) {
  //                       System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage()); // if we have issues with reading the data, throw an exception
  //                   }
  //               })
  //               .join(); // Wait for the async operation to complete
  //               return weatherCells;
  //   }

}
