import Person.State.Companion.DEATH
import java.util.Random

/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 2:43
 * */

class Person(private val city: City, override var x: Int, override var y: Int) : Point(x, y) {
    /**
     * 正态分布N(mu,sigma)随机位移目标位置
     */
    private val targetXU: Double = MathUtil.stdGaussian(100.0, x.toDouble()) //x方向的均值mu
    private val targetYU: Double = MathUtil.stdGaussian(100.0, y.toDouble()) //y方向的均值mu
    private val targetSig: Double = 50.0 //方差sigma
    private var moveTarget: MoveTarget? = null

    private val sig = 1 //人群流动意愿影响系数：正态分布方差sigma
    private val SAFE_DIST = 2.0//安全距离
    var useBed: Bed? = null

    var state = State.NORMAL
    var infectedTime = 0//感染时刻
    var confirmedTime = 0//确诊时刻
    var dieMoment = 0//死亡时刻，为0代表未确定，-1代表不会病死


    interface State {
        companion object {
            const val NORMAL = 0//正常人，未感染的健康人
            const val SUSPECTED = NORMAL + 1//有暴露感染风险
            const val SHADOW = SUSPECTED + 1//潜伏期
            const val CONFIRMED = SHADOW + 1//发病且已确诊为感染病人
            const val FREEZE = CONFIRMED + 1//隔离治疗，禁止位移

            //已治愈出院的人转为NORMAL即可，否则会与作者通过数值大小判断状态的代码冲突
            const val DEATH = FREEZE + 1//病死者
            //int CURED = DEATH + 1;//治愈数量用于计算治愈出院后归还床位数量，该状态是否存续待定
        }
    }

    /**
     * 流动意愿标准化
     *
     * 根据标准正态分布生成随机人口流动意愿
     *
     * 流动意愿标准化后判断是在0的左边还是右边从而决定是否流动。
     *
     * 设X随机变量为服从正态分布，sigma是影响分布形态的系数，从而影响整体人群流动意愿分布
     * u值决定正态分布的中轴是让更多人群偏向希望流动或者希望懒惰。
     *
     * value的推导：
     * StdX = (X-u)/sigma
     * X = sigma * StdX + u
     *
     * @return
     */
    fun wantMove(): Boolean = MathUtil.stdGaussian(sig.toDouble(), Constants.u) > 0

    fun isInfected(): Boolean = state >= State.SHADOW

    fun beInfected() {
        state = State.SHADOW
        infectedTime = MyPanel.worldTime
    }
    /**
     * 住院
     */
    private fun freezy() {
        state = State.FREEZE
    }

    /**
     * 计算两点之间的直线距离
     *
     * @param person
     * @return
     */
    fun distance(person: Person): Double = Math.sqrt(Math.pow((x - person.x).toDouble(), 2.0) + Math.pow((y - person.y).toDouble(), 2.0))

    /**
     * 不同状态下的单个人实例运动行为
     */
    private fun action() {

        if (state == State.FREEZE || state == State.DEATH) {
            return //如果处于隔离或者死亡状态，则无法行动
        }
        if (!wantMove()) {
            return
        }
        //存在流动意愿的，将进行流动，流动位移仍然遵循标准正态分布
        if (moveTarget == null || moveTarget!!.arrived) {
            //在想要移动并且没有目标时，将自身移动目标设置为随机生成的符合正态分布的目标点
            //产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
            val targetX = MathUtil.stdGaussian(targetSig, targetXU)
            val targetY = MathUtil.stdGaussian(targetSig, targetYU)
            moveTarget = MoveTarget(targetX.toInt(), targetY.toInt())

        }

        //计算运动位移
        val dX = moveTarget!!.x - x
        val dY = moveTarget!!.y - y

        val length = Math.sqrt(Math.pow(dX.toDouble(), 2.0) + Math.pow(dY.toDouble(), 2.0))//与目标点的距离

        if (length < 1) {
            //判断是否到达目标点
            moveTarget!!.arrived = true
            return
        }

        var udX = (dX / length).toInt()//x轴dX为位移量，符号为沿x轴前进方向, 即udX为X方向表示量
        if (udX == 0 && dX != 0)
            udX = if (dX > 0) 1 else -1


        var udY = (dY / length).toInt()//y轴dY为位移量，符号为沿x轴前进方向，即udY为Y方向表示量
        //FIXED: 修正一处错误
        if (udY == 0 && dY != 0)
            udY = if (dY > 0) 1 else -1

        //横向运动边界
        if (x > Constants.CITY_WIDTH || x < 0) {
            moveTarget = null
            if (udX > 0)
                udX = -udX
        }
        //纵向运动边界
        if (y > Constants.CITY_HEIGHT || y < 0) {
            moveTarget = null
            if (udY > 0)
                udY = -udY
        }
        moveTo(udX, udY)

    }


    /**
     * 对各种状态的人进行不同的处理，更新发布市民健康状态
     */
    fun update() {
        //@TODO找时间改为状态机
        if (state == State.FREEZE || state == State.DEATH)
            return//如果已经隔离或者死亡了，就不需要处理了

        //处理已经确诊的感染者（即患者）
        if (state == State.CONFIRMED && dieMoment == 0) {
            val dieTime = MathUtil.stdGaussian(Constants.DIE_VARIANCE, Constants.DIE_TIME.toDouble()).toInt()
            confirmedTime + dieTime//发病后确定死亡时刻

        }
        //TODO 暂时缺失治愈出院市民的处理。需要确定一个变量用于治愈时长。由于案例太少，暂不加入。


        if (state == State.CONFIRMED && MyPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
            //如果患者已经确诊，且（世界时刻-确诊时刻）大于医院响应时间，即医院准备好病床了，可以抬走了
            val bed = Hospital.hospital.pickBed()//查找空床位
            if (bed == null) {
                //没有床位了，报告需求床位数

            } else {
                //安置病人
                useBed = bed
                state = State.FREEZE
                x = bed.x
                y = bed.y
                bed.isEmpty = false
            }
        }

        //处理病死者
        if ((state == State.CONFIRMED || state == State.FREEZE) && MyPanel.worldTime >= dieMoment && dieMoment > 0) {
            state = State.DEATH//患者死亡
            Hospital.hospital.returnBed(useBed)//归还床位
        }

        //增加一个正态分布用于潜伏期内随机发病时间
        val stdRnShadowtime = MathUtil.stdGaussian(25.0, Constants.SHADOW_TIME / 2)
        //处理发病的潜伏期感染者
        if (MyPanel.worldTime - infectedTime > stdRnShadowtime && state == State.SHADOW) {
            state = State.CONFIRMED//潜伏者发病
            confirmedTime = MyPanel.worldTime//刷新时间
        }
        //处理未隔离者的移动问题
        action()
        //处理健康人被感染的问题
        val people = PersonPool.personPool.personList
        if (state >= State.SHADOW) {
            return
        }
        //通过一个随机幸运值和安全距离决定感染其他人
        for (person in people) {
            if (person.state == State.NORMAL) {
                continue
            }
            val random = Random().nextFloat()
            if (random < Constants.BROAD_RATE && distance(person) < SAFE_DIST) {
                 this.beInfected()
                break
            }
        }
    }
}