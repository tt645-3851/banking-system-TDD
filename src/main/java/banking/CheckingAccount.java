package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CheckingAccount extends Account {

	public CheckingAccount(String id, double apr) {
		super(id, apr, 0.00);
	}

	public boolean checkDepositAmount(double amountToDeposit) {
		return (amountToDeposit >= 0 && amountToDeposit <= 1000);
	}

	public boolean checkWithdrawAmount(double amountToWithdraw) {
		return (amountToWithdraw >= 0 && amountToWithdraw <= 400);
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
	}

	@Override
	public String getCurrentStatus(Account account) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
		String formatBalance = decimalFormat.format(account.getBalance());
		String formatAPR = decimalFormat.format(account.getAPR());
		return "Checking" + " " + account.getID() + " " + formatBalance + " " + formatAPR;
	}
}
