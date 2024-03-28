package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEventCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;

    // event text fields
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea eventDescriptionArea;


    // participant text fields

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField iBanField;
    @FXML
    private TextField bICField;
    @FXML
    private TextField accountHolderField;

    // this list will store all added participants until
    // the create event button is clicked, then it will be added to the database
    // via foreign keys
    private List<Participant> participants;

    @Inject
    public CreateEventCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        participants = new ArrayList<>();
        this.config = config;

    }


    public void setTitle(String title) {
        titleField.setText(title);
    }

    @FXML
    public void cancel() {
        mainCtrl.showStartScreen(titleField.getText());
    }

    @FXML
    public void addParticipant() {
        String name = nameField.getText();
        String email = emailField.getText();
        String iBan = iBanField.getText();
        String bIC = bICField.getText();
        String accountHolder = accountHolderField.getText();

        if     (name == null || name.isEmpty() ||
                email == null || email.isEmpty() ||
                iBan == null || iBan.isEmpty() ||
                bIC == null || bIC.isEmpty() ||
                accountHolder == null || accountHolder.isEmpty()) {
            // give warning *every field must be filled in*
            return;
        }
//        Participant participant = new Participant(name, 0.0, iBan, bIC, accountHolder, email);
//        participants.add(participant);

        nameField.setText("");
        emailField.setText("");
        iBanField.setText("");
        bICField.setText("");
        accountHolderField.setText("");
    }

    @FXML
    public void createEvent() {
        String name = titleField.getText();
        String dateString = datePicker.getEditor().getText();
        if(dateString.isBlank()||dateString.isEmpty()) {
            datePicker.setPromptText("Invalid date");
            datePicker.setValue(null);
            return;
        }
        if(name.isEmpty()||name.isBlank()){
            titleField.setPromptText("invalid name");
            titleField.setText("");
            return;
        }
        LocalDate localDate = datePicker.getValue();
        String description = eventDescriptionArea.getText();
        Date date = new Date();
        boolean error = false;
        try {
//            String[] dateArr = dateString.split("-");
//            date = new Date(Integer.parseInt(dateArr[2]) - 1900,
//                    Integer.parseInt(dateArr[1]) - 1,
//                    Integer.parseInt(dateArr[0]));
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        } catch (Exception e) {
            error = true;
            // error message invalid date, use eu format dd/mm/yyyy
        }
        if (name == null || name.isEmpty() || error){
            if (name == null || name.isEmpty()) {
                // error message *fill in name*
            }
            return;
        }
        //fetch owner
        String owner = config.getId();

        //System.out.println("Created new event: " + name);
        // create new event and add to database, go to that event overview and add participants via database.
        Event event = new Event(name, date, owner, description);
        Event eventCreated = serverUtils.addEvent(event);
        mainCtrl.showSplittyOverview(eventCreated.getId());
    }
}
