package com.martindavey.lotto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class OurNumbers {

	public static void main (String... args) {
		//byte[] numbers = new byte[] {17, 21, 25, 27, 30, 37};
		//System.out.println(OurNumbers.getResultsMessage(numbers, null, null));
	}
	
	private static final List<byte[]> lines = new ArrayList<byte[]>();

	static {
		lines.add(new byte[] { 03, 05, 15, 29, 42, 49 });
		lines.add(new byte[] { 03, 05, 15, 37, 42, 44 });
		lines.add(new byte[] { 03, 05, 29, 37, 40, 44 });
		lines.add(new byte[] { 03, 15, 21, 29, 37, 42 });
		lines.add(new byte[] { 03, 15, 21, 37, 40, 44 });
		lines.add(new byte[] { 03, 15, 29, 37, 42, 44 });
		lines.add(new byte[] { 03, 21, 29, 37, 40, 42 });
		lines.add(new byte[] { 05, 15, 21, 37, 40, 49 });
		lines.add(new byte[] { 05, 15, 29, 37, 42, 49 });
		lines.add(new byte[] { 05, 21, 29, 42, 44, 49 });
		lines.add(new byte[] { 15, 21, 29, 37, 42, 49 });
		lines.add(new byte[] { 15, 21, 37, 40, 44, 49 });
	}

	public static String getResultsMessage(byte[] drawLine, LottoPrizeBreakdown prizeBreakdown, byte bonusBall) {
		StringBuilder message = new StringBuilder(128);
		for (byte[] line : lines) {
			StringBuilder str = new StringBuilder(32);
			int matchedNumbers = 0;
			for (byte number : line) {
				if (isNumberInDraw(number, drawLine)) {
					str.append(String.format("(%2d)", number));
					matchedNumbers++;
				}
				else {
					str.append(String.format(" %2d ", number));
				}
			}
			BigDecimal won = new BigDecimal("0.00");
			switch (matchedNumbers) {
			case 6: won = new BigDecimal(prizeBreakdown.getPrizeForJackPot()); break;
			case 5: 
				if (isBonusMatched(bonusBall)) {
					won = new BigDecimal(prizeBreakdown.getPrizeFor5AndBonus());
				}
				else {
					won = new BigDecimal(prizeBreakdown.getPrizeForMatch5());
				}
				break;
			case 4: won = new BigDecimal(prizeBreakdown.getPrizeForMatch4()); break;
			case 3: won = new BigDecimal(prizeBreakdown.getPrizeForMatch3()); break;
			}
			message.append(String.format("%s = %s\n", str, 
					matchedNumbers >= 3 ? String.format("x%d (£%.02f)", matchedNumbers, won) : "no win"));
		}
		return message.toString();
	}

	public static BigDecimal getWinningsForDraw(byte[] drawLine, LottoPrizeBreakdown prizeBreakdown, byte bonusBall) {
		BigDecimal won = new BigDecimal("0.00");
		for (byte[] line : lines) {
			int matchedNumbers = 0;
			for (byte number : line) {
				if (isNumberInDraw(number, drawLine)) {
					matchedNumbers++;
				}
			}
			switch (matchedNumbers) {
			case 6: won = won.add(new BigDecimal(prizeBreakdown.getPrizeForJackPot())); break;
			case 5: 
				if (isBonusMatched(bonusBall)) {
					won = won.add(new BigDecimal(prizeBreakdown.getPrizeFor5AndBonus()));
				}
				else {
					won = won.add(new BigDecimal(prizeBreakdown.getPrizeForMatch5()));
				}
				break;
			case 4: won = won.add(new BigDecimal(prizeBreakdown.getPrizeForMatch4())); break;
			case 3: won = won.add(new BigDecimal(prizeBreakdown.getPrizeForMatch3())); break;
			}
		}
		won = won.setScale(2, RoundingMode.HALF_UP);
		return won;
	}

	private static boolean isBonusMatched(byte bonusBall) {
		return false;
	}

	private static boolean isNumberInDraw(byte number, byte[] drawLine) {
		for (byte drawNumber : drawLine) {
			if (number == drawNumber) return true;
		}
		return false;
	}
}
