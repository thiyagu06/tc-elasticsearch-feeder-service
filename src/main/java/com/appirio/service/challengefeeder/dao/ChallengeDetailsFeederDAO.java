/*
 * Copyright (C) 2017 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.dao;

import java.util.List;
import java.util.Map;

import com.appirio.service.challengefeeder.v2.api.RegistrantsData;
import com.appirio.supply.dataaccess.ApiQueryInput;
import com.appirio.supply.dataaccess.DatasourceName;
import com.appirio.supply.dataaccess.SqlQueryFile;
import com.appirio.tech.core.api.v3.request.QueryParameter;

/**
 * DAO to interact with challenge details data
 *
 * Version 1.0 - Topcoder - Create CronJob For Populating Changed Challenges To
 * Elasticsearch v1.0 - add the methods to get the changed challenge ids and
 * current timestamp
 * 
 * 
 * @author TCCODER
 * @version 1.0
 */
@DatasourceName("oltp")
public interface ChallengeDetailsFeederDAO {

	/**
	 * Get challenges details
	 *
	 * @param queryParameter
	 *            the queryParameter to use
	 * @return the List<Map<String,Object> result
	 */
	@SqlQueryFile("sql/challenge-feeder/challenge_details/challenge_details.sql")
	List<Map<String, Object>> getChallenges(@ApiQueryInput QueryParameter queryParameter);
	
	
	/**
	 * Get challenge registrants data
	 *
	 * @param queryParameter
	 *            the queryParameter to use
	 * @return the List<Map<String,Object> result
	 */
	@SqlQueryFile("sql/challenge-feeder/challenge_details/challenge_registrants.sql")
	List<RegistrantsData> getChallengeRegistrants(@ApiQueryInput QueryParameter queryParameter);
}
