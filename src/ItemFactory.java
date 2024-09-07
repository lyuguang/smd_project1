
/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class ItemFactory {
    public static Item createItem(String type, int floor, int room, int arrivalTime, int weight) {
        return switch (type.toLowerCase()) {
            case "letter" -> new Letter(floor, room, arrivalTime);
            case "parcel" -> new Parcel(floor, room, arrivalTime, weight);
            default -> throw new IllegalArgumentException("Unknown item type: " + type);
        };
    }
}