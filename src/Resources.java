import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Resources {
    // to add an image to the environment:
    // 1. put the file into the res folder.
    // 2. Declare a variable before the static block.
    // 3. Initialize the variable by copying and pasting and modifying the
    //    ImageIO line.


    public static BufferedImage tennis_player, opponent, ref;
    public static BufferedImage[] pics;


    static{
        try{
            tennis_player = ImageIO.read(new File("res/tp2.png"));
            opponent = ImageIO.read(new File("res/opponent.png"));
            ref = ImageIO.read(new File("res/ref.png"));







        }catch(Exception e){e.printStackTrace();}
    }
}