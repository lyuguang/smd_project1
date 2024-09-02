/**
 * @author Guangxing Lyu and Yujian Wang
 * @date 2/9/2024 AM11:34
 */
public class FloorRobot extends Robot{
    private final int assignedFloor;

    public FloorRobot(MailRoom mailRoom, int capacity, int floor) {
        super(mailRoom, capacity);
        this.assignedFloor = floor;
        place(floor, 1);  // start at room 1 of the assigned floor
    }

    @Override
    void tick() {
        if (isEmpty()) {
            if (getRoom() > 1) {
                move(Building.Direction.LEFT);
            }
        } else {
            if (getRoom() == letters.getFirst().myRoom()) {
                while (!isEmpty() && getRoom() == letters.getFirst().myRoom()) {
                    Simulation.deliver(letters.removeFirst());
                }
            } else {
                move(Building.Direction.RIGHT);
            }
        }
    }

    public int getFloor(){
        return assignedFloor;
    }
}
