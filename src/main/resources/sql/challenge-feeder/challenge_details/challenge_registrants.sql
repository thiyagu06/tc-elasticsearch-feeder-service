select 
      u.handle AS handle
     , rur.create_date AS registrationDate
    , (select max(s.create_date) from submission s, upload u
                 where u.project_id = p.project_id and s.upload_id = u.upload_id       
                 and upload_status_id !=2 and s.submission_status_id != 5
                 and u.resource_id = rur.resource_id) AS submissionDate
     , decode(ri4.value, 'N/A', '0', ri4.value)::int AS rating
     , ri5.value::int AS reliability
     , p.project_id
  from resource rur
     , resource_info ri1
     , project p
     , user u
     , outer resource_info ri4
     , outer resource_info ri5
 where 
 
   p.project_id = rur.project_id
  and rur.resource_id = ri1.resource_id
  and rur.resource_role_id = 1
  and ri1.resource_info_type_id = 1
  and ri4.resource_id = rur.resource_id
  and ri4.resource_info_type_id = 4
  and ri5.resource_id = rur.resource_id
  and ri5.resource_info_type_id = 5
  and ri1.value = u.user_id
and {filter}