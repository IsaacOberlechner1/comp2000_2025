import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public abstract class Actor {
  List<Polygon> polygons = new ArrayList<Polygon>();
  Color color;
  Cell loc;

  public void paint(Graphics g) {
    for(int i = 0; i < polygons.size(); i++){
      g.setColor(color);
      g.fillPolygon(polygons.get(i));
      g.setColor(Color.BLACK);
      g.drawPolygon(polygons.get(i));
    }

  }
}
