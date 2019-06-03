package asteroids;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

import asteroids.components.GameComponent;
import asteroids.components.gameitems.SpaceBackground;
import asteroids.components.gameitems.Asteroid;
import asteroids.components.gameitems.Bullet;
import asteroids.components.gameitems.Powerups;
import asteroids.listeners.MoveListener;
import asteroids.components.gameitems.Player;
import java.util.Random;

public final class AsteroidsGUI extends JPanel implements Runnable {

    private JFrame frame = new JFrame("Asteroids");
    private JPanel mainPanel = new JPanel(); //Layout
    private ArrayList<GameComponent> components = new ArrayList<>();
    private ArrayList<Bullet> bulletList = new ArrayList<>();
    private ArrayList<Asteroid> asteroidList = new ArrayList<>();
    private ArrayList<Powerups> powerupsList = new ArrayList<>();
    private int frameLength = 1000;
    private int frameHeight = 600;
    private int maxAsteroidDelay = 250;
    private int asteroidDelay = 0;

    private SpaceBackground sbg = new SpaceBackground(this);
    //Has it's own run method for precise twinkle frequency.
    //Also doesn't get painted in an arraylist.
    
    public Player player = new Player(500, 300, Color.WHITE, .1);
    private boolean running = false;



    public void start() {
        Thread thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            // Check for collision, draw objects and sleep
            for (GameComponent i : components) {
                i.update(this); //Updates state of game objects.
            }
            for (int i = 0; i < bulletList.size(); i++) { //Don't change this to a for each
                bulletList.get(i).update(this); //Updates state of game objects.
            }
            for (int i = 0; i < asteroidList.size(); i++) { //Don't change this to a for each
                asteroidList.get(i).update(this); //Updates state of game objects.
            }
            for (int i = 0; i < powerupsList.size(); i++) {
                powerupsList.get(i).update(this);
            }
            spawnAsteroid();
            repaint(); //Draws objects
            try {
                Thread.sleep(17);
            } catch (Exception e) {
            }
        }
    }

    public AsteroidsGUI() {
        // Add the plane and control panels to the main panel
        components.add(player);
        // Configure the frame
        frame.addKeyListener(new MoveListener(this));
        frame.setVisible(true);
        frame.setSize(frameLength, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setFocusable(true);
        asteroidList.add(new Asteroid(750, 250, Color.WHITE, 90, 5, 50, 3));
        start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < components.size(); i++) {
            components.get(i).paintComponent(g2);
        }
        for (int i = 0; i < bulletList.size(); i++) {
            bulletList.get(i).paintComponent(g2); //Paints state of Bullets.
        }
        for (int i = 0; i < asteroidList.size(); i++) {
            asteroidList.get(i).paintComponent(g2);
        }
        for (int i = 0; i < powerupsList.size(); i++) {
            powerupsList.get(i).paintComponent(g2);
        }
        sbg.paintComponent(g2);
        this.setBackground(Color.decode("#240046"));
    }

    public void spawnAsteroid() {
        asteroidDelay++;
        if (asteroidDelay > maxAsteroidDelay && asteroidList.size() < 15) {
            createAsteroid();
            asteroidDelay = 0;
            maxAsteroidDelay -= (maxAsteroidDelay > 0) ? 10 : 0;
        }
    }

    public void createAsteroid() {
        Random random = new Random();
        int spawnX = random.nextInt(2) == 0 ? 0 : random.nextInt(frameLength);
        int spawnY = spawnX == 0 ? random.nextInt(frameHeight) : 0;

        Color spawnColor = Color.WHITE;
        double angle = random.nextInt(360);
        double maxHealth = 5;
        double radius = 50;
        double velocity = 3;
        asteroidList.add(new Asteroid(spawnX, spawnY, spawnColor, angle, maxHealth, radius, velocity));
    }

    public void addPlayerBullet(Bullet newBullet) {
        bulletList.add(newBullet);
    }

    public void removePlayerBullet(Bullet newBullet) {
        bulletList.remove(newBullet);
    }

    public ArrayList getBulletList() {
        return bulletList;
    }

    public ArrayList getPowerupsList() {
        return powerupsList;
    }

    public void addPowerup(Powerups powerup) {
        powerupsList.add(powerup);
    }

    public ArrayList getAsteroidList() {
        return asteroidList;
    }

    public void addAsteroid(Asteroid asteroid) {
        asteroidList.add(asteroid);
    }

    public void removeAsteroid(Asteroid asteroid) {
        asteroidList.remove(asteroid);
    }
    
    public int getFrameLength() {
        return frameLength;
    }
    
    public int getFrameHeight() {
        return frameHeight;
    }

    public static void main(String[] args) {
        AsteroidsGUI gui = new AsteroidsGUI(); //MAIN HERE
    }
}