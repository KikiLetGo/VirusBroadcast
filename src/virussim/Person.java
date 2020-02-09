package virussim;

import java.util.List;
import java.util.Random;

/**
 * 能够随机运动的民众
 *
 * @ClassName: Person
 * @Description: 能够随机运动的民众
 * @author: Bruce Young
 * @date: 2020年02月02日 17:05
 */

public class Person {
    private City city;
    private int x;
    private int y;
    private MoveTarget moveTarget;
    int sig = 1;

    double targetXU;
    double targetYU;
    double targetSig = 50;

    // public interface State {
    //     int NORMAL = 0;
    //     int SUSPECTED = NORMAL + 1;
    //     int SHADOW = SUSPECTED + 1;

    //     int CONFIRMED = SHADOW + 1;
    //     int FREEZE = CONFIRMED + 1;
    //     int CURED = FREEZE + 1;
    // }

    /**
     * The {@code State} enum is an alternative of the previous {@code State} interface
     * 
     * @author dy55
     */
    public enum State {
        NORMAL, SUSPECTED, SHADOW, CONFIRMED, FREEZE, DEATH, CURED
    }

    public Person(City city, int x, int y) {
        this.city = city;
        this.x = x;
        this.y = y;
        targetXU = 100 * new Random().nextGaussian() + x;
        targetYU = 100 * new Random().nextGaussian() + y;

    }

    public boolean wantMove() {
        double value = sig * new Random().nextGaussian() + Constants.u;
        return value > 0;
    }

    private State state = State.NORMAL;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    int infectedTime = 0;
    int confirmedTime = 0;
    int dieMoment = 0;// 死亡时刻，为0代表未确定，-1代表不会病死

    public boolean isInfected() {
        return state.compareTo(State.SHADOW) > 0;
    }

    public void beInfected() {
        state = State.SHADOW;
        infectedTime = MyPanel.worldTime;
    }

    public double distance(Person person) {
        return Math.sqrt(Math.pow(x - person.getX(), 2) + Math.pow(y - person.getY(), 2));
    }

    // private void freezy() {
    //     state = State.FREEZE;
    // }

    private void moveTo(int x, int y) {
        this.x += x;
        this.y += y;
    }

    private void action() {
        if (state == State.FREEZE) {
            return;
        }
        if (!wantMove()) {
            return;
        }
        if (moveTarget == null || moveTarget.isArrived()) {

            double targetX = targetSig * new Random().nextGaussian() + targetXU;
            double targetY = targetSig * new Random().nextGaussian() + targetYU;
            moveTarget = new MoveTarget((int) targetX, (int) targetY);

        }

        int dX = moveTarget.getX() - x;
        int dY = moveTarget.getY() - y;
        double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        if (length < 1) {
            moveTarget.setArrived(true);
            return;
        }
        int udX = (int) (dX / length);
        if (udX == 0 && dX != 0) {
            if (dX > 0) {
                udX = 1;
            } else {
                udX = -1;
            }
        }
        int udY = (int) (dY / length);
        if (udY == 0 && dY != 0) {
            if (dY > 0) {
                udY = 1;
            } else {
                udY = -1;
            }
        }

        if (x > 700) {
            moveTarget = null;
            if (udX > 0) {
                udX = -udX;
            }
        }
        moveTo(udX, udY);

        //        if(wantMove()){
        //        }

    }

    public void update() {
        if (state.compareTo(State.FREEZE) > 0) {
            return;
        }
        if (state == State.CONFIRMED && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
            Bed bed = Hospital.getInstance().pickBed();
            if (bed == null) {
                // 没有床位
                // System.out.println("隔离区没有空床位");
                if (Main.HOSPITAL_STATE.getText().isEmpty()) {
                    Main.HOSPITAL_STATE.setText("隔离区没有空床位");
                }
            } else {
                // 安置病人
                state = State.FREEZE;
                x = bed.getX();
                y = bed.getY();
                bed.setEmpty(false);
            }
        }
        
        // 增加一个正态分布用于潜伏期内随机发病时间
        double stdRnShadowtime = MathUtil.stdGaussian(25, Constants.SHADOW_TIME / 2);
        // 处理发病的潜伏期感染者
        if (MyPanel.worldTime - infectedTime > stdRnShadowtime && state == State.SHADOW) {
            state = State.CONFIRMED;// 潜伏者发病
            confirmedTime = MyPanel.worldTime;// 刷新时间
        }

        action();

        List<Person> people = PersonPool.getInstance().personList;
        if (state.compareTo(State.SHADOW) > 0) {
            return;
        }
        for (Person person : people) {
            if (person.getState() == State.NORMAL) {
                continue;
            }
            float random = new Random().nextFloat();
            if (random < Constants.BROAD_RATE && distance(person) < Constants.SAFE_DIST) {
                this.beInfected();
            }
        }
    }
}
