using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {
	public class MoveTarget : Point {
		public MoveTarget(int x, int y) : base(x, y) {
		}

		public bool IsArrived { get; set; } = false;

	}
}
