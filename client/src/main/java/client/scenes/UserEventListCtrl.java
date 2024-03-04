package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

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

    @FXML
    public void back() {
        mainCtrl.showStartScreen();
    }


}
