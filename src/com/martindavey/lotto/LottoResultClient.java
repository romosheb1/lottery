package com.martindavey.lotto;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LottoResultClient {
	public static void main(String[] args) throws Exception {

		String SOAPUrl = "http://www.sansburycc.co.uk/services/uk_lotto_results.asmx";
		String SOAPAction = "http://sansburycc.co.uk/services/uk_lotto_results/LottoResult";

		URL url = new URL(SOAPUrl);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;

		StringBuilder sbMessage = new StringBuilder(500);
		sbMessage.append("<?xml version='1.0' encoding='utf-8'?>\r\n");
		sbMessage.append("<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\r\n");
		sbMessage.append("  xmlns:xsd='http://www.w3.org/2001/XMLSchema'\r\n");
		sbMessage.append("  xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\r\n");
		sbMessage.append("  <soap:Body>\r\n");
		sbMessage.append("    <LottoResult xmlns='http://sansburycc.co.uk/services/uk_lotto_results' />\r\n");
		sbMessage.append("  </soap:Body>\r\n");
		sbMessage.append("</soap:Envelope>");
		byte[] messageByteArray = sbMessage.toString().getBytes();

		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length", String.valueOf(messageByteArray.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		OutputStream out = httpConn.getOutputStream();
		out.write(messageByteArray);
		out.close();

		Document document = Utils.readXml(httpConn.getInputStream());

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd-MMM-yyyy");
		NodeList nodes = document.getElementsByTagName("DrawDate");
		String drawDate = nodes.item(0).getTextContent();
		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(drawDate.substring(0, 4));
		int month = Integer.parseInt(drawDate.substring(5, 7));
		int day = Integer.parseInt(drawDate.substring(8, 10));
		cal.set(year, month - 1, day);
		System.out.printf("Date: %s\n\nDraw:    ", dateFormat.format(cal.getTime()));

		nodes = document.getElementsByTagName("LottoNumbers");
		Node lottoNumbers = nodes.item(0);
		byte[] numbers = MyBase64.decode(lottoNumbers.getTextContent());
		for (byte number : numbers) {
			System.out.printf("  %2d  ", number);
		}
		//System.out.println("\n-----------------------------------------------------------------");
		//System.out.println(OurNumbers.getResultsMessage(numbers));
	}
}