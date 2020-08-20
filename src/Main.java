import java.util.logging.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

/**
 * 模拟程序主入口
 *
 * @author
 * @comment GinRyan
 * @modify badboysing
 * 
 */
public class Main extends JFrame {

    public Main() {
        initComponents();
    }
    
    /*
     *  初始参数定义窗口 ---- 用户可在模拟程序运行前定义参数 （功能尚未完善，比如未限制用户输入非数字）
     * Variable Modifier Interface 
     * 
     * */
                    
    private void initComponents() {

        RunBttn = new JButton();
        CityPopTxtBox = new JTextField();
        CityPopLbl = new JLabel();
        HosCapLab = new JLabel();
        HosCapTxtBox = new JTextField();
        defaultValueRBttn = new JRadioButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        RunBttn.setText("Run");
        RunBttn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                RunBttnActionPerformed(evt);
            }
        });

        CityPopTxtBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CityPopTxtBoxActionPerformed(evt);
            }
        });

        CityPopLbl.setText("City Population");

        HosCapLab.setText("Hospital Capacity");

        HosCapTxtBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                HosCapTxtBoxActionPerformed(evt);
            }
        });

        defaultValueRBttn.setText("Default Value");
        defaultValueRBttn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                defaultValueRBttnActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RunBttn, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(defaultValueRBttn)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(CityPopLbl, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                            .addComponent(HosCapLab, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(HosCapTxtBox)
                            .addComponent(CityPopTxtBox, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(CityPopLbl)
                    .addComponent(CityPopTxtBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(HosCapLab)
                    .addComponent(HosCapTxtBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(defaultValueRBttn)
                .addGap(9, 9, 9)
                .addComponent(RunBttn)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }   
    /*
     * Execute after clicking Run button
     * */

    private void RunBttnActionPerformed(ActionEvent evt) { 
    	String cityPerson = CityPopTxtBox.getText();
    	Constants.CITY_PERSON_SIZE = Integer.parseInt(cityPerson);
    	String hospitalBed = HosCapTxtBox.getText();
    	Constants.BED_COUNT = Integer.parseInt(hospitalBed);
    	initHospital();
        initPanel();
        initInfected();
    }

    private void CityPopTxtBoxActionPerformed(ActionEvent evt) {                                              
    	
    }                                             

    private void HosCapTxtBoxActionPerformed(ActionEvent evt) {                                             
 
    }                                            
    
    /*
     * 设定默认数值，城市人口 = 5000人， 医院病床 = 1000张
     * Default Variable = City Population = 5000, Hospital Capacity = 1000
     * */
    private void defaultValueRBttnActionPerformed(ActionEvent evt) {                                                  
            CityPopTxtBox.setText("5000");
            HosCapTxtBox.setText("1000");
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


    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration                  
    private JLabel CityPopLbl;
    private JTextField CityPopTxtBox;
    private JLabel HosCapLab;
    private JTextField HosCapTxtBox;
    private JButton RunBttn;
    private JRadioButton defaultValueRBttn;
    // End of variables declaration                   
}
