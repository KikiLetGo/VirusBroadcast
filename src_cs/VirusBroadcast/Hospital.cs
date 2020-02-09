using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {
	public class Hospital : Point {

		public static int HOSPITAL_X { get; } = 720;
		public static int HOSPITAL_Y { get; } = 80;

		public int Width { get; }

		public int Height { get; } = 600;

		private Point point = new Point(800, 100);

		private List<Bed> beds = new List<Bed>();

		public static Hospital Instance { get; } = new Hospital();

		private Hospital() : base(HOSPITAL_X, HOSPITAL_Y) {
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

		public Bed LeaveBed(Bed bed) {
			if(bed != null) {
				bed.IsEmpty = true;
			}
			return bed;
		}
	}
}
