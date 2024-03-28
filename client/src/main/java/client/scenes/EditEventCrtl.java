package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.EventDTO;
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

    private void setUp(Button submitButton){
        submitButton.setVisible(true);
    }

    public void back(){
        mainCtrl.showSplittyOverview(eventCode);
    }

    public void changeName(ActionEvent actionEvent) {
        String newName = nameChange.getText();
        String idTemp = eventId.getText();
        int eventId = Integer.parseInt(idTemp);
        if(newName == null || newName.isEmpty()){
            nameChange.setText("please provide a name");
            throw new NoSuchElementException();
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

    public void succesFullyChangeName(){
        succesFullyChanged.setVisible(true);
    }
}
