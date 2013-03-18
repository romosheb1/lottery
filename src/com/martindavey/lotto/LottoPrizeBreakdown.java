package com.martindavey.lotto;

public class LottoPrizeBreakdown {

	private int drawNo;

	private int claimsForJackPot;
	private int claimsFor5AndBonus;
	private int claimsForMatch5;
	private int claimsForMatch4;
	private int claimsForMatch3;

	private int prizeForJackPot;
	private int prizeFor5AndBonus;
	private int prizeForMatch5;
	private int prizeForMatch4;
	private int prizeForMatch3;

	private boolean isValidated = false;

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public int getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(int drawNo) {
		this.drawNo = drawNo;
	}

	public int getClaimsForJackPot() {
		return claimsForJackPot;
	}

	public void setClaimsForJackPot(int claimsForJackPot) {
		this.claimsForJackPot = claimsForJackPot;
	}

	public int getClaimsFor5AndBonus() {
		return claimsFor5AndBonus;
	}

	public void setClaimsFor5AndBonus(int claimsFor5AndBonus) {
		this.claimsFor5AndBonus = claimsFor5AndBonus;
	}

	public int getClaimsForMatch5() {
		return claimsForMatch5;
	}

	public void setClaimsForMatch5(int claimsForMatch5) {
		this.claimsForMatch5 = claimsForMatch5;
	}

	public int getClaimsForMatch4() {
		return claimsForMatch4;
	}

	public void setClaimsForMatch4(int claimsForMatch4) {
		this.claimsForMatch4 = claimsForMatch4;
	}

	public int getClaimsForMatch3() {
		return claimsForMatch3;
	}

	public void setClaimsForMatch3(int claimsForMatch3) {
		this.claimsForMatch3 = claimsForMatch3;
	}

	public int getPrizeForJackPot() {
		return prizeForJackPot;
	}

	public void setPrizeForJackPot(int prizeForJackPot) {
		this.prizeForJackPot = prizeForJackPot;
	}

	public int getPrizeFor5AndBonus() {
		return prizeFor5AndBonus;
	}

	public void setPrizeFor5AndBonus(int prizeFor5AndBonus) {
		this.prizeFor5AndBonus = prizeFor5AndBonus;
	}

	public int getPrizeForMatch5() {
		return prizeForMatch5;
	}

	public void setPrizeForMatch5(int prizeForMatch5) {
		this.prizeForMatch5 = prizeForMatch5;
	}

	public int getPrizeForMatch4() {
		return prizeForMatch4;
	}

	public void setPrizeForMatch4(int prizeForMatch4) {
		this.prizeForMatch4 = prizeForMatch4;
	}

	public int getPrizeForMatch3() {
		return prizeForMatch3;
	}

	public void setPrizeForMatch3(int prizeForMatch3) {
		this.prizeForMatch3 = prizeForMatch3;
	}

	public int getTotalClaims() {
		return this.claimsForJackPot + this.claimsFor5AndBonus + this.claimsForMatch5 + this.claimsForMatch4 + this.claimsForMatch3;
	}
}
