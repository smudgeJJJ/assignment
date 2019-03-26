package vehicles;

import routes.Route;

/**
 * 巴士
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class Bus extends PublicTransport {

    /**
     * 注册号
     */
    private String registrationNumber;

    public Bus(int id, int capacity, Route route, String registrationNumber) {
        super(id, capacity, route);
        if (registrationNumber == null) {
            this.registrationNumber = "";
            return;
        }
        this.registrationNumber = registrationNumber.replaceAll("[\\n,\\r]", "");
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
}
