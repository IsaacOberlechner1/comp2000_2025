import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Cell extends Rectangle {
  static int size = 35;
  char col;
  int row;
  String currentWeather; // the current event
  float weatherStrength; // the strength of the current event
  Color cellColour; // the colour of the cell - updates depending on whether an event is present

  public Cell(char inCol, int inRow, int x, int y) {
    super(x, y, size, size);
    col = inCol;
    row = inRow;
    cellColour = Color.WHITE;
  }

  public void paint(Graphics g, Point mousePos) {
    if(contains(mousePos)) {
      g.setColor(Color.GRAY);
    } else {
      g.setColor(cellColour);
    }
    g.fillRect(x, y, size, size);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, size, size);
  }

  @Override
  public boolean contains(Point p) {
    if(p != null) {
      return super.contains(p);
    } else {
      return false;
    }
  }

  public int leftOfComparison(Cell c) {
    return Integer.compare(col, c.col);
  }

  public int aboveComparison(Cell c) {
    return Integer.compare(row, c.row);
  }

  // updates the weather
  public void weatherUpdate(String event, float strength, Color colour) {
    currentWeather = event;
    weatherStrength = strength;
    cellColour = colour;
  }
}

