ALTER TABLE `answer`
    MODIFY `image_url` VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `answer_rank`
    MODIFY `image_url` VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `level`
    MODIFY `image_url` VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `member`
    MODIFY `image_url` VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `question`
    MODIFY `image_url` VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL;