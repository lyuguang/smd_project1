import java.util.List;

public class ColumnRobot extends Robot implements IRobot{

    private List<IRobot> activeRobots;
    private int numRoom;

    public void setActiveRobots(List<IRobot> activeRobots) {
        this.activeRobots = activeRobots;
    }

    public ColumnRobot(MailRoom mailroom, int capacity,List<IRobot> activeRobots,int numRoom) {
        super(mailroom, capacity);
        this.activeRobots = activeRobots;
        this.numRoom = numRoom;
    }

}
