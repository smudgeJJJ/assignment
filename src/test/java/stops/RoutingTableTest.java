package stops;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author mazhenjie
 * @since 2019-05-21
 */
public class RoutingTableTest {
    Stop stop = new Stop("stop", 0, 0);
    RoutingTable routingTable = new RoutingTable(stop);

    Stop stop1 = new Stop("stop1", 1, 0);
    Stop stop2 = new Stop("stop2", 0, 0);
    Stop stop3 = new Stop("stop3", 1, 0);

    @Test
    public void addNeighbour() {
        Stop stop1 = new Stop("stop1", 1, 0);
        routingTable.addNeighbour(stop1);
        Assert.assertEquals(stop1, routingTable.getStop());
    }

    @Test
    public void addOrUpdateEntry() {
        boolean result = routingTable.addOrUpdateEntry(stop1, stop1.distanceTo(stop2), stop2);
        Assert.assertTrue(result);
        boolean result1 = routingTable.addOrUpdateEntry(stop1, stop1.distanceTo(stop3), stop3);
        Assert.assertTrue(result1);
    }

    @Test
    public void costTo() {
        routingTable.addOrUpdateEntry(stop1, stop1.distanceTo(stop2), stop2);
        int cost = routingTable.costTo(stop1);
        Assert.assertEquals(1, cost);
    }

    @Test
    public void transferEntries() {
        stop2.addNeighbouringStop(stop3);
        routingTable.addOrUpdateEntry(stop2, stop1.distanceTo(stop2), stop1);
        boolean result = routingTable.transferEntries(stop3);
        Assert.assertTrue(result);
    }

    @Test
    public void traverseNetwork() {
        stop2.addNeighbouringStop(stop3);
        routingTable.addOrUpdateEntry(stop2, stop1.distanceTo(stop2), stop1);
        List<Stop> list = routingTable.traverseNetwork();
        Assert.assertEquals(list.size(), 3);
    }
}