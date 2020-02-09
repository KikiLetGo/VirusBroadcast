import java.util.regex.Pattern;

/**
 * 参数传递工具
 * 
 * @author dy55
 */
public class ArgSolver {

	private static int cursor = 0;

	/**
	 * 初始化
	 * 
	 * @param args 传入参数
	 */
	public static void cliInit(String[] args) {
		for (; cursor < args.length; cursor++) {
			if (!Pattern.matches("-.+", args[cursor])) {
				errReport("传入参数错误");
				System.exit(0);
			}
			switch (args[cursor].trim().toLowerCase()) {
			// 初始感染数量
			case "-o":
			case "--original":
				Constants.ORIGINAL_COUNT = Integer.valueOf(args[++cursor]);
				break;

			// 传播率
			case "-b":
			case "--broad-rate":
				Constants.BROAD_RATE = Float.valueOf(args[++cursor]);
				break;

			// 潜伏时间
			case "-s":
			case "--shadow":
				Constants.SHADOW_TIME = Float.valueOf(args[++cursor]);
				break;

			// 医院收治时间
			case "-r":
			case "--receive":
				Constants.HOSPITAL_RECEIVE_TIME = Integer.valueOf(args[++cursor]);
				break;

			// 床位
			case "-c":
			case "--bed-count":
				Constants.BED_COUNT = Integer.valueOf(args[++cursor]);
				break;

			// 流动意向平均值μ
			case "-m":
			case "--move-u":
				Constants.u = Float.valueOf(args[++cursor]);
				break;

			// 总人口
			case "-p":
			case "--population":
				Constants.POPULATION = Integer.valueOf(args[++cursor]);
				break;

			// 安全距离
			case "-d":
			case "--safe-dist":
				Constants.SAFE_DIST = Float.valueOf(args[++cursor]);
				break;

			// 其他
			default:
				errReport("传入参数错误");
				// 没有Break，直接运行帮助

				// 帮助
			case "-h":
			case "-?":
				getHelp();
				System.exit(0);
				break;
			}
		}

		System.out.println("-------------------");
		System.out.println("参数信息：\n");

		System.out.println("初始感染数量：" + Constants.ORIGINAL_COUNT);
		System.out.println("传播率：" + Constants.BROAD_RATE);
		System.out.println("潜伏时间：" + Constants.SHADOW_TIME);
		System.out.println("收治响应时间：" + Constants.HOSPITAL_RECEIVE_TIME);
		System.out.println("医院床位：" + Constants.BED_COUNT);
		System.out.println("流动意向平均值：" + Constants.u);
		System.out.println("城市总人口：" + Constants.POPULATION);
		System.out.println("安全距离：" + Constants.SAFE_DIST);

		System.out.println("--------------------");
	}

	/**
	 * 获取帮助信息
	 */
	private static void getHelp() {
		System.out.println("瘟疫传播模拟程序");
		System.out.println("----------------------------------------------------");
		System.out.println("可用命令：");
		System.out.println("            -h, -?    获取帮助信息\n");

		System.out.println("    -o, --original    自定义初始感染数量");
		System.out.println("  -b, --broad-rate    自定义传播率（0 ~ 1的小数）");
		System.out.println("      -s, --shadow    自定义潜伏时间");
		System.out.println("     -r, --receive    自定义医院收治时间");
		System.out.println("   -c, --bed-count    自定义床位");
		System.out.println("      -m, --move-u    自定义流动意向平均值（-0.99 ~ 0.99）");
		System.out.println("  -p, --population    自定义城市人口（建议在5000左右）");
		System.out.println("   -d, --safe-dist    自定义安全距离");
		System.out.println("----------------------------------------------------");
	}

	/**
	 * 错误输出
	 * 
	 * @param message 错误信息
	 */
	private static void errReport(String message) {
		System.out.println();
		System.err.println("错误：");
		System.err.println(message);
	}
}