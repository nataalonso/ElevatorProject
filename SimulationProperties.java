import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.FileReader;

/**
 * The SimulationProperties class handles the loading and accessing of 
 * configuration properties for an elevator simulation.
 * It reads properties from a specified file and provides methods to
 * retrieve these properties in the required data types.
 */
public class SimulationProperties {
    
    private final Properties properties;
    private boolean fileLoaded = false; 

    /**
     * Constructs a SimulationProperties instance.
     * Attempts to load properties from the specified file path. If the file
     * cannot be read or is not specified, default values are set for the properties.
     *
     * @param propertyFilePath The file path of the properties file.
     * @throws IOException If there is an error in reading the properties file.
     */
    public SimulationProperties(String propertyFilePath) throws IOException {
        properties = new Properties();

        if (propertyFilePath != null && !propertyFilePath.isEmpty()) {
            try (FileInputStream in = new FileInputStream(propertyFilePath)) {
                properties.load(in);
                fileLoaded = true; // Set to true only if file is successfully loaded
            } catch (IOException e) {
                System.out.println("Error reading properties file: " + e.getMessage());
                setDefaultValues(); // Set default values if file reading fails
            }
        }

        if (!fileLoaded) {
            setDefaultValues(); // Set default values if file path is null or empty
        }
    }

    /**
     * Checks whether the properties file was successfully loaded.
     *
     * @return true if the properties file was successfully loaded, false otherwise.
     */
    public boolean isFileLoaded() {
        return fileLoaded;
    }

    /**
     * Loads all properties from a specified file path.
     *
     * @param propertiesFilePath The path of the file to load properties from.
     * @return The Properties object loaded from the file.
     * @throws IOException If there is an error in reading the file.
     */
    public Properties getAllProperties(String propertiesFilePath) throws IOException {
        Properties allProps = new Properties();
        try (FileReader readFile = new FileReader(propertiesFilePath)) {
            allProps.load(readFile);
        }
        return allProps;
    }

    // Method to set default values
    private void setDefaultValues() {
        // Default values are directly set here
        properties.setProperty("floors", "32");
        properties.setProperty("passengers", "0.03");
        properties.setProperty("elevators", "1");
        properties.setProperty("elevatorCapacity", "10");
        properties.setProperty("duration", "500");
        properties.setProperty("maxTravelDistance", "5");
        properties.setProperty("listType", "ArrayList");
    }

    /**
     * Retrieves the number of floors configured in the properties.
     * If the value is not set or is invalid, a default value is returned.
     *
     * @return The number of floors as an integer.
     */
    public int getFloors() {
        try {
            return Integer.parseInt(properties.getProperty("floors", "32"));
        } catch (NumberFormatException e) {
            return 32; // Default value
        }
    }
    /**
     * Retrieves the probability of a passenger appearing, as configured in the properties.
     * If the value is not set or is invalid, a default value is returned.
     *
     * @return The passenger probability as a double.
     */
    public double getPassengerProbability() {
        try {
            return Double.parseDouble(properties.getProperty("passengers", "0.03"));
        } catch (NumberFormatException e) {
            return 0.03; // Default value
        }
    }
    /**
     * Retrieves the number of elevators configured in the properties.
     * If the value is not set or is invalid, a default value is returned.
     *
     * @return The number of elevators as an integer.
     */
    public int getNumberOfElevators() {
        try {
            return Integer.parseInt(properties.getProperty("elevators", "1"));
        } catch (NumberFormatException e) {
            return 1; // Default value
        }
    }

    /**
     * Retrieves the elevator capacity as configured in the properties.
     * If the value is not set or is invalid, a default value is returned.
     *
     * @return The elevator capacity as an integer.
     */
    public int getElevatorCapacity() {
        try {
            return Integer.parseInt(properties.getProperty("elevatorCapacity", "10"));
        } catch (NumberFormatException e) {
            return 10; // Default value
        }
    }

    /**
     * Retrieves the duration for the simulation as configured in the properties.
     * If the value is not set or is invalid, a default value is returned.
     *
     * @return The simulation duration as an integer.
     */
    public int getDuration() {
        try {
            return Integer.parseInt(properties.getProperty("duration", "500"));
        } catch (NumberFormatException e) {
            return 500; // Default value
        }
    }

    /**
     * Retrieves the maximum travel distance for an elevator, as configured in the properties.
     * If the value is not set or is invalid, a default value is returned.
     *
     * @return The maximum travel distance as an integer.
     */
    public int getMaxTravelDistance() {
        try {
            return Integer.parseInt(properties.getProperty("maxTravelDistance", "5"));
        } catch (NumberFormatException e) {
            return 5; // Default value
        }
    }
    
    /**
     * Retrieves the type of list to be used in the simulation, as configured in the properties.
     * If the value is not set, a default value is returned.
     *
     * @return The type of list as a String.
     */
    public String getListType() {
        return properties.getProperty("listType", "ArrayList");
    }
}
