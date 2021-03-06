package SpaceInvadersGame;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    public static boolean play = false;
    public static boolean onMenu = true;
    private Rectangle2D[] stars;
    public int menuFrames = 1;
    private int timeMinutes = 0;
    private int timeSeconds = 0;
    private int bestMinutes = 0;
    private int bestSeconds = 0;
    private int row;
    private int col;

    private FileReader fr;

    private int totalEnemies;

    private Timer timer;
    private int delay = 10;

    public MapGenerator enemies;
    private Player player;
    public static ArrayList<Missile> missiles;
    public int screenWidth;
    public int screenHeight;

    public Gameplay(int width, int height){
        stars = new Rectangle[10];
        new Thread(()->{while(onMenu){
            menuFrames = menuFrames > 2 ? 1 : menuFrames + 1;
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        }}).start();
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("abduction2002.ttf")));
        } catch (IOException|FontFormatException e) { }
        enemies = new MapGenerator(5, 11, width, height);
        screenWidth = width;
        screenHeight = height;
        floatingStars();
        row = enemies.map.length;
        col = enemies.map[0].length;
        totalEnemies = row * col;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        player = new Player(width/2 - 25, 500, 50, 20, 20, 5);
        missiles = new ArrayList<>();

    }
    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);
        g.setColor(Color.white);
        for (int i = 0; i < stars.length; i++) {
            g.drawRect((int)stars[i].getX(), (int)stars[i].getY(), (int)stars[i].getWidth(), (int)stars[i].getHeight());
        }

        if(!onMenu) {
            //enemies
            enemies.draw((Graphics2D) g);

            //scores / hp
            g.setColor(Color.white);
            g.setFont(new Font("abduction2002", Font.PLAIN, 25));
            g.drawString("Health points: " + player.hp, 420, 35);
            g.drawString("Time: " + ((timeMinutes < 10) ? "0" + timeMinutes : timeMinutes) + ":" +
                    ((timeSeconds < 10) ? "0" + timeSeconds : timeSeconds), 30, 35);

            //missiles
            missiles.forEach((m) -> {
                if (m != null) m.paint((Graphics2D) g);
            });

            //player
            player.paint((Graphics2D) g);
            if (!player.isAlive) gameOver(g, false);

            if (totalEnemies <= 0) gameOver(g, true);
        }
        else{
            //menu
            for (int i = 0, x = 50, sc = 100, c = -1; i < menuFrames*2; i++, x+=3, sc += 6, c*=-1) {
                g.setColor(new Color(c));
                g.fillRoundRect(x, x, getWidth() - sc, getHeight() - sc, 20, 20);
            }
            g.setColor(Color.white);
            g.setFont(new Font("abduction2002", Font.PLAIN, 160));
            g.drawString("PJATK", 90, 220);
            g.setFont(new Font("abduction2002", Font.PLAIN, 60));
            g.drawString("SPACE INVADERS", 80, 300);
            g.setFont(new Font("abduction2002", Font.PLAIN, 20));
            g.drawString("Press 'ENTER' to PLAY", 210, 350);
            g.setFont(new Font("abduction2002", Font.PLAIN, 16));
            g.drawString("Press 'ESC' to QUIT", 250, 480);
        }
        g.dispose();
    }
    private void missilesBehaviour() {
        for (int i = 0; i < missiles.size(); i++) {
            if(missiles.get(i) != null) {
                missiles.get(i).posY += missiles.get(i).speed*missiles.get(i).directY;
                if(missiles.get(i).posY > 600 || missiles.get(i).posY <= 0){
                    missiles.set(i, null);
                }
            }
        } //missiles behaviour
    }
    private void enemiesMissilesDependencies() {
        for (int i = 0; i < enemies.map.length; i++) {
            for (int j = 0; j < enemies.map[i].length; j++) {
                for (int k = 0; k < missiles.size(); k++) {
                    if(missiles.get(k) != null)
                        if(new Rectangle(enemies.map[i][j].posX, enemies.map[i][j].posY, enemies.map[i][j].width, enemies.map[i][j].height).
                                intersects(missiles.get(k).posX, missiles.get(k).posY, missiles.get(k).width, missiles.get(k).height)
                                && enemies.map[i][j].isAlive && missiles.get(k).shooter instanceof Player)
                        {
                            missiles.set(k, null);
                            enemies.map[i][j].isAlive = false;
                            totalEnemies--;
                        }
                }
            }
        }
        missiles.forEach((m) ->{
            if(m != null)
                if(new Rectangle(player.posX, player.posY, player.width, player.height).
                        intersects(m.posX, m.posY, m.width, m.height) && player.isAlive && m.shooter instanceof Enemy){
                    missiles.set(missiles.indexOf(m), null);
                    player.hp--;
                    //System.out.println(player.hp);
                    if(player.hp <= 0) player.isAlive = false;
                }
        });
    }
    public void moveRight(){
        play = true;
        player.posX += player.movementSpeed;
    }
    public void moveLeft(){
        play = true;
        player.posX -= player.movementSpeed;
    }
    private void gameOver(Graphics g, boolean isWon){
        Thread.interrupted();
        play = false;
        for (int i = 0, x = 75, sc = 130, c = -1; i < 4; i++, x+=5, sc += 10, c*=-1) {
            g.setColor(new Color(c));
            g.fillRoundRect(x, x, getWidth() - sc, getHeight() -100 - sc, 20, 20);
        }

        g.setColor(Color.red);
        g.setFont(new Font("abduction2002", Font.PLAIN, 60));
        g.drawString("GAME OVER!", 170, 160);
        if(!isWon){
            g.setFont(new Font("abduction2002", Font.PLAIN, 28));
            g.drawString("I'm sorry.. You failed!", 165, 200);
        }
        else{
            g.setFont(new Font("abduction2002", Font.PLAIN, 40));
            g.drawString("Congratulations!", 165, 200);
            if(timeMinutes < bestMinutes || (timeMinutes == bestMinutes && timeSeconds < bestSeconds)){
                bestMinutes = timeMinutes;
                bestSeconds = timeSeconds;
                PrintWriter zapis = null;
                try {zapis = new PrintWriter("C:\\Users\\oskar\\Documents\\PJATK dev\\SpaceInvadersGame\\src\\SpaceInvadersGame\\highscore.txt");}
                catch (FileNotFoundException e) {e.printStackTrace();}
                zapis.println(timeMinutes);
                zapis.println(timeSeconds);
                zapis.close();
            }
        }
        g.setColor(Color.white);
        g.setFont(new Font("abduction2002", Font.PLAIN, 28));
        g.drawString("Best time: " + ((bestMinutes < 10) ? "0" + bestMinutes : bestSeconds) + ":" +
                ((bestSeconds < 10) ? "0" + bestSeconds : bestSeconds), 220, 270);
        g.setFont(new Font("abduction2002", Font.PLAIN, 20));
        g.drawString("Your time: " + ((timeMinutes < 10) ? "0" + timeMinutes : timeMinutes) + ":" +
                ((timeSeconds < 10) ? "0" + timeSeconds : timeSeconds), 250, 310);
        g.setFont(new Font("abduction2002", Font.PLAIN, 14));
        g.drawString("Press ESC to QUIT", 110, 380);
        g.setFont(new Font("abduction2002", Font.PLAIN, 14));
        g.drawString("Press ENTER to PLAY", 420, 380);
        missiles.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            enemiesMissilesDependencies();
            missilesBehaviour();
        }
        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                for (int i = 0; i < enemies.map.length; i++) {
                    for (int j = 0; j < enemies.map[i].length; j++) {
                        enemies.map[i][j].isAlive = false;
                    }
                }
                player.isAlive = true;
                player.posX = 310;
                timeSeconds = 0;
                timeMinutes = 0;
                enemies = new MapGenerator(row, col, this.screenWidth, this.screenHeight);
                totalEnemies = enemies.map.length * enemies.map[0].length;
                player = new Player(screenWidth / 2 - 25, 500, 50, 20, 20, 5);
                enemies.enemiesBehaviour();
                Scanner sc = null;
                try {
                    sc = new Scanner(new File("C:\\Users\\oskar\\Documents\\PJATK dev\\SpaceInvadersGame\\src\\SpaceInvadersGame\\highscore.txt"));
                    bestMinutes = Integer.parseInt(sc.nextLine());
                    bestSeconds = Integer.parseInt(sc.nextLine());
                } catch (FileNotFoundException ex) {ex.printStackTrace();}
                repaint();
            }
            onMenu = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            totalEnemies = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            player.isAlive = false;
        }
        if(!onMenu) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if(!play) floatingTime();
                play = true;
                if (player.posX <= 10) {
                    player.posX = 10;
                } else {
                    moveLeft();
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(!play) floatingTime();
                play = true;
                if(player.posX >= screenWidth - player.width - 20){
                    player.posX = screenWidth - player.width - 20;
                }
                else{
                    moveRight();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (play) {
                    missiles.add(new Missile(player.posX + player.width / 2 - Missile.globalWidth / 2, player.posY, 5, -1, player));
                }
            }
        }
    }

    private void floatingStars(){
        new Thread(()->{
            for (int i = 0; i < stars.length; i++) {
                stars[i] = new Rectangle((int) (Math.random() * screenWidth), (int) (Math.random() * screenHeight), 2, 2);
            }
            while(true) {
                for (int i = 0; i < stars.length; i++) {
                    if(stars[i] != null){
                        stars[i].setRect(stars[i].getX(), stars[i].getY() + 1*i/7 + 1, stars[i].getWidth(), stars[i].getHeight());
                        if(stars[i].getY() >= screenHeight) stars[i] = new Rectangle((int) (Math.random() * getWidth()),0, 2, 2);
                    }
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void floatingTime(){
        new Thread(()-> {
            boolean delayed = false;
            try {Thread.sleep(100);} catch (InterruptedException ex) {System.out.println("Nie spij");}
            while(play){
                if(!delayed) try {Thread.sleep(900);} catch (InterruptedException e) {e.printStackTrace();}
                delayed = true;
                if(timeSeconds >= 59){
                    timeMinutes++;
                    timeSeconds = 0;
                }
                else timeSeconds++;
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
