package routes;

/**
 * 渡轮线路
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class FerryRoute extends Route {
    /**
     * 渡轮类型常量
     */
    private static final String FERRY_TYPE = "ferry";

    public FerryRoute(int routeNumber, String name) {
        super(routeNumber, name);
    }

    @Override
    public String getType() {
        return FERRY_TYPE;
    }
}
