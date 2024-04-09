package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;



//import java.awt.*;
import java.util.NoSuchElementException;

public class EditEventCrtl {
    int eventId;

    @FXML
    private TextField nameChange;

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label eventNameText;
    @FXML
    private Label titleError;
    @FXML
    private Label oldEventnameText;
    @FXML
    private Label oldEventname;


    @FXML
    private Label succesFullyChanged;
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;




    @Inject
    public EditEventCrtl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void initialize() {
        mainCtrl.setButtonGreenProperty(submitButton);

    }
    public void reset(){
        this.succesFullyChanged.setVisible(false);
        this.nameChange.setText("");
    }
    public void back(){
        mainCtrl.showSplittyOverview(mainCtrl.getCurrentEventCode());
    }
    @FXML
    public void changeValues() {
        //possibly edit date and description
        String newName = nameChange.getText();
        if(newName == null || newName.isEmpty()){
            nameChange.setPromptText("please provide a name");
            return;
        }
        try{
            Event event = serverUtils.getEventById(eventId);
            Event updatedEvent = serverUtils.updateEvent(event, newName);
            if (updatedEvent == null){
                throw new RuntimeException();
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
        succesFullyChangeName();
    }

    public void setOldEventName(){
        this.oldEventname.setText(serverUtils.getEventById(eventId).getName());
    }


    public void succesFullyChangeName(){
        succesFullyChanged.setVisible(true);
    }

    public void setEventNameText(String txt){
        this.eventNameText.setText(txt);
    }
    public void setCreateButton(String txt){
        this.submitButton.setText(txt);
    }
    public void setCancelButton(String txt){
        this.cancelButton.setText(txt);
    }
    public void setTitleError(String txt){
        this.titleError.setText(txt);
    }
    public void setSuccesFullyChanged(String txt) {
        this.succesFullyChanged.setText(txt);
    }
    public void setOldEventnameText(String txt) {
        this.oldEventnameText.setText(txt);
    }
}
