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
     * @param state 市民类型 Person.State的值，若为-1则返回当前总数目
     * @return 获取指定人群数量
     */
    public int getPeopleSize(int state) {
        if (state == -1) {
            return personList.size();
        }
        int i = 0;
        for (Person person : personList) {
            if (person.getState() == state) {
                i++;
            }
        }
        return i;
    }
    

/*
 * 初始化对象的时候,添加人群每个人的城市和坐标
 * 
 * 设定城市中心
 * 
 * 以城市中心作正态分布，生成城市里每个人的初始坐标，然后生成移动后的坐标，添加到personList
 */
    private PersonPool() {
        City city = new City(400, 400);//设置城市中心为坐标(400,400)
        //添加城市居民
        for (int i = 0; i < Constants.CITY_PERSON_SIZE; i++) {
            Random random = new Random();
            //产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
            int x = (int) (100 * random.nextGaussian() + city.getCenterX());
            int y = (int) (100 * random.nextGaussian() + city.getCenterY());
//            正态分布x不超过700，y不受限制
            if (x > 700) {
                x = 700;
            }
//            以城市为中心生成每个人的坐标
            personList.add(new Person(city, x, y));
        }
    }
}
