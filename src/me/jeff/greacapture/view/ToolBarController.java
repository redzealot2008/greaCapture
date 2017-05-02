package me.jeff.greacapture.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author Jeff Chou
 *         date 2017/4/22 0022
 */
public class ToolBarController {

    public ToolBarController() {
    }

    public void imageToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        WritableImage writableImage = new WritableImage(GlobalProxy.INS.getBufferedImage().getWidth(), GlobalProxy.INS.getBufferedImage().getHeight());
        clipboardContent.putImage(SwingFXUtils.toFXImage(GlobalProxy.INS.getBufferedImage(), writableImage));
        clipboard.setContent(clipboardContent);
    }

    public void imageToFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(String.format("greaCapture截图%tF-%<tH-%<tM-%<tS.jpg", new Date()));
            File saveFile = fileChooser.showSaveDialog(GlobalProxy.INS.getRootLayoutScene().getWindow());
            if (null != saveFile) {
                ImageIO.write(GlobalProxy.INS.getBufferedImage(), "JPEG", saveFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
