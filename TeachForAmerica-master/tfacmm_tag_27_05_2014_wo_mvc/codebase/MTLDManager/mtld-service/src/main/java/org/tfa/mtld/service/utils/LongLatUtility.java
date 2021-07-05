package org.tfa.mtld.service.utils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tfa.mtld.service.bean.Address;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class LongLatUtility {
	private static Logger logger = Logger.getLogger(LongLatUtility.class);

	private static final String GEOCODE_REQUEST_URL = "https://maps.googleapis.com/maps/api/geocode/xml?sensor=false&";
	private static HttpClient httpClient = new HttpClient(
			new MultiThreadedHttpConnectionManager());

	public static void getLongitudeLatitude(Address address) {
		try {
			StringBuilder strAddress = new StringBuilder();
			strAddress.append(address.getAddress().trim());
			if (null != address.getCity()) {
				strAddress.append(",");
				strAddress.append(address.getCity());
			}

			if (null != address.getState()) {
				strAddress.append(",");
				strAddress.append(address.getState());
			}

			if (null != address.getZipCode()) {
				strAddress.append(",");
				strAddress.append(address.getZipCode());
			}

			StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
			if (strAddress != null && !"".equals(strAddress)) {
				urlBuilder.append("&address=").append(
						URLEncoder.encode(strAddress.toString(), "UTF-8"));
			}
			final GetMethod getMethod = new GetMethod(urlBuilder.toString());
			try {
				httpClient.executeMethod(getMethod);
				Reader reader = new InputStreamReader(
						getMethod.getResponseBodyAsStream(),
						getMethod.getResponseCharSet());

				int data = reader.read();
				char[] buffer = new char[1024];
				Writer writer = new StringWriter();
				while ((data = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, data);
				}

				String result = writer.toString();

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader("<"
						+ writer.toString().trim()));
				Document doc = db.parse(is);

				String strLatitude = getXpathValue(doc,
						"//GeocodeResponse/result/geometry/location/lat/text()");
				logger.debug("Latitude:" + strLatitude);

				String strLongtitude = getXpathValue(doc,
						"//GeocodeResponse/result/geometry/location/lng/text()");
				logger.debug("Longitude:" + strLongtitude);

				if (StringUtils.isNotBlank(strLatitude)
						&& StringUtils.isNotBlank(strLongtitude)) {
					address.setLatitude(Double.valueOf(strLatitude));
					address.setLongitude(Double.valueOf(strLongtitude));
				}

			} finally {
				getMethod.releaseConnection();
			}
		} catch (Exception e) {
			logger.error("Exceptin in Lat and Long" , e);
		}
	}

	private static String getXpathValue(Document doc, String strXpath)
			throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(strXpath);
		String resultData = null;
		Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result4;
		for (int i = 0; i < nodes.getLength(); i++) {
			resultData = nodes.item(i).getNodeValue();
		}
		return resultData;
	}
}
