package Service;

import Model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseManager {
    List<Expense> expenses;
    Map<String, User> userMap;
    // Map<UserID_Who_Owes, Map<UserID_Who_Lends, Amount>>
    Map<String, Map<String, Double>> balanceSheet;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        userMap = new HashMap<>();
        balanceSheet = new HashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
        balanceSheet.put(user.getId(), new HashMap<>());
    }

    public void addExpense(ExpenseType type, double amount, String paidBy, List<Split> splits) {
        // 1. Get the actual User object for the payer
        User paidByUser = userMap.get(paidBy);

        // 2. Create the Expense using the Service (Logic separated!)
        Expense expense = ExpenseService.createExpense(type, amount, paidByUser, splits);
        expenses.add(expense);

        // 3. Update the Balance Sheet (The tricky part)
        for (Split split : expense.getSplits()) {
            String paidTo = split.getUser().getId();

            // Self-payment (A pays for A) - Ignore
            if (paidTo.equals(paidBy)) {
                continue;
            }

            // Update: 'paidTo' owes 'paidBy'
            Map<String, Double> balancesForPaidTo = balanceSheet.get(paidTo);
            if (!balancesForPaidTo.containsKey(paidBy)) {
                balancesForPaidTo.put(paidBy, 0.0);
            }

            // Add the new amount to existing debt
            balancesForPaidTo.put(paidBy, balancesForPaidTo.get(paidBy) + split.getAmount());

            // OPTIONAL: Simplify debt (A owes B 100, B owes A 40 -> A owes B 60)
            // For now, we skip this to keep it simple.
        }
    }

    public void showBalance(String userId) {
        boolean isEmpty = true;
        for (Map.Entry<String, Double> userBalance : balanceSheet.get(userId).entrySet()) {
            if (userBalance.getValue() != 0) {
                isEmpty = false;
                printBalance(userId, userBalance.getKey(), userBalance.getValue());
            }
        }

        if (isEmpty) {
            System.out.println("No balances for " + userId);
        }
    }

    public void showBalances() {
        boolean isEmpty = true;
        for (Map.Entry<String, Map<String, Double>> allBalances : balanceSheet.entrySet()) {
            for (Map.Entry<String, Double> userBalance : allBalances.getValue().entrySet()) {
                if (userBalance.getValue() > 0) {
                    isEmpty = false;
                    printBalance(allBalances.getKey(), userBalance.getKey(), userBalance.getValue());
                }
            }
        }

        if (isEmpty) {
            System.out.println("No balances");
        }
    }

    private void printBalance(String user1, String user2, double amount) {
        String user1Name = userMap.get(user1).getName();
        String user2Name = userMap.get(user2).getName();
        // Format: User4 owes User1: 250
        if (amount > 0) {
            System.out.println(user1Name + " owes " + user2Name + ": " + amount);
        }
    }
}