package SpaceInvadersGame;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        Gameplay gameplay = new Gameplay(700, 600);
        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("SpaceInvaders Game");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameplay);
    }
}
