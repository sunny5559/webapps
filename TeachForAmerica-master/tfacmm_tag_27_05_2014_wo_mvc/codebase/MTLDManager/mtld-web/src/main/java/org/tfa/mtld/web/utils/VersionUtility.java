/**
 * -----------------------------------------------------------------------------
 * Company: Teach for America
 * -----------------------------------------------------------------------------
 *
 * This file contains trade secrets of Teach for America, No part may
 * be reproduced or transmitted in any form by any means or for any purpose
 * without the express written permission of Teach for America, Inc.
 *
 * Copyright: (C) 2014
 */
package org.tfa.mtld.web.utils;

/**
 * @author arun.rathore
 */

public class VersionUtility {

	private static String version;

	public String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		VersionUtility.version = version;
	}

}