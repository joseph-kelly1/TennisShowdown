import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Sprite {

    private boolean isServing, isTurn;
    public Player(Point location, boolean isServing) {
        super(Resources.tennis_player, location);
        this.isServing = isServing;
        this.isTurn = true;
    }

    public boolean getServing(){return isServing;}

    public void setServing(boolean isServing){this.isServing = isServing;}

    public boolean getTurn(){return isTurn;}

    public void setTurn(boolean isTurn){this.isTurn = isTurn;}



}