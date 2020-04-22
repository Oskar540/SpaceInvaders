package SpaceInvadersGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Enemy extends Spaceship {
    public Image enemyImg;
    public Enemy(int posX, int posY, int width, int height, int movementSpeed, int hp) {
        super(posX, posY, width, height, movementSpeed, hp);
        try {
            enemyImg = ImageIO.read(new File("C:\\Users\\oskar\\Documents\\PJATK dev\\SpaceInvadersGame\\src\\SpaceInvadersGame\\alien.png"));
            enemyImg = enemyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Enemy(int posX,int posY) {
        super(posX, posY, 33, 24, 10, 1);
    }
    public Enemy() {
        super(11, 0, 33, 24, 10, 1);
        try {
            enemyImg = ImageIO.read(new File("C:\\Users\\oskar\\Documents\\PJATK dev\\SpaceInvadersGame\\src\\SpaceInvadersGame\\alien.png"));
            enemyImg = enemyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void paint(Graphics2D g) {
        g.drawImage(enemyImg, posX, posY, null);
    }

    void paint(Graphics2D g, Color color) {
        g.drawImage(enemyImg, posX, posY, null);
    }
}
