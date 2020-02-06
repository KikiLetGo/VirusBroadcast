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
     * 市民的状态
     * <p>
     * 市民状态应该需要细分，虽然有的状态暂未纳入模拟，但是细分状态应该保留
     */
    public interface State {
        int NORMAL = 0;//正常人，未感染的健康人
        int SUSPECTED = NORMAL + 1;//有暴露感染风险
        int SHADOW = SUSPECTED + 1;//潜伏期
        int CONFIRMED = SHADOW + 1;//发病且已确诊为感染病人
        int FREEZE = CONFIRMED + 1;//隔离治疗，禁止位移
        int CURED = FREEZE + 1;//已治愈出院
        int HEAVEN = CURED + 1;//病死状态
    }

    public Person(City city, int x, int y) {
        super(x, y);
        this.city = city;
        //生成随机移动均值x-mu、y-mu，
        targetXU = MathUtil.stdGaussian(100, x);
        targetYU =  MathUtil.stdGaussian(100, y);

    }

    /**
     * 根据标准正态分布生成随机人口流动意愿
     * <p>
     *
     * @return
     */
    public boolean wantMove() {
        return MathUtil.stdGaussian(sig, Constants.u) > 0;
    }

    private int state = State.NORMAL;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
        return Math.sqrt(Math.pow(getX() - person.getX(), 2) + Math.pow(getY() - person.getY(), 2));
    }

    /**
     * 住院
     */
    private void freezy() {
        state = State.FREEZE;
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
            double targetX = MathUtil.stdGaussian(targetSig, targetXU);
            double targetY =  MathUtil.stdGaussian(targetSig, targetYU);
            moveTarget = new MoveTarget((int) targetX, (int) targetY);

        }

        //计算运动位移
        int dX = moveTarget.getX() - getX();
        int dY = moveTarget.getY() - getY();
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

        if (getX() > 700) {
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

    /**
     * 更新发布市民健康状态
     */
    public void update() {
        //@TODO找时间改为状态机
        if (state >= State.FREEZE) {
            return;
        }

        if (state == State.CONFIRMED
                && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
            Bed bed = Hospital.getInstance().pickBed();//查找空床位
            if (bed == null) {
                //没有床位了
//                System.out.println("隔离区没有空床位");
            } else {
                //安置病人
                state = State.FREEZE;
                setX(bed.getX());
                setY(bed.getY());
                bed.setEmpty(false);
            }
        }
        //增加一个正态分布用于潜伏期内随机发病时间
        double stdRnShadowtime = MathUtil.stdGaussian(25,  Constants.SHADOW_TIME / 2);
        if (MyPanel.worldTime - infectedTime > stdRnShadowtime && state == State.SHADOW) {
            state = State.CONFIRMED;//潜伏者发病
            confirmedTime = MyPanel.worldTime;//刷新时间
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
