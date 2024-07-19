package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckingAccountTest {

	private static final double DELTA = 1e-2;
	Account checking;

	@BeforeEach
	void setup() {
		checking = new CheckingAccount("12345678", 0.8);
	}

	@Test
	void create_checking_account_with_id() {
		assertEquals("12345678", checking.getID());
	}

	@Test
	void checking_account_created_with_no_balance_initially() {
		assertEquals(0.00, checking.getBalance());
	}

	@Test
	void checking_account_created_with_given_APR() {
		assertEquals(0.8, checking.getAPR());
	}

	@Test
	void deposit_cash_in_checking_account() {
		checking.depositCash(100.00);
		assertEquals(100.00, checking.getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_in_checking_account() {
		checking.depositCash(100.00);
		checking.withdrawCash(50.00);
		assertEquals(50.00, checking.getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_more_than_balance_decreases_to_zero() {
		checking.depositCash(100.00);
		checking.withdrawCash(200.00);
		assertEquals(0.00, checking.getBalance());
	}

	@Test
	void deposit_cash_twice_in_checking_account() {
		checking.depositCash(100.00);
		checking.depositCash(50.00);
		assertEquals(150.00, checking.getBalance(), DELTA);
	}

	@Test
	void withdraw_cash_twice_in_savings_account() {
		checking.depositCash(250.13);
		checking.withdrawCash(50.00);
		checking.withdrawCash(50.00);
		assertEquals(150.13, checking.getBalance(), DELTA);
	}
}
