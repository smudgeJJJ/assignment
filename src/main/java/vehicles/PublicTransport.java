package vehicles;

import exceptions.EmptyRouteException;
import exceptions.OverCapacityException;
import passengers.Passenger;
import routes.Route;
import stops.Stop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 公共交通工具基类
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public abstract class PublicTransport {

    /**
     * 标识id
     */
    private int id;

    /**
     * 容量
     */
    private int capacity;

    /**
     * 线路
     */
    private Route route;

    /**
     * 当前停靠站
     */
    private Stop currentStop;

    /**
     * 所载乘客
     */
    private List<Passenger> passengers;

    public PublicTransport(int id, int capacity, Route route) {
        if (Objects.isNull(route)) {
            throw new RuntimeException("Route Can Not Be Empty!");
        }

        this.id = id;
        this.capacity = capacity < 0 ? 0 : capacity;
        this.route = route;
        this.passengers = new ArrayList<>();
        try {
            this.currentStop = route.getStartStop();
        } catch (EmptyRouteException e) {
            this.currentStop = null;
        }
    }

    /**
     * 当前人数
     *
     * @return
     */
    public int passengerCount() {
        return passengers.size();
    }

    /**
     * 获取线路类型
     *
     * @return
     */
    public String getType() {
        return route.getType();
    }

    /**
     * 指定乘客上车
     *
     * @param passenger
     * @throws OverCapacityException
     */
    public void addPassenger(Passenger passenger) throws OverCapacityException {
        if (Objects.isNull(passenger)) {
            return;
        }

        //人已满抛OverCapacityException
        if (passengers.size() >= capacity) {
            throw new OverCapacityException();
        }

        passengers.add(passenger);
    }

    /**
     * 指定乘客下车
     *
     * @param passenger
     * @return
     */
    public boolean removePassenger(Passenger passenger) {
        if (Objects.nonNull(passenger) && passengers.contains(passenger)) {
            return passengers.remove(passenger);
        }
        return false;
    }

    /**
     * 清客并返回所有乘客
     *
     * @return
     */
    public List<Passenger> unload() {
        List<Passenger> unloadPassenger = new ArrayList<>();
        unloadPassenger.addAll(passengers);
        passengers.clear();
        return unloadPassenger;
    }

    /**
     * 前往指定站
     *
     * @param stop
     */
    public void travelTo(Stop stop) {
        //指定站空或不在线路内时，交通工具不动
        if (Objects.isNull(stop) || Objects.isNull(route.getStopsOnRoute()) || !route.getStopsOnRoute().contains(stop)) {
            return;
        }
        this.currentStop = stop;
    }

    @Override
    public String toString() {
        //格式:{type} number {id} ({capacity}) on route {route}
        return String.format("%s number %s (%s) on route %s", getType(), id, capacity, route.getRouteNumber());
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Route getRoute() {
        return route;
    }

    public Stop getCurrentStop() {
        return currentStop;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
