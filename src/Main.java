import javax.swing.*;

import java.awt.Color;
import java.util.List;
import java.util.Random;

public class Main {
    
    public static final JLabel hospitalState = new JLabel();

    public static void main(String[] args) {
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
        JPanel statePanel = new JPanel();
        statePanel.setBackground(null);
        hospitalState.setForeground(Color.RED);
        statePanel.add(Main.hospitalState);

        p.add(statePanel);
        frame.add(p);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setTitle("瘟疫传播模拟");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();
    }

    /**
     * 初始化初始感染者
     */
    private static void initInfected() {
        List<Person> people = PersonPool.getInstance().getPersonList();// 获取所有的市民
        for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));// 随机挑选一个市民
            } while (person.isInfected());// 如果该市民已经被感染，重新挑选
            person.beInfected();// 让这个幸运的市民成为感染者
        }
    }
}
