
/**
 * @ClassName: Constants
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 21:40
 */
public class Constants {

    public static int ORIGINAL_COUNT = 3;//初始感染数量
    public static float BROAD_RATE = 0.7f;//传播率
    public static float SHADOW_TIME = 80;//平均潜伏时间
    public static int HOSPITAL_RECEIVE_TIME = 15;//医院收治响应时间
    public static int BED_COUNT = 8;//初始医院床位
    public static double uStart = 0.99;//流动意向初始值

    public static double uA = 0.1;//流动意向递减加速度
    public static int PERSON_NUM = 5000;//人口总数
    public static int CURE_TIME = 120;//平均治愈时间
    public static int BED_A = 2;//医院床位加速度
    public static double  MORTALITY= 0.03;//有床位死亡率
    public static double  MORTALITYNoBed= 0.3;//无床位死亡率
    public static int GOVREACTION = 10;//政府反应时间

    public static int deadNum = 0; //死亡数
    public static int cureNum = 0;
}
