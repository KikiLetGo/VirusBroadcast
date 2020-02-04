import org.apache.commons.cli.*;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        if (!overrideConstantsFromArgs(args)) {
            return;
        }

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
        for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
            int index = new Random().nextInt(people.size() - 1);
            Person person = people.get(index);

            while (person.isInfected()) {
                index = new Random().nextInt(people.size() - 1);
                person = people.get(index);
            }
            person.beInfected();

        }


    }

    private static boolean overrideConstantsFromArgs(String[] args) {
        Options options = new Options();
        options.addOption("i", "infected_count", true, "初始的感染人数");
        options.addOption("t", "transfer_rate", true, "传播率");
        options.addOption("s", "shadow_time", true, "潜伏时间");
        options.addOption("r", "receive_time", true, "医院收治响应时间");
        options.addOption("b", "bed_count", true, "医院床位");
        options.addOption("u", "unstable_rate", true, "流动意向平均值");
        options.addOption("h", "help", false, "帮助信息");
        DefaultParser defaultParser = new DefaultParser();
        try {
            CommandLine parse = defaultParser.parse(options, args);
            if (parse.hasOption('h')) {
                showHelp(options);
                return false;
            }
            Constants.ORIGINAL_COUNT = Integer.parseInt(
                    parse.getOptionValue('i', String.valueOf(Constants.ORIGINAL_COUNT)));
            Constants.BROAD_RATE = Float.parseFloat(
                    parse.getOptionValue('t', String.valueOf(Constants.BROAD_RATE)));
            Constants.BED_COUNT = Integer.parseInt(
                    parse.getOptionValue('b', String.valueOf(Constants.BED_COUNT)));
            Constants.HOSPITAL_RECEIVE_TIME = Integer.parseInt(
                    parse.getOptionValue('r', String.valueOf(Constants.HOSPITAL_RECEIVE_TIME)));
            Constants.SHADOW_TIME = Float.parseFloat(
                    parse.getOptionValue('r', String.valueOf(Constants.SHADOW_TIME)));
            Constants.u = Float.parseFloat(
                    parse.getOptionValue('u', String.valueOf(Constants.u)));
            showHelp(options);
        } catch (ParseException e) {
            showHelp(options);
            return false;
        }
        return true;
    }

    private static void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("2019-nCov", options);
    }
}
