ALTER TABLE `question`
    MODIFY `title` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL;

ALTER TABLE `member`
    MODIFY `introduction` VARCHAR(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `reservation_article`
    MODIFY `title` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL;