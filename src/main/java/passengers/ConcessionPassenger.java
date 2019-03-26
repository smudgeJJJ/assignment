package passengers;

import stops.Stop;

/**
 * 特价票乘客类
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class ConcessionPassenger extends Passenger {

    /**
     * 优惠码长度
     */
    private static final int CONCESSION_RULE_SIZE = 6;

    /**
     * 优惠码前缀
     */
    private static final String CONCESSION_RULE_PREFIX = "42";

    /**
     * 乘客优惠码
     */
    private int concessionId;

    public ConcessionPassenger(String name, Stop destination, int concessionId) {
        super(name, destination);
        this.concessionId = concessionId;
    }

    /**
     * 过期
     */
    public void expire() {
        this.concessionId = 0;
    }

    /**
     * 是否验证
     * <p>
     * 优惠码有效时返回true
     * 有效规则：42****
     *
     * @return
     */
    public boolean isValid() {
        String concessionId = String.valueOf(this.concessionId);
        if (concessionId.startsWith(CONCESSION_RULE_PREFIX) && concessionId.length() == CONCESSION_RULE_SIZE) {
            return true;
        }
        return false;
    }

    /**
     * 更新优惠码
     *
     * @param newId
     */
    public void renew(int newId) {
        this.concessionId = newId;
    }
}
