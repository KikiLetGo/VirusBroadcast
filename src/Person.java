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

public class Person extends Point {
    private City city;

    private MoveTarget moveTarget;
    /**
     * 人群流动意愿影响系数：正态分布方差sigma
     */
    int sig = 1;

    /**
     * 正态分布N(mu,sigma)随机位移目标位置
     */

    double targetXU;//x方向的均值mu
    double targetYU;//y方向的均值mu
    double targetSig = 50;//方差sigma

    /**
     * 市民的状态
     * 
     * @author dy55
     */
    public enum State {
        NORMAL, SUSPECTED, SHADOW, CONFIRMED, FREEZE, DEATH
    }

    public Person(City city, int x, int y) {
        super(x, y);
        this.city = city;
        //对市民的初始位置进行N(x,100)的正态分布随机
        targetXU = MathUtil.stdGaussian(100, x);
        targetYU = MathUtil.stdGaussian(100, y);

    }

    /**
     * 流动意愿标准化
     * <p>
     * 根据标准正态分布生成随机人口流动意愿
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
        return MathUtil.stdGaussian(sig, Constants.u) > 0;
    }

    private State state = State.NORMAL;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    int infectedTime = 0;//感染时刻
    int confirmedTime = 0;//确诊时刻
    int dieMoment = 0;//死亡时刻，为0代表未确定


    public boolean isInfected() {
        return state.compareTo(State.SHADOW) >= 0;
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
        return Math.sqrt(Math.pow(getX() - person.getX(), 2) + Math.pow(getY() - person.getY(), 2));
    }

    /**
     * 住院
     */
    private void freeze() {
        state = State.FREEZE;
    }

    /**
     * 不同状态下的单个人实例运动行为
     */
    private void action() {
        // 如果处于隔离或死亡状态，或者不想移动，则无法行动
        if (state == State.FREEZE || state == State.DEATH || !wantMove()) {
            return;
        }
        //存在流动意愿的，将进行流动，流动位移仍然遵循标准正态分布
        if (moveTarget == null || moveTarget.isArrived()) {
            //在想要移动并且没有目标时，将自身移动目标设置为随机生成的符合正态分布的目标点
            //产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
            double targetX = MathUtil.stdGaussian(targetSig, targetXU);
            double targetY = MathUtil.stdGaussian(targetSig, targetYU);
            moveTarget = new MoveTarget((int) targetX, (int) targetY);

        }

        //计算运动位移
        int dX = moveTarget.getX() - getX();
        int dY = moveTarget.getY() - getY();

        double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));//与目标点的距离

        if (length < 1) {
            //判断是否到达目标点
            moveTarget.setArrived(true);
            return;
        }

        int udX = (int) (dX / length);//x轴dX为位移量，符号为沿x轴前进方向, 即udX为X方向表示量
        if (udX == 0 && dX != 0) {
            udX = round(dX);
        }

        int udY = (int) (dY / length);//y轴dY为位移量，符号为沿x轴前进方向，即udY为Y方向表示量
        if (udY == 0 && dY != 0) {
            udY = round(dY);
        }

        //横向运动边界
        if (getX() > Constants.CITY_WIDTH || getX() < 0) {
            moveTarget = null;
            if (udX > 0) {
                udX = -udX;
            }
        }
        //纵向运动边界
        if (getY() > Constants.CITY_HEIGHT || getY() < 0) {
            moveTarget = null;
            if (udY > 0) {
                udY = -udY;
            }
        }
        moveTo(udX, udY);

    }
    
    private int round(int value) {
        if (value > 0) {
            return 1;
        }
        return -1;
    }

    public Bed useBed;

    private final float SAFE_DIST = Constants.SAFE_DIST;//安全距离

    /**
     * 对各种状态的人进行不同的处理，更新发布市民健康状态
     */
    public void update() {
        //@TODO找时间改为状态机

        if (state == State.DEATH) {
            if (Hospital.getInstance().inHospital(getX(), getY())) {
                setX(-1);
                setY(-1);
            }
        }

        if (state == State.FREEZE) {
            // 对隔离患者判断治愈成功率
            float success = new Random().nextFloat();
            if (success < Constants.RECOVERY_RATE) {
                state = State.NORMAL;
                Random random = new Random();
                int x = (int) (100 * random.nextGaussian() + city.getCenterX());
                int y = (int) (100 * random.nextGaussian() + city.getCenterY());
                if (x > 700) {
                    x = 700;
                }
                setX(x);
                setY(y);
            }
        }

        //处理已经确诊的感染者（即患者）
        if (state == State.CONFIRMED && dieMoment == 0) {
            int dieTime = (int) MathUtil.stdGaussian(Constants.DIE_VARIANCE, Constants.DIE_TIME);
            dieMoment = confirmedTime + dieTime;//发病后确定死亡时刻
        }


        if (state == State.CONFIRMED
                && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
            //如果患者已经确诊，且（世界时刻-确诊时刻）大于医院响应时间，即医院准备好病床了，可以抬走了
            Bed bed = Hospital.getInstance().pickBed();//查找空床位
            if (bed == null) {

                //没有床位了，报告需求床位数

            } else {
                //安置病人
                useBed = bed;
                freeze();
                setX(bed.getX());
                setY(bed.getY());
                bed.setEmpty(false);
            }
        }

        //处理病死者
        if ((state == State.CONFIRMED || state == State.FREEZE) && MyPanel.worldTime >= dieMoment) {
            state = State.DEATH;//患者死亡
            Hospital.getInstance().returnBed(useBed);//归还床位
        }

        //增加一个正态分布用于潜伏期内随机发病时间
        double stdRnShadowtime = MathUtil.stdGaussian(25, Constants.SHADOW_TIME / 2);
        //处理发病的潜伏期感染者
        if (MyPanel.worldTime - infectedTime > stdRnShadowtime && state == State.SHADOW) {
            state = State.CONFIRMED;//潜伏者发病
            confirmedTime = MyPanel.worldTime;//刷新时间
        }
        //处理未隔离者的移动问题
        action();
        //处理健康人被感染的问题
        List<Person> people = PersonPool.getInstance().personList;
        if (state.compareTo(State.SHADOW) >= 0) {
            return;
        }

        for (Person person : people) {
            if (person.getState() == State.NORMAL) {
                continue;
            }
            float random = new Random().nextFloat();
            if (random < Constants.BROAD_RATE && distance(person) < SAFE_DIST) {
                this.beInfected();
                break;
            }
        }
    }
}
