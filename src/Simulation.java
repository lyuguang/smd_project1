import java.util.*;

public class Simulation {
    private static AutomailController controller;

    public static void deliver(Item mailItem) {
        if (controller != null) {
            controller.deliver(mailItem);
        } else {
            System.err.println("Error: AutomailController not initialized");
        }
    }

    Simulation(Properties properties) {
        controller = new AutomailController(properties);
    }

    public static int now() { return AutomailController.now(); }

    void run() {
        controller.run();
    }

}
