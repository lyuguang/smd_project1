import java.util.List;

public class FlooringRobot extends Robot implements IRobot{
    private List<IRobot> activeRobots;

    public int getTransferPosition() {
        return transferPosition;
    }

    public void setTransferPosition(int transferPosition) {
        this.transferPosition = transferPosition;
    }

    private int transferPosition = -1;

    private int numRooms;

    FlooringRobot(MailRoom mailroom, int capacity, List<IRobot> activeRobots, int numRooms, int floor, int room) {
        super(mailroom, capacity);
        this.activeRobots = activeRobots;
        this.numRooms = numRooms;
        this.floor = floor;
        this.room = room;
    }

    public List<IRobot> getActiveRobots() {
        return activeRobots;
    }

    public void setActiveRobots(List<IRobot> activeRobots) {
        this.activeRobots = activeRobots;
    }
}
