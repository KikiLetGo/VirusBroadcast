import javax.swing.*
import java.awt.*
import java.util.Timer
import java.util.TimerTask

/**
 * 主面板。
 *
 * @ClassName: MyPanel
 * @Description: 主面板
 * @author: Bruce Young
 * @date: 2020年02月02日 17:03
 */
class MyPanel : JPanel(), Runnable {
    val timer = Timer()
    companion object {
        var worldTime = 0//世界时间
    }
    init {
        this.background = Color(0x444444)
    }
    internal inner class MyTimerTask : TimerTask() {
        override fun run() {
            this@MyPanel.repaint()
            worldTime++
        }
    }
    override fun run() {
        timer.schedule(MyTimerTask(), 0, 100)//启动世界计时器，时间开始流动（突然脑补DIO台词：時は停た）
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        g!!.color = Color(0x00ff00)//设置医院边界颜色
        //绘制医院边界
        g.drawRect(Hospital.hospital.x, Hospital.hospital.y,
                Hospital.hospital.width, Hospital.hospital.height)
        g.font = Font("微软雅黑", Font.BOLD, 16)
        g.color = Color(0x00ff00)
        g.drawString("医院", Hospital.hospital.x + Hospital.hospital.width / 4, Hospital.hospital.y - 16)
        //绘制代表人类的圆点
        val people = PersonPool.personPool.personList
//        if (people.size == 0) return
        for (person in people) {
            when (person.state) {
                Person.State.NORMAL -> g.color = Color(0xdddddd) //健康人
                Person.State.SHADOW -> g.color = Color(0xffee00) //潜伏期感染者
                Person.State.CONFIRMED -> g.color = Color(0xff0000) //确诊患者
                Person.State.FREEZE -> g.color = Color(0x48FFFC)    //已隔离者
                Person.State.DEATH -> g.color = Color(0x000000)     //死亡患者
            }
            person.update()//对各种状态的市民进行不同的处理
            g.fillOval(person.x, person.y, 3, 3)
        }

        val captionStartOffsetX = 700 + Hospital.hospital.width + 40
        val captionStartOffsetY = 40
        val captionSize = 24

        //显示数据信息
        g.color = Color.WHITE
        g.drawString("城市总人数：" + Constants.CITY_PERSON_SIZE, captionStartOffsetX, captionStartOffsetY)
        g.color = Color(0xdddddd)
        g.drawString("健康者人数：" + PersonPool.personPool.getPeopleSize(Person.State.NORMAL), captionStartOffsetX, captionStartOffsetY + captionSize)
        g.color = Color(0xffee00)
        g.drawString("潜伏期人数：" + PersonPool.personPool.getPeopleSize(Person.State.SHADOW), captionStartOffsetX, captionStartOffsetY + 2 * captionSize)
        g.color = Color(0xff0000)
        g.drawString("发病者人数：" + PersonPool.personPool.getPeopleSize(Person.State.CONFIRMED), captionStartOffsetX, captionStartOffsetY + 3 * captionSize)
        g.color = Color(0x48FFFC)
        g.drawString("已隔离人数：" + PersonPool.personPool.getPeopleSize(Person.State.FREEZE), captionStartOffsetX, captionStartOffsetY + 4 * captionSize)
        g.color = Color(0x00ff00)
        g.drawString("空余病床：" + Math.max(Constants.BED_COUNT - PersonPool.personPool.getPeopleSize(Person.State.FREEZE), 0), captionStartOffsetX, captionStartOffsetY + 5 * captionSize)
        g.color = Color(0xE39476)
        //暂定急需病床数量为 NEED = 确诊发病者数量 - 已隔离住院数量
        val needBeds = PersonPool.personPool.getPeopleSize(Person.State.CONFIRMED) - PersonPool.personPool.getPeopleSize(Person.State.FREEZE)

        g.drawString("急需病床：" + if (needBeds > 0) needBeds else 0, captionStartOffsetX, captionStartOffsetY + 6 * captionSize)
        g.color = Color(0xccbbcc)
        g.drawString("病死人数：" + PersonPool.personPool.getPeopleSize(Person.State.DEATH), captionStartOffsetX, captionStartOffsetY + 7 * captionSize)
        g.color = Color(0xffffff)
        g.drawString("世界时间（天）：" + (worldTime / 10.0).toInt(), captionStartOffsetX, captionStartOffsetY + 8 * captionSize)
    }


}