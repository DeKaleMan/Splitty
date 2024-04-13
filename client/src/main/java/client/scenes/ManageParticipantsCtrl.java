package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageParticipantsCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private int eventId;
    @FXML
    public Button removeButton;
    @FXML
    public Button back;
    @FXML
    public Button editButton;
    @FXML
    public Button addButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label participantsText;

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
    public Label participantDeletedConfirmation;
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        setImages();
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

    private void setImages() {
        ImageView plus = new ImageView(new Image("plusicon.png"));
        plus.setFitWidth(15);
        plus.setFitHeight(15);
        addButton.setGraphic(plus);
        ImageView edit = new ImageView(new Image("editIcon.png"));
        edit.setFitWidth(15);
        edit.setFitHeight(15);
        editButton.setGraphic(edit);
        ImageView trash = new ImageView(new Image("trashIcon.png"));
        trash.setFitWidth(14);
        trash.setFitHeight(14);
        removeButton.setGraphic(trash);
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
            Participant selected = participantsList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                setPauseTransition(noParticipantSelectedError);
                return;
            }
            // only allow to delete if balance is 0
            if(selected.getBalance() != 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(mainCtrl.translate("Deleting Participant"));
                alert.setHeaderText(mainCtrl.translate("Cannot delete a participant"));
                alert.setContentText(mainCtrl.translate("Participant owes/is owed money."));
                alert.showAndWait();
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle(mainCtrl.translate("Deleting Participant"));
            confirmation.setContentText(mainCtrl.translate("Are you sure you want to delete ") + selected.getName()+
                    mainCtrl.translate(" from ") + titleLabel.getText() + "?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("remove" + selected);
                    remove(selected);
                    setPauseTransition(participantDeletedConfirmation);
                }
            });
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
    public void showInvitation(){
        mainCtrl.showInvitation(this.eventId);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            backEventOverview();
        }
    }

    public void editParticipant() {

        Participant selected = participantsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setPauseTransition(noParticipantSelectedError);
            return;
        }
        mainCtrl.showEditParticipant(eventId, selected.getUuid());

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

    public void setParticipantAddedConfirmation() {
        setPauseTransition(participantEditedConfirmation);
    }
    public void setParticipantEditedConfirmation() {
        setPauseTransition(participantEditedConfirmation);
    }

    public void editOnClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) {
            editParticipant();
        }
    }

    public void setEditButton(String txt){
        Platform.runLater(() -> {
            this.editButton.setText(txt);
        });
    }
    public void setRemoveButton(String txt) {
        Platform.runLater(() -> {
            this.removeButton.setText(txt);
        });
    }

    public void setAddButton(String txt) {
        Platform.runLater(() -> {
            this.addButton.setText(txt);
        });
    }

    public void setTitleLabel(String txt) {
        Platform.runLater(() -> {
            this.titleLabel.setText(txt);
        });
    }

    public void setParticipantsText(String txt) {
        Platform.runLater(() -> {
            this.participantsText.setText(txt);
        });
    }

    public void setUndo(String txt) {
        Platform.runLater(() -> {
            this.undo.setText(txt);
        });
    }

    public void setNoParticipantSelectedError(String txt) {
        Platform.runLater(() -> {
            this.noParticipantSelectedError.setText(txt);
        });
    }

    public void setUnknownError(String txt) {
        Platform.runLater(() -> {
            this.unknownError.setText(txt);
        });
    }

    public void setParticipantAddedConfirmation(String txt) {
        Platform.runLater(() -> {
            this.participantAddedConfirmation.setText(txt);
        });
    }

    public void setParticipantEditedConfirmation(String txt) {
        Platform.runLater(() -> {
            this.participantEditedConfirmation.setText(txt);
        });
    }

    public void setParticipantDeletedConfirmation(String txt) {
        Platform.runLater(() -> {
            this.participantDeletedConfirmation.setText(txt);
        });
    }
    public void setBackButton(String txt) {
        Platform.runLater(() -> {
            this.back.setText(txt);
        });
    }
}
