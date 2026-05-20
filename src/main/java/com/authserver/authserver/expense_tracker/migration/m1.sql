START TRANSACTION;

ALTER TABLE expense_labels
ADD COLUMN sub_category_count INT NOT NULL DEFAULT 0;

UPDATE expense_labels el
SET sub_category_count = (
    SELECT COUNT(*)
    FROM expense_labels esc
    WHERE esc.parent_id = el.id
);

COMMIT;
