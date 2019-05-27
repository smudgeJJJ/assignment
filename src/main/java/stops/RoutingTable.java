package stops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

/**
 * 线路表
 *
 * @author mazhenjie
 * @since 2019/5/15
 */
public class RoutingTable {

    /**
     * 存储停靠站
     */
    private List<Stop> stops;

    /**
     * 停靠站关联条目
     */
    private List<RoutingEntry> routingEntries;

    public RoutingTable(Stop initialStop) {
        this.stops = new ArrayList<Stop>() {{
            add(initialStop);
        }};
        this.routingEntries = new ArrayList<RoutingEntry>() {{
            add(new RoutingEntry(initialStop, 0));
        }};
    }

    /**
     * 将给定的停靠点添加为此表中存储的停靠点的邻居。
     * 应该在此表中添加相邻的停靠点作为目的地，到达该目的地的成本只是该表的停靠点与给定的邻居停靠点之间的曼哈顿距离。
     * <p>
     * 如果表中已存在给定的邻居，则应更新它（如addOrUpdateEntry（Stop，int，Stop）中所定义）。
     * <p>
     * 此表的停止和新邻居停止之间的“中间”/“下一个”停止应该只是邻居停止本身。
     * <p>
     * 将新邻居添加为条目后，应使用synchronize（）方法将此表与网络的其余部分同步。
     *
     * @param neighbour
     */
    public void addNeighbour(Stop neighbour) {
        for (Stop stop : this.stops) {
            stop.addNeighbouringStop(neighbour);
            if (this.addOrUpdateEntry(neighbour, stop.distanceTo(neighbour), stop)) {
                this.synchronise();
            }
            break;
        }
    }

    /**
     * 如果表中目前没有目的地条目，则应添加给定目的地的新条目，其中包含给定成本的RoutingEntry和下一个（中间）停止。
     * 如果已存在给定目标的条目，并且newCost低于与目标关联的当前成本，则应更新该条目以使给定的newCost和next（中间）停止。
     * <p>
     * 如果已存在给定目标的条目，但newCost大于或等于与目标关联的当前成本，则该条目应保持不变。
     *
     * @param destination
     * @param newCost
     * @param intermediate
     * @return
     */
    public boolean addOrUpdateEntry(Stop destination, int newCost, Stop intermediate) {
        if (!this.stops.contains(destination)) {
            this.stops.add(destination);
            this.routingEntries.add(new RoutingEntry(intermediate, newCost));
            return true;
        }

        int index = this.stops.indexOf(destination);
        if (newCost < routingEntries.get(index).getCost()) {
            this.routingEntries.set(index, new RoutingEntry(intermediate, newCost));
            return true;
        }

        return false;
    }

    public int costTo(Stop stop) {
        if (!this.stops.contains(stop)) {
            return Integer.MAX_VALUE;
        }

        int index = this.stops.indexOf(stop);
        return this.routingEntries.get(index).getCost();
    }

    public Map<Stop, Integer> getCosts() {
        Map map = new HashMap(this.stops.size());
        for (int i = 0; i < this.stops.size(); i++) {
            map.put(this.stops.get(i), this.routingEntries.get(i).getCost());
        }
        return map;
    }

    public Stop getStop() {
        return this.stops.get(this.stops.size() - 1);
    }

    public Stop nextStop(Stop destination) {
        if (!this.stops.contains(destination)) {
            return null;
        }
        int index = this.stops.indexOf(destination);
        return this.routingEntries.get(index).getNext();
    }

    /**
     * 使此路由表与网络中的其他表同步。
     * 在每次迭代中，必须考虑网络中每个可以通过此表的停止（由traverseNetwork（）返回）到达的停靠点。对于网络中的每个停止x，必须访问其每个邻居，并且必须将来自x的条目传送到每个邻居（使用transferEntries（Stop）方法）。
     * <p>
     * 如果这些传输中的任何一个导致对正在传输条目的表进行更改，则必须再次重复整个过程。这些迭代应该继续发生，直到网络中的任何表都没有发生变化。
     * <p>
     * 此过程旨在处理需要在整个网络中传播的更改，这可能需要多次迭代。
     */
    public void synchronise() {
        List<Stop> stopList = this.traverseNetwork();
        boolean isChange = false;
        for (Stop stop : stopList) {
            isChange = this.transferEntries(stop);
        }
    }

    /**
     * 使用此路由表中的条目更新给定其他站点的路由表中的条目。
     * 如果此路由表具有其他停靠表不具有的条目，则应将条目添加到另一个表（如addOrUpdateEntry（Stop，int，Stop）中所定义），并更新成本以包括距离。
     * <p>
     * 如果此路由表具有其他停靠表所具有的条目，并且新成本将低于与其现有条目关联的条目，则应更新其条目（如addOrUpdateEntry（Stop，int，Stop）中所定义）。
     * <p>
     * 如果此路由表具有其他停靠表所具有的条目，但新成本将大于或等于与其现有条目关联的条目，则其条目应保持不变。
     *
     * @param other
     * @return
     */
    public boolean transferEntries(Stop other) {
        if (!this.getStop().getNeighbours().contains(other)) {
            return false;
        }
        boolean isChange = false;
        RoutingTable routingTable = other.getRoutingTable();
        Map<Stop, Integer> map = routingTable.getCosts();
        for (int i = 0; i < this.stops.size(); i++) {
            Stop stop = this.stops.get(i);
            RoutingEntry entry = this.routingEntries.get(i);
            if (Objects.isNull(map.get(stop))) {
                routingTable.addOrUpdateEntry(other, other.distanceTo(stop), stop);
                isChange = true;
                continue;
            }
            if (map.get(stop) < entry.getCost()) {
                this.addOrUpdateEntry(stop, stop.distanceTo(other), other);
                isChange = true;
                continue;
            }
        }
        return isChange;
    }

    /**
     * 执行遍历网络中所有停靠点的操作，并返回可从此表中存储的停靠点访问的每个停靠点的列表。
     * 首先创建一个空的停止列表和一个空的停止堆栈。将RoutingTable的Stop停在堆栈上。
     * 虽然堆栈不是空的，
     * 从堆栈中弹出顶部停止（当前）。
     * 对于那个站点的邻居，
     * 如果它们不在列表中，则将它们添加到堆栈中。
     * 然后将当前Stop添加到列表中。
     * 返回看到的停靠点列表。
     *
     * @return
     */
    public List<Stop> traverseNetwork() {
        List<Stop> stopList = new ArrayList<>();
        Stack<Stop> stopStack = new Stack<>();
        for (Stop stop : this.stops) {
            stopStack.push(stop);
        }
        while (!stopStack.empty()) {
            Stop popStop = stopStack.pop();
            for (Stop neighbour : popStop.getNeighbours()) {
                if (!stopList.contains(neighbour)) {
                    stopStack.push(neighbour);
                }
            }
            stopList.add(popStop);
        }
        return stopList;
    }

}
