/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import lombok.Data;

/**
 * Represents the document Data model
 * 
 * @author TCCoder
 * @version 1.0
 */
@Data
public class DocumentData {

	/**
	 * The document Name
	 */
	private String documentName;

	/**
	 * The document URL
	 */
	private String url;

	/**
	 * The challenge id field. Should not written to ES and json response.
	 */
	@JsonIgnore
	@Expose(serialize = false)
	private Long challengeId;
}
