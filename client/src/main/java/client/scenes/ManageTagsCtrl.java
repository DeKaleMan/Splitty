package client.scenes;

import client.utils.ServerUtils;
import commons.Expense;
import commons.Tag;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
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
    private boolean addExpense;
    private Expense expense;
    public List<Tag> tagList = new ArrayList<>();
    @FXML
    public Label titleLabel;
    @FXML
    public ListView<Tag> tagListView;

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
    public Label otherSelectedError;
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
                if (empty || tag == null || tag.getName().isEmpty()) {
                    setText(null);
                    setBackground(Background.EMPTY);
                } else {
                    setText(tag.getName());
                    if (tag.getColour() == null || tag.getColour().isEmpty()) {
                        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                        if (isSelected()) {
                            setBackground(new Background(new BackgroundFill(Color.BLUE, null, null))); // Darken the color when selected
                            setTextFill(Color.WHITE);
                        } else {
                            setTextFill(Color.BLACK);
                        }
                        return;
                    }
                    Color color = Color.web(tag.getColour());
                    setBackground(new Background(new BackgroundFill(color, null, null)));
                    if (isSelected()) {
                        setBackground(new Background(new BackgroundFill(Color.BLUE, null, null))); // Darken the color when selected
                        setTextFill(Color.WHITE);
                    } else {
                        setTextFill(color.getBrightness() < 0.5 ? Color.WHITE : Color.BLACK);
                    }
                }
            }
        });
    }

    public void refreshList(int eventId, boolean addExpense, Expense expense) {
        this.expense = expense;
        this.addExpense = addExpense;
        this.eventId = eventId;
        setTagListUp();
        titleLabel.setText(serverUtils.getEventById(eventId).getName());
        tagListView.getItems().clear();
        try{
            tagList = serverUtils.getTagsByEvent(eventId);
        } catch (RuntimeException e ){
            tagList = new ArrayList<>();
            System.out.println(e);
        }
        tagListView.getItems().addAll(tagList);
    }

    @FXML
    private void editTag() {
        Tag selected = tagListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setPauseTransition(noTagSelectedError);
            return;
        }
        mainCtrl.showAddTag(selected, eventId, addExpense, expense);
    }
    @FXML
    public void addTag() {
        mainCtrl.showAddTag(eventId, addExpense, expense);
    }
    @FXML
    public void removeTag() {
        try {
            Tag selected = tagListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                setPauseTransition(noTagSelectedError);
                return;
            }
            String other = "Other";
            if (other.equals(selected.getName())) {
                throw new IllegalArgumentException();
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle(mainCtrl.translate("Deleting Tag"));
            confirmation.setContentText(mainCtrl.translate("Are you sure you want to delete the tag: ")
                    + selected.getName());
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("remove" + selected);
                    if (remove(selected)) {
                        setPauseTransition(tagDeletedConfirmation);
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            setPauseTransition(otherSelectedError);
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }

    public boolean remove(Tag tag) {
        try {
            serverUtils.deleteTag(eventId, tag.getName());
            tagListView.getItems().remove(tag);
            return true;
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
            return false;
        }
    }
    public void back() {
        if (addExpense) {
            mainCtrl.showAddExpense(eventId);
        } else {
            mainCtrl.showEditExpense(expense);
        }
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
