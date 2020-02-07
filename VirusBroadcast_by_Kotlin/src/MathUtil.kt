import java.util.*

/**
 * @ClassName:
 * @Description:数学算法工具类
 * @Author: cnctemaR
 * @Date: 2020/2/7 2:31
 * */

class MathUtil{
    companion object {
        private val randomGen = Random()
        /**
         * 标准正态分布化
         * <p>
         * 流动意愿标准化后判断是在0的左边还是右边从而决定是否流动。
         * <p>
         * 设X随机变量为服从正态分布，sigma是影响分布形态的系数 u值决定正态分布均值
         * <p>
         * <p>
         * 推导：
         * StdX = (X-u)/sigma
         * X = sigma * StdX + u
         *
         * @param sigma 正态标准差sigma值
         * @param u     正态均值参数mu
         * @return
         */
        fun stdGaussian(sigma:Double,u:Double) = sigma*randomGen.nextGaussian()+u
    }
}