import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class Grid implements Observer {
  Cell[][] cells = new Cell[20][20];
  
  public Grid() {
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[i].length; j++) {
        cells[i][j] = new Cell(colToLabel(i), j, 10+Cell.size*i, 10+Cell.size*j);
      }
    }
  }

  private char colToLabel(int col) {
    return (char) (col + Character.valueOf('A'));
  }

  private int labelToCol(char col) {
    return (int) (col - Character.valueOf('A'));
  }

  public void paint(Graphics g, Point mousePos) {
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[i].length; j++) {
        cells[i][j].paint(g, mousePos);
      }
    }
  }

  public Optional<Cell> cellAtColRow(int c, int r) {
    if(c >= 0 && c < cells.length && r >=0 && r < cells[c].length) {
      return Optional.of(cells[c][r]);
    } else {
      return Optional.empty();
    }
  }

  public Optional<Cell> cellAtColRow(char c, int r) {
    return cellAtColRow(labelToCol(c), r);
  }

  public Optional<Cell> cellAtPoint(Point p) {
    for(int i=0; i < cells.length; i++) {
      for(int j=0; j < cells[i].length; j++) {
        if(cells[i][j].contains(p)) {
          return Optional.of(cells[i][j]);
        }
      }
    }
    return Optional.empty();
  }

  public List<Cell> getRadius(Cell from, int size) {
    int i = labelToCol(from.col);
    int j = from.row;
    Set<Cell> inRadius = new HashSet<Cell>();
    if (size > 0) {
        cellAtColRow(colToLabel(i), j - 1).ifPresent(inRadius::add);
        cellAtColRow(colToLabel(i), j + 1).ifPresent(inRadius::add);
        cellAtColRow(colToLabel(i - 1), j).ifPresent(inRadius::add);
        cellAtColRow(colToLabel(i + 1), j).ifPresent(inRadius::add);
    }

    for(Cell c: inRadius.toArray(new Cell[0])) {
        inRadius.addAll(getRadius(c, size - 1));
    }
    return new ArrayList<Cell>(inRadius);
  }

  public void paintOverlay(Graphics g, List<Cell> cells, Color color) {
    g.setColor(color);
    for(Cell c: cells) {
      g.fillRect(c.x+2, c.y+2, c.width-4, c.height-4);
    }
  }

  public void update(WeatherData data, Color weatherColour) {
    // find the event cell in our grid
    Cell currentCell= cellAtColRow(data.location.col, data.location.row).get(); 

    // create a random threhsold for each cell to allow dynamic weather
    Random rand = new Random();
    float threshold = rand.nextFloat() - 0.3f;
    
    // if the cell in this grid has no weather event, or the strength of the passed cell is higher than the existing strength, update with a new event
    if (currentCell.currentWeather == null || currentCell.weatherStrength < threshold) {
      cellAtColRow(data.location.col, data.location.row).get().weatherUpdate(data.event, data.strength, weatherColour);
    } else {
      // do nothing - the cell already has an event and the passed one is too weak to override the existing one
    }
  }
}
