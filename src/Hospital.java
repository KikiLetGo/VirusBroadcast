
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
    private int height=66;
    //private int height=506;

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
        int column = (int)(Math.ceil(Constants.BED_COUNT/10.0));
        width = column*6;

        int bedNum = Constants.BED_COUNT;
        for(int i=0;i<column;i++){
            for(int j=10;j<=66;j+=6){
                if(bedNum<=0) break;
                Bed bed = new Bed(point.getX()+i*6,point.getY()+j);
                beds.add(bed);
                bedNum--;
            }
        }
    }


    public Bed pickBed(){
        for(Bed bed:beds){
            if(bed.isEmpty()){
                bed.setEmpty(false);
                return bed;
            }
        }
        return null;
    }

    public Bed getUsedBed(){
        for(Bed bed:beds){
            if(!bed.isEmpty()){
                bed.setEmpty(true);
                return bed;
            }
        }
        return null;
    }

    public Bed getLastBed(){
        return beds.get(beds.size()-1);
    }

    public List<Bed> getBeds(){
        return beds;
    }

    public void refreshBeds() {
        Bed lastBed = getLastBed();
        int column = (int)(Math.ceil(Constants.BED_COUNT/10.0));
        width = column*6;
        //床位增长时
        if(beds.size()<Constants.BED_COUNT){
            //上一个床位如果在最后一行，就换行，否则只增加Y坐标
            if(lastBed.getY()>=(point.getY()+66)){
                Bed bed = new Bed(lastBed.getX()+6,point.getY()+10);
                beds.add(bed);
            }else{
                Bed bed = new Bed(lastBed.getX(),lastBed.getY()+6);
                beds.add(bed);
            }

        }
    }

    public Long getEmptyBedsNum() {
        return beds.stream().filter(a->a.isEmpty()).count();
    }
}
