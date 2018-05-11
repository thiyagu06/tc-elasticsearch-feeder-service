package com.appirio.service.challengefeeder.v2.api;

import lombok.Data;

@Data
public class TermsData {

	private Long termsOfUseId;
	
	private String title;
	
	private String url;
	
	private String agreeabilityType;
	
	private Long templateId;
	
	private String role;
	
	private String agreed;
}
