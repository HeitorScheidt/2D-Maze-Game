package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import entity.PlayerMP;
import main.GamePanel;
import main.KeyHandler;
import packets.Packet;
import packets.Packet00Login;
import packets.Packet.PackTypes;


public class GameServer extends Thread{

    private DatagramSocket socket;
    private GamePanel gp;
    private KeyHandler keyH;
    private List<PlayerMP> connectedPlayers =  new ArrayList<PlayerMP>();

    public GameServer(GamePanel gp, KeyHandler keyH){
        this.keyH = keyH;
        this.gp = gp;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }

            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            /*String message = new String(packet.getData());

            System.out.println("Cliente > ["+ packet.getAddress().getHostAddress() + " : " + packet.getPort()  +"]" + message);
            if(message.trim().equalsIgnoreCase("ping")){
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }*/
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PackTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch(type){
            default:
            case INVALID:
            break;

            case LOGIN:
                Packet00Login packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername()
                    + " se conectou...");
                //para localhost
                //Vamo verificar para ver se é necessario um segundo construtor de Player por conta do username
                PlayerMP player = new PlayerMP(gp, keyH, ((Packet00Login) packet).getUsername(), address, port);
                //this.addConnection(player, (Packet00Login) packet);
                if(player != null)
                    this.connectedPlayers.add(player);
                //adiciona outro personagem;
                //game.lavel.addEntity(Player);
                gp.player = player;
            break;

            case DISCONNECT:
            break;
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for(PlayerMP p : connectedPlayers){
            sendData(data, p.ipAddress, p.port);
        }
    }

    /*  void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if (p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                // relay to the current connected player that there is a new
                // player
                sendData(packet.getData(), p.ipAddress, p.port);

                // relay to the new player that the currently connect player
                // exists
                packet = new Packet00Login(p.getUsername());
                sendData(packet.getData(), player.ipAddress, player.port);
            }
        }
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }*/
}
