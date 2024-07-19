package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SavingsAccountTest {

	private static final double DELTA = 1e-2;
	Account savings;

	@BeforeEach
	void setup() {
		savings = new SavingsAccount("01234567", 5.5);
	}

	@Test
	void savings_account_created_with_no_balance_initially() {
		assertEquals(0.00, savings.getBalance());
	}

	@Test
	void savings_account_created_with_specified_apr() {
		assertEquals(5.5, savings.getAPR());
	}

	@Test
	void deposit_cash_in_savings_account() {
		savings.depositCash(250.00);
		assertEquals(250.00, savings.getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_in_savings_account() {
		savings.depositCash(250.00);
		savings.withdrawCash(100.00);
		double actual = savings.getBalance();
		assertEquals(150.00, actual, DELTA);
	}

	@Test
	void withdraw_more_cash_than_balance_decreases_to_zero() {
		savings.depositCash(250.00);
		savings.withdrawCash(300.00);
		double actual = savings.getBalance();
		assertEquals(0.00, actual);
	}

	@Test
	void deposit_cash_twice_in_savings_account() {
		savings.depositCash(250.00);
		savings.depositCash(250.00);
		double actual = savings.getBalance();
		assertEquals(500.00, actual, DELTA);
	}

	@Test
	void withdraw_cash_twice_in_savings_account_but_only_withdraws_once() {
		savings.depositCash(250.00);
		savings.withdrawCash(100.00);
		savings.withdrawCash(100.00);
		double actual = savings.getBalance();
		assertEquals(150.00, actual, DELTA);
	}
}
