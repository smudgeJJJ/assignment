package vehicles;

import exceptions.EmptyRouteException;
import exceptions.IncompatibleTypeException;
import exceptions.OverCapacityException;
import exceptions.TransportFormatException;
import passengers.Passenger;
import routes.Route;
import stops.Stop;
import utilities.Writeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A base public transport vehicle in the transportation network.
 */
public abstract class PublicTransport implements Writeable {
    private static final int DECODE_LIST_SIZE = 5;

    // the passengers currently on board the vehicle
    private List<Passenger> passengers;

    // the place the vehicle is currently stopped
    private Stop currentLocation;

    // the maximum passengers allowed on board the vehicle
    private int capacity;

    // the vehicle's identifier
    private int id;

    // the route the vehicle follows
    private Route route;

    /**
     * Creates a new public transport vehicle with the given id, capacity, and
     * route.
     * <p>
     * <p>The vehicle should initially have no passengers on board, and should be placed
     * at the beginning of the given route (given by {@link Route#getStartStop()}).
     * If the route is empty, the current location should be stored as null.
     * <p>
     * <p> If the given capacity is negative, 0 should be stored as the capacity
     * instead (meaning no passengers will be allowed on board this vehicle).
     *
     * @param id       The identifying number of the vehicle.
     * @param capacity The maximum number of passengers allowed on board.
     * @param route    The route the vehicle follows. Note that the given route should
     *                 never be null (@require route != null), and thus will not be
     *                 tested with a null value.
     */
    public PublicTransport(int id, int capacity, Route route) {
        this.passengers = new ArrayList<>();
        this.capacity = capacity < 0 ? 0 : capacity;
        this.id = id;
        this.route = route;
        try {
            this.currentLocation = route.getStartStop();
        } catch (EmptyRouteException e) {
            this.currentLocation = null;
        }
    }

    /**
     * 解码
     *
     * @param transportString
     * @param existingRoutes
     * @return
     * @throws TransportFormatException
     */
    public static PublicTransport decode(String transportString, List<Route> existingRoutes)
            throws TransportFormatException {

        if (Objects.isNull(existingRoutes) || existingRoutes.isEmpty()) {
            throw new TransportFormatException();
        }
        if (Objects.isNull(transportString) || transportString.isEmpty()) {
            throw new TransportFormatException();
        }
        String[] transportStrings = transportString.split(",");
        if (transportStrings.length != DECODE_LIST_SIZE) {
            throw new TransportFormatException();
        }

        List<String> transportList = new ArrayList<>(Arrays.asList(transportStrings));
        String type = transportList.get(0);
        Integer id;
        Integer capacity;
        Integer routeNo;
        String extra = transportList.get(4);
        try {
            id = Integer.valueOf(transportList.get(1));
            capacity = Integer.valueOf(transportList.get(2));
            routeNo = Integer.valueOf(transportList.get(3));
        } catch (NumberFormatException e) {
            throw new TransportFormatException();
        }

        Optional<Route> routeOptional = existingRoutes.stream().filter(route1 -> Objects.equals(route1.getRouteNumber(), routeNo)).findFirst();
        Route route = routeOptional.orElseThrow(TransportFormatException::new);

        if (!Objects.equals(route.getType(), type)) {
            throw new TransportFormatException();
        }

        PublicTransport publicTransport;
        switch (type) {
            case "bus":
                publicTransport = new Bus(id, capacity, route, extra);
                break;
            case "train":
                Integer carriageCount;
                try {
                    carriageCount = Integer.valueOf(extra);
                } catch (NumberFormatException e) {
                    throw new TransportFormatException();
                }
                publicTransport = new Train(id, capacity, route, carriageCount);
                break;
            case "ferry":
                publicTransport = new Ferry(id, capacity, route, extra);
                break;
            default:
                throw new TransportFormatException();

        }

        try {
            route.addTransport(publicTransport);
        } catch (EmptyRouteException | IncompatibleTypeException e) {
            throw new TransportFormatException();
        }

        return publicTransport;
    }

    @Override
    public String encode() {
        return String.format("%s,%s,%s,%s", getType(), this.id, this.capacity, this.route.getRouteNumber());
    }

    /**
     * Returns the route this vehicle is on.
     *
     * @return The route this vehicle is on.
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Returns the id of this vehicle.
     *
     * @return The id of this vehicle.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the current location of this vehicle.
     *
     * @return The stop this vehicle is currently located at, or null if it is not
     * currently located at a stop.
     */
    public Stop getCurrentStop() {
        return currentLocation;
    }

    /**
     * Returns the number of passengers currently on board this vehicle.
     *
     * @return The number of passengers in the vehicle.
     */
    public int passengerCount() {
        return passengers.size();
    }

    /**
     * Returns the maximum number of passengers allowed on this vehicle.
     *
     * @return The maximum capacity of the vehicle.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the type of this vehicle, as determined by the type of the route it
     * is on (i.e. The type returned by {@link Route#getType()}).
     *
     * @return The type of this vehicle.
     */
    public String getType() {
        return route.getType();
    }

    /**
     * Returns the passengers currently on-board this vehicle.
     * <p>
     * <p>No specific order is required for the passenger objects in the returned
     * list.
     * <p>
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The passengers currently on the public transport vehicle.
     */
    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    /**
     * Adds the given passenger to this vehicle.
     * <p>
     * <p>If the passenger is null, the method should return without adding it
     * to the vehicle.
     * <p>
     * <p>If the vehicle is already at (or over) capacity, an exception should
     * be thrown and the passenger should not be added to the vehicle.
     *
     * @param passenger The passenger boarding the vehicle.
     * @throws OverCapacityException If the vehicle is already at (or over) capacity.
     */
    public void addPassenger(Passenger passenger) throws OverCapacityException {
        if (passenger == null) {
            return;
        }

        if (passengers.size() >= capacity) {
            throw new OverCapacityException();
        }
        passengers.add(passenger);
    }

    /**
     * Removes the given passenger from the vehicle.
     * <p>
     * <p>If the passenger is null, or is not on board the vehicle, the method should
     * return false, and should not have any effect on the passengers currently
     * on the vehicle.
     *
     * @param passenger The passenger disembarking the vehicle.
     * @return True if the passenger was successfully removed, false otherwise (including
     * the case where the given passenger was not on board the vehicle to
     * begin with).
     */
    public boolean removePassenger(Passenger passenger) {
        return passengers.remove(passenger);
    }

    /**
     * Empties the vehicle of all its current passengers, and returns all the passengers
     * who were removed.
     * <p>
     * <p>No specific order is required for the passenger objects in the returned
     * list.
     * <p>
     * <p>If there are no passengers currently on the vehicle, the method just
     * returns an empty list.
     * <p>
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The passengers who used to be on the vehicle.
     */
    public List<Passenger> unload() {
        List<Passenger> leaving = passengers;
        passengers = new ArrayList<>();
        return leaving;
    }

    /**
     * Updates the current location of the vehicle to be the given stop.
     * <p>
     * <p>If the given stop is null, or is not on this public transport's route
     * the current location should remain unchanged.
     *
     * @param stop The stop the vehicle has travelled to.
     */
    public void travelTo(Stop stop) {
        if (!route.getStopsOnRoute().contains(stop)) {
            return;
        }

        currentLocation = stop == null ? currentLocation : stop;
    }

    /**
     * Creates a string representation of a public transport vehicle in the format:
     * <p>
     * <p>'{type} number {id} ({capacity}) on route {route}'
     * <p>
     * <p>without the surrounding quotes, and where {type} is replaced by the type of
     * the vehicle, {id} is replaced by the id of the vehicle, {capacity} is replaced
     * by the maximum capacity of the vehicle, and {route} is replaced by the route
     * number of the route the vehicle is on. For example:
     * <p>
     * <p>bus number 1 (30) on route 1
     *
     * @return A string representation of the vehicle.
     */
    @Override
    public String toString() {
        return getType() + " number " + id + " (" + capacity + ") on route " + route.getRouteNumber();
    }
}
