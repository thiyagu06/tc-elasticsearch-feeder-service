package com.appirio.service.challengefeeder.v2.api;

import java.util.Date;

import com.appirio.service.challengefeeder.helper.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class RegistrantsData {

	private Long challengeId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date submissionDate;
	
	private int reliability;
	
	private String colorStyle;
	
	private String handle;
	
	private Integer rating;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date registrationDate;
}
