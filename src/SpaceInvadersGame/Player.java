package SpaceInvadersGame;

import java.awt.*;

public class Player extends Spaceship {
    public static int posX = 0;

    public Player(int posX, int posY, int width, int height, int movementSpeed, int hp) {
        super(posX, posY, width, height, movementSpeed, hp);
        Player.posX = posX;
    }
    public Player() {
        super(150, 50, 50, 20, 20, 5);
        Player.posX = this.posX;
    }

    @Override
    void paint(Graphics2D g) {
        if(this.isAlive){
            g.setColor(Color.white);
            g.fillRect(posX, posY, width, height);
            g.fillRect(posX + width/2 - width/4/2, posY - height/4, width/4, height/4);
        }
    }
}
