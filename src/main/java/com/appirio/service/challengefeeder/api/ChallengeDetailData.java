/**
 * 
 */
package com.appirio.service.challengefeeder.api;

import java.util.List;

import com.appirio.service.challengefeeder.v2.api.SubmissionData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Thiyagu
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChallengeDetailData extends IdentifiableData {

	private List<SubmissionData> submissions;

	private String detailedRequirements;

	private String finalSubmissionGuidelines;

	private List<DocumentData> document;

	private List<RegistrantsData> registrants;

	private List<TermsOfUseData> terms;

	private List<CheckpointsData> checkpoints;

	private String snippet;

	private String introduction;

	@JsonIgnore
	@Expose(serialize = false)
	private boolean isStudio;
}
