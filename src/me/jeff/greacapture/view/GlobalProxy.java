package me.jeff.greacapture.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Jeff Chou
 *         date 2017/4/22 0022
 */
public enum GlobalProxy {
    INS;

    private Scene rootLayoutScene;
    private Stage primaryStage;
    private BufferedImage bufferedImage;

    public GlobalProxy initRooLayout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GlobalProxy.class.getResource("RootLayout.fxml"));
            BorderPane rootLayout = fxmlLoader.load();
            rootLayoutScene = new Scene(rootLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  this;
    }

    public Scene getRootLayoutScene() {
        return rootLayoutScene;
    }

    public void showPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(rootLayoutScene);
        this.primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showToolBar() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GlobalProxy.class.getResource("ToolBar.fxml"));
            ToolBar toolBar = fxmlLoader.load();
            Scene scene = new Scene(toolBar);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
