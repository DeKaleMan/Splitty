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

import client.utils.ServerUtils;
import client.utils.SetLanguage;
import commons.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainCtrlTest {

    @Spy
    private MainCtrl sut;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void showStartScreen() {
        Stage stage = mock(Stage.class);
        Scene startScreen = mock(Scene.class);
        StartScreenCtrl startScreenCtrl = mock(StartScreenCtrl.class);
        sut.setStartScreen(startScreen);
        sut.setStartScreenCtrl(startScreenCtrl);
        sut.setPrimaryStage(stage);

        sut.showStartScreen();

        verify(stage).setTitle("Splitty");
        verify(stage).setScene(startScreen);
        verify(startScreenCtrl).fetchList();
        verify(startScreenCtrl).setNoEventsError(false);
    }

    @Test
    public void showStartScreenException() {
        Stage stage = mock(Stage.class);
        Scene startScreen = mock(Scene.class);
        StartScreenCtrl startScreenCtrl = mock(StartScreenCtrl.class);
        doThrow(RuntimeException.class).when(startScreenCtrl).fetchList();
        sut.setStartScreen(startScreen);
        sut.setStartScreenCtrl(startScreenCtrl);
        sut.setPrimaryStage(stage);

        sut.showStartScreen();

        verify(stage).setTitle("Splitty");
        verify(stage).setScene(startScreen);
        verify(startScreenCtrl).setNoEventsError(true);
    }

    @Test
    void testGetStage() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);

        assertSame(stage, sut.getPrimaryStage());
    }

    @Test
    void showServerStartup() {
        ServerCtrl serverCtrl = mock(ServerCtrl.class);
        sut.setServerCtrl(serverCtrl);
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        Scene server = mock(Scene.class);
        sut.setServer(server);

        sut.showServerStartup(true);

        verify(stage).setScene(server);
        verify(stage).setTitle("Server");
        verify(serverCtrl).setFields(true);
    }

    @Test
    void addLang() {
        SetLanguage setLanguage = mock(SetLanguage.class);
        sut.setSetLanguage(setLanguage);

        sut.addLang("new");

        verify(setLanguage).addLang("new");
    }

    @Test
    void changeLanguage() {
        SetLanguage setLanguage = mock(SetLanguage.class);
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        StartScreenCtrl startScreenCtrl = mock(StartScreenCtrl.class);

        sut.setSetLanguage(setLanguage);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);
        sut.setStartScreenCtrl(startScreenCtrl);

        sut.changeLanguage("new");

        assertEquals("new", sut.getLanguage());
        verify(setLanguage).changeTo("new");
        verify(splittyOverviewCtrl).setLanguageSelect();
        verify(startScreenCtrl).setLanguageSelect();
    }

    @Test
    void translateEN() {
        sut.setLanguage("en");
        String query = "English sentence";
        assertEquals(query, sut.translate(query));
    }

    @Test
    void translateNotEN() {
        sut.setLanguage("other");
        SetLanguage setLanguage = mock(SetLanguage.class);
        sut.setSetLanguage(setLanguage);
        String query = "English sentence";
        when(setLanguage.translate(anyString(), anyString(), anyString())).thenReturn(
            "translation");
        assertEquals("translation", sut.translate(query));
        verify(setLanguage).translate(query, "en", "other");
    }

    @Test
    void showSplittyOverview() {
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        Event event = mock(Event.class);
        when(serverUtils.getEventById(anyInt())).thenReturn(event);
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);
        Scene overview = mock(Scene.class);
        sut.setSplittyOverview(overview);
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);

        sut.showSplittyOverview(1);

        verify(splittyOverviewCtrl).initializeAll(event);
        verify(splittyOverviewCtrl).setEventCode(1);
        verify(stage).setTitle("Event overview");
        verify(stage).setScene(overview);
    }

    @Test
    void showSplittyOverviewException() {
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        when(serverUtils.getEventById(anyInt())).thenThrow(RuntimeException.class);
        doNothing().when(sut).checkConnection();
        sut.showSplittyOverview(1);

        verify(sut).checkConnection();
    }

    @Test
    void setAdmin() {
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);

        sut.setAdmin(true);

        verify(splittyOverviewCtrl).setAdmin(true);
    }

    @Test
    void getFlag() {
        sut.setLanguage("lang");
        SetLanguage setLanguage = mock(SetLanguage.class);
        sut.setSetLanguage(setLanguage);

        sut.getFlag();

        verify(setLanguage).getFlag("lang");
    }

    @Test
    void addFlag() {
        sut.setLanguage("lang");
        SetLanguage setLanguage = mock(SetLanguage.class);
        sut.setSetLanguage(setLanguage);
        File image = mock(File.class);

        sut.addFlag(image);

        verify(setLanguage).addFlag(image, "lang");
    }

    @Test
    void getMyEventsNull() {
        sut.setSettingCtrl(null);

        assertNull(sut.getMyEvents());
    }

    @Test
    void getMyEvents() {
        SettingsCtrl settingsCtrl = mock(SettingsCtrl.class);
        sut.setSettingCtrl(settingsCtrl);
        when(settingsCtrl.getId()).thenReturn("id");
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        Event e1 = new Event();
        e1.id = 1;
        Event e2 = new Event();
        e2.id = 2;
        List<Event> expected = List.of(e1, e2);
        when(serverUtils.getEventsByParticipant("id")).thenReturn(expected);

        assertEquals(expected, sut.getMyEvents());
    }

    @Test
    void showAddExpense() {
        Stage stage = mock(Stage.class);
        AddExpenseCtrl addExpenseCtrl = mock(AddExpenseCtrl.class);
        Scene addExpense = mock(Scene.class);
        sut.setPrimaryStage(stage);
        sut.setAddExpenseCtrl(addExpenseCtrl);
        sut.setAddExpense(addExpense);

        sut.showAddExpense(1);

        verify(stage).setTitle("Add expense");
        verify(addExpenseCtrl).refresh(1);
        verify(stage).setScene(addExpense);
    }

    @Test
    void showAddExpenseException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setTitle(anyString());
        doNothing().when(sut).checkConnection();
        sut.showAddExpense(1);

        verify(sut).checkConnection();
    }

    @Test
    void showInvitationNoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showInvitation(1);

        verify(sut).showStartScreen();
    }

    @Test
    void showInvitation() {
        doReturn(true).when(sut).getConnection();
        InvitationCtrl invitationCtrl = mock(InvitationCtrl.class);
        sut.setInvitationCtrl(invitationCtrl);
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        Scene invitation = mock(Scene.class);
        sut.setInvitation(invitation);
        ServerUtils serverUtils = mock(ServerUtils.class);
        ServerUtils.server = "server";
        sut.setServerUtils(serverUtils);
        Event e = new Event("name", new Date(), "", "");
        e.setInviteCode("code");
        when(serverUtils.getEventById(anyInt())).thenReturn(e);

        sut.showInvitation(1);

        verify(invitationCtrl).refresh();
        verify(stage).setTitle("Invitation");
        verify(stage).setScene(invitation);
        verify(invitationCtrl).setEventCode(1);
        verify(invitationCtrl).setInviteCode("code");
        verify(invitationCtrl).showInviteCode();
        verify(invitationCtrl).setTitle("name");
        verify(invitationCtrl).setServer("server");
    }

    @Test
    void showAdminLoginNoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showAdminLogin();

        verify(sut).showStartScreen();
    }

    @Test
    void showAdminLogin() {
        doReturn(true).when(sut).getConnection();

        AdminLoginCtrl adminLoginCtrl = mock(AdminLoginCtrl.class);
        Scene login = mock(Scene.class);
        sut.setAdminLoginCtrl(adminLoginCtrl);
        sut.setAdminLogin(login);
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);

        sut.showAdminLogin();

        verify(stage).setTitle("Server management login");
        verify(adminLoginCtrl).reset();
        verify(stage).setScene(login);
    }

    @Test
    void showAdminOverviewNoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showAdminOverview();

        verify(sut).showStartScreen();
    }

    @Test
    void showAdminOverview() {
        doReturn(true).when(sut).getConnection();

        AdminOverviewCtrl adminOverviewCtrl = mock(AdminOverviewCtrl.class);
        Scene overview = mock(Scene.class);
        sut.setAdminOverviewCtrl(adminOverviewCtrl);
        sut.setAdminOverview(overview);
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);

        sut.showAdminOverview();

        verify(stage).setTitle("Admin management overview");
        verify(adminOverviewCtrl).refreshEvents();
        verify(adminOverviewCtrl).resetServerTag();
        verify(stage).setScene(overview);
    }

    @Test
    void showUserEventList() {
        UserEventListCtrl userEventListCtrl = mock(UserEventListCtrl.class);
        sut.setUserEventListCtrl(userEventListCtrl);
        Scene scene = mock(Scene.class);
        sut.setUserEventList(scene);
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);

        sut.showUserEventList();

        verify(userEventListCtrl).initialize();
        verify(stage).setScene(scene);
        verify(userEventListCtrl).reset();
        verify(stage).setTitle("Event List");
    }

    @Test
    void showUserEventListException() {
        UserEventListCtrl userEventListCtrl = mock(UserEventListCtrl.class);
        sut.setUserEventListCtrl(userEventListCtrl);
        doThrow(RuntimeException.class).when(userEventListCtrl).initialize();
        doNothing().when(sut).checkConnection();
        sut.showUserEventList();

        verify(sut).checkConnection();
    }

    @Test
    void showCreateEvent() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        CreateEventCtrl createEventCtrl = mock(CreateEventCtrl.class);
        sut.setCreateEventCtrl(createEventCtrl);
        Scene scene = mock(Scene.class);
        sut.setCreateEvent(scene);

        sut.showCreateEvent("name");

        verify(stage).setTitle("Create Event");
        verify(stage).setScene(scene);
        verify(createEventCtrl).resetValues();
        verify(createEventCtrl).setTitle("name");
    }

    @Test
    void showCreateEventNoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showCreateEvent("name");

        verify(sut).showStartScreen();
    }

    @Test
    void showParticipantManager() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        ManageParticipantsCtrl manageParticipantsCtrl = mock(ManageParticipantsCtrl.class);
        sut.setManageParticipantsCtrl(manageParticipantsCtrl);
        Scene scene = mock(Scene.class);
        sut.setManageParticipants(scene);

        sut.showParticipantManager(1);

        verify(stage).setTitle("ManageParticipants");
        verify(stage).setScene(scene);
        verify(manageParticipantsCtrl).setupParticipants(1);
    }

    @Test
    void showParticipantManagerException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setTitle(anyString());

        sut.showParticipantManager(1);

    }

    @Test
    void showStatistics() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        StatisticsCtrl statisticsCtrl = mock(StatisticsCtrl.class);
        sut.setStatisticsCtrl(statisticsCtrl);
        Scene scene = mock(Scene.class);
        sut.setStatistics(scene);

        sut.showStatistics("title", 1);

        verify(stage).setTitle("Statistics");
        verify(stage).setScene(scene);
        verify(statisticsCtrl).setTitle("title");
        verify(statisticsCtrl).setEventCode(1);
        verify(statisticsCtrl).refresh();
        verify(statisticsCtrl).fetchStat();
        verify(statisticsCtrl).setPieChart();
        verify(statisticsCtrl).showHoverLabel();
    }

    @Test
    void showStatisticsException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setTitle(anyString());
        doNothing().when(sut).checkConnection();
        sut.showStatistics("title", 1);

        verify(sut).checkConnection();
    }

    @Test
    void getCurrentEventCode() {
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);
        when(splittyOverviewCtrl.getCurrentEventId()).thenReturn(1);

        assertEquals(1, sut.getCurrentEventCode());
    }

    @Test
    void getCurrentEventCodeNull() {
        sut.setSplittyOverviewCtrl(null);

        assertThrows(RuntimeException.class, () -> sut.getCurrentEventCode());
    }

    @Test
    void viewDebtsPerEvent() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        DebtCtrl debtCtrl = mock(DebtCtrl.class);
        sut.setDebtCtrl(debtCtrl);
        Scene scene = mock(Scene.class);
        sut.setDebts(scene);

        sut.viewDeptsPerEvent(1);

        verify(stage).setTitle("Debts per event");
        verify(stage).setScene(scene);
        verify(debtCtrl).refresh(1);
    }

    @Test
    void viewDebtsPerEventException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setTitle(anyString());
        doNothing().when(sut).checkConnection();
        sut.viewDeptsPerEvent(1);

        verify(sut).checkConnection();
    }

    @Test
    void showSettings() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        SettingsCtrl settingsCtrl = mock(SettingsCtrl.class);
        sut.setSettingCtrl(settingsCtrl);
        Scene scene = mock(Scene.class);
        sut.setSettings(scene);

        sut.showSettings(true);

        verify(stage).setScene(scene);
        verify(stage).setTitle("Settings");
        verify(settingsCtrl).initializeFields(true);
    }

    @Test
    void showEditEvent() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        EditEventCrtl editEventCrtl = mock(EditEventCrtl.class);
        sut.setEditEventCrtl(editEventCrtl);
        Scene scene = mock(Scene.class);
        sut.setEditEvent(scene);

        sut.showEditEvent(1);

        verify(stage).setTitle("EditEvent");
        verify(editEventCrtl).setEventId(1);
        verify(editEventCrtl).setOldEventName();
        verify(editEventCrtl).reset();
        verify(stage).setScene(scene);
    }

    @Test
    void showEditEventException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setTitle(anyString());
        doNothing().when(sut).checkConnection();
        sut.showEditEvent(1);

        verify(sut).checkConnection();
    }

    @Test
    void setConfirmationSettings() {
        StartScreenCtrl startScreenCtrl = mock(StartScreenCtrl.class);
        sut.setStartScreenCtrl(startScreenCtrl);

        sut.setConfirmationSettings();
        verify(startScreenCtrl).setSettingsSavedLabel();
    }

    @Test
    void setConfirmationJoinedEvent() {
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);
        sut.setConfirmationJoinedEvent();

        verify(splittyOverviewCtrl).setJoinedEventLabel();
    }

    @Test
    void setConfirmationEventCreated() {
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);

        sut.setConfirmationEventCreated();

        verify(splittyOverviewCtrl).setEventCreatedLabel();
    }

    @Test
    void addEvent() {
        StartScreenCtrl startScreenCtrl = mock(StartScreenCtrl.class);
        sut.setStartScreenCtrl(startScreenCtrl);

        Event e = new Event();
        sut.addEvent(e);
        verify(startScreenCtrl).addEvent(e);
    }

    @Test
    void showEditExpense() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        EditExpenseCtrl editExpenseCtrl = mock(EditExpenseCtrl.class);
        sut.setEditExpenseCtrl(editExpenseCtrl);
        Scene scene = mock(Scene.class);
        sut.setEditExpense(scene);

        Expense e = new Expense();
        sut.showEditExpense(e);
    }

    @Test
    void showEditExpenseException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setTitle(anyString());
        doNothing().when(sut).checkConnection();
        sut.showEditExpense(new Expense());

        verify(sut).checkConnection();
    }

    @Test
    void getConnection() {
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);

        assertTrue(sut.getConnection());
        verify(serverUtils).getAllEvents();
    }

    @Test
    void getConnectionException() {
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        doThrow(RuntimeException.class).when(serverUtils).getAllEvents();

        assertFalse(sut.getConnection());
    }

    @Test
    void closeStage() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);

        sut.closeStage();

        verify(stage).close();
    }

    @Test
    void getAmountInDifferentCurrencySameCurrency() {
        double amount = 10.0;

        assertEquals(amount,
            sut.getAmountInDifferentCurrency(Currency.EUR, Currency.EUR, new Date(), amount),
            0.001);
    }

    @Test
    void getAmountInDifferentCurrencyNull() {
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        when(serverUtils.getConversion(any(), any(), anyString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            sut.getAmountInDifferentCurrency(Currency.EUR, Currency.USD, new Date(), 10.0);
        });
    }

    @Test
    void getAmountInDifferentCurrency() {
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        when(serverUtils.getConversion(any(), any(), anyString())).thenReturn(new Conversion(
            "", "", 10.0, ""
        ));

        assertEquals(100.0, sut.getAmountInDifferentCurrency(Currency.EUR, Currency.USD,
            new Date(), 10.0), 0.001);
    }

    @Test
    void getFormattedDoubleString() {
        assertEquals("1.10", sut.getFormattedDoubleString(1.10320));
    }

    @Test
    void showAddParticipantNoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showAddParticipant(1);

        verify(sut).showStartScreen();
    }

    @Test
    void showAddParticipant() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        AddParticipantCtrl addParticipantCtrl = mock(AddParticipantCtrl.class);
        sut.setAddParticipantCtrl(addParticipantCtrl);
        Scene scene = mock(Scene.class);
        sut.setAddParticipant(scene);

        sut.showAddParticipant(1);

        verify(stage).setScene(scene);
        verify(stage).setTitle("Add participant");
        verify(addParticipantCtrl).setEventId(1);
    }

    @Test
    void showEditParticipant1NoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showEditParticipant(1, "uuid");

        verify(sut).showStartScreen();
    }

    @Test
    void showEditParticipant1() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        EditParticipantCtrl editParticipantCtrl = mock(EditParticipantCtrl.class);
        sut.setEditParticipantCtrl(editParticipantCtrl);
        Scene scene = mock(Scene.class);
        sut.setEditParticipant(scene);

        sut.showEditParticipant(1, "uuid");

        verify(stage).setScene(scene);
        verify(stage).setTitle("Edit participant");
        verify(editParticipantCtrl).setEventId(1);
        verify(editParticipantCtrl).autoFillWithMyData("uuid");
        verify(editParticipantCtrl).setHost(true);
    }

    @Test
    void showEditParticipant2NoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showEditParticipant(1);

        verify(sut).showStartScreen();
    }

    @Test
    void showEditParticipant2() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        EditParticipantCtrl editParticipantCtrl = mock(EditParticipantCtrl.class);
        sut.setEditParticipantCtrl(editParticipantCtrl);
        Scene scene = mock(Scene.class);
        sut.setEditParticipant(scene);

        sut.showEditParticipant(1);

        verify(stage).setScene(scene);
        verify(stage).setTitle("Edit participant");
        verify(editParticipantCtrl).setEventId(1);
        verify(editParticipantCtrl).autoFillWithMyData();
        verify(editParticipantCtrl).setHost(false);
    }

    @Test
    void showManageTags() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        ManageTagsCtrl manageTagsCtrl = mock(ManageTagsCtrl.class);
        sut.setManageTagsCtrl(manageTagsCtrl);
        Scene scene = mock(Scene.class);
        sut.setManageTags(scene);
        Expense e = new Expense();

        sut.showManageTags(1, true, e, false);

        verify(stage).setScene(scene);
        verify(stage).setTitle("Manage Tags");
        verify(manageTagsCtrl).refreshList(1, true, e, false);
    }

    @Test
    void showManageTagsException() {
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        doThrow(RuntimeException.class).when(stage).setScene(any());
        doNothing().when(sut).checkConnection();
        Expense e = new Expense();

        sut.showManageTags(1, true, e, false);

        verify(sut).checkConnection();
    }

    @Test
    void showAddTag1NoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showAddTag(1, true, new Expense(), true);

        verify(sut).showStartScreen();
    }

    @Test
    void showAddTag1() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        AddTagCtrl addTagCtrl = mock(AddTagCtrl.class);
        sut.setAddTagCtrl(addTagCtrl);
        Scene scene = mock(Scene.class);
        sut.setAddTag(scene);
        Expense e = new Expense();

        sut.showAddTag(1, true, e, false);

        verify(addTagCtrl).setFields(1, true, e, false);
        verify(stage).setScene(scene);
        verify(stage).setTitle("Add Tag");
    }

    @Test
    void showAddTag2NoConnection() {
        doReturn(false).when(sut).getConnection();
        doNothing().when(sut).showStartScreen();

        sut.showAddTag(new Tag(), 1, true, new Expense(), true);

        verify(sut).showStartScreen();
    }

    @Test
    void showAddTag2() {
        doReturn(true).when(sut).getConnection();
        Stage stage = mock(Stage.class);
        sut.setPrimaryStage(stage);
        AddTagCtrl addTagCtrl = mock(AddTagCtrl.class);
        sut.setAddTagCtrl(addTagCtrl);
        Scene scene = mock(Scene.class);
        sut.setAddTag(scene);
        Expense e = new Expense();
        Tag t = new Tag();

        sut.showAddTag(t, 1, true, e, false);

        verify(addTagCtrl).setFields(t, 1, true, e, false);
        verify(stage).setScene(scene);
        verify(stage).setTitle("Edit Tag");
    }

    @Test
    void setConfirmationEditParticipant(){
        ManageParticipantsCtrl manageParticipantsCtrl = mock(ManageParticipantsCtrl.class);
        sut.setManageParticipantsCtrl(manageParticipantsCtrl);

        sut.setConfirmationEditParticipant();
        verify(manageParticipantsCtrl).setParticipantEditedConfirmation();
    }

    @Test
    void setConfirmationAddParticipant(){
        ManageParticipantsCtrl manageParticipantsCtrl = mock(ManageParticipantsCtrl.class);
        sut.setManageParticipantsCtrl(manageParticipantsCtrl);

        sut.setConfirmationAddParticipant();
        verify(manageParticipantsCtrl).setParticipantAddedConfirmation();
    }

    @Test
    void setConfirmationAddedTag(){
        ManageTagsCtrl manageTagsCtrl = mock(ManageTagsCtrl.class);
        sut.setManageTagsCtrl(manageTagsCtrl);

        sut.setConfirmationAddedTag();
        verify(manageTagsCtrl).setAddedTagConfirmation();
    }

    @Test
    void setConfirmationEditedTag(){
        ManageTagsCtrl manageTagsCtrl = mock(ManageTagsCtrl.class);
        sut.setManageTagsCtrl(manageTagsCtrl);

        sut.setConfirmationEditedTag();
        verify(manageTagsCtrl).setEditedTagConfirmation();
    }

    @Test
    void stopLongPolling(){
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);

        sut.stopLongPolling();
        verify(splittyOverviewCtrl).stopUpdates();
    }

    @Test
    void showUndoInOverview(){
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);

        sut.showUndoInOverview();
        verify(splittyOverviewCtrl).showUndo();
    }

    @Test
    void updateOverviewUndoStacks(){
        SplittyOverviewCtrl splittyOverviewCtrl = mock(SplittyOverviewCtrl.class);
        sut.setSplittyOverviewCtrl(splittyOverviewCtrl);
        Expense e = new Expense();
        List<Debt> debts = new ArrayList<>();
        String method = "method";

        sut.updateOverviewUndoStacks(e,debts,method);

        verify(splittyOverviewCtrl).pushUndoStacks(e,debts,method);
    }

    @Test
    void setNewLang(){
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);

        sut.setNewLang("for", "new");

        verify(serverUtils).setNewLang("for", "new");
    }

    @Test
    void setNewLangException(){
        ServerUtils serverUtils = mock(ServerUtils.class);
        sut.setServerUtils(serverUtils);
        doThrow(RuntimeException.class).when(serverUtils).setNewLang(anyString(), anyString());
        doNothing().when(sut).checkConnection();

        sut.setNewLang("for", "new");

        verify(sut).checkConnection();
    }
}