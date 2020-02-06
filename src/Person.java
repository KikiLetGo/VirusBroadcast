import java.util.List;
import java.util.Random;

/**
 * @ClassName: Person
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:05
 */
public class Person {
    private City city;
    private int x;
    private int y;
    private MoveTarget moveTarget;
    int sig = 1;//移动意愿的正态分布方差sigma

    //正态分布相关参数N(mu,sigma)
    double targetXU;//x方向的均值mu
    double targetYU;//y方向的均值mu
    double targetSig = 50;//方差sigma


    public interface State {//市民状态
        int NORMAL = 0;//未被感染
        int SHADOW = NORMAL + 1;//潜伏者

        int CONFIRMED = SHADOW + 1;//感染者
        int FREEZE = CONFIRMED + 1;//已隔离
        int DEATH = FREEZE + 1;//病死者
    }

    public Person(City city, int x, int y) {
        this.city = city;
        this.x = x;
        this.y = y;
        //对市民的初始位置进行N(x,100)的正态分布随机
        targetXU = 100 * new Random().nextGaussian() + x;
        targetYU = 100 * new Random().nextGaussian() + y;

    }

    /**
     * 根据想要移动的意愿的平均值来进行正态分布计算
     * @return 是否想要移动
     */
    public boolean wantMove() {
    	//产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
        double value = sig * new Random().nextGaussian() + Constants.u;
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

    int infectedTime = 0;//感染时刻
    int confirmedTime = 0;//确诊时刻
    int dieMoment = 0;//死亡时刻

    public boolean isInfected() {
        return state >= State.SHADOW;
    }

    public void beInfected() {
        state = State.SHADOW;
        infectedTime = MyPanel.worldTime;
    }

    public double distance(Person person) {
        return Math.sqrt(Math.pow(x - person.getX(), 2) + Math.pow(y - person.getY(), 2));
    }

    private void freezy() {
        state = State.FREEZE;
    }

    private void moveTo(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * 处理未隔离者的移动问题
     * 
     */
    private void action() {
        if (state == State.FREEZE || state == State.DEATH) {
            return;//如果处于隔离或者死亡状态，则无法行动
        }
        if (!wantMove()) {
            return;
        }
        if (moveTarget == null || moveTarget.isArrived()) {
        	//在想要移动并且没有目标时，将自身移动目标设置为随机生成的符合正态分布的目标点
        	//产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
            double targetX = targetSig * new Random().nextGaussian() + targetXU;
            double targetY = targetSig * new Random().nextGaussian() + targetYU;
            moveTarget = new MoveTarget((int) targetX, (int) targetY);

        }


        int dX = moveTarget.getX() - x;
        int dY = moveTarget.getY() - y;
        double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));//与目标点的距离

        if (length < 1) {
        	//判断是否到达目标点
            moveTarget.setArrived(true);
            return;
        }
        int udX = (int) (dX / length);//符号为沿x轴前进方向
        if (udX == 0 && dX != 0) {
            if (dX > 0) {
                udX = 1;//TODO:移动步长为1，如果要调整移动步长可以看一下这里
            } else {
                udX = -1;
            }
        }
        int udY = (int) (dY / length);//符号为沿y轴前进方向
        if (udY == 0 && udY != 0) {
            if (dY > 0) {
                udY = 1;
            } else {
                udY = -1;
            }
        }

        if (x > 700) {
        	//这个700也许是x方向边界的意思，因为画布大小1000x800
        	//TODO:如果是边界那么似乎边界判断还差一个y方向        	
            moveTarget = null;
            if (udX > 0) {
                udX = -udX;
            }
        }
        moveTo(udX, udY);

//        if(wantMove()){
//        }


    }

    private float SAFE_DIST = 2f;//安全距离

    /**
     * 对各种状态的人进行不同的处理
     */
    public void update() {
        //TODO:找时间改为状态机
        if (state >= State.FREEZE) {
            return;//如果已经隔离或者死亡了，就不需要处理了
        }
        //处理已经确诊的感染者（即患者）
        //
        if (state == State.CONFIRMED && dieMoment == 0) {
        	int destiny = new Random().nextInt(10000)+1;//命运数字，[1,10000]随机数
        	if (1 <= destiny && destiny <= (int)(Constants.FATALITY_RATE * 10000)) {
        		//如果命运数字落在死亡区间
        		int dieTime = (int) (Constants.DIE_VARIANCE * new Random().nextGaussian()+Constants.DIE_TIME);
            	dieMoment = confirmedTime + dieTime;//发病后确定死亡时刻
            	//System.out.printf("%d,%f,%d\n",destiny,Constants.FATALITY_RATE * 10000,dieTime);
        	}
        	else {
        		dieMoment = -1;//逃过了死神的魔爪
        	}
        	
        }
        //*/
        if (state == State.CONFIRMED && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
        	//如果患者已经确诊，且（世界时刻-确诊时刻）大于医院响应时间，即医院准备好病床了，可以抬走了
            Bed bed = Hospital.getInstance().pickBed();//查找空床位
            if (bed == null) {
                //没有床位了
//                System.out.println("隔离区没有空床位");
            } else {
                //安置病人
                state = State.FREEZE;
                x = bed.getX();
                y = bed.getY();
                bed.setEmpty(false);
            }
        }
        //处理病死者
        if((state == State.CONFIRMED || state == State.FREEZE )&& MyPanel.worldTime >= dieMoment && dieMoment > 0) {
        	state = State.DEATH;//患者死亡
        }
        //处理发病的潜伏期感染者
        if (MyPanel.worldTime - infectedTime > Constants.SHADOW_TIME && state == State.SHADOW) {
            state = State.CONFIRMED;//潜伏者发病
            confirmedTime = MyPanel.worldTime;//刷新时间
        }
        //处理未隔离者的移动问题
        action();
        //处理健康人被感染的问题
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
