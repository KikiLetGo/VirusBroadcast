import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
        MyPanel p = new MyPanel();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(1100, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();
        // 从该地区进行初始感染源随机选取。随机选取ORIGINAL_COUNT个Person为初始感染源
        List<Person> people = PersonPool.getInstance().getPersonList();
        for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
            int index = new Random().nextInt(people.size() - 1);
            Person person = people.get(index);
            //如果随机选择为已感染则重新选择
            while (person.isInfected()) {
                index = new Random().nextInt(people.size() - 1);
                person = people.get(index);
            }
            //设定为感染对象
            person.beInfected();

        }


    }
}
