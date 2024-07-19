package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

	private static final double DELTA = 1e-2;
	Account checking;
	Account savings;
	Account cdAccount;

	@BeforeEach
	void setup() {
		checking = new CheckingAccount("12345678", 0.8);
		savings = new SavingsAccount("01234567", 5.5);
		cdAccount = new CDAccount("76543210", 4.8, 500.33);
	}

	@Test
	void account_is_created_with_no_balance_initially() {
		assertEquals(0.00, checking.getBalance());
		assertEquals(0.00, savings.getBalance());
		assertEquals(500.33, cdAccount.getBalance(), DELTA);
	}

	@Test
	void account_created_with_given_APR() {
		assertEquals(0.8, checking.getAPR());
		assertEquals(5.5, savings.getAPR());
		assertEquals(4.8, cdAccount.getAPR());
	}

	@Test
	void deposit_cash_in_account() {
		checking.depositCash(100.00);
		assertEquals(100.00, checking.getBalance(), DELTA);
		savings.depositCash(250.00);
		assertEquals(250.00, savings.getBalance(), DELTA);
		cdAccount.depositCash(100.11);
		assertEquals(500.33, cdAccount.getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_in_account() {
		checking.depositCash(100.00);
		checking.withdrawCash(50.00);
		assertEquals(50.00, checking.getBalance(), DELTA);

		savings.depositCash(250.00);
		savings.withdrawCash(100.00);
		assertEquals(150.00, savings.getBalance(), DELTA);

		cdAccount.months = 12;
		cdAccount.withdrawCash(500.33);
		assertEquals(0, cdAccount.getBalance(), DELTA);
	}

	@Test
	void withdraw_more_than_balance_decreases_balance_to_zero() {
		checking.depositCash(100.00);
		checking.withdrawCash(200.00);
		assertEquals(0.00, checking.getBalance());

		savings.depositCash(250.00);
		savings.withdrawCash(300.00);
		assertEquals(0.00, savings.getBalance());

		cdAccount.months = 12;
		cdAccount.withdrawCash(600.54);
		assertEquals(0.00, cdAccount.getBalance());
	}

	@Test
	void deposit_cash_twice_in_account() {
		checking.depositCash(100.00);
		checking.depositCash(100.00);
		assertEquals(200.00, checking.getBalance(), DELTA);

		savings.depositCash(250.00);
		savings.depositCash(250.00);
		assertEquals(500.00, savings.getBalance(), DELTA);

		cdAccount.depositCash(50.67);
		cdAccount.depositCash(150.55);
		assertEquals(500.33, cdAccount.getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_twice_in_account() {
		checking.depositCash(100.00);
		checking.withdrawCash(50.00);
		checking.withdrawCash(50.00);
		assertEquals(0.00, checking.getBalance(), DELTA);

		savings.depositCash(250.00);
		savings.withdrawCash(100.00);
		savings.withdrawCash(100.00);
		assertEquals(150.00, savings.getBalance(), DELTA);
	}

}
