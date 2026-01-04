package Model;


public class ExactSplit extends Split {
    // ExactSplit needs both User and Amount
    public ExactSplit(User user, double amount) {
        super(user);
        this.amount = amount;
    }
}