public class FloorRobot extends Robot{
    private int movedirection = 2;
    FloorRobot(MailRoom mailroom, int capacity ){
        super(mailroom,capacity);
    }
    public void setmovedirection(int movedirection){
        this.movedirection= movedirection;
    }
    public int getMovedirection(){
        return movedirection;
    }
}
