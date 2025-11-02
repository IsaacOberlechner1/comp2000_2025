public class DoubleMovement implements MovementState {
    public void defaultMovement(Actor a) {
        // set the state to default
        a.moves = a.defaultMoves;
        a.setState(new DefaultMovement());
    }

    public void halvedMovement(Actor a) { 
        // set the state to half
        a.moves = a.halfMoves;
        a.setState(new HalvedMovement());
    }

    public void doubleMovement(Actor a) {
        // nothing - already doubled
    }

    public String stateDetails(Actor a) {
        if(a.getClass().getName() == "bird") { // bird 
            return a.getClass().getName() + " flows in the rhythm of the wind... movement DOUBLED this turn.";
        } else { // cat and dog
            return "It's too wet for the " + a.getClass().getName() + "!";
        }
    }
}
