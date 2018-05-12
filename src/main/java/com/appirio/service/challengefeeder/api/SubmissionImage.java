/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.api;

import lombok.Data;

/**
 * Represents the SubmissionImage data model
 * 
 * @author TCCoder
 * @version 1.0
 */
@Data
public class SubmissionImage {

	/**
	 * The tiny url of the submission image
	 */
	private String tiny;

	/**
	 * The small url of the submission image
	 */
	private String small;

	/**
	 * The medium url of the submission image
	 */
	private String medium;

	/**
	 * The full url of the submission image
	 */
	private String full;

	/**
	 * The thumb url of the submission image
	 */
	private String thumb;

	/**
	 * The preview url of the submission image
	 */
	private String previewPackage;
}
