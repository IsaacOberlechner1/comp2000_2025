public class DoubleMovement implements MovementState {
    public void defaultMovement(Actor a) {
        a.moves = a.defaultMoves;
        a.setState(new DefaultMovement());
    }

    public void halvedMovement(Actor a) { 
        a.moves = a.halfMoves;
        a.setState(new HalvedMovement());
    }

    public void doubleMovement(Actor a) {
        // nothing - already doubled
    }

    public String stateDetails(Actor a) {
        if(a.getClass().getName() == "bird") {
            return a.getClass().getName() + " flows in the rhythm of the wind... movement DOUBLED this turn.";
        } else {
            return "It's too wet for the " + a.getClass().getName() + "!";
        }
    }
}
