package com.appirio.service.challengefeeder.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.appirio.service.challengefeeder.v2.api.ChallengeDetailData;

public class ChallengeDetailsFeederUtil {

	private ChallengeDetailsFeederUtil() {

	}

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
			System.out.println("challengeDetailID->" + challengeDetailData.getId());
		}

		return challengeDetailsData;

	}

	@SuppressWarnings("unchecked")
	private static <T> T CastObjectToTargetType(Object source, Class<T> clazz) {
		if (source != null)
			return (T) source;
		else
			return null;
	}
}
