import javax.swing.*;

import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.MenuListener;

import javax.swing.event.MenuEvent;

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
	 * 初始化窗体及画布
	 */
	private static void initPanel() {
		MyPanel p = new MyPanel();
		Thread panelThread = new Thread(p);
		JFrame frame = new JFrame();
		frame.getContentPane().add(p);
		frame.setSize(Constants.CITY_WIDTH + hospitalWidth + 300, Constants.CITY_HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setTitle("瘟疫传播模拟——————新冠状病毒");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelThread.start();// 开启画布线程，即世界线程，接着看代码的下一站可以转MyPanel.java

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(frame, popupMenu);

		JMenuItem menuItem = new JMenuItem("关于");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				JOptionPane.showMessageDialog(null, "武汉加油！湖北加油！中国加油！");
			}
		});
		popupMenu.add(menuItem);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("控制人口流动");
		menu.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuSelected(MenuEvent e) {

				Constants.u = -0.99f;
				resetWorld();

			}
		});

		menuBar.add(menu);

		JMenu menu_1 = new JMenu("不控制人口流动");
		menu_1.addMenuListener(new MenuListener() {

			public void menuCanceled(MenuEvent e) {
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuSelected(MenuEvent e) {
				Constants.u = 0.99f;
				resetWorld();
			}
		});
		menuBar.add(menu_1);

		frame.setVisible(true);
	}

	/**
	 * 重新开始
	 */
	static void resetWorld() {
		List<Person> people = PersonPool.getInstance().getPersonList();
		for (Person person : people) {
			person.setState(Person.State.NORMAL);
		}

		List<Bed> beds = Hospital.getInstance().getBeds();
		for (Bed bed : beds) {
			bed.setEmpty(true);
		}

		initInfected();
	}

	/**
	 * 医院宽度
	 */
	private static int hospitalWidth;

	/**
	 * 初始化医院参数
	 * 
	 * 根据最大床位数生成医院宽度和所有床位坐标
	 * 
	 * 返回医院宽度
	 */
	private static void initHospital() {
		hospitalWidth = Hospital.getInstance().getWidth();
	}

	/**
	 * 初始化初始感染者
	 */
	private static void initInfected() {
		List<Person> people = PersonPool.getInstance().getPersonList();// 获取所有的市民信息（城市中心坐标，个人坐标）
//       随机选择一部分未被感染的人进行感染
		for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
			Person person;
			do {
				person = people.get(new Random().nextInt(people.size() - 0));// 随机挑选一个市民
			} while (person.isInfected());// 如果该市民已经被感染，重新挑选
			person.beInfected();// 让这个幸运的市民成为感染者
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
