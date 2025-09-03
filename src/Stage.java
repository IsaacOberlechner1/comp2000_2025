import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Stage {
  Grid grid;
  List<Actor> characters = new ArrayList<Actor>();

  public Stage() {
    grid = new Grid();
    characters.add(new Cat(grid.cellAtColRow(0, 0).get()));
    characters.add(new Dog(grid.cellAtColRow(0, 15).get()));
    characters.add(new Bird(grid.cellAtColRow(12, 9).get()));
  }

  public void paint(Graphics g, Point mouseLoc) {
    grid.paint(g, mouseLoc);
    for(int i = 0; i < characters.size(); i++){
      characters.get(i).paint(g);
    }
    Cell coordinates = grid.cellAtPoint(mouseLoc).get();
    g.drawString(coordinates.toString(), 720, 360);
  }
}
