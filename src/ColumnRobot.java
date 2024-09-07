import java.util.List;

public class ColumnRobot extends Robot implements IRobot{

    private List<IRobot> activeRobots;
    private int numRoom;
    private int currFloor;
    private int currRoom;

    public void setActiveRobots(List<IRobot> activeRobots) {
        this.activeRobots = activeRobots;
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public void setCurrFloor(int currFloor) {
        this.currFloor = currFloor;
    }

    public int getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(int currRoom) {
        this.currRoom = currRoom;
    }

    public ColumnRobot(MailRoom mailroom, int capacity,List<IRobot> activeRobots,int numRoom) {
        super(mailroom, capacity);
        this.activeRobots = activeRobots;
        this.numRoom = numRoom;
    }

}
