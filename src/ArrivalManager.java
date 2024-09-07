import java.util.*;

/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class ArrivalManager {
    private static final Map<Integer, List<Item>> waitingToArrive = new HashMap<>();

    public void addToArrivals(int arrivalTime, Item item) {
        System.out.println(item.toString());
        waitingToArrive.computeIfAbsent(arrivalTime, k -> new LinkedList<>()).add(item);
    }

    public List<Item> getArrivingItems(int time) {
        return waitingToArrive.getOrDefault(time, new LinkedList<>());
    }

    public boolean hasArrivingItems(int time) {
        return waitingToArrive.containsKey(time);
    }
}