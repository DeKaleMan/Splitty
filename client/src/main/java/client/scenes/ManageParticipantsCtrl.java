package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

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

    private int eventId;

    @FXML
    private ListView<Participant> participantsList;

    @FXML
    private Button undo;
    private Participant undone;

    private List<Participant> list;

    @FXML
    public Label noParticipantSelectedError;
    @FXML
    public Label unknownError;
    @FXML
    public Label participantAddedConfirmation;
    @FXML
    public Label participantEditedConfirmation;
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
                    if (item.getName() == null) {
                        setText("unknown");
                    } else {
                        setText(item.getName());
                    }
                }
            }
        });
    }

    @Inject
    public ManageParticipantsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void backEventOverview(){
        mainCtrl.showSplittyOverview(eventId);
    }

    /**
     * removes the participant from the list
     */
    @FXML
    public void removeParticipant() {
        try {
            if (participantsList == null || participantsList.getItems().isEmpty()) System.out.println("empty list");
            else {
                Participant selected = participantsList.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    setPauseTransition(noParticipantSelectedError);
                    return;
                }
                System.out.println("remove" + selected);
                remove(selected);
            }
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }

    public void setupParticipants(int id) {
        this.eventId = id;
        titleLabel.setText(serverUtils.getEventById(id).getName());
        participantsList.getItems().clear();
        try{
            list = serverUtils.getParticipants(eventId); // only the ghost participants can be edited
            list = list.stream().filter(Participant::isGhost).toList();
        } catch (RuntimeException e){
            list = new ArrayList<>();
            System.out.println(e);
        }
        participantsList.getItems().addAll(list);
    }

    public void remove(Participant p) {
        //make get the person linked to this name and delete from db
        try {
            serverUtils.deleteParticipant(p.getUuid(), eventId);
            participantsList.getItems().remove(p);
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void addToList(Participant p) {
        list.add(p);
        participantsList.getItems().add(p);
        setPauseTransition(participantAddedConfirmation);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            backEventOverview();
        }
    }

    public void editParticipant() {
        if (participantsList == null || participantsList.getItems().isEmpty()) System.out.println("empty list");
        else {
            Participant selected = participantsList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                setPauseTransition(noParticipantSelectedError);
                return;
            }
            mainCtrl.showEditParticipant(eventId, selected.getUuid());
        }
    }

    public void addParticipant() {
        mainCtrl.showAddParticipant(eventId);
    }

    public void setPauseTransition(Label l) {
        if (l == null) return;
        l.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event1 -> l.setVisible(false));
        pause.play();
    }
}
