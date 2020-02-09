using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {

	/// <summary>
	/// Random类扩展
	/// </summary>
	public static class RandomExtensions {

		/// <summary>
		/// 生成一个服从正态分布（高斯分布）的随机数（扩展方法）
		/// </summary>
		/// <param name="this">所在对象</param>
		/// <param name="mu">均值 (μ)</param>
		/// <param name="sigma">标准差 (σ)</param>
		/// <returns>一个正态随机数</returns>
		public static double NextGaussian(this Random @this, double sigma = 1, double mu = 0) {
			var u1 = @this.NextDouble();
			var u2 = @this.NextDouble();

			var stdRand = Math.Sqrt(-2.0 * Math.Log(u1) * Math.Sin(2.0 * Math.PI * u2));
			var rand = mu + sigma * stdRand;
			return rand;
		}
	}

}
