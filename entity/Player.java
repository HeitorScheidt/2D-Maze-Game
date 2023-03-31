package entity;

import main.KeyHandler;
import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.Rectangle;
//import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

//import entity.PlayerMP;
//import packets.Packet00Login;

//import java.awt.Color;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    String username;

    public final int screenX;
    public final int screenY;
    int hasKey = 0;

    private List<Entity> entities = new ArrayList<Entity>();

    public Player (GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        //this.username = username;
        setDefaultValues();
        getPlayerImage();

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHight/2 - (gp.tileSize/2);

        /*definindo a area de colisão do nosso player */
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
    }
    
    public synchronized List<Entity> getEntities() {
        return this.entities;
    }

    public synchronized void addEntity(Entity entity) {
        this.getEntities().add(entity);
    }

    /*private int getPlayerMPIndex(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }*/

    //posição inicial do player
    public void setDefaultValues(){

        //if(index == 0){
        worldX = gp.tileSize * 1;
        worldY = gp.tileSize * 30;
        speed = 4;
        //}
        
        /*if(index == 1){
            worldX = gp.tileSize * 2;
            worldY = gp.tileSize * 30;
            speed = 4;
            }*/

        direction = "down";
    }

    public void update(){

        //verifica a direção
        if(keyH.upPressed == true ||keyH.downPressed == true||keyH.leftPressed == true||keyH.rightPressed == true){
            
            //int index = getPlayerMPIndex(username);
            //PlayerMP player = (PlayerMP) this.getEntities().get(index);

            if(keyH.upPressed == true){
                direction = "up";
            }else if(keyH.downPressed == true){
                direction = "down";
            }else if(keyH.leftPressed == true){
                direction = "left";
            }else if(keyH.rightPressed == true){
                direction = "right";
            }

            //Verifica Colisão de Tile
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //verifica colisão com objeto
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //Se collision é false, play pode andar
            if(collisionOn == false){
                
                switch(direction){

                    case "up":
                        worldY -= speed;
                    break;
                    case "down":
                        worldY += speed;
                    break;
                    case "left":
                        worldX -= speed;
                    break;
                    case "right":
                        worldX += speed;
                    break;
                }
            }

            spriteCounter++;
            
            if(spriteCounter > 10){
                
                if(spriteNum == 1){
                    spriteNum = 2;
                }else if(spriteNum == 2){
                    spriteNum = 1;
                }

                spriteCounter = 0;
            }
        }
    }

    public void getPlayerImage(){
        try{

            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/boy_right_2.png"));

        }catch(IOException e){

            e.printStackTrace();
        }
    }

    public void pickUpObject(int index){
        //so vai ser diferente se tiver relado em algum objeto
        if(index != 999){
            //deleta o objeto q tocarmos
            //gp.obj[index] = null;

            String objectName = gp.obj[index].name;

            switch(objectName){
                case "key":
                gp.soundEfect(3);
                    hasKey++;
                    //deleta o objeto q tocarmos
                    gp.obj[index] = null;
                    System.out.println("Voce pegou " + hasKey + " chaves!");
                break;

                case "door":
                    if(hasKey > 0){
                        gp.soundEfect(1);
                        gp.obj[index] = null;
                        hasKey--;
                        System.out.println("PARABENS VOCÊ ECONTROU A SAIDA E VENCEU O JOGO!!");
                        System.exit(0);
                    }
                    gp.soundEfect(2);
                break;
            }
        }
    }

    public void draw(Graphics2D g2){

//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch(direction){

            case "up":
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                }
            break;

            case "down":
                if(spriteNum == 1) {image = down1;}
                if(spriteNum == 2) {image = down2;}
            break;

            case "left":
                if(spriteNum == 1) {image = left1;}
                if(spriteNum == 2) {image = left2;}
            break;

            case "right":
                if(spriteNum == 1) {image = right1;}
                if(spriteNum == 2) {image = right2;}
            break;
        }

        //desenha uma imagen na tela
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public String getUsername() {
        return this.username;
    }
}
