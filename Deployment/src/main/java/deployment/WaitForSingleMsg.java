package deployment;

public class WaitForSingleMsg extends WaitForMsg {
    private String name;
    private int id;

    public WaitForSingleMsg(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public boolean isWaitfor(String name, int id) {
        return this.name.equals(name) && this.id == id;
    }
}