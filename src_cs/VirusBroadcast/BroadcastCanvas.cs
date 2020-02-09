using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Text;
using System.Windows.Controls;
using System.Timers;

namespace VirusBroadcast {
    public partial class BroadcastCanvas: Canvas {

        public static int WorldTime { get; private set; } = 0;

        public Timer timer = new Timer();

        public void RunAction() {
            timer.Interval += 100;
            timer.Elapsed += (sender, e) => {

            };
        }
    }
}
