import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.Math;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class GameMain extends JPanel {

    private boolean[] keys;
    private int rally, playerScore, oppScore, playerSets, oppSets, refCount;
    private Player player;
    private Opponent opponent;
    private Ball ball;

    private Sprite ref;

    private boolean refCalling;
    private String refCall, playerBoard, oppBoard;

    private Timer refTimer;

    public GameMain() {
        super();
        keys = new boolean[256];
        refCalling = false;
        refCall = "";
        playerBoard = "00";
        oppBoard = "00";

        player = new Player(new Point(645, 700), true);
        opponent = new Opponent(Resources.opponent, new Point(665, 30));
        ref = new Sprite(Resources.ref, new Point(1080, 400));


        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });

        refTimer = new Timer(1000 / 60, e -> refCounter());

        Timer timer = new Timer(1000 / 60, e -> update());
        timer.start();
    }

    public void update() {
        // I left this here as a reminder how to handle the key input
        int dx = keys[KeyEvent.VK_A] ? -1 : 0;
        dx = keys[KeyEvent.VK_D] ? 1 : dx;

        if (refCalling && !refTimer.isRunning()){
//            refTimer.restart();
            refTimer.start();

        }
        else {
            // SERVING
            if (player.getServing() && !refCalling) {
                ball = new Ball(new Point(player.getX() + 50, player.getY()));
                if (keys[KeyEvent.VK_A]) {
                    player.move(-8, 0);
                    if (player.getX() < getWidth() / 2 - 380)
                        player.setLocation(getWidth() / 2 - 380, player.getY());
                }
                if (keys[KeyEvent.VK_D]) {
                    player.move(8, 0);
                    if (player.getX() > getWidth() / 2 + 380 - player.getWidth())
                        player.setLocation(getWidth() / 2 + 380 - player.getWidth(), player.getY());
                }

                if (player.intersects(ball)) {
                    if (keys[KeyEvent.VK_UP]) {
                        player.setServing(false);
                        player.setTurn(false);
                        ball.setDx(0);
                        ball.setDy(-8);
                    } else if (keys[KeyEvent.VK_LEFT]) {
                        player.setServing(false);
                        player.setTurn(false);
                        ball.setDx(-3);
                        ball.setDy(-6);
                    } else if (keys[KeyEvent.VK_RIGHT]) {
                        player.setServing(false);
                        player.setTurn(false);
                        ball.setDx(3);
                        ball.setDy(-6);
                    }
                }
            }

            // PLAYING
            if (!player.getServing() && !refCalling) {

                // PLAYER

                // MOVEMENT
                if (keys[KeyEvent.VK_A]) {
                    player.move(-10, 0);
                    if (player.getX() < 0)
                        player.setLocation(0, player.getY());
                }
                if (keys[KeyEvent.VK_D]) {
                    player.move(10, 0);
                    if (player.getX() + player.getWidth() > getWidth())
                        player.setLocation(getWidth() - player.getWidth(), player.getY());
                }
                if (keys[KeyEvent.VK_W]) {
                    player.move(0, -10);
                    if (player.getY() < getHeight() / 2 - 20)
                        player.setLocation(player.getX(), getHeight() / 2 - 20);
                }
                if (keys[KeyEvent.VK_S]) {
                    player.move(0, 10);
                    if (player.getY() + player.getWidth() / 2 > getHeight())
                        player.setLocation(player.getX(), getHeight() - player.getWidth() / 2);
                }

                // ATTACKING
                if (player.getTurn()) {
                    if (player.intersects(ball)) {
                        if (keys[KeyEvent.VK_UP]) {
                            player.setTurn(false);
                            ball.setDx(0);
                            ball.setDy(-8 - rally / 3);
                            rally += 1;
                        } else if (keys[KeyEvent.VK_LEFT]) {
                            player.setTurn(false);
                            ball.setDx(-4 - rally / 3);
                            ball.setDy(-8 - rally / 3);

                            rally += 1;
                        } else if (keys[KeyEvent.VK_RIGHT]) {
                            player.setTurn(false);
                            ball.setDx(4 + rally / 3);
                            ball.setDy(-8 - rally / 3);
                            rally += 1;
                        } else if (keys[KeyEvent.VK_DOWN]) {
                            player.setTurn(false);
                            ball.setDx(0);
                            ball.setDy(-7);
                            rally = 0;
                        }
                    }
                }

                //OPPONENT
                if (ball.getDy() < 0) {

                    // BOUNDS
                    if (opponent.getY() > getHeight() / 2 - 100) {
                        opponent.setLocation(opponent.getX(), getHeight() / 2 - 100);
                    }
                    if (opponent.getY() < 0) {
                        opponent.setLocation(opponent.getX(), 0);
                    }
                    if (opponent.getX() < getWidth() / 2 - 300) {
                        opponent.setLocation(getWidth() / 2 - 300, opponent.getY());
                    }
                    if (opponent.getX() > getWidth() / 2 + 300 - opponent.getWidth()) {
                        opponent.setLocation(getWidth() / 2 + 300 - opponent.getWidth(), opponent.getY());
                    }

                    // BALL TRACKING
                    if (ball.getLoc().y > opponent.getY()) {
                        opponent.move(0, 2);
                    }
                    if (ball.getLoc().x < opponent.getX()) {
                        opponent.move(-7, 0);
                    }
                    if (ball.getLoc().x > opponent.getX()) {
                        opponent.move(7, 0);
                    }

                    // ATTACKING
                    if (opponent.intersects(ball) && !player.getTurn()) {

                        if (opponent.getX() < getWidth() / 2) {
                            if (ball.getDy() > -12) {
                                if (Math.random() * 100 > 2) {
                                    if (Math.random() * 10 > 5) {
                                        ball.setDx(0);
                                        ball.setDy(-ball.getDy());
                                    } else {
                                        ball.setDx(4 + rally / 3);
                                        ball.setDy(-ball.getDy());
                                    }
                                }
                            } else {
                                if (Math.random() * 100 > 5) {
                                    if (Math.random() * 10 > 5) {
                                        ball.setDx(0);
                                        ball.setDy(-ball.getDy());
                                    } else {
                                        ball.setDx(4 + rally / 3);
                                        ball.setDy(-ball.getDy());
                                    }
                                }
                            }
                        } else {
                            if (ball.getDy() > -12) {
                                if (Math.random() * 100 > 2) {
                                    if (Math.random() * 10 > 5) {
                                        ball.setDx(0);
                                        ball.setDy(-ball.getDy());
                                    } else {
                                        ball.setDx(-4 - rally / 3);
                                        ball.setDy(-ball.getDy());
                                    }
                                }
                            } else {
                                if (Math.random() * 100 > 5) {
                                    if (Math.random() * 10 > 5) {
                                        ball.setDx(0);
                                        ball.setDy(-ball.getDy());
                                    } else {
                                        ball.setDx(-4 - rally / 3);
                                        ball.setDy(-ball.getDy());
                                    }
                                }
                            }
                        }
                        player.setTurn(true);
                    }
                }

                // RETURNING TO BASE

                else {
                    Point startPosition = new Point(665, 30);
                    Point currentPosition = opponent.getLocation();

                    if (!Objects.equals(currentPosition, startPosition)) {
                        int oppDx = startPosition.x - currentPosition.x;
                        int oppDy = startPosition.y - currentPosition.y;

                        if (Math.abs(oppDx) > 5) {
                            oppDx = (oppDx > 0) ? 5 : -3;
                        }
                        if (Math.abs(oppDy) > 5) {
                            oppDy = (oppDy > 0) ? 5 : -3;
                        }

                        opponent.move(oppDx, oppDy);
                    }
                }

                // Point Scoring
                if (ball.getLoc().y < 0) {
                    if (ball.getDx() == 0 && (ball.getLoc().x < getWidth()/2-300 || ball.getLoc().x > getWidth()/2+300)){
                        oppScore += 1;
                        refCall = "Out";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    }
                    else if (ball.getLoc().x < getWidth() / 2 - 450 || ball.getLoc().x > getWidth() / 2 + 450) {
                        oppScore += 1;
                        refCall = "Out";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    } else if ((ball.getLoc().x < getWidth() / 2 - 410 && ball.getDy() < -12) || (ball.getLoc().x > getWidth() / 2 + 410 && ball.getDy() < -12)) {
                        oppScore += 1;
                        refCall = "Out";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    } else {
                        playerScore += 1;
                        refCall = "In";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    }
                }
                if (ball.getLoc().y > getHeight() - 20) {
                    if (ball.getLoc().x < getWidth() / 2 - 450 || ball.getLoc().x > getWidth() / 2 + 450) {
                        playerScore += 1;
                        refCall = "Out";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    } else if ((ball.getLoc().x < getWidth() / 2 - 410 && ball.getDy() > 12) || (ball.getLoc().x > getWidth() / 2 + 410 && ball.getDy() > 12)) {
                        playerScore += 1;
                        refCall = "Out";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    } else {
                        oppScore += 1;
                        refCall = "In";
                        refCalling = true;
                        player.setServing(true);
                        rally = 0;
                    }
                }

                if (playerScore == 0)
                    playerBoard = "00";
                else if (playerScore == 1)
                    playerBoard = "15";
                else if (playerScore == 2)
                    playerBoard = "30";
                else if (playerScore == 3)
                    playerBoard = "40";

                if (oppScore == 0)
                    oppBoard = "00";
                else if (oppScore == 1)
                    oppBoard = "15";
                else if (oppScore == 2)
                    oppBoard = "30";
                else if (oppScore == 3)
                    oppBoard = "40";

                if (playerScore > 3 || oppScore > 3){
                    if (playerScore - oppScore > 1){
                        playerSets++;
                        playerBoard = "00";
                        oppBoard = "00";
                        playerScore = 0;
                        oppScore = 0;
                    }
                    if (oppScore - playerScore > 1){
                        oppSets++;
                        playerBoard = "00";
                        oppBoard = "00";
                        playerScore = 0;
                        oppScore = 0;
                    }
                    if (playerScore >= 4 && oppScore == playerScore-1){
                        playerBoard = "AD";
                        oppBoard = "40";
                    }
                    if (oppScore >= 4 && playerScore == oppScore-1){
                        oppBoard = "AD";
                        playerBoard = "40";
                    }
                    else if (oppScore == playerScore && (oppScore > 0)){
                        oppBoard = "40";
                        playerBoard = "40";
                    }


                }

            }

        }

        ball.update();

        repaint();  //update graphics
    }

    public void refCounter(){
        refCount++;
        if(refCount%100==0)
            System.out.println(refCount);
        if (refCount > 120){
            System.out.println(refCount);
            refTimer.stop();
            player.setLocation(645, 700);
            opponent.setLocation(665, 30);
            refCalling = false;
            refCount = 0;
        }

    }

    @Override
    // All drawing originates from this method
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        Color background = new Color(135, 190, 100);
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
        Color court = new Color(66, 102, 155);
        g.setColor(court);
        g.fillRect(getWidth() / 2 - 300, getHeight() / 2 - 350, 600, 700);

        g.setStroke(new BasicStroke(3));
        g.setColor(Color.white);
        g.drawRect(getWidth() / 2 - 300, getHeight() / 2 - 350, 600, 700);
        g.drawLine(getWidth() / 2 - 200, getHeight() / 2 - 350, getWidth() / 2 - 200, getHeight() / 2 + 350);
        g.drawLine(getWidth() / 2 + 200, getHeight() / 2 - 350, getWidth() / 2 + 200, getHeight() / 2 + 350);
        g.drawLine(getWidth() / 2 - 200, getHeight() / 2 - 215, getWidth() / 2 + 200, getHeight() / 2 - 215);
        g.drawLine(getWidth() / 2 - 200, getHeight() / 2 + 215, getWidth() / 2 + 200, getHeight() / 2 + 215);
        g.drawLine(getWidth() / 2, getHeight() / 2 - 215, getWidth() / 2, getHeight() / 2 + 215);
        g.drawLine(getWidth() / 2, getHeight() / 2 - 350, getWidth() / 2, getHeight() / 2 - 335);
        g.drawLine(getWidth() / 2, getHeight() / 2 + 350, getWidth() / 2, getHeight() / 2 + 335);

        g.setStroke(new BasicStroke(5));
        g.drawLine(getWidth() / 2 - 325, getHeight() / 2, getWidth() / 2 + 325, getHeight() / 2);

        g.fillOval(getWidth() / 2 - 335, getHeight() / 2 - 10, 20, 20);
        g.fillOval(getWidth() / 2 + 315, getHeight() / 2 - 10, 20, 20);

        player.draw(g);
        opponent.draw(g);
        ref.draw(g);
        ball.draw(g);


        // SCOREBOARD

        g.setColor(Color.white);
        g.fillRect(20, 50, 380, 300);

        g.setColor(Color.black);
        g.drawRect(20, 50, 380, 300);

        g.setStroke(new BasicStroke(4));
        g.drawRect(50, 100, 120, 120);
        g.drawRect(250, 100, 120, 120);

        g.drawRect(130, 265, 70, 70);
        g.drawRect(220, 265, 70, 70);

        g.setColor(Color.black);
        Font hacs = new Font("Arial", Font.PLAIN, 30);
        g.setFont(hacs);
        g.drawString("HACS", 68, 95);
        Font apcsa = new Font("Arial", Font.PLAIN, 30);
        g.setFont(apcsa);
        g.drawString("AP CS A", 252, 95);

        g.drawString("Games Won", 126, 255);

        if (Objects.equals(playerBoard, "AD")){
            Font score = new Font("Arial", Font.BOLD, 70);
            g.setFont(score);
            g.drawString(playerBoard, 60, 185);
        }
        else {
            Font score = new Font("Arial", Font.BOLD, 90);
            g.setFont(score);
            g.drawString(playerBoard, 60, 195);
        }
        if (Objects.equals(oppBoard, "AD")){
            Font score = new Font("Arial", Font.BOLD, 70);
            g.setFont(score);
            g.drawString(oppBoard, 260, 185);
        }
        else {
            Font score = new Font("Arial", Font.BOLD, 90);
            g.setFont(score);
            g.drawString(oppBoard, 260, 195);
        }


        Font sets = new Font("Arial", Font.BOLD, 60);
        g.setFont(sets);
        g.drawString(""+playerSets, 148, 320);
        g.drawString(""+oppSets, 238, 320);

        if (refCalling){
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.white);
            g.fillRoundRect(1200, 300, 150, 80, 10, 10);
            g.fillPolygon(new int[] {1210, 1240, 1205}, new int[] {380, 380, 420}, 3);

            g.setColor(Color.black);
            g.drawRoundRect(1200, 300, 150, 80, 10, 10);

            g.setColor(Color.white);
            g.drawLine(1210, 380, 1238, 380);

            g.setColor(Color.black);
            g.drawLine(1210, 380, 1205, 420);
            g.drawLine(1240, 380, 1205, 420);


            Font call = new Font("Arial", Font.BOLD, 40);
            g.setFont(call);
            if (refCall.equals("In"))
                g.drawString(refCall, 1255, 355);
            else g.drawString(refCall, 1235, 355);
        }

        // TESTING LINES
//        g.setColor(Color.red);
//        if (Math.abs(ball.getDy()) < 12){
//            g.drawLine(getWidth() / 2 - 450, 0, getWidth() / 2 - 450, getHeight());
//            g.drawLine(getWidth() / 2 + 450, 0, getWidth() / 2 + 450, getHeight());
//        }
//        else{
//            g.drawLine(getWidth() / 2 - 410, 0, getWidth() / 2 - 410, getHeight());
//            g.drawLine(getWidth() / 2 + 410, 0, getWidth() / 2 + 410, getHeight());
//        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Tennis");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GameMain panel = new GameMain();
        panel.setFocusable(true);
        panel.grabFocus();
        window.add(panel);

        // Maximize the window initially
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }
}
