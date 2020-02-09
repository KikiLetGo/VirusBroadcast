using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {
    public class Bed : Point {
        public Bed(int x, int y) : base(x, y) {
        }

        public bool IsEmpty { get; set; } = true;
    }
}
