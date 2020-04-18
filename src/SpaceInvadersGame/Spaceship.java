package SpaceInvadersGame;

import java.awt.*;

abstract class Spaceship {
    public int posX;
    public int posY;
    int width;
    int height;
    int movementSpeed;
    public boolean isAlive;
    public int hp;

    public Spaceship(int posX, int posY, int width, int height, int movementSpeed, int hp) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.hp = hp;
        isAlive = true;
    }

    abstract void paint(Graphics2D g);
}
