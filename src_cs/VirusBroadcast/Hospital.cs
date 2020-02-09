using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {
	public class Hospital {

		public int X { get; } = 800;

		public int Y { get; } = 110;

		public int Width { get; }

		public int Height { get; } = 606;

		private Point point = new Point(800, 100);

		private List<Bed> beds = new List<Bed>();

		public static Hospital Instance { get; } = new Hospital();

		private Hospital() {
			var column = Constants.BED_COUNT / 100;
			Width = column * 6;

			for (int i = 0; i < column; i++) {
				for (int j = 10; j <= 604; j += 6) {
					beds.Add(new Bed(point.X + i * 6, point.Y + j));
				}
			}
		}

		public Bed PickBed() {
			foreach(var bed in beds) {
				if (bed.IsEmpty) {
					return bed;
				}
			}
			return null;
		}
	}
}
