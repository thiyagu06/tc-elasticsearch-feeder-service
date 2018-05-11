package com.appirio.service.challengefeeder.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.appirio.service.challengefeeder.api.ChallengeDetailData;
import com.appirio.service.challengefeeder.api.CheckpointsData;
import com.appirio.service.challengefeeder.api.DocumentData;
import com.appirio.service.challengefeeder.api.RegistrantsData;
import com.appirio.service.challengefeeder.api.TermsOfUseData;
import com.appirio.service.challengefeeder.v2.api.SubmissionData;

public class ChallengeDetailsFeederUtil {

	private ChallengeDetailsFeederUtil() {

	}

	/**
	 * Represents Percentage of Placement Points for digital run
	 */
	private static final double[][] DR_POINT = new double[][] { { 1 }, { 0.7, 0.3 }, { 0.65, 0.25, 0.10 },
			{ 0.6, 0.22, 0.1, 0.08 }, { 0.56, 0.2, 0.1, 0.08, 0.06 } };

	public static List<ChallengeDetailData> buildChallengeDetailData(List<Map<String, Object>> challengeDetails) {
		List<ChallengeDetailData> challengeDetailsData = new ArrayList<>();
		for (Map<String, Object> challengeDetail : challengeDetails) {
			ChallengeDetailData challengeDetailData = new ChallengeDetailData();
			boolean isStudio = "1".equals(challengeDetail.get("is_studio").toString());
			challengeDetailData.setStudio(isStudio);
			challengeDetailData.setId(Long.parseLong(String.valueOf(challengeDetail.get("challengeId"))));
			challengeDetailData
					.setIntroduction(CastObjectToTargetType(challengeDetail.get("introduction"), String.class));
			String studioDetailedRequirements = challengeDetail.get("studioDetailedRequirements") == null ? null
					: new String((byte[]) challengeDetail.get("studioDetailedRequirements"));
			String softwareDetailedRequirements = challengeDetail.get("softwareDetailedRequirements") == null ? null
					: new String((byte[]) challengeDetail.get("softwareDetailedRequirements"));
			StringBuilder builder = new StringBuilder();
			StringBuilder detailedRequirements = isStudio ? builder.append(studioDetailedRequirements)
					: builder.append(softwareDetailedRequirements);
			String round1Introduction = CastObjectToTargetType(challengeDetail.get("round1Introduction"), String.class);
			String round2Introduction = CastObjectToTargetType(challengeDetail.get("round2Introduction"), String.class);
			if (StringUtils.isNotEmpty(round1Introduction)) {
				detailedRequirements.append(round1Introduction);
			}
			if (StringUtils.isNotEmpty(round2Introduction)) {
				detailedRequirements.append(round2Introduction);
			}
			challengeDetailData.setDetailedRequirements(detailedRequirements.toString());
			String finalSubmissionGuidelines = challengeDetail.get("finalSubmissionGuidelines") == null ? null
					: new String((byte[]) challengeDetail.get("finalSubmissionGuidelines"));
			challengeDetailData.setFinalSubmissionGuidelines(finalSubmissionGuidelines);
			challengeDetailsData.add(challengeDetailData);
		}

		return challengeDetailsData;

	}

	public static void associateRegistrantData(List<ChallengeDetailData> challengeDetails,
			List<RegistrantsData> registrants) {
		for (RegistrantsData registrant : registrants) {
			for (ChallengeDetailData challengeDetailData : challengeDetails) {
				if (registrant.getChallengeId().equals(challengeDetailData.getId())) {
					if (CollectionUtils.isEmpty(challengeDetailData.getRegistrants())) {
						challengeDetailData.setRegistrants(new ArrayList<>());
					}
					challengeDetailData.getRegistrants().add(registrant);
					break;
				}
			}
		}
		for (RegistrantsData registrant : registrants) {
			registrant.setChallengeId(null);
		}
	}

	/**
	 * Associate all terms of use
	 *
	 * @param challenges
	 *            the challenges to use
	 * @param termsOfUse
	 *            the termsOfUse to use
	 */
	public static void associateAllTermsOfUse(List<ChallengeDetailData> challenges, List<TermsOfUseData> termsOfUse) {
		for (TermsOfUseData item : termsOfUse) {
			for (ChallengeDetailData challenge : challenges) {
				if (challenge.getId().equals(item.getChallengeId())) {
					if (challenge.getTerms() == null) {
						challenge.setTerms(new ArrayList<TermsOfUseData>());
					}
					challenge.getTerms().add(item);
					break;
				}
			}
		}
		for (TermsOfUseData item : termsOfUse) {
			item.setChallengeId(null);
		}
	}

	/**
	 * Associate all submissions
	 *
	 * @param challenges
	 *            the challenges to use
	 * @param submissions
	 *            the submissions to use
	 */
	public static void associateAllSubmissions(List<ChallengeDetailData> challenges, List<SubmissionData> submissions) {
		for (SubmissionData item : submissions) {
			for (ChallengeDetailData challenge : challenges) {
				if (challenge.getId().equals(item.getChallengeId())) {
					if (challenge.getSubmissions() == null) {
						challenge.setSubmissions((new ArrayList<SubmissionData>()));
					}
					calculatePoints(item);
					challenge.getSubmissions().add(item);
					break;
				}
			}
		}
		for (SubmissionData item : submissions) {
			item.setChallengeId(null);
		}
	}
	
	/**
	 * Associate all submissions
	 *
	 * @param challenges
	 *            the challenges to use
	 * @param submissions
	 *            the submissions to use
	 */
	public static void associateAllCheckpoints(List<ChallengeDetailData> challenges, List<SubmissionData> submissions) {
		for (SubmissionData item : submissions) {
			for (ChallengeDetailData challenge : challenges) {
				if (challenge.getId().equals(item.getChallengeId())) {
					if (challenge.getCheckpoints() == null) {
						challenge.setCheckpoints((new ArrayList<CheckpointsData>()));
					}
					CheckpointsData checkpointsData = new CheckpointsData();
					checkpointsData.setSubmissionId(item.getSubmissionId());
					checkpointsData.setSubmissionTime(item.getSubmissionTime());
					checkpointsData.setSubmitter(item.getSubmitter());
					challenge.getCheckpoints().add(checkpointsData);
					break;
				}
			}
		}
		for (SubmissionData item : submissions) {
			item.setChallengeId(null);
		}
	}
	
	
	
	/**
	 * Associate all submissions
	 *
	 * @param challenges
	 *            the challenges to use
	 * @param submissions
	 *            the submissions to use
	 */
	public static void associateDocumentData(List<ChallengeDetailData> challenges, List<DocumentData> documents) {
		for (DocumentData item : documents) {
			for (ChallengeDetailData challenge : challenges) {
				if (challenge.getId().equals(item.getChallengeId())) {
					if (challenge.getDocument() == null) {
						challenge.setDocument((new ArrayList<DocumentData>()));
					}
					challenge.getDocument().add(item);
					break;
				}
			}
		}
		for (DocumentData item : documents) {
			item.setChallengeId(null);
		}
	}

	private static void calculatePoints(SubmissionData item) {
		int passedReview = 0;
		Integer placement = item.getPlacement();
		if (placement != null) {
			passedReview++;
		}
		double[] drTable = DR_POINT[passedReview - 1 < 4 ? passedReview : 4];
		Double digitalRunPoints = item.getDigitalRunPoints();
		if (placement != null && drTable.length > placement) {
			Double points = drTable[Integer.parseInt(placement.toString()) - 1] * digitalRunPoints;
			item.setPoints(points);
		}

	}

	@SuppressWarnings("unchecked")
	private static <T> T CastObjectToTargetType(Object source, Class<T> clazz) {
		if (source != null)
			return (T) source;
		else
			return null;
	}
}
