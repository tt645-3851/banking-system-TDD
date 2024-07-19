package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CDAccountTest {

	private static final double DELTA = 1e-2;
	Account CD;

	@BeforeEach
	void setup() {
		CD = new CDAccount("76543210", 4.8, 500.33);
	}

	@Test
	void CD_account_created_with_specified_balance_initially() {
		assertEquals(500.33, CD.getBalance(), DELTA);
	}

	@Test
	void CD_account_created_with_given_APR() {
		assertEquals(4.8, CD.getAPR());
	}

	@Test
	void deposit_cash_in_CD_account() {
		CD.depositCash(100.11);
		double actual = CD.getBalance();
		assertEquals(500.33, actual, DELTA);
	}

	@Test
	void withdraw_cash_in_CD_account() {
		CD.months = 12;
		CD.withdrawCash(500.33);
		double actual = CD.getBalance();
		assertEquals(0, actual, DELTA);
	}

	@Test
	void withdraw_more_than_balance_decreases_to_zero() {
		CD.months = 12;
		CD.withdrawCash(600.00);
		double actual = CD.getBalance();
		assertEquals(0.00, actual);
	}

	@Test
	void deposit_twice_in_CD_account() {
		CD.depositCash(50.67);
		CD.depositCash(150.55);
		double actual = CD.getBalance();
		assertEquals(500.33, actual, DELTA);
	}

	@Test
	void withdraw_twice_in_CD_account_but_not_in_full_amount() {
		CD.months = 12;
		CD.withdrawCash(100.33);
		CD.withdrawCash(300.22);
		double actual = CD.getBalance();
		assertEquals(500.33, actual, DELTA);
	}

	@Test
	void withdraw_with_max_balance() {
		CD.months = 12;
		CD.withdrawCash(500.33);
		double actual = CD.getBalance();
		assertEquals(0, actual);
	}
}
