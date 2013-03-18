package com.martindavey.lotto;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Utils {

	public static Document readXml(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		db.setEntityResolver(new NullResolver());

		return db.parse(is);
	}

	/**
	 * Takes a list of String arrays where the expected format is:
	 * 
	 * Header1,Header2,HeaderN Line1Col1,Line1Col2,Line1Col3
	 * Line2Col1,Line2Col2,Line2Col3 Line1ColN,LineNCol2,LineNCol3
	 * 
	 * Returns HashMap with Column header as key to String array of lines, eg:
	 * "DrawDate", {"06-Feb-2013", "02-Feb-2013"} "Ball 1", {9, 2}
	 * 
	 * @param csvData
	 * @return
	 */
	public static HashMap<String, List<String>> parseCsvArray(List<String[]> csvData) throws Exception {
		HashMap<String, List<String>> csvMap = new HashMap<String, List<String>>();

		List<String> headings = new ArrayList<String>();
		boolean isHeader = true;
		for (String[] data : csvData) {
			if (isHeader) {
				isHeader = false;
				for (String heading : data) {
					headings.add(heading);
					csvMap.put(heading, new ArrayList<String>());
				}
			}
			else {
				if (data.length != headings.size()) {
					throw new Exception("Number of data items differs to number of column headings");
				}
				else {
					for (int index = 0; index < data.length; index++) {
						List<String> columnData = csvMap.get(headings.get(index));
						columnData.add(data[index]);
					}
				}
			}
		}

		return csvMap;
	}

	public static int deriveMonthNumber(String monthName) {
		final String months = "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC";
		monthName = monthName.substring(0, 3).toUpperCase();
		return (months.indexOf(monthName) + 4) / 4;
	}
}

class NullResolver implements EntityResolver {
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		return new InputSource(new StringReader(""));
	}
}