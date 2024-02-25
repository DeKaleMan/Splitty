package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class StartScreenCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField createEventTextField;
    @FXML
    private TextField joinEventTextField;

    @Inject
    public StartScreenCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Creates an event with the title specified in the createEventTextField
     * TO DO - actually create an event
     */
    public void createEvent(){
        System.out.println("Created event: " + createEventTextField.getText());
        mainCtrl.showSplittyOverview(createEventTextField.getText());
        //TO DO
    }

    /**
     * Join an event with the title specified in the joinEventTextField
     * TO DO - join an event by the event id/URL
     */
    public void joinEvent(){
        System.out.println("Joined event: " + joinEventTextField.getText());
        mainCtrl.showSplittyOverview(joinEventTextField.getText());
        //TO DO
    }
}
