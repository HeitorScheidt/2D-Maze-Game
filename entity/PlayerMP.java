package entity;

import java.net.InetAddress;

import main.GamePanel;
import main.KeyHandler;

public class PlayerMP extends Player{

    public InetAddress ipAddress;

    public int port;

    public PlayerMP(GamePanel gp, KeyHandler keyH, String username, InetAddress ipAddress, int port) {
        super(gp, keyH);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    //@Override
    /*public void update(){
        super.update();
    }*/

}