import java.util.*;

/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class ItemQueue {
    private List<Item>[] waitingForDelivery;

    public ItemQueue(int numFloors) {
        waitingForDelivery = new List[numFloors];
        for (int i = 0; i < numFloors; i++) {
            waitingForDelivery[i] = new LinkedList<>();
        }
    }

    public void addItem(Item item) {
        waitingForDelivery[item.myFloor() - 1].add(item);
    }

    public boolean hasItems() {
        for (List<Item> floorItems : waitingForDelivery) {
            if (!floorItems.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int floorWithEarliestItem() {
        int floor = -1;
        int earliest = Simulation.now() + 1;
        for (int i = 0; i < waitingForDelivery.length; i++) {
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

    public List<Item> getItemsForFloor(int floor) {
        return waitingForDelivery[floor];
    }
}
