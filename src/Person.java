import java.util.List;
import java.util.Random;

/**
 * 能够随机运动的民众
 * 
 * 
 * 一个人类对象,包含了:城市、坐标、状态、行动意愿
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

	double targetXU;// x方向的均值mu
	double targetYU;// y方向的均值mu
	double targetSig = 50;// 方差sigma

	/**
	 * 市民的状态
	 * <hr>
	 * 市民状态应该需要细分，虽然有的状态暂未纳入模拟，但是细分状态应该保留
	 */
	public interface State {
		int NORMAL = 0;// 正常人，未感染的健康人，0
		int SUSPECTED = NORMAL + 1;// 有暴露感染风险,疑似病人，1
		int SHADOW = SUSPECTED + 1;// 潜伏期，2
		int CONFIRMED = SHADOW + 1;// 发病且已确诊为感染病人,确诊病人，3
		int FREEZE = CONFIRMED + 1;// 隔离治疗，禁止位移,重症隔离，4

		// 已治愈出院的人转为NORMAL即可，否则会与作者通过数值大小判断状态的代码冲突
		int DEATH = FREEZE + 1;// 病死者，5
		// int CURED = DEATH + 1;//治愈数量用于计算治愈出院后归还床位数量，该状态是否存续待定
	}

	/**
	 * 初始方法，设置每个人的城市中心坐标和流动目标
	 * 
	 * @param city
	 * @param x    以城市为中心随机生成的坐标
	 * @param y    以城市为中心随机生成的坐标
	 */
	public Person(City city, int x, int y) {
		super(x, y);
		this.city = city;
		// 对市民的初始位置进行N(x,100)的正态分布随机
//        以市民的初始位置为中心点，生成每个单位正态移动后的坐标
		targetXU = MathUtil.stdGaussian(100, x);
		targetYU = MathUtil.stdGaussian(100, y);

	}

	/**
	 * 流动意愿标准化
	 * <p>
	 * 根据标准正态分布生成随机人口流动意愿
	 * <p>
	 * 流动意愿标准化后判断是在0的左边还是右边从而决定是否流动。大于0则移动，否则不移动
	 * <p>
	 * 设X随机变量为服从正态分布，sigma是影响分布形态的系数，从而影响整体人群流动意愿分布 u值决定正态分布的中轴是让更多人群偏向希望流动或者希望懒惰。
	 * <p>
	 * value的推导： StdX = (X-u)/sigma X = sigma * StdX + u
	 *
	 * @return 是否移动
	 */
	public boolean wantMove() {
		return MathUtil.stdGaussian(sig, Constants.u) > 0;
	}

	String wantMoveSubs(float x) {
		return x > 0.5 ? "人们随意走动" : "人们限制出行";
	}

	private int state = State.NORMAL;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	int infectedTime = 0;// 感染时刻
	int confirmedTime = 0;// 确诊时刻
	int dieMoment = 0;// 死亡时刻，为0代表未确定，-1代表不会病死

//已经感染（潜伏或者确诊）
	public boolean isInfected() {
		return state >= State.SHADOW;
	}

	/**
	 * 感染一个人，使其成为潜伏者
	 *
	 * 设置感染时间
	 */
	public void beInfected() {
		state = State.SHADOW;
		infectedTime = MyPanel.worldTime;
	}

	/**
	 * 计算两点之间的直线距离
	 *
	 * 斜边
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
	 * 单个人实例运动行为
	 * <p>
	 * 控制一个人（正常、疑似、潜伏、确诊）的移动
	 * <p>
	 * 画面中移动的小点代表移动的人群
	 * <p>
	 * 根据正态分布随机决定人群的移动意向，根据移动意向以1px为单位移动
	 */
	private void action() {

		if (state == State.FREEZE || state == State.DEATH) {
			return;// 如果处于隔离或者死亡状态，则无法行动
		}
//		如果此人不想移动，就跳过
		if (!wantMove()) {
			return;
		}
		// 存在流动意愿但没有流动坐标的，将进行流动，流动位移仍然遵循标准正态分布
		if (moveTarget == null || moveTarget.isArrived()) {
			// 在想要移动并且没有目标时，将自身移动目标设置为随机生成的符合正态分布的目标点
			// 产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
//			在正态目标的基础上，再次进行正态定位，作为最终移动目标
			double targetX = MathUtil.stdGaussian(targetSig, targetXU);
			double targetY = MathUtil.stdGaussian(targetSig, targetYU);
//			目的地
			moveTarget = new MoveTarget((int) targetX, (int) targetY);

		}

		// 计算运动位移
		int dX = moveTarget.getX() - getX();
		int dY = moveTarget.getY() - getY();
		// 计算斜边
		double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));// 与目标点的距离

//		距离小于1则设置为抵达
		if (length < 1) {

			moveTarget.setArrived(true);
			return;
		}
//直边不为零的话，直边必然小于斜边，相除的int必然为零，用来判断纵横轴的运动方向
		int udX = (int) (dX / length);// x轴dX为位移量，符号为沿x轴前进方向, 即udX为X方向表示量
		if (udX == 0 && dX != 0) {
			if (dX > 0) {
				udX = 1;
			} else {
				udX = -1;
			}
		}

		int udY = (int) (dY / length);// y轴dY为位移量，符号为沿x轴前进方向，即udY为Y方向表示量
		// FIXED: 修正一处错误
		if (udY == 0 && dY != 0) {
			if (dY > 0) {
				udY = 1;
			} else {
				udY = -1;
			}
		}

//		超出边界则往相反方向运动
		// 横向运动边界
		if (getX() > Constants.CITY_WIDTH || getX() < 0) {
			moveTarget = null;
			if (udX > 0) {
				udX = -udX;
			}
		}
		// 纵向运动边界
		if (getY() > Constants.CITY_HEIGHT || getY() < 0) {
			moveTarget = null;
			if (udY > 0) {
				udY = -udY;
			}
		}

//		更改坐标
		moveTo(udX, udY);

	}

	public Bed useBed;

	private float SAFE_DIST = 2f;// 安全距离

	/**
	 * 对各种状态的人进行不同的处理，更新发布市民健康状态
	 * 
	 * 潜伏、隔离、死亡之外的人
	 * 
	 */
	public void update() {
		// @TODO找时间改为状态机
//隔离、死亡者不继续执行
		if (state == State.FREEZE || state == State.DEATH) {
			return;// 如果已经隔离或者死亡了，就不需要处理了
		}

		// 处理已经确诊的感染者（即患者）
		if (state == State.CONFIRMED && dieMoment == 0) {

			int destiny = new Random().nextInt(10000) + 1;// 幸运数字，[1,10000]随机数
//	位于1~5000，这个是50%的死亡率
			if (1 <= destiny && destiny <= (int) (Constants.FATALITY_RATE * 10000)) {

				// 如果幸运数字落在死亡区间
				int dieTime = (int) MathUtil.stdGaussian(Constants.DIE_VARIANCE, Constants.DIE_TIME);
				dieMoment = confirmedTime + dieTime;// 发病后确定死亡时刻
			} else {
				dieMoment = -1;// 逃过了死神的魔爪

			}
		}
		// TODO 暂时缺失治愈出院市民的处理。需要确定一个变量用于治愈时长。由于案例太少，暂不加入。
//确诊到当前间隔的时间大于医院相应时间则可以安排床位
		if (state == State.CONFIRMED && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
			// 如果患者已经确诊，且（世界时刻-确诊时刻）大于医院响应时间，即医院准备好病床了，可以抬走了
			Bed bed = Hospital.getInstance().pickBed();// 查找空床位
			if (bed == null) {

				// 没有床位了，报告需求床位数
//				Constants.NEED_BED_COUNT++;

			} else {
				// 安置病人
//				移到医院区域，这个时候小点依然是红色。会出现这种情况：一轮循环在下一轮开始前移动很多红点到医院区域，下一轮开始之后才变为蓝色
				useBed = bed;
				state = State.FREEZE;
				setX(bed.getX());
				setY(bed.getY());
				bed.setEmpty(false);
			}
		}

		// 处理病死者
//		已确诊、隔离的人里面，达到死亡时间的，就判定为死亡，并且归还床位
		if ((state == State.CONFIRMED || state == State.FREEZE) && MyPanel.worldTime >= dieMoment && dieMoment > 0) {
			state = State.DEATH;// 患者死亡
			Hospital.getInstance().returnBed(useBed);// 归还床位
		}

		// 增加一个正态分布用于生成潜伏期内随机发病时间
//		以7天为均值，随机生成发病时间
		double stdRnShadowtime = MathUtil.stdGaussian(25, Constants.SHADOW_TIME / 2);
		// 处理发病的潜伏期感染者
//		潜伏期达到发病时间就转为确诊
		if (MyPanel.worldTime - infectedTime > stdRnShadowtime && state == State.SHADOW) {
			state = State.CONFIRMED;// 潜伏者发病
			confirmedTime = MyPanel.worldTime;// 刷新时间
		}
		// 处理未隔离者的移动问题
		action();
		// 处理健康人被感染的问题
		List<Person> people = PersonPool.getInstance().personList;

//		潜伏以上者不继续执行
		if (state >= State.SHADOW) {
			return;
		}
//设置一个人（正常、疑似）与其余人（疑似、确诊）接触之后就有概率被感染
		// 通过一个随机幸运值和安全距离决定感染其他人
//		一个正常、疑似状态的人，如果近距离接触任何疑似以上的人，就有一定概率成为潜伏者
		for (Person person : people) {
//			正常者直接跳过
			if (person.getState() == State.NORMAL) {
				continue;
			}
//一个正常、疑似状态的人与疑似以上的人越过安全距离，则有很大概率感染，然后成为潜伏者
			float random = new Random().nextFloat();
			if (random < Constants.BROAD_RATE && distance(person) < SAFE_DIST) {
				this.beInfected();
				break;
			}
		}
	}
}
