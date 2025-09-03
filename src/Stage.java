import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Stage {
  Grid grid;
  List<Actor> characters = new ArrayList<Actor>();

  public Stage() {
    grid = new Grid();
    characters.add(new Cat(grid.cellAtColRow(0, 0)));
    characters.add(new Dog(grid.cellAtColRow(0, 15)));
    characters.add(new Bird(grid.cellAtColRow(12, 9)));
  }

  public void paint(Graphics g, Point mouseLoc) {
    grid.paint(g, mouseLoc);
    for(int i = 0; i < characters.size(); i++){
      characters.get(i).paint(g);
    }
  }
}
