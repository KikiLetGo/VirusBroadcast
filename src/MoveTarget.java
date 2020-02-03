/**
 * @ClassName: MoveTarget
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:47
 */
public class MoveTarget {
    private int x;
    private int y;
    private boolean arrived=false;

    public MoveTarget(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }
}
