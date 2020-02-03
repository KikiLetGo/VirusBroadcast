import java.util.List;
import java.util.Random;

/**
 * @ClassName: Person
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:05
 */
public class Person {
    private City city;
    private int x;
    private int y;
    private MoveTarget moveTarget;
    int sig=1;


    double targetXU;
    double targetYU;
    double targetSig=50;


    public interface State{
        int NORMAL = 0;
        int SUSPECTED = NORMAL+1;
        int SHADOW = SUSPECTED+1;

        int CONFIRMED = SHADOW+1;
        int FREEZE = CONFIRMED+1;
        int CURED = FREEZE+1;
    }

    public Person(City city, int x, int y) {
        this.city = city;
        this.x = x;
        this.y = y;
        targetXU = 100*new Random().nextGaussian()+x;
        targetYU = 100*new Random().nextGaussian()+y;

    }
    public boolean wantMove(){
        double value = sig*new Random().nextGaussian()+Constants.u;
        return value>0;
    }

    private int state=State.NORMAL;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    int infectedTime=0;
    int confirmedTime=0;
    public boolean isInfected(){
        return state>=State.SHADOW;
    }
    public void beInfected(){
        state = State.SHADOW;
        infectedTime=MyPanel.worldTime;
    }

    public double distance(Person person){
        return Math.sqrt(Math.pow(x-person.getX(),2)+Math.pow(y-person.getY(),2));
    }

    private void freezy(){
        state = State.FREEZE;
    }
    private void moveTo(int x,int y){
        this.x+=x;
        this.y+=y;
    }
    private void action(){
        if(state==State.FREEZE){
            return;
        }
        if(!wantMove()){
            return;
        }
        if(moveTarget==null||moveTarget.isArrived()){

            double targetX = targetSig*new Random().nextGaussian()+targetXU;
            double targetY = targetSig*new Random().nextGaussian()+targetYU;
            moveTarget = new MoveTarget((int)targetX,(int)targetY);

        }


        int dX = moveTarget.getX()-x;
        int dY = moveTarget.getY()-y;
        double length=Math.sqrt(Math.pow(dX,2)+Math.pow(dY,2));

        if(length<1){
            moveTarget.setArrived(true);
            return;
        }
        int udX = (int) (dX/length);
        if(udX==0&&dX!=0){
            if(dX>0){
                udX=1;
            }else{
                udX=-1;
            }
        }
        int udY = (int) (dY/length);
        if(udY==0&&udY!=0){
            if(dY>0){
                udY=1;
            }else{
                udY=-1;
            }
        }

        if(x>700){
            moveTarget=null;
            if(udX>0){
                udX=-udX;
            }
        }
        moveTo(udX,udY);

//        if(wantMove()){
//        }


    }

    private float SAFE_DIST = 2f;

    public void update(){
        //@TODO找时间改为状态机
        if(state>=State.FREEZE){
            return;
        }
        if(state==State.CONFIRMED&&MyPanel.worldTime-confirmedTime>=Constants.HOSPITAL_RECEIVE_TIME){
            Bed bed = Hospital.getInstance().pickBed();
            if(bed==null){
                System.out.println("隔离区没有空床位");
            }else{
                state=State.FREEZE;
                x=bed.getX();
                y=bed.getY();
                bed.setEmpty(false);
            }
        }
        if(MyPanel.worldTime-infectedTime>Constants.SHADOW_TIME&&state==State.SHADOW){
            state=State.CONFIRMED;
            confirmedTime = MyPanel.worldTime;
        }

        action();

        List<Person> people = PersonPool.getInstance().personList;
        if(state>=State.SHADOW){
            return;
        }
       for(Person person:people){
           if(person.getState()== State.NORMAL){
               continue;
           }
           float random = new Random().nextFloat();
           if(random<Constants.BROAD_RATE&&distance(person)<SAFE_DIST){
               this.beInfected();
           }
       }
    }
}
