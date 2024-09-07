import java.util.Properties;
import java.util.Random;

/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class AutomailController {
    public static int time = 0;
    public final int endArrival;
    final public MailRoom mailroom;
    private static int timeout;

    private static int deliveredCount = 0;
    private static int deliveredTotalTime = 0;

    public AutomailController(Properties properties) {
        int seed = Integer.parseInt(properties.getProperty("seed"));
        Random random = new Random(seed);
        this.endArrival = Integer.parseInt(properties.getProperty("mail.endarrival"));
        int numLetters = Integer.parseInt(properties.getProperty("mail.letters"));
        int numParcels = Integer.parseInt(properties.getProperty("mail.parcels"));
        int maxWeight = Integer.parseInt(properties.getProperty("mail.parcelmaxweight"));
        int numFloors = Integer.parseInt(properties.getProperty("building.floors"));
        int numRooms = Integer.parseInt(properties.getProperty("building.roomsperfloor"));
        int numRobots = Integer.parseInt(properties.getProperty("robot.number"));
        int robotCapacity = Integer.parseInt(properties.getProperty("robot.capacity"));
        timeout = Integer.parseInt(properties.getProperty("timeout"));
        MailRoom.Mode mode = MailRoom.Mode.valueOf(properties.getProperty("mode"));

        Building.initialise(numFloors, numRooms);
        Building building = Building.getBuilding();
        mailroom = new MailRoom(building.NUMFLOORS, numRobots, robotCapacity, numRooms, mode);

        generateItems(random, numLetters, numParcels, maxWeight, building);
    }

    private void generateItems(Random random, int numLetters, int numParcels, int maxWeight, Building building) {
        for (int i = 0; i < numLetters; i++) {
            generateItem("letter", random, building, 0);
        }

        for (int i = 0; i < numParcels; i++) {
            generateItem("parcel", random, building, maxWeight);
        }
    }

    private void generateItem(String type, Random random, Building building, int maxWeight) {
        int arrivalTime = random.nextInt(endArrival) + 1;
        int floor = random.nextInt(building.NUMFLOORS) + 1;
        int room = random.nextInt(building.NUMROOMS) + 1;
        int weight = type.equals("parcel") ? random.nextInt(maxWeight) + 1 : 0;
        Item item = ItemFactory.createItem(type, floor, room, arrivalTime, weight);
        mailroom.addToArrivals(arrivalTime, item);
    }

    public void step() {
        if (mailroom.hasArrivingItems(time)) {
            mailroom.arrive(mailroom.getArrivingItems(time));
        }
        mailroom.tick();
    }

    public void deliver(Item mailItem) {
        System.out.println("Delivered: " + mailItem);
        deliveredCount++;
        deliveredTotalTime += time - mailItem.myArrival();
    }

    public void run() {
        while (time++ <= endArrival || mailroom.someItems()) {
            step();
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.printf("Finished: Items delivered = %d; Average time for delivery = %.2f%n",
                deliveredCount, (float) deliveredTotalTime / deliveredCount);
    }

    public static int now() {
        return time;
    }
}