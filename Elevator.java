import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The Elevator class represents an elevator in the simulation.
 * It manages the movement of the elevator, boarding and disembarking of passengers,
 * and tracks the current floor and direction of the elevator.
 */
public class Elevator {

    private int currentFloor;
    private Direction direction;
    private final int capacity;
    private final List<Passenger> passengers;
    private final int maxTravelDistance;
    private final int topFloor;

    /**
     * Constructs an Elevator object.
     *
     * @param listType          the type of list to use for managing passengers (ArrayList or LinkedList).
     * @param capacity          the maximum number of passengers the elevator can carry.
     * @param maxTravelDistance the maximum distance the elevator can travel in one move.
     * @param topFloor          the topmost floor that the elevator can reach.
     */
    public Elevator(String listType, int capacity, int maxTravelDistance, int topFloor) {
        this.currentFloor = 0; // Ground floor as start
        this.direction = Direction.UP; // Initial direction
        this.capacity = capacity;
        this.maxTravelDistance = maxTravelDistance;
        this.topFloor = topFloor;
        this.passengers = listType.equals("Linked") ? new LinkedList<Passenger>() : new ArrayList<Passenger>();

    }

     /**
     * Changes the direction of the elevator.
     *
     * @param newDirection the new direction to set for the elevator.
     */
    public void changeDirection(Direction newDirection) {
        this.direction = newDirection;
    }

    /**
     * Determines whether the elevator should change its direction.
     * 
     * @return true if the elevator should change direction; otherwise, false.
     */
    private boolean shouldChangeDirection() {
        if ((currentFloor == 0 && direction == Direction.DOWN) || (currentFloor == topFloor && direction == Direction.UP)) {
            return true;
        } else if (currentFloor == topFloor && direction == Direction.UP) {
            return true;
        }
        for (Passenger passenger : passengers) {
            if ((direction == Direction.UP && passenger.getDestinationFloor() > currentFloor) ||
                (direction == Direction.DOWN && passenger.getDestinationFloor() < currentFloor)) {
                return false;
            }
        }
        return true; // Change direction if no passengers need to move in the current direction
    }
    
    
    /**
     * Moves the elevator one step in its current direction, changing direction if necessary.
     *
     * @param checkDirectionChange whether to check and change direction before moving.
     */
    public void move(boolean checkDirectionChange) {
        // If there are no passengers, do not move
        if (passengers.isEmpty()) {
            // Log for testing
            //System.out.println("Elevator is waiting for passengers");
            return;
        }
    
        // Check if a change in direction is necessary
        if (checkDirectionChange && shouldChangeDirection()) {
            direction = direction == Direction.UP ? Direction.DOWN : Direction.UP;
        }
    
        // If the elevator is at the top floor and trying to move up, change direction to DOWN
        if (currentFloor == topFloor && direction == Direction.UP) {
            direction = Direction.DOWN;
        }
        // If the elevator is at the ground floor and trying to move down, change direction to UP
        else if (currentFloor == 0 && direction == Direction.DOWN) {
            direction = Direction.UP;
        }
    
        // Move the elevator within the limits
        if (direction == Direction.UP) {
            currentFloor = Math.min(currentFloor + maxTravelDistance, topFloor);
        } else {
            currentFloor = Math.max(currentFloor - maxTravelDistance, 0);
        }
    
        // Log the movement for testing
        //System.out.println("Elevator is now on floor: " + currentFloor + ", moving " + direction);
    }
    
    /**
     * Boards a passenger onto the elevator, if there is capacity.
     *
     * @param passenger the passenger to board.
     */
    public void boardPassenger(Passenger passenger) {
        if (canBoard(passenger)) {
            passengers.add(passenger);
        }
    }

    /**
     * Checks if a passenger can board the elevator.
     *
     * @param passenger the passenger attempting to board.
     * @return true if the passenger can board; otherwise, false.
     */
    public boolean canBoard(Passenger passenger) {
        // Check if the elevator has space and if the passenger is going in the same direction
        return passengers.size() < capacity && passenger.getDirection() == this.direction;
    }

    /**
     * Disembarks passengers who have reached their destination floor.
     *
     * @return a list of passengers who have disembarked.
     */
    public List<Passenger> disembarkPassengers() {
        List<Passenger> disembarking = new ArrayList<>();
    
        // Iterate over passengers and collect those who are to disembark
        for (Passenger passenger : passengers) {
            if (passenger.getDestinationFloor() == currentFloor) {
                disembarking.add(passenger);
            }
        }
    
        // Remove the disembarking passengers from the main list
        passengers.removeAll(disembarking);
    
        return disembarking;
    }
    
    /**
     * Gets the current floor of the elevator.
     *
     * @return the current floor.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Gets the current direction of the elevator.
     *
     * @return the current direction.
     */
    public Direction getDirection() {
        return direction;
    }
}


