package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {

	private final Map<String, Account> accounts;
	private final List<String> accountOrder;

	public Bank() {
		accounts = new HashMap<>();
		accountOrder = new ArrayList<>();
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void addAccount(String accountType, String id, double apr, double balance) {

		Account account;

		if (balance == 0.00) {
			if (accountType.equalsIgnoreCase("checking")) {
				account = new CheckingAccount(id, apr);
			} else {
				account = new SavingsAccount(id, apr);
			}
		} else {
			account = new CDAccount(id, apr, balance);
		}
		accounts.put(id, account);
		accountOrder.add(id);
	}

	public Account retrieveAccount(String id) {
		return accounts.get(id);
	}

	public boolean accountExistsById(String accountId) {
		return accounts.get(accountId) == null;
	}

	public void transferAmountToID(Account fromAccount, Account toAccount, double amount) {
		fromAccount.withdrawCash(amount);
		toAccount.depositCash(amount);
	}

    public void passTime(int months) {
		List<String> closeAccountList = new ArrayList<>();
		for (int month = 1; month <= months; month++) {
			for (String accountID : accounts.keySet()) {
				Account account = accounts.get(accountID);
				if (account.balance == 0) {
					closeAccountList.add(accountID);
				} else {
					if (account.balance < 100) {
						accounts.get(accountID).withdrawCash(25);
					}
					account.passAndCalculateAPR(1);
				}
			}
		}
		for (String ID : closeAccountList) {
			accounts.remove(ID);
			accountOrder.remove(ID);
		}
	}

	public List<String> getAccountList() {
		return accountOrder;
	}
}
