/**
 * 
 */
package com.appirio.service.challengefeeder.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Thiyagu
 *
 */
@Data
public class DocumentData {

	private String documentName;
	
	private String url;
	
	@JsonIgnore
	private Long challengeId;
}
