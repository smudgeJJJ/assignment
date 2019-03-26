package routes;

/**
 * 火车线路
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class TrainRoute extends Route {
    /**
     * 火车类型常量
     */
    private static final String TRAIN_TYPE = "train";

    public TrainRoute(int routeNumber, String name) {
        super(routeNumber, name);
    }

    @Override
    public String getType() {
        return TRAIN_TYPE;
    }
}
