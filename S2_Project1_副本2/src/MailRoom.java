import java.util.*;

import static java.lang.String.format;

public class MailRoom {
    public enum Mode {CYCLING, FLOORING}
    List<Letter>[] waitingForDelivery;
    private final int numRobots;


    Queue<Robot> idleRobots;
    List<Robot> activeRobots;
    List<Robot> deactivatingRobots; // Don't treat a robot as both active and idle by swapping directly
    List<Robot> activeColumnRobots;

    private Mode mode;
    private int numRooms;
    private boolean isstar = false;

    public boolean someItems() {
        for (int i = 0; i < Building.getBuilding().NUMFLOORS; i++) {
            if (!waitingForDelivery[i].isEmpty()) {
                    return true;
            }
        }
        return false;
    }

    private int floorWithEarliestItem() {
        int floor = -1;
        int earliest = Simulation.now() + 1;
        for (int i = 0; i < Building.getBuilding().NUMFLOORS; i++) {
            if (!waitingForDelivery[i].isEmpty()) {
                int arrival = waitingForDelivery[i].getFirst().myArrival();
                if (earliest > arrival) {
                    floor = i;
                    earliest = arrival;
                }
            }
        }
        return floor;
    }

    MailRoom(int numFloors, int numRobots, Mode mode,int robotCapacity, int numRooms) {
        this.mode = mode;
        this.numRooms = numRooms;
        waitingForDelivery = new List[numFloors];
        for (int i = 0; i < numFloors; i++) {
            waitingForDelivery[i] = new LinkedList<>();
        }
        this.numRobots = numRobots;

        idleRobots = new LinkedList<>();
        activeColumnRobots = new ArrayList<>();
        // cycling mode
        if(mode == Mode.CYCLING){
            for (int i = 0; i < numRobots; i++)
                idleRobots.add(new Robot(MailRoom.this,robotCapacity));
        }else if(mode == Mode.FLOORING){
            idleRobots.add(new ColumnRobot(MailRoom.this,robotCapacity));
            idleRobots.add(new ColumnRobot(MailRoom.this,robotCapacity));
            Building building = Building.getBuilding();
            for(int i= 0; i < building.NUMFLOORS;i++){
                activeRobots.add(new FloorRobot(MailRoom.this,robotCapacity));
            }
        }
        activeRobots = new ArrayList<>();
        deactivatingRobots = new ArrayList<>();
    }

    void arrive(List<Letter> items) {
        for (Letter item : items) {
            waitingForDelivery[item.myFloor()-1].add(item);
            System.out.printf("Item: Time = %d Floor = %d Room = %d Weight = %d\n",
                    item.myArrival(), item.myFloor(), item.myRoom(), 0);
        }
    }

    public int checkearly(Robot lefbot, Robot rightbot){
        List<Letter> lettersleft = lefbot.letters;
        List<Letter> lettersright = rightbot.letters;
        Collections.sort(lettersleft,Comparator.comparingInt(Letter::myArrival));
        Collections.sort(lettersright,Comparator.comparingInt(Letter::myArrival));
        int eariestleft = lettersleft.get(0).myArrival();
        int eariestright = lettersright.get(0).myArrival();
        if(eariestleft<eariestright){
            return 0;
        }else if(eariestleft>eariestright){
            return 1;
        }else if(eariestleft==eariestright){
            return 0;
        }
        return 2;
    }
    // bob is a floor robot
    public int iswaiting(Robot bob){
        int left = 0;
        int right= 0;
        Robot leftbot = null;
        Robot rightbot = null;
        for(Robot robot: activeRobots){
            if(robot.getFloor() == bob.getFloor()&& !robot.letters.isEmpty() && robot.letters.getFirst().myFloor()==bob.getFloor()){
                if(robot.getRoom() == 0){
                    left=1;
                    leftbot=robot;
                }else if(robot.getRoom() == (numRooms+1)){
                    right = 1;
                    rightbot = robot;
                }
            }
        }
        if (left == 1 && right == 0){
            return 0;
        }else if(left == 0 && right == 1){
            return 1;
        }else  if (left == 1 && right == 1){
            if(checkearly(leftbot,rightbot)==0){
                return 0;
            }
        }else {
            return 1;
        }
        return 2;
    }

    public void tick() { // Simulation time unit
        if(this.mode == Mode.CYCLING){
            for (Robot activeRobot : activeRobots) {
                System.out.printf("About to tick: " + activeRobot.toString() + "\n"); activeRobot.tick();
            }
            robotDispatch();  // dispatch a robot if conditions are met
            // These are returning robots who shouldn't be dispatched in the previous step
            ListIterator<Robot> iter = deactivatingRobots.listIterator();
            while (iter.hasNext()) {  // In timestamp order
                Robot robot = iter.next();
                iter.remove();
                activeRobots.remove(robot);
                idleRobots.add(robot);
            }
        }
        else if(this.mode == Mode.FLOORING){
            // floorRobot behavior
            for(Robot Robot: activeRobots){
                System.out.printf("About to tick: " + Robot.toString() + "\n");
                if(Robot.letters.isEmpty()) {
                    // column Robot is just beside the floor Robot
                    if (iswaiting(Robot) == 0 && Robot.getRoom() == 1) {
                        Robot bot = null;
                        for (Robot robot : activeColumnRobots) {
                            if (robot.getId().equals("R1")) {
                                bot = robot;
                                break;
                            }
                        }
                        if (bot != null) {
                            Robot.transfer(bot);
                        }
                        ((FloorRobot) Robot).setmovedirection(0);
                    } // column Robot is just not beside the floor Robot
                    else if (iswaiting(Robot) == 0 && Robot.getRoom() != 1) {
                        Robot.move(Building.Direction.LEFT);
                    }
                    // column robot in the right
                    else if (iswaiting(Robot) == 1 && Robot.getRoom() == numRooms) {
                        Robot bot = null;
                        for (Robot robot : activeColumnRobots) {
                            if (robot.getId().equals("R2")) {
                                bot = robot;
                                break;
                            }
                        }
                        if (bot != null) {
                            Robot.transfer(bot);
                        }
                        ((FloorRobot) Robot).setmovedirection(1);
                    }
                    // column robot in thr right but they are not close
                    else if (iswaiting(Robot) == 0 && Robot.getRoom() != 1) {
                        Robot.move(Building.Direction.RIGHT);
                    }
                }
                else{
                    // there is letter in bot and he goes to
                    if(Robot.getFloor()==Robot.letters.get(0).myFloor() && Robot.getRoom() == Robot.letters.get(0).myRoom()){
                       do{
                           Letter num1letter = Robot.letters.get(0);
                           Robot.ReamingCapacity += num1letter.getWeight();
                           Simulation.deliver(Robot.letters.removeFirst());
                       }while(!Robot.letters.isEmpty()&& Robot.getRoom() == Robot.letters.getFirst().myRoom());
                    }
                    else if (((FloorRobot) Robot).getMovedirection()==0){
                        Robot.move(Building.Direction.RIGHT);
                    }else if(((FloorRobot) Robot).getMovedirection()==1){
                        Robot.move(Building.Direction.LEFT);
                    }
                }
            }
            dispatchFlooringMode();
        }

    }
    void dispatchFlooringMode(){
        if (isstar == false) {
            isstar = true;
            int floorNum = 1;
            for (Robot robot : activeRobots) {
                robot.place(floorNum, 1);
                floorNum += 1;
            }
        }
    }

    void robotDispatch() { // Can dispatch at most one robot; it needs to move out of the way for the next
        System.out.println("Dispatch at time = " + Simulation.now());
        // Need an idle robot and space to dispatch (could be a traffic jam)
        if (!idleRobots.isEmpty() && !Building.getBuilding().isOccupied(0,0)) {
            int fwei = floorWithEarliestItem();
            if (fwei >= 0) {  // Need an item or items to deliver, starting with earliest
                Robot robot = idleRobots.remove();
                loadRobot(fwei, robot);
                // Room order for left to right delivery
                robot.sort();
                activeRobots.add(robot);
                System.out.println("Dispatch @ " + Simulation.now() +
                        " of Robot " + robot.getId() + " with " + robot.numItems() + " item(s)");
                robot.place(0, 0);
            }
        }
    }

    void robotReturn(Robot robot) {
        Building building = Building.getBuilding();
        int floor = robot.getFloor();
        int room = robot.getRoom();
        assert floor == 0 && room == building.NUMROOMS+1: format("robot returning from wrong place - floor=%d, room ==%d", floor, room);
        assert robot.isEmpty() : "robot has returned still carrying at least one item";
        building.remove(floor, room);
        deactivatingRobots.add(robot);
    }

    void loadRobot(int floor, Robot robot) {
        Collections.sort(waitingForDelivery[floor], Comparator.comparingInt(Letter::myArrival));
        ListIterator<Letter> iter = waitingForDelivery[floor].listIterator();
        int reaminCapacity = robot.getReamingCapacity();
        while (iter.hasNext()) {  // In timestamp order
            Letter letter = iter.next();
            if(letter.getWeight()==0){
                robot.add(letter);
                iter.remove();
            }
            else if (letter.getWeight()>0 && letter.getWeight() < reaminCapacity){
                robot.add(letter);
                reaminCapacity -= letter.getWeight();
                iter.remove();
                robot.setReamingCapacity(reaminCapacity);
            }
        }
    }

}
