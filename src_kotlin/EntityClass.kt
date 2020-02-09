import java.util.*
import java.util.ArrayList


/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
open class Point(open var x: Int, open var y: Int) {
    fun moveTo(x: Int, y: Int) {
        this.x += x
        this.y += y
    }
}

/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
class Bed(x: Int, y: Int) : Point(x, y) {
    var isEmpty = true
}

/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
class MoveTarget(var x: Int, var y: Int) {
    var arrived: Boolean = false //是否到达目标点
}

/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
class City(var centerX: Int, var cneterY: Int)

/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
class Hospital private constructor(x: Int, y: Int) : Point(x, y) {
    companion object {
        const val HOSPITAL_X = 720
        const val HOSPITAL_Y = 80
        val hospital = Hospital(HOSPITAL_X,HOSPITAL_Y)
    }

    var width = 0
        private set
    var height = 600
        private set
    val point = Point(HOSPITAL_X, HOSPITAL_Y)   //第一个床位所在坐标，用于给其他床位定绝对坐标
    val beds = ArrayList<Bed>()

    //初始化构造
    init {
        //医院矩形所在坐标
        this.x = HOSPITAL_X
        this.y = HOSPITAL_Y + 10
        //根据床位数量调整医院矩形的大小
        if (Constants.BED_COUNT == 0) {
            width = 0
            height = 0
        }
        //根据医院床位数量计算医院宽度
        //因为高度定了只能装载100个床位
        val column = Constants.BED_COUNT / 100
        width = column * 6
        //根据第一个床位坐标初始化其他床位的坐标
        for (i in 0 until column)
            for (j in 10..606 step 6) {
                val bed = Bed(point.x + i * 6, point.y + j)
                beds.add(bed)
                if (beds.size >= Constants.BED_COUNT)  //确定医院床位承载数量
                    break
            }
    }

    fun pickBed(): Bed? {
        return beds.find { it.isEmpty }
    }

    //死亡或痊愈出院空出床位
    fun returnBed(bed: Bed?): Bed? {
        if (bed != null)
            bed.isEmpty = true
        return bed
    }
}

/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
class PersonPool private constructor() {
    var personList: MutableList<Person> = ArrayList()
        private set

    init {
        val city = City(400, 400)    //设置城市中心为坐标(400,400)
        for (i in 0 until Constants.CITY_PERSON_SIZE) {
            val random = Random()
            var x = (100 * random.nextGaussian() + city.centerX).toInt()
            val y = (100 * random.nextGaussian() + city.cneterY).toInt()
            if (x > 700)
                x = 700

            personList.add(Person(city, x, y))
        }
    }

    companion object {
        val personPool = PersonPool()
    }

    /**
     * @param state 市民类型 Person.State的值，若为-1则返回当前总数目
     * @return 获取指定人群数量
     */
    fun getPeopleSize(state: Int): Int {
        if (state == -1)
            return personList.size
        return personList.count {it.state == state}
    }

}