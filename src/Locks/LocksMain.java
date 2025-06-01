package Locks;

public class LocksMain {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();

        // Runnable that withdraws money using lock-based method
        Runnable task = () -> bankAccount.lockingWithdraw(50);

        Thread transaction1 = new Thread(task, "Transaction 1");
        Thread transaction2 = new Thread(task, "Transaction 2");

        transaction1.start();
        transaction2.start();
    }
}
