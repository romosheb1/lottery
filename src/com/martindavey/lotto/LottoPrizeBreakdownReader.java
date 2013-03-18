package com.martindavey.lotto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LottoPrizeBreakdownReader {

	public LottoPrizeBreakdown getPrizeBreakdown(int drawNoRequired) throws Exception {
		LottoPrizeBreakdown prizeBreakdown = new LottoPrizeBreakdown();

		final String url = "https://www.national-lottery.co.uk/player/lotto/results/prizeBreakdown.ftl?drawNumber="
				+ drawNoRequired + "&drawSequence=0";

		URL lotto;
		lotto = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(lotto.openStream()));

		final String drawNo = "Draw " + drawNoRequired;
		String match = "Match 6";
		final String strong = "<strong>";
		final String pound = "&#163;";
		final String totals = "Totals";

		boolean isDrawFound = false;
		boolean isMatchFound = false;
		boolean isClaimsFound = false;
		boolean isPrizeFound = false;
		boolean isTotalsFound = false;

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			if (isDrawFound) {
				if (isMatchFound) {
					if (isClaimsFound) {
						if (isPrizeFound) {
							if (match.equals("Match 3")) {
								if (isTotalsFound) {
									if (inputLine.contains(strong)) {
										String[] tmp = inputLine.trim().split(strong);
										String totalClaims = tmp[1].substring(0, tmp[1].indexOf("<")).trim().replaceAll(",", "");
										if (Integer.parseInt(totalClaims) == prizeBreakdown.getTotalClaims()) {
											prizeBreakdown.setValidated(true);
										}
										break;
									}
								}
								else if (inputLine.contains(totals)) {
									isTotalsFound = true;
								}
								continue;
							}
							else if (match.equals("Match 4")) {
								match = "Match 3";
							}
							else if (match.equals("Match 5")) {
								match = "Match 4";
							}
							else if (match.equals("Match 5 plus Bonus")) {
								match = "Match 5";
							}
							else if (match.equals("Match 6")) {
								match = "Match 5 plus Bonus";
							}
							else {
								throw new Exception("Unknown search string: [" + match + "]");
							}
							isMatchFound = false;
							isClaimsFound = false;
							isPrizeFound = false;
						}
						else if (inputLine.contains(pound)) {
							String[] tmp = inputLine.trim().split(pound);
							String prize = tmp[1].substring(0, tmp[1].indexOf("<")).trim().replaceAll(",", "");
							if (match.equals("Match 6")) {
								prizeBreakdown.setPrizeForJackPot(Integer.parseInt(prize));
							}
							else if (match.equals("Match 5 plus Bonus")) {
								prizeBreakdown.setPrizeFor5AndBonus(Integer.parseInt(prize));
							}
							else if (match.equals("Match 5")) {
								prizeBreakdown.setPrizeForMatch5(Integer.parseInt(prize));
							}
							else if (match.equals("Match 4")) {
								prizeBreakdown.setPrizeForMatch4(Integer.parseInt(prize));
							}
							else if (match.equals("Match 3")) {
								prizeBreakdown.setPrizeForMatch3(Integer.parseInt(prize));
							}
							else {
								throw new Exception("Unknown search string: [" + match + "]");
							}
							isPrizeFound = true;
						}
					}
					else if (inputLine.contains(strong)) {
						String[] tmp = inputLine.trim().split(strong);
						String claims = tmp[1].substring(0, tmp[1].indexOf("<")).trim().replaceAll(",", "");
						if (match.equals("Match 6")) {
							prizeBreakdown.setClaimsForJackPot(Integer.parseInt(claims));
						}
						else if (match.equals("Match 5 plus Bonus")) {
							prizeBreakdown.setClaimsFor5AndBonus(Integer.parseInt(claims));
						}
						else if (match.equals("Match 5")) {
							prizeBreakdown.setClaimsForMatch5(Integer.parseInt(claims));
						}
						else if (match.equals("Match 4")) {
							prizeBreakdown.setClaimsForMatch4(Integer.parseInt(claims));
						}
						else if (match.equals("Match 3")) {
							prizeBreakdown.setClaimsForMatch3(Integer.parseInt(claims));
						}
						else {
							throw new Exception("Unknown search string: [" + match + "]");
						}
						isClaimsFound = true;
					}
				}
				else if (inputLine.contains(match)) {
					isMatchFound = true;
				}
			}
			else if (inputLine.contains(drawNo)) {
				isDrawFound = true;
			}
		}
		in.close();
		return prizeBreakdown;
	}
}
