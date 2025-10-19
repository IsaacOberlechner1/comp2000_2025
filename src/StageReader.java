import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

public class StageReader {
  public static Stage readStage(String path) throws IOException {
    Stage stage = new Stage();
    List<String> lines = Files.readAllLines(Paths.get(path));

    for (int i = 0; i < lines.size(); i++){
      // variables for column, row, and actor
      char column;
      int row;
      String actor;

      try {
        // assign the column
        if(lines.get(i).charAt(0) >= 65 && lines.get(i).charAt(0) <= 84) {
          column = lines.get(i).charAt(0);
        } else {
          throw new NoSuchElementException("Column is not a letter from A-T!");
        }

        if(lines.get(i).charAt(2) == '='){
          row = Character.getNumericValue(lines.get(i).charAt(1));
          actor = lines.get(i).substring(3, lines.get(i).length());
        } else if (lines.get(i).charAt(3) == '='){
          row = Integer.parseInt(lines.get(i).substring(1, 3));
          actor = lines.get(i).substring(4, lines.get(i).length());
        } else {
          throw new NoSuchElementException("'=' sign is missing or incorrectly placed!");
        }

      // Add the actors
      if(actor.equalsIgnoreCase("cat")){
        stage.actors.add(new Cat(stage.grid.cellAtColRow(column, row).get()));
      } else if (actor.equalsIgnoreCase("dog")){
        stage.actors.add(new Dog(stage.grid.cellAtColRow(column, row).get()));
      } else if(actor.equalsIgnoreCase("bird")) {
        stage.actors.add(new Bird(stage.grid.cellAtColRow(column, row).get()));
      } else {
        throw new NoSuchElementException("Missing or invalid actor!");
      }

      } catch (StringIndexOutOfBoundsException e) {
        System.out.println("Line " + (lines.indexOf(lines.get(i))+1) + " is incorrectly formatted!");
      } 
    }

    return stage;
  }
}
/* Need to handle errors around reading the text file. Potential errors:
- Col is not a character
- Row is not an integer
- Actor contains something other than character (e.g. an integer)
*/