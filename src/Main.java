import javax.swing.*;

import java.util.List;
import java.util.Random;

/**
 * 模拟程序主入口
 *
 * @author
 * @comment GinRyan
 */
public class Main {

    public static void main(String[] args) {
        initHospital();
        initPanel();
        initInfected();
    }

    /**
     * 初始化画布
     */
    private static void initPanel() {
        MyPanel p = new MyPanel();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(Constants.CITY_WIDTH + hospitalWidth + 300, Constants.CITY_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("瘟疫传播模拟");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();//开启画布线程，即世界线程，接着看代码的下一站可以转MyPanel.java
    }

    private static int hospitalWidth;

    /**
     * 初始化医院参数
     */
    private static void initHospital() {
        hospitalWidth = Hospital.getInstance().getWidth();
    }

    /**
     * 初始化初始感染者
     */
    private static void initInfected() {
        List<Person> people = PersonPool.getInstance().getPersonList();//获取所有的市民
        for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));//随机挑选一个市民
            } while (person.isInfected());//如果该市民已经被感染，重新挑选
            person.beInfected();//让这个幸运的市民成为感染者
        }
    }

}
