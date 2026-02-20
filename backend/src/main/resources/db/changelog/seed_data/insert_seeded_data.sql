-- liquibase formatted sql
-- changeset seededData:user runOnChange:True; logicalFilePath:path-independent
SET FOREIGN_KEY_CHECKS = 0; 
TRUNCATE table user;
INSERT INTO `user`(`id`,`first_name`,`last_name`) VALUES (1,'Admin','User');
SET FOREIGN_KEY_CHECKS = 1;