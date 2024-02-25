package client.scenes;

import client.utils.ServerUtils;
import commons.Person;
import commons.Quote;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SplittyOverviewCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private Button sendInvites;
    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    public void sendInvitesOnClick(){
        mainCtrl.showInvitation();
    }

}

