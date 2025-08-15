import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {
    public static void main(String[] args) throws Exception {
      Main window = new Main(); // create a new Main object called 'window'
      window.run(); // renders the window for runtime
    }

    class Canvas extends JPanel {
      public Canvas() { // constructor for a Canvas object
        setPreferredSize(new Dimension(720, 720)); // sets the preferred window size to 720x720 pixels
      }

      @Override
      public void paint(Graphics g) { // paints (renders) the following when called 
        g.setColor(java.awt.Color.BLACK); // sets the "brush" to black, meaning all things drawn will be black
        g.drawRect(10, 10, 700, 700); // draws the borders
        /* Draw a 20x20 grid
         * Needs to be 10 pixels off the top and left borders
         * 
         * Draw 20 squares (35x35) across the top of the screen
         * Then, draw another 20 squares 35 pixels down
         * Until we've drawn 20 rows
         */
        int offset = 10;
        int size = 35;
        for (int i = 0; i < 20; i++){
          g.drawRect(offset + (size * i), offset, size, size);
          for(int k = 0; k < 20; k++){
            g.drawRect(offset + (i * size), offset + (k * size), size, size);
          }
        }
        }
        
        // for(int row = 0; row < 20; row++){
        //   g.drawRect(offset, offset + (row * size), size, size);
        // }
        }

    private Main() { // constructor for Main object 
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes the application when we close the window
      Canvas canvas = new Canvas(); // instantiates a canvas
      this.setContentPane(canvas); // set the content pane as our canvas object
      this.pack(); // makes the window size based on Main's size (720x720, based on Canvas' parameters)
      this.setVisible(true); // makes the window visible
    }

    public void run() { // method for main allowing the application to run
      while(true) { // while the window is active
        repaint(); // calls the paint method
      }
    }
}
