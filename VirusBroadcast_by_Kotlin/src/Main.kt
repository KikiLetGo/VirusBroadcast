import javax.swing.JFrame
import java.util.Random



/**
 * @ClassName:
 * @Description:
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:12
 * */
private var hospitalWidth: Int = 0

fun main(args: Array<String>) {
    initHospital()
    initPanel()
    initInfected()
}

/**
 * 初始化画布
 */
fun initPanel(){
    val p = MyPanel()
    val panelThread = Thread(p)
    val frame = JFrame()
    frame.add(p)
    frame.setSize(Constants.CITY_WIDTH + hospitalWidth + 300, Constants.CITY_HEIGHT)
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
    frame.title = "瘟疫传播模拟"
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    panelThread.start()//开启画布线程，即世界线程，接着看代码的下一站可以转MyPanel.java

}

/**
 * 初始化医院参数
 */
private fun initHospital() {
    hospitalWidth = Hospital.hospital.width
}

/**
 * 初始化初始感染者
 */
private fun initInfected(){
    val people = PersonPool.personPool.personList   //获取所有的市民
    for (i in 0 until Constants.ORIGINAL_COUNT) {
        var person: Person
        do {
            person = people[Random().nextInt(people.size - 1)]//随机挑选一个市民
        } while (person.isInfected())//如果该市民已经被感染，重新挑选
        person.beInfected()//让这个幸运的市民成为感 染者
    }
}