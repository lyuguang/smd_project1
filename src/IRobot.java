import java.util.List;

/**
 * @author Guangxing Lyu and Yujian Wang
 */
public interface IRobot {
    void tick();
    void transfer(IRobot robot);
    void add(Item item);
    void sort();
    void reverseSort();
    int getRemainingCapacity();
    void setRemainingCapacity(int capacity);
    int getFloor();
    int getRoom();
    void place(int floor, int room);
    void move(Building.Direction direction);
    String getId();
    int numItems();
    boolean isEmpty();
    List<Item> getItems();
}
