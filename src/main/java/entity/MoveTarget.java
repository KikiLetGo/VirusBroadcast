package entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 位移目标对象
 *
 * @ClassName: MoveTarget
 * @Description: 位移目标对象
 * @author: Bruce Young
 * @date: 2020年02月02日 17:47
 */
public class MoveTarget {
    @Getter
    @Setter
    private int x;
    @Getter
    @Setter
    private int y;
    @Getter
    @Setter
    private boolean arrived = false;//是否到达目标点

    public MoveTarget(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
