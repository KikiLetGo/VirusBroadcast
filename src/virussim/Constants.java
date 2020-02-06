package virussim;

/**
 * @ClassName: Constants
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 21:40
 */
public class Constants {

    public static int ORIGINAL_COUNT = 10; // 初始感染数量
    
    public static float BROAD_RATE = 0.8f; // 传播率
    
    public static float SHADOW_TIME = 70; // 潜伏时间
    
    public static int HOSPITAL_RECEIVE_TIME = 10; // 医院收治响应时间
    
    public static int BED_COUNT = 200; // 医院床位
    
    public static float u = 0.99f; // 流动意向平均值
    
    public static int POPULATION = 5000; // 总人口
    
    public static float SAFE_DIST = 2f; // 安全距离

}
