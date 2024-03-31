package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;



//import java.awt.*;
import java.util.NoSuchElementException;

public class EditEventCrtl {

    @FXML
    private TextField nameChange;
    @FXML
    private TextField eventId;

    @FXML
    private Button submitButton;

    @FXML
    private Label succesFullyChanged;
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    //TODO make eventCode not hardcoded
    private int eventCode = 1;



    @Inject
    public EditEventCrtl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        mainCtrl.setButtonGreenProperty(submitButton);
    }
    private void setUp(Button submitButton){
        submitButton.setVisible(true);
    }

    public void back(){
        succesFullyChanged.setVisible(false);
        mainCtrl.showSplittyOverview(eventCode);
    }

    public void changeName(ActionEvent actionEvent) {
        String newName = nameChange.getText();
        String idTemp = eventId.getText();
        int eventIdd;
        try{
            eventIdd = Integer.parseInt(idTemp);
        } catch (NumberFormatException ex){
            eventIdd = -1;
            eventId.setText("Event ID must be an integer");
            return;
        }
        if(newName == null || newName.isEmpty()){
            nameChange.setText("please provide a name");
            throw new NoSuchElementException();
        }
        try{
            Event event = serverUtils.getEventById(eventIdd);
            Event updatedEvent = serverUtils.updateEvent(event, newName);
            if (updatedEvent == null){
                throw new RuntimeException();
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
        succesFullyChangeName();
        nameChange.setText("");
        eventId.setText("");
    }

    public void succesFullyChangeName(){
        succesFullyChanged.setVisible(true);
    }
}
