package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageParticipantsCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    public Button removeButton;
    @FXML
    private Label titleLabel;

    private int eventCode;

    @FXML
    private ListView participantsList;

    @FXML
    private Button undo;
    private Participant undone;
    @FXML
    private Button sendInvites;

    private List<Participant> list;
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.setButtonRedProperty(removeButton);
        //for now this is hardcoded but this should eventually be passed on
        this.list = new ArrayList<>();
        participantsList.setCellFactory(param -> new ListCell<Participant>(){
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        try{
            list = serverUtils.getParticipants(eventCode);
        }catch (Exception e){
            list = new ArrayList<>();
            System.out.println(e);
        }
        participantsList.getItems().addAll(list);
    }

    @Inject
    public ManageParticipantsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void backEventOverview(){
        mainCtrl.showSplittyOverview(eventCode);
    }

    /**
     * removes the participant from the list
     */
    @FXML
    public void removeParticipantFromList(){
        if(participantsList == null || participantsList.getItems().isEmpty()) System.out.println("empty list");
        else{
            ObservableList selected = participantsList.getSelectionModel().getSelectedItems();
            if(selected.isEmpty()) {
                System.out.println("none selected");
                return;
            }
            System.out.println("remove" + selected);
            remove((String) selected.getFirst());
        }

    }

    public void remove(String p){
        //make get the person linked to this name and delete from db
        participantsList.getItems().remove(p);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }


    @FXML
    public void showInvitation(){
        mainCtrl.showInvitation(this.eventCode);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            backEventOverview();
        }
    }
    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }
}
