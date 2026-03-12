INSERT INTO role_access_rights (role_id, access_rights_id) SELECT r.id, a.id from role_model r join access_rights a where a.route like '/code%' and r.role_name = 'USER';
