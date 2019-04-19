package routes;

import exceptions.EmptyRouteException;
import exceptions.IncompatibleTypeException;
import exceptions.TransportFormatException;
import stops.Stop;
import utilities.Writeable;
import vehicles.Bus;
import vehicles.Ferry;
import vehicles.PublicTransport;
import vehicles.Train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a route in the transportation network.
 * <p>
 * <p>A route is essentially a collection of stops which public transport vehicles
 * can follow.
 */
public abstract class Route implements Writeable {
    // the name of the route
    private String name;

    // the number of the route
    private int routeNumber;

    // tracks where vehicles are currently located on the route
    private List<PublicTransport> vehicles;

    // the stops which make up the route
    private List<Stop> route;

    /**
     * Creates a new Route with the given name and number.
     * <p>
     * <p>The route should initially have no stops or vehicles on it.
     * <p>
     * <p>If the given name contains any newline characters ('\n') or carriage returns
     * ('\r'), they should be removed from the string before it is stored.
     * <p>
     * <p>If the given name is null, an empty string should be stored in its place.
     *
     * @param name        The name of the route.
     * @param routeNumber The route number of the route.
     */
    public Route(String name, int routeNumber) {
        this.name = name == null ? "" : name.replace("\n", "")
                .replace("\r", "");
        this.routeNumber = routeNumber;
        this.vehicles = new ArrayList<>();
        this.route = new ArrayList<>();
    }

    @Override
    public String encode() {
        //{type},{name},{number}:{stop0}|{stop1}|...|{stopN}
        return this.toString();
    }

    public static Route decode(String routeString, List<Stop> existingStops)
            throws TransportFormatException {

        if (Objects.isNull(routeString) || routeString.isEmpty()) {
            throw new TransportFormatException();
        }
        if (Objects.isNull(routeString) || routeString.isEmpty()) {
            throw new TransportFormatException();
        }

        String[] routeStrings = routeString.split(":");
        if (routeStrings.length != 2) {
            throw new TransportFormatException();
        }
        String[] routeBaseStrings = routeStrings[0].split(",");
        if (routeBaseStrings.length != 3) {
            throw new TransportFormatException();
        }

        String type = routeBaseStrings[0];
        String name = routeBaseStrings[1];
        Integer number;
        try {
            number = Integer.valueOf(routeBaseStrings[2]);
        } catch (NumberFormatException e) {
            throw new TransportFormatException();
        }
        String[] stopStrings = routeStrings[1].split("\\|");

        List<Stop> stopList = new ArrayList<>();
        for (String stopName : stopStrings) {
            Optional<Stop> stopOptional = existingStops.stream().filter(stop -> Objects.equals(stop.getName(), stopName)).findFirst();
            Stop stop = stopOptional.orElseThrow(TransportFormatException::new);
            stopList.add(stop);
        }

        Route route;
        switch (type) {
            case "bus":
                route = new BusRoute(name, number);
                break;
            case "train":
                route = new TrainRoute(name, number);
                break;
            case "ferry":
                route = new FerryRoute(name, number);
                break;
            default:
                throw new TransportFormatException();

        }
        stopList.stream().forEach(e -> route.addStop(e));
        return route;
    }

    /**
     * Returns the name of the route.
     *
     * @return The route name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of the route.
     *
     * @return The route number.
     */
    public int getRouteNumber() {
        return routeNumber;
    }

    /**
     * Returns the stops which comprise this route.
     * <p>
     * <p>The order of the stops in the returned list should be the same as the
     * order in which the stops were added to the route.
     * <p>
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The stops making up the route.
     */
    public List<Stop> getStopsOnRoute() {
        return new ArrayList<>(route);
    }

    /**
     * Returns the first stop of the route (i.e. the first stop to be added to the
     * route).
     *
     * @return The start stop of the route.
     * @throws EmptyRouteException If there are no stops currently on the route
     */
    public Stop getStartStop() throws EmptyRouteException {
        if (route.isEmpty()) {
            throw new EmptyRouteException();
        }

        return route.get(0);
    }

    /**
     * Adds a stop to the route.
     * <p>
     * <p>If the given stop is null, it should not be added to the route.
     * <p>
     * <p>If this is the first stop to be added to the route, the given stop should
     * be recorded as the starting stop of the route. Otherwise, the given stop
     * should be recorded as a neighbouring stop of the previous stop on the route
     * (and vice versa) using the {@link Stop#addNeighbouringStop(Stop)} method.
     * <p>
     * <p>This route should also be added as a route of the given stop (if the given
     * stop is not null) using the {@link Stop#addRoute(Route)} method.
     *
     * @param stop The stop to be added to this route.
     */
    public void addStop(Stop stop) {
        if (stop == null) {
            return;
        }

        stop.addRoute(this);
        route.add(stop);

        // return if this was the first stop
        if (route.size() == 1) {
            return;
        }

        Stop previous = route.get(route.size() - 2);
        previous.addNeighbouringStop(stop);
        stop.addNeighbouringStop(previous);
    }

    /**
     * Returns the public transport vehicles currently on this route.
     * <p>
     * <p>No specific order is required for the public transport objects in the
     * returned list.
     * <p>
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The vehicles currently on the route.
     */
    public List<PublicTransport> getTransports() {
        return new ArrayList<>(this.vehicles);
    }

    /**
     * Adds a vehicle to this route.
     * <p>
     * <p>If the given transport is null, it should not be added to the route.
     * <p>
     * <p>The method should check for the transport being null first, then for an
     * empty route, and then for incompatible types (in that order).</p>
     *
     * @param transport The vehicle to be added to the route.
     * @throws EmptyRouteException       If there are not yet any stops on the route.
     * @throws IncompatibleTypeException If the given vehicle is of the incorrect
     *                                   type for this route. This depends on the type of the route, i.e. a
     *                                   BusRoute can only accept Bus instances.
     */
    public void addTransport(PublicTransport transport)
            throws EmptyRouteException, IncompatibleTypeException {
        if (transport == null) {
            return;
        }

        if (route.isEmpty()) {
            throw new EmptyRouteException();
        }

        if (!getType().equals(transport.getType())) {
            throw new IncompatibleTypeException();
        }

        vehicles.add(transport);
    }

    /**
     * Compares this stop with another object for equality.
     * <p>
     * Two routes are equal if their names and route numbers are equal.
     * <p>
     * {@inheritDoc}
     *
     * @param other The other object to compare for equality.
     * @return True if the objects are equal (as defined above), false otherwise
     * (including if other is null or not an instance of the {@link Route}
     * class.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Route)) {
            return false;
        }
        Route otherRoute = (Route) other;
        return name.equals(((Route) other).getName())
                && routeNumber == otherRoute.getRouteNumber();
    }

    @Override
    public int hashCode() {
        return routeNumber;
    }

    /**
     * Returns the type of this route.
     *
     * @return The type of the route (see subclasses)
     */
    public abstract String getType();

    /**
     * Creates a string representation of a route in the format:
     * <p>
     * <p>'{type},{name},{number}:{stop0}|{stop1}|...|{stopN}'
     * <p>
     * <p>without the surrounding quotes, and where {type} is replaced by the type
     * of the route, {name} is replaced by the name of the route, {number} is replaced
     * by the route number, and {stop0}|{stop1}|...|{stopN} is replaced by a list of
     * the names of the stops stops making up the route. For example:
     * <p>
     * <p>bus,red,1:UQ Lakes|City|Valley
     *
     * @return A string representation of the route.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getType()).append(",");
        builder.append(name).append(",").append(routeNumber);
        builder.append(":");

        for (Stop stop : route) {
            builder.append(stop.getName()).append("|");
        }

        if (!route.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }
}
