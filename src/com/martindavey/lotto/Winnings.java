package com.martindavey.lotto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class Winnings {

	int drawNo;
	String drawDateYYYYMMDD;
	BigDecimal amount;
	int totalLines;
	HashMap<Integer, Integer> memberShares;

	public Winnings(int drawNo, String drawDateYYYYMMDD, BigDecimal amount, int totalLines, HashMap<Integer, Integer> memberShares) {
		this.drawNo = drawNo;
		this.drawDateYYYYMMDD = drawDateYYYYMMDD;
		this.amount = amount;
		this.amount = this.amount.setScale(2, RoundingMode.HALF_UP);
		this.totalLines = totalLines;
		this.memberShares = memberShares;
	}

	public int getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(int drawNo) {
		this.drawNo = drawNo;
	}

	public String getDrawDateYYYYMMDD() {
		return drawDateYYYYMMDD;
	}

	public void setDrawDateYYYYMMDD(String drawDateYYYYMMDD) {
		this.drawDateYYYYMMDD = drawDateYYYYMMDD;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}

	public HashMap<Integer, Integer> getMemberShares() {
		return memberShares;
	}

	public void setMemberShares(HashMap<Integer, Integer> memberShares) {
		this.memberShares = memberShares;
	}

}
