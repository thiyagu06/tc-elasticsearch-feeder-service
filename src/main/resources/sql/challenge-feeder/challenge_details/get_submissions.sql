SELECT
       u.project_id as challengeId,
       s.submission_id as submissionId,
       s.create_date as submittedAt,
       ssl.name as status,
       s.placement as placement,
       stl.name as submissionType,
       s.create_user as submitterId,
       usr.handle as submitter,
       s.screening_score as screeningScore,
       s.initial_score as initialScore,
       s.final_score as finalScore,
       CASE WHEN pidr.value = 'On' THEN 
       NVL((SELECT value::decimal FROM project_info pi_dr WHERE pi_dr.project_info_type_id = 30 AND pi_dr.project_id = u.project_id), (SELECT NVL(pi16.value::decimal, 1) FROM project_info pi16 WHERE pi16.project_info_type_id = 16 AND pi16.project_id = u.project_id))
       ELSE NULL END AS digitalRunPoints
   FROM
       submission s, upload u, submission_status_lu ssl, submission_type_lu stl, user usr, project_info pidr 
   WHERE
        u.upload_id = s.upload_id
        AND s.create_user = usr.user_id
        AND s.submission_status_id = ssl.submission_status_id
        AND s.submission_type_id = stl.submission_type_id
        AND s.submission_status_id <> 5
        AND s.submission_type_id in (1,3)
        AND u.upload_type_id = 1
        AND u.upload_status_id = 1 
        AND pidr.project_id = u.project_id
   		AND pidr.project_info_type_id = 26
        AND {filter}