package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.Player;
//import entity.PlayerMP;
import environment.EnvironmentManager;
import net.GameServer;
import object.SuperObject;
import packets.Packet00Login;
import tile.tileManager;

public class GamePanel extends JPanel  implements Runnable{
    //SCREEN SETTINGS
    final int originalTilesSize = 16; //16x16 Tile (é o tamanho de cada bloco nosso)
    final int scale = 3;

    /*Fazemos isso pois 16x é muito pequeno, assim fica com melhor vizualização */
    public final int tileSize = originalTilesSize * scale; //48x48 tile
    /*
     * Define a tela tendo um total de 16x12 tile
     */
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 px
    public final int screenHight = tileSize * maxScreenRow; //576 px

    //World Settings
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    //public final int worldWidth = tileSize * maxWorldCol;   podem ser deletados****
    //public final int worldHeight = tileSize * maxWorldRow;  podem ser deletados****

    //net
    private GameClient socketClient;
    private GameServer socketServer;

    //FPS
    int FPS = 60;

    //SYSTEM
    tileManager tileM = new tileManager(this);
    KeyHandler keyH = new KeyHandler(); //instaciamos keyHandler
    Music music = new Music();
    public collisionChecker cChecker = new collisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    Thread gameThread;
    EnvironmentManager eManager = new EnvironmentManager(this);

    //ENTITY E OBJECT
    public Player player = new Player(this, keyH);
    //nesse dei baixo temos q passar um int
    //public Player player = new Player(this, keyH, JOptionPane.showConfirmDialog(this, "Voce quer rodar o servidor? "));
    public SuperObject obj[] = new SuperObject[10]; //10 é o numero de 0bjetos que podemos ter no jogo.


    //define a posição padrão do jogador
    //int playerX = 100;
    //int playerY = 100;
    //int playerSpeed = 4; //se refere me pixel entao a velocidade é de 4 pixels



    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //melhora a performace da renderização do jogo, mas como?
        this.addKeyListener(keyH);
        this.setFocusable(true); //permite que p GamePanel seja "priorisada" em receber o Key input
    }

    public void setupGmame(){
        aSetter.setObject(); //criadno esse metodo podemos adicionar outros setups no futuro.
        eManager.setup();
        
        //toca a musica que está na posição 0 do array
        playMusic(0);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();

        //if(JOptionPane.showConfirmDialog(this, "Voce quer rodar o servidor? ") == 0){
            socketServer = new GameServer(this, keyH);
            socketServer.start();
        //}
        socketClient = new GameClient(this, "localhost");
        socketClient.start();

        socketClient.sendData("ping".getBytes());
    }

    /*public void inito(){
        socketClient.sendData("ping".getBytes());
    }
    /*aqui vamos criar nosso game loop que vai ser o core do nosso jogo */
    @Override
    public void run() {
                    
        double drawIntervale = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime(); //retorna o atual valor da jvm em alta resolução de tempo
        long currentTime;
        //player2  = new PlayerMP(this, keyH, JOptionPane.showInputDialog(this, "Escreva um Username"), null, -1);
        //socketClient.sendData("ping".getBytes());
        //player2.addEntity(player);
        Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog(this, "Escreva um Username"));
        //Packet00Login loginPacket = new Packet00Login(player2.getUsername());
        //if (socketServer != null) {
            //socketServer.addConnection((PlayerMP) player2, loginPacket);
        loginPacket.writeData(socketClient);
        //inito();
        while(gameThread != null){

            currentTime = System.nanoTime(); //definimos o tempo agora
            delta += (currentTime - lastTime) / drawIntervale; //a subtralão pelo lasttime resulta no quanto de tempo se passou
            lastTime = currentTime;

            /*tudo isso que foi feito é necessario pois o computador atualiza muito rapido e ainda adicinamos +4 de speed
             * para funcionar devemos calcular o tempo que leva entre o update e o rapaint e é isso que fizemos
             */
            if(delta >= 1){

                //1- UPDATE: atualia a informação como a posição do personagem
                update();

                //2- DROW: desenha a tela com a informação atualizada
                repaint(); //é o metodo de chamar o método paintComponent 

                delta--;
            }
        }
    }

    /*atualiza as coordenadas do player */
    public void update(){

        player.update();
        //socketClient.sendData("ping".getBytes());
    }

    /*public void drawToTempScreen(){
        eManager.draw(g2);
    }*/

    /*paintComponent é um metodo standard para desenhar coisas no JPanel.*/
    public void paintComponent(Graphics g){  //O  Graphics é uma classe que possui muitas funções para desenhar objetos na tela.
                                            
        super.paintComponent(g);

         //a classe Graphics2D herda de Graphics e prove controles mais sofisticados sobre a geometria, transformação de coordenadas, ferramenta de cores e layouts de texto
        Graphics2D g2 = (Graphics2D)g; 

        //TILE
        //aqui temos que imprimir primeiro o tile e depois o play, caso contrario o tile vai esconder o player
        tileM.draw(g2);
        //OBJECT
        for(int i = 0; i<obj.length; i++){
            //verifica e o slot do array obj esta vazio e evita erro de NullPointer
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }
        //PLAYER
        player.draw(g2);

        eManager.draw(g2);

        g2.dispose();   //descarta o conteudo de Graphics e libera qualquer recurso do sistema que estiver usando
        
        //socketClient.sendData("ping".getBytes());
        
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void soundEfect(int i){
        music.setFile(i);
        music.play();
    }
}
