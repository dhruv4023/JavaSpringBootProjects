START TRANSACTION;

-- phase 1



-- access_rights --
ALTER TABLE access_rights ADD COLUMN uuid BINARY(16);
UPDATE access_rights  SET uuid = UNHEX(REPLACE(UUID(), '-', ''));

-- update access_right_uuid in role_access_rights
ALTER TABLE role_access_rights ADD COLUMN access_right_uuid BINARY(16);
UPDATE role_access_rights rar JOIN access_rights ar on rar.access_rights_id = ar.id set access_right_uuid = ar.uuid;







-- role_model --
ALTER TABLE role_model ADD COLUMN uuid BINARY(16);
UPDATE role_model SET uuid = UNHEX(REPLACE(UUID(), '-', ''));

-- update role_uuid in user_model
ALTER TABLE user_model ADD COLUMN role_uuid BINARY(16);
UPDATE user_model ur JOIN role_model rm ON ur.role_id = rm.id SET ur.role_uuid = rm.uuid;


-- update role_uuid in role_access_rights
ALTER TABLE role_access_rights ADD COLUMN role_uuid BINARY(16);
UPDATE role_access_rights rar JOIN role_model rm on rar.role_id = rm.id SET rar.role_uuid = rm.uuid;






-- user_model --
ALTER TABLE user_model ADD COLUMN uuid BINARY(16);
UPDATE user_model SET uuid = UNHEX(REPLACE(UUID(), '-', ''));

-- update user_id in label
ALTER TABLE expense_labels ADD COLUMN user_uuid BINARY(16);
UPDATE expense_labels el JOIN user_model um on el.user_id = um.id set user_uuid = um.uuid;


-- update user_id in transaction transaction
ALTER TABLE expense_transactions ADD COLUMN user_uuid BINARY(16);
UPDATE expense_transactions et JOIN user_model um on et.user_id = um.id set user_uuid = um.uuid;







-- label --
ALTER TABLE expense_labels ADD COLUMN uuid BINARY(16);
UPDATE expense_labels SET uuid = UNHEX(REPLACE(UUID(), '-', ''));



-- update parent_uuid in label
ALTER TABLE expense_labels ADD COLUMN parent_uuid BINARY(16);
UPDATE expense_labels el JOIN expense_labels el2 on el.parent_id = el2.id set el.parent_uuid = el2.uuid;



-- update label_id in transaction transaction --
ALTER TABLE expense_transactions ADD COLUMN label_uuid BINARY(16);
UPDATE expense_transactions et JOIN expense_labels el on et.label_id = el.id set label_uuid = el.uuid;











-- transaction --
ALTER TABLE expense_transactions ADD COLUMN uuid BINARY(16);
UPDATE expense_transactions SET uuid = UNHEX(REPLACE(UUID(), '-', ''));


COMMIT;
