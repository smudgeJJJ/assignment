package stops;

import java.util.Objects;

/**
 * 线路条目
 *
 * @author mazhenjie
 * @since 2019/5/15
 */
public class RoutingEntry {

    /**
     * 下一定靠站
     */
    private Stop next;

    /**
     * 花费
     */
    private int cost;

    public RoutingEntry() {
        this.cost = Integer.MAX_VALUE;
    }

    public RoutingEntry(Stop stop, int cost) {
        if (Objects.isNull(stop) || cost < 0) {
            this.next = null;
            this.cost = Integer.MAX_VALUE;
            return;
        }

        this.next = stop;
        this.cost = cost;
    }

    public Stop getNext() {
        return next;
    }

    public int getCost() {
        return cost;
    }
}
