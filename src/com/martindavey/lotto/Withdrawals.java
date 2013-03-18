package com.martindavey.lotto;

import java.math.BigDecimal;

public class Withdrawals {

	String dateYYYYMMDD;
	int memberId;
	BigDecimal amount;

	public Withdrawals(String dateYYYYMMDD, int memberId, BigDecimal amount) {
		this.dateYYYYMMDD = dateYYYYMMDD;
		this.memberId = memberId;
		this.amount = amount;
	}

	public String getDateYYYYMMDD() {
		return dateYYYYMMDD;
	}

	public void setDateYYYYMMDD(String dateYYYYMMDD) {
		this.dateYYYYMMDD = dateYYYYMMDD;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}