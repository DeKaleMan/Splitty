package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import commons.Participant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParticipantTest {
    private Participant participant;

    @BeforeEach
    public void setUp() {
        participant = new Participant("Yavor Pachedjiev",
                200.0,
                "BG18RZBB91550123456789",
                "ABCDGB2LXXX",
                "Yavor Pachedjiev",
                "yavor@delf.nl");
    }

    @Test
    public void addingValidExpenseUpdatesExpenseList() {
        participant.addExpense("100.25");
        assertEquals(1, participant.getExpenses().size(),
                "Expense list size did not update correctly after adding a valid expense.");
        assertTrue(participant.getExpenses().contains("100.25"),
                "Expense list does not contain the added expense.");
    }

    @Test
    public void calculatingTotalExpensesWithValidInputs() {
        participant.addExpense("50.50");
        participant.addExpense("149.50");
        assertEquals(200.0, participant.calculateTotalExpenses(),
                "Total expenses calculation is incorrect with valid inputs.");
    }

    @Test
    public void updatingBalanceReflectsCorrectly() {
        participant.updateBalance(-75.75);
        assertEquals(124.25, participant.getBalance(),
                "Balance did not update correctly after deduction.");
        participant.updateBalance(100.00);
        assertEquals(224.25, participant.getBalance(),
                "Balance did not update correctly after addition.");
    }

    @Test
    public void testEmailUpdateAndValidation() {
        // Assuming validation logic is added or exists
        String newEmail = "yavor.new@delf.nl";
        participant.setEmail(newEmail);
        assertEquals(newEmail, participant.getEmail(),
                "Email update failed or validation error occurred.");
    }

    @Test
    public void addingMultipleExpensesUpdatesTotalCorrectly() {
        participant.addExpense("10.00");
        participant.addExpense("20.00");
        participant.addExpense("30.00");
        assertEquals(60.00, participant.calculateTotalExpenses(),
                "Total expenses are not calculated correctly after adding multiple expenses.");
    }

    @Test
    public void updatingBalanceForMultipleTransactions() {
        // Simulating multiple transactions
        participant.updateBalance(-50); // Expense
        participant.updateBalance(75); // Receiving money
        participant.updateBalance(-25); // Another expense
        assertEquals(200, participant.getBalance(),
                "Balance incorrect after multiple updates.");
    }

}
