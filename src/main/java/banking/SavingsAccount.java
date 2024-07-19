package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SavingsAccount extends Account {

	private boolean withdrawalUsed;

	public SavingsAccount(String id, double apr) {
		super(id, apr, 0.00);
		this.withdrawalUsed = false;
	}

	@Override
	public boolean checkDepositAmount(double amountToDeposit) {
		return (amountToDeposit >= 0 && amountToDeposit <= 2500);
	}

	@Override
	public boolean checkWithdrawAmount(double amountToWithdraw) {
		if (!withdrawalUsed && amountToWithdraw >= 0 && amountToWithdraw <= 1000) {
			withdrawalUsed = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean canTransfer() {
		return true;
	}

	@Override
	public void passAndCalculateAPR(int month) {
		for (int currentMonth = 1; currentMonth <= month; currentMonth++) {
			months++;
			balance += ((apr / 100) / 12) * balance;
		}
		withdrawalUsed = false;
	}

	@Override
	public String getCurrentStatus(Account account) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
		String formatBalance = decimalFormat.format(account.getBalance());
		String formatAPR = decimalFormat.format(account.getAPR());
		return "Savings" + " " + account.getID() + " " + formatBalance + " " + formatAPR;
	}
}
