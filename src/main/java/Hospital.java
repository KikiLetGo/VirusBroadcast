import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Hospital
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 20:58
 */
public class Hospital {


    private int x=800;
    private int y=110;

    private int width;
    private int height=606;

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
    public static Hospital getInstance(){
        return hospital;
    }
    private Point point = new Point(800,100);
    private List<Bed> beds = new ArrayList<>();

    private Hospital() {
        if(Constants.BED_COUNT==0){
            width=0;
            height=0;
        }
        int column = Constants.BED_COUNT/100;
        width = column*6;

        for(int i=0;i<column;i++){

            for(int j=10;j<=610;j+=6){
                Bed bed = new Bed(point.getX()+i*6,point.getY()+j);
                beds.add(bed);

            }

        }
    }

    public Bed pickBed(){
        for(Bed bed:beds){
            if(bed.isEmpty()){
                return bed;
            }
        }
        return null;
    }
}
