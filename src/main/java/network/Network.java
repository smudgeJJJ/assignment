package network;

import exceptions.TransportFormatException;
import routes.Route;
import stops.Stop;
import vehicles.PublicTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 运输网络
 *
 * @author mazhenjie
 * @since 2019/4/14
 */
public class Network {

    /**
     * 文件名
     */
    private String filename;

    /**
     * 线路
     */
    private List<Route> routes = new ArrayList<>();

    /**
     * 停靠站
     */
    private List<Stop> stops = new ArrayList<>();

    /**
     * 交通工具
     */
    private List<PublicTransport> vehicles = new ArrayList<>();

    public Network() {
    }

    public Network(String filename) throws IOException, TransportFormatException {
        this.filename = filename;
        //todo IO
    }

    public void addRoute(Route route) {
        if (Objects.isNull(route)) {
            return;
        }
        this.routes.add(route);
    }

    public void addStop(Stop stop) {
        if (Objects.isNull(stop)) {
            return;
        }
        this.stops.add(stop);
    }

    public void addStops(List<Stop> stops) {
        if (Objects.isNull(stops) || stops.isEmpty()) {
            return;
        }
        if (stops.contains(null)) {
            return;
        }
        this.stops.addAll(stops);
    }

    public void addVehicle(PublicTransport vehicle) {
        if (Objects.isNull(vehicle)) {
            return;
        }
        this.vehicles.add(vehicle);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<PublicTransport> getVehicles() {
        return vehicles;
    }

    public void save(String filename) throws IOException {
        //todo io
    }
}
