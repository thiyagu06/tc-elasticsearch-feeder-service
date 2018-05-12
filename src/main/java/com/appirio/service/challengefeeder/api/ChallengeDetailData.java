/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.api;

import java.util.List;

import com.appirio.service.challengefeeder.v2.api.SubmissionData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the ChallengeDetailData model 
 * 
 * @author TCCoder
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChallengeDetailData extends IdentifiableData {

	/**
	 * The list of submissions field
	 */
	private List<SubmissionData> submissions;

	/**
	 * The detailed requirements field
	 */
	private String detailedRequirements;

	/**
	 * The final submission Guidelines field
	 */
	private String finalSubmissionGuidelines;

	/**
	 * The list of documents.
	 */
	private List<DocumentData> document;

	/**
	 * The list of registrants for the challenge
	 */
	private List<RegistrantsData> registrants;

	/**
	 * The list of terms
	 */
	private List<TermsOfUseData> terms;

	/**
	 * The list of checkpoints data
	 */
	private List<CheckpointsData> checkpoints;

	/**
	 * The snippet field
	 */
	private String snippet;

	/**
	 * The introduction field
	 */
	private String introduction;

	/**
	 * The field to differentiate studio and dev challenges. Should not written
	 * to ES or json response.
	 */
	@JsonIgnore
	@Expose(serialize = false)
	private boolean isStudio;
}
