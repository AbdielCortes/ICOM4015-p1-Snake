package Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class Images {


    public static BufferedImage[] butstart;
    public static BufferedImage title;
    public static BufferedImage Pause;
    public static BufferedImage GameOver;
    public static BufferedImage[] Resume;
    public static BufferedImage[] BTitle;
    public static BufferedImage[] Options;
    public static BufferedImage[] Restart;
    public static ImageIcon icon;

    public Images() {

        butstart = new BufferedImage[3];
        Resume = new BufferedImage[3];
        BTitle = new BufferedImage[3];
        Options = new BufferedImage[2];
        Restart = new BufferedImage[3];

        try {

            title = ImageIO.read(getClass().getResourceAsStream("/Sheets/DioTitleScreen.png"));
            Pause = ImageIO.read(getClass().getResourceAsStream("/Sheets/DioPauseScreen.png"));
            GameOver = ImageIO.read(getClass().getResourceAsStream("/Sheets/JotaroGameOverScreen.png"));
            Resume[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/NormalButtonResume.png"));
            Resume[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/HoverButtonResume.png"));
            Resume[2] = ImageIO.read(getClass().getResourceAsStream("/Buttons/ClickedButtonResume.png"));
            BTitle[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/NormalButtonTitle.png"));
            BTitle[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/HoverButtonTitle.png"));
            BTitle[2] = ImageIO.read(getClass().getResourceAsStream("/Buttons/ClickedButtonTitle.png"));
            Restart[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/NormalButtonRestart.png"));
            Restart[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/HoverButtonRestart.png"));
            Restart[2] = ImageIO.read(getClass().getResourceAsStream("/Buttons/ClickedButtonRestart.png"));
            Options[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Options.png"));
            Options[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/OptionsP.png"));
            butstart[0]= ImageIO.read(getClass().getResourceAsStream("/Buttons/NormalButtonStart.png"));//normbut
            butstart[1]= ImageIO.read(getClass().getResourceAsStream("/Buttons/HoverButtonStart.png"));//hoverbut
            butstart[2]= ImageIO.read(getClass().getResourceAsStream("/Buttons/ClickedButtonStart.png"));//clickbut

            icon =  new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/Sheets/HeartIcon.png")));


        }catch (IOException e) {
        e.printStackTrace();
    }


    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Images.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
