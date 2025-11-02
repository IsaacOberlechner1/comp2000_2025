public interface MovementState {
    public void defaultMovement(Actor a);
    public void halvedMovement(Actor a);
    public void doubleMovement(Actor a);
    public String stateDetails(Actor a);
}
