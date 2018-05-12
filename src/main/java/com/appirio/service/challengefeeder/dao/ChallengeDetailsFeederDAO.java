/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.skife.jdbi.v2.sqlobject.Bind;

import com.appirio.service.challengefeeder.api.DocumentData;
import com.appirio.service.challengefeeder.api.RegistrantsData;
import com.appirio.service.challengefeeder.api.TermsOfUseData;
import com.appirio.service.challengefeeder.dto.DatabaseTimestamp;
import com.appirio.service.challengefeeder.v2.api.SubmissionData;
import com.appirio.supply.dataaccess.ApiQueryInput;
import com.appirio.supply.dataaccess.DatasourceName;
import com.appirio.supply.dataaccess.SqlQueryFile;
import com.appirio.tech.core.api.v3.TCID;
import com.appirio.tech.core.api.v3.request.QueryParameter;

/**
 * DAO to interact with challenge details data
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

	/**
	 * Get challenge terms data
	 *
	 * @param queryParameter
	 *            the queryParameter to use
	 * @return the result
	 */
	@SqlQueryFile("sql/challenge-feeder/challenge_details/get_terms.sql")
	List<TermsOfUseData> getTerms(@ApiQueryInput QueryParameter queryParameter);

	/**
	 * Get challenge submissions data
	 *
	 * @param queryParameter
	 *            the queryParameter to use
	 * @return the result
	 */
	@SqlQueryFile("sql/challenge-feeder/challenge_details/get_submissions.sql")
	List<SubmissionData> getSubmissions(@ApiQueryInput QueryParameter queryParameter);

	@SqlQueryFile("sql/challenge-feeder/challenge_details/challenge_documents.sql")
	List<DocumentData> getDocumentData(@ApiQueryInput QueryParameter queryParameter);

	/**
	 * Get timestamp of the database
	 *
	 * @param queryParameter
	 *            the queryParameter to use
	 * @return the result
	 */
	@SqlQueryFile("sql/challenge-feeder/job/get_timestamp.sql")
	DatabaseTimestamp getTimestamp();

	/**
	 * Get changed challenge ids based on timestamp
	 *
	 * @param lastRunTimestamp
	 *            the lastRunTimestamp to use
	 * @return the List<TCID> result
	 */
	@SqlQueryFile("sql/challenge-feeder/job/get_changed_challenge_ids.sql")
	List<TCID> getChangedChallengeIds(@Bind("lastRunTimestamp") Date lastRunTimestamp);
	
	/**
     * Get challenge submissions for challenge details
     *
     * @param challengeId the challengeId to use
     * @param submissionType the submissionType to use
     * @return the result
     */
    @SqlQueryFile("sql/challenge-feeder/challenge_details/challenge_submissions_for_challenge_details.sql")
    List<SubmissionData> getChallengeSubmissionsForChallengeDetails(@ApiQueryInput QueryParameter queryParameter, @Bind("submissionType") long submissionType);
    
    /**
     * Get studio challenge detail submissions 
     *
     * @param challengeId the challengeId to use
     * @param submssionTypeId the submssionTypeId to use
     * @return the result
     */
    @SqlQueryFile("sql/challenge-feeder/challenge_details/get_studio_challenge_detail_submissions.sql")
    List<SubmissionData> getStudioChallengeDetailSubmissions(@ApiQueryInput QueryParameter queryParameter, @Bind("submssionTypeId") long submssionTypeId);

    @SqlQueryFile("sql/challenge-feeder/challenge_details/get-design-submission-ids.sql")
    List<Map<String, Object>> getSubmissionIdsWithImages(@ApiQueryInput QueryParameter queryParameter);

}
