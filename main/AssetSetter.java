package main;

import object.Key;
import object.Door;

public class AssetSetter {
    
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new Key();
        gp.obj[0].worldX = 6 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;

        gp.obj[1] = new Door();
        gp.obj[1].worldX = 30 * gp.tileSize;
        gp.obj[1].worldY = 1 * gp.tileSize;
    }
}
