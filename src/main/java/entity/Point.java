package entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 位置坐标基类
 *
 * @ClassName: Point
 * @Description: 位置坐标基类
 * @author: Bruce Young
 * @date: 2020年02月02日 20:59
 */
public class Point {
    @Setter
    @Getter
    private int x;
    @Setter
    @Getter
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 位移
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
