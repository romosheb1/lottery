package com.martindavey.lotto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

public class Member {

	private int id;
	private boolean isActive;
	private int defaultShare;
	private String name;
	private String emailAddress;
	private final HashMap<Integer, Winnings> winnings;
	private final List<Withdrawals> withdrawals;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getDefaultShare() {
		return defaultShare;
	}

	public void setDefaultShare(int defaultShare) {
		this.defaultShare = defaultShare;
	}

	public HashMap<Integer, Winnings> getWinnings() {
		return winnings;
	}

	public List<Withdrawals> getWithdrawals() {
		return withdrawals;
	}

	public BigDecimal getWinningsToDate() {
		BigDecimal winningsToDate = new BigDecimal("0.00");
		BigDecimal share = new BigDecimal("0.00");

		for (Integer drawNo : winnings.keySet()) {
			Winnings winning = winnings.get(drawNo);
			Integer noOfShares = winning.getMemberShares().get(this.id);
			if (noOfShares != null) {
				share = winning.getAmount().divide(new BigDecimal(winning.getTotalLines()), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(noOfShares));
				share = share.setScale(2, RoundingMode.HALF_UP);
				winningsToDate = winningsToDate.add(share);
			}
		}
		winningsToDate = winningsToDate.setScale(2, RoundingMode.HALF_UP);
		return winningsToDate;
	}

	public BigDecimal getWithdrawalsToDate() {
		BigDecimal withdrawalsToDate = new BigDecimal("0.00");
		for (Withdrawals withdrawal : withdrawals) {
			if (withdrawal.getMemberId() == this.id) {
				withdrawalsToDate = withdrawalsToDate.add(withdrawal.getAmount());
			}
		}
		withdrawalsToDate = withdrawalsToDate.setScale(2, RoundingMode.HALF_UP);
		return withdrawalsToDate;
	}

	public Member(int id, String name, String emailAddress, 
			HashMap<Integer, Winnings> winnings, List<Withdrawals> withdrawals, int defaultShare, boolean isActive) {
		this.id = id;
		this.name = name;
		this.emailAddress = emailAddress;
		this.winnings = winnings;
		this.withdrawals = withdrawals;
		this.defaultShare = defaultShare;
		this.isActive = isActive;
	}

}
