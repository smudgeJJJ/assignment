package passengers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stops.Stop;

/**
 * @author mazhenjie
 * @since 2019/3/24
 */
public class PassengerTest {
    private static final String NAME = "testName";
    private static final String NO_NAME = "Anonymous passenger";
    private Passenger passengerOnlyName;
    private Stop stop;
    private Stop newStop;
    private Passenger passenger;

    @Before
    public void setUp() throws Exception {
        passengerOnlyName = new Passenger("\rtestName\n");
        stop = new Stop("testStop", 1, 1);
        newStop = new Stop("testStopNew", 2, 2);
        passenger = new Passenger(null, stop);
    }

    @Test
    public void getName() {
        Assert.assertEquals("", passenger.getName());
        Assert.assertEquals(NAME, passengerOnlyName.getName());
    }

    @Test
    public void getDestination() {
        Assert.assertEquals(stop, passenger.getDestination());
    }

    @Test
    public void setDestination() {
        passenger.setDestination(newStop);
        Assert.assertEquals(newStop, passenger.getDestination());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(NO_NAME, passenger.toString());
        Assert.assertEquals(String.format("Passenger named %s", NAME), passengerOnlyName.toString());
    }

}