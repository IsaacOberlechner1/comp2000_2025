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
  int defaultMoves;
  int doubleMoves;
  int halfMoves;
  int turns;
  boolean altered = false;
  MoveStrategy mover;

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

    if(loc.currentWeather == "raining" && altered != true) {
      moves = doubleMoves;
      altered = true;
      System.out.println("Moves have been doubled!" + " " + altered);
    } else if (loc.currentWeather == "windy" && altered != true){
      moves = doubleMoves;
      altered = true;
      System.out.println("Moves have been doubled!" + " " + altered);
    } else if (loc.currentWeather == "hot" && altered != true) {
        moves = halfMoves;
        altered = true;
        System.out.println("Moves have been halved!" + " " + altered);
    } else if(loc.currentWeather == null) {
      moves = defaultMoves;
      altered = false;
      System.out.println("Standard moves" + " " + altered);
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
}
