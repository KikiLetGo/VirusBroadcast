
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @ClassName: MyPanel
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:03
 */
public class MyPanel extends JPanel implements Runnable {


    private int pIndex = 0;

    public MyPanel() {
        this.setBackground(new Color(0x444444));
    }

    @Override
    public void paint(Graphics arg0) {
        super.paint(arg0);
        //draw border
        //如果大于政府响应时间1.增加床位  2.减少人群活动
        List<Person> people = PersonPool.getInstance().getPersonList();
        Long confNum = people.stream().filter(a -> a.getState() == Person.State.CONFIRMED || a.getState() == Person.State.FREEZE).count();
        if (worldTime / 10 >= Constants.GOVREACTION) {
            if (worldTime % 10 == 0 && confNum > 30) {
                Constants.BED_COUNT = Constants.BED_COUNT + Constants.BED_A;
                if (Constants.BED_COUNT > 500) {
                    Constants.BED_COUNT = 500;
                }
            }
            Hospital.getInstance().refreshBeds();
            Constants.uStart = Constants.uStart - Constants.uA * 0.1;
            if (Constants.uStart < -0.99) {
                Constants.uStart = -0.99;
            }
        }
        arg0.setColor(new Color(0x00ff00));
        arg0.drawRect(Hospital.getInstance().getX(), Hospital.getInstance().getY(),
                Hospital.getInstance().getWidth(), Hospital.getInstance().getHeight());

        //文字描绘:
        arg0.setFont(new Font("宋体", Font.PLAIN, 20));
        int intY = 20;
        int intX = 10;
        //arg0.drawString("worldTime: " + worldTime, intX + 10, intY + 80);
        arg0.drawString("总人口: " + 5000, intX + 10, intY + 10);
        Long susNum = people.stream().filter(a -> a.getState() == Person.State.SHADOW || a.getState() == Person.State.SUSPECTED).count();
        arg0.drawString("疑似患者: " + susNum, intX + 180, intY + 10);
        arg0.drawString("确诊患者: " + confNum, intX + 360, intY + 10);
        arg0.setFont(new Font("宋体", Font.PLAIN, 24));
        arg0.drawString("第   天", intX + 540, intY + 10);
        arg0.setColor(Color.RED);
        arg0.setFont(new Font("宋体", Font.PLAIN, 26));
        arg0.drawString(worldTime / 10 + 1 + "", intX + 570, intY + 10);
        arg0.setColor(new Color(0x00ff00));
        arg0.setFont(new Font("宋体", Font.PLAIN, 20));
        arg0.drawString("死亡人数: " + Constants.deadNum, intX + 10, intY + 40);
        arg0.drawString("康复人数: " + Constants.cureNum, intX + 180, intY + 40);
        if (worldTime / 10 >= Constants.GOVREACTION) {
            arg0.drawString("医院总床位: " + Constants.BED_COUNT, intX + 730, intY + 40);
            Long emptyBeds = Hospital.getInstance().getEmptyBedsNum();
            arg0.drawString("空闲床位: " + emptyBeds, intX + 730, intY + 80);
            arg0.drawString("人员流动意向值: " + Constants.uStart, intX + 730, intY + 10);
        }
        if (worldTime / 10 >= Constants.GOVREACTION && worldTime / 10 <= (Constants.GOVREACTION + 3)) {
            arg0.setFont(new Font("宋体", Font.BOLD, 28));
            arg0.setColor(Color.RED);
            arg0.drawString("政府开始反应 ", intX + 350, intY + 80);
        }
        arg0.setFont(new Font("宋体", Font.PLAIN, 24));
        arg0.setColor(new Color(0xdddddd));
        arg0.drawString("未感染人员 " , intX + 800, intY + 290);
        arg0.setColor(new Color(0xffee00));
        arg0.drawString("疑似病例 " , intX + 800, intY + 320);
        arg0.setColor(new Color(0xff0000));
        arg0.drawString("确诊病例", intX + 800, intY + 350);
        arg0.setColor(new Color(0xddddd));
        arg0.drawString("康复病例" , intX + 800, intY + 380);


        if (people == null) {
            return;
        }
        people.get(pIndex).update();
        for (Person person : people) {

            switch (person.getState()) {
                case Person.State.NORMAL: {  //正常
                    arg0.setColor(new Color(0xdddddd));

                }
                break;
                case Person.State.SHADOW: {  //疑似
                    arg0.setColor(new Color(0xffee00));

                }
                break;
                case Person.State.CONFIRMED:
                case Person.State.FREEZE: {  //确诊
                    arg0.setColor(new Color(0xff0000));

                }
                break;
                case Person.State.CURED: {    //康复
                    arg0.setColor(new Color(0xddddd));
                }
                break;
                case Person.State.DEAD: {    //死亡
                    arg0.setColor(new Color(0x444444));
                }
                break;

            }
            person.update();
            arg0.fillOval(person.getX(), person.getY(), 3, 3);

        }
        pIndex++;
        if (pIndex >= people.size()) {
            pIndex = 0;
        }

    }

    public static int worldTime = 0;

    @Override
    public void run() {
        while (true) {
            this.repaint();
            try {
                Thread.sleep(200);
                worldTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
