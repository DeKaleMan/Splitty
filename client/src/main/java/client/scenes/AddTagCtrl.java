package client.scenes;

import client.utils.ServerUtils;
import commons.Expense;
import commons.Tag;
import commons.dto.TagDTO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTagCtrl {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
    private int eventId;
    private boolean addTag;
    private boolean addExpense;
    private Expense expense;
    private List<Tag> tags = new ArrayList<>();
    String oldName;

    @FXML
    public Label title;
    @FXML
    public Label unknownError;
    @FXML
    public Label duplicateName;
    @FXML
    public Label invalidName;
    @FXML
    public Label invalidColour;
    @FXML
    public TextField nameField;
    @FXML
    public TextField colourField;
    @FXML
    public Button applyChangesButton;
    @FXML
    public Button cancelButton;
    @Inject
    public AddTagCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    public void cancel() {
        mainCtrl.showManageTags(eventId, addExpense, expense);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            cancel();
        }
    }

    public void setFields(int eventId, boolean addExpense, Expense expense) {
        this.addExpense = addExpense;
        this.expense = expense;
        nameField.setEditable(true);
        addTag = true;
        nameField.setText("");
        colourField.setText("");
        this.eventId = eventId;
        setTitleText(mainCtrl.translate("Add Tag"));
    }
    public void setFields(Tag tag, int eventId, boolean addExpense, Expense expense) {
        this.addExpense = addExpense;
        this.expense = expense;
        String other = "Other";
        if (other.equals((tag.getName()))) {
            nameField.setEditable(false);
        } else {
            nameField.setEditable(true);
        }
        addTag = false;
        this.eventId = eventId;
        setTitleText(mainCtrl.translate("Edit Tag"));
        this.oldName = tag.getName();
        nameField.setText(tag.getName());
        colourField.setText(tag.getColour());
    }

    @FXML
    public void applyChanges() {
        if (addTag) {
            addTag();
        } else {
            editTag();
        }
    }

    public List<String> getTagNames() {
        tags = serverUtils.getTagsByEvent(eventId);
        return tags.stream().map(Tag::getName).toList();
    }

    private void editTag() {
        try {
            TagDTO t = getTagDTO();
            if (t == null) {
                return;
            }
            serverUtils.updateTag(t, oldName, eventId);
            mainCtrl.setConfirmationAddedTag();
            mainCtrl.showManageTags(eventId, addExpense, expense);
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }
    private void addTag() {
        try {
            TagDTO t = getTagDTO();
            if (t == null) {
                return;
            }
            serverUtils.addTag(t);
            mainCtrl.setConfirmationAddedTag();
            mainCtrl.showManageTags(eventId, addExpense, expense);
        } catch (RuntimeException e) {
            setPauseTransition(unknownError);
        }
    }

    public TagDTO getTagDTO() {
        boolean error = false;
        List<String> names = getTagNames();
        String name = nameField.getText();
        String colour = colourField.getText();
        if (name == null || name.isEmpty()) {
            error = true;
            setPauseTransition(invalidName);
        }
        if (names.contains(name)) {
            if (addTag) {
                setPauseTransition(duplicateName);
                error = true;
            } else {
                if (!oldName.equals(name)) {
                    error = true;
                    setPauseTransition(duplicateName);
                }
            }
        }
        if (!checkColour(colour)) {
            setPauseTransition(invalidColour);
            error = true;
        }
        if (error) {
            return null;
        }
        return new TagDTO(eventId, name, colour);
    }


    public boolean checkColour(String colour) {
        if (colour == null || colour.isEmpty()) {
            return false;
        }
        if (colour.length() != 4 && colour.length() != 7) {
            return false;
        }
        Pattern hexaPattern = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
        Matcher matcher = hexaPattern.matcher(colour);
        return matcher.matches();
    }

    public void setPauseTransition(Label l) {
        if (l == null) return;
        l.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event1 -> l.setVisible(false));
        pause.play();
    }

    public void setTitleText(String text) {
        title.setText(text);
    }
}
