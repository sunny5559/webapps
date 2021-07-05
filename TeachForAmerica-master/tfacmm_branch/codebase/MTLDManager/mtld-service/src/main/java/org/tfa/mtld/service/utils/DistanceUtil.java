package org.tfa.mtld.service.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 11:47 AM To
 * change this template use File | Settings | File Templates.
 */
public class DistanceUtil {

	/**
	 * Calculate the distance between two points on Earth in miles.
	 * 
	 * @param lat1
	 *            Latitude of the first point, in the format ddd.ddddX, where X
	 *            is N or S
	 * @param lon1
	 *            Longitude of the first point, in the format ddd.ddddX, where X
	 *            is E or W
	 * @param lat2
	 *            Latitude of the second point, in the format ddd.ddddX, where X
	 *            is N or S
	 * @param lon2
	 *            Longitude of the second point, in the format ddd.ddddX, where
	 *            X is E or W
	 * @return Distance in miles between the two points.
	 */
/*	public static double calculateDistance(String lat1, String lon1,
			String lat2, String lon2) {
		GlobalCoordinates position1 = new GlobalCoordinates(getLatDouble(lat1),getLonDouble(lon1));
		GlobalCoordinates position2 = new GlobalCoordinates(getLatDouble(lat2),getLonDouble(lon2));
		Ellipsoid reference = Ellipsoid.Sphere;
		GeodeticCalculator calc = new GeodeticCalculator();
		return convertMetersToMiles(calc.calculateGeodeticCurve(reference,
				position1, position2).getEllipsoidalDistance());
	}*/


	protected static Double getLatDouble(String lat) {
		if (lat == null) {
			return null;
		}
		String rv = lat.trim();
		char lastChar = rv.charAt(rv.length() - 1);
		if (lastChar == 'N') {
			rv = rv.substring(0, rv.length() - 1);
		} else if (lastChar == 'S') {
			rv = "-" + rv.substring(0, rv.length() - 1);
		} else {
			throw new IllegalArgumentException(
					"Format should be ddd.ddddX where X is 'N' or 'S'");
		}
		Double rvDouble = null;
		try {
			rvDouble = new Double(rv);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Format should be ddd.ddddX where X is 'N' or 'S'");
		}
		return rvDouble;
	}

	protected static Double getLonDouble(String lon) {
		if (lon == null) {
			return null;
		}
		String rv = lon.trim();
		char lastChar = rv.charAt(rv.length() - 1);
		if (lastChar == 'E') {
			rv = rv.substring(0, rv.length() - 1);
		} else if (lastChar == 'W') {
			rv = "-" + rv.substring(0, rv.length() - 1);
		} else {
			throw new IllegalArgumentException(
					"Format should be ddd.ddddX where X is 'E' or 'W'");
		}
		Double rvDouble = null;
		try {
			rvDouble = new Double(rv);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Format should be ddd.ddddX where X is 'E' or 'W'");
		}
		return rvDouble;
	}

	public static double convertMetersToMiles(double meters) {
		return meters * 0.00062137119;
	}

	public static double calculateDistance(Double lat1, Double lon1,
			Double lat2, Double lon2) {
		GlobalCoordinates position1 = new GlobalCoordinates(lat1, lon1);
		GlobalCoordinates position2 = new GlobalCoordinates(lat2, lon2);
		Ellipsoid reference = Ellipsoid.Sphere;
		GeodeticCalculator calc = new GeodeticCalculator();
		return convertMetersToMiles(calc.calculateGeodeticCurve(reference,
				position1, position2).getEllipsoidalDistance());
	}
}
