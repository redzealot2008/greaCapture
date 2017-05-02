package me.jeff.greacapture.view;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Jeff Chou
 *         date 2017/4/22 0022
 */
public class RootLayoutController {

    public RootLayoutController() {
    }

    public void fullScreenCapture() {
        // 获取系统屏幕尺寸
//        Rectangle2D rectScreen = Screen.getPrimary().getBounds();
//        pictureCapture(new Rectangle((int) rectScreen.getMinX(), (int) rectScreen.getMinY(), (int) rectScreen.getWidth(), (int) rectScreen.getHeight()));
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        pictureCapture(new Rectangle(screenDimension));
    }

    public void windowCapture() {
        //TODO 方案1：用户选择窗口截图之后，用户通过鼠标选择需要截图的窗口，未能实现，目前思路是构建一个系统级全局鼠标监控钩子。如何判断一个窗口是被鼠标激活的？
        //方案2：用户选择窗口截图之后，弹出所有窗口列表供用户选择。
        getTaskbarWindows();
        showWindowsDialog();
//        GlobalProxy.INS.getPrimaryStage().addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() == 2) {
//                    RECT rect = getSelectedWindow();
//                    pictureCapture(rect);
//                }
//            }
//        });
    }

    public void areaCapture() {
        // 监听鼠标拖放事件，确定用户框选区域位置和大小，在鼠标拖放期间绘制选框，鼠标释放后显示工具条，允许右击鼠标重新框选。
        Stage stage = new Stage();
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        Pane pane = new Pane();
        pane.setStyle("-fx-background:transparent;");
        Scene scene = new Scene(pane);
        scene.setFill(null);

        stage.setScene(scene);
        stage.setOpacity(0.2d);
        stage.show();
    }

    private void pictureCapture(Rectangle rect) {
        try {
            //定义机器人
            Robot robot = new Robot();
            // 截屏缓存
            GlobalProxy.INS.setBufferedImage(robot.createScreenCapture(rect));
            //最小化程序窗口
            minimize();
            //显示工具条
            showToolBar();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void fullVideo() {

    }

    public void windowVideo() {

    }

    public void areaVideo() {

    }

    private void minimize() {
        GlobalProxy.INS.getPrimaryStage().setIconified(true);
    }

    private void showToolBar() {
        GlobalProxy.INS.showToolBar();
    }

    /**
     * 使用JNA调用本地方法获取当前激活窗口坐标以及大小
     *
     * @return RECT
     */
    private RECT getSelectedWindow() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        RECT rect = new RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);
        return rect;
    }

    /**
     * 获取任务栏窗口
     */
    private void getTaskbarWindows() {
//        HWND hwnd = User32.INSTANCE.GetDesktopWindow();
//        hwnd = User32.INSTANCE.GetWindow(hwnd, new WinDef.DWORD(WinUser.GW_CHILD));
//        char[] buf = new char[200];
//        int num = 1;
//        while (null != hwnd) {
//            User32.INSTANCE.GetWindowText(hwnd, buf, 200);
//            if (User32.INSTANCE.IsWindowVisible(hwnd)) {
//                System.out.println(num++ + ":" + String.copyValueOf(buf));
//            }
//            hwnd = User32.INSTANCE.GetWindow(hwnd, new WinDef.DWORD(WinUser.GW_HWNDNEXT));
//        }
        windowTextList.clear();
        hwndList.clear();
        User32.INSTANCE.EnumWindows(new EnumTaskbarWnds(), null);
    }

    /**
     * 弹出窗口列表对话框
     */
    private void showWindowsDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("请选择截图窗口");
        ListView listView = new ListView();
        listView.setItems(FXCollections.observableArrayList(windowTextList));
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!listView.contains(event.getSceneX(), event.getSceneY())) {
                    listView.getSelectionModel().clearSelection();
                    return;
                }
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < windowTextList.size()) {
                    HWND selectedWindow = hwndList.get(listView.getSelectionModel().getSelectedIndex());
                    User32.INSTANCE.SetForegroundWindow(selectedWindow);
                    RECT rect = new RECT();
                    User32.INSTANCE.GetWindowRect(selectedWindow, rect);
                    pictureCapture(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
                    dialog.setResult(ButtonType.CLOSE);
                    dialog.close();
                }
            }
        });
        dialog.getDialogPane().setContent(listView);
        dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                ((Dialog) event.getSource()).hide();
            }
        });
        dialog.showAndWait();
    }

    private ArrayList<String> windowTextList = new ArrayList<String>();
    private ArrayList<HWND> hwndList = new ArrayList<HWND>();

    class EnumTaskbarWnds implements WinUser.WNDENUMPROC {

        @Override
        public boolean callback(HWND hwnd, Pointer pointer) {
            if (null == User32.INSTANCE.GetWindow(hwnd, new WinDef.DWORD(WinUser.GW_OWNER)) && User32.INSTANCE.IsWindowVisible(hwnd)) {
                char[] windowText = new char[200];
                User32.INSTANCE.GetWindowText(hwnd, windowText, 200);
                char[] className = new char[200];
                User32.INSTANCE.GetClassName(hwnd, className, 200);
                if ((!"Shell_TrayWnd".equals(String.valueOf(className).trim())) && (!"Progman".equals(String.valueOf(className).trim()))) {
                    windowTextList.add(String.copyValueOf(windowText));
                    hwndList.add(hwnd);
                    System.out.println(String.copyValueOf(windowText) + "=============" + String.copyValueOf(className));
                }
            }
            return true;
        }
    }

}
