DROP TABLE attachments;
DROP TABLE status_history;
DROP TABLE complaints;
DROP TABLE categories;
DROP TABLE users;

@/tmp/001_create_users_table.sql
@/tmp/002_create_categories_table.sql
@/tmp/003_create_complaints_table.sql
@/tmp/004_trigger_complaints_updated_at.sql
@/tmp/005_create_status_history_table.sql
@/tmp/006_create_attachments_table.sql
