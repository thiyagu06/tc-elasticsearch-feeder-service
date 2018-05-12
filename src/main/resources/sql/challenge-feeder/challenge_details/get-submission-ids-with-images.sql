select
s.submission_id id,
from submission s, upload u, project_info pi, submission_status_lu ssl, resource r
where
u.upload_id = s.upload_id
and pi.project_id = u.project_id
and u.resource_id = r.resource_id
and s.submission_status_id = ssl.submission_status_id
and s.submission_status_id <> 5
and s.submission_type_id in (1,3)
and u.upload_type_id = 1
and u.upload_status_id = 1
and pi.project_info_type_id = 53
and pi.value = 'true'
and r.resource_role_id = 1
and {filter}