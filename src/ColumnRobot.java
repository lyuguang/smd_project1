/**
 * @author Guangxing Lyu and Yujian Wang
 * @date 2/9/2024 AM11:48
 */
public class ColumnRobot extends Robot{
    private final boolean isLeftColumn;
    private int destinationFloor;

    public ColumnRobot(MailRoom mailRoom, int capacity, boolean isLeftColumn) {
        super(mailRoom, capacity);
        this.isLeftColumn = isLeftColumn;
        place(0, isLeftColumn ? 0 : Building.getBuilding().NUMROOMS + 1);
    }

    @Override
    void tick() {
        if (isEmpty()) {
            if (getFloor() > 0) {
                move(Building.Direction.DOWN);
            } else {
                // At mailroom, try to load items
                int floorWithItems = mailroom.floorWithEarliestItem();
                if (floorWithItems >= 0) {
                    mailroom.loadRobot(floorWithItems, this);
                    if (!isEmpty()) {
                        destinationFloor = floorWithItems + 1;
                    }
                }
            }
        } else {
            if (getFloor() < destinationFloor) {
                move(Building.Direction.UP);
            } else if (getFloor() == destinationFloor) {
                FloorRobot floorRobot = mailroom.getFloorRobot(destinationFloor);
                if (floorRobot != null) {
                    floorRobot.transfer(this);
                    System.out.println("Transferring items from " + getId() + " to floor robot " + floorRobot.getId());
                }
                if (isEmpty()) {
                    destinationFloor = 0;
                }
            }
        }
    }

    public void setDestinationFloor(int floor){
        this.destinationFloor = floor;
    }

    public boolean isAtMailRoom(){
        return getFloor() == 0;
    }
}
