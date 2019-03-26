package routes;

/**
 * 巴士线路
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class BusRoute extends Route {
    /**
     * 巴士类型常量
     */
    private static final String BUS_TYPE = "bus";

    public BusRoute(int routeNumber, String name) {
        super(routeNumber, name);
    }

    @Override
    public String getType() {
        return BUS_TYPE;
    }
}
