SELECT
  document_name as documentName
, document_id as documentId
FROM comp_documentation cd
, project_info pi
WHERE cd.comp_vers_id = pi.value
AND pi.project_id = :challengeId
AND pi.project_info_type_id = 1
AND (cd.document_type_id in (0, 24) 
    or cd.document_type_id in (select 25 from project pr where pr.project_id = pi.project_id and pr.project_category_id = 1)
    or cd.document_type_id in (select 26 from project pr where pr.project_id = pi.project_id and pr.project_category_id = 2))
ORDER BY document_id ASC