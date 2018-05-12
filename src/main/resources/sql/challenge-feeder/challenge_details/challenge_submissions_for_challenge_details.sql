SELECT u.handle AS submitter
  , u.project_id AS challengeId,
  , substat.name AS submissionStatus
  , sub.placement AS placement
  , sub.screening_score AS screeningScore
  , sub.initial_score AS initialScore
  , sub.final_score AS finalScore
  , sub.create_date AS submissionTime
  , sub.submission_type_id AS submissionTypeId
  , sub.submission_id AS submissionId,
  CASE WHEN pidr.value = 'On' THEN 
       NVL((SELECT value::decimal FROM project_info pi_dr WHERE pi_dr.project_info_type_id = 30 AND pi_dr.project_id = u.project_id), (SELECT NVL(pi16.value::decimal, 1) FROM project_info pi16 WHERE pi16.project_info_type_id = 16 AND pi16.project_id = u.project_id))
       ELSE NULL END AS digitalRunPoints

FROM submission sub
  INNER JOIN upload u1 ON sub.upload_id = u1.upload_id 
  INNER JOIN user u ON sub.create_user = u.user_id
  INNER JOIN submission_status_lu substat ON sub.submission_status_id = substat.submission_status_id
  INNERR JOIN ON pidr.project_id = u1.project_id
WHERE 1=1 
 AND sub.submission_type_id = :submissionType
 AND sub.submission_status_id !=5
 AND pidr.project_info_type_id = 26
 AND {filter}
ORDER BY CASE WHEN sub.placement IS NULL THEN 1 Else 0 End, sub.placement