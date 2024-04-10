package client.scenes;

import client.utils.ServerUtils;
import commons.Tag;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ManageTagsCtrl implements Initializable {


    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
    private int eventId;
    @FXML
    public Label titleLabel;
    @FXML
    public ListView<Tag> tagListView;

    public List<Tag> tagList = new ArrayList<>();

    @FXML
    public Button editButton;
    @FXML
    public Button addButton;
    @FXML
    public Button removeButton;
    @FXML
    public Label noTagSelectedError;
    @FXML
    public Label unknownError;
    @FXML
    public Label tagAddedConfirmation;
    @FXML
    public Label tagEditedConfirmation;
    @FXML
    public Label tagDeletedConfirmation;
    @FXML
    public Label tagsLabel;
    @Inject
    public ManageTagsCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.setButtonRedProperty(removeButton);
        setTagListUp();
    }

    private void setTagListUp() {
        tagListView.setCellFactory(param -> new ListCell<Tag>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                } else {
                    setText(tag.getName());
                }
            }
        });
    }

    public void refreshList(int eventId) {
        this.eventId = eventId;
        titleLabel.setText(serverUtils.getEventById(eventId).getName());
        tagListView.getItems().clear();
        try{
            tagList = serverUtils.getTagsByEvent(eventId);
        } catch (RuntimeException e ){
            tagList = new ArrayList<>();
            System.out.println(e);
        }
        String other = "Other";
        Tag tag = tagList.stream().filter(t -> other.equals(t.getName())).toList().getFirst();
        tagList.remove(tag);
        tagListView.getItems().addAll(tagList);
    }

    @FXML
    private void editTag() {
        Tag selected = tagListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setPauseTransition(noTagSelectedError);
            return;
        }
        mainCtrl.showAddTag(selected, eventId);
    }
    @FXML
    public void addTag() {
        mainCtrl.showAddTag(eventId);
    }
    @FXML
    public void removeTag() {
        try {
            Tag selected = tagListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                setPauseTransition(noTagSelectedError);
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle(mainCtrl.translate("Deleting Tag"));
            confirmation.setContentText(mainCtrl.translate("Are you sure you want to delete the tag: ")
                    + selected.getName());
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("remove" + selected);
                    remove(selected);
                    setPauseTransition(tagDeletedConfirmation);
                }
            });
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }

    public void remove(Tag tag) {
        try {
            serverUtils.deleteTag(eventId, tag.getName());
            tagListView.getItems().remove(tag);
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }
    public void back() {
        mainCtrl.showAddExpense(eventId);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }
    @FXML
    public void editOnClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) {
            editTag();
        }
    }

    public void setPauseTransition(Label l) {
        if (l == null) return;
        l.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event1 -> l.setVisible(false));
        pause.play();
    }

    public void setAddedTagConfirmation() {
        setPauseTransition(tagAddedConfirmation);
    }

    public void setEditedTagConfirmation() {
        setPauseTransition(tagEditedConfirmation);
    }
}
