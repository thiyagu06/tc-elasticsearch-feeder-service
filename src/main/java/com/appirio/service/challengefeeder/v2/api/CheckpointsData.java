package com.appirio.service.challengefeeder.v2.api;

import java.util.Date;

import com.appirio.service.challengefeeder.helper.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class CheckpointsData {

	private Long submissionId;

	private String submitter;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date submissionTime;
}
