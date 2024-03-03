package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;

import java.awt.*;

import static java.awt.Color.yellow;

public class UserEventListCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;



    @FXML
    private Button cookie;


    @Inject
    public UserEventListCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }


//    @FXML
//    private void changeColour() {
//        boolean green = false;
//        boolean red = false;
//        boolean blue = false;
//        boolean pink = false;
//        boolean yellow = true;
//
//        for (int i = 0; i < 5; i++) {
//            i = i - 1;
//
//            if (yellow) {
//                yellow = false;
//                pink = true;
//                cookies.setForeground(Color.yellow);
//            }
//            if (pink) {
//                pink = false;
//                green = true;
//                cookies.setForeground(Color.pink);
//            }
//            if (green) {
//                green = false;
//                red = true;
//                cookies.setForeground(Color.green);
//            }
//            if (red) {
//                red = false;
//                blue = true;
//                cookies.setForeground(Color.red);
//            }
//            if (blue) {
//                blue = false;
//                yellow = true;
//
//            }
//
//        }
//    }


}
