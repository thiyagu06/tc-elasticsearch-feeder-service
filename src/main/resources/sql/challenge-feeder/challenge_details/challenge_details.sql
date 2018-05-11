SELECT 
       pcl.description AS challengeType
     , pn.value AS challengeName
     , p.project_id AS challengeId
     , p.tc_direct_project_id AS projectId
     , sps.contest_description_text AS studioDetailedRequirements
     , sps.contest_introduction AS introduction
     , sps.round_one_introduction
     , sps.round_two_introduction
     , ps.detailed_requirements_text AS softwareDetailedRequirements
     ,CASE WHEN (p.project_studio_spec_id is NULL) THEN 0 ELSE 1 END as is_studio
  FROM project p
   ,project_studio_specification sps
   ,project_category_lu pcl     
 WHERE 1=1
   AND p.project_id = :challengeId
   AND p.project_category_id = pcl.project_category_id
   AND {filter}
   AND ps.project_id = p.project_id
   AND sps.project_studio_spec_id = p.project_studio_spec_id
   AND p.project_category_id = pcl.project_category_id
   AND pcl.project_type_id in in(1, 2, 3)
