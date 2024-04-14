/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.utils.AdminWindows;
import client.utils.EventPropGrouper;
import client.utils.TagsGrouper;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage){
        Platform.setImplicitExit(true);
        var server = FXML.load(ServerCtrl.class, "client", "scenes", "Server.fxml");
        var settings = FXML.load(SettingsCtrl.class, "client", "scenes", "Settings.fxml");
        try {
            var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
            var editParticipant = FXML.load(EditParticipantCtrl.class, "client", "scenes", "EditParticipant.fxml");
            var addParticipant = FXML.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
            var splittyOverview = FXML.load(SplittyOverviewCtrl.class, "client", "scenes", "SplittyOverview.fxml");
            var startScreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
            var contactDetails = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
            var userEventList = FXML.load(UserEventListCtrl.class, "client", "scenes", "UserEventList.fxml");
            var createEvent = FXML.load(CreateEventCtrl.class, "client", "scenes", "createEvent.fxml");
            var addExpense = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
            var manageParticipants = FXML.load(ManageParticipantsCtrl.class,
                    "client", "scenes", "ManageParticipants.fxml");
            var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");
            var debts = FXML.load(DebtCtrl.class, "client", "scenes", "Debts.fxml");
            var editEvent = FXML.load(EditEventCrtl.class, "client", "scenes", "EditEvent.fxml");
            var editExpense = FXML.load(EditExpenseCtrl.class,"client", "scenes", "EditExpense.fxml");
            // group these in the EventPropGrouper
            var eventPropGrouper = new EventPropGrouper(addExpense, addParticipant, editParticipant,
                    statistics, debts,editEvent, editExpense, manageParticipants);
            var addTag = FXML.load(AddTagCtrl.class, "client", "scenes", "AddTag.fxml");
            var manageTags = FXML.load(ManageTagsCtrl.class, "client", "scenes", "ManageTags.fxml");
            var tagsGrouper = new TagsGrouper(addTag, manageTags);
            var adminLogin = FXML.load(AdminLoginCtrl.class, "client", "scenes", "AdminLogin.fxml");
            var adminOverview = FXML.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
            var adminWindows = new AdminWindows(adminLogin, adminOverview);
            var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
            mainCtrl.initialize(primaryStage, invitation,splittyOverview,
                    startScreen, contactDetails, eventPropGrouper, userEventList,
                    createEvent, adminWindows, settings, server, tagsGrouper);
            primaryStage.setOnCloseRequest((event) -> mainCtrl.stopLongPolling());

        } catch (Exception e) {
            var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
            mainCtrl.initialize(primaryStage, server, settings);
            primaryStage.setOnCloseRequest((event) -> mainCtrl.stopLongPolling());
        }

    }
}