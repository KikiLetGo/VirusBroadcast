import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主面板。
 *
 * @ClassName: MyPanel
 * @Description: 主面板
 * @author: Bruce Young
 * @date: 2020年02月02日 17:03
 */
public class MyPanel extends JPanel implements Runnable {

	public MyPanel() {
		super();
//		背景颜色,深灰
		this.setBackground(new Color(0x444444));
	}

	@Override
	public void paint(Graphics g) {
//		保留每次绘制的图像
		super.paint(g);
		g.setColor(new Color(0x00ff00));// 设置医院边界颜色
		// 绘制医院边界
		g.drawRect(Hospital.getInstance().getX(), Hospital.getInstance().getY(), Hospital.getInstance().getWidth(),
				Hospital.getInstance().getHeight());
//		写标题
		g.setFont(new Font("微软雅黑", Font.BOLD, 16));
		g.setColor(new Color(0x00ff00));
		g.drawString("医院", Hospital.getInstance().getX() + Hospital.getInstance().getWidth() / 4,
				Hospital.getInstance().getY() - 16);
		// 绘制代表人类的圆点
//		以单个人为单位的数组,即人群
		List<Person> people = PersonPool.getInstance().getPersonList();
//		人群数为0不继续执行
		if (people == null) {
			return;
		}

//		遍历人群，给每个人设置属性和移动
		for (Person person : people) {
//			根据每个人的状态进行上色
			switch (person.getState()) {
			case Person.State.NORMAL: {
				// 健康人,灰
				g.setColor(new Color(0xdddddd));
				break;
			}
			case Person.State.SHADOW: {
				// 潜伏期感染者,黄
				g.setColor(new Color(0xffee00));
				break;
			}

			case Person.State.CONFIRMED: {
				// 确诊患者,红
				g.setColor(new Color(0xff0000));
				break;
			}
			case Person.State.FREEZE: {
				// 已隔离者,蓝
				g.setColor(new Color(0x48FFFC));
				break;
			}
			case Person.State.DEATH: {
				// 死亡患者,黑
				g.setColor(new Color(0x000000));
				break;
			}
			}
			person.update();// 对各种状态的市民进行不同的处理
//			画一个长宽为3px的点
			g.fillOval(person.getX(), person.getY(), 3, 3);

		}

//		描述区域
		int captionStartOffsetX = 700 + Hospital.getInstance().getWidth() + 40;
		int captionStartOffsetY = 40;
		int captionSize = 24;

		// 显示数据信息
		g.setColor(Color.WHITE);
		g.drawString("城市总人数：" + Constants.CITY_PERSON_SIZE, captionStartOffsetX, captionStartOffsetY);
		g.setColor(new Color(0xdddddd));
		g.drawString("健康者人数：" + PersonPool.getInstance().getPeopleSize(Person.State.NORMAL), captionStartOffsetX,
				captionStartOffsetY + captionSize);
		g.setColor(new Color(0xffee00));
		g.drawString("潜伏期人数：" + PersonPool.getInstance().getPeopleSize(Person.State.SHADOW), captionStartOffsetX,
				captionStartOffsetY + 2 * captionSize);
		g.setColor(new Color(0xff0000));
		g.drawString("发病者人数（确诊）：" + PersonPool.getInstance().getPeopleSize(Person.State.CONFIRMED), captionStartOffsetX,
				captionStartOffsetY + 3 * captionSize);
		g.setColor(new Color(0x48FFFC));
		g.drawString("已隔离人数：" + PersonPool.getInstance().getPeopleSize(Person.State.FREEZE), captionStartOffsetX,
				captionStartOffsetY + 4 * captionSize);
		g.setColor(new Color(0xffffff));
		g.drawLine(captionStartOffsetX, captionStartOffsetY + 4 * captionSize + 5, captionStartOffsetX + 300,
				captionStartOffsetY + 4 * captionSize + 5);

		g.setColor(new Color(0xffffff));
		g.drawString("总病床：" + Math.max(Constants.BED_COUNT, 0), captionStartOffsetX,
				captionStartOffsetY + 5 * captionSize);
		g.setColor(new Color(0x00ff00));
		g.drawString("空余病床："
				+ Math.max(Constants.BED_COUNT - PersonPool.getInstance().getPeopleSize(Person.State.FREEZE), 0),
				captionStartOffsetX, captionStartOffsetY + 6 * captionSize);
		g.setColor(new Color(0xE39476));
		// 暂定急需病床数量为 NEED = 确诊发病者数量 - 已隔离住院数量
		/*
		 * int needBeds = PersonPool.getInstance().getPeopleSize(Person.State.CONFIRMED)
		 * - PersonPool.getInstance().getPeopleSize(Person.State.FREEZE);
		 */
		/*
		 * 医院床位满了之后，新确诊的都没有床位可用，都属于急需病床
		 * 
		 * 急需病床数=确诊人数-空余床位
		 */ int needBeds = PersonPool.getInstance().getPeopleSize(Person.State.CONFIRMED)
				- Math.max(Constants.BED_COUNT - PersonPool.getInstance().getPeopleSize(Person.State.FREEZE), 0);
		g.drawString("急需病床：" + (needBeds > 0 ? needBeds : 0), captionStartOffsetX,
				captionStartOffsetY + 7 * captionSize);
		g.setColor(new Color(0xccbbcc));
		g.drawString("病死人数：" + PersonPool.getInstance().getPeopleSize(Person.State.DEATH), captionStartOffsetX,
				captionStartOffsetY + 8 * captionSize);
		g.setColor(new Color(0xffffff));
//  每秒增加一天
		g.drawString("世界时间（天）：" + (int) (worldTime / 10.0), captionStartOffsetX, captionStartOffsetY + 9 * captionSize);

	}

	public static int worldTime = 0;// 世界时间

	public Timer timer = new Timer();

	/*
	 *
	 * 不断调用paint方法,刷新图形
	 * 
	 * 增加时间,每秒加十
	 * 
	 * timer的回调函数
	 */
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			MyPanel.this.repaint();
			worldTime++;
		}
	}

	/*
	 * 延迟0秒,每隔100毫秒执行一次func (non-Javadoc)
	 * 
	 * 每秒执行10次
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		timer.schedule(new MyTimerTask(), 0, 100);// 启动世界计时器，时间开始流动（突然脑补DIO台词：時は停た）
	}

}
