/**
 * @author Guangxing Lyu and Yujian Wang
 */
public interface DeliveryMode {
    void tick(MailRoom mailRoom);
    void robotDispatch(MailRoom mailRoom);
}
