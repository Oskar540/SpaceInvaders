package SpaceInvadersGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int row;
    private int col;

    private int totalEnemies;

    private Timer timer;
    private int delay = 8;

    public MapGenerator enemies;
    private Player player;
    private ArrayList<Missile> missiles;
    public int screenWidth;
    public int screenHeight;
    private boolean isMovingThreadPlayed;

    public Gameplay(int width, int height){
        enemies = new MapGenerator(5, 11, width, height);
        row = enemies.map.length;
        col = enemies.map[0].length;
        totalEnemies = row * col;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        screenWidth = width;
        screenHeight = height;
        player = new Player(width/2 - 25, 550, 50, 20, 20, 5);
        missiles = new ArrayList<>();
        isMovingThreadPlayed = false;
    }

    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);
        g.setColor(Color.white);
        g.drawLine(screenWidth/2, 0, screenWidth/2, screenHeight);

        //enemies
        enemies.draw((Graphics2D)g);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 590, 30);

        //missiles
        missiles.forEach((m) -> {
            if(m != null) m.paint((Graphics2D) g);
        });

        //player
        player.paint((Graphics2D) g);

        if(totalEnemies <= 0) gameOver(g, true);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            for (int i = 0; i < enemies.map.length; i++) {
                for (int j = 0; j < enemies.map[i].length; j++) {
                    AtomicInteger ai = new AtomicInteger(i);
                    AtomicInteger aj = new AtomicInteger(j);
                    missiles.forEach((m) ->{
                        if(m != null)
                        if(new Rectangle(enemies.map[ai.get()][aj.get()].posX,
                                        enemies.map[ai.get()][aj.get()].posY,
                                        enemies.map[ai.get()][aj.get()].width,
                                        enemies.map[ai.get()][aj.get()].height).
                                        intersects(m.posX, m.posY, m.width, m.height)
                                        && enemies.map[ai.get()][aj.get()].isAlive){
                            enemies.map[ai.get()][aj.get()].isAlive = false;
                            totalEnemies--;
                            missiles.set(missiles.indexOf(m), null);
                        }
                    });

                }
            } //enemies <-> missiles dependencies
            for (int i = 0; i < missiles.size(); i++) {
                if(missiles.get(i) != null) {
                    missiles.get(i).posY += missiles.get(i).speed*missiles.get(i).directY;
                    System.out.println("Position: " + missiles.get(i).posY);
                    if(missiles.get(i).posY > 600 || missiles.get(i).posY <= 0){
                        missiles.set(i, null);
                        System.out.println("Bullet " + i + " poofed..");
                    }
                }
            } //missiles behaviour
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(player.posX >= screenWidth - player.width - 10){
                    player.posX = screenWidth - player.width - 10;
                }
                else{
                    moveRight();
                }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(player.posX <= 10){
                player.posX = 10;
            }
            else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                player.posX = 310;
                score = 0;
                enemies = new MapGenerator(row, col, this.screenWidth, this.screenHeight);
                totalEnemies = enemies.map.length * enemies.map[0].length;
                repaint();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE && play){
            missiles.add(new Missile(player.posX + player.width/2 - Missile.globalWidth/2, player.posY, 5, -1, player));
        }
    }

    public void moveRight(){
        if(!play) enemies.enemiesMovement();
        play = true;
        player.posX += player.movementSpeed;
    }
    public void moveLeft(){
        if(!play) enemiesMovement();
        play = true;
        player.posX -= player.movementSpeed;
    }
    private void gameOver(Graphics g, boolean isWon){
        play = false;
        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD, 30));
        if(isWon) g.drawString("You won! Scores: "+ score, 260, 300);
        else g.drawString("Game Over! Scores: "+ score, 190, 300);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Press Enter to restart", 230, 350);
        missiles.clear();
    }
    public void enemiesMovement(){
        new Thread(()->{
                while(true) {
                    try {
                        Thread.sleep(500);
                        enemies.parentPosX += enemies.globalMovementSpeed;
                        if((enemies.parentPosX <= 10) && (enemies.globalMovementSpeed < 0)){
                            enemies.globalMovementSpeed *= -1;
                        }
                        if(enemies.parentPosX + enemies.enemyWidth*enemies.map[0].length + enemies.gap*(enemies.map[0].length - 1) + 30>= screenWidth && (enemies.globalMovementSpeed > 0)){
                            enemies.globalMovementSpeed *= -1;
                        }
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
        }).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
