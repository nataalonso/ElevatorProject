public class Passenger {
    
    private final int currentFloor;
    private final int destinationFloor;
    private final int arrivalTick;
    private Direction direction;

    private Integer destinationReachedTick = null;

    public Passenger(int currentFloor, int destinationFloor, int arrivalTick) {
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.arrivalTick = arrivalTick;
        this.direction = destinationFloor > currentFloor ? Direction.UP : Direction.DOWN;
    }

    public void setDestinationReachedTick(int tick) {
        this.destinationReachedTick = tick;
    }

    public boolean hasReachedDestination() {
        return destinationReachedTick != null;
    }

    public int totalTimeTaken(int currentTick) {
        return hasReachedDestination() ? destinationReachedTick - arrivalTick : currentTick - arrivalTick;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getArrivalTick() {
        return arrivalTick;
    }

    public Direction getDirection() {
        return direction;
    }
}
