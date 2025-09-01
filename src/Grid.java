import java.awt.Graphics;
import java.awt.Point;

public class Grid {
    // variable
    Cell[][] cells = new Cell[20][20];

    // constructor
    Grid (){
        for (int i = 0; i < cells.length; i++){
            for(int k = 0; k < cells[i].length; k++){
                cells[i][k] = new Cell(10 + (35*i), 10 + (35*k)); // 
            }
        }
    }

    // methods
    public void paint(Graphics g, Point p){
        for (int i = 0; i < cells.length; i++){
            for(int k = 0; k < cells[i].length; k++){
                cells[i][k].paint(g, p);
            }
        }
    }
    }
