package vehicles;

import routes.BusRoute;
import routes.Route;

import java.util.Objects;

/**
 * 渡轮
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class Ferry extends PublicTransport {
    /**
     * 默认类型
     */
    private static final String DEFAULT_TYPE = "CityCat";
    /**
     * 渡轮类型
     */
    private String ferryType;

    public Ferry(int id, int capacity, Route route, String ferryType) {
        super(id, capacity, route);
        if (Objects.isNull(ferryType) || Objects.equals(ferryType, "")) {
            this.ferryType = DEFAULT_TYPE;
            return;
        }
        this.ferryType = ferryType.replaceAll("[\\n,\\r]", "");
    }

    public String getFerryType() {
        return ferryType;
    }
}
