package Driver;

import Model.*;
import Service.ExpenseManager;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ExpenseManager expenseManager = new ExpenseManager();

        // 1. Add Users
        User u1 = new User("u1", "User1", "u1@gmail.com");
        User u2 = new User("u2", "User2", "u2@gmail.com");
        User u3 = new User("u3", "User3", "u3@gmail.com");
        User u4 = new User("u4", "User4", "u4@gmail.com");

        expenseManager.addUser(u1);
        expenseManager.addUser(u2);
        expenseManager.addUser(u3);
        expenseManager.addUser(u4);

        // 2. Scenario: u1 pays 1000 for u1, u2, u3, u4 (EQUAL)
        List<Split> splits1 = new ArrayList<>();
        splits1.add(new EqualSplit(u1));
        splits1.add(new EqualSplit(u2));
        splits1.add(new EqualSplit(u3));
        splits1.add(new EqualSplit(u4));

        expenseManager.addExpense(ExpenseType.EQUAL, 1000, "u1", splits1);

        // 3. Scenario: u1 pays 1250 for u2 and u3 (EXACT)
        // u2 owes 370, u3 owes 880
        List<Split> splits2 = new ArrayList<>();
        splits2.add(new ExactSplit(u2, 370));
        splits2.add(new ExactSplit(u3, 880));

        expenseManager.addExpense(ExpenseType.EXACT, 1250, "u1", splits2);

        // 4. Show All Balances
        System.out.println("--- All Balances ---");
        expenseManager.showBalances();

        System.out.println("--- u2 Balances ---");
        expenseManager.showBalance("u2");
    }
}