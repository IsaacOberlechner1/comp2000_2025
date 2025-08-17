import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Cell {
    // variable
    private int x; 
    private int y;
    static int size = 35;
    
    // constructor
    Cell (int xPos, int yPos){
        this.x = xPos;
        this.y = yPos;
    }

    // methods
    public void paint(Graphics g, Point mousePosition){
        if(checkMousePosition(mousePosition) == true){
            g.setColor(Color.GRAY);
            g.fillRect(x, y, 35, 35);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, 35, 35);
        }
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 35, 35);
    }

    public boolean checkMousePosition(Point mousePosition){
        if((x < mousePosition.x && x+size > mousePosition.x) && (y < mousePosition.y && y+35 > mousePosition.y)){
            return true;
        } else {
            return false;
        }
    }
}
