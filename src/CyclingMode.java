/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class CyclingMode implements DeliveryMode {
    @Override
    public void tick(MailRoom mailRoom) {
        for (IRobot activeRobot : mailRoom.robotDispatcher.getActiveRobots()) {
            System.out.printf("About to tick: " + activeRobot.toString() + "\n");
            activeRobot.tick();
        }
        robotDispatch(mailRoom);
        mailRoom.robotDispatcher.processDeactivatingRobots();
    }

    @Override
    public void robotDispatch(MailRoom mailRoom) {
        System.out.println("Dispatch at time = " + Simulation.now());
        if (!mailRoom.robotDispatcher.getIdleRobots().isEmpty() && !Building.getBuilding().isOccupied(0,0)) {
            int fwei = mailRoom.itemQueue.floorWithEarliestItem();
            if (fwei >= 0) {
                IRobot robot = mailRoom.robotDispatcher.getIdleRobot();
                mailRoom.loadRobot(fwei, robot);
                robot.sort();
                mailRoom.robotDispatcher.addActiveRobot(robot);
                System.out.println("Dispatch @ " + Simulation.now() +
                        " of Robot " + robot.getId() + " with " + robot.numItems() + " item(s)");
                robot.place(0, 0);
            }
        }
    }
}
