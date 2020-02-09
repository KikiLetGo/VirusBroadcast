using System;
using System.Collections.Generic;
using System.Text;

namespace VirusBroadcast {
	public class PersonPool {

		public static PersonPool Instance { get; } = new PersonPool();

		public List<Person> PersonList { get; } = new List<Person>();

		public int GetPeopleSize(Person.State state) {
			int i = 0;
			foreach (var person in PersonList) {
				if (person.CurState.CompareTo(state) == 0) {
					i++;
				}
			}
			return i;
		}

		public int GetPeopleSize() => PersonList.Count;

		private PersonPool() {
			var city = new City(400, 400);
			// 添加城市居民
			for (var i = 0; i < Constants.POPULATION; i++) {
				var rand = new Random();
				int x = (int)rand.NextGaussian(100, city.CenterX);
				int y = (int)rand.NextGaussian(100, city.CenterY);
				if (x > 700) {
					x = 700;
				}
				PersonList.Add(new Person(city, x, y));
			}
		}
	}
}
