public class HalvedMovement implements MovementState {
    public void defaultMovement(Actor a) {
        a.moves = a.defaultMoves;
        a.setState(new DefaultMovement());
    }

    public void halvedMovement(Actor a) { 
        // nothing - already halved
    }

    public void doubleMovement(Actor a) {
        a.moves = a.doubleMoves;
        a.setState(new DoubleMovement());
    }

    public String stateDetails(Actor a) {
        return "It's too hot for the " + a.getClass().getName() + "!";
    }
}
