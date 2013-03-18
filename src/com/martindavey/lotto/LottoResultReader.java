package com.martindavey.lotto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class LottoResultReader {

	private static String url = "https://www.national-lottery.co.uk/player/lotto/results/downloadResultsCSV.ftl";

	// (Gets results for past 180 days)
	// DrawDate,Ball 1,Ball 2,Ball 3,Ball 4,Ball 5,Ball 6,Bonus Ball,Ball
	// Set,Machine
	// 06-Feb-2013,9,18,10,16,27,42,43,6,ARTHUR
	// 02-Feb-2013,2,13,14,21,12,30,39,7,ARTHUR
	// 30-Jan-2013,10,9,43,48,47,24,5,5,ARTHUR
	// ...
	// 11-Aug-2012,18,1,5,44,14,34,23,3,GUINEVERE

	public static void main(String[] args) throws Exception {

		URL lotto = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(lotto.openStream()));

		List<String[]> csvData = new ArrayList<String[]>();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			String[] fields = inputLine.split(",");
			csvData.add(fields);
		}
		in.close();

		HashMap<String, List<String>> csvMap = Utils.parseCsvArray(csvData);

		// Get latest Saturday draws
		List<String> drawDates = csvMap.get("DrawDate");
		List<String> ball1s = csvMap.get("Ball 1");
		List<String> ball2s = csvMap.get("Ball 2");
		List<String> ball3s = csvMap.get("Ball 3");
		List<String> ball4s = csvMap.get("Ball 4");
		List<String> ball5s = csvMap.get("Ball 5");
		List<String> ball6s = csvMap.get("Ball 6");
		List<String> bonusBalls = csvMap.get("Bonus Ball");

		StringBuilder results = new StringBuilder(1024);

		String lastDateChecked = "20130309"; // TODO Derive from file

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(lastDateChecked.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(lastDateChecked.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(lastDateChecked.substring(6, 8)));
		cal.add(Calendar.DATE, 7);

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yy");
		String dateRequired = dateFormat.format(cal.getTime());

		LottoPrizeBreakdown prizeBreakdown = null;
		int drawNo = 0;
		try {
			LottoDrawNoReader drawNoReader = new LottoDrawNoReader();
			drawNo = drawNoReader.getDrawNo(dateRequired);
			System.out.printf("Draw No: %d (%s)\n", drawNo, dateRequired);

			LottoPrizeBreakdownReader prizeBreakdownReader = new LottoPrizeBreakdownReader();
			prizeBreakdown = prizeBreakdownReader.getPrizeBreakdown(drawNo);
			System.out.printf("Prize Breakdown (%s)\n", prizeBreakdown.isValidated() ? "Passed Validation" : "Failed Validation");
			System.out.printf("Jackpot Winners: %7d (£%8d)\n", prizeBreakdown.getClaimsForJackPot(), prizeBreakdown.getPrizeForJackPot());
			System.out.printf("5+Bonus Winners: %7d (£%8d)\n", prizeBreakdown.getClaimsFor5AndBonus(), prizeBreakdown.getPrizeFor5AndBonus());
			System.out.printf("Match 5 Winners: %7d (£%8d)\n", prizeBreakdown.getClaimsForMatch5(), prizeBreakdown.getPrizeForMatch5());
			System.out.printf("Match 4 Winners: %7d (£%8d)\n", prizeBreakdown.getClaimsForMatch4(), prizeBreakdown.getPrizeForMatch4());
			System.out.printf("Match 3 Winners: %7d (£%8d)\n", prizeBreakdown.getClaimsForMatch3(), prizeBreakdown.getPrizeForMatch3());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		byte bonusBall = 0;
		byte[] numbers = null;
		for (int index = 0; index < drawDates.size(); index++) {
			String drawDate = drawDates.get(index);
			cal.set(Calendar.YEAR, Integer.parseInt(drawDate.substring(7, 11)));
			cal.set(Calendar.MONTH, Utils.deriveMonthNumber(drawDate.substring(3, 6)) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(drawDate.substring(0, 2)));
			drawDate = dateFormat.format(cal.getTime());

			if (drawDate.equals(dateRequired)) {

				numbers = new byte[] { 
						Byte.parseByte(ball1s.get(index)), 
						Byte.parseByte(ball2s.get(index)), 
						Byte.parseByte(ball3s.get(index)),
						Byte.parseByte(ball4s.get(index)), 
						Byte.parseByte(ball5s.get(index)), 
						Byte.parseByte(ball6s.get(index)), };

				for (byte number : numbers) {
					results.append(String.format(" %2d ", number));
				}

				bonusBall = Byte.parseByte(bonusBalls.get(index));
				results.append(String.format(" Bonus: %d\n", bonusBall));
				results.append("---------------------------------------\n");
				results.append(OurNumbers.getResultsMessage(numbers, prizeBreakdown, bonusBall)).append("\n");
				results.append("---------------------------------------\n");
				break;
			}
		}
		System.out.println(results);

		DataSource dataSource = new DataSource();
		List<Member> members = dataSource.readMembers(dataSource.getWinnings(), dataSource.getWithdrawals());

		if (prizeBreakdown != null && prizeBreakdown.isValidated()) {
			BigDecimal totalWonThisDraw = OurNumbers.getWinningsForDraw(numbers, prizeBreakdown, bonusBall);
			int totalShares = 0;
			for (Member member : members) {
				if (member.isActive()) {
					totalShares += member.getDefaultShare();
				}
			}
			for (Member member : members) {
				if (member.isActive()) {
					sendEmail(member, drawNo, dateRequired, results.toString(), totalWonThisDraw, totalShares);
				}
			}
		}
		else {
			sendErrorEmail(dateRequired);
		}
	}

	private static void sendErrorEmail(String dateRequired) {
		String[] recipients = { "martin.davey@clearchannel.co.uk" };
		String[] bcc = {};
		String subject = String.format("Lottery Results for Date: '%s'. FAILED VALIDATION...", dateRequired);

		StringBuilder htmlMessage = new StringBuilder(1024); 
		htmlMessage.append("<div style='font-size:0.75em;'>");
		htmlMessage.append(String.format("The lottery results for <b>%s</b> FAILED TO VALIDATE!", dateRequired));
		htmlMessage.append("</div>");

		StringBuilder message = new StringBuilder(1024);
		message.append(String.format("The lottery results for <b>%s</b> FAILED TO VALIDATE!", dateRequired));

		MailUtil mailUtil = new MailUtil();
		mailUtil.sendMail(recipients, bcc, subject, message.toString(), htmlMessage.toString());
	}

	private static void sendEmail(Member member, int drawNo, String dateRequired, 
			String results, BigDecimal totalWonThisDraw, int totalShares) {
		String[] recipients = { member.getEmailAddress() };
		String[] bcc = { "martin.davey@clearchannel.co.uk" };
		String subject = String.format("Lottery Results for Draw %d on %s...", drawNo, dateRequired);

		BigDecimal yourShare = totalWonThisDraw.divide(
				new BigDecimal(totalShares), 6, RoundingMode.HALF_UP).multiply(
						new BigDecimal(member.getDefaultShare()));
		yourShare = yourShare.setScale(2, RoundingMode.HALF_UP);

		BigDecimal newBalance = yourShare.add(
				member.getWinningsToDate()).subtract(
						member.getWithdrawalsToDate());
		newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);

		StringBuilder htmlMessage = new StringBuilder(1024); 
		htmlMessage.append("<div style='font-size:0.75em;'>");
		htmlMessage.append(String.format("Hi %s,<br/>", member.getName()));
		htmlMessage.append(String.format("Here are the lottery results for draw <b>%d</b> which took place on <b>%s</b>:<br/><br/>", drawNo, dateRequired));
		htmlMessage.append("<div style='font-family:courier;'/><pre>");
		htmlMessage.append(results.replaceAll("\n", "<br/>"));
		htmlMessage.append("<br/>");
		htmlMessage.append(String.format("Winning this week = £%.02f<br/><br/>", totalWonThisDraw));
		htmlMessage.append(String.format("Your share            = £%8.02f<br/>", yourShare));
		htmlMessage.append(String.format("Plus winnings to date = £%8.02f<br/>", member.getWinningsToDate()));
		htmlMessage.append(String.format("Less withdrawals      = £%8.02f<br/>", member.getWithdrawalsToDate()));
		htmlMessage.append(String.format("Your new balance      = £%8.02f<br/><br/>", newBalance));
		htmlMessage.append("</pre></div>");
		htmlMessage.append("Best wishes,<br/>");
		htmlMessage.append("Martin.");
		htmlMessage.append("</div>");

		StringBuilder message = new StringBuilder(1024);
		message.append(String.format("Hi %s,\n", member.getName()));
		message.append(String.format("Here are the lottery results for draw %d on %s:\n\n", drawNo, dateRequired));
		message.append(results);
		message.append("\n");
		message.append(String.format("Total winnings this draw = £%8.02f\n\n", totalWonThisDraw));
		message.append(String.format("Your share for this draw = £%8.02f\n", yourShare));
		message.append(String.format("Plus winnings to date    = £%8.02f\n", member.getWinningsToDate()));
		message.append(String.format("Less withdrawals to date = £%8.02f\n", member.getWithdrawalsToDate()));
		message.append(String.format("Your new balance         = £%8.02f\n\n", newBalance));
		message.append("Best wishes,\n");
		message.append("Martin.");

		MailUtil mailUtil = new MailUtil();
		mailUtil.sendMail(recipients, bcc, subject, message.toString(), htmlMessage.toString());
	}
}
