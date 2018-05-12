select
       (select ri.value from resource_info ri where  ri.resource_id = u.resource_id and resource_info_type_id = 2) AS submitter, 
       s.submission_id AS submissionId,
       s.create_date as submissionTime
from upload u,
     submission s
where u.project_id = :challengeId
  and s.upload_id = u.upload_id
  and s.submission_status_id in (1,4)
  and s.submission_type_id = :submssionTypeId
order by s.placement desc