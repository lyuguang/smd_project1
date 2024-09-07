import java.util.*;

/**
 * @author Guangxing Lyu and Yujian Wang
 */
public class RobotDispatcher {
    private Queue<IRobot> idleRobots;
    private List<IRobot> activeRobots;
    private List<IRobot> activeRobotsColumn;
    private List<IRobot> deactivatingRobots;

    public RobotDispatcher() {
        idleRobots = new LinkedList<>();
        activeRobots = new ArrayList<>();
        activeRobotsColumn = new ArrayList<>();
        deactivatingRobots = new ArrayList<>();
    }

    public void addIdleRobot(IRobot robot) {
        idleRobots.add(robot);
    }

    public void addActiveRobot(IRobot robot) {
        activeRobots.add(robot);
    }

    public void addActiveColumnRobot(IRobot robot) {
        activeRobotsColumn.add(robot);
    }

    public IRobot getIdleRobot() {
        return idleRobots.poll();
    }

    public List<IRobot> getActiveRobots() {
        return activeRobots;
    }

    public List<IRobot> getActiveColumnRobots() {
        return activeRobotsColumn;
    }

    public void deactivateRobot(IRobot robot) {
        deactivatingRobots.add(robot);
    }

    public Queue<IRobot> getIdleRobots() {
        return idleRobots;
    }

    public void processDeactivatingRobots() {
        ListIterator<IRobot> iter = deactivatingRobots.listIterator();
        while (iter.hasNext()) {
            IRobot robot = iter.next();
            iter.remove();
            activeRobotsColumn.remove(robot);
            idleRobots.add(robot);
        }
    }
}