/**
 * 模拟参数
 *
 * @ClassName: Constants
 * @Description: 模拟参数
 * @author: Bruce Young
 * @date: 2020年02月02日 21:40
 */
public class Constants {

    public static int ORIGINAL_COUNT = 50;//初始感染数量
    public static float BROAD_RATE = 0.8f;//传播率
    public static float SHADOW_TIME = 140;//潜伏时间，14天为140
    public static int HOSPITAL_RECEIVE_TIME = 10;//医院收治响应时间
    public static int BED_COUNT = 1000;//医院床位
    /**
     * 流动意向平均值，建议调整范围：[-0.99,0.99]
     * <p>
     * -0.99 人群流动最慢速率，甚至完全控制疫情传播
     * 0.99为人群流动最快速率, 可导致全城感染
     * 流动意向平均值随感染(包括死亡和潜伏和隔离)人数占比的函数，可以使用双曲正切函数拟合得到，函数解析式为
     * 使用f(x)将感染比例值域从[0, 1]转化为[-1 , 1], f(x) = -2 * (感染人数+死亡人数+隔离人数+潜伏人数)/城市总人数 -1，
     * 简写为f(x) = -2x + 1
     * 意向流动平均值g(x) = tanh(2 * f(x)) 简写为 y = tanh(3x)
     * g(x)函数的定义域为[0, 1], 值域为(-1,1)
     */
    public static float u = 0.99f;
    public static int CITY_PERSON_SIZE = 5000;//城市总人口数量
    public static float FATALITY_RATE = 0.50f;//fatality_rate病死率，根据2月6日数据估算（病死数/确诊数）为0.02
    public static int DIE_TIME = 100;//死亡时间均值，30天，从发病（确诊）时开始计时
    public static double DIE_VARIANCE = 1;//死亡时间方差
    /**
     * 城市大小即窗口边界，限制不允许出城
     */
    public static final int CITY_WIDTH = 700;
    public static final int CITY_HEIGHT = 800;

}
