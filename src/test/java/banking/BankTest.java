package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {

	public static final String CHECKING_ID = "12345678";
	public static final String SAVINGS_ID = "01234567";
	public static final String CD_ACCOUNT_ID = "11223344";
	private static final double DELTA = 1e-2;
	Bank bank;

	@BeforeEach
	void setup() {
		bank = new Bank();
	}

	@Test
	void bank_has_no_accounts_initially() {
		assertTrue(bank.getAccounts().isEmpty());
	}

	@Test
	void add_account_to_bank() {
		bank.addAccount("checking", CHECKING_ID, 1.2, 0.0);
		assertEquals(CHECKING_ID, bank.getAccounts().get(CHECKING_ID).getID());
	}

	@Test
	void add_two_accounts_to_bank() {
		bank.addAccount("checking", CHECKING_ID, 1.2, 0.0);
		bank.addAccount("savings", SAVINGS_ID, 9.9, 0.0);
		assertEquals(2, bank.getAccounts().size());
	}

	@Test
	void get_a_specified_account_in_bank() {
		bank.addAccount("checking", CHECKING_ID, 1.2, 0.0);
		bank.addAccount("savings", SAVINGS_ID, 9.9, 0.0);
		bank.addAccount("cd", CD_ACCOUNT_ID, 2.2, 400.99);
		Account account = bank.retrieveAccount(SAVINGS_ID);
		assertEquals(bank.getAccounts().get(SAVINGS_ID), account);
	}

	@Test
	void deposit_cash_in_account_from_bank() {
		bank.addAccount("checking", CHECKING_ID, 1.2, 0.0);
		bank.getAccounts().get(CHECKING_ID).depositCash(350.22);
		assertEquals(350.22, bank.retrieveAccount(CHECKING_ID).getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_in_account_from_bank() {
		bank.addAccount("cd", CD_ACCOUNT_ID, 2.2, 400.99);
		Account account = bank.retrieveAccount(CD_ACCOUNT_ID);
		account.months = 12;
		account.withdrawCash(400.99);
		assertEquals(0, bank.retrieveAccount(CD_ACCOUNT_ID).getBalance(), DELTA);
	}

	@Test
	void deposit_twice_in_account_from_bank() {
		bank.addAccount("savings", SAVINGS_ID, 9.9, 0.00);
		bank.getAccounts().get(SAVINGS_ID).depositCash(55.55);
		bank.getAccounts().get(SAVINGS_ID).depositCash(100.35);
		assertEquals(155.90, bank.retrieveAccount(SAVINGS_ID).getBalance(), DELTA);
	}

	@Test
	void withdraw_twice_in_account_from_bank() {
		bank.addAccount("checking", CHECKING_ID, 1.2, 0.00);
		bank.retrieveAccount(CHECKING_ID).depositCash(200.12);
		bank.retrieveAccount(CHECKING_ID).withdrawCash(100.45);
		bank.retrieveAccount(CHECKING_ID).withdrawCash(50.22);
		assertEquals(49.45, bank.retrieveAccount(CHECKING_ID).getBalance(), DELTA);
	}
}
