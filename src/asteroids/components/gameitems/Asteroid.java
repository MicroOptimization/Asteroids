package asteroids.components.gameitems;

import asteroids.AsteroidsGUI;
import asteroids.components.GameComponent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 *
 * @author vaenthan
 */
public class Asteroid extends GameComponent {

    protected double maxHealth;
    protected double health;
    protected double radius;
    protected Ellipse2D.Double asteroidBody = new Ellipse2D.Double(x, y, radius, radius);
    protected double angle;
    protected double velocity;

    public Asteroid(double x, double y, Color color, double angle, double maxHealth, double radius, double velocity) {
        super(x, y, color);
        this.angle = angle;
        this.radius = radius;
        this.maxHealth = maxHealth;
        this.velocity = velocity;
        health = maxHealth;
    }

    public void update(AsteroidsGUI gui) {
        collisionCheck(gui);
        move();
        if (isDead()) {
            shatter(gui);
            gui.removeAsteroid(this);
            int randomInt = (int) (Math.random() * 20 + 1);
            if (randomInt == 1) {
                gui.getPowerupsList().add(new Powerups(x, y, Color.GREEN, 25, "Health"));
            } else if (randomInt == 2) {
                gui.getPowerupsList().add(new Powerups(x, y, Color.BLUE, 25, "Bomb"));
            }
            System.out.println(randomInt);
            System.out.println("powerupsList size = " + gui.getPowerupsList().size() + " x " + x + " y " + y);
        }

        x = (x < 0 || x > 1000) ? Math.abs(x - 1000) : x;
        y = (y < 0 || y > 600) ? Math.abs(y - 600) : y;
        asteroidBody = new Ellipse2D.Double(x, y, radius, radius);
    }

    public void shatter(AsteroidsGUI gui) {
        if (maxHealth > 1) {
            gui.addAsteroid(new Asteroid(x, y - 10, Color.BLACK, angle + 10, maxHealth - 2, radius / 2, velocity + 0.5));
            gui.addAsteroid(new Asteroid(x, y + 10, Color.BLACK, angle - 10, maxHealth - 2, radius / 2, velocity + 0.5));
        }
    }

    public Ellipse2D getBody() {
        return asteroidBody;
    }

    public void collisionCheck(AsteroidsGUI gui) {
        Area asteroidArea;
        Area bulletArea;
        Bullet bullet;
        ArrayList<Bullet> bulletList = gui.getBulletList();
        boolean hasCollided = false;

        for (int i = 0; i < bulletList.size(); i++) {
            bullet = bulletList.get(i);
            bulletArea = new Area((Shape) bullet.getBody());
            asteroidArea = new Area((Shape) asteroidBody);

            asteroidArea.intersect(bulletArea);
            hasCollided = !asteroidArea.isEmpty();
            if (hasCollided) {
                bulletList.remove(bullet);
                health--;
                break;
            }
        }
    }

    public boolean isDead() {
        return health == 0;
    }

    public void move() { //Don't forget to put this in the update method
        x += velocity * Math.cos(Math.toRadians(-angle - 90));
        y += velocity * Math.sin(Math.toRadians(-angle - 90));
    }

    public void paintComponent(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(asteroidBody);
        g2.setColor(Color.red);
        //g2.draw(asteroidBody.getBounds());
        Rectangle approximation = new Rectangle();
        //System.out.println(x + "," + y);
        /*approximation.add(new Point((int) x + 25, (int) y + 50));
        approximation.add(new Point((int) x + 50, (int) y + 25));
        approximation.add(new Point((int) x + 25, (int) y + 0));
        approximation.add(new Point((int) x + 0, (int) y + 25));
        */
        approximation.add(200, 200);
        approximation.add(200, 100);
        //g2.draw(approximation);
        g2.draw(new Area((Shape) asteroidBody));
    }
}