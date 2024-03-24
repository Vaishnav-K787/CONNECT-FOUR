package ConnectFour.src;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class ServersListener implements Runnable
{
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private char player;
    private static char turn = 'R';
    private static GameData gameData = new GameData();
    private static ArrayList<ObjectOutputStream> outs = new ArrayList<>();
    public ServersListener(ObjectInputStream is, ObjectOutputStream os, char player) {
        this.is = is;
        this.os = os;
        this.player = player;
        outs.add(os);
    }
    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromClient cfc = (CommandFromClient) is.readObject();
                System.out.println(""+cfc.getCommand() + turn + !(gameData.isWinner()=='R') + !(gameData.isWinner() == 'Y') + !(gameData.tie()));
                if (cfc.getCommand() == CommandFromClient.CLOSE) {
                    sendCommand(new CommandFromServer(CommandFromServer.CLOSE, null));
                }
                if (cfc.getCommand() == CommandFromClient.CNF) {
                    sendCommand(new CommandFromServer(CommandFromServer.CNFRST, null));
                }
                if (cfc.getCommand() == CommandFromClient.RESTART) {
                    gameData.reset();
                    reset();
                }


                if(cfc.getCommand()==CommandFromClient.MOVE && turn==player && gameData.isWinner() == ' ' && !gameData.tie())
                {
                    String data=cfc.getData();
                    int column = data.charAt(0) - '0';
                    boolean valid = false;
                    for(int i = 5; i >= 0; i--){
                        if(gameData.getGrid()[i][column] == ' '){
                            gameData.getGrid()[i][column] = player;
                            valid = true;
                            data += i;
                            break;
                        }
                    }
                    if(!valid)
                        continue;

                    sendCommand(new CommandFromServer(CommandFromServer.MOVE,data));
                    changeTurn();
                    checkGameOver();
                }
                else{
                    System.out.println(cfc.getCommand()==CommandFromClient.MOVE);
                    System.out.println(turn==player);
                    System.out.println(gameData.isWinner() == ' ');
                    System.out.println(!gameData.tie());
                    System.out.println();
                }
            }
        }
        catch(Exception e)
        {
            sendCommand(new CommandFromServer(CommandFromServer.CLOSE, null));
            e.printStackTrace();
        }
    }
    public void changeTurn()
    {
        if(turn=='R')
            turn = 'Y';
        else
            turn ='R';
        if (turn == 'R')
            sendCommand(new CommandFromServer(CommandFromServer.RED_TURN, null));
        else
            sendCommand(new CommandFromServer(CommandFromServer.YELLOW_TURN, null));
    }
    public void checkGameOver()
    {
        int c = -1;
        if(gameData.tie())
            c = CommandFromServer.TIE;
        else if(gameData.isWinner() == 'R')
            c = CommandFromServer.RED_WINS;
        else if(gameData.isWinner() == 'Y')
            c = CommandFromServer.YELLOW_WINS;
        // if the game ended, informs both clients of the game's end state
        if(c!=-1)
            sendCommand(new CommandFromServer(c, null));
    }
    public void reset() {
        turn = 'R';
        sendCommand(new CommandFromServer(CommandFromServer.RESET, null));
    }

    public void sendCommand(CommandFromServer cfs)
    {
        // Sends command to both players
        for (ObjectOutputStream o : outs) {
            try {
                o.writeObject(cfs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}