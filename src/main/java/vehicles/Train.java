package vehicles;

import routes.Route;

/**
 * 火车
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class Train extends PublicTransport {

    /**
     * 托架数
     */
    private int carriageCount;

    public Train(int id, int capacity, Route route, int carriageCount) {
        super(id, capacity, route);
        this.carriageCount = carriageCount <= 0 ? 1 : carriageCount;
    }

    public int getCarriageCount() {
        return carriageCount;
    }
}
