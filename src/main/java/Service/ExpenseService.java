package Service;

import Model.*;
import java.util.List;

public class ExpenseService {

    // The method needs Inputs to work with!
    public static Expense createExpense(ExpenseType type, double amount, User paidBy, List<Split> splits) {

        // 1. Validate the inputs (Optional but good practice)
        validateSplits(type, amount, splits);

        // 2. Do the Math based on the Type
        switch (type) {
            case EQUAL:
                double equalShare = amount / splits.size();
                // We must update EVERY split with this new amount
                for (Split split : splits) {
                    split.setAmount(equalShare);
                }
                break;

            case PERCENT:
                for (Split split : splits) {
                    // cast to PercentSplit to access getPercent()
                    PercentSplit pSplit = (PercentSplit) split;
                    double calculatedShare = (amount * pSplit.getPercent()) / 100.0;
                    split.setAmount(calculatedShare);
                }
                break;

            case EXACT:
                // For EXACT, the amount is already set in the Split constructor,
                // so we don't need to calculate anything.
                // We just trust the input (validated below).
                break;
        }

        // 3. Return the finished Expense object
        // We use a random ID (or you can use a counter)
        String id = "EXP" + System.currentTimeMillis();
        return new Expense(id, amount, paidBy, splits, type);
    }

    // A helper method to check if the math adds up
    private static void validateSplits(ExpenseType type, double amount, List<Split> splits) {
        if (type == ExpenseType.PERCENT) {
            double totalPercent = 0;
            for (Split split : splits) {
                PercentSplit pSplit = (PercentSplit) split;
                totalPercent += pSplit.getPercent();
            }
            if (totalPercent != 100) {
                // In a real app, throw an exception here
                System.out.println("ERROR: Percentages don't add up to 100!");
            }
        }
    }
}