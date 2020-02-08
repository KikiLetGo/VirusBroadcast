/**
 * 城市描述对象
 *
 * @ClassName: City
 * @Description: 城市描述对象
 * @author: Bruce Young
 * @date: 2020年02月02日 17:48
 */
public class City {
    private int centerX;
    private int centerY;

    public City(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
