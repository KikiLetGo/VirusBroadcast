import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 区域人群对象池
 *
 * @ClassName: PersonPool
 * @Description: 区域人群对象池，该地区假设为一个近似封闭的环境，拥有几乎不变的民众数量
 * @author: Bruce Young
 * @date: 2020年02月02日 17:21
 */
public class PersonPool {
    private static PersonPool personPool = new PersonPool();

    public static PersonPool getInstance() {
        return personPool;
    }

    List<Person> personList = new ArrayList<Person>();

    public List<Person> getPersonList() {
        return personList;
    }

    /**
     * 人群对象池，当前仅单个城市实例
     */
    private PersonPool() {
        City city = new City(400, 400);
        for (int i = 0; i < 5000; i++) {
            Random random = new Random();
            int x = (int) (100 * random.nextGaussian() + city.getCenterX());
            int y = (int) (100 * random.nextGaussian() + city.getCenterY());
            if (x > 700) {
                x = 700;
            }
            Person person = new Person(city, x, y);
            personList.add(person);
        }
    }
}
