package virussim;

/**
 * @ClassName: MoveTarget
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:47
 */
public class MoveTarget extends Point {
    
    public MoveTarget(int x, int y) {
        super(x, y);
    }

    private boolean arrived = false;

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }
}
