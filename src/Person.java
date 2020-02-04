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
    /**
     * 人群流动意愿影响系数sigma
     */
    int sig = 1;

    /**
     * 正态随机位移目标位置
     */
    double targetXU;
    double targetYU;
    double targetSig = 50;

    /**
     * 人的状态
     */
    public interface State {
        int NORMAL = 0;//正常人，未感染的健康人
        int SUSPECTED = NORMAL + 1;//有暴露感染风险
        int SHADOW = SUSPECTED + 1;//潜伏期
        int CONFIRMED = SHADOW + 1;//已确诊为感染病人
        int FREEZE = CONFIRMED + 1;//隔离治疗，禁止位移
        int CURED = FREEZE + 1;//已治愈出院
    }

    public Person(City city, int x, int y) {
        this.city = city;
        this.x = x;
        this.y = y;
        targetXU = 100 * new Random().nextGaussian() + x;
        targetYU = 100 * new Random().nextGaussian() + y;

    }

    /**
     * 流动意愿标准化
     * <p>
     * 流动意愿标准化后判断是在0的左边还是右边从而决定是否流动。
     * <p>
     * 设X随机变量为服从正态分布，sigma是影响分布形态的系数，从而影响整体人群流动意愿分布
     * u值决定正态分布的中轴是让更多人群偏向希望流动或者希望懒惰。
     * <p>
     * value的推导：
     * StdX = (X-u)/sigma
     * X = sigma * StdX + u
     *
     * @return
     */
    public boolean wantMove() {
        double stdX = new Random().nextGaussian();
        double value = sig * stdX + Constants.u;
        return value > 0;
    }

    private int state = State.NORMAL;

    public int getState() {
        return state;
    }

    public void setState(int state) {
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


    public boolean isInfected() {
        return state >= State.SHADOW;
    }

    public void beInfected() {
        state = State.SHADOW;
        infectedTime = MyPanel.worldTime;
    }

    /**
     * 计算两点之间的直线距离
     *
     * @param person
     * @return
     */
    public double distance(Person person) {
        return Math.sqrt(Math.pow(x - person.getX(), 2) + Math.pow(y - person.getY(), 2));
    }

    /**
     * 住院
     */
    private void freezy() {
        state = State.FREEZE;
    }

    /**
     * 位移
     *
     * @param x
     * @param y
     */
    private void moveTo(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * 不同状态下的单个人实例运动行为
     */
    private void action() {
        if (state == State.FREEZE) {
            return;
        }
        if (!wantMove()) {
            return;
        }
        //存在流动意愿的，将进行流动，流动位移仍然遵循标准正态分布
        if (moveTarget == null || moveTarget.isArrived()) {

            double targetX = targetSig * new Random().nextGaussian() + targetXU;
            double targetY = targetSig * new Random().nextGaussian() + targetYU;
            moveTarget = new MoveTarget((int) targetX, (int) targetY);

        }

        //计算运动位移
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
        //FIXED: 修正一处错误
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

    private float SAFE_DIST = 2f;

    public void update() {
        //@TODO找时间改为状态机
        if (state >= State.FREEZE) {
            return;
        }
        if (state == State.CONFIRMED && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
            Bed bed = Hospital.getInstance().pickBed();
            if (bed == null) {
                System.out.println("隔离区没有空床位");
            } else {
                state = State.FREEZE;
                x = bed.getX();
                y = bed.getY();
                bed.setEmpty(false);
            }
        }
        if (MyPanel.worldTime - infectedTime > Constants.SHADOW_TIME && state == State.SHADOW) {
            state = State.CONFIRMED;
            confirmedTime = MyPanel.worldTime;
        }

        action();

        List<Person> people = PersonPool.getInstance().personList;
        if (state >= State.SHADOW) {
            return;
        }
        for (Person person : people) {
            if (person.getState() == State.NORMAL) {
                continue;
            }
            float random = new Random().nextFloat();
            if (random < Constants.BROAD_RATE && distance(person) < SAFE_DIST) {
                this.beInfected();
            }
        }
    }
}
