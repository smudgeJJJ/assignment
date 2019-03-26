package passengers;

import stops.Stop;

import java.util.Objects;

/**
 * 乘客基类
 *
 * @author mazhenjie
 * @since 2019/3/24
 */
public class Passenger {

    /**
     * 乘客姓名
     */
    private String name;

    /**
     * 乘客停靠站点
     */
    private Stop destination;

    /**
     * 只有姓名的构造器
     * <p>
     * Passenger(null)           this.name=''
     * Passenger("\na\r\n")      this.name='a'
     *
     * @param name
     */
    public Passenger(String name) {
        if (name == null) {
            this.name = "";
            return;
        }
        this.name = name.replaceAll("[\\n,\\r]", "");
    }

    /**
     * 指定姓名和停靠站的构造器
     *
     * @param name
     * @param destination
     */
    public Passenger(String name, Stop destination) {
        this(name);
        this.destination = destination;
    }

    @Override
    public String toString() {
        //格式Anonymous passenger 或 Passenger named {name}
        return Objects.equals("", this.name) ? "Anonymous passenger" : String.format("Passenger named %s", this.name);
    }

    public String getName() {
        return name;
    }

    public Stop getDestination() {
        return destination;
    }

    public void setDestination(Stop destination) {
        this.destination = destination;
    }
}
