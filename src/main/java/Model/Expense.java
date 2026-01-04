package Model;

import Model.ExpenseType;
import Model.Split;
import Model.User;

import java.util.List;

public class Expense {
    private String id;
    private double amount;
    private User paidBy;           // The person who swiped the card
    private ExpenseType type;      // EQUAL, EXACT, or PERCENT
    private List<Split> splits;    // The list of people involved (including the payer usually)

    // Optional metadata
    // private String description;
    // private Date date;

    public Expense(String id, double amount, User paidBy, List<Split> splits, ExpenseType type) {
        this.id = id;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public ExpenseType getType() {
        return type;
    }
}