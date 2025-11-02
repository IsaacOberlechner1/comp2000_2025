# Assignment 2 Documentation
**Name**: Isaac Oberlechner 

**SID**: 46615962
## Feature Overview
### Feature - Weather Effects 

The feature involves cells updating based on weather effects at their location. Cells can receive the following weather events, which have the following effects on *Actors*:
* **Clear** - The cell is white. All actors will move based on their default movement. 
* **Rainy** - The cell will turn blue. *Cat* and *Dog* actors on a cell with this event will have their movement radius *doubled* for that turn. 
* **Windy** - The cell will turn gray. *Bird* actors on a cell with this event will have their movement radius *doubled* for that turn.
* **Hot** - The cell will turn orange. All actors on a cell with this event will have their movement radius *halved* for that turn. 

The actor's name and movement state are also displayed in red text on the right side of the application window. Each text is tailored to reflect the actor's type and state uniquely, but all actors share the same three states. The states are:
* **Default Movement** - The actor move radius is default as per their <code>moves</code> value in their class. 
* **Doubled Movement** - The actor move radius is doubled from their <code> moves</code> value.
* **Halved Movement** - The actor move radius is halved from their <code>moves</code> value

The feature provides a new tool for implementing challenges (i.e. hindered movement) and rewards (i.e. boosted movement) in levels that can update dynamically at runtime. 

### How to run
1. Run the game
2. Hover over a white, blue, gray, and orange cell and observe the following:
* The white cell should display *(clear)* next to the cell location text.
* The blue cell should display *(rainy)* next to the cell location text.
* The gray cell shoul display *(windy)* next to the cell location text.
* The orange cell should display *(hot)* next to the cell location text.

3. Move the Cat and observe the following behaviour:
* If the Cat is on a white cell, they should have a default movement radius for that turn. This effect should be identical for all actors. On the right of the application window, the phrase *The [actor name] is moving normally* (the [actor name] slot in this instance will be Cat) should appear in red text. 
* If the Cat is on a blue (rainy) cell, the movement radius should be doubled for that turn. This effect should be identical for the Dog, but not the Bird. On the right of the application window, the phrase *It's too wet for the [actor name]!* (the [actor name] slot in this instance will be Cat) should appear in red text.
* If the  Cat is on a gray cell, their movement radius should be default. On the right of the application window, the phrase *The [actor name] is moving normally* (the [actor name] slot in this instance will be Cat) should appear in red text. Only the Bird should be affected (i.e. doubled movement) on this cell.
* If the cat is on an orange cell, their movement radius should be halved. This should be identical for all animals. On the right of the application window, the phrase *It's too hot for the [actor name]!* (the [actor name] slot in this instance will be Cat) should appear in red text. 

## Justification
### Design Patterns
Relevant classes:

**Observer Pattern**:
* WeatherStation (Subject)
* WeatherData (custom type to hold weather data)
* Grid (Observer)
* State (where the observers are registered and weather stream is pulled)
* Observer (interface for the observers)
* Subject (interface for the subjects)

**State Pattern**:
* MovementState (Interface for the states)
* DefaultMovement (state)
* DoubleMovement (state)
* HalvedMovement (state)
* Actor (the context class)
* Stage (where the state text is drawn)

The **Observer Pattern** was used to create a weather station that the grid subscribes to. In <code>WeatherStation</code>, the stream for pulling the weather data is held within a method. Once the data is being pulled, it is parsed (converted into a custom <code>WeatherData</code> type) and passed to any notified observers (i.e. the grid). In <code>Grid</code>, the weather data recieved is used to find the corresponding cell on the game's grid and apply the weather effect if their strength is above a threshold. The pattern was chosen for it's loosely coupled design, allowing the weather data to be passed to any other object without modifying the class. For example, if we wanted to create a UI class that displays weather conditions (e.g. most occuring event, highest strength cell), we can register the new class to the weather station, pull the relevant information from the passed weather data, and code our calculations in the new class. Though it has tigher coupling than a typical observer pattern (i.e. it requires weather data and colour information to be passed to observers), it still effectively separates the weather data from other objects and allows it to be used in different ways through subscription. 

The **State Pattern** was used to create the movement states for the avatar. In <code>MovementState</code>, the interface for the movements are defined, requiring all states to have implementations for handling default movement, doubeld movement, halved movement, and state details. The <code>DefaultMovement</code>, <code>DoubleMovement</code>, and <code>HalvedMovement</code> classes are the concrete state implementations. The <code>Actor</code> context class contains logic for altering the state based on what the cell's current weather event is and what actor type (i.e. Cat/Dog/Bird) is on the cell. This pattern helped reduced complex and performance-heavy code in the Actor class (e.g. large if-else statements) by delegating changes in movement logic to separate classes that are interchangable at runtime. It also ensures appropriate encapsulation by keeping the Actor class responsible for rendering and holding the properties of players, while the state classes are responsible for managing changes in movement logic and communicating desired behaviour to the actors. 

### Lambdas and Streams
**Lambdas** and Streams were used in the <code>WeatherStation</code> class to ensure the incoming weather data was parsed and processed accordingly. The stream was encapsulated into a method to ensure it was only called and running whilst the application was running. Through the stream, the weather data was able to be partitioned into meaningful pieces (i.e. separating time, event, location, and strength), limited in its output (to not overload the board), and passed to a separate function to convert the data into a <WeatherData> object. Lambdas ensured behaviours (i.e. splitting the string based on spaces, converting negative locations to positive, and parsing data based on the event occuring) could be parameterised within the stream functions, reducing the need to code new functions for each behaviour. In combination with stacking stream functions, this ensured a concise pipeline of data processing could be established. The combination of lambdas and streams allowed data processing to stay concise and localised, facilitating greater code clarity and efficiency.