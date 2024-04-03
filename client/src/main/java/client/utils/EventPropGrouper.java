package client.utils;

import client.scenes.*;
import javafx.scene.Parent;
import javafx.util.Pair;

/**
 * @param addExpense
 * @param addParticipant
 * @param statistics
 * @param manageParticipants
 */
public record EventPropGrouper(Pair<AddExpenseCtrl, Parent> addExpense,
                               Pair<AddParticipantCtrl, Parent> addParticipant,
                               Pair<EditParticipantCtrl, Parent> editParticipant,
                               Pair<StatisticsCtrl, Parent> statistics,
                               Pair<DebtCtrl, Parent> debts,

                               Pair<EditEventCrtl, Parent> editEvent,
                               Pair<EditExpenseCtrl, Parent> editExpense,
                               Pair<ManageParticipantsCtrl, Parent> manageParticipants) {
}


