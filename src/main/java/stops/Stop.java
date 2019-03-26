package stops;

import exceptions.NoNameException;
import passengers.Passenger;
import routes.Route;
import vehicles.PublicTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 停靠站类
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class Stop {

    /**
     * 站名
     */
    private String name;

    /**
     * 站x坐标
     */
    private int x;

    /**
     * 站y坐标
     */
    private int y;

    /**
     * 任何路线上与此相邻的所有站点
     */
    private List<Stop> neighbours;

    /**
     * 有此站的所有线路
     */
    private List<Route> routes;

    /**
     * 当前在此站的所有车辆
     */
    private List<PublicTransport> vehicles;

    /**
     * 当前在此站候车的乘客
     */
    private List<Passenger> waitingPassengers;

    public Stop(String name, int x, int y) {
        if (name == null || name.isEmpty()) {
            throw new NoNameException();
        }
        this.x = x;
        this.y = y;
        this.name = name.replaceAll("[\\n,\\r]", "");
        this.neighbours = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.waitingPassengers = new ArrayList<>();
    }

    /**
     * 增加线路
     *
     * @param route
     */
    public void addRoute(Route route) {
        if (Objects.nonNull(route)) {
            this.routes.add(route);
        }
    }

    /**
     * 增加相邻站
     *
     * @param neighbour
     */
    public void addNeighbouringStop(Stop neighbour) {
        if (Objects.isNull(neighbour) || this.neighbours.contains(neighbour)) {
            return;
        }
        this.neighbours.add(neighbour);
    }

    /**
     * 增加乘客
     *
     * @param passenger
     */
    public void addPassenger(Passenger passenger) {
        if (Objects.nonNull(passenger)) {
            this.waitingPassengers.add(passenger);
        }
    }

    /**
     * 交通工具是否在站
     *
     * @param transport
     * @return
     */
    public boolean isAtStop(PublicTransport transport) {
        return Objects.nonNull(transport) && this.vehicles.contains(transport);
    }

    /**
     * 车辆到站
     *
     * @param transport
     */
    public void transportArrive(PublicTransport transport) {
        if (Objects.isNull(transport) || this.vehicles.contains(transport)) {
            return;
        }

        //卸乘客
        List<Passenger> unloadPassengers = transport.unload();
        //放站点
        this.waitingPassengers.addAll(unloadPassengers);
        //记录车辆
        this.vehicles.add(transport);

    }

    /**
     * 车辆出发
     *
     * @param transport
     * @param nextStop
     */
    public void transportDepart(PublicTransport transport, Stop nextStop) {
        //车辆为空或下一站为空或车辆不在此站直接返回
        if (Objects.isNull(transport) || Objects.isNull(nextStop) || !this.vehicles.contains(transport)) {
            return;
        }
        //离站
        vehicles.remove(transport);
        //更新车辆停靠站
        transport.travelTo(nextStop);
        //下一站进站
        nextStop.transportArrive(transport);
    }

    /**
     * 计算两站距离
     * <p>
     * 公式:abs(x1 - x2) + abs(y1 - y2)
     *
     * @param stop
     * @return
     */
    public int distanceTo(Stop stop) {
        if (Objects.isNull(stop)) {
            return -1;
        }
        return Math.abs(this.x - stop.getX()) + Math.abs(this.y - stop.getY());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stop stop = (Stop) o;
        return x == stop.x &&
                y == stop.y &&
                Objects.equals(name, stop.name) &&
                Objects.equals(routes, stop.routes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, y, routes);
    }

    @Override
    public String toString() {
        return this.name + ":" + this.x + ":" + this.y;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Stop> getNeighbours() {
        return neighbours;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<PublicTransport> getVehicles() {
        return vehicles;
    }

    public List<Passenger> getWaitingPassengers() {
        return waitingPassengers;
    }
}
