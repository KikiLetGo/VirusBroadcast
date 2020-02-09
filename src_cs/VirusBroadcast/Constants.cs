using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {
	public class Constants {
		private Constants() { }

        public static int ORIGINAL_COUNT { get; set; } = 10; // 初始感染数量

        public static float BROAD_RATE { get; set; } = 0.8f; // 传播率

        public static float SHADOW_TIME { get; set; } = 70; // 潜伏时间

        public static int HOSPITAL_RECEIVE_TIME { get; set; } = 10; // 医院收治响应时间

        public static int BED_COUNT { get; set; } = 200; // 医院床位

        public static float Mu { get; set; } = 0.99f; // 流动意向平均值

        public static int POPULATION { get; set; } = 5000; // 总人口

        public static float SAFE_DIST { get; set; } = 2f; // 安全距离
    }
}
