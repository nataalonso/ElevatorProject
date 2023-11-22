import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The ElevatorSimulation class simulates the operation of elevators in a building.
 * It manages the movement of elevators, the boarding and disembarking of passengers,
 * and calculates the average, longest, and shortest travel times of the passengers.
 */
public class ElevatorSimulation {
    private SimulationProperties simulationProperties;
    private List<Elevator> elevators;
    private List<List<Passenger>> floorPassengerMap;
    private List<Passenger> completedPassengers = new ArrayList<>();
    private int currentTick;

    /**
     * Constructs an ElevatorSimulation instance.
     * Initializes the simulation properties, elevators, and passenger floor map.
     *
     * @param propertyFilePath the file path of the simulation properties.
     */
    public ElevatorSimulation(String propertyFilePath) {
        try {
            simulationProperties = new SimulationProperties(propertyFilePath);
    
            // Initialize elevators
            elevators = new ArrayList<>();
            for (int i = 0; i < simulationProperties.getNumberOfElevators(); i++) {
                elevators.add(new Elevator(simulationProperties.getListType(),
                                simulationProperties.getElevatorCapacity(),
                                simulationProperties.getMaxTravelDistance(),
                                simulationProperties.getFloors()));
            }
            // Initialize the floor-passenger map
            floorPassengerMap = new ArrayList<>(simulationProperties.getFloors());
            for (int i = 0; i <= simulationProperties.getFloors(); i++) {
                floorPassengerMap.add(new ArrayList<>());
            }
        } catch (IOException e) {
            System.out.println("Error reading properties file, using default values: " + e.getMessage());
            return;
       
        }
    }
    

    /**
     * Runs the elevator simulation for the duration specified in the simulation properties.
     */
    public void runSimulation() {
        // Loop through each simulation tick up to the duration
        for (int tick = 0; tick < simulationProperties.getDuration(); tick++) {
            currentTick = tick;
            simulateTick(tick);
        }
        reportResults();
    }

    /**
     * Simulates the events that occur in a single tick of the simulation.
     * This includes generating new passengers, moving elevators,
     * and handling passenger boarding and disembarking.
     *
     * @param tick the current tick of the simulation.
     */
    private void simulateTick(int tick) {
        // Generate new passengers at this tick
        generateNewPassengers(tick);
        // Move each elevator and handle passenger interactions
        for (Elevator elevator : elevators) {
            elevator.move(true);
            handlePassengerDisembark(elevator, tick);
            handlePassengerBoard(elevator);
        }
    }

    /**
     * Generates new passengers for each floor of the building based on a probability.
     *
     * @param tick the current tick of the simulation.
     */
    private void generateNewPassengers(int tick) {
        for (int floor = 0; floor < simulationProperties.getFloors(); floor++) {
            // Check if a new passenger appears on this floor based on probability
            if (Math.random() < simulationProperties.getPassengerProbability()) {
                int destinationFloor;
                // Determine a random destination floor different from the current floor
                do {
                    destinationFloor = (int) (Math.random() * simulationProperties.getFloors());
                } while (destinationFloor == floor);

                // Create a new passenger and add to the floor's passenger list
                Passenger newPassenger = new Passenger(floor, destinationFloor, tick);
                floorPassengerMap.get(floor).add(newPassenger);
            }
        }
    }

    /**
     * Handles the boarding of passengers onto an elevator.
     * Removes passengers from the floor list once they board the elevator.
     *
     * @param elevator the elevator onto which passengers will board.
     */
    private void handlePassengerBoard(Elevator elevator) {
        int currentFloor = elevator.getCurrentFloor();
        if (currentFloor < 0 || currentFloor >= floorPassengerMap.size()) {
            System.err.println("Invalid floor index: " + currentFloor);
            return;
        }
        // Get the list of passengers on the current floor of the elevator
        List<Passenger> floorPassengers = floorPassengerMap.get(elevator.getCurrentFloor());
        // Remove passengers from the floor list if they board the elevator
        floorPassengers.removeIf(passenger -> {
            if (elevator.canBoard(passenger)) {
                elevator.boardPassenger(passenger);
                return true;
            }
            return false;
        });
    }

    /**
     * Handles the disembarking of passengers from an elevator.
     * Adds the passengers to the completed passengers list.
     *
     * @param elevator the elevator from which passengers will disembark.
     * @param tick the current tick of the simulation.
     */
    private void handlePassengerDisembark(Elevator elevator, int tick) {
        // Get the list of passengers disembarking at the current floor
        List<Passenger> disembarkingPassengers = elevator.disembarkPassengers();
        // Update each disembarking passenger's status and add to completed list
        for (Passenger passenger : disembarkingPassengers) {
            passenger.setDestinationReachedTick(tick);
            completedPassengers.add(passenger);
        }
    }

    /**
     * Reports the results of the simulation by calculating and displaying
     * the average, longest, and shortest travel times of passengers.
     */
    private void reportResults() {
        int totalTravelTime = 0;
        int longestTime = Integer.MIN_VALUE;
        int shortestTime = Integer.MAX_VALUE;
        int passengersCount = completedPassengers.size();
    
        for (Passenger p : completedPassengers) {
            int timeTaken = p.totalTimeTaken(currentTick);
    
            // Accumulate total time for average calculation
            totalTravelTime += timeTaken;
    
            // Check for longest and shortest times
            if (timeTaken > longestTime) {
                longestTime = timeTaken;
            }
            if (timeTaken < shortestTime) {
                shortestTime = timeTaken;
            }
        }
    
        double averageTime = passengersCount > 0 ? (double) totalTravelTime / passengersCount : 0.0;
    
        // Handle case where no passengers completed the journey
        if (passengersCount == 0) {
            longestTime = 0;
            shortestTime = 0;
        }
    
        System.out.println("Average time: " + averageTime);
        System.out.println("Longest time: " + longestTime);
        System.out.println("Shortest time: " + shortestTime);
    }
    
    /**
     * Checks whether the properties file was successfully loaded.
     * 
     * @return true if the properties file was successfully loaded, false otherwise.
     */
    public boolean propertiesFileLoaded() {
        return simulationProperties.isFileLoaded();
    }
    
    /**
     * The entry point for the program.
     * This method initializes and runs the elevator simulation using the properties
     * file specified as a command-line argument.
     * 
     * @param args Command-line arguments. The first argument should be the path to the
     * properties file that configures the simulation. If no argument is provided,
     * the simulation will use default settings.
     */
    public static void main(String[] args) {
        ElevatorSimulation simulation;

        // Check if the file name is provided as a command-line argument
        if (args.length > 0) {
            String propertyFileName = args[0]; // Take the first argument as the file name
            simulation = new ElevatorSimulation(propertyFileName);
        } else {
            System.out.println("No properties file name provided. Using default simulation settings.");
            simulation = new ElevatorSimulation(null);
        }

        // Check if the properties file was loaded successfully
        if (!simulation.propertiesFileLoaded()) {
            System.out.println("Warning: Properties file not loaded correctly. Default values are used.");
        }

        // Run the simulation
        simulation.runSimulation();
    }
    
}