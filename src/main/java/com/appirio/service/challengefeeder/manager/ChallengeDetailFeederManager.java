/*
 * Copyright (C) 2017 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appirio.service.challengefeeder.dao.ChallengeDetailsFeederDAO;
import com.appirio.service.challengefeeder.dto.ChallengeFeederParam;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.FilterParameter;
import com.appirio.tech.core.api.v3.request.QueryParameter;
import com.appirio.tech.core.auth.AuthUser;

import io.searchbox.client.JestClient;

/**
 * 
 * Version 1.3 - Topcoder - Elasticsearch Service - Populate Challenge Points
 * Prize In Challenges Index - add the methods to associate the prize points for
 * the challenge ids - add the DAO calls to get prize points for the challenge
 * ids.
 * 
 * @author TCSCODER
 * @version 1.3
 */
public class ChallengeDetailFeederManager {

	/**
	 * Logger used to log events
	 */
	private static final Logger logger = LoggerFactory.getLogger(ChallengeDetailFeederManager.class);

	/**
	 * MM Round id project_info_type_id
	 */
	private static final long MM_PROPERTY_ID = 56;

	/**
	 * DAO to access challenge data from the transactional database.
	 */
	private final ChallengeDetailsFeederDAO challengeDetailsFeederDAO;

	/**
	 * The jestClient field
	 */
	private final JestClient jestClient;

	/**
	 * Create ChallengeFeederManager
	 *
	 * @param jestClient
	 *            the jestClient to use
	 * @param challengeDetailsFeederDAO
	 *            the challengeFeederDAO to use
	 */
	public ChallengeDetailFeederManager(JestClient jestClient, ChallengeDetailsFeederDAO challengeDetailsFeederDAO) {
		this.jestClient = jestClient;
		this.challengeDetailsFeederDAO = challengeDetailsFeederDAO;
	}

	/**
	 * Push challenge feeder
	 *
	 * @param authUser
	 *            the authUser to use
	 * @param param
	 *            the challenge feeders param to use
	 * @throws SupplyException
	 *             if any error occurs
	 */
	public void pushChallengeFeeder(AuthUser authUser, ChallengeFeederParam param) throws SupplyException {
		logger.info("Enter of pushChallengeFeeder");
		this.pushChallengeFeeder(param);
	}

	/**
	 * Push challenge feeder
	 *
	 * @param param
	 *            the challenge feeders param to use
	 * @throws SupplyException
	 *             if any error occurs
	 */
	public void pushChallengeFeeder(ChallengeFeederParam param) throws SupplyException {
		if (param.getType() == null || param.getType().trim().length() == 0) {
			param.setType("challenges");
		}
		if (param.getIndex() == null || param.getIndex().trim().length() == 0) {
			throw new SupplyException("The index should be non-null and non-empty string.",
					HttpServletResponse.SC_BAD_REQUEST);
		}
		if (param.getChallengeIds() == null || param.getChallengeIds().size() == 0) {
			throw new SupplyException("Challenge ids must be provided", HttpServletResponse.SC_BAD_REQUEST);
		}
		if (param.getChallengeIds().contains(null)) {
			throw new SupplyException("Null challenge id is not allowed", HttpServletResponse.SC_BAD_REQUEST);
		}

		FilterParameter filter = new FilterParameter(
				"challengeIds=in(" + ChallengeFeederUtil.listAsString(param.getChallengeIds()) + ")");
		QueryParameter queryParameter = new QueryParameter(new FieldSelector());
		queryParameter.setFilter(filter);
		List<Map<String, Object>> challenges = this.challengeDetailsFeederDAO.getChallenges(queryParameter);

		List<Long> idsNotFound = new ArrayList<>();
		for (Long id : param.getChallengeIds()) {
			boolean hit = false;
			for (Map<String, Object> data : challenges) {
				if (id.longValue() == (Long) data.get("challengeId")) {
					hit = true;
					break;
				}
			}
			if (!hit) {
				idsNotFound.add(id);
			}
		}
		if (!idsNotFound.isEmpty()) {
			logger.warn("These challenge ids can not be found:" + idsNotFound);
		}
		List<ChallengeDetailData> challengeDetails = ChallengeDetailsFeederUtil.buildChallengeDetailData(challenges);

		logger.info("aggregating challenge data for " + param.getChallengeIds());
		
		
	}

}
