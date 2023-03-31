package main;

import java.net.URL;

//import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

    //usamos para abrir arquivos de audio.
    Clip clip;

    //guardamos o file path dos arquivos de som
    URL soundURL[] = new URL[30];

    public Music(){
        soundURL[0] = getClass().getResource("/res/sound/Dungeon.wav");
        soundURL[1] = getClass().getResource("/res/sound/dooropen.wav");
        soundURL[2] = getClass().getResource("/res/sound/blocked.wav");
        soundURL[3] = getClass().getResource("/res/sound/coin.wav");
    }

    public void setFile(int i){

        try {
            
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
    
}
