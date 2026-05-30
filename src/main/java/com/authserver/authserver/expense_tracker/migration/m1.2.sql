ALTER TABLE user_model DROP FOREIGN KEY FKmxf33ugxv0f2fs2djs08sh2rb;

ALTER TABLE role_access_rights DROP FOREIGN KEY FKmxbvby62acphvvtdu8lroicbn;

ALTER TABLE expense_transactions DROP FOREIGN KEY FKqt2listb6je7lay5p0t1ni4ml;

ALTER TABLE expense_labels DROP FOREIGN KEY FK3p7ydpc94k9hmxel61t9eehiu;
ALTER TABLE expense_labels DROP FOREIGN KEY FKiercapcccja2nn8xfjepc4gn8;

ALTER TABLE role_model DROP INDEX UKqh0gf17r2yjrnhqx4gldk75ru;

ALTER TABLE user_model DROP INDEX UKla8xty622mpbfdhq2iixt9lhu;
ALTER TABLE user_model DROP INDEX UKasi811mgonyf7p7aj2tl97a91;
ALTER TABLE user_model DROP INDEX FKmxf33ugxv0f2fs2djs08sh2rb;

ALTER TABLE role_access_rights DROP INDEX FKmxbvby62acphvvtdu8lroicbn;

ALTER TABLE expense_transactions DROP INDEX idx_txn_user_date;
ALTER TABLE expense_transactions DROP INDEX idx_txn_user_label_date;
ALTER TABLE expense_transactions DROP INDEX FKqt2listb6je7lay5p0t1ni4ml;

ALTER TABLE expense_labels DROP INDEX idx_label_name;
ALTER TABLE expense_labels DROP INDEX FK3p7ydpc94k9hmxel61t9eehiu;
ALTER TABLE expense_labels DROP INDEX FKiercapcccja2nn8xfjepc4gn8;

ALTER TABLE access_rights DROP INDEX UKnos1kdbm135528lrallfe9rjv;


-- primary keys

ALTER TABLE role_model ADD UNIQUE KEY uk_role_uuid (uuid);
ALTER TABLE role_model MODIFY COLUMN id BIGINT(20) NOT NULL;
ALTER TABLE role_model DROP PRIMARY KEY;
ALTER TABLE role_model ADD PRIMARY KEY (uuid);
ALTER TABLE role_model DROP COLUMN id;

ALTER TABLE user_model ADD UNIQUE KEY uk_user_uuid (uuid);
ALTER TABLE user_model MODIFY COLUMN id BIGINT(20) NOT NULL;
ALTER TABLE user_model DROP PRIMARY KEY;
ALTER TABLE user_model ADD PRIMARY KEY (uuid);
ALTER TABLE user_model DROP COLUMN id;

ALTER TABLE role_access_rights ADD UNIQUE KEY uk_role_access_rights_uuid (uuid);
ALTER TABLE role_access_rights MODIFY COLUMN id BIGINT(20) NOT NULL;
ALTER TABLE role_access_rights DROP PRIMARY KEY;
ALTER TABLE role_access_rights ADD PRIMARY KEY (uuid);
ALTER TABLE role_access_rights DROP COLUMN id;

ALTER TABLE expense_transactions ADD UNIQUE KEY uk_expense_transactions_uuid (uuid);
ALTER TABLE expense_transactions MODIFY COLUMN id BIGINT(20) NOT NULL;
ALTER TABLE expense_transactions DROP PRIMARY KEY;
ALTER TABLE expense_transactions ADD PRIMARY KEY (uuid);
ALTER TABLE expense_transactions DROP COLUMN id;

ALTER TABLE expense_labels ADD UNIQUE KEY uk_expense_labels_uuid (uuid);
ALTER TABLE expense_labels MODIFY COLUMN id BIGINT(20) NOT NULL;
ALTER TABLE expense_labels DROP PRIMARY KEY;
ALTER TABLE expense_labels ADD PRIMARY KEY (uuid);
ALTER TABLE expense_labels DROP COLUMN id;

ALTER TABLE access_rights ADD UNIQUE KEY uk_access_rights_uuid (uuid);
ALTER TABLE access_rights MODIFY COLUMN id BIGINT(20) NOT NULL;
ALTER TABLE access_rights DROP PRIMARY KEY;
ALTER TABLE access_rights ADD PRIMARY KEY (uuid);
ALTER TABLE access_rights DROP COLUMN id;
