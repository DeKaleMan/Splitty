package client.scenes;

import javafx.scene.Parent;
import javafx.util.Pair;

/**
 *
 * @param addExpense
 * @param manageParticipants
 * @param statistics
 */
public record EventPropGrouper(Pair<AddExpenseCtrl, Parent> addExpense,
                               Pair<ManageParticipantsCtrl, Parent> manageParticipants,
                               Pair<StatisticsCtrl, Parent> statistics,
                               Pair<DebtCtrl, Parent> debts) {

}
//package client.scenes;
//
//        import javafx.scene.Parent;
//        import javafx.util.Pair;
//
//public class EventPropGrouper {
//    private Pair<AddExpenseCtrl, Parent> addExpense;
//    private Pair<ManageParticipantsCtrl, Parent> manageParticipants;
//    private Pair<StatisticsCtrl, Parent> statistics;
//    public EventPropGrouper(Pair<AddExpenseCtrl, Parent> addExpense,
//                            Pair<ManageParticipantsCtrl, Parent> manageParticipants,
//                            Pair<StatisticsCtrl, Parent> statistics) {
//        this.addExpense = addExpense;
//        this.manageParticipants = manageParticipants;
//        this.statistics = statistics;
//    }
//
//    public Pair<AddExpenseCtrl, Parent> getAddExpense() {
//        return addExpense;
//    }
//
//    public void setAddExpense(Pair<AddExpenseCtrl, Parent> addExpense) {
//        this.addExpense = addExpense;
//    }
//
//    public Pair<ManageParticipantsCtrl, Parent> getManageParticipants() {
//        return manageParticipants;
//    }
//
//    public void setManageParticipants(Pair<ManageParticipantsCtrl, Parent> manageParticipants) {
//        this.manageParticipants = manageParticipants;
//    }
//
//    public Pair<StatisticsCtrl, Parent> getStatistics() {
//        return statistics;
//    }
//
//    public void setStatistics(Pair<StatisticsCtrl, Parent> statistics) {
//        this.statistics = statistics;
//    }
//}

