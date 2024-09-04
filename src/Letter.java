

public class Letter implements Comparable<Letter> {
    private final int floor;
    private final int room;
    private final int arrival;

    protected int weight;  // add the weight attribute to record the parcel weight

    @Override public int compareTo(Letter i) {
        int floorDiff = this.floor - i.floor;  // Don't really need this as only deliver to one floor at a time
        return (floorDiff == 0) ? this.room - i.room : floorDiff;
    }

    Letter(int floor, int room, int arrival, int weight) {
        this.floor = floor;
        this.room = room;
        this.arrival = arrival;
        this.weight = weight;  //initilization parameters
    }

    /**
     * change the return
     * @author Guangxing Lyu and Yujian Wang
     * @return
     */
    public String toString() {
        return "Floor: " + floor + ", Room: " + room + ", Arrival: " + arrival + ", Weight: " + weight;
    }

    int myFloor() { return floor; }
    int myRoom() { return room; }
    int myArrival() { return arrival; }

    /**
     * add method of getWeight
     * @author Guangxing Lyu and Yujian Wang
     * @return
     */
    int getWeight() { return weight; }
}
