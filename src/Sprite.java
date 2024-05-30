import java.awt.*;
import java.awt.image.BufferedImage;

/* This class represents a Sprite in a game.
   Sprites have an image and location, and can
   detect collision/intersection with another Sprite.
   This class is set up to have rectangular collision
   detection - rectangles intersecting rectangles.
 */

public class Sprite {

    private BufferedImage image;
    private Point location;

    public Sprite(BufferedImage image, Point location) {
        this.image = image;
        this.location = location;
    }

    public void draw(Graphics2D g2){
        g2.drawImage(image, location.x, location.y, null);
    }

    public boolean intersects(Ball other){
        Rectangle hitBox = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
        Rectangle otherHitBox = new Rectangle(other.getLoc().x, other.getLoc().y, 12, 12);
        return hitBox.intersects(otherHitBox);
    }

    public void move(int dx, int dy){
        location.translate(dx, dy);
    }

    public int getX(){return location.x;}
    public int getY(){return location.y;}
    public int getWidth(){ return image.getWidth();}
    public void setLocation(int x, int y){ location = new Point(x, y); }

    public Point getLocation() {
        return location;
    }

    public Point getCenter() {
        return new Point(getX() - getX()/2, getY() - getY()/2);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
