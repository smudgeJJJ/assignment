package vehicles;

import routes.Route;

/**
 * Represents a ferry in the transportation network.
 */
public class Ferry extends PublicTransport {
    // the type of the ferry
    private String ferryType;

    /**
     * Creates a new Ferry object with the given id, capacity, route, and type.
     * <p>
     * <p>Should meet the specification of {@link PublicTransport#PublicTransport(int, int, Route)},
     * as well as extending it to include the following:
     * <p>
     * <p>If the given ferryType is null or empty, the string "CityCat" should be
     * stored instead. If the ferry type contains any newline characters ('\n')
     * or carriage returns ('\r'), they should be removed from the string before
     * it is stored.
     *
     * @param id        The identifying number of the ferry.
     * @param capacity  The maximum capacity of the ferry.
     * @param route     The route this ferry is on.
     * @param ferryType The type of the ferry (e.g. CityCat).
     */
    public Ferry(int id, int capacity, Route route, String ferryType) {
        super(id, capacity, route);
        this.ferryType = ferryType == null || ferryType.isEmpty() ? "CityCat" :
                ferryType.replace("\n", "").replace("\r", "");
    }

    @Override
    public String encode() {
        return String.format("%s,%s", super.encode(), this.ferryType);
    }

    /**
     * Returns the type of this ferry.
     *
     * @return The type of the ferry.
     */
    public String getFerryType() {
        return ferryType;
    }
}
