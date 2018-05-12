/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.api;

import java.util.Date;

import com.appirio.service.challengefeeder.helper.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.Expose;

import lombok.Data;

/**
 * Represents the RegistrantsData data model
 * 
 * @author TCCoder
 * @version 1.0
 */
@Data
public class RegistrantsData {

	/**
	 * The challenge id field. Should not written to ES and json response.
	 */
	@JsonIgnore
	@Expose(serialize=false)
	private Long challengeId;

	/**
	 *  The submission date field
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date submissionDate;

	/**
	 *  The reliability field
	 */
	private int reliability;

	/**
	 *  The color style field
	 */
	private String colorStyle;

	/**
	 * The handle field
	 */
	private String handle;

	/**
	 * The rating field
	 */
	private Integer rating;

	/**
	 *  The registration field
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date registrationDate;
}
