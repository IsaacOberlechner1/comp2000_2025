public class DefaultMovement implements MovementState {

    public void defaultMovement(Actor a) {
        // nothing - already in default
    }

    public void halvedMovement(Actor a) { 
        // set the state to half movement
        a.moves = a.halfMoves;
        a.setState(new HalvedMovement());
    }

    public void doubleMovement(Actor a) {
        // set the state to double movement
        a.moves = a.doubleMoves;
        a.setState(new DoubleMovement());
    }

    public String stateDetails(Actor a) {
        return "The " + a.getClass().getName() + " is moving normally...";
    }
}
