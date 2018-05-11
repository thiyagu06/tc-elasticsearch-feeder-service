/**
 * 
 */
package com.appirio.service.challengefeeder.v2.api;

import java.util.List;

import com.appirio.service.challengefeeder.api.IdentifiableData;

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

	private DocumentData document;

	private List<RegistrantsData> registrants;

	private List<TermsData> terms;

	private List<CheckpointsData> checkpoints;

	private String snippet;
	
	private String introduction;
	
	private transient boolean isStudio;
}
