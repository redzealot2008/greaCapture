package me.jeff.greacapture;/**
 * 功能：主要分两大功能：截图和录像。每个功能又分全屏捕捉，窗口捕捉，区域捕捉。截图缓存系统剪切板或保存到本地硬盘，录制屏幕鼠标键盘操作并保存到本地硬盘以供回放，录制过程中可以截图
 * 1.全屏捕捉思路：点击全屏捕捉按钮之后，弹出对话框询问用户是否最小化本程序，并在屏幕右下角浮动操作工具条
 * 2.窗口捕捉思路：点击窗口捕捉按钮之后，弹出对话框询问用户是否最小化本程序，用户自行选定需要捕捉的窗口，并在屏幕右下角浮动操作工具条
 * 3.区域捕捉思路：点击区域捕捉按钮之后，弹出对话框询问用户是否最小化本程序，用户用鼠标划定捕捉范围，并在屏幕右下角浮动操作工具条
 *
 * @author Jeff Chou
 * date 2017/4/22 0022
 */

import javafx.application.Application;
import javafx.stage.Stage;
import me.jeff.greacapture.view.GlobalProxy;

public class Main extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("greaCapture");

        GlobalProxy.INS.initRooLayout().showPrimaryStage(primaryStage);
    }

}
