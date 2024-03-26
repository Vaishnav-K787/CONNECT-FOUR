package ConnectFour.src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientsListener implements Runnable {
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private CCC4Frame frame = null;


    public ClientsListener(ObjectInputStream is,
                           ObjectOutputStream os,
                           CCC4Frame frame) {
        this.is = is;
        this.os = os;
        this.frame = frame;


    }


    @Override
    public void run() {
        try {
            while (true) {
                CommandFromServer cfs = (CommandFromServer) is.readObject();
                if (cfs.getCommand() == CommandFromServer.CLOSE) {
                    frame.closing();
                }
                if (cfs.getCommand() == CommandFromServer.RESET) {
                        frame.conf("Your op would like to rematch Press OK to accept a rematch, else exit out the pop up", "Rematch Request");
                }
                if (cfs.getCommand() == CommandFromServer.RED_TURN) {
                    frame.setTurn('R');
                } else if (cfs.getCommand() == CommandFromServer.YELLOW_TURN) {
                    frame.setTurn('Y');
                } else if (cfs.getCommand() == cfs.MOVE) {
                    String data = cfs.getData();
                    int column = data.charAt(0) - '0';
                    int row = data.charAt(1) - '0';
                    frame.makeMove(column, row, data.charAt(2));
                }
                else if (cfs.getCommand() == CommandFromServer.TIE) {
                    frame.setText("Tie game.");
                    frame.setTurn('A');
                } else if (cfs.getCommand() == CommandFromServer.RED_WINS) {
                    frame.setText("Red wins!");
                    frame.setTurn('A');
                } else if (cfs.getCommand() == CommandFromServer.YELLOW_WINS) {
                    frame.setText("Yellow wins!");
                    frame.setTurn('A');
                }
            }
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }
    }
}