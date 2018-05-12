/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.challengefeeder.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.appirio.service.challengefeeder.api.ChallengeDetailData;
import com.appirio.service.challengefeeder.api.CheckpointsData;
import com.appirio.service.challengefeeder.api.DocumentData;
import com.appirio.service.challengefeeder.api.RegistrantsData;
import com.appirio.service.challengefeeder.api.SubmissionImage;
import com.appirio.service.challengefeeder.api.TermsOfUseData;
import com.appirio.service.challengefeeder.v2.api.SubmissionData;

/**
 * ChallengeDetailsFeederUtil provides common methods such as associating the
 * challenge details data.
 * 
 * @author TCSCODER
 * @version 1.0
 */
public class ChallengeDetailsFeederUtil {

	/**
	 * private constructor to avoid object creation for static class
	 */
	private ChallengeDetailsFeederUtil() {

	}

	/**
	 * Represents Percentage of Placement Points for digital run
	 */
	private static final double[][] DR_POINT = new double[][] { { 1 }, { 0.7, 0.3 }, { 0.65, 0.25, 0.10 },
			{ 0.6, 0.22, 0.1, 0.08 }, { 0.56, 0.2, 0.1, 0.08, 0.06 } };

	/**
	 * Method to build challenge details model from DAO returned data.
	 * 
	 * @param challengeDetails
	 * @return list of challenge details
	 */
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

	/**
	 * Associate all the registrants data to challenges
	 * 
	 * @param challengeDetails
	 *            the challenges to use
	 * @param registrants
	 *            the registrants to use
	 */
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
					checkpointsData.setSubmissionTime(item.getSubmissionDate());
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

	/**
	 * The method to calculate points for submission.
	 * 
	 * @param submissionData
	 */
	private static void calculatePoints(SubmissionData submissionData) {
		int passedReview = 0;
		Integer placement = submissionData.getPlacement();
		if (placement != null) {
			passedReview++;
		}
		double[] drTable = DR_POINT[passedReview - 1 < 4 ? passedReview : 4];
		Double digitalRunPoints = submissionData.getDigitalRunPoints();
		if (placement != null && drTable.length > placement) {
			Double points = drTable[Integer.parseInt(placement.toString()) - 1] * digitalRunPoints;
			submissionData.setPoints(points);
		}

	}
	
	public static List<Long> getStudioTypeChallengeIds(List<ChallengeDetailData> challengeDetails) {
		List<Long> challengeIds = challengeDetails.stream().filter(challengeDetail -> challengeDetail.isStudio())
				.map(ChallengeDetailData::getId).collect(Collectors.toList());
		return challengeIds;
	}
	
	public static List<Long> getNonStudioTypeChallengeIds(List<ChallengeDetailData> challengeDetails) {
		List<Long> challengeIds = challengeDetails.stream().filter(challengeDetail -> !challengeDetail.isStudio())
				.map(ChallengeDetailData::getId).collect(Collectors.toList());
		return challengeIds;
	}
	
	public static void associateSubmssionImageWithChallengeSubmissions(List<ChallengeDetailData> challengeDetails, List<Long> submissionIdsWithImages,String rootDomain){
		        for(ChallengeDetailData challengeDetailData : challengeDetails){
		        	for(SubmissionData submissionData : challengeDetailData.getSubmissions()){
		        		if(submissionIdsWithImages.contains(submissionData.getSubmissionId())){
		        			submissionData.setSubmissionImage(generateSubmissionImageUrls(rootDomain,submissionData.getSubmissionId()));
		        		}
		        	}
		        }
	}


	 private static SubmissionImage generateSubmissionImageUrls(String rootDomain,Long submissionId) {
	        String templateUrl = String.format("//studio.%s/studio.jpg?module=DownloadSubmission&sbmid=%s", rootDomain.trim(), submissionId);

	        String tinyImage = String.format("%s&sbt=tiny", templateUrl);
	        String smallImage = String.format("%s&sbt=small", templateUrl);
	        String mediumImage = String.format("%s&sbt=medium", templateUrl);
	        String fullImage = String.format("%s&sbt=full", templateUrl);
	        String thumbImage = String.format("%s&sbt=thumb", templateUrl);

	        SubmissionImage submissionImage = new SubmissionImage();
	        submissionImage.setPreviewPackage(templateUrl);
	        submissionImage.setTiny(tinyImage);
	        submissionImage.setSmall(smallImage);
	        submissionImage.setMedium(mediumImage);
	        submissionImage.setThumb(thumbImage);
	        submissionImage.setFull(fullImage);

	        return submissionImage;
	    }
	@SuppressWarnings("unchecked")
	private static <T> T CastObjectToTargetType(Object source, Class<T> clazz) {
		if (source != null)
			return (T) source;
		else
			return null;
	}
}
