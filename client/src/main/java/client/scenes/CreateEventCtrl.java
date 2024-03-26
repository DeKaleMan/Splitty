package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateEventCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

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
    public CreateEventCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        participants = new ArrayList<>();

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
        String description = eventDescriptionArea.getText();
        Date date;

        boolean error = false;
        try {
            String[] dateArr = dateString.split("-");
            date = new Date(Integer.parseInt(dateArr[2]) - 1900,
                    Integer.parseInt(dateArr[1]) - 1,
                    Integer.parseInt(dateArr[0]));
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
        mainCtrl.showStartScreen();
        System.out.println("Created new event: " + name);
        // create new event and add to database, go to that event overview and add participants via database.
    }
}
