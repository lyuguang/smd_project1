import java.util.*;
import static java.lang.String.format;

public class MailRoom {
    public enum Mode {CYCLING, FLOORING}

    ItemQueue itemQueue;
    RobotDispatcher robotDispatcher;
    ArrivalManager arrivalManager;
    DeliveryMode deliveryMode;

    private int robotCapacity;
    private int numRooms;

    MailRoom(int numFloors, int numRobots, int robotCapacity, int numRooms, Mode mode) {
        this.robotCapacity = robotCapacity;
        this.numRooms = numRooms;

        itemQueue = new ItemQueue(numFloors);
        robotDispatcher = new RobotDispatcher();
        arrivalManager = new ArrivalManager();

        if (mode == Mode.CYCLING) {
            deliveryMode = new CyclingMode();
            for (int i = 0; i < numRobots; i++)
                robotDispatcher.addIdleRobot(new Robot(this, robotCapacity));
        } else if (mode == Mode.FLOORING) {
            deliveryMode = new FlooringMode();
            robotDispatcher.addIdleRobot(new ColumnRobot(this, robotCapacity, robotDispatcher.getActiveRobots(), numRooms));
            robotDispatcher.addIdleRobot(new ColumnRobot(this, robotCapacity, robotDispatcher.getActiveRobots(), numRooms));
            Building building = Building.getBuilding();
            for (int i = 0; i < building.NUMFLOORS; i++)
                robotDispatcher.addActiveRobot(new FlooringRobot(this, robotCapacity, robotDispatcher.getActiveColumnRobots(), numRooms, i+1, 1));
        }
    }

    public boolean someItems() {
        return itemQueue.hasItems();
    }

    public void addToArrivals(int arrivalTime, Item item) {
        arrivalManager.addToArrivals(arrivalTime, item);
    }

    public List<Item> getArrivingItems(int time) {
        return arrivalManager.getArrivingItems(time);
    }

    void arrive(List<Item> items) {
        for (Item item : items) {
            itemQueue.addItem(item);
            System.out.printf("Item: Time = %d Floor = %d Room = %d Weight = %d\n",
                    item.myArrival(), item.myFloor(), item.myRoom(), item.myWeight());
        }
    }

    public boolean hasArrivingItems(int time) {
        return arrivalManager.hasArrivingItems(time);
    }

    public int checkEarly(IRobot leftRobot, IRobot rightRobot) {
        List<Item> items1 = new ArrayList<>(leftRobot.getItems());
        Collections.sort(items1, Comparator.comparingInt(Item::myArrival));

        List<Item> items2 = new ArrayList<>(rightRobot.getItems());
        Collections.sort(items2, Comparator.comparingInt(Item::myArrival));

        if (items1.isEmpty() && items2.isEmpty()) {
            return -1;
        } else if (items1.isEmpty()) {
            return 1;
        } else if (items2.isEmpty()) {
            return 0;
        }

        int earlyLeft = items1.get(0).myArrival();
        int earlyRight = items2.get(0).myArrival();

        if (earlyLeft < earlyRight) {
            return 0;
        } else if (earlyLeft > earlyRight) {
            return 1;
        } else {
            return 0;
        }
    }

    public int checkWaiting2(IRobot ref) {
        int left = 0;
        int right = 0;
        IRobot leftRobot = null;
        IRobot rightRobot = null;
        for (IRobot robot : robotDispatcher.getActiveColumnRobots()) {
            if (robot.getFloor() == ref.getFloor() && !robot.isEmpty() && robot.getItems().get(0).myFloor() == ref.getFloor()) {
                if (robot.getRoom() == 0) {
                    left = 1;
                    leftRobot = robot;
                } else if (robot.getRoom() == (numRooms + 1)) {
                    right = 1;
                    rightRobot = robot;
                }
            }
        }
        if (left == 1 && right == 0) {
            return 0;
        } else if (left == 0 && right == 1) {
            return 1;
        } else if (left == 1 && right == 1) {
            return (checkEarly(leftRobot, rightRobot) == 0) ? 0 : 1;
        }

        return -1;
    }

    public void tick() {
        deliveryMode.tick(this);
    }

    void robotReturn(IRobot robot) {
        Building building = Building.getBuilding();
        int floor = robot.getFloor();
        int room = robot.getRoom();
        assert floor == 0 && room == building.NUMROOMS+1: format("robot returning from wrong place - floor=%d, room ==%d", floor, room);
        assert robot.isEmpty() : "robot has returned still carrying at least one item";
        building.remove(floor, room);
        robotDispatcher.deactivateRobot(robot);
    }

    void loadRobot(int floor, IRobot robot) {
        List<Item> floorItems = itemQueue.getItemsForFloor(floor);
        Collections.sort(floorItems, Comparator.comparingInt(Item::myArrival));

        ListIterator<Item> iter = floorItems.listIterator();
        int remainingCapacity = robot.getRemainingCapacity();
        while (iter.hasNext()) {
            Item item = iter.next();
            if (item.myWeight() == 0) {
                robot.add(item);
                iter.remove();
            }
            else if (item.myWeight() > 0 && item.myWeight() < remainingCapacity) {
                robot.add(item);
                remainingCapacity -= item.myWeight();
                iter.remove();
                robot.setRemainingCapacity(remainingCapacity);
            }
        }
    }

    public int getNumRooms() {
        return numRooms;
    }

    public RobotDispatcher getRobotDispatcher() {
        return robotDispatcher;
    }

    public ItemQueue getItemQueue() {
        return itemQueue;
    }
}