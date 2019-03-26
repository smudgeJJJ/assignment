package routes;

import exceptions.EmptyRouteException;
import exceptions.IncompatibleTypeException;
import stops.Stop;
import vehicles.PublicTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 线路基类
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public abstract class Route {

    /**
     * 线路编号
     */
    private int routeNumber;

    /**
     * 线路名
     */
    private String name;

    /**
     * 线路停靠站
     */
    private List<Stop> stopsOnRoute;

    /**
     * 公共交通
     */
    private List<PublicTransport> transports;

    public Route(int routeNumber, String name) {
        this.routeNumber = routeNumber;
        this.stopsOnRoute = new ArrayList<>();
        this.transports = new ArrayList<>();
        if (name == null) {
            this.name = "";
            return;
        }
        this.name = name.replaceAll("[\\n,\\r]", "");
    }

    /**
     * 获取线路类型
     *
     * @return
     */
    public abstract String getType();

    /**
     * 增加停靠站
     *
     * @param stop
     */
    public void addStop(Stop stop) {
        if (Objects.isNull(stop)) {
            return;
        }

        //非始发站时，将线路当前最后一站和增加的站互相关联为相邻站
        if (!stopsOnRoute.isEmpty()) {
            Stop lastStop = stopsOnRoute.get(stopsOnRoute.size() - 1);
            stop.addNeighbouringStop(lastStop);
            lastStop.addNeighbouringStop(stop);
        }
        //增加站
        stopsOnRoute.add(stop);
        //站记录此线路
        stop.addRoute(this);
    }

    /**
     * 增加交通工具
     *
     * @param transport
     * @throws EmptyRouteException
     * @throws IncompatibleTypeException
     */
    public void addTransport(PublicTransport transport) throws EmptyRouteException, IncompatibleTypeException {
        if (Objects.isNull(transport)) {
            return;
        }

        //线路为空抛EmptyRouteException
        if (this.stopsOnRoute.isEmpty()) {
            throw new EmptyRouteException();
        }

        //运输类型不匹配抛IncompatibleTypeException
        if (!Objects.equals(transport.getType(), this.getType())) {
            throw new IncompatibleTypeException();
        }

        transports.add(transport);
    }

    /**
     * 查找始发站
     * 没有始发站时throw EmptyRouteException
     *
     * @return
     */
    public Stop getStartStop() throws EmptyRouteException {
        return stopsOnRoute.stream().findFirst().orElseThrow(EmptyRouteException::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Route route = (Route) o;
        return routeNumber == route.routeNumber &&
                Objects.equals(name, route.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeNumber, name);
    }

    @Override
    public String toString() {
        //格式{type},{name},{number}:{stop0}|{stop1}|...|{stopN}
        return String.format("%s,%s,%s:%s", getType(), name, routeNumber, buildStopsName());
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public String getName() {
        return name;
    }

    public List<Stop> getStopsOnRoute() {
        return stopsOnRoute;
    }

    public List<PublicTransport> getTransports() {
        return transports;
    }

    /**
     * 构造停靠站names
     *
     * @return
     */
    private String buildStopsName() {
        StringBuilder stringBuilder = new StringBuilder();
        stopsOnRoute.stream().forEach(e -> stringBuilder.append(e.getName()).append("|"));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
