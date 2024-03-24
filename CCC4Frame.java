package ConnectFour.src;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;


public class CCC4Frame extends JFrame implements MouseListener, WindowListener {
    CCC4Panel p = null;

    private String text = "";
    private char player;
    private GameData gameData = null;
    ObjectOutputStream os;
    private boolean sRR = false;
    private boolean sCR = false;
    private char turn;
    boolean out = false;
    private long time  = -1;

    public CCC4Frame(GameData gameData, ObjectOutputStream os, char player) {
        // Setups Frame
        super("Connect Four");
        this.gameData = gameData;
        this.os = os;
        this.player = player;
        addMouseListener(this);
        //addWindowListener(e-> );
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        if (player == 'R') {
//            text = "Waiting for Yellow to Connect";
//            if(out == true) {
//                //closing();
//            }
//        }

        setSize(530, 590);
        setResizable(false);
        setAlwaysOnTop(true);
        setVisible(true);


        //p = new CCC4Panel(1000, 1000);
        // p.setBackground(Color.YELLOW);
        setResizable(false);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 530, 590);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, 530, 590);
        int a = 20;
        int b = 50;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (gameData.grid[i][j] == 'Y') {
                    g.setColor(Color.YELLOW);
                    g.drawOval(a, b, 60, 60);
                    g.fillOval(a, b, 60, 60);
                } else if (gameData.grid[i][j] == 'R') {
                    g.setColor(Color.RED);
                    g.drawOval(a, b, 60, 60);
                    g.fillOval(a, b, 60, 60);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawOval(a, b, 60, 60);
                    g.fillOval(a, b, 60, 60);
                }
                a+= 70;
            }
            b += 70;
            a = 20;
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 18));
        g.drawString(text,100,560);
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }
    public void q()
    {
        text = "Closing in 5";
    }

    public void setTurn(char turn) {
        boolean tempCheck = true;
        for(int i = 0;i < gameData.grid.length; ++i){
            for(int j = 0; j < gameData.grid[i].length; ++j){
                if(gameData.grid[i][j] != ' '){
                    tempCheck = false;
                    break;
                }
            }
            if(!tempCheck){
                break;
            }
        }
        if(tempCheck){
            if(player == 'R'){
                setText("Your Turn");
            }
            else{
                setText("Red Turn");
            }
        }
        else{
            if (turn == player)
                setText("Your Turn");
            else if (turn == 'A') {
                // Game ended, update text based on win/tie
                if (gameData.isWinner() == 'R')
                    setText("Red Win");
                else if (gameData.isWinner() == 'Y')
                    setText("Yellow Win");
                else if (gameData.tie())
                    setText("Tie Game");
            } else {
                // Game ongoing, update text based on current turn
                if (turn == 'R')
                    setText("Red Turn");
                else
                    setText("Yellow Turn");
            }
        }
        repaint();
    }



    public void makeMove(int c, int r, char letter) {
        gameData.grid[r][c] = letter;
        repaint();
    }

    public boolean gSR()
    {
        return sRR;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
//        System.out.println("MOUSECLICKED");
        if(SwingUtilities.isRightMouseButton(e))
        {
            if(gameData.isWinner() == 'R' || gameData.isWinner() == 'Y' || gameData.tie())
            {
//                if(sCR == false)
//                {
//                    try
//                    {
//                        sCR = true;
//                        os.writeObject(new CommandFromClient(CommandFromClient.CNF,""));
//                        reset();
//                    }
//                    catch(IOException o)
//                    {
//                        o.printStackTrace();
//                    }
//                }
                try {
                    os.writeObject(new CommandFromClient(CommandFromClient.RESTART, ""));
                } catch (Exception er) {
                    er.printStackTrace();
                }
                sCR = true;
                return;
            }
            sRR = true;
        }
    }

    public void mousePressed(MouseEvent e)
    {
        if (gameData.isWinner() == 'R' || gameData.isWinner() == 'Y' || gameData.tie()) {
//            if (sCR) {
//                try {
//                    os.writeObject(new CommandFromClient(CommandFromClient.CNF, ""));
//                } catch (Exception ea) {
//                    ea.printStackTrace();
//                }
//            }
//            sRR = true;
//            try {
//                os.writeObject(new CommandFromClient(CommandFromClient.RESTART, ""));
//            } catch (Exception eb) {
//                eb.printStackTrace();
//            }
//            mouseClicked(e);
            return;
        }
        if(e.getButton() != MouseEvent.BUTTON1){
            return;
        }
        int X_COORDINATE = e.getX();
        int Y_COORDINATE = e.getY();
        int a = ((X_COORDINATE - 20) / 70) * 70 + 20;
        int I = (a - 20) / 70;
        int J = gameData.grid.length - 1;
        while (J >= 0 && gameData.grid[J][I] != ' ') {
            --J;
        }
        if (J != -1) {
            try {
                os.writeObject(new CommandFromClient(CommandFromClient.MOVE, "" + I + J + player));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
//            makeMove(I, J, player);
        }
    }
    public void clear()
    {
        gameData.reset();
        //reset();
        time = -1;
        sRR = false;
        sCR = false;
    }


    public void reset() {
        if (player == turn) {
            if (gameData.isWinner() == 'R' || gameData.isWinner() == 'Y' || gameData.tie()) {
                if (sCR) {
                    try {
                        os.writeObject(new CommandFromClient(CommandFromClient.CNF, ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                sRR = true;
                try {
                    os.writeObject(new CommandFromClient(CommandFromClient.RESTART, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {

    }
    public void conf(String m, String title) {
//        System.out.println("RAN");
        sCR = true;
        if(gameData.WHICH_CALL){
            gameData.WHICH_CALL = false;
            if (gameData.isWinner() == 'R'|| gameData.isWinner() == 'Y') {
                text = "Right Click to accept the reset request.";
            }
            else {
                if (player == 'R') {
                    text = "Sent request to restart. Waiting for confirmation...";
                } else {
                    text = "Waiting for the other player to accept the request...";
                }
            }
            repaint();
        }
        else{
            gameData.WHICH_CALL = true;
            if(player == 'R'){
                setText("Your Turn");
            }
            else{
                setText("Red Turn");
            }
            clear();
            turn = 'R';
            repaint();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
//            System.out.println("here");
            os.writeObject(new CommandFromClient(CommandFromClient.CLOSE, "" + player));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        dispose();
    }

    public void closing() throws InterruptedException
    {
//        System.out.println("here");
        /*time = System.currentTimeMillis();
        long t2 = -1;
        while (true)
        {
            long tp = System.currentTimeMillis() - time;
            long sp = tp/1000;
            if(sp>t2)
            {
                text = "Client disconnected - closing in:"+(5-sp);
                t2 = sp;
                repaint();
            }
            if(sp>=6)
            {
                break;
            }
        }
        System.exit(0);*/
        for (int i = 5 ; i>=0; i--)
        {
            try {
                Thread.sleep(1000);
                if(player == 'R')
                {
                    text = "Yellow quit. Screen will close in: " + i;
                }
                else
                {
                    text = "Red quit. Screen will close in: " + i;
                }
                repaint();
            } catch(Exception f)
            {
                f.printStackTrace();
            }
        }
        dispose();
        System.exit(0);
    }


    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}