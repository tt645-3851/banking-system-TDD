package banking;

public abstract class Account {

	public int months;
	public double balance;
	public final double apr;
	private final String id;

	protected Account(String id, double apr, double balance) {
		this.id = id;
		this.balance = balance;
		this.apr = apr;
		this.months = 0;
	}

	public double getBalance() {
		return balance;
	}

	public double getAPR() {
		return apr;
	}

	public int getMonths() {
		return months;
	}

	public void depositCash(double cashToDeposit) {
		if (checkDepositAmount(cashToDeposit)) {
			balance += cashToDeposit;
		}
	}

	public void withdrawCash(double cashToWithdraw) {
		if (checkWithdrawAmount(cashToWithdraw)) {
			if (cashToWithdraw >= getBalance()) {
				balance = 0;
			} else {
				balance -= cashToWithdraw;
			}
		}
	}

	public String getID() {
		return id;
	}

	public abstract boolean checkDepositAmount(double amountToDeposit);

	public abstract boolean checkWithdrawAmount(double commandArg);

	public abstract boolean canTransfer();

    public abstract void passAndCalculateAPR(int month);

	public abstract String getCurrentStatus(Account account);
}
