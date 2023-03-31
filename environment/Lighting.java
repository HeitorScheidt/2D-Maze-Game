package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.Shape;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Lighting {
    
    GamePanel gp;
    BufferedImage darknessFilter;

    public Lighting(GamePanel gp, int circleSize){

        //criando o bufferedImage
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();
        
        //cria um retangulo do tamanho da tela
        Area screenArea = new Area(new Rectangle2D.Double(0,0,gp.screenWidth, gp.screenHight));
        
        //pega a coordenado do centro do circulo
        int centerX = gp.player.screenX + (gp.tileSize / 2);
        int centerY = gp.player.screenY + (gp.tileSize / 2);

        //pega as coordenadas do canto superior do circulo
        double x = centerX - (circleSize / 2);
        double y = centerY - (circleSize / 2);

        //cria um circulo de luz
        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);

        //cria a area do circulo de luz
        Area lightArea = new Area(circleShape);

        //subtrai a area do circulo da area do retangulo
        screenArea.subtract(lightArea);

        //cria um degrade no interior do circulo
        Color color[] = new Color[5]; //5 é o nivel de detalhe do degrade
        float fraction[] = new float[5]; //é a distancia entre essas cores

        color[0] = new Color(0,0,0,0f);
        color[1] = new Color(0,0,0,0.25f);
        color[2] = new Color(0,0,0,0.5f);
        color[3] = new Color(0,0,0,0.75f);
        color[4] = new Color(0,0,0,1f);

        fraction[0] = 0f;
        fraction[1] = 0.25f;
        fraction[2] = 0.5f;
        fraction[3] = 0.75f;
        fraction[4] = 1f;

        //criando as ferramentas de pintura
        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (circleSize / 2), fraction, color);

        //definindo como data de g2
        g2.setPaint(gPaint);

        //desenha a area do circulo
        g2.fill(lightArea);

        //define a cor do retangulo como preta
        //g2.setColor(new Color(0,0,0,1f));

        //desenha o retangulo sem o circulo
        g2.fill(screenArea);

        g2.dispose();
    }

    public void draw(Graphics2D g2){
        g2.drawImage(darknessFilter, 0, 0, null);
    }
}
