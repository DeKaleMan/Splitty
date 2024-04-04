package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.AdminWindows;
import client.utils.Config;
import client.utils.EventPropGrouper;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;


public class ServerCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Config config;

    boolean noConnection = false;
    @FXML
    public Label startupNotification;
    @FXML
    public Label notConnectedError;
    @FXML
    public ImageView imageView;
    @FXML
    public TextField serverField;
    @FXML
    public Button connectButton;
    @FXML
    public Button backButton;

    @Inject
    public ServerCtrl(MainCtrl mainCtrl, Config config) {
        this.mainCtrl = mainCtrl;
        this.config = config;
    }


    public void setFields(boolean noConnection) {
        this.noConnection = noConnection;
        if (config.getConnection() != null) {
            serverField.setText(config.getConnection());
        }
        if (noConnection) {
            startupNotification.setVisible(true);
            imageView.setImage(new Image("no-connection.png"));
            backButton.setText("Settings");
        } else {
            startupNotification.setVisible(false);
            imageView.setImage(new Image("connection2.png"));
            backButton.setText("Back");
        }
    }

    public void connect() {
        notConnectedError.setVisible(false);
        try {
            ServerUtils.serverDomain = serverField.getText();
            ServerUtils.resetServer();
            serverUtils = new ServerUtils();
            config.setConnection(serverField.getText());
            config.write();
            relaunch();
            mainCtrl.closeStage();
            noConnection = false;
        } catch (RuntimeException e) {
            notConnectedError.setVisible(true);
            noConnection = true;
            setFields(true);
        }
    }

    private void relaunch() throws RuntimeException {
        Injector injector = createInjector(new MyModule());
        MyFXML fxml = new MyFXML(injector);
        var editParticipant = fxml.load(EditParticipantCtrl.class, "client", "scenes", "EditParticipant.fxml");
        var addParticipant = fxml.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
        var server = fxml.load(ServerCtrl.class, "client", "scenes", "Server.fxml");
        var settings = fxml.load(SettingsCtrl.class, "client", "scenes", "Settings.fxml");
        var invitation = fxml.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var splittyOverview = fxml.load(SplittyOverviewCtrl.class, "client", "scenes", "SplittyOverview.fxml");
        var startScreen = fxml.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var contactDetails = fxml.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
        var userEventList = fxml.load(UserEventListCtrl.class, "client", "scenes", "UserEventList.fxml");
        var createEvent = fxml.load(CreateEventCtrl.class, "client", "scenes", "createEvent.fxml");
        var addExpense = fxml.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var manageParticipants = fxml.load(ManageParticipantsCtrl.class, "client", "scenes", "ManageParticipants.fxml");
        var statistics = fxml.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");
        var debts = fxml.load(DebtCtrl.class, "client", "scenes", "Debts.fxml");
        var editEvent = fxml.load(EditEventCrtl.class, "client", "scenes", "EditEvent.fxml");
        var editExpense = fxml.load(EditExpenseCtrl.class, "client", "scenes", "EditExpense.fxml");
        // group these in the EventPropGrouper
        var eventPropGrouper = new EventPropGrouper(addExpense, addParticipant, editParticipant,
                statistics, debts,editEvent, editExpense, manageParticipants);

        var adminLogin = fxml.load(AdminLoginCtrl.class, "client", "scenes", "AdminLogin.fxml");
        var adminOverview = fxml.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
        var adminWindows = new AdminWindows(adminLogin, adminOverview);
        var mainCtrl = injector.getInstance(MainCtrl.class);
        mainCtrl.initialize(new Stage(), invitation,splittyOverview,
                startScreen, contactDetails, eventPropGrouper, userEventList,
                createEvent, adminWindows, settings, server);

    }

    public void back() {
        mainCtrl.showSettings(noConnection);
    }
    public void resetError(KeyEvent keyEvent) {
        notConnectedError.setVisible(false);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }
}
