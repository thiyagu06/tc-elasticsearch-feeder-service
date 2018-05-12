/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.appirio.service.challengefeeder.api.ChallengeDetailData;
import com.appirio.service.challengefeeder.api.DocumentData;
import com.appirio.service.challengefeeder.api.RegistrantsData;
import com.appirio.service.challengefeeder.api.TermsOfUseData;
import com.appirio.service.challengefeeder.dao.ChallengeDetailsFeederDAO;
import com.appirio.service.challengefeeder.dto.ChallengeFeederParam;
import com.appirio.service.challengefeeder.util.JestClientUtils;
import com.appirio.service.challengefeeder.v2.api.SubmissionData;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.TCID;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.FilterParameter;
import com.appirio.tech.core.api.v3.request.QueryParameter;
import com.appirio.tech.core.auth.AuthUser;

import io.searchbox.client.JestClient;
import lombok.Getter;
import lombok.Setter;

/**
 * The challenge Detail Feeder manager to handle challenge details feedera.
 * 
 * @author TCSCODER
 * @version 1.0
 */
public class ChallengeDetailFeederManager {

	/**
	 * Logger used to log events
	 */
	private static final Logger logger = LoggerFactory.getLogger(ChallengeDetailFeederManager.class);

	/**
	 * DAO to access challenge data from the transactional database.
	 */
	private final ChallengeDetailsFeederDAO challengeDetailsFeederDAO;

	/**
	 * The jestClient field
	 */
	private final JestClient jestClient;

	/**
	 * The root domain to be used for submission image url
	 */
	@Getter
	@Setter
	private String rootDomain;

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
	 * Push challenge details feeder
	 *
	 * @param authUser
	 *            the authUser to use
	 * @param param
	 *            the challenge feeders param to use
	 * @throws SupplyException
	 *             if any error occurs
	 */
	public void pushChallengeDetailsFeeder(AuthUser authUser, ChallengeFeederParam param) throws SupplyException {
		logger.info("Enter of pushChallengeFeeder");
		this.pushChallengeDetailsFeeder(param);
	}

	/**
	 * Push challenge details feeder
	 *
	 * @param param
	 *            the challenge feeders param to use
	 * @throws SupplyException
	 *             if any error occurs
	 */
	public void pushChallengeDetailsFeeder(ChallengeFeederParam param) throws SupplyException {
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
				if (id.longValue() == Long.parseLong(String.valueOf(data.get("challengeId")))) {
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

		logger.info("aggregating challenge details data for " + param.getChallengeIds());

		List<ChallengeDetailData> challengeDetails = ChallengeDetailsFeederUtil.buildChallengeDetailData(challenges);

		List<RegistrantsData> registrantsData = this.challengeDetailsFeederDAO.getChallengeRegistrants(queryParameter);
		ChallengeDetailsFeederUtil.associateRegistrantData(challengeDetails, registrantsData);

		List<TermsOfUseData> termsOfUseData = this.challengeDetailsFeederDAO.getTerms(queryParameter);
		ChallengeDetailsFeederUtil.associateAllTermsOfUse(challengeDetails, termsOfUseData);

		List<Long> studioTypeChallengeIds = ChallengeDetailsFeederUtil.getStudioTypeChallengeIds(challengeDetails);
		if (!CollectionUtils.isEmpty(studioTypeChallengeIds)) {
			FilterParameter studioChallengeIdFilter = new FilterParameter(
					"challengeIds=in(" + ChallengeFeederUtil.listAsString(studioTypeChallengeIds) + ")");
			QueryParameter studioChallengeIdFilterParam = new QueryParameter(new FieldSelector());
			studioChallengeIdFilterParam.setFilter(studioChallengeIdFilter);
			List<SubmissionData> submissions = this.challengeDetailsFeederDAO
					.getStudioChallengeDetailSubmissions(studioChallengeIdFilterParam, 1);
			List<SubmissionData> checkpoints = this.challengeDetailsFeederDAO
					.getStudioChallengeDetailSubmissions(studioChallengeIdFilterParam, 3);
			ChallengeDetailsFeederUtil.associateAllSubmissions(challengeDetails, submissions);
			ChallengeDetailsFeederUtil.associateAllCheckpoints(challengeDetails, checkpoints);
		}

		List<Long> nonStudioTypeChallengeIds = ChallengeDetailsFeederUtil
				.getNonStudioTypeChallengeIds(challengeDetails);
		if (!CollectionUtils.isEmpty(nonStudioTypeChallengeIds)) {
			FilterParameter nonStudioChallengeIdFilter = new FilterParameter(
					"challengeIds=in(" + ChallengeFeederUtil.listAsString(nonStudioTypeChallengeIds) + ")");
			QueryParameter nonStudioChallengeIdFilterParam = new QueryParameter(new FieldSelector());
			nonStudioChallengeIdFilterParam.setFilter(nonStudioChallengeIdFilter);
			List<SubmissionData> submissions = this.challengeDetailsFeederDAO
					.getChallengeSubmissionsForChallengeDetails(queryParameter, 1);
			List<SubmissionData> checkpoints = this.challengeDetailsFeederDAO
					.getChallengeSubmissionsForChallengeDetails(queryParameter, 3);
			ChallengeDetailsFeederUtil.associateAllSubmissions(challengeDetails, submissions);
			ChallengeDetailsFeederUtil.associateAllCheckpoints(challengeDetails, checkpoints);
		}

		List<Map<String, Object>> resultRows = challengeDetailsFeederDAO.getSubmissionIdsWithImages(queryParameter);
		// Marshalling
		List<Long> resultAsLong = new ArrayList<>();
		for (Map<String, Object> resultRow : resultRows)
			resultAsLong.add(Long.parseLong(resultRow.get("id").toString()));

		ChallengeDetailsFeederUtil.associateSubmssionImageWithChallengeSubmissions(challengeDetails, resultAsLong,
				this.rootDomain);

		List<DocumentData> documentData = this.challengeDetailsFeederDAO.getDocumentData(queryParameter);
		ChallengeDetailsFeederUtil.associateDocumentData(challengeDetails, documentData);

		logger.info("pushing challenge details data to elasticsearch for " + param.getChallengeIds());
		try {
			JestClientUtils.pushFeeders(jestClient, param, challengeDetails);
		} catch (IOException ioe) {
			SupplyException se = new SupplyException("Internal server error occurs", ioe);
			se.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			throw se;
		}

	}

	/**
	 * Get timestamp from the persistence
	 *
	 * @throws SupplyException
	 *             if any error occurs
	 * @return the Date result
	 */
	public Date getTimestamp() throws SupplyException {
		return this.challengeDetailsFeederDAO.getTimestamp().getDate();
	}

	/**
	 * Get changed challenge ids
	 *
	 * @param lastRunTimestamp
	 *            the lastRunTimestamp to use
	 * @return the List<TCID> result
	 */
	public List<TCID> getChangedChallengeIds(Date lastRunTimestamp) {
		if (lastRunTimestamp == null) {
			throw new IllegalArgumentException("The lastRunTimestamp should be non-null.");
		}
		return this.challengeDetailsFeederDAO.getChangedChallengeIds(lastRunTimestamp);
	}

}
