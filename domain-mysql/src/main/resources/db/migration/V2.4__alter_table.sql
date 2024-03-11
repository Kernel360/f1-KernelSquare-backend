ALTER TABLE `reservation_article`
    ADD COLUMN `end_time` datetime NOT NULL DEFAULT '2199-12-31T00:00:00';

ALTER TABLE `reservation_article`
    MODIFY `end_time` datetime NOT NULL;

ALTER TABLE `reservation_article`
    ADD COLUMN  `introduction` VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL;
