package virussim;

import java.util.ArrayList;
import java.util.List;

/**
 * 医院
 *
 * @ClassName: Hospital
 * @Description: 医院，包含床位容量
 * @author: Bruce Young
 * @date: 2020年02月02日 20:58
 */
public class Hospital extends Point {

    public static final int HOSPITAL_X = 720;
    public static final int HOSPITAL_Y = 80;

    private int width;
    private int height = 600;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private static Hospital hospital = new Hospital();

    public static Hospital getInstance() {
        return hospital;
    }

    private Point point = new Point(HOSPITAL_X, HOSPITAL_Y);
    private List<Bed> beds = new ArrayList<>();

    /**
     * 获取所有床位
     *
     * @return
     */
    public List<Bed> getBeds() {
        return beds;
    }

    private Hospital() {
        super(HOSPITAL_X, HOSPITAL_Y + 10);
        // if (Constants.BED_COUNT == 0) {
        //     width = 0;
        //     height = 0;
        // }
        int column = Constants.BED_COUNT / 100;
        width = column * 6;

        for (int i = 0; i < column; i++) {

            for (int j = 10; j <= 604; j += 6) {
                Bed bed = new Bed(point.getX() + i * 6, point.getY() + j);
                beds.add(bed);

            }

        }
    }

    public Bed pickBed() {
        for (Bed bed : beds) {
            if (bed.isEmpty()) {
                return bed;
            }
        }
        return null;
    }
}
