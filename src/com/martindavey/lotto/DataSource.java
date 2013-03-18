package com.martindavey.lotto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataSource {

	/**
	 * Reads the member csv file and returns in a HashMap: 
	 * Example: 1,Martin Davey,martinjohndavey@gmail.com,true
	 * 
	 * @param winnings
	 * @return
	 */
	public List<Member> readMembers(HashMap<Integer, Winnings> winnings, List<Withdrawals> withdrawals) {
		List<Member> members = new ArrayList<Member>();

		URL url = DataSource.class.getClassLoader().getResource("files/members.csv");
		String fileName = url.getPath();

		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] fields = line.split(",");
				members.add(new Member(
						Integer.parseInt(fields[0]), 
						fields[1].trim(), 
						fields[2].trim(), 
						winnings,
						withdrawals,
						Integer.parseInt(fields[3].trim()),
						fields[4].trim().toUpperCase().equals("TRUE") ? true : false));
			}

			bufferedReader.close();
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return members;
	}

	/**
	 * Reads the winnings csv file and returns in a HashMap:
	 * Example: 1691, 20130105, 150.00, 12, 1^2|2^2|
	 * @return
	 */
	public HashMap<Integer, Winnings> getWinnings() {
		HashMap<Integer, Winnings> winnings = new HashMap<Integer, Winnings>();
		URL url = DataSource.class.getClassLoader().getResource("files/winnings.csv");
		String fileName = url.getPath();
		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] fields = line.split(",");

				int drawNo = Integer.parseInt(fields[0]);

				HashMap<Integer, Integer> memberShares = new HashMap<Integer,Integer>();
				String[] memberSharesRows = fields[4].split("\\|");
				for (String memberSharesRow : memberSharesRows) {
					String[] memberSharesCols = memberSharesRow.split("\\^");
					memberShares.put(Integer.parseInt(memberSharesCols[0].trim()), Integer.parseInt(memberSharesCols[1].trim()));
				}

				winnings.put(drawNo, new Winnings(
						drawNo, 
						fields[1].trim(), 
						new BigDecimal(fields[2].trim()), 
						Integer.parseInt(fields[3].trim()), 
						memberShares));
			}

			bufferedReader.close();
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return winnings;
	}

	/**
	 * Reads the withdrawals csv file and returns in a List:
	 * Example: 20010922, 10, 14.40
	 * @return
	 */
	public List<Withdrawals> getWithdrawals() {
		List<Withdrawals> withdrawals = new ArrayList<Withdrawals>();
		URL url = DataSource.class.getClassLoader().getResource("files/withdrawals.csv");
		String fileName = url.getPath();
		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] fields = line.split(",");

				withdrawals.add(new Withdrawals(
					fields[0].trim(),
					Integer.parseInt(fields[1].trim()),
					new BigDecimal(fields[2].trim())));
			}

			bufferedReader.close();
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return withdrawals;
	}
}