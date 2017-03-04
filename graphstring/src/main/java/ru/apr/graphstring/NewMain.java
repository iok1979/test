package ru.apr.graphstring;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 *
 * @author prusakovan
 */
public class NewMain {

    public static void main(String[] args) {

        String text = args.length > 0 ? args[0] : "ПОВТОРНО";
        int font = args.length > 1 ? Integer.valueOf(args[1]) : 26;
        int w = args.length > 2 ? Integer.valueOf(args[2]) : 300;
        int h = args.length > 3 ? Integer.valueOf(args[3]) : 300;
        int degreed = args.length > 4 ? Integer.valueOf(args[4]) : 45;

        InputStream is = null;
        try {
            is = GenerateImage.getText(text, font, w, h, degreed);
            Path f = Paths.get("./output_image.png");
            Files.deleteIfExists(f);
            long copy = Files.copy(is, f);
            System.out.println(format("<-- saved %d byte", copy));
        } catch (IOException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
