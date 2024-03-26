package ConnectFour.src;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CC4ClientMain
{
    public static void main(String[] args)
    {
        try {
            GameData gameData = new GameData();

            Socket socket = new Socket("127.0.0.1",8014);
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());

            CommandFromServer cfs = (CommandFromServer) is.readObject();
            CCC4Frame frame;

            if(cfs.getCommand() == CommandFromServer.CONNECTED_AS_RED) {
                frame = new CCC4Frame(gameData, os, 'R');
                frame.setText("Waiting for  Yellow (opponent) to join...");
            }
            else
            {
                frame = new CCC4Frame(gameData, os, 'Y');
            }

            ClientsListener cl = new ClientsListener(is,os,frame);
            Thread t = new Thread(cl);
            t.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
