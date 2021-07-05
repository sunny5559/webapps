package org.tfa.mtld.service.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 11:49 AM To
 * change this template use File | Settings | File Templates.
 */
public class DistanceUtilTest {

	@Test
	public void testCalculateDistance() {
		String lat1 = "45.4132N";
		String lon1 = "45.1234W";
		String lat2 = "45.6511N";
		String lon2 = "45.6213W";
		assertEquals(29.2026,
				DistanceUtil.calculateDistance(DistanceUtil.getLatDouble(lat1), DistanceUtil.getLonDouble(lon1), DistanceUtil.getLatDouble(lat2), DistanceUtil.getLonDouble(lon2)), 0.1);
		assertEquals(29.2026,
				DistanceUtil.calculateDistance(DistanceUtil.getLatDouble(lat2), DistanceUtil.getLonDouble(lon2), DistanceUtil.getLatDouble(lat1), DistanceUtil.getLonDouble(lon1)), 0.1);
	}

	@Test
	public void testGetLatDouble() {
		assertEquals(45.1341, DistanceUtil.getLatDouble("45.1341N"), 0.0);
		assertEquals(-45.1341, DistanceUtil.getLatDouble("45.1341S"), 0.0);
		assertNull(DistanceUtil.getLatDouble(null));
		try {
			DistanceUtil.getLatDouble("45.1231W");
			fail("Should have gotten IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Should have gotten IllegalArgumentException");
		}
		try {
			DistanceUtil.getLatDouble("45.1231");
			fail("Should have gotten IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Should have gotten IllegalArgumentException");
		}
	}

	@Test
	public void testGetLonDouble() {
		assertEquals(45.1341, DistanceUtil.getLonDouble("45.1341E"), 0.0);
		assertEquals(-45.1341, DistanceUtil.getLonDouble("45.1341W"), 0.0);
		assertNull(DistanceUtil.getLonDouble(null));
		try {
			DistanceUtil.getLonDouble("45.1231");
			fail("Should have gotten IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Should have gotten IllegalArgumentException");
		}
		try {
			DistanceUtil.getLonDouble("45.1231S");
			fail("Should have gotten IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Should have gotten IllegalArgumentException");
		}
	}
}
