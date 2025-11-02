import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

public abstract class Actor implements Pulse {
  Color baseColor, color;
  Cell loc;
  List<Polygon> display;
  boolean bot;
  int moves;
  int defaultMoves; // holds ourt default moves
  int doubleMoves; // holds the value for double our moves
  int halfMoves; // holds the value for half our moves
  int turns;
  MoveStrategy mover;
  private MovementState currentState; // the state for our movement

  protected Actor(Cell inLoc, Color inColor, boolean isBot, int inMoves) {
    loc = inLoc;
    baseColor = inColor;
    color = inColor;
    bot = isBot;
    defaultMoves = inMoves;
    moves = inMoves;
    doubleMoves = defaultMoves * 2;
    halfMoves = defaultMoves / 2;
    turns = 1;
    currentState = new DefaultMovement(); // start in default
    setPoly();
  }

  public void paint(Graphics g) {
    for(Polygon p: display) {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(Color.GRAY);
      g.drawPolygon(p);
    }
  }

  protected abstract void setPoly();

  public boolean isBot() {
    return bot;
  }

  public void setLocation(Cell inLoc) {
    loc = inLoc;

    // change the state of our movement based on the weather
    if(loc.currentWeather == "raining" && (this.getClass().getName() == "Cat" || this.getClass().getName() == "Dog")) { // rain doubles cat and dog movement
      currentState.doubleMovement(this);
    } else if (loc.currentWeather == "windy" && this.getClass().getName() == "Bird"){ // wind doubles bird movement
      currentState.doubleMovement(this);
    } else if (loc.currentWeather == "hot") { // hot halves all actor movements
      currentState.halvedMovement(this);
    } else { // normal movement
      currentState.defaultMovement(this);
    } 

    if(loc.row % 2 == 0) {
      mover = new MoveRandomly();
    } else {
      mover = new MoveLeft();
    }
    setPoly();
  }

  public void pulsate(char phase, int percentage) {
    // Adjust color saturation according to the beat
    float[] hsbValues = new float[3];
    Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsbValues);
    hsbValues[1] = ((float) percentage) / 100.0f;
    color = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
  }

  // get the current state
  public String getState(Actor a) {
    return currentState.stateDetails(a);
  }

  // set the current state
  public void setState(MovementState newState) {
    currentState = newState;
  }
}
