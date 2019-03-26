package stops;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import passengers.Passenger;
import routes.BusRoute;
import routes.FerryRoute;
import routes.Route;
import vehicles.Bus;
import vehicles.Ferry;
import vehicles.PublicTransport;

/**
 * @author mazhenjie
 * @since 2019/3/24
 */
public class StopTest {
    private Stop stop;
    private Stop stop1;
    private Route route;
    private Stop stop2;
    private Passenger passenger;
    private PublicTransport publicTransport;

    @Before
    public void setUp() throws Exception {
        stop = new Stop("stop", 0, 0);
        stop1 = new Stop("stop1", 1, 1);
        stop2 = new Stop("stop2", 2, 2);
        route = new BusRoute(1, "route1");
        route.addStop(stop1);
        route.addStop(stop2);

        passenger = new Passenger("testName1", stop1);
        stop1.addPassenger(new Passenger("testName2", stop1));
        stop1.addPassenger(new Passenger("testName3", stop1));

        publicTransport = new Bus(1, 30, route, "BUS1");
        publicTransport.addPassenger(passenger);
    }

    @Test
    public void addRoute() {
        stop1.addRoute(route);
        Assert.assertEquals(stop1.getRoutes().get(0), route);
    }

    @Test
    public void addNeighbouringStop() {
        stop.addNeighbouringStop(null);
        Assert.assertTrue(stop.getNeighbours().size() == 0);
        stop1.addNeighbouringStop(stop);
        Assert.assertTrue(stop1.getNeighbours().contains(stop));
        stop1.addNeighbouringStop(stop);
        Assert.assertTrue(stop1.getNeighbours().size() == 2);
    }

    @Test
    public void addPassenger() {
        stop.addPassenger(passenger);
        Assert.assertEquals(stop.getWaitingPassengers().get(0), passenger);
    }

    @Test
    public void isAtStop() {
        Assert.assertTrue(!stop1.isAtStop(publicTransport));
        stop1.transportArrive(publicTransport);
        Assert.assertTrue(stop1.isAtStop(publicTransport));
    }

    @Test
    public void transportArrive() {
        stop.transportArrive(null);
        Assert.assertTrue(stop.getVehicles().isEmpty());

        PublicTransport ferry = new Ferry(2, 100, new FerryRoute(2, "ferryName"), "ferryType");
        stop1.transportArrive(publicTransport);
        Assert.assertTrue(stop1.getWaitingPassengers().size() == 3);
        Assert.assertTrue(stop1.isAtStop(publicTransport));
        stop1.transportArrive(publicTransport);
        Assert.assertTrue(stop1.getWaitingPassengers().size() == 3);
    }

    @Test
    public void transportDepart() {
        stop1.transportArrive(publicTransport);
        Assert.assertTrue(stop1.isAtStop(publicTransport));

        stop1.transportDepart(publicTransport, stop2);
        Assert.assertTrue(!stop1.isAtStop(publicTransport));
        Assert.assertTrue(stop2.isAtStop(publicTransport));
        Assert.assertEquals(publicTransport.getCurrentStop(), stop2);

    }

    @Test
    public void distanceTo() {
        Assert.assertTrue(stop1.distanceTo(stop2) == 2);
    }

    @Test
    public void getName() {
        Assert.assertEquals(stop1.getName(), "stop1");

    }

    @Test
    public void getX() {
        Assert.assertTrue(stop1.getX() == 1);
    }

    @Test
    public void getY() {
        Assert.assertTrue(stop1.getY() == 1);

    }

    @Test
    public void getNeighbours() {
        Assert.assertEquals(stop2.getNeighbours().get(0), stop1);

    }

    @Test
    public void getRoutes() {
        Assert.assertEquals(stop1.getRoutes().get(0), route);
    }

    @Test
    public void getVehicles() {
        stop1.transportArrive(publicTransport);
        Assert.assertEquals(stop1.getVehicles().get(0), publicTransport);
    }

    @Test
    public void getWaitingPassengers() {
        Assert.assertTrue(stop1.getWaitingPassengers().size() == 2);
    }
}