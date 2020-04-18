package SpaceInvadersGame;

import java.awt.*;

public class Enemy extends Spaceship {
    public Enemy(int posX, int posY, int width, int height, int movementSpeed, int hp) {
        super(posX, posY, width, height, movementSpeed, hp);
    }
    public Enemy(int posX,int posY) {
        super(posX, posY, 30, 30, 10, 1);
    }
    public Enemy() {
        super(11, 0, 30, 30, 10, 1);
        //this.shootingController();
    }

    @Override
    void paint(Graphics2D g) {
        g.setColor(Color.green);
        g.fillRect(posX, posY, width, height);
    }

    void shootingController(){
        new Thread(()->{
            while(true){
                //System.out.println("Player:" + Player.posX);
                if(Math.abs(this.posX - 5) <= Player.posX){
                    System.out.println("Alien: " + this.posX);
                }
            }
        }).start();
    }
}
