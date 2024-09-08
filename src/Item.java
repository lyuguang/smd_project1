public class Item implements Comparable<Item> {
    protected final int floor;
    protected final int room;

    protected final int arrival;
    protected int weight = 0;
    @Override public int compareTo(Item i) {
        int floorDiff = this.floor - i.floor;
        return (floorDiff == 0) ? this.room - i.room : floorDiff;
    }

    Item(int floor, int room, int arrival) {
        this.floor = floor;
        this.room = room;
        this.arrival = arrival;
    }

    public String toString() {
        return "Floor: " + floor + ", Room: " + room + ", Arrival: " + arrival + ", Weight: " + weight;
    }

    int myFloor() { return floor; }
    int myRoom() { return room; }
    int myArrival() { return arrival; }
    int myWeight() { return weight; }
}
