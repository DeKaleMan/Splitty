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
package client.scenes;

import client.utils.AdminWindows;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private Scene invitation;
    private InvitationCtrl invitationCtrl;

    private Scene splittyOverview;
    private SplittyOverviewCtrl splittyOverviewCtrl;

    private Scene startScreen;
    private StartScreenCtrl startScreenCtrl;

    private Scene addExpense;
    private AddExpenseCtrl addExpenseCtrl ;

    private Scene contactDetails;
    private ContactDetailsCtrl contactDetailsCtrl;

    private Scene adminLogin;
    private AdminLoginCtrl adminLoginCtrl;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add, Pair<InvitationCtrl, Parent> invitation,
                           Pair<SplittyOverviewCtrl, Parent> splittyOverview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<ContactDetailsCtrl, Parent> contactDetails,
                           AdminWindows adminWindows){
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());

        this.splittyOverviewCtrl = splittyOverview.getKey();
        this.splittyOverview = new Scene(splittyOverview.getValue());

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        this.contactDetailsCtrl = contactDetails.getKey();
        this.contactDetails = new Scene(contactDetails.getValue());

        this.adminLoginCtrl = adminWindows.adminLogin().getKey();
        this.adminLogin = new Scene(adminWindows.adminLogin().getValue());

        showStartScreen();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showSplittyOverview(String title){
        primaryStage.setTitle("Event overview");
        primaryStage.setScene(splittyOverview);
        splittyOverviewCtrl.setTitle(title);
    }

    public void showAddExpense(String title) {
        primaryStage.setTitle("Add expense");
        primaryStage.setScene(addExpense);
        addExpenseCtrl.setTitle(title);
    }

    public void showInvitation(String title){
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
        invitationCtrl.showInviteCode();
        invitationCtrl.setTitle(title);
    }

    public void showAdminLogin() {
        primaryStage.setTitle("Server management login");
        primaryStage.setScene(adminLogin);
    }

    public void showStartScreen(){
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(startScreen);
    }


}