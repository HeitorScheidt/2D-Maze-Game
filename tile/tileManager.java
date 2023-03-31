package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

import java.awt.Graphics2D;

public class tileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public tileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int [gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/res/maps/map01.txt");
    }

    public void getTileImage(){

        try {
            //carregamos as imagens

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/grass.png"));


            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/earth.png"));
            //tile[2].collision = true;

            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){

        try {
            //"importamos o arquivo de texto"
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); 

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line = br.readLine();//le a linha do texto

                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");//divite a string em sub strings a cada espaço
                    int num = Integer.parseInt(numbers[col]);//convertemos para inteiro, pois era string

                    mapTileNum[col][row] = num; //guardamos o numero no mapTileNum
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        //g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
        //g2.drawImage(tile[1].image, 48, 0, gp.tileSize, gp.tileSize, null);
        //g2.drawImage(tile[2].image, 96, 0, gp.tileSize, gp.tileSize, null);

        int worldCol = 0;
        int worldRow = 0;
        //int x = 0;
        //int y = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            worldCol++;
            //x += gp.tileSize;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                //x = 0;
                worldRow++;
                //y+= gp.tileSize;
            }
        }
    }
}
