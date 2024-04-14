package client.utils;

import client.scenes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SetLanguageTest {

    @Mock
    private StartScreenCtrl startScreenCtrl;

    @Mock
    private SplittyOverviewCtrl splittyOverviewCtrl;

    @Mock
    private AddExpenseCtrl addExpenseCtrl;

    @Mock
    private AdminLoginCtrl adminLoginCtrl;

    @Mock
    private AdminOverviewCtrl adminOverviewCtrl;

    @Mock
    private CreateEventCtrl createEventCtrl;

    @Mock
    private SettingsCtrl settingsCtrl;

    @Mock
    private StatisticsCtrl statisticsCtrl;

    @Mock
    private ServerCtrl serverCtrl;

    @Mock
    private InvitationCtrl invitationCtrl;

    @Mock
    private ManageParticipantsCtrl manageParticipantsCtrl;

    @Mock
    private EditParticipantCtrl editParticipantCtrl;

    @Mock
    private AddParticipantCtrl addParticipantCtrl;

    @Mock
    private EditExpenseCtrl editExpenseCtrl;

    @Mock
    private EditEventCrtl editEventCrtl;

    private SetLanguage setLanguage;

    @Mock
    private ClientFileIOutil io;

    @Mock
    ServerUtils serverUtils;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setLanguage = new SetLanguage(startScreenCtrl, splittyOverviewCtrl,
                addExpenseCtrl, adminLoginCtrl, adminOverviewCtrl, createEventCtrl,
                settingsCtrl, statisticsCtrl, serverCtrl, invitationCtrl, manageParticipantsCtrl,
                editParticipantCtrl, addParticipantCtrl, editExpenseCtrl, editEventCrtl);
        setLanguage.setServerUtilsIO(serverUtils, io);
    }

    @Test
    public void testSetMainScreen() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setMainScreen("en");
        verify(startScreenCtrl).setCreateEventText("TranslatedText");
        verify(startScreenCtrl).setJoinEventText("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(17)).translate(anyString(), anyString(), anyString());
    }
    @Test
    public void testSplittyOverview() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setSpittyoverview("en");
        verify(splittyOverviewCtrl).setHostOptionsButton("TranslatedText");
        verify(splittyOverviewCtrl).setmyDetails("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(17)).translate(anyString(), anyString(), anyString());

    }
    @Test
    public void testSetExpenses() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setAddExpense("en");
        verify(addExpenseCtrl).setExpenseTypeBox("TranslatedText");
        verify(addExpenseCtrl).setSharedExpense("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(16)).translate(anyString(), anyString(), anyString());
    }
    @Test
    public void testSetAdminLogin() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setAdminLogin("en");
        verify(adminLoginCtrl).setBack("TranslatedText");
        verify(adminLoginCtrl).setPasswordField("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(8)).translate(anyString(), anyString(), anyString());
    }
    @Test
    public void testSetAdminOverview() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setAdminOverview("en");
        verify(adminOverviewCtrl).setAdminManagementOverviewText("TranslatedText");
        verify(adminOverviewCtrl).setImportEventButtonText("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(9)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetCreateEvent() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setCreateEvent("en");
        verify(createEventCtrl).setEventNameText("TranslatedText");
        verify(createEventCtrl).setDateText("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(9)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetSettings() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setSettings("en");
        verify(settingsCtrl).setSettingsText("TranslatedText");
        verify(settingsCtrl).setAddLanguage("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(15)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetStatistics() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setStatistics("en");
        verify(statisticsCtrl).setTotalCostText("TranslatedText");
        verify(statisticsCtrl).setStatisticsText("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(4)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetServer() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setServer("en");
        verify(serverCtrl).setServerText("TranslatedText");
        verify(serverCtrl).setConnectButton("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(5)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetInvite() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setInvite("en");
        verify(invitationCtrl).setBack("TranslatedText");
        verify(invitationCtrl).setInviteCodeText("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(10)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetManageParticipants() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setManageParticipants("en");
        verify(manageParticipantsCtrl).setAddButton("TranslatedText");
        verify(manageParticipantsCtrl).setBackButton("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(8)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetEditParticipant() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setEditParticipant("en");
        verify(editParticipantCtrl).setApplyChangesButton("TranslatedText");
        verify(editParticipantCtrl).setTitle("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(8)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetAddParticipant() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setAddPartiticipant("en");
        verify(addParticipantCtrl).setApplyChangesButton("TranslatedText");
        verify(addParticipantCtrl).setTitle("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(8)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetEditExpense() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setEditExpense("en");
        verify(editExpenseCtrl).setSceneTypeText("TranslatedText");
        verify(editExpenseCtrl).setWhoPaid("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(16)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testSetEditEvent() {
        when(serverUtils.translate(anyString(), anyString(), anyString())).thenReturn("TranslatedText");
        setLanguage.setEditEvent("en");
        verify(editEventCrtl).setEventNameText("TranslatedText");
        verify(editEventCrtl).setCreateButton("TranslatedText");
        // Verify other text setting methods
        // Verify translation service called for each text
        verify(serverUtils, times(6)).translate(anyString(), anyString(), anyString());
    }

    @Test
    public void testGetFlagForExistingLanguage() {
        // When
        when(io.getFlagFolder()).thenReturn("flagFolder");
        when(io.fileExists(new File("flagFolder" + File.separator + "enFlag.png"))).thenReturn(true);
        //Image image = setLanguage.getFlag("en");
        // Then
//        assertEquals(new Image("fileFolder" + File.separator + "enFlag.png"), image);
    }

    @Test
    public void testChangeLanguage(){
        String lang = "ToLang";
        SetLanguage mock = mock(SetLanguage.class);
        mock.changeTo(lang);
        // When
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            setLanguage.changeTo(lang);
        });

        // Then
        assertDoesNotThrow(() -> {
            future.get(5, TimeUnit.SECONDS); // Wait for the async operation to complete
        }, "Exception occurred during async execution");

    }
    @Test
    public void testGetFlagForNonExistingLanguage() {

    }

    @Test
    public void testGetFlagForDefaultLanguage() {

    }

}