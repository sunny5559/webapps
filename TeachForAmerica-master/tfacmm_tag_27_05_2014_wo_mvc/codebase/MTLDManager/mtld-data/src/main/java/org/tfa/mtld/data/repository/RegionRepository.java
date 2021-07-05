package org.tfa.mtld.data.repository;

import org.tfa.mtld.data.model.Region;

/**
 * @author divesh.solanki
 *
 */
public interface RegionRepository {

	public Region getRegionByRegionCode(String regionCode) throws Exception;
}
