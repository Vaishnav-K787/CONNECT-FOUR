package ConnectFour.src;

import java.net.*;
import java.io.*;


public class CC4ServerMain {
    public static void main(String[] args) {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8014);
            Socket rCon = serverSocket.accept();
            ObjectOutputStream ros = new ObjectOutputStream(rCon.getOutputStream());
            ObjectInputStream ris = new ObjectInputStream(rCon.getInputStream());

            ros.writeObject(new CommandFromServer(CommandFromServer.CONNECTED_AS_RED,null));
            System.out.println("Red has connected");

            ServersListener sl = new ServersListener(ris,ros,'R');
            Thread t = new Thread(sl);
            t.start();

            Socket yCon = serverSocket.accept();
            ObjectOutputStream yos = new ObjectOutputStream(yCon.getOutputStream());
            ObjectInputStream yis = new ObjectInputStream(yCon.getInputStream());

            yos.writeObject(new CommandFromServer(CommandFromServer.CONNECTED_AS_YELLOW,null));
            System.out.println("Yellow has connected");

            sl = new ServersListener(yis,yos,'Y');
            t = new Thread(sl);
            t.start();

            ros.writeObject(new CommandFromServer(CommandFromServer.RED_TURN,null));
            yos.writeObject(new CommandFromServer(CommandFromServer.YELLOW_TURN,null));


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
