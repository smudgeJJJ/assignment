package network;

import exceptions.DuplicateStopException;
import exceptions.TransportFormatException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import routes.BusRoute;
import routes.Route;
import stops.Stop;
import vehicles.Bus;
import vehicles.PublicTransport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mazhenjie
 * @since 2019/4/20
 */
public class NetWorkTest {

    Network network = new Network();

    private static final String ERROR = "error";

    private Stop stop;

    static {

    }

    @Before
    public void buildNetWork() throws DuplicateStopException, IOException {
        stop = new Stop("stop1", 1, 1);
        Stop stop1 = new Stop("stop2", 2, 1);
        Route route = new BusRoute("route", 1);
        route.addStop(stop);
        route.addStop(stop1);
        PublicTransport publicTransport = new Bus(1, 1, route, "bus1");

        network.addStop(stop);
        network.addStop(stop1);
        network.addRoute(route);
        network.addVehicle(publicTransport);

        BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR));
        writer.write("1");
        writer.close();
    }

    @Test
    public void testInit() throws IOException, TransportFormatException {
        Network network1 = new Network("correct1");
        Assert.assertEquals(network.getRoutes(), network1.getRoutes());
        Assert.assertEquals(network.getStops(), network1.getStops());
        //PublicTransport没有重写hashCodeAndEquals，不能直接比较
        Assert.assertEquals(network.getVehicles().size(), network1.getVehicles().size());
    }

    @Test(expected = TransportFormatException.class)
    public void testInitError() throws IOException, TransportFormatException {
        new Network("error");
    }

    @Test
    public void save() throws IOException {
        network.save("correct1");
    }

    @Test(expected = DuplicateStopException.class)
    public void testAddStop() throws DuplicateStopException {
        network.addStop(stop);
    }

    @Test(expected = DuplicateStopException.class)
    public void testAddStopListError() throws DuplicateStopException {
        List<Stop> stopList = new ArrayList<Stop>() {{
            add(stop);
        }};
        network.addStops(stopList);
    }

    @Test
    public void testAddStopList() throws DuplicateStopException {
        List<Stop> stopList = new ArrayList<Stop>() {{
            add(new Stop("stop33", 2, 1));
        }};
        network.addStops(stopList);
        Assert.assertEquals(network.getStops().size(), 3);
    }
}
