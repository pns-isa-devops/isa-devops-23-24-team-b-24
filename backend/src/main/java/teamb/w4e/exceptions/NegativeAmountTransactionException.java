package teamb.w4e.exceptions;

public class NegativeAmountTransactionException extends Exception {

        private double amount;

        public NegativeAmountTransactionException(double amount) {
            this.amount = amount;
        }

        public NegativeAmountTransactionException() {
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
}
