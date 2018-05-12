/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.v2.api;

import java.util.Date;

import com.appirio.service.challengefeeder.api.SubmissionImage;
import com.appirio.service.challengefeeder.helper.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

/**
 * The submission Data field
 * 
 * @author Thiyagu
 *
 */
@Data
public class SubmissionData {

	/**
	 * The placement field
	 */
	private Integer placement;

	/**
	 * The submission image field. applicable only for studio challenges
	 */
	private SubmissionImage submissionImage;

	/**
	 * The handle field
	 */
	private String handle;

	/**
	 * The screening score field
	 */
	private Double screeningScore;

	/**
	 * The initial score field
	 */
	private Double initialScore;

	/**
	 * The final score field
	 */
	private Double finalScore;

	/**
	 * The points field
	 */
	private Double points;

	/**
	 * The submission Status field
	 */
	private String submissionStatus;

	/**
	 * The submission Date field
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date submissionDate;

	/**
	 * The submission id field
	 */
	private Long submissionId;

	/**
	 * The submitter field
	 */
	private String submitter;

	/**
	 * The challenge id field
	 */
	@JsonIgnore
	private Long challengeId;

	/**
	 * The digital run points field used only for points calculation.
	 */
	@JsonIgnore
	private Double digitalRunPoints;

}
