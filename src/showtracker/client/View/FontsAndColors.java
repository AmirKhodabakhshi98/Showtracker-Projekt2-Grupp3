package showtracker.client.View;

import javax.swing.*;
import java.awt.*;


/**
 * This class is created to assist in developing GUI components
 * the class holds methods for used fonts, colors and images in the project.
 * Adda
 * @author Paul Moustakas
 * @version 1.0.0
 */
public abstract class FontsAndColors {

    /**
     * Body text
     * @return Monospaced font in bold size of choice.
     */
    public static Font getFontPlain(int size) {
        return new Font("Monospaced", Font.PLAIN, size);
    }

    /**
     * Body text Bold
     * @return Monospaced font in bold size of choice.
     */
    public static Font getFontBold(int size) {
        return new Font("Monospaced", Font.BOLD, size);
    }


    /**
     * Body text
     * @return Monospaced font in italic size of choice.
     */
    public static Font getFontItalic(int size) {
        return new Font("Monospaced", Font.ITALIC, size);
    }


    /**
     * Title Bold
     * @return Monospaced title font - size of choice.
     */
    public static Font getFontTitle(int size) {
        return new Font("Monospaced", Font.BOLD, size);
    }


    /**
     * Project Color Blue #6A86AA
     */
    public static Color getProjectBlue () {
        return Color.decode(("#6A86AA"));
    }



    /**
     * Project new Logo Image
     * @return Project logo - size of choice
     */
    public static Image getLogo (int width, int height) {
        ImageIcon imi = new ImageIcon("images/Showtrack.png");
        Image image = imi.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return image;
    }


    //TEST COLOR WHITE
    //		pnlSearchResult.setBackground(Color.decode("#F8F7ED"));


}
