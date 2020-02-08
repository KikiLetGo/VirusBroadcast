import java.util.ArrayList;
import java.util.List;

/**
 * 医院
 * <p>
 * 床位容量
 *
 * @ClassName: Hospital
 * @Description: 医院，包含床位容量
 * @author: Bruce Young
 * @date: 2020年02月02日 20:58
 */
public class Hospital {

	//医院矩形所在坐标
    private int x = 800;
    private int y = 110;

    private int width;
    private int height = 606;

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private static Hospital hospital = new Hospital();

    public static Hospital getInstance() {
        return hospital;
    }

    private Point point = new Point(800, 100);//第一个床位所在坐标，用于给其他床位定绝对坐标
    private List<Bed> beds = new ArrayList<Bed>();

    private Hospital() {
    	//根据床位数量调整医院矩形的大小
        if (Constants.BED_COUNT == 0) {
            width = 0;
            height = 0;
        }
        //根据医院床位数量计算医院宽度
        //因为高度定了只能装载100个床位
        int column = Constants.BED_COUNT / 100;
        width = column * 6;
        //根据第一个床位坐标初始化其他床位的坐标
        for (int i = 0; i < column; i++) {

            for (int j = 10; j <= 610; j += 6) {
                Bed bed = new Bed(point.getX() + i * 6, point.getY() + j);
                beds.add(bed);
            }

        }
    }

    /**
     * 使用床位
     *
     * @return
     */
    public Bed pickBed() {
        for (Bed bed : beds) {
            if (bed.isEmpty()) {
                return bed;
            }
        }
        return null;
    }

    /**
     * 死亡或痊愈出院空出床位
     *
     * @param bed
     * @return
     */
    public Bed returnBed(Bed bed) {
        bed.setEmpty(false);
        return bed;
    }

    public void returnBed() {
        for (Bed bed : beds){
            if (!bed.isEmpty()){
                bed.setEmpty(true);
            }
        }
    }
}
