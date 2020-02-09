using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Text;

namespace VirusBroadcast {
	public class Person : Point {

		public City City { get; }

		private TargetMovement move;

		private readonly int SIGMA = 1; // σ

		private double targetXU;
		private double targetYU;
		private double targetSig = 50;

		public enum State {
			NORMAL, SUSPECTED, SHADOW, CONFIRMED, FREEZE, CURED
		}

		public Person(City city, int x, int y) : base(x, y) {
			City = city;
			targetXU = 100 * new Random().NextGaussian() + x;
			targetYU = 100 * new Random().NextGaussian() + y;
		}

		public bool WantMove() {
			return new Random().NextGaussian(Constants.Mu, SIGMA) > 0;
		}

		public State CurState { get; set; } = State.NORMAL;

		private int infectedTime = 0;
		private int comfirmedTime = 0;

		public bool IsInfected() {
			return CurState.CompareTo(State.SHADOW) > 0;
		}

		public void BeInfected() {
			CurState = State.SHADOW;
			//infectedTime = 
			// TODO
		}
	}

}
