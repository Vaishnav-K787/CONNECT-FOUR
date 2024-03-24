package ConnectFour.src;
import java.io.Serializable;

public class CommandFromServer implements Serializable
{
    // The command being sent to the client
    private int command;
    // Text data for the command
    private String data ="";

    // Command list
    public static final int CONNECTED_AS_RED=0;
    public static final int CONNECTED_AS_YELLOW=1;
    public static final int RED_TURN=2;
    public static final int YELLOW_TURN=3;
    public static final int MOVE=4;
    public static final int RED_WINS=5;
    public static final int YELLOW_WINS=6;
    public static final int TIE=7;
    public static final int RESET=8;
    public static final int CLOSE = 9;
    public static final int CNFRST = 10;


    public CommandFromServer(int command, String data) {
        this.command = command;
        this.data = data;
    }

    public int getCommand() {
        return command;
    }

    public String getData() {
        return data;
    }
}