/**
 * @ClassName: Constants
 * @Description: 模拟参数
 * @Author: cnctemaR
 * @Date: 2020/2/7 0:40
 * */
object Constants {
    const val ORIGINAL_COUNT = 50//初始感染数量
    const val BROAD_RATE = 0.8//传播率
    const val SHADOW_TIME = 140.0//潜伏时间，14天为140
    const val HOSPITAL_RECEIVE_TIME = 10//医院收治响应时间
    const val BED_COUNT = 1000//医院床位
    /**
     * 流动意向平均值，建议调整范围：[-0.99,0.99]
     *
     *
     * -0.99 人群流动最慢速率，甚至完全控制疫情传播
     * 0.99为人群流动最快速率, 可导致全城感染
     */
    const val u = 0.99
    const val CITY_PERSON_SIZE = 5000//城市总人口数量
    const val FATALITY_RATE = 0.50//fatality_rate病死率，根据2月6日数据估算（病死数/确诊数）为0.02
    const val DIE_TIME = 100//死亡时间均值，30天，从发病（确诊）时开始计时
    const val DIE_VARIANCE = 1.0//死亡时间方差
    /**
     * 城市大小即窗口边界，限制不允许出城
     */
    const val CITY_WIDTH = 700
    const val CITY_HEIGHT = 800
}
