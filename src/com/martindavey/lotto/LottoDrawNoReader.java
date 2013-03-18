package com.martindavey.lotto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LottoDrawNoReader {

	public int getDrawNo(String dateRequired) throws Exception {
		final String url = "https://www.national-lottery.co.uk/player/p/drawHistory.do";

		URL lotto;
		lotto = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(lotto.openStream()));

		final String pound = "&#163;";
		final String openingTd = "<td>";
		boolean isDateFound = false;
		boolean isPoundFound = false;
		boolean isOpeningTdFound = false;
		int drawNo = 0;

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			if (isDateFound) {
				if (isPoundFound) {
					if (isOpeningTdFound) {
						drawNo = Integer.parseInt(inputLine.trim());
						break;
					}
					else if (inputLine.contains(openingTd)) {
						isOpeningTdFound = true;
					}
				}
				else if (inputLine.contains(pound)) {
					isPoundFound = true;
				}
			}
			else if (inputLine.contains(dateRequired)) {
				isDateFound = true;
			}
		}
		in.close();
		return drawNo;
	}
}
