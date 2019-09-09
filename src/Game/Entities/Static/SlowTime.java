package Game.Entities.Static;
import Main.Handler;

//class for slow time power up
public class SlowTime {

    private Handler handler;

    public int xCoord;
    public int yCoord;

    public SlowTime(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }

}