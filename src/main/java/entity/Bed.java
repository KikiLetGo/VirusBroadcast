package entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 床位
 *
 * @ClassName: Bed
 * @Description: 床位
 * @author: Bruce Young
 * @date: 2020年02月02日 21:00
 */
public class Bed extends Point {
    public Bed(int x, int y) {
        super(x, y);
    }

    /**
     * 是否占用了该床位
     */
    @Setter
    @Getter
    private boolean isEmpty = true;
}
