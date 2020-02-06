import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName: MyPanel
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:03
 */
public class MyPanel extends JPanel implements Runnable {

    private int pIndex = 0;

    public MyPanel() {
        super();
        this.setBackground(new Color(0x444444));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0x00ff00));//设置医院边界颜色
        //绘制医院边界
        g.drawRect(Hospital.getInstance().getX(), Hospital.getInstance().getY(),
                Hospital.getInstance().getWidth(), Hospital.getInstance().getHeight());
        g.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g.setColor(new Color(0x00ff00));
        g.drawString("医院", Hospital.getInstance().getX() + Hospital.getInstance().getWidth() / 4, Hospital.getInstance().getY() - 16);

        List<Person> people = PersonPool.getInstance().getPersonList();
        if (people == null) {
            return;
        }
        people.get(pIndex).update();
        for (Person person : people) {
            switch (person.getState()) {
                case Person.State.NORMAL: {
                    g.setColor(new Color(0xdddddd));
                    break;
                }
                case Person.State.SHADOW: {
                    g.setColor(new Color(0xffee00));
                    break;
                }
                case Person.State.CONFIRMED:
                    g.setColor(new Color(0xff0000));
                    break;
                case Person.State.FREEZE: {
                    g.setColor(new Color(0x48FFFC));
                    break;
                }
            }
            person.update();
            g.fillOval(person.getX(), person.getY(), 3, 3);

        }
        pIndex++;
        if (pIndex >= people.size()) {
            pIndex = 0;
        }

        //显示数据信息
        g.setColor(Color.WHITE);
        g.drawString("城市总人数：" + Constants.CITY_PERSON_SIZE, 16, 40);
        g.setColor(new Color(0xdddddd));
        g.drawString("健康者人数：" + PersonPool.getInstance().getPeopleSize(Person.State.NORMAL), 16, 64);
        g.setColor(new Color(0xffee00));
        g.drawString("潜伏者人数：" + PersonPool.getInstance().getPeopleSize(Person.State.SHADOW), 16, 88);
        g.setColor(new Color(0xff0000));
        g.drawString("感染者人数：" + PersonPool.getInstance().getPeopleSize(Person.State.CONFIRMED), 16, 112);
        g.setColor(new Color(0x48FFFC));
        g.drawString("已隔离人数：" + PersonPool.getInstance().getPeopleSize(Person.State.FREEZE), 16, 136);
        g.setColor(new Color(0x00ff00));
        g.drawString("空余病床：" + (Constants.BED_COUNT - PersonPool.getInstance().getPeopleSize(Person.State.FREEZE)), 16, 160);
    }

    public static int worldTime = 0;

    public Timer timer = new Timer();

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            MyPanel.this.repaint();
            worldTime++;
        }
    }

    @Override
    public void run() {
        timer.schedule(new MyTimerTask(), 0, 100);
    }


}
