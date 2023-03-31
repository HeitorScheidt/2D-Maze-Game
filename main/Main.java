package main;
import javax.swing.JFrame;

public class Main{
    /*
     * Criancando uma Janela com Jframe
     */
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //faz com que quando clicamos no x a aba feche
        window.setResizable(false); //não deixa com que mudemos o tamanho da tela
        window.setTitle("2D Adventure");

        GamePanel gamePanel =  new GamePanel();
        window.add(gamePanel); //adicionamos gamepanel na tela

        window.pack(); // dimenciona o quadro para e todo e conteudo estaja em cima de deus tamanhos perfeitos

        window.setLocationRelativeTo(null); //a nova janela abrirá no centro da tela
        window.setVisible(true); //para podermos ver a janela

        gamePanel.setupGmame();
        gamePanel.startGameThread();
    }

}
