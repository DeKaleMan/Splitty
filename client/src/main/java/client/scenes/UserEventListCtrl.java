package client.scenes;

import client.utils.ServerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class UserEventListCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField searchbar;

    private ObservableList<String> filters = FXCollections.observableArrayList(
            "Last activity", "Title", "Creation date");

    @FXML
    private ChoiceBox<String> choiceBox;

    @Inject
    public UserEventListCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        choiceBox.setItems(filters);
    }

    public void reset(){
        this.searchbar.setText(null);
    }
    @FXML
    public void back() {
        mainCtrl.showStartScreen();
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }
}
