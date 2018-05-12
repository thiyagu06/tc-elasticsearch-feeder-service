/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.api;

import java.util.Date;

import com.appirio.service.challengefeeder.helper.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
/**
 * Represents the checkpoints data model 
 * 
 * @author TCCoder
 * @version 1.0
 */
@Data
public class CheckpointsData {

	/**
	 *  The submission Id field
	 */
	private Long submissionId;

	/**
	 *  The submitter field
	 */
	private String submitter;

	/**
	 *  The submissionTime field
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date submissionTime;
}
