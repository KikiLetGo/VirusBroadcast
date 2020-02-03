import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {




    public static void main(String[] args) {
        MyPanel p = new MyPanel();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();

        List<Person> people = PersonPool.getInstance().getPersonList();
        for(int i=0;i<Constants.ORIGINAL_COUNT;i++){
            int index = new Random().nextInt(people.size()-1);
            Person person = people.get(index);

            while (person.isInfected()){
                index = new Random().nextInt(people.size()-1);
                person = people.get(index);
            }
            person.beInfected();

        }


    }
}
