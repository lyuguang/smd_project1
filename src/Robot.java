import java.util.*;

public class Robot implements IRobot{
    private static int count = 1;
    final private String id;
    protected int floor;
    protected int room;
    final private MailRoom mailroom;
    final protected List<Item> items = new ArrayList<>();

    private int load;
    protected int remainingCapacity;

    @Override
    public String toString() {
        return "Id: " + id + " Floor: " + floor + ", Room: " + room + ", #items: " + numItems() + ", Load: " + calLoad() ;
    }

    Robot(MailRoom mailroom, int capacity) {
        this.id = "R" + count++;
        this.mailroom = mailroom;
        this.remainingCapacity = capacity;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    @Override
    public int getFloor() { return floor; }
    @Override
    public int getRoom() { return room; }

    int calLoad() {
        load = 0;
        for (Item item : items) {
            load+= item.myWeight();
        }
        return load;
    }

    @Override
    public boolean isEmpty() { return items.isEmpty(); }

    @Override
    public void place(int floor, int room) {
        Building building = Building.getBuilding();
        building.place(floor, room, id);
        this.floor = floor;
        this.room = room;
    }

    @Override
    public void move(Building.Direction direction) {
        Building building = Building.getBuilding();
        int dfloor, droom;
        switch (direction) {
            case UP    -> {dfloor = floor+1; droom = room;}
            case DOWN  -> {dfloor = floor-1; droom = room;}
            case LEFT  -> {dfloor = floor;   droom = room-1;}
            case RIGHT -> {dfloor = floor;   droom = room+1;}
            default -> throw new IllegalArgumentException("Unexpected value: " + direction);
        }
        if (!building.isOccupied(dfloor, droom)) { // If destination is occupied, do nothing
            building.move(floor, room, direction, id);
            floor = dfloor; room = droom;
            if (floor == 0) {
                System.out.printf("About to return: " + this + "\n");
                mailroom.robotReturn(this);
            }
        }
    }

    @Override
    public void transfer(IRobot robot) {  // Transfers every item assuming receiving robot has capacity
        ListIterator<Item> iter = robot.getItems().listIterator();
        while(iter.hasNext()) {
            Item item = iter.next();
            this.add(item);
            this.remainingCapacity-= item.myWeight();
            iter.remove();
            robot.setRemainingCapacity((robot.getRemainingCapacity()+item.myWeight()));
        }
    }

    @Override
    public void tick() {
            Building building = Building.getBuilding();
            if (items.isEmpty()) {
                // Return to MailRoom
                if (room == building.NUMROOMS + 1) {
                    move(Building.Direction.DOWN);
                } else {
                    move(Building.Direction.RIGHT); // move towards right end column
                }
            }
            else {
                if (floor == items.getFirst().myFloor()) {
                    if (room == items.getFirst().myRoom()) { //then deliver all relevant items to that room
                        do {
                            Item firstItem = items.getFirst();
                            remainingCapacity += firstItem.myWeight();
                            Simulation.deliver(items.removeFirst());


                        } while (!items.isEmpty() && room == items.getFirst().myRoom());
                    }
                    else {
                        move(Building.Direction.RIGHT); // move towards next delivery
                    }
                }
                else {
                    move(Building.Direction.UP); // move towards floor
                }
            }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int numItems () {
        return items.size();
    }

    @Override
    public void add(Item item) {
        items.add(item);
    }

    @Override
    public void sort() {
        Collections.sort(items);
    }

    @Override
    public void reverseSort() {Collections.sort(items, Comparator.reverseOrder());}

    @Override
    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    @Override
    public void setRemainingCapacity(int remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

}
