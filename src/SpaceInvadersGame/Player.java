package SpaceInvadersGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Spaceship {
    public static int posX = 0;
    private Image playerImg;

    public Player(int posX, int posY, int width, int height, int movementSpeed, int hp) {
        super(posX, posY, width, height, movementSpeed, hp);
        Player.posX = posX;
        try {
            playerImg = ImageIO.read(new File("C:\\Users\\oskar\\Documents\\PJATK dev\\SpaceInvadersGame\\src\\SpaceInvadersGame\\player.png"));
            playerImg = playerImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } catch (IOException e) {}
    }
    public Player() {
        super(150, 550, 50, 20, 20, 5);
        Player.posX = this.posX;
    }


    @Override
    void paint(Graphics2D g) {

        if(this.isAlive){
            g.drawImage(playerImg, posX, posY, null);
            //g.setColor(Color.white);
            //g.fillRect(posX, posY, width, height);
            //g.fillRect(posX + width/2 - width/4/2, posY - height/4, width/4, height/4);
        }
    }
}
