import java.util.List;

/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class FlooringMode implements DeliveryMode {
    private boolean initialized = false;

    @Override
    public void tick(MailRoom mailRoom) {
        RobotDispatcher dispatcher = mailRoom.getRobotDispatcher();
        int numRooms = mailRoom.getNumRooms();

        for (IRobot robot : dispatcher.getActiveRobots()) {
            System.out.printf("About to tick: " + robot.toString() + "\n");

            if (robot.isEmpty()) {
                if (mailRoom.checkWaiting2(robot) == 0 && robot.getRoom() == 1) {
                    IRobot sourceRobot = null;
                    for (IRobot r : dispatcher.getActiveColumnRobots()) {
                        if (r.getId().equals("R1")) {
                            sourceRobot = r;
                            break;
                        }
                    }
                    if (sourceRobot != null) {
                        robot.transfer(sourceRobot);
                    }
                    ((FlooringRobot) robot).setTransferPosition(0);
                }
                else if (mailRoom.checkWaiting2(robot) == 0 && robot.getRoom() != 1) {
                    robot.move(Building.Direction.LEFT);
                }
                else if (mailRoom.checkWaiting2(robot) == 1 && robot.getRoom() == numRooms) {
                    IRobot sourceRobot = null;
                    for (IRobot r : dispatcher.getActiveColumnRobots()) {
                        if (r.getId().equals("R2")) {
                            sourceRobot = r;
                            break;
                        }
                    }
                    if (sourceRobot != null) {
                        robot.transfer(sourceRobot);
                    }
                    ((FlooringRobot) robot).setTransferPosition(1);
                }
                else if (mailRoom.checkWaiting2(robot) == 1 && robot.getRoom() != numRooms) {
                    robot.move(Building.Direction.RIGHT);
                }
            } else {
                List<Item> items = robot.getItems();
                if (robot.getFloor() == items.get(0).myFloor() && robot.getRoom() == items.get(0).myRoom()) {
                    do {
                        Item firstItem = items.get(0);
                        robot.setRemainingCapacity(robot.getRemainingCapacity() + firstItem.myWeight());
                        Simulation.deliver(items.remove(0));
                    } while (!items.isEmpty() && robot.getRoom() == items.get(0).myRoom());
                }
                else if (((FlooringRobot) robot).getTransferPosition() == 0) {
                    robot.move(Building.Direction.RIGHT);
                }
                else if (((FlooringRobot) robot).getTransferPosition() == 1) {
                    robot.move(Building.Direction.LEFT);
                }
            }
        }

        robotDispatch(mailRoom);

        for (IRobot r : dispatcher.getActiveColumnRobots()) {
            List<Item> items = r.getItems();
            if (!items.isEmpty() && r.getFloor() != items.get(0).myFloor()) {
                r.move(Building.Direction.UP);
            } else if (!items.isEmpty() && r.getFloor() == items.get(0).myFloor()) {
                // nothing
            } else if (items.isEmpty() && r.getFloor() != 0) {
                r.move(Building.Direction.DOWN);
            }
        }

        int length = dispatcher.getIdleRobots().size();
        while (length > 0) {
            System.out.println("Dispatch at time = " + Simulation.now());
            int fwei = mailRoom.getItemQueue().floorWithEarliestItem();
            if (fwei >= 0) {
                IRobot robot = dispatcher.getIdleRobot();
                length -= 1;
                mailRoom.loadRobot(fwei, robot);
                if (robot.getId().equals("R1")) {
                    robot.sort();
                }
                else if (robot.getId().equals("R2")) {
                    robot.reverseSort();
                }

                dispatcher.addActiveColumnRobot(robot);
                System.out.println("Dispatch @ " + Simulation.now() +
                        " of Robot " + robot.getId() + " with " + robot.numItems() + " item(s)");
                if (robot.getId().equals("R1")) {
                    robot.place(0, 0);
                } else if (robot.getId().equals("R2")) {
                    robot.place(0, numRooms + 1);
                }
            }
        }

        dispatcher.processDeactivatingRobots();
    }

    @Override
    public void robotDispatch(MailRoom mailRoom) {
        if (!initialized) {
            initialized = true;
            int floorNum = 1;
            for (IRobot robot : mailRoom.getRobotDispatcher().getActiveRobots()) {
                robot.place(floorNum, 1);
                floorNum += 1;
            }
        }
    }
}