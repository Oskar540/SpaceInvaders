package SpaceInvadersGame;

import java.awt.*;

public class MapGenerator {
    public Enemy map[][];
    public int globalMovementSpeed;
    public int parentPosX;
    public int parentPoxY;
    public int enemyWidth;
    public int enemyHeight;
    public int screenWidth;
    public int screenHeight;
    public int gap;
    private int distance;
    private int startposX;
    public MapGenerator(int row, int col, int screenWidth, int screenHeight){
        map = new Enemy[row][col];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                map[i][j] = new Enemy();
            }
        }
        globalMovementSpeed = map[0][0].movementSpeed;
        enemyWidth = map[0][0].width;
        enemyHeight = map[0][0].height;
        gap = 10;
        distance = 50;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        double posEnemies = col/2.0d*(double)(enemyWidth);
        double posGaps = (col-1)/2.0d*(double)(gap);
        startposX = (int)((screenWidth/2.0d) - (posEnemies + posGaps));
        parentPosX = startposX;
        parentPoxY = distance;
    }

    public void draw(Graphics2D g){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j].isAlive){
                    map[i][j].posX = j * (enemyWidth + gap) + parentPosX;
                    map[i][j].posY = i * (enemyHeight + gap) + parentPoxY;
                    map[i][j].movementSpeed = globalMovementSpeed;
                    map[i][j].paint(g);
                }
            }
        }
    }
    public void enemiesMovement(){
        System.out.println("Watek");
        new Thread(()->{
            while(true) {
                try {
                    Thread.sleep(500);
                    this.parentPosX += this.globalMovementSpeed;
                    if((this.parentPosX <= 10) && (this.globalMovementSpeed < 0)){
                        this.globalMovementSpeed *= -1;
                    }
                    if(this.parentPosX + this.enemyWidth*this.map[0].length + this.gap*(this.map[0].length - 1) + 30>= screenWidth && (this.globalMovementSpeed > 0)){
                        this.globalMovementSpeed *= -1;
                    }
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }).start();
    }
}
