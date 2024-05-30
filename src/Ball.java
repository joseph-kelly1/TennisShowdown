import javax.swing.*;
import java.awt.*;

public class Ball {

    private Point loc;
    private int dx, dy;

    public Ball(Point loc) {
        this.loc = loc;
    }

    public Rectangle getHitBox(){
        return new Rectangle(loc.x, loc.y, 12, 12);
    }

    public void draw(Graphics2D g2){
        //  all my sprites are black rectangles.
        g2.setColor(Color.black);
        g2.drawOval(loc.x, loc.y, 12, 12);
        Color ballColor = new Color(210, 230, 80);
        g2.setColor(ballColor);
        g2.fillOval(loc.x, loc.y, 12, 12);
    }

    public void setLoc(Point loc) {
        this.loc = loc;
    }

    public void setX(int x){this.loc.x = x;}

    public void setY(int y){this.loc.y = y;}

    public int getDx(){return this.dx;}
    public int getDy(){return this.dy;}
    public void setDx(int dx){this.dx = dx;}
    public void setDy(int dy){this.dy = dy;}

    public Point getLoc() {
        return loc;
    }

    public void move(int dx, int dy){
        loc.translate(dx, dy);
    }

    public void update(){
        move(dx, dy);
        if (loc.x < 0  || loc.x > 1440) {
            setDx(-dx);
            setDy(dy);
        }

        if (loc.y < 0 || loc.y > 900) {
            setDx(dx);
            setDy(-dy);
        }

    }


}