package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.Date;

public class StartScreenCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField createEventTextField;
    @FXML
    private TextField joinEventTextField;

    // the events
    @FXML
    private Button eventButton1;
    @FXML
    private Label eventLabel1;

    @FXML
    private Button eventButton2;
    @FXML
    private Label eventLabel2;

    @FXML
    private Button eventButton3;
    @FXML
    private Label eventLabel3;

    @FXML
    private Label noEventLabel;

    @FXML
    private ImageView imageView;

    private int eventCode = 1;

    @Inject
    public StartScreenCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    // list the 3 most recent events on the start page
    public void initialize() {
        // retrieve from database based on recency (now null to have something)
        // the commented below is for testing
        noEventLabel.setVisible(false);
        Event event1 = new Event("test event", new Date(10, 10, 2005), "Admin", "This is just for testing");
        Event event2 = null;
        Event event3 = null;
        setup(event1, eventButton1, eventLabel1);
        setup(event2, eventButton2, eventLabel2);
        setup(event3, eventButton3, eventLabel3);
        // this will be querified
        if (event1 == null && event2 == null && event3 == null) {
            noEventLabel.setVisible(true);
        }

        // Load the image
        Image image = new Image("Logo_.png"); // Path relative to your resources folder

        // Set the image to the ImageView
        imageView.setImage(image);

    }

    private void setup(Event event, Button button, Label label) {
        if (event == null) {
            button.setVisible(false);
            label.setVisible(false);
            return;
        }
        button.setVisible(true);
        label.setVisible(true);

        button.setOnAction(something -> {
            mainCtrl.showSplittyOverview(event.getId());
        });

        button.setText(event.getName());
        label.setText(event.getDate() + ": " + event.getDescription());
    }

    /**
     * Creates an event with the title specified in the createEventTextField
     * TO DO - actually create an event
     */
    public void createEvent(){
        String name = createEventTextField.getText();
        if (name == null || name.isEmpty()) {
            name = "New event";
        }
        mainCtrl.showCreateEvent(name);
        //TO DO: add event to database, fill in more information about the event.
        //This will happen in the CreateEventCtrl class!
    }

    /**
     * Join an event with the title specified in the joinEventTextField
     * TO DO - join an event by the event id/URL
     */
    public void joinEvent(){
        System.out.println("Joined event: " + joinEventTextField.getText());
        mainCtrl.showSplittyOverview(eventCode);
        //TO DO, this will happen here in this method
    }

    @FXML
    public void showAllEvents() {
        mainCtrl.showUserEventList();
    }

    public void setTitle(String eventTitle) {
        createEventTextField.setText(eventTitle);
    }


    public void showAdminLogin(ActionEvent actionEvent) {
        mainCtrl.showAdminLogin();
    }
}
