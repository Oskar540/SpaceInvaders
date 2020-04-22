package SpaceInvadersGame;

import java.awt.*;

public class Missile {
    int posX;
    int posY;
    int width;
    int height;
    int speed;
    int directY;
    boolean isAlive;
    Spaceship shooter;
    static int globalWidth = 10;

    public Missile(int posX, int posY, int speed, int directY, Spaceship shooter) {
        this.posX = posX;
        this.posY = posY;
        this.width = 10;
        this.height = 25;
        this.speed = speed;
        this.directY = directY;
        isAlive = true;
        this.shooter = shooter;
    }

    synchronized void paint(Graphics2D g){
        g.setColor(Color.red);
        g.fillRect(posX, posY, width, height);
    }
}
