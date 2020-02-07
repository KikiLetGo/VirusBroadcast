package entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 城市描述对象
 *
 * @ClassName: City
 * @Description: 城市描述对象
 * @author: Bruce Young
 * @date: 2020年02月02日 17:48
 */
public class City {
    @Getter
    @Setter
    private int centerX;
    @Getter
    @Setter
    private int centerY;

    public City(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }
}
