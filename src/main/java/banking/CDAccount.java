package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CDAccount extends Account {

	public CDAccount(String id, double apr, double balance) {
		super(id, apr, balance);
	}

	@Override
	public boolean checkDepositAmount(double amountToDeposit) {
		return false;
	}

	@Override
	public boolean checkWithdrawAmount(double amountToWithdraw) {
		return (amountToWithdraw >= getBalance() && months >= 12);
	}

	@Override
	public boolean canTransfer() {
		return false;
	}

	@Override
	public void passAndCalculateAPR(int month) {
		for (int currentMonth = 1; currentMonth <= month; currentMonth++) {
			months++;
			for (int i = 0; i < 4; i++) {
				balance += ((apr / 100) / 12) * balance;
			}
		}
	}

	@Override
	public String getCurrentStatus(Account account) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
		String formatBalance = decimalFormat.format(account.getBalance());
		String formatAPR = decimalFormat.format(account.getAPR());
		return "Cd" + " " + account.getID() + " " + formatBalance + " " + formatAPR;
	}


}
