package network;

import exceptions.TransportFormatException;
import routes.Route;
import stops.Stop;
import vehicles.PublicTransport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * 文件存放所在目录相对路径
     * 参考工程结构改名
     * TODO change path
     */
    private static final String FILE_PATH = "src/main/resources";
    /**
     * 后缀
     */
    private static final String FILE_SUFFIX = ".txt";
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

        if (Objects.isNull(filename) || filename.isEmpty()) {
            throw new IOException();
        }

        this.filename = filename;
        Path path = this.getPath(filename);
        List<String> lines = Files.readAllLines(path);
        try {
            //读取stops
            Integer stopSize = Integer.valueOf(lines.get(0));
            List<String> stops = lines.subList(1, stopSize + 1);
            for (String s : stops) {
                this.addStop(Stop.decode(s));
            }

            //读取routes
            Integer routeSize = Integer.valueOf(lines.get(stopSize + 1));
            List<String> routes = lines.subList(stopSize + 2, stopSize + 2 + routeSize);
            for (String s : routes) {
                this.addRoute(Route.decode(s, this.getStops()));
            }

            //读取vehicles
            Integer vehiclesSize = Integer.valueOf(lines.get(stopSize + 2 + routeSize));
            List<String> vehicles = lines.subList(stopSize + 3 + routeSize, stopSize + 3 + routeSize + vehiclesSize);
            for (String s : vehicles) {
                this.addVehicle(PublicTransport.decode(s, this.getRoutes()));
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new TransportFormatException();
        }
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
        Path path = this.getPath(filename);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Files.write(path, buildLines());
    }

    private Path getPath(String filename) {
        return Paths.get(FILE_PATH, filename + FILE_SUFFIX);
    }

    private List<String> buildLines() {
        List<String> lines = new ArrayList<>();
        lines.add(String.valueOf(stops.size()));
        stops.stream().forEach(e -> lines.add(e.encode()));
        lines.add(String.valueOf(routes.size()));
        routes.stream().forEach(e -> lines.add(e.encode()));
        lines.add(String.valueOf(vehicles.size()));
        vehicles.stream().forEach(e -> lines.add(e.encode()));
        return lines;
    }
}
